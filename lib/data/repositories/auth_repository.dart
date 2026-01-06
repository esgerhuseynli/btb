import 'dart:convert';
import 'package:flutter/foundation.dart';
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
import '../models/bank_cards_response.dart';
import '../models/bank_accounts_response.dart';

class AuthRepository {
  final ApiService _apiService;

  AuthRepository(this._apiService);

  Future<SignInResponse> signIn({
    required RequestInfo requestInfo,
    required int keystoreType,
    required int signInType,
  }) async {
    // MobileUser is already set in RequestInfo
    // MobileNumber and MobileNumberSecretCode should be null for regular sign-in
    final request = SignInRequest(
      requestInfo: requestInfo,
      keystoreType: keystoreType,
      signInType: signInType,
      mobileNumber: null,
      mobileNumberSecretCode: null,
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

  Future<ApiResponse<dynamic>> forgotPassword({
    required RequestInfo requestInfo,
    required Map<String, dynamic> forgotPasswordData,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
      ...forgotPasswordData,
    };

    return await _apiService.forgotPassword(request);
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
      final requestJson = request.toJson();
      final encoder = JsonEncoder.withIndent('  ');
      final formattedJson = encoder.convert(requestJson);
      debugPrint('=== Change Keystore Request ===');
      debugPrint(formattedJson);
      debugPrint('===============================');
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
    required int signInType,
    required String mobileNumber,
    required String mobileNumberSecretCode,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
      'SignInType': signInType,
      'MobileNumber': mobileNumber,
      'MobileNumberSecretCode': mobileNumberSecretCode,
    };

    final response = await _apiService.getMobileUserData(request);
    if (response.responseInfo.responseType == 0) {
      return response.data as Map<String, dynamic>? ?? {};
    }
    throw Exception(
      response.responseInfo.responseMessage ??
          response.responseInfo.errorMessage ??
          'Failed to get mobile user data',
    );
  }
}
