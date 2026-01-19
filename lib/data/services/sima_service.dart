import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:crypto/crypto.dart';
import 'package:sima/sima.dart';
import 'package:sima/src/sima_challange.dart' show SimaChallenge;
import '../models/sima_response.dart';

class SimaService {
  // Use custom method channel for isInstalled/openStore (not available in sima package)
  static const MethodChannel _customChannel = MethodChannel('az.btb.btb_mobile_flutter/sima');
  
  // SIMA Configuration
  static const String _packageName = 'az.dpc.sima';
  static const String _clientId = '1082001';
  static const String _masterKey = '9569936E-9A4D-4AAC-B286-E6242CD08FA0';
  static const String _serviceName = 'Bank BTB (App2App) (TEST)';
  static const String _returnScheme = 'btbmobile';
  static const String _logoPath = 'assets/images/SIMA_logo.svg';

  /// Sign a PDF document using SIMA
  /// 
  /// Note: The sima package doesn't support PDF signing directly.
  /// This method is kept for compatibility but may need native implementation.
  /// 
  /// [documentPath] - Path to the PDF file to sign
  /// [userFinCode] - User's FIN code (required by SIMA)
  /// [logoDataUri] - Optional logo as data URI (max 500KB)
  /// 
  /// Returns [SimaSignPdfResponse] with signed document path on success
  Future<SimaSignPdfResponse> signPdf({
    required String documentPath,
    required String userFinCode,
    String? logoDataUri,
  }) async {
    // PDF signing is not supported by the sima package
    // You may need to use native implementation for this
    return SimaSignPdfResponse(
      status: 'error',
      message: 'PDF signing not yet implemented with sima package',
    );
  }

  /// Sign a challenge using SIMA
  /// 
  /// [challenge] - Challenge bytes to sign (typically 64 bytes)
  /// [userFinCode] - User's FIN code (required by SIMA)
  /// [logoDataUri] - Optional logo as data URI (max 500KB) - ignored, using asset logo
  /// 
  /// Returns [SimaSignChallengeResponse] with signature and certificate on success
  Future<SimaSignChallengeResponse> signChallenge({
    required List<int> challenge,
    required String userFinCode,
    String? logoDataUri,
  }) async {
    try {
      debugPrint('=== SIMA signChallenge START ===');
      debugPrint('User FIN Code: $userFinCode');
      debugPrint('Challenge length: ${challenge.length} bytes');
      debugPrint('Client ID: $_clientId');
      debugPrint('Return Scheme: $_returnScheme');
      debugPrint('Service Name: $_serviceName');
      debugPrint('Platform: ${Platform.isIOS ? "iOS" : "Android"}');
      
      // Use SIMA package loginSafe method for both iOS and Android
      debugPrint('Using sima package for ${Platform.isIOS ? "iOS" : "Android"}');
      
      // Convert challenge to Uint8List and create SimaChallenge
      final challengeBytes = Uint8List.fromList(challenge);
      final simaChallenge = SimaChallenge(challengeBytes);

      // Calculate signature using master key (HMAC-SHA256)
      final hash = sha256.convert(challengeBytes);
      final hmac = Hmac(sha256, utf8.encode(_masterKey));
      final signatureBytes = hmac.convert(hash.bytes);
      final signature = base64Encode(signatureBytes.bytes);
      
      debugPrint('Signature calculated (base64 length: ${signature.length})');
      debugPrint('Calling Sima.loginSafe...');

      // Use SIMA package loginSafe method to sign challenge
      final result = await Sima.loginSafe(
        clientId: _clientId,
        returnScheme: _returnScheme,
        serviceName: _serviceName,
        logoPath: _logoPath,
        challenge: simaChallenge,
        signature: signature,
      );

      debugPrint('=== SIMA Response Received ===');
      debugPrint('Result is null: ${result == null}');
      
      if (result == null) {
        debugPrint('SIMA Response: NULL (operation cancelled or failed)');
        return SimaSignChallengeResponse(
          status: 'error',
          message: 'SIMA operation failed or was cancelled',
          signatureBytes: null,
          certificateBytes: null,
        );
      }

      debugPrint('Result.isSuccess: ${result.isSuccess}');
      debugPrint('Result.message: ${result.message}');
      debugPrint('Result.signature: ${result.signature != null ? "Present (${result.signature!.length} chars)" : "NULL"}');
      debugPrint('Result.certificate: ${result.certificate != null ? "Present (${result.certificate!.length} chars)" : "NULL"}');
      
      if (result.signature != null) {
        debugPrint('Signature (first 50 chars): ${result.signature!.substring(0, result.signature!.length > 50 ? 50 : result.signature!.length)}...');
      }
      if (result.certificate != null) {
        debugPrint('Certificate (first 50 chars): ${result.certificate!.substring(0, result.certificate!.length > 50 ? 50 : result.certificate!.length)}...');
      }

      if (result.isSuccess) {
        // Convert base64 strings to byte lists
        final signatureBytesList = result.signature != null
            ? base64Decode(result.signature!)
            : null;
        final certificateBytesList = result.certificate != null
            ? base64Decode(result.certificate!)
            : null;

        debugPrint('=== SIMA Response: SUCCESS ===');
        debugPrint('Signature bytes: ${signatureBytesList?.length ?? 0} bytes');
        debugPrint('Certificate bytes: ${certificateBytesList?.length ?? 0} bytes');
        debugPrint('=== SIMA signChallenge END (SUCCESS) ===');

        return SimaSignChallengeResponse(
          status: 'success',
          message: null,
          signatureBytes: signatureBytesList?.toList(),
          certificateBytes: certificateBytesList?.toList(),
        );
      } else {
        debugPrint('=== SIMA Response: ERROR ===');
        debugPrint('Error message: ${result.message ?? "Unknown error"}');
        debugPrint('=== SIMA signChallenge END (ERROR) ===');
        
        return SimaSignChallengeResponse(
          status: 'error',
          message: result.message ?? 'Unknown error',
          signatureBytes: null,
          certificateBytes: null,
        );
      }
    } catch (e, stackTrace) {
      debugPrint('=== SIMA Exception ===');
      debugPrint('Error: $e');
      debugPrint('Stack trace: $stackTrace');
      debugPrint('=== SIMA signChallenge END (EXCEPTION) ===');
      
      return SimaSignChallengeResponse(
        status: 'error',
        message: SimaError.getErrorMessage(e.toString()),
      );
    }
  }

  /// Generate a random challenge (64 bytes)
  /// Uses cryptographically secure random generator
  List<int> generateChallenge() {
    final challenge = Sima.createChallenge(length: 64);
    return challenge.bytes.toList();
  }


  /// Check if SIMA app is installed
  Future<bool> isSimaInstalled() async {
    try {
      // Use custom method channel (sima package doesn't provide isInstalled)
      final result = await _customChannel.invokeMethod('isSimaInstalled');
      return result as bool? ?? false;
    } catch (e) {
      return false;
    }
  }

  /// Open App Store/Play Store to install SIMA app
  Future<void> openPlayStore() async {
    try {
      // Use custom method channel (sima package doesn't provide openStore)
      await _customChannel.invokeMethod('openPlayStore');
    } catch (e) {
      // Ignore errors
    }
  }
}

