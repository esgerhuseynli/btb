import 'dart:convert';
import 'dart:typed_data';
import 'package:flutter/foundation.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../core/bloc/base_bloc.dart';
import '../../../core/constants/app_constants.dart';
import '../../../core/utils/app_utils.dart';
import '../../../core/utils/request_builder.dart';
import '../../../data/repositories/auth_repository.dart';
import '../../../data/models/mobile_user.dart';
import '../../../data/models/sign_up_request.dart';
import '../../../data/models/verify_code_request.dart';
import '../../../data/models/card_send_request.dart';
import '../../../data/models/card_send_response.dart';
import '../../../data/models/api_response.dart';
import '../../../data/models/request_info.dart';
import '../../../data/models/mobile_device_specifications.dart';
import '../../../data/models/bank_card.dart';
import '../../../data/models/bank_account.dart';
import 'auth_event.dart';
import 'auth_state.dart';

class AuthBloc extends BaseBloc<AuthEvent, AuthState> {
  final AuthRepository _authRepository;
  final RequestBuilder _requestBuilder;
  final FlutterSecureStorage _secureStorage;

  AuthBloc(
      this._authRepository,
      this._requestBuilder,
      this._secureStorage,
      ) : super(const AuthInitial()) {
    on<CheckAuthStatusEvent>(_onCheckAuthStatus);
    on<SignInEvent>(_onSignIn);
    on<SignOutEvent>(_onSignOut);
    on<SignUpEvent>(_onSignUp);
    on<VerifyCodeEvent>(_onVerifyCode);
    on<SendCardNumberEvent>(_onSendCardNumber);
    on<SendCardNumberForSignInEvent>(_onSendCardNumberForSignIn);
    on<SendCardNumberForCifEvent>(_onSendCardNumberForCif);
    on<ChangeKeystoreEvent>(_onChangeKeystore);
    on<SetupPinEvent>(_onSetupPin);
    on<VerifyPinEvent>(_onVerifyPin);
    on<VerifyBiometricEvent>(_onVerifyBiometric);
    on<ForgotPasswordEvent>(_onForgotPassword);
    on<ChangeForgotPasswordEvent>(_onChangeForgotPassword);
    on<SendOtpEvent>(_onSendOtp);
    on<VerifyOtpEvent>(_onVerifyOtp);
    on<SimaAuthenticateEvent>(_onSimaAuthenticate);
  }

  Future<void> _onCheckAuthStatus(
      CheckAuthStatusEvent event,
      Emitter<AuthState> emit,
      ) async {
    try {
      // Read all keys in parallel for faster performance
      final results = await Future.wait([
        _secureStorage.read(key: AppConstants.hasActiveSession),
        _secureStorage.read(key: AppConstants.pinHash),
        _secureStorage.read(key: AppConstants.username),
        _secureStorage.read(key: AppConstants.passwordHash),
        _secureStorage.read(key: AppConstants.signInType),
      ]);

      final hasActiveSession = results[0];
      final pinHash = results[1];
      final username = results[2];
      final passwordHash = results[3];
      final signInTypeStr = results[4];

      // Debug logging
      if (kDebugMode) {
        debugPrint('=== CheckAuthStatus Debug ===');
        debugPrint('hasActiveSession: $hasActiveSession');
        debugPrint('pinHash exists: ${pinHash != null && pinHash.isNotEmpty}');
        debugPrint('username: $username');
        debugPrint('passwordHash exists: ${passwordHash != null && passwordHash.isNotEmpty}');
        debugPrint('signInType: $signInTypeStr');
        debugPrint('============================');
      }

      if (hasActiveSession == 'true' &&
          pinHash != null &&
          pinHash.isNotEmpty &&
          username != null &&
          username.isNotEmpty &&
          passwordHash != null &&
          passwordHash.isNotEmpty &&
          signInTypeStr != null &&
          signInTypeStr.isNotEmpty) {
        // User has active session and PIN - navigate to PIN verification
        final signInType = int.parse(signInTypeStr);
        if (kDebugMode) {
          debugPrint('✅ User has active session - emitting PinVerificationRequired');
        }
        emit(PinVerificationRequired(
          username: username,
          passwordHash: passwordHash,
          signInType: signInType,
        ));
      } else {
        // No active session - show intro/sign-in
        if (kDebugMode) {
          debugPrint('❌ No active session or missing data - emitting AuthUnauthenticated');
        }
        emit(const AuthUnauthenticated());
      }
    } catch (e) {
      if (kDebugMode) {
        debugPrint('❌ CheckAuthStatus error: $e');
      }
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onSignIn(
      SignInEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      final isEmail = event.username.contains('@');
      final passwordHash = AppUtils.passwordHash(event.password);

      // Create MobileUser with username and passwordHash
      // For phone numbers: normalize to include +994 prefix
      // For emails: just remove spaces
      final username = isEmail
          ? event.username.replaceAll(' ', '')
          : AppUtils.normalizePhoneNumber(event.username);

      final mobileUser = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: null,
        saltSignature: null,
      );

      // Build RequestInfo with MobileUser set
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Determine sign-in type (1 for email, 2 for phone number)
      final signInType = isEmail
          ? AppConstants.signInUpTypeEmail
          : AppConstants.signInUpTypeNumber;

      // Step 1: Initial Sign-In (keystoreType=0)
      final response = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 0, // No keystore yet
        signInType: signInType, // 1 for email, 2 for phone number
      );

      // Debug logging
      if (kDebugMode) {
        debugPrint('=== Sign In Response ===');
        debugPrint('isSuccess: ${response.isSuccess}');
        debugPrint('responseType: ${response.responseInfo.responseType}');
        debugPrint('responseMessage: ${response.responseInfo.responseMessage}');
        debugPrint('errorMessage: ${response.responseInfo.errorMessage}');
        debugPrint('signInType: $signInType (${signInType == AppConstants.signInUpTypeNumber ? "phone" : "email"})');
        debugPrint('username: $username');
        debugPrint('========================');
      }

      // Check for responseType == 3 first (user needs to sign up)
      // Show popup and navigate to sign-up page when user closes it
      if (response.responseInfo.responseType == 3) {
        await _secureStorage.write(
          key: AppConstants.username,
          value: event.username.replaceAll(' ', ''),
        );
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: passwordHash,
        );
        // User registered but current device needs to be registered
        // Show popup and navigate to sign-up screen when user closes it
        emit(DeviceNeedsRegistration(
          username: event.username,
          isEmail: isEmail,
        ));
      } else if (response.isSuccess && response.responseInfo.responseType == 0) {
        // Password is correct - save credentials
        // Save session key if available
        if (response.sessionKey != null && response.sessionKey!.isNotEmpty) {
          await _secureStorage.write(
            key: AppConstants.sessionKey,
            value: response.sessionKey!,
          );
        }

        // IMPORTANT: Save the ORIGINAL password hash from user input
        // This is the password hash that will be used for PIN verification later
        await _secureStorage.write(
          key: AppConstants.username,
          value: username,
        );
        
        // Save ORIGINAL password hash from user input (BEFORE ChangeKeystore)
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: passwordHash, // Save ORIGINAL password hash from user input
        );
        
        // Verify it was saved correctly
        final savedPasswordHash = await _secureStorage.read(key: AppConstants.passwordHash);
        if (kDebugMode) {
          debugPrint('=== SAVING PASSWORD HASH ===');
          debugPrint('Password hash from user input: ${passwordHash.substring(0, 20)}...${passwordHash.substring(passwordHash.length - 10)}');
          debugPrint('Saving to secure storage...');
          debugPrint('Verification - Read back from storage: ${savedPasswordHash?.substring(0, 20) ?? "NULL"}...${savedPasswordHash?.substring(savedPasswordHash.length - 10) ?? ""}');
          debugPrint('Match: ${savedPasswordHash == passwordHash}');
          debugPrint('============================');
        }

        // Store sign-in type (already determined above)
        await _secureStorage.write(
          key: AppConstants.signInType,
          value: signInType.toString(),
        );

        // Step 2: ChangeKeystore - Set up device keystore
        // Copy session key from sign-in response and use it ONLY as saltSignature
        final sessionKey = response.sessionKey!;
        final updatedMobileUser = MobileUser(
          username: username,
          passwordHash: passwordHash, // Original password hash for ChangeKeystore
          sessionKey: null, // Don't include sessionKey in request body
          saltSignature: sessionKey, // Use copied session key ONLY as saltSignature
        );
        
        final updatedRequestInfo = await _requestBuilder.buildRequestInfo(
          mobileUser: updatedMobileUser,
        );

        // Call ChangeKeystore API
        final changeKeystoreResponse = await _authRepository.changeKeystore(
          requestInfo: updatedRequestInfo,
          keystoreType: 1, // Set up keystore
          deviceSpecs: MobileDeviceSpecifications(
            nfc: 'Available',
            faceID: 'Available',
            touchID: 'NotAvailable',
          ),
        );

        if (changeKeystoreResponse.responseInfo.responseType == 0 && 
            changeKeystoreResponse.responseInfo.isSuccess && 
            changeKeystoreResponse.passwordHash != null) {
          // Get new password hash from ChangeKeystore (used for final sign-in, not saved)
          final newPasswordHash = changeKeystoreResponse.passwordHash!;
          
          if (kDebugMode) {
            debugPrint('=== Password Hash Info ===');
            debugPrint('Original hash (from user input - SAVED): ${passwordHash.substring(0, 20)}...');
            debugPrint('New hash (from ChangeKeystore - for final sign-in only): ${newPasswordHash.substring(0, 20)}...');
            debugPrint('Original password hash saved to secure storage for PIN verification');
            debugPrint('==========================');
          }
          
          // Check signInActionCode to determine next step
          // signInActionCode == 1: OTP verification required
          // signInActionCode == 0 or other: Proceed to PIN setup
          if (response.signInActionCode == 1 && signInType == AppConstants.signInUpTypeNumber) {
            // OTP verification required for phone number sign-in
            if (kDebugMode) {
              debugPrint('Sign-in successful - OTP verification required (signInActionCode: 1)');
              debugPrint('Sending OTP to: $username');
            }
            
            // Save new password hash from ChangeKeystore temporarily (will be used after OTP verification)
            await _secureStorage.write(
              key: AppConstants.tempPasswordHashForPinSetup,
              value: newPasswordHash,
            );
            
            // Send OTP for phone number verification
            try {
              await _authRepository.sendOtp(
                phoneNumber: username, // Already normalized with +994 prefix
                text: 'Sign in', // OTP text for sign-in
                type: 1, // OTP type for sign-in/verification
                userId: username, // Use phone number as userId
              );
              
              // Success - OTP sent, navigate to OTP verification screen
              emit(OtpSent(
                phoneNumber: username,
                remainingMinutes: 5,
                remainingSeconds: 0,
                canResend: false,
              ));
            } catch (e) {
              if (kDebugMode) {
                debugPrint('Failed to send OTP: ${e.toString()}');
              }
              // Clean up temporary password hash on error
              await _secureStorage.delete(key: AppConstants.tempPasswordHashForPinSetup);
              emit(AuthError('Failed to send OTP: ${e.toString()}'));
            }
          } else {
            // No OTP required - proceed directly to PIN setup
            if (kDebugMode) {
              debugPrint('Sign-in successful - proceeding to PIN setup (signInActionCode: ${response.signInActionCode})');
            }
            
            // Navigate directly to PIN Setup Screen
            // Use new password hash from ChangeKeystore for final sign-in after PIN setup
            emit(PinSetupRequired(
              username: username,
              passwordHash: newPasswordHash, // New hash for final sign-in after PIN setup
              signInType: signInType,
              isComingFromSignIn: true,
            ));
          }
        } else {
          emit(AuthError(
            changeKeystoreResponse.responseInfo.errorMessage ??
                changeKeystoreResponse.responseInfo.responseMessage ??
                'Change keystore failed',
          ));
        }
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Giriş uğursuz oldu',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onSignOut(
      SignOutEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Step 1: Get stored credentials for sign-in
      final username =
          await _secureStorage.read(key: AppConstants.username) ?? '';
      final passwordHash =
          await _secureStorage.read(key: AppConstants.passwordHash) ?? '';

      if (username.isEmpty || passwordHash.isEmpty) {
        // No stored credentials, just perform cleanup
        await _performSignOutActions();
        emit(const AuthUnauthenticated());
        return;
      }

      // Remove spaces from username
      final cleanUsername = username.replaceAll(' ', '');

      // Create MobileUser for sign-in
      final mobileUser = MobileUser(
        username: cleanUsername,
        passwordHash: passwordHash,
        sessionKey: null,
        saltSignature: null,
      );

      // Build RequestInfo with MobileUser
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Step 2: Sign-In first to get fresh sessionKey
      try {
        final signInResponse = await _authRepository.signIn(
          requestInfo: requestInfo,
          keystoreType: 1, // 1 because keystore is already set up
          signInType: 1, // Always 1 for sign-in
        );

        if (signInResponse.isSuccess &&
            signInResponse.responseInfo.responseType == 0) {
          // Success - got new sessionKey
          if (signInResponse.sessionKey != null) {
            await _secureStorage.write(
              key: AppConstants.sessionKey,
              value: signInResponse.sessionKey!,
            );
          }

          // Step 3: Call SignOut API
          final updatedRequestInfo = await _requestBuilder.buildRequestInfo(
            mobileUser: mobileUser,
          );

          try {
            await _authRepository.signOut(requestInfo: updatedRequestInfo);
          } catch (e) {
            // SignOut API error is ignored - proceed with cleanup
            debugPrint('SignOut API error: $e');
          }

          // Step 4: Perform cleanup
          await _performSignOutActions();
          emit(const AuthUnauthenticated());
        } else if (signInResponse.responseInfo.responseType == 2) {
          // Keystore incident - report it
          // try {
          //   final incidentRequestInfo =
          //       await _requestBuilder.buildRequestInfo();
          //   await _authRepository.reportKeystoreIncident(
          //     requestInfo: incidentRequestInfo,
          //     incidentType: 1, // 1 = "OpenFaultAttempt"
          //     incidentCount: 0,
          //   );
          // } catch (e) {
          //   // Ignore keystore incident error
          //   debugPrint('Keystore incident error: $e');
          // }

          // Still proceed with cleanup
          await _performSignOutActions();
          emit(const AuthUnauthenticated());
        } else {
          // Other error - still proceed with cleanup
          await _performSignOutActions();
          emit(const AuthUnauthenticated());
        }
      } catch (e) {
        // Sign-in error - still proceed with cleanup
        debugPrint('Sign-in for logout failed: $e');
        await _performSignOutActions();
        emit(const AuthUnauthenticated());
      }
    } catch (e) {
      // Any other error - still proceed with cleanup
      debugPrint('Logout error: $e');
      await _performSignOutActions();
      emit(const AuthUnauthenticated());
    }
  }

  Future<void> _performSignOutActions() async {
    // Clear all SharedPreferences except FCM token
    final prefs = await SharedPreferences.getInstance();
    final allKeys = prefs.getKeys();

    for (String key in allKeys) {
      if (key != AppConstants.fcmNotificationToken) {
        await prefs.remove(key);
      }
    }

    // Clear secure storage (except FCM token if stored there)
    final allSecureKeys = await _secureStorage.readAll();
    for (String key in allSecureKeys.keys) {
      if (key != AppConstants.fcmNotificationToken) {
        await _secureStorage.delete(key: key);
      }
    }

    // Note: In Flutter, notifications are typically handled by the app
    // Cancel all notifications if using flutter_local_notifications
    // await flutterLocalNotificationsPlugin.cancelAll();
  }

  Future<void> _onSignUp(
      SignUpEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Create MobileUser with phone/email and password
      final username = event.usernameType == AppConstants.signInUpTypeEmail
          ? event.phoneNumber.replaceAll(' ', '')
          : AppUtils.normalizePhoneNumber(event.phoneNumber);
      final passwordHash = AppUtils.passwordHash(event.password);

      final mobileUser = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: null,
        saltSignature: null,
      );

      // Build RequestInfo with MobileUser
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
        language: 2, // Default to English for sign-up
      );

      // Create SignUpRequest
      final signUpRequest = SignUpRequest(
        requestInfo: requestInfo,
        usernameType: event.usernameType,
        signUpType: event.signUpType,
        verificationCode: event.verificationCode,
        pan: event.pan,
        customerNumber: event.customerNumber,
        customerBirthdate: event.customerBirthdate,
        mobileNumber: event.mobileNumber,
        mobileNumberSecretCode: event.mobileNumberSecretCode,
      );

      final response = await _authRepository.signUp(request: signUpRequest);

      if (response.isSuccess && response.responseInfo.responseType == 0 && response.responseInfo.errorCode == 0) {
        // Save username and password hash for later use
        await _secureStorage.write(
          key: AppConstants.username,
          value: username,
        );
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: passwordHash,
        );
        await _secureStorage.write(
          key: AppConstants.signInType,
          value: event.usernameType.toString(),
        );

        // Sign-In to get session key
        final signInRequestInfo = await _requestBuilder.buildRequestInfo(
          mobileUser: mobileUser,
        );

        final signInResponse = await _authRepository.signIn(
          requestInfo: signInRequestInfo,
          keystoreType: 1, // Keystore is set up
          signInType: event.usernameType, // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL
        );

        if (signInResponse.isSuccess && signInResponse.responseInfo.responseType == 0) {
          final sessionKey = signInResponse.sessionKey!;
          
          // Save session key
          await _secureStorage.write(
            key: AppConstants.sessionKey,
            value: sessionKey,
          );

          // Navigate directly to PIN Setup Screen (skip ChangeKeystore)
          emit(PinSetupRequired(
            username: username,
            passwordHash: passwordHash,
            signInType: event.usernameType,
            isComingFromSignIn: false, // Coming from sign-up
          ));
        } else {
          emit(AuthError(
            signInResponse.responseInfo.responseMessage ??
                signInResponse.responseInfo.errorMessage ??
                'Sign-in after sign-up failed',
          ));
        }
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Qeydiyyat uğursuz oldu',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onVerifyCode(
      VerifyCodeEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: await _requestBuilder.getStoredMobileUser(),
      );

      // Get stored CIF and birthdate for CIF sign-up type
      String? customerNumber;
      String? customerBirthdate;
      String? pan;

      if (event.requestType == AppConstants.signUpTypeCif) {
        final prefs = await SharedPreferences.getInstance();
        customerNumber = prefs.getString('signUpCif');
        customerBirthdate = prefs.getString('signUpDateOfBirth');
      } else if (event.requestType == AppConstants.signUpTypePan) {
        final prefs = await SharedPreferences.getInstance();
        pan = prefs.getString('signUpPan');
      }

      // Build VerifyCodeRequest based on signUpType
      final verifyCodeRequest = VerifyCodeRequest(
        requestInfo: requestInfo,
        signUpType: event.requestType,
        verificationCode: event.verificationCode,
        pan: pan,
        customerNumber: customerNumber,
        customerBirthdate: customerBirthdate,
      );

      final response =
      await _authRepository.verifyCode(request: verifyCodeRequest);

      if (response.isSuccess) {
        // Check mobileUserSignUpStatus
        // 0 = need to complete sign-up (navigate to sign-up screen)
        // 1 = sign-up already complete (navigate to sign-in)
        // 2 = unknown status
        if (response.mobileUserSignUpStatus == 0) {
          emit(CodeVerified(
            phone: event.phone,
            email: event.email,
          ));
        } else if (response.mobileUserSignUpStatus == 1) {
          // Sign-up already complete, navigate to sign-in
          emit(const AuthUnauthenticated());
        } else {
          emit(AuthError('Naməlum xəta baş verdi'));
        }
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Kod yanlışdır',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onSendCardNumber(
      SendCardNumberEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      final response =
      await _authRepository.sendCardNumber(request: event.request);

      if (response.isSuccess) {
        emit(CodeSent(
          phone: response.mobileNumber,
          email: response.email,
        ));
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Xəta baş verdi',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onSendCardNumberForSignIn(
      SendCardNumberForSignInEvent event,
      Emitter<AuthState> emit,
      ) async {
    // When coming from sign-in, we need to show dialog first
    // Then user needs to choose Card/CIF sign-up type
    // For now, just emit state to show dialog and navigate to sign-up types
    // The actual CardSendRequest will be sent from Card/CIF screens
    emit(const DeviceNeedsRegistrationDialog());
  }

  Future<void> _onSendCardNumberForCif(
      SendCardNumberForCifEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: await _requestBuilder.getStoredMobileUser(),
      );

      // Android sends empty strings for unused fields
      // For CIF: Pan="", CustomerNumber=cif, CustomerBirthdate=date, MobileNumber="", MobileNumberSecretCode=""
      final cardSendRequest = CardSendRequest(
        requestInfo: requestInfo,
        signUpType: AppConstants.signUpTypeCif,
        pan: '',
        // Empty string for CIF sign-up
        customerNumber: event.cif,
        customerBirthdate: event.birthdate,
        mobileNumber: '',
        // Empty string for CIF sign-up
        mobileNumberSecretCode: '', // Empty string for CIF sign-up
      );

      final response =
      await _authRepository.sendCardNumber(request: cardSendRequest);

      if (response.isSuccess) {
        emit(CodeSent(
          phone: response.mobileNumber,
          email: response.email,
        ));
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Xəta baş verdi',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onChangeKeystore(
      ChangeKeystoreEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Get stored username and password hash
      final username = await _secureStorage.read(key: AppConstants.username) ?? '';
      final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash) ?? '';

      if (username.isEmpty || passwordHash.isEmpty) {
        emit(const AuthError('Missing credentials'));
        return;
      }

      // Create MobileUser with saltSignature (sessionKey)
      // Use copied session key ONLY as saltSignature, not in sessionKey field
      final mobileUser = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: null, // Don't include sessionKey in request body
        saltSignature: event.sessionKey, // Use copied session key ONLY as saltSignature
      );

      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Call ChangeKeystore API
      final changeKeystoreResponse = await _authRepository.changeKeystore(
        requestInfo: requestInfo,
        keystoreType: 1, // Set up keystore
        deviceSpecs: MobileDeviceSpecifications(
          nfc: 'Available',
          faceID: 'Available',
          touchID: 'NotAvailable',
        ),
      );

      if (changeKeystoreResponse.responseInfo.responseType == 0 && 
          changeKeystoreResponse.responseInfo.isSuccess && 
          changeKeystoreResponse.passwordHash != null) {
        // Navigate to PIN Setup Screen
        emit(PinSetupRequired(
          username: username,
          passwordHash: changeKeystoreResponse.passwordHash,
          signInType: event.signInType,
          isComingFromSignIn: true,
        ));
      } else {
        emit(AuthError(
          changeKeystoreResponse.responseInfo.responseMessage ??
              'Keystore setup failed',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onSetupPin(
      SetupPinEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Create MobileUser with new password hash from ChangeKeystore
      final mobileUser = MobileUser(
        username: event.username,
        passwordHash: event.passwordHash, // New hash from ChangeKeystore
        sessionKey: null,
        saltSignature: null,
      );

      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Step 1: Final Sign-In with keystoreType=1
      final signInResponse = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 1, // Keystore is set up
        signInType: event.signInType, // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL
      );

      if (signInResponse.isSuccess && signInResponse.responseInfo.responseType == 0) {
        // Save all session data
        final pinHash = AppUtils.passwordHash(event.pin);

        await _secureStorage.write(
          key: AppConstants.pinHash,
          value: pinHash,
        );
        await _secureStorage.write(
          key: AppConstants.username,
          value: event.username,
        );
        // IMPORTANT: Do NOT overwrite passwordHash here
        // The original password hash (from user input) was already saved in _onSignIn
        // event.passwordHash is the NEW hash from ChangeKeystore, which should NOT be saved
        // We keep the original password hash for PIN verification
        await _secureStorage.write(
          key: AppConstants.sessionKey,
          value: signInResponse.sessionKey!,
        );
        await _secureStorage.write(
          key: AppConstants.hasActiveSession,
          value: 'true',
        );
        await _secureStorage.write(
          key: AppConstants.signInType,
          value: event.signInType.toString(),
        );

        // Update requestInfo with new session key
        final updatedMobileUser = MobileUser(
          username: event.username,
          passwordHash: event.passwordHash,
          sessionKey: signInResponse.sessionKey,
          saltSignature: signInResponse.sessionKey,
        );

        final updatedRequestInfo = await _requestBuilder.buildRequestInfo(
          mobileUser: updatedMobileUser,
        );

        // Step 1: Send FCM Token (ChangeDevicePushInfoToken) - fire and forget
        try {
          final prefs = await SharedPreferences.getInstance();
          final fcmToken = prefs.getString(AppConstants.fcmNotificationToken);
          if (fcmToken != null && fcmToken.isNotEmpty) {
            await _authRepository.sendFCMToken(
              requestInfo: updatedRequestInfo,
              fcmToken: fcmToken,
            );
          }
        } catch (e) {
          debugPrint('Failed to send FCM token: $e');
          // Ignore FCM token errors
        }

        // Step 2: Load Bank Cards (ListBankCards)
        List<BankCard> bankCards = [];
        try {
          bankCards = await _authRepository.loadBankCards(
            requestInfo: updatedRequestInfo,
          );
        } catch (e) {
          debugPrint('Failed to load bank cards: $e');
          // Continue even if bank cards fail
        }

        // Step 3: Load Bank Accounts (ListBankAccounts)
        List<BankAccount> bankAccounts = [];
        try {
          bankAccounts = await _authRepository.loadBankAccounts(
            requestInfo: updatedRequestInfo,
          );
        } catch (e) {
          debugPrint('Failed to load bank accounts: $e');
          // Continue even if bank accounts fail
        }

        // Step 4: Get Mobile User Data (GetMobileUserData)
        // Use username as MobileNumber and passwordHash as MobileNumberSecretCode
        try {
          final mobileUserData = await _authRepository.getMobileUserData(
            requestInfo: updatedRequestInfo,
          );

          // Store customer name if available
          if (mobileUserData['mobileUserData'] != null) {
            final userData = mobileUserData['mobileUserData'] as Map<String, dynamic>;
            final customerName = userData['customerName'] as String?;
            if (customerName != null && customerName.isNotEmpty) {
              await _secureStorage.write(
                key: AppConstants.customerName,
                value: customerName,
              );
            }
          }
        } catch (e) {
          debugPrint('Failed to get mobile user data: $e');
          // Continue even if user data fails
        }

        // Now emit AuthAuthenticated - user is fully authenticated
        emit(const AuthAuthenticated());
      } else {
        // Handle keystore incident (responseType == 2)
        if (signInResponse.responseInfo.responseType == 2) {
          try {
            final incidentRequestInfo = await _requestBuilder.buildRequestInfo(
              mobileUser: mobileUser,
            );
            await _authRepository.reportKeystoreIncident(
              requestInfo: incidentRequestInfo,
              incidentType: 1, // OpenFaultAttempt
              incidentCount: 0,
            );
          } catch (e) {
            debugPrint('Failed to report keystore incident: $e');
          }
        }

        emit(AuthError(
          signInResponse.responseInfo.responseMessage ??
              signInResponse.responseInfo.errorMessage ??
              'Giriş uğursuz oldu',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onVerifyPin(
      VerifyPinEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Get stored credentials
      final storedPinHash = await _secureStorage.read(key: AppConstants.pinHash);
      final username = await _secureStorage.read(key: AppConstants.username);
      final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      final signInTypeStr = await _secureStorage.read(key: AppConstants.signInType);

      if (kDebugMode) {
        debugPrint('=== PIN VERIFICATION - READING STORED DATA ===');
        debugPrint('Username: $username');
        debugPrint('PasswordHash from storage: ${passwordHash?.substring(0, 20) ?? "NULL"}...${passwordHash?.substring(passwordHash.length - 10) ?? ""}');
        debugPrint('SignInType: $signInTypeStr');
        debugPrint('PIN Hash exists: ${storedPinHash != null}');
        debugPrint('==============================================');
      }

      if (storedPinHash == null ||
          username == null ||
          passwordHash == null ||
          signInTypeStr == null) {
        emit(const AuthError('Missing stored credentials'));
        return;
      }

      // Verify PIN
      final enteredPinHash = AppUtils.passwordHash(event.pin);
      if (enteredPinHash != storedPinHash) {
        emit(const AuthError('Yanlış PIN kodu'));
        return;
      }

      // PIN is correct - proceed with sign in
      await _performSignInAfterVerification(username, passwordHash, signInTypeStr, emit);
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _onVerifyBiometric(
      VerifyBiometricEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Get stored credentials
      final username = await _secureStorage.read(key: AppConstants.username);
      final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      final signInTypeStr = await _secureStorage.read(key: AppConstants.signInType);

      if (username == null ||
          passwordHash == null ||
          signInTypeStr == null) {
        emit(const AuthError('Missing stored credentials'));
        return;
      }

      // Biometric authentication successful - proceed with sign in (skip PIN verification)
      await _performSignInAfterVerification(username, passwordHash, signInTypeStr, emit);
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }

  Future<void> _performSignInAfterVerification(
      String username,
      String passwordHash,
      String signInTypeStr,
      Emitter<AuthState> emit,
      ) async {
    try {
      // Sign in with keystoreType=1 (keystore is already set up after PIN setup)
      final signInType = int.parse(signInTypeStr);
      final mobileUser = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: null,
        saltSignature: null,
      );

      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Sign in with keystoreType=0 for PIN verification
      // Note: Even though keystore is set up, API expects keystoreType=0 for SignInNew after PIN verification
      if (kDebugMode) {
        debugPrint('=== PIN Verification SignIn ===');
        debugPrint('KeystoreType: 0 (required by API for PIN verification)');
        debugPrint('SignInType: $signInType');
        debugPrint('==============================');
      }
      
      final signInResponse = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 0, // API expects 0 for SignInNew after PIN verification
        signInType: signInType,
      );

      if (signInResponse.isSuccess && signInResponse.responseInfo.responseType == 0) {
        // Update session key
        await _secureStorage.write(
          key: AppConstants.sessionKey,
          value: signInResponse.sessionKey!,
        );
        await _secureStorage.write(
          key: AppConstants.hasActiveSession,
          value: 'true',
        );

        // Update requestInfo with new session key
        final updatedMobileUser = MobileUser(
          username: username,
          passwordHash: passwordHash,
          sessionKey: signInResponse.sessionKey,
          saltSignature: signInResponse.sessionKey,
        );

        final updatedRequestInfo = await _requestBuilder.buildRequestInfo(
          mobileUser: updatedMobileUser,
        );

        // Step 1: Send FCM Token (ChangeDevicePushInfoToken) - fire and forget
        try {
          final prefs = await SharedPreferences.getInstance();
          final fcmToken = prefs.getString(AppConstants.fcmNotificationToken);
          if (fcmToken != null && fcmToken.isNotEmpty) {
            await _authRepository.sendFCMToken(
              requestInfo: updatedRequestInfo,
              fcmToken: fcmToken,
            );
          }
        } catch (e) {
          debugPrint('Failed to send FCM token: $e');
        }

        // Step 2: Load Bank Cards (ListBankCards)
        try {
          await _authRepository.loadBankCards(
            requestInfo: updatedRequestInfo,
          );
        } catch (e) {
          debugPrint('Failed to load bank cards: $e');
        }

        // Step 3: Load Bank Accounts (ListBankAccounts)
        try {
          await _authRepository.loadBankAccounts(
            requestInfo: updatedRequestInfo,
          );
        } catch (e) {
          debugPrint('Failed to load bank accounts: $e');
        }

        // Step 4: Get Mobile User Data (GetMobileUserData)
        try {
          final mobileUserData = await _authRepository.getMobileUserData(
            requestInfo: updatedRequestInfo,
          );

          // Store customer name if available
          if (mobileUserData['mobileUserData'] != null) {
            final userData = mobileUserData['mobileUserData'] as Map<String, dynamic>;
            final customerName = userData['customerName'] as String?;
            if (customerName != null && customerName.isNotEmpty) {
              await _secureStorage.write(
                key: AppConstants.customerName,
                value: customerName,
              );
            }
          }
        } catch (e) {
          debugPrint('Failed to get mobile user data: $e');
        }

        // User is authenticated - navigate to home
        emit(const AuthAuthenticated());
      } else {
        // Handle keystore incident (responseType == 2)
        if (signInResponse.responseInfo.responseType == 2) {
          try {
            final incidentRequestInfo = await _requestBuilder.buildRequestInfo(
              mobileUser: mobileUser,
            );
            await _authRepository.reportKeystoreIncident(
              requestInfo: incidentRequestInfo,
              incidentType: 1, // OpenFaultAttempt
              incidentCount: 0,
            );
          } catch (e) {
            debugPrint('Failed to report keystore incident: $e');
          }
        }

        emit(AuthError(
          signInResponse.responseInfo.responseMessage ??
              signInResponse.responseInfo.errorMessage ??
              'Giriş uğursuz oldu',
        ));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }


  Future<void> _onForgotPassword(
      ForgotPasswordEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Normalize username (remove spaces, handle phone numbers)
      final isEmail = event.username.contains('@');
      final username = isEmail
          ? event.username.replaceAll(' ', '')
          : AppUtils.normalizePhoneNumber(event.username);

      // Create MobileUser with username, pinCode (FIN code), phoneNumber, and birthDate
      // for the new forgot password API
      final mobileUser = MobileUser(
        username: username,
        passwordHash: null,
        sessionKey: null,
        saltSignature: null,
        pinCode: event.finCode,
        phoneNumber: isEmail ? null : username,
        birthDate: event.birthDate,
      );

      // Build RequestInfo with MobileUser containing username
      final requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Call new forgot password API
      // New API structure: requestInfo (camelCase), requestParametersValidationMessage, requestParametersValidated
      final CardSendResponse response = await _authRepository.forgotPassword(
        requestInfo: requestInfo,
        requestParametersValidationMessage: '',
        requestParametersValidated: true,
      );

      if (response.isSuccess && response.responseInfo.responseType == 0) {
        // Success - send OTP to user's phone number
        // Use the normalized username (phone number) for sending OTP
        if (!isEmail && username.isNotEmpty) {
          try {
            await _authRepository.sendOtp(
              phoneNumber: username, // Already normalized with +994 prefix
              text: 'Password reset', // OTP text for password reset
              type: 2, // OTP type for password reset
              userId: username, // Use phone number as userId
            );

            // Store username and finCode temporarily for use in change password API
            await _secureStorage.write(
              key: AppConstants.tempForgotPasswordUsername,
              value: username,
            );
            await _secureStorage.write(
              key: AppConstants.tempForgotPasswordFinCode,
              value: event.finCode,
            );

            // OTP sent successfully - emit ForgotPasswordSuccess state
            emit(ForgotPasswordSuccess(
              mobileNumber: username, // Use the normalized phone number
            ));
          } catch (e) {
            emit(AuthError('Failed to send OTP: ${e.toString()}'));
          }
        } else {
          emit(AuthError('Phone number is required for password reset'));
        }
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Password recovery failed',
        ));
      }
    } catch (e) {
      emit(AuthError('Failed to recover password: ${e.toString()}'));
    }
  }

  Future<void> _onChangeForgotPassword(
      ChangeForgotPasswordEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Retrieve stored username and finCode from forgot password flow
      final username = await _secureStorage.read(key: AppConstants.tempForgotPasswordUsername) ?? '';
      final finCode = await _secureStorage.read(key: AppConstants.tempForgotPasswordFinCode) ?? '';

      if (username.isEmpty || finCode.isEmpty) {
        emit(const AuthError('Missing required information for password change'));
        return;
      }

      // Create MobileUser with username only (other fields empty as per API spec)
      final mobileUser = MobileUser(
        username: username,
        passwordHash: '',
        sessionKey: '',
        saltSignature: '',
        pinCode: null,
        phoneNumber: null,
        birthDate: null,
      );

      // Build RequestInfo with MobileUser
      final RequestInfo requestInfo = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser,
      );

      // Hash the new password
      final String newPasswordHash = AppUtils.passwordHash(event.newPassword);

      // Call change forgot password API
      final ApiResponse<dynamic> response = await _authRepository.changeForgotPassword(
        requestInfo: requestInfo,
        phoneNumber: username,
        pinCode: finCode,
        newPasswordHash: newPasswordHash,
      );

      if (response.responseInfo.responseType == 0) {
        // Success - password changed
        // Emit success with phone number before cleaning up
        emit(PasswordChangedSuccess(phoneNumber: username));
        
        // Clean up temporary storage after emitting state
        await _secureStorage.delete(key: AppConstants.tempForgotPasswordUsername);
        await _secureStorage.delete(key: AppConstants.tempForgotPasswordFinCode);
      } else {
        emit(AuthError(
          response.responseInfo.responseMessage ??
              response.responseInfo.errorMessage ??
              'Failed to change password',
        ));
      }
    } catch (e) {
      emit(AuthError('Failed to change password: ${e.toString()}'));
    }
  }

  Future<void> _onSendOtp(
      SendOtpEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Normalize phone number to full format (+994XXXXXXXXX)
      final normalizedPhone = AppUtils.normalizePhoneNumber(event.phoneNumber);
      
      await _authRepository.sendOtp(
        phoneNumber: normalizedPhone,
        text: event.text,
        type: event.type,
        userId: event.userId,
      );

      // Success - OTP sent
      emit(OtpSent(
        phoneNumber: normalizedPhone,
        remainingMinutes: 5,
        remainingSeconds: 0,
        canResend: false,
      ));
    } catch (e) {
      emit(AuthError('Failed to send OTP: ${e.toString()}'));
    }
  }

  Future<void> _onVerifyOtp(
      VerifyOtpEvent event,
      Emitter<AuthState> emit,
      ) async {
    emit(const AuthLoading());
    try {
      // Normalize phone number to full format (+994XXXXXXXXX)
      final normalizedPhone = AppUtils.normalizePhoneNumber(event.phoneNumber);
      
      await _authRepository.verifyOtp(
        otpCode: event.otpCode,
        phoneNumber: normalizedPhone,
      );

      // Success - OTP verified
      emit(const OtpVerified());
      
      // For forgot password flow, emit state with verification code to navigate to new password screen
      if (event.flowType == OtpFlowType.forgotPassword) {
        emit(OtpVerifiedForForgotPassword(
          verificationCode: event.otpCode,
          phone: normalizedPhone,
        ));
      } else if (event.flowType == OtpFlowType.regularSignIn) {
        // Get stored credentials from sign-in
        final username = await _secureStorage.read(key: AppConstants.username) ?? '';
        final signInTypeStr = await _secureStorage.read(key: AppConstants.signInType) ?? '2';
        final signInType = int.tryParse(signInTypeStr) ?? AppConstants.signInUpTypeNumber;
        
        // Get new password hash from ChangeKeystore (saved temporarily before OTP was sent)
        // If not found, fall back to original password hash
        final newPasswordHash = await _secureStorage.read(key: AppConstants.tempPasswordHashForPinSetup);
        final passwordHash = newPasswordHash ?? await _secureStorage.read(key: AppConstants.passwordHash) ?? '';
        
        if (username.isNotEmpty && passwordHash.isNotEmpty) {
          // Clean up temporary password hash
          if (newPasswordHash != null) {
            await _secureStorage.delete(key: AppConstants.tempPasswordHashForPinSetup);
          }
          
          // Navigate to PIN setup with new password hash from ChangeKeystore
          emit(PinSetupRequired(
            username: username,
            passwordHash: passwordHash, // Use new hash from ChangeKeystore if available
            signInType: signInType,
            isComingFromSignIn: true,
          ));
        } else {
          emit(const AuthError('Missing credentials for PIN setup'));
        }
      } else if (event.flowType == OtpFlowType.simaSignIn) {
        // For SIMA sign-in flow, OtpVerified state will be handled by the screen
        // The screen will navigate to FIN code screen
      }
      // For other flow types, OtpVerified state will be handled by the screen
    } catch (e) {
      emit(AuthError('Failed to verify OTP: ${e.toString()}'));
    }
  }

  Future<void> _onSimaAuthenticate(
      SimaAuthenticateEvent event,
      Emitter<AuthState> emit,
      ) async {
    if (kDebugMode) {
      debugPrint('=== _onSimaAuthenticate CALLED ===');
      debugPrint('Event received at: ${DateTime.now()}');
      debugPrint('Phone Number: ${event.phoneNumber}');
      debugPrint('FIN Code: ${event.finCode}');
      debugPrint('Certificate bytes: ${event.certificateBytes != null ? "${event.certificateBytes!.length} bytes" : "NULL"}');
      debugPrint('Signature bytes: ${event.signatureBytes != null ? "${event.signatureBytes!.length} bytes" : "NULL"}');
    }
    
    emit(const AuthLoading());
    try {
      // Normalize phone number to username format (9 digits)
      final username = AppUtils.normalizePhoneNumber(event.phoneNumber)
          .replaceAll('+994', '')
          .replaceAll(RegExp(r'\D'), '');
      
      if (kDebugMode) {
        debugPrint('Normalized username: $username (length: ${username.length})');
      }
      
      if (username.length != 9) {
        if (kDebugMode) {
          debugPrint('ERROR: Invalid phone number format - username length is ${username.length}, expected 9');
        }
        emit(const AuthError('Invalid phone number format'));
        return;
      }

      // Verify SIMA certificate with backend API
      if (event.certificateBytes == null || event.certificateBytes!.isEmpty) {
        if (kDebugMode) {
          debugPrint('ERROR: SIMA certificate is missing or empty');
        }
        emit(const AuthError('SIMA certificate is missing'));
        return;
      }

      if (event.finCode.isEmpty) {
        if (kDebugMode) {
          debugPrint('ERROR: FIN code is empty');
        }
        emit(const AuthError('FIN code is missing'));
        return;
      }

      if (kDebugMode) {
        debugPrint('=== SIMA Authenticate Event ===');
        debugPrint('Phone Number: ${event.phoneNumber}');
        debugPrint('FIN Code: ${event.finCode}');
        debugPrint('FIN Code length: ${event.finCode.length}');
        debugPrint('Certificate bytes length: ${event.certificateBytes!.length}');
        debugPrint('==============================');
      }

      // Convert certificate bytes to base64 string
      // Use Uint8List to ensure proper encoding
      final certificateUint8List = Uint8List.fromList(event.certificateBytes!);
      final certificateBase64 = base64Encode(certificateUint8List);
      
      // Verify the base64 encoding is correct
      try {
        final decodedBack = base64Decode(certificateBase64);
        if (decodedBack.length != event.certificateBytes!.length) {
          throw Exception('Base64 encoding verification failed: decoded length mismatch');
        }
        // Verify bytes match
        for (int i = 0; i < decodedBack.length; i++) {
          if (decodedBack[i] != event.certificateBytes![i]) {
            throw Exception('Base64 encoding verification failed: byte mismatch at index $i');
          }
        }
      } catch (e) {
        emit(AuthError('Certificate base64 encoding error: $e'));
        return;
      }
      
      if (kDebugMode) {
        debugPrint('=== Certificate Encoding ===');
        debugPrint('Original bytes length: ${event.certificateBytes!.length}');
        debugPrint('Uint8List length: ${certificateUint8List.length}');
        debugPrint('Base64 encoded length: ${certificateBase64.length}');
        debugPrint('Base64 first 100 chars: ${certificateBase64.substring(0, certificateBase64.length > 100 ? 100 : certificateBase64.length)}...');
        debugPrint('Base64 last 100 chars: ...${certificateBase64.length > 100 ? certificateBase64.substring(certificateBase64.length - 100) : certificateBase64}');
        debugPrint('Base64 encoding verified: true');
        debugPrint('Certificate is valid base64: ${certificateBase64.length == 1412}');
        debugPrint('===========================');
      }
      
      // Validate base64 encoding
      if (certificateBase64.isEmpty) {
        emit(const AuthError('Failed to encode certificate to base64'));
        return;
      }
      
      // Call SIMA certificate verification API with base64 encoded certificate
      if (kDebugMode) {
        debugPrint('=== Calling SIMA Certificate Verification API ===');
        debugPrint('Certificate (base64) length: ${certificateBase64.length}');
        debugPrint('FIN Code (PIN): ${event.finCode}');
        debugPrint('About to call _authRepository.verifySimaCertificate...');
      }
      
      try {
        if (kDebugMode) {
          debugPrint('=== BEFORE API CALL ===');
          debugPrint('Calling verifySimaCertificate at: ${DateTime.now()}');
        }
        
        await _authRepository.verifySimaCertificate(
          certificate: certificateBase64,
          pinCode: event.finCode,
        );
        
        if (kDebugMode) {
          debugPrint('=== AFTER API CALL (SUCCESS) ===');
          debugPrint('API call completed at: ${DateTime.now()}');
          debugPrint('=== SIMA Certificate Verification Success ===');
          debugPrint('Certificate verified successfully, proceeding to PIN setup...');
          debugPrint('=============================================');
        }
      } catch (e, stackTrace) {
        if (kDebugMode) {
          debugPrint('=== AFTER API CALL (ERROR) ===');
          debugPrint('API call failed at: ${DateTime.now()}');
          debugPrint('=== SIMA Certificate Verification Failed ===');
          debugPrint('Error: $e');
          debugPrint('Stack trace: $stackTrace');
          debugPrint('===========================================');
        }
        emit(AuthError('SIMA certificate verification failed: ${e.toString()}'));
        return;
      }

      // SIMA authentication succeeded - proceed to PIN setup
      // Save user session data
      await _secureStorage.write(
        key: AppConstants.username,
        value: username,
      );
      await _secureStorage.write(
        key: AppConstants.hasActiveSession,
        value: 'true',
      );
      await _secureStorage.write(
        key: AppConstants.signInType,
        value: AppConstants.signInUpTypeNumber.toString(),
      );
      
      // Generate a session key (or use a placeholder for SIMA authentication)
      // Since we're not calling the backend, we'll use a generated session key
      final sessionKey = DateTime.now().millisecondsSinceEpoch.toString();
      await _secureStorage.write(
        key: AppConstants.sessionKey,
        value: sessionKey,
      );

      // After SIMA authentication, ALWAYS navigate to PIN setup
      // (User needs to set up PIN even if they had one before)
      // For SIMA, we need to get password hash from backend or use a placeholder
      // Since SIMA doesn't use password, we'll need to handle this differently
      // For now, we'll check if there's a stored password hash, otherwise we'll need
      // to call the backend to set up keystore
      
      // Get or create password hash for SIMA user
      String passwordHashToUse = '';
      final storedPasswordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      
      if (storedPasswordHash != null && storedPasswordHash.isNotEmpty) {
        // Use stored password hash
        passwordHashToUse = storedPasswordHash;
      } else {
        // Generate a password hash from username for SIMA users
        passwordHashToUse = AppUtils.passwordHash(username);
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: passwordHashToUse,
        );
      }
      
      // Navigate directly to PIN setup (skip ChangeKeystore)
      if (kDebugMode) {
        debugPrint('=== SIMA Authenticate: Emitting PinSetupRequired ===');
        debugPrint('Username: $username');
        debugPrint('Password hash length: ${passwordHashToUse.length}');
        debugPrint('Sign in type: ${AppConstants.signInUpTypeNumber}');
      }
      
      emit(PinSetupRequired(
        username: username,
        passwordHash: passwordHashToUse,
        signInType: AppConstants.signInUpTypeNumber,
        isComingFromSignIn: true,
      ));
      
      if (kDebugMode) {
        debugPrint('=== SIMA Authenticate: PinSetupRequired state emitted ===');
      }
    } catch (e) {
      if (kDebugMode) {
        debugPrint('=== SIMA Authenticate: Error occurred ===');
        debugPrint('Error: $e');
      }
      emit(AuthError('SIMA authentication error: ${e.toString()}'));
    }
  }
}
