package az.btb.btb_mobile_flutter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.UUID

class MainActivity : FlutterActivity() {
    private val DEVICE_CHANNEL = "az.btb.btb_mobile_flutter/device"
    private val SIMA_CHANNEL = "az.btb.btb_mobile_flutter/sima"
    
    // SIMA Configuration
    private val PACKAGE_NAME = "az.dpc.sima"
    private val SIGN_PDF_OPERATION = "sima.sign.pdf"
    private val SIGN_CHALLENGE_OPERATION = "sima.sign.challenge"
    private val CLIENT_SIGNATURE_ALGORITHM = "HmacSHA256"
    private val CLIENT_HASH_ALGORITHM = "SHA-256"
    
    // Intent field names
    private val EXTRA_CLIENT_ID_FIELD = "client_id"
    private val EXTRA_SERVICE_FIELD = "service_name"
    private val EXTRA_CHALLENGE_FIELD = "challenge"
    private val EXTRA_SIGNATURE_FIELD = "signature"
    private val EXTRA_USER_CODE_FIELD = "user_code"
    private val EXTRA_REQUEST_ID_FIELD = "request_id"
    private val EXTRA_LOGO_FIELD = "service_logo"
    
    private var pendingSimaResult: MethodChannel.Result? = null
    private var pendingSimaOperation: String? = null
    private var pendingChallenge: ByteArray? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        
        // Device channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, DEVICE_CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getAndroidId") {
                val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                result.success(androidId)
            } else {
                result.notImplemented()
            }
        }
        
        // SIMA channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, SIMA_CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "signPdf" -> {
                    val documentPath = call.argument<String>("documentPath")
                    val clientId = call.argument<Int>("clientId") ?: 0
                    val serviceName = call.argument<String>("serviceName") ?: ""
                    val signature = call.argument<String>("signature") ?: ""
                    val userCode = call.argument<String>("userCode") ?: ""
                    val requestId = call.argument<String>("requestId") ?: ""
                    val logo = call.argument<String>("logo") ?: ""
                    
                    if (documentPath == null) {
                        result.error("invalid_argument", "Document path is required", null)
                        return@setMethodCallHandler
                    }
                    
                    signPdf(documentPath, clientId, serviceName, signature, userCode, requestId, logo, result)
                }
                "signChallenge" -> {
                    val challengeList = call.argument<List<Int>>("challenge")
                    val clientId = call.argument<Int>("clientId") ?: 0
                    val serviceName = call.argument<String>("serviceName") ?: ""
                    val signature = call.argument<String>("signature") ?: ""
                    val userCode = call.argument<String>("userCode") ?: ""
                    val requestId = call.argument<String>("requestId") ?: ""
                    val logo = call.argument<String>("logo") ?: ""
                    
                    if (challengeList == null) {
                        result.error("invalid_argument", "Challenge is required", null)
                        return@setMethodCallHandler
                    }
                    
                    val challenge = challengeList.map { it.toByte() }.toByteArray()
                    signChallenge(challenge, clientId, serviceName, signature, userCode, requestId, logo, result)
                }
                "isSimaInstalled" -> {
                    val isInstalled = isSimaInstalled()
                    result.success(isInstalled)
                }
                "openPlayStore" -> {
                    openPlayStore()
                    result.success(null)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle deep link if app was opened via SIMA callback
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle deep link if app was already running
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.data?.scheme == "btbmobile") {
            Log.d("SIMA", "Received deep link callback from SIMA: ${intent.data}")
            // Handle SIMA callback via deep link
            handleSimaDeepLink(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == 1001) { // SIMA request code
            handleSimaResult(resultCode, data)
        }
    }

    private fun isSimaInstalled(): Boolean {
        return try {
            val packageInfo = packageManager.getPackageInfo(PACKAGE_NAME, 0)
            Log.d("SIMA", "SIMA app found: ${packageInfo.packageName}, version: ${packageInfo.versionName}")
            true
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SIMA", "SIMA app not found: $PACKAGE_NAME")
            false
        }
    }
    
    private fun checkSimaIntentFilters() {
        try {
            val packageInfo = packageManager.getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES or PackageManager.GET_INTENT_FILTERS)
            Log.d("SIMA", "SIMA package info: ${packageInfo.packageName}")
            Log.d("SIMA", "SIMA activities count: ${packageInfo.activities?.size ?: 0}")
            // Log all activities and their intent filters
            packageInfo.activities?.forEach { activity ->
                Log.d("SIMA", "Activity: ${activity.name}")
                // Note: Intent filters are not directly accessible via PackageInfo
                // We'd need to use PackageManager.getActivityInfo() for each activity
            }
        } catch (e: Exception) {
            Log.e("SIMA", "Error checking SIMA package info: ${e.message}")
        }
    }

    private fun openPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PACKAGE_NAME"))
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$PACKAGE_NAME"))
            startActivity(intent)
        }
    }

    private fun signPdf(
        documentPath: String,
        clientId: Int,
        serviceName: String,
        signature: String,
        userCode: String,
        requestId: String,
        logo: String,
        result: MethodChannel.Result
    ) {
        if (!isSimaInstalled()) {
            result.error("sima_not_installed", "Sima app is not installed", null)
            openPlayStore()
            return
        }

        try {
            // Handle both file:// URIs and regular file paths
            val documentUri = when {
                documentPath.startsWith("file://") -> Uri.parse(documentPath)
                documentPath.startsWith("/") -> {
                    // Use FileProvider for better security (Android 7.0+)
                    // For now, use file:// URI which works for most cases
                    Uri.parse("file://$documentPath")
                }
                else -> Uri.parse("file://$documentPath")
            }
            
            val intent = Intent(SIGN_PDF_OPERATION).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setData(documentUri)
                putExtra(EXTRA_SERVICE_FIELD, serviceName)
                putExtra(EXTRA_CLIENT_ID_FIELD, clientId)
                putExtra(EXTRA_SIGNATURE_FIELD, signature)
                putExtra(EXTRA_LOGO_FIELD, logo)
                putExtra(EXTRA_USER_CODE_FIELD, userCode)
                putExtra(EXTRA_REQUEST_ID_FIELD, requestId)
                setPackage(PACKAGE_NAME)
            }

            pendingSimaResult = result
            pendingSimaOperation = "signPdf"
            
            // Ensure we're on the main thread
            runOnUiThread {
                try {
            startActivityForResult(intent, 1001)
                } catch (e: Exception) {
                    Log.e("SIMA", "Exception in startActivityForResult (signPdf): ${e.message}", e)
                    pendingSimaResult = null
                    pendingSimaOperation = null
                    result.error("intent_error", "Failed to start Sima activity: ${e.message}", null)
                }
            }
        } catch (e: Exception) {
            // Clear pending result if error occurs before starting activity
            pendingSimaResult = null
            pendingSimaOperation = null
            result.error("intent_error", "Failed to start Sima: ${e.message}", null)
        }
    }

    private fun signChallenge(
        challenge: ByteArray,
        clientId: Int,
        serviceName: String,
        signature: String,
        userCode: String,
        requestId: String,
        logo: String,
        result: MethodChannel.Result
    ) {
        Log.d("SIMA", "signChallenge called with userCode: $userCode, clientId: $clientId")
        
        // Check if SIMA is installed
        if (!isSimaInstalled()) {
            Log.e("SIMA", "SIMA app is not installed")
            result.error("sima_not_installed", "Sima app is not installed", null)
            openPlayStore()
            return
        }
        
        // Debug: Check SIMA package info
        checkSimaIntentFilters()

        try {
            // According to SIMA Android documentation, use Intent with action
            // Convert signature from base64 string to byte array
            val signatureBytes = try {
                Base64.decode(signature, Base64.NO_WRAP)
            } catch (e: Exception) {
                Log.e("SIMA", "Failed to decode signature: ${e.message}")
                result.error("signature_error", "Invalid signature format", null)
                return
            }
            
            // Validate userCode is not empty
            if (userCode.isEmpty()) {
                Log.e("SIMA", "signChallenge: userCode (FIN code) is empty")
                result.error("empty-user-code", "User code (FIN) is required", null)
                return
            }
            
            Log.d("SIMA", "signChallenge: Preparing intent with userCode='$userCode' (FIN code will be validated by SIMA)")
            
            // Create intent with action and set package directly
            val intent = Intent(SIGN_CHALLENGE_OPERATION).apply {
                setFlags(0) // Clear all flags first as per documentation
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                setPackage(PACKAGE_NAME) // Set package first
                putExtra(EXTRA_CHALLENGE_FIELD, challenge)
                putExtra(EXTRA_SERVICE_FIELD, serviceName)
                putExtra(EXTRA_CLIENT_ID_FIELD, clientId)
                putExtra(EXTRA_SIGNATURE_FIELD, signatureBytes) // Use byte array, not base64 string
                putExtra(EXTRA_LOGO_FIELD, logo)
                putExtra(EXTRA_USER_CODE_FIELD, userCode) // FIN code - SIMA will validate this matches user's actual FIN
                putExtra(EXTRA_REQUEST_ID_FIELD, requestId)
            }
            
            Log.d("SIMA", "Intent action: $SIGN_CHALLENGE_OPERATION, Package: $PACKAGE_NAME")
            Log.d("SIMA", "Intent extras: userCode='$userCode', clientId=$clientId, serviceName='$serviceName'")
            
            // Some apps don't expose intent filters to queries, so we'll try to start it anyway
            // But first, let's check if we can resolve it
            val resolveInfo = try {
                packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            } catch (e: Exception) {
                Log.e("SIMA", "Error resolving activity: ${e.message}", e)
                null
            }
            
            if (resolveInfo == null) {
                Log.w("SIMA", "Cannot resolve activity via query, but SIMA app is installed")
                Log.w("SIMA", "This might be normal - some apps don't expose intent filters to queries")
                Log.w("SIMA", "Attempting to start activity anyway...")
                // Continue - we'll try to start it and catch the exception if it fails
            } else {
                Log.d("SIMA", "Intent resolved successfully, activity: ${resolveInfo.activityInfo.name}, package: ${resolveInfo.activityInfo.packageName}")
            }

            // Only set pending result if we're about to start the activity
            pendingSimaResult = result
            pendingSimaOperation = "signChallenge"
            pendingChallenge = challenge
            
            // Ensure we're on the main thread
            runOnUiThread {
                try {
            startActivityForResult(intent, 1001)
                    Log.d("SIMA", "startActivityForResult called, waiting for SIMA response...")
                } catch (e: Exception) {
                    Log.e("SIMA", "Exception in startActivityForResult: ${e.message}", e)
                    pendingSimaResult = null
                    pendingSimaOperation = null
                    pendingChallenge = null
                    result.error("intent_error", "Failed to start Sima activity: ${e.message}", null)
                }
            }
        } catch (e: Exception) {
            Log.e("SIMA", "Error creating intent: ${e.message}", e)
            // Clear pending result if error occurs before starting activity
            pendingSimaResult = null
            pendingSimaOperation = null
            pendingChallenge = null
            result.error("intent_error", "Failed to create Sima intent: ${e.message}", null)
        }
    }

    private fun handleSimaDeepLink(intent: Intent) {
        val result = pendingSimaResult ?: return
        val operation = pendingSimaOperation ?: return
        
        // Clear pending values immediately
        pendingSimaResult = null
        pendingSimaOperation = null
        pendingChallenge = null
        
        try {
            val uri = intent.data ?: run {
                Log.e("SIMA", "handleSimaDeepLink: intent.data is null")
                result.error("empty-response", "Empty response from Sima", null)
                return
            }
            
            Log.d("SIMA", "Handling deep link callback: $uri")
            
            // Parse query parameters from URL
            val status = uri.getQueryParameter("status") ?: ""
            val message = uri.getQueryParameter("message") ?: ""
            
            Log.d("SIMA", "handleSimaDeepLink: status='$status', message='$message'")
            
            // Log all query parameters for debugging
            val queryParams = uri.queryParameterNames
            Log.d("SIMA", "handleSimaDeepLink: Query parameters: $queryParams")
            for (param in queryParams) {
                Log.d("SIMA", "handleSimaDeepLink: $param = ${uri.getQueryParameter(param)}")
            }
            
            // Check if status indicates an error (not "success")
            if (status != "success") {
                // Use message as error code, or default to status if message is empty
                val errorCode = if (message.isNotEmpty()) {
                    message
                } else if (status.isNotEmpty()) {
                    status
                } else {
                    "unknown_error"
                }
                
                Log.e("SIMA", "handleSimaDeepLink: SIMA returned error - code='$errorCode'")
                result.error(errorCode, SimaErrorHandler.getErrorMessage(errorCode), null)
                return
            }
            
            when (operation) {
                "signChallenge" -> {
                    val signatureBase64 = uri.getQueryParameter("signature") ?: ""
                    val certificateBase64 = uri.getQueryParameter("certificate") ?: ""
                    
                    Log.d("SIMA", "handleSimaDeepLink: signChallenge - signature present=${signatureBase64.isNotEmpty()}, certificate present=${certificateBase64.isNotEmpty()}")
                    
                    if (signatureBase64.isEmpty() || certificateBase64.isEmpty()) {
                        Log.e("SIMA", "handleSimaDeepLink: signChallenge - missing signature or certificate")
                        result.error("empty-response", "No signature or certificate returned", null)
                        return
                    }
                    
                    val signatureBytes = Base64.decode(signatureBase64, Base64.NO_WRAP)
                    val certificateBytes = Base64.decode(certificateBase64, Base64.NO_WRAP)
                    
                    Log.d("SIMA", "handleSimaDeepLink: signChallenge success - signature size=${signatureBytes.size}, certificate size=${certificateBytes.size}")
                    
                    result.success(mapOf(
                        "status" to "success",
                        "signature" to signatureBytes.toList(),
                        "certificate" to certificateBytes.toList()
                    ))
                }
                "signPdf" -> {
                    val signedDocumentPath = uri.getQueryParameter("signedDocumentPath") ?: ""
                    if (signedDocumentPath.isEmpty()) {
                        Log.e("SIMA", "handleSimaDeepLink: signPdf - no signed document path")
                        result.error("empty-response", "No signed document returned", null)
                        return
                    }
                    Log.d("SIMA", "handleSimaDeepLink: signPdf success - path=$signedDocumentPath")
                    result.success(mapOf(
                        "status" to "success",
                        "signedDocumentPath" to signedDocumentPath
                    ))
                }
                else -> {
                    Log.e("SIMA", "handleSimaDeepLink: unknown operation '$operation'")
                    result.error("unknown_operation", "Unknown operation: $operation", null)
                }
            }
        } catch (e: Exception) {
            Log.e("SIMA", "Error handling deep link: ${e.message}", e)
            try {
                result.error("processing_error", "Error processing Sima result: ${e.message}", null)
            } catch (e2: IllegalStateException) {
                // Result already replied to, ignore
                Log.d("SIMA", "handleSimaDeepLink: Result already replied to, ignoring exception")
            }
        }
    }

    private fun handleSimaResult(resultCode: Int, data: Intent?) {
        // Get and clear pending result immediately to prevent double reply
        val result = pendingSimaResult ?: return
        val operation = pendingSimaOperation ?: return
        
        // Clear pending values immediately to prevent handling twice
        pendingSimaResult = null
        pendingSimaOperation = null
        pendingChallenge = null

        try {
            Log.d("SIMA", "handleSimaResult: resultCode=$resultCode, data=$data")
            
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Log.e("SIMA", "handleSimaResult: data is null")
                    result.error("empty-response", "Empty response from Sima", null)
                    return
                }

                // Get all extras for debugging
                val extras = data.extras
                if (extras != null) {
                    Log.d("SIMA", "handleSimaResult: Extras keys: ${extras.keySet()}")
                    for (key in extras.keySet()) {
                        Log.d("SIMA", "handleSimaResult: $key = ${extras.get(key)}")
                    }
                }

                val status = data.getStringExtra("status") ?: ""
                val message = data.getStringExtra("message") ?: ""
                
                Log.d("SIMA", "handleSimaResult: status='$status', message='$message'")

                // Check if status indicates an error (not "success")
                if (status != "success") {
                    // Use message as error code, or default to status if message is empty
                    val errorCode = if (message.isNotEmpty()) {
                        message
                    } else if (status.isNotEmpty()) {
                        status
                    } else {
                        "unknown_error"
                    }
                    
                    Log.e("SIMA", "handleSimaResult: SIMA returned error - code='$errorCode'")
                    result.error(errorCode, SimaErrorHandler.getErrorMessage(errorCode), null)
                    return
                }

                when (operation) {
                    "signPdf" -> {
                        val signedDocumentUri = data.data
                        if (signedDocumentUri != null) {
                            Log.d("SIMA", "handleSimaResult: signPdf success")
                            result.success(mapOf(
                                "status" to "success",
                                "signedDocumentPath" to signedDocumentUri.toString()
                            ))
                        } else {
                            Log.e("SIMA", "handleSimaResult: signPdf - no signed document returned")
                            result.error("empty-response", "No signed document returned", null)
                        }
                    }
                    "signChallenge" -> {
                        val signatureBytes = data.getByteArrayExtra("signature")
                        val certificateBytes = data.getByteArrayExtra("certificate")
                        
                        Log.d("SIMA", "handleSimaResult: signChallenge - signature=${signatureBytes != null}, certificate=${certificateBytes != null}")
                        
                        if (signatureBytes != null && certificateBytes != null) {
                            Log.d("SIMA", "handleSimaResult: signChallenge success - signature size=${signatureBytes.size}, certificate size=${certificateBytes.size}")
                            result.success(mapOf(
                                "status" to "success",
                                "signature" to signatureBytes.toList(),
                                "certificate" to certificateBytes.toList()
                            ))
                        } else {
                            Log.e("SIMA", "handleSimaResult: signChallenge - missing signature or certificate")
                            result.error("empty-response", "No signature or certificate returned", null)
                        }
                    }
                    else -> {
                        Log.e("SIMA", "handleSimaResult: unknown operation '$operation'")
                        result.error("unknown_operation", "Unknown operation: $operation", null)
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("SIMA", "handleSimaResult: User canceled the operation")
                result.error("operation-canceled", "User canceled the operation", null)
            } else {
                Log.e("SIMA", "handleSimaResult: Unknown result code: $resultCode")
                result.error("unknown_error", "Unknown error occurred (resultCode: $resultCode)", null)
            }
        } catch (e: Exception) {
            Log.e("SIMA", "handleSimaResult: Exception occurred", e)
            // Only send error if result hasn't been replied to yet
            try {
                result.error("processing_error", "Error processing Sima result: ${e.message}", null)
            } catch (e2: IllegalStateException) {
                // Result already replied to, ignore
                Log.d("SIMA", "handleSimaResult: Result already replied to, ignoring exception")
            }
        }
    }
}

object SimaErrorHandler {
    fun getErrorMessage(errorCode: String): String {
        return when (errorCode) {
            "operation-canceled" -> "İstifadəçi əməliyyatı ləğv etdi"
            "wrong-operation-type" -> "Boş və ya naməlum əməliyyat növü"
            "empty-data" -> "Boş imzalama məlumatı (sənəd və ya challenge)"
            "empty-service" -> "Boş xidmət adı"
            "empty-client-id" -> "Boş client id"
            "empty-signature" -> "Boş imza"
            "empty-user-code" -> "Boş istifadəçi kodu (FIN)"
            "wrong-user-code" -> "Yanlış istifadəçi kodu (FIN)"
            "wrong-logo-format" -> "Yanlış logo formatı"
            "wrong-logo-size" -> "Logo ölçüsü çox böyükdür (>500KB)"
            "document-processing-error" -> "Sənəd məlumatlarının işlənməsi zamanı xəta"
            "challenge-processing-error" -> "Challenge məlumatlarının işlənməsi zamanı xəta"
            "validate-request-error" -> "İmzalama sorğusunun yoxlanılması zamanı xəta (yanlış client id və ya imza)"
            "timestamp-request-error" -> "Sənəd imzalama üçün timestamp sorğusu zamanı xəta"
            "approve-request-error" -> "İmzalama sorğusunun təsdiqlənməsi zamanı xəta"
            "sign-document-error" -> "Sənədin imzalanması zamanı xəta"
            "sign-challenge-error" -> "Challenge-in imzalanması zamanı xəta"
            "internal-error" -> "Daxili Sima xətası"
            "empty-response" -> "Sima-dan boş cavab"
            else -> "Naməlum xəta: $errorCode"
        }
    }
}
