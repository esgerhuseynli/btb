import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:dio/dio.dart';
import '../datasources/remote/api_service.dart';
import '../models/request_info.dart';
import '../models/sign_in_request.dart';
import '../models/sign_in_response.dart';
import '../models/sign_up_request.dart';
import '../models/sign_up_response.dart';
import '../models/card_send_request.dart';
import '../models/card_send_response.dart';
import '../models/verify_code_request.dart';
import '../models/verify_code_response.dart';
import '../models/api_response.dart';
import '../models/change_keystore_request.dart';
import '../models/change_keystore_response.dart';
import '../models/fcm_token_request.dart';
import '../models/mobile_device_specifications.dart';
import '../models/bank_card.dart';
import '../models/bank_account.dart';
import '../models/mobile_user_data_request.dart';
import '../models/bank_cards_response.dart';
import '../models/bank_accounts_response.dart';
import '../models/otp_request.dart';
import '../models/otp_verify_request.dart';
import '../../core/constants/app_constants.dart';
import '../../core/constants/api_endpoints.dart';
import '../models/response_info.dart';
import '../../core/network/interceptors/api_logging_interceptor.dart';

class AuthRepository {
  final ApiService _apiService;
  final Dio _otpDio;
  final Dio _simaDio;

  AuthRepository(this._apiService) 
      : _otpDio = Dio(
          BaseOptions(
            baseUrl: AppConstants.otpApiBaseUrl,
            connectTimeout: const Duration(seconds: 60),
            receiveTimeout: const Duration(seconds: 60),
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
            contentType: Headers.jsonContentType,
            responseType: ResponseType.json,
          ),
        ),
        _simaDio = Dio(
          BaseOptions(
            baseUrl: 'http://94.20.61.252:8091',
            connectTimeout: const Duration(seconds: 60),
            receiveTimeout: const Duration(seconds: 60),
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
            contentType: Headers.jsonContentType,
            responseType: ResponseType.json,
          ),
        ) {
    // Add logging interceptor to OTP Dio instance
    _otpDio.interceptors.add(ApiLoggingInterceptor());
    // Add logging interceptor to SIMA Dio instance
    _simaDio.interceptors.add(ApiLoggingInterceptor());
  }

  Future<SignInResponse> signIn({
    required RequestInfo requestInfo,
    required int keystoreType,
    required int signInType,
    String? mobileNumber,
    String? mobileNumberSecretCode,
  }) async {
    // MobileUser is already set in RequestInfo
    // MobileNumber and MobileNumberSecretCode are used for SIMA sign-in
    final request = SignInRequest(
      requestInfo: requestInfo,
      keystoreType: keystoreType,
      signInType: signInType,
      mobileNumber: mobileNumber,
      mobileNumberSecretCode: mobileNumberSecretCode,
    );

    // Log request in readable format
    if (kDebugMode) {
      final requestJson = request.toJson();
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('=== Sign In Request ===');
      debugPrint(formattedJson);
      debugPrint('======================');
    }

    return await _apiService.signIn(request);
  }

  Future<CardSendResponse> sendCardNumber({
    required CardSendRequest request,
  }) async {
    // Log request in readable format
    if (kDebugMode) {
      final requestJson = request.toJson();
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('=== Card Send Request ===');
      debugPrint(formattedJson);
      debugPrint('======================');
    }

    return await _apiService.sendCardNumber(request);
  }

  Future<VerifyCodeResponse> verifyCode({
    required VerifyCodeRequest request,
  }) async {
    return await _apiService.verifyCode(request);
  }

  Future<SignUpResponse> signUp({
    required SignUpRequest request,
  }) async {
    return await _apiService.registerMobileUser(request);
  }

  Future<ApiResponse<dynamic>> signOut({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    return await _apiService.signOut(request);
  }

  Future<void> reportKeystoreIncident({
    required RequestInfo requestInfo,
    required int incidentType,
    required int incidentCount,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
      'KeystoreSecurityIncidentType': incidentType,
      'KeystoreSecurityIncidentCount': incidentCount,
    };

    // Fire and forget - no response handling needed
    try {
      await _apiService.reportKeystoreIncident(request);
    } catch (e) {
      // Ignore errors for keystore incident reporting
      if (kDebugMode) {
        debugPrint('Keystore incident reporting failed: $e');
      }
    }
  }

  Future<CardSendResponse> forgotPassword({
    required RequestInfo requestInfo,
    String requestParametersValidationMessage = '',
    bool requestParametersValidated = true,
  }) async {
    // Build request with camelCase keys as required by the API
    // Include all 7 fields for userInfo: username, passwordHash, sessionKey, saltSignature, pinCode, phoneNumber, birthDate
    // All fields must be present even if null/empty
    final mu = requestInfo.mobileUser;
    
    // Build userInfo map with all 7 fields - always include all fields
    final userInfoMap = <String, dynamic>{
      'username': mu?.username ?? '',
      'passwordHash': mu?.passwordHash ?? '',
      'sessionKey': mu?.sessionKey ?? '',
      'saltSignature': mu?.saltSignature ?? '',
      'pinCode': mu?.pinCode ?? '',
      'phoneNumber': mu?.phoneNumber ?? '',
      'birthDate': mu?.birthDate ?? '',
    };

    final requestInfoMap = <String, dynamic>{
      'userInfo': userInfoMap,
      'deviceInfo': {
        'deviceID': requestInfo.deviceInfo.deviceID,
        'vendor': requestInfo.deviceInfo.vendor,
        'model': requestInfo.deviceInfo.model,
        'osName': requestInfo.deviceInfo.osName,
        'osVersion': requestInfo.deviceInfo.osVersion,
      },
      'appInfo': {
        'appFor': requestInfo.appInfo.appFor,
        'appName': requestInfo.appInfo.appName,
        'appVersion': requestInfo.appInfo.appVersion,
        'appHash': requestInfo.appInfo.apiHash ?? '',
      },
      'language': requestInfo.language,
    };

    final request = <String, dynamic>{
      'requestInfo': requestInfoMap,
      'requestParametersValidationMessage': requestParametersValidationMessage,
      'requestParametersValidated': requestParametersValidated,
    };

    if (kDebugMode) {
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(request);
      debugPrint('=== Forgot Password Request ===');
      debugPrint(formattedJson);
      debugPrint('userInfo fields count: ${userInfoMap.length}');
      debugPrint('userInfo keys: ${userInfoMap.keys.toList()}');
      debugPrint('================================');
    }

    return await _apiService.forgotPassword(request);
  }

  Future<ApiResponse<dynamic>> changeForgotPassword({
    required RequestInfo requestInfo,
    required String phoneNumber,
    required String pinCode,
    required String newPasswordHash,
    String requestParametersValidationMessage = '',
    bool requestParametersValidated = true,
  }) async {
    // Build request with camelCase keys as required by the API
    // mobileUser should only have: username, passwordHash, sessionKey, saltSignature
    final mu = requestInfo.mobileUser;
    
    // Build mobileUser map with only 4 fields as per API spec
    final mobileUserMap = <String, dynamic>{
      'username': mu?.username ?? '',
      'passwordHash': mu?.passwordHash ?? '',
      'sessionKey': mu?.sessionKey ?? '',
      'saltSignature': mu?.saltSignature ?? '',
    };

    final requestInfoMap = <String, dynamic>{
      'mobileUser': mobileUserMap,
      'phoneNumber': phoneNumber,
      'pinCode': pinCode,
      'deviceInfo': {
        'deviceID': requestInfo.deviceInfo.deviceID,
        'vendor': requestInfo.deviceInfo.vendor,
        'model': requestInfo.deviceInfo.model,
        'osName': requestInfo.deviceInfo.osName,
        'osVersion': requestInfo.deviceInfo.osVersion,
      },
      'appInfo': {
        'appFor': requestInfo.appInfo.appFor,
        'appName': requestInfo.appInfo.appName,
        'appVersion': requestInfo.appInfo.appVersion,
        'appHash': requestInfo.appInfo.apiHash ?? '',
      },
      'language': requestInfo.language,
    };

    final request = <String, dynamic>{
      'requestInfo': requestInfoMap,
      'newPasswordHash': newPasswordHash,
      'requestParametersValidationMessage': requestParametersValidationMessage,
      'requestParametersValidated': requestParametersValidated,
    };

    if (kDebugMode) {
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(request);
      debugPrint('=== Change Forgot Password Request ===');
      debugPrint(formattedJson);
      debugPrint('mobileUser fields count: ${mobileUserMap.length}');
      debugPrint('mobileUser keys: ${mobileUserMap.keys.toList()}');
      debugPrint('========================================');
    }

    return await _apiService.changeForgotPassword(request);
  }

  Future<ChangeKeystoreResponse> changeKeystore({
    required RequestInfo requestInfo,
    required int keystoreType,
    required MobileDeviceSpecifications deviceSpecs,
  }) async {
    final request = ChangeKeystoreRequest(
      requestInfo: requestInfo,
      keystoreType: keystoreType,
      mobileDeviceSpecifications: deviceSpecs,
    );

    if (kDebugMode) {
      debugPrint('=== Change Keystore Request Object ===');
      debugPrint('Request keystoreType: ${request.keystoreType}');
      debugPrint('Request has deviceSpecs: ${request.mobileDeviceSpecifications != null}');
      debugPrint('Request has requestInfo: ${request.requestInfo != null}');
      
      final requestJson = request.toJson();
      debugPrint('Request JSON keys: ${requestJson.keys.toList()}');
      debugPrint('Request JSON KeystoreType value: ${requestJson['KeystoreType']}');
      debugPrint('Request JSON has MobileDeviceSpecifications: ${requestJson.containsKey('MobileDeviceSpecifications')}');
      
      // Verify all required fields are present
      if (!requestJson.containsKey('KeystoreType') || 
          !requestJson.containsKey('MobileDeviceSpecifications') || 
          !requestJson.containsKey('RequestInfo')) {
        debugPrint('ERROR: Missing required fields in request JSON!');
        debugPrint('Expected: KeystoreType, MobileDeviceSpecifications, RequestInfo');
        debugPrint('Actual keys: ${requestJson.keys.toList()}');
      }
      
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('Full Request JSON:');
      debugPrint(formattedJson);
      debugPrint('=====================================');
    }

    // Ensure the request has all required fields before sending
    final requestJson = request.toJson();
    if (!requestJson.containsKey('KeystoreType') || 
        !requestJson.containsKey('MobileDeviceSpecifications')) {
      throw Exception('ChangeKeystoreRequest is missing required fields. Please regenerate code with: flutter pub run build_runner build --delete-conflicting-outputs');
    }

    return await _apiService.changeKeystore(request);
  }

  Future<void> sendFCMToken({
    required RequestInfo requestInfo,
    required String fcmToken,
  }) async {
    final request = FcmTokenRequest(
      requestInfo: requestInfo,
      devicePushInfoToken: fcmToken,
    );

    // Fire and forget - no response handling needed
    try {
      await _apiService.sendFCMToken(request);
    } catch (e) {
      // Ignore errors for FCM token registration
      if (kDebugMode) {
        debugPrint('FCM token registration failed: $e');
      }
    }
  }

  Future<List<BankCard>> loadBankCards({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    final response = await _apiService.listBankCards(request);
    if (response.responseInfo.responseType == 0) {
      return response.bankCards ?? [];
    }
    throw Exception(
      response.responseInfo.responseMessage ??
          response.responseInfo.errorMessage ??
          'Failed to load bank cards',
    );
  }

  Future<List<BankAccount>> loadBankAccounts({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    final response = await _apiService.listBankAccounts(request);
    if (response.responseInfo.responseType == 0) {
      return response.bankAccounts ?? [];
    }
    throw Exception(
      response.responseInfo.responseMessage ??
          response.responseInfo.errorMessage ??
          'Failed to load bank accounts',
    );
  }

  Future<Map<String, dynamic>> getMobileUserData({
    required RequestInfo requestInfo,
  }) async {
    final request = MobileUserDataRequest(
      requestInfo: requestInfo,
    );

    final response = await _apiService.getMobileUserData(request.toJson());
    if (response.responseInfo.responseType == 0) {
      return response.data as Map<String, dynamic>? ?? {};
    }
    throw Exception(
      response.responseInfo.responseMessage ??
          response.responseInfo.errorMessage ??
          'Failed to get mobile user data',
    );
  }

  Future<void> sendOtp({
    required String phoneNumber,
    required String text,
    required int type,
    required String userId,
  }) async {
    final request = OtpRequest(
      phoneNumber: phoneNumber,
      text: text,
      type: type,
      userId: userId,
    );

    if (kDebugMode) {
      final requestJson = request.toJson();
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('=== Send OTP Request ===');
      debugPrint('URL: ${AppConstants.otpApiBaseUrl}${ApiEndpoints.sendOtp}');
      debugPrint(formattedJson);
      debugPrint('========================');
    }

    try {
      final response = await _otpDio.post(
        ApiEndpoints.sendOtp,
        data: request.toJson(),
        options: Options(
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'x-api-key': 'OTP_SUPER_SECRET_API_KEY_159357456258',
          },
        ),
      );
      
      // Parse response - OTP API returns different format: {isSuccess, data, errors, statusCode, message}
      if (response.data != null) {
        final responseData = response.data as Map<String, dynamic>;
        final isSuccess = responseData['isSuccess'] as bool? ?? false;
        final statusCode = responseData['statusCode'] as int?;
        final message = responseData['message'] as String?;
        final errors = responseData['errors'] as List<dynamic>?;
        
        if (kDebugMode) {
          debugPrint('=== Send OTP Response ===');
          debugPrint('isSuccess: $isSuccess');
          debugPrint('statusCode: $statusCode');
          debugPrint('message: $message');
          debugPrint('errors: $errors');
          debugPrint('========================');
        }
        
        // Check if request was successful
        if (!isSuccess || (statusCode != null && statusCode != 200)) {
          final errorMessage = errors?.isNotEmpty == true
              ? errors!.first.toString()
              : message ?? 'Failed to send OTP';
          throw Exception(errorMessage);
        }
        
        // Success - OTP sent
        if (kDebugMode) {
          debugPrint('OTP sent successfully: $message');
        }
      }
    } on DioException catch (e) {
      if (kDebugMode) {
        debugPrint('OTP API Error: ${e.message}');
        debugPrint('Response: ${e.response?.data}');
      }
      
      // Try to extract error message from response
      if (e.response?.data != null) {
        final responseData = e.response!.data as Map<String, dynamic>?;
        final message = responseData?['message'] as String?;
        final errors = responseData?['errors'] as List<dynamic>?;
        final errorMessage = errors?.isNotEmpty == true
            ? errors!.first.toString()
            : message ?? e.message ?? 'Failed to send OTP';
        throw Exception(errorMessage);
      }
      
      throw Exception(e.message ?? 'Failed to send OTP');
    }
  }

  Future<void> verifyOtp({
    required String otpCode,
    required String phoneNumber,
  }) async {
    final request = OtpVerifyRequest(
      otpCode: otpCode,
      phoneNumber: phoneNumber,
    );

    if (kDebugMode) {
      final requestJson = request.toJson();
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('=== Verify OTP Request ===');
      debugPrint('URL: ${AppConstants.otpApiBaseUrl}${ApiEndpoints.verifyOtp}');
      debugPrint(formattedJson);
      debugPrint('==========================');
    }

    try {
      final response = await _otpDio.post(
        ApiEndpoints.verifyOtp,
        data: request.toJson(),
        options: Options(
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'x-api-key': 'OTP_SUPER_SECRET_API_KEY_159357456258',
          },
        ),
      );
      
      // Parse response - OTP API returns different format: {isSuccess, data, errors, statusCode, message}
      if (response.data != null) {
        final responseData = response.data as Map<String, dynamic>;
        final isSuccess = responseData['isSuccess'] as bool? ?? false;
        final statusCode = responseData['statusCode'] as int?;
        final message = responseData['message'] as String?;
        final errors = responseData['errors'] as List<dynamic>?;
        
        if (kDebugMode) {
          debugPrint('=== Verify OTP Response ===');
          debugPrint('isSuccess: $isSuccess');
          debugPrint('statusCode: $statusCode');
          debugPrint('message: $message');
          debugPrint('errors: $errors');
          debugPrint('==========================');
        }
        
        // Check if request was successful
        if (!isSuccess || (statusCode != null && statusCode != 200)) {
          final errorMessage = errors?.isNotEmpty == true
              ? errors!.first.toString()
              : message ?? 'Failed to verify OTP';
          throw Exception(errorMessage);
        }
        
        // Success - OTP verified
        if (kDebugMode) {
          debugPrint('OTP verified successfully: $message');
        }
      }
    } on DioException catch (e) {
      if (kDebugMode) {
        debugPrint('OTP API Error: ${e.message}');
        debugPrint('Response: ${e.response?.data}');
      }
      
      // Try to extract error message from response
      if (e.response?.data != null) {
        final responseData = e.response!.data as Map<String, dynamic>?;
        final message = responseData?['message'] as String?;
        final errors = responseData?['errors'] as List<dynamic>?;
        final errorMessage = errors?.isNotEmpty == true
            ? errors!.first.toString()
            : message ?? e.message ?? 'Failed to verify OTP';
        throw Exception(errorMessage);
      }
      
      throw Exception(e.message ?? 'Failed to verify OTP');
    }
  }

  /// Verifies SIMA certificate with the backend API
  /// 
  /// [certificate] should be a base64-encoded string of the certificate bytes
  /// [pinCode] is the user's PIN code (FIN code)
  Future<void> verifySimaCertificate({
    required String certificate, // base64-encoded certificate
    required String pinCode,
  }) async {
    try {
      if (kDebugMode) {
        debugPrint('=== SIMA Certificate Verification ===');
        debugPrint('Endpoint: /api/sima/auth/verifycertificate');
        debugPrint('Certificate (base64) length: ${certificate.length}');
        debugPrint('PIN Code length: ${pinCode.length}');
      }

      final response = await _simaDio.post(
        '/api/sima/auth/verifycertificate',
        data: {
          'certificate': certificate, // base64-encoded certificate
          'pinCode': pinCode,
        },
        options: Options(
          headers: {
            'x-api-key': 'SIMA_SUPER_SECRET_API_KEY_12345879871',
          },
        ),
      );

      if (kDebugMode) {
        debugPrint('SIMA Certificate Verification Response:');
        debugPrint('Status Code: ${response.statusCode}');
        debugPrint('Response Data: ${response.data}');
        debugPrint('=====================================');
      }

      if (response.statusCode != 200) {
        throw Exception('SIMA certificate verification failed with status: ${response.statusCode}');
      }
    } catch (e) {
      if (kDebugMode) {
        debugPrint('SIMA Certificate Verification Error: $e');
      }
      throw Exception('Failed to verify SIMA certificate: ${e.toString()}');
    }
  }
}
