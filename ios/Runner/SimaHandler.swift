import Foundation
import Flutter
import UIKit
import CryptoKit

@objc class SimaHandler: NSObject {
    private var methodChannel: FlutterMethodChannel?
    private var pendingResult: FlutterResult?
    private var pendingOperation: String?
    private var pendingChallenge: Data?
    
    // SIMA Configuration
    private let SIMA_SCHEME = "sima"
    private let SIMA_URL = "https://apps.apple.com/us/app/si-ma-beta/id1602500636"
    private let SIGN_PDF_OPERATION = "sign-pdf"
    private let SIGN_CHALLENGE_OPERATION = "sign-challenge"
    private let CLIENT_MASTER_KEY = "9569936E-9A4D-4AAC-B286-E6242CD08FA0"
    private let CLIENT_ID = 1082001
    private let SERVICE_NAME = "Bank BTB (App2App) (TEST)"
    private let RETURN_SCHEME = "btbmobile"
    
    // URL parameter field names
    private let EXTRA_RETURN_SCHEME_FIELD = "scheme"
    private let EXTRA_DOCUMENT_FIELD = "document"
    private let EXTRA_DOCUMENT_NAME_FIELD = "document-name"
    private let EXTRA_CHALLENGE_FIELD = "challenge"
    private let EXTRA_SIGNATURE_FIELD = "signature"
    private let EXTRA_SERVICE_FIELD = "service-name"
    private let EXTRA_LOGO_FIELD = "service-logo"
    private let EXTRA_USER_CODE_FIELD = "user-code"
    private let EXTRA_CLIENT_ID_FIELD = "client-id"
    private let EXTRA_REQUEST_ID_FIELD = "request-id"
    
    func setupMethodChannel(binaryMessenger: FlutterBinaryMessenger) {
        methodChannel = FlutterMethodChannel(
            name: "az.btb.btb_mobile_flutter/sima",
            binaryMessenger: binaryMessenger
        )
        
        methodChannel?.setMethodCallHandler { [weak self] (call: FlutterMethodCall, result: @escaping FlutterResult) in
            guard let self = self else { return }
            
            switch call.method {
            case "signPdf":
                self.handleSignPdf(call: call, result: result)
            case "signChallenge":
                self.handleSignChallenge(call: call, result: result)
            case "isSimaInstalled":
                self.handleIsSimaInstalled(result: result)
            case "openPlayStore":
                self.handleOpenAppStore(result: result)
            default:
                result(FlutterMethodNotImplemented)
            }
        }
    }
    
    // MARK: - Method Handlers
    
    private func handleSignPdf(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any],
              let documentPath = args["documentPath"] as? String,
              let userCode = args["userCode"] as? String else {
            result(FlutterError(code: "invalid_argument", message: "Missing required parameters", details: nil))
            return
        }
        
        let clientId = args["clientId"] as? Int ?? CLIENT_ID
        let serviceName = args["serviceName"] as? String ?? SERVICE_NAME
        let signature = args["signature"] as? String ?? ""
        let requestId = args["requestId"] as? String ?? UUID().uuidString
        let logo = args["logo"] as? String ?? ""
        
        let documentUrl = URL(string: documentPath) ?? URL(fileURLWithPath: documentPath)
        guard let documentData = try? Data(contentsOf: documentUrl) else {
            result(FlutterError(code: "file_error", message: "Failed to read document", details: nil))
            return
        }
        
        // Use signature from Flutter (already calculated)
        // If signature is empty, calculate it here as fallback
        let signatureData: Data
        if !signature.isEmpty, let sigData = Data(base64Encoded: signature) {
            signatureData = sigData
        } else {
            // Calculate SHA-256 hash
            let hash = SHA256.hash(data: documentData)
            let hashData = Data(hash)
            
            // Calculate HMAC-SHA256 signature
            guard let calculatedSig = calculateHMAC(data: hashData, key: CLIENT_MASTER_KEY) else {
                result(FlutterError(code: "signature_error", message: "Failed to calculate signature", details: nil))
                return
            }
            signatureData = calculatedSig
        }
        
        // Build URL
        var components = URLComponents()
        components.scheme = SIMA_SCHEME
        components.host = SIGN_PDF_OPERATION
        components.path = ""
        components.queryItems = [
            URLQueryItem(name: EXTRA_RETURN_SCHEME_FIELD, value: RETURN_SCHEME),
            URLQueryItem(name: EXTRA_DOCUMENT_FIELD, value: documentData.base64EncodedString()),
            URLQueryItem(name: EXTRA_DOCUMENT_NAME_FIELD, value: documentUrl.lastPathComponent),
            URLQueryItem(name: EXTRA_SERVICE_FIELD, value: serviceName),
            URLQueryItem(name: EXTRA_CLIENT_ID_FIELD, value: String(clientId)),
            URLQueryItem(name: EXTRA_SIGNATURE_FIELD, value: signatureData.base64EncodedString()),
            URLQueryItem(name: EXTRA_LOGO_FIELD, value: logo),
            URLQueryItem(name: EXTRA_USER_CODE_FIELD, value: userCode),
            URLQueryItem(name: EXTRA_REQUEST_ID_FIELD, value: requestId),
        ]
        
        guard let url = components.url else {
            result(FlutterError(code: "url_error", message: "Failed to create URL", details: nil))
            return
        }
        
        pendingResult = result
        pendingOperation = SIGN_PDF_OPERATION
        
        openSimaApp(url: url)
    }
    
    private func handleSignChallenge(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any],
              let challengeList = args["challenge"] as? [Int],
              let userCode = args["userCode"] as? String else {
            result(FlutterError(code: "invalid_argument", message: "Missing required parameters", details: nil))
            return
        }
        
        let clientId = args["clientId"] as? Int ?? CLIENT_ID
        let serviceName = args["serviceName"] as? String ?? SERVICE_NAME
        let signature = args["signature"] as? String ?? ""
        let requestId = args["requestId"] as? String ?? UUID().uuidString
        let logo = args["logo"] as? String ?? ""
        
        // Convert challenge list to Data
        let challenge = Data(challengeList.map { UInt8($0) })
        
        // Use signature from Flutter (already calculated)
        // If signature is empty, calculate it here as fallback
        let signatureData: Data
        if !signature.isEmpty, let sigData = Data(base64Encoded: signature) {
            signatureData = sigData
        } else {
            // Calculate SHA-256 hash
            let hash = SHA256.hash(data: challenge)
            let hashData = Data(hash)
            
            // Calculate HMAC-SHA256 signature
            guard let calculatedSig = calculateHMAC(data: hashData, key: CLIENT_MASTER_KEY) else {
                result(FlutterError(code: "signature_error", message: "Failed to calculate signature", details: nil))
                return
            }
            signatureData = calculatedSig
        }
        
        // Build URL
        var components = URLComponents()
        components.scheme = SIMA_SCHEME
        components.host = SIGN_CHALLENGE_OPERATION
        components.path = ""
        components.queryItems = [
            URLQueryItem(name: EXTRA_RETURN_SCHEME_FIELD, value: RETURN_SCHEME),
            URLQueryItem(name: EXTRA_CHALLENGE_FIELD, value: challenge.base64EncodedString()),
            URLQueryItem(name: EXTRA_SERVICE_FIELD, value: serviceName),
            URLQueryItem(name: EXTRA_CLIENT_ID_FIELD, value: String(clientId)),
            URLQueryItem(name: EXTRA_SIGNATURE_FIELD, value: signatureData.base64EncodedString()),
            URLQueryItem(name: EXTRA_LOGO_FIELD, value: logo),
            URLQueryItem(name: EXTRA_USER_CODE_FIELD, value: userCode),
            URLQueryItem(name: EXTRA_REQUEST_ID_FIELD, value: requestId),
        ]
        
        guard let url = components.url else {
            result(FlutterError(code: "url_error", message: "Failed to create URL", details: nil))
            return
        }
        
        pendingResult = result
        pendingOperation = SIGN_CHALLENGE_OPERATION
        pendingChallenge = challenge
        
        openSimaApp(url: url)
    }
    
    private func handleIsSimaInstalled(result: @escaping FlutterResult) {
        let testUrl = URL(string: "\(SIMA_SCHEME)://\(SIGN_CHALLENGE_OPERATION)")!
        let isInstalled = UIApplication.shared.canOpenURL(testUrl)
        result(isInstalled)
    }
    
    private func handleOpenAppStore(result: @escaping FlutterResult) {
        guard let url = URL(string: SIMA_URL) else {
            result(FlutterError(code: "url_error", message: "Invalid App Store URL", details: nil))
            return
        }
        UIApplication.shared.open(url)
        result(nil)
    }
    
    // MARK: - URL Callback Handler
    
    func handleUrlCallback(url: URL) -> Bool {
        guard let components = URLComponents(url: url, resolvingAgainstBaseURL: true),
              let operation = components.host,
              let queryItems = components.queryItems else {
            handleError("parse-url-error")
            return false
        }
        
        guard operation == SIGN_CHALLENGE_OPERATION || operation == SIGN_PDF_OPERATION else {
            handleError("wrong-operation-type")
            return false
        }
        
        var query: [String: String] = [:]
        for item in queryItems {
            query[item.name] = item.value
        }
        
        guard let status = query["status"] else {
            handleError("empty-status")
            return false
        }
        
        let message = query["message"]
        
        guard status == "success" else {
            handleError(message ?? "unknown-error")
            return false
        }
        
        guard let result = pendingResult else {
            return false
        }
        
        if operation == SIGN_CHALLENGE_OPERATION {
            guard let signatureBase64 = query["signature"],
                  let certificateBase64 = query["certificate"],
                  let signatureData = Data(base64Encoded: signatureBase64),
                  let certificateData = Data(base64Encoded: certificateBase64) else {
                handleError("empty-response")
                return false
            }
            
            // Verify signature (optional but recommended)
            if let challenge = pendingChallenge {
                if verifySignature(challenge: challenge, signature: signatureData, certificate: certificateData) {
                    result([
                        "status": "success",
                        "signature": signatureData.map { Int($0) },
                        "certificate": certificateData.map { Int($0) }
                    ])
                } else {
                    handleError("signature-verification-error")
                    return false
                }
            } else {
                result([
                    "status": "success",
                    "signature": signatureData.map { Int($0) },
                    "certificate": certificateData.map { Int($0) }
                ])
            }
        } else if operation == SIGN_PDF_OPERATION {
            guard let documentBase64 = query["document"],
                  let documentData = Data(base64Encoded: documentBase64) else {
                handleError("empty-response")
                return false
            }
            
            // Save signed document to temporary file
            let tempDir = FileManager.default.temporaryDirectory
            let fileName = "signed_\(UUID().uuidString).pdf"
            let fileUrl = tempDir.appendingPathComponent(fileName)
            
            do {
                try documentData.write(to: fileUrl)
                result([
                    "status": "success",
                    "signedDocumentPath": fileUrl.path
                ])
            } catch {
                handleError("file-save-error")
                return false
            }
        }
        
        pendingResult = nil
        pendingOperation = nil
        pendingChallenge = nil
        
        return true
    }
    
    // MARK: - Helper Methods
    
    private func openSimaApp(url: URL) {
        if UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        } else {
            if let appStoreUrl = URL(string: SIMA_URL) {
                UIApplication.shared.open(appStoreUrl)
            }
            if let result = pendingResult {
                result(FlutterError(code: "sima_not_installed", message: "SIMA app is not installed", details: nil))
                pendingResult = nil
            }
        }
    }
    
    private func calculateHMAC(data: Data, key: String) -> Data? {
        guard let keyData = key.data(using: .utf8) else {
            return nil
        }
        
        let symmetricKey = SymmetricKey(data: keyData)
        let hmac = HMAC<SHA256>.authenticationCode(for: data, using: symmetricKey)
        return Data(hmac)
    }
    
    private func verifySignature(challenge: Data, signature: Data, certificate: Data) -> Bool {
        guard let secCertificate = SecCertificateCreateWithData(nil, certificate as CFData),
              let publicKey = SecCertificateCopyKey(secCertificate) else {
            return false
        }
        
        var error: Unmanaged<CFError>?
        let algorithm: SecKeyAlgorithm = .ecdsaSignatureMessageX962SHA256
        
        let verified = SecKeyVerifySignature(
            publicKey,
            algorithm,
            challenge as CFData,
            signature as CFData,
            &error
        )
        
        if let error = error {
            print("Signature verification error: \(error.takeRetainedValue())")
        }
        
        return verified
    }
    
    private func handleError(_ error: String?) {
        guard let result = pendingResult else { return }
        
        let errorCode = error ?? "unknown-error"
        let errorMessage = getErrorMessage(errorCode)
        
        result(FlutterError(
            code: errorCode,
            message: errorMessage,
            details: nil
        ))
        
        pendingResult = nil
        pendingOperation = nil
        pendingChallenge = nil
    }
    
    private func getErrorMessage(_ errorCode: String) -> String {
        switch errorCode {
        case "operation-canceled":
            return "İstifadəçi əməliyyatı ləğv etdi"
        case "wrong-operation-type":
            return "Boş və ya naməlum əməliyyat növü"
        case "empty-data":
            return "Boş imzalama məlumatı (sənəd və ya challenge)"
        case "empty-service":
            return "Boş xidmət adı"
        case "empty-client-id":
            return "Boş client id"
        case "empty-signature":
            return "Boş imza"
        case "empty-user-code":
            return "Boş istifadəçi kodu (FIN)"
        case "wrong-user-code":
            return "Yanlış istifadəçi kodu (FIN)"
        case "wrong-logo-format":
            return "Yanlış logo formatı"
        case "wrong-logo-size":
            return "Logo ölçüsü çox böyükdür (>500KB)"
        case "document-processing-error":
            return "Sənəd məlumatlarının işlənməsi zamanı xəta"
        case "challenge-processing-error":
            return "Challenge məlumatlarının işlənməsi zamanı xəta"
        case "validate-request-error":
            return "İmzalama sorğusunun yoxlanılması zamanı xəta (yanlış client id və ya imza)"
        case "timestamp-request-error":
            return "Sənəd imzalama üçün timestamp sorğusu zamanı xəta"
        case "approve-request-error":
            return "İmzalama sorğusunun təsdiqlənməsi zamanı xəta"
        case "sign-document-error":
            return "Sənədin imzalanması zamanı xəta"
        case "sign-challenge-error":
            return "Challenge-in imzalanması zamanı xəta"
        case "internal-error":
            return "Daxili Sima xətası"
        case "empty-response":
            return "Sima-dan boş cavab"
        default:
            return "Naməlum xəta: \(errorCode)"
        }
    }
}

