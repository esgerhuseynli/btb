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
  }

  Future<void> _onCheckAuthStatus(
    CheckAuthStatusEvent event,
    Emitter<AuthState> emit,
  ) async {
    try {
      // Check if user has active session
      final hasActiveSession = await _secureStorage.read(
        key: AppConstants.hasActiveSession,
      );
      final pinHash = await _secureStorage.read(key: AppConstants.pinHash);
      final username = await _secureStorage.read(key: AppConstants.username);
      final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      final signInTypeStr = await _secureStorage.read(key: AppConstants.signInType);

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
        emit(PinVerificationRequired(
          username: username,
          passwordHash: passwordHash,
          signInType: signInType,
        ));
      } else {
        // No active session - show intro/sign-in
        emit(const AuthUnauthenticated());
      }
    } catch (e) {
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

      // Step 1: Initial Sign-In (keystoreType=0)
      final response = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 0, // No keystore yet
        signInType: 1, // Always 1 for sign-in
      );

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
        // Save session key temporarily
        final sessionKey = response.sessionKey!;
        
        // Save username and password hash for later use
        await _secureStorage.write(
          key: AppConstants.username,
          value: username,
        );
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: passwordHash,
        );
        
        // Determine sign-in type (number or email)
        final signInType = isEmail
            ? AppConstants.signInUpTypeEmail
            : AppConstants.signInUpTypeNumber;
        
        await _secureStorage.write(
          key: AppConstants.signInType,
          value: signInType.toString(),
        );

        // Step 2: ChangeKeystore - Set up device keystore
        // Update mobileUser with saltSignature (sessionKey)
        final updatedMobileUser = MobileUser(
          username: username,
          passwordHash: passwordHash,
          sessionKey: sessionKey,
          saltSignature: sessionKey, // Set saltSignature for ChangeKeystore
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

        if (changeKeystoreResponse.responseInfo.responseType == 0) {
          // Navigate to PIN Setup Screen
          emit(PinSetupRequired(
            username: username,
            passwordHash: changeKeystoreResponse.passwordHash,
            signInType: signInType,
            isComingFromSignIn: true,
          ));
        } else {
          emit(AuthError(
            changeKeystoreResponse.responseInfo.responseMessage ??
                'Keystore setup failed',
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
        language: 1, // Default to Azerbaijani for sign-up
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

        // Step 1: Sign-In (keystoreType=0) to get session key for ChangeKeystore
        final signInRequestInfo = await _requestBuilder.buildRequestInfo(
          mobileUser: mobileUser,
        );

        final signInResponse = await _authRepository.signIn(
          requestInfo: signInRequestInfo,
          keystoreType: 0, // No keystore yet
          signInType: event.usernameType, // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL
        );

        if (signInResponse.isSuccess && signInResponse.responseInfo.responseType == 0) {
          final sessionKey = signInResponse.sessionKey!;

          // Step 2: ChangeKeystore
          final updatedMobileUser = MobileUser(
            username: username,
            passwordHash: passwordHash,
            sessionKey: sessionKey,
            saltSignature: sessionKey, // Set saltSignature for ChangeKeystore
          );

          final updatedRequestInfo = await _requestBuilder.buildRequestInfo(
            mobileUser: updatedMobileUser,
          );

          final changeKeystoreResponse = await _authRepository.changeKeystore(
            requestInfo: updatedRequestInfo,
            keystoreType: 1, // Set up keystore
            deviceSpecs: MobileDeviceSpecifications(
              nfc: 'Available',
              faceID: 'Available',
              touchID: 'NotAvailable',
            ),
          );

          if (changeKeystoreResponse.responseInfo.responseType == 0) {
            // Navigate to PIN Setup Screen
            emit(PinSetupRequired(
              username: username,
              passwordHash: changeKeystoreResponse.passwordHash,
              signInType: event.usernameType,
              isComingFromSignIn: false, // Coming from sign-up
            ));
          } else {
            emit(AuthError(
              changeKeystoreResponse.responseInfo.responseMessage ??
                  'Keystore setup failed',
            ));
          }
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
      final mobileUser = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: event.sessionKey,
        saltSignature: event.sessionKey, // Set saltSignature for ChangeKeystore
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

      if (changeKeystoreResponse.responseInfo.responseType == 0) {
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
        await _secureStorage.write(
          key: AppConstants.passwordHash,
          value: event.passwordHash,
        );
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
            signInType: event.signInType,
            mobileNumber: event.username, // Username is the mobile number or email
            mobileNumberSecretCode: event.passwordHash, // Password hash is the secret code
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

      // PIN is correct - sign in with keystoreType=1
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

      // Sign in with keystoreType=1
      final signInResponse = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 1, // Keystore is set up
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
            signInType: signInType,
            mobileNumber: username,
            mobileNumberSecretCode: passwordHash,
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
}
