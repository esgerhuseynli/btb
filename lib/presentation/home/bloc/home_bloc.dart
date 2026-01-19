import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:local_auth/local_auth.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../core/bloc/base_bloc.dart';
import '../../../core/utils/request_builder.dart';
import '../../../core/constants/app_constants.dart';
import '../../../data/repositories/bank_accounts_repository.dart';
import '../../../data/repositories/auth_repository.dart';
import '../../../data/models/transaction_data.dart';
import '../../../data/models/mobile_user.dart';
import '../../../data/models/mobile_device_specifications.dart';
import '../../../data/models/bank_cards_response.dart';
import '../../../data/models/bank_card.dart';
import '../../../data/models/bank_account.dart';
import 'home_event.dart';
import 'home_state.dart';

class HomeBloc extends BaseBloc<HomeEvent, HomeState> {
  final BankAccountsRepository _bankAccountsRepository;
  final AuthRepository _authRepository;
  final RequestBuilder _requestBuilder;
  final FlutterSecureStorage _secureStorage;
  final LocalAuthentication _localAuth;

  HomeBloc(
    this._bankAccountsRepository,
    this._authRepository,
    this._requestBuilder,
    this._secureStorage,
    this._localAuth,
  ) : super(const HomeLoading()) {
    on<LoadBankCardsEvent>(_onLoadBankCards);
    on<LoadBankAccountsEvent>(_onLoadBankAccounts);
    on<RefreshHomeDataEvent>(_onRefreshHomeData);
    on<LoadUserDataEvent>(_onLoadUserData);
    on<LoadTransactionsEvent>(_onLoadTransactions);
    on<InitializeHomeEvent>(_onInitializeHome);
  }

  Future<void> _onLoadBankCards(
    LoadBankCardsEvent event,
    Emitter<HomeState> emit,
  ) async {
    emit(const HomeLoading());
    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final response = await _bankAccountsRepository.getBankCards(
        requestInfo: requestInfo,
      );

      // Debug logging
      debugPrint('_onLoadBankCards - isSuccess: ${response.responseInfo.isSuccess}, responseType: ${response.responseInfo.responseType}');
      debugPrint('_onLoadBankCards - bankCards: ${response.bankCards?.length ?? 0}');
      if (response.bankCards != null && response.bankCards!.isNotEmpty) {
        debugPrint('_onLoadBankCards - First card: ${response.bankCards!.first.cardNumber}, balance: ${response.bankCards!.first.cardBalance}');
      }

      if (response.responseInfo.isSuccess && response.bankCards != null) {
        final currentState = state;
        debugPrint('_onLoadBankCards - Emitting HomeLoaded with ${response.bankCards!.length} cards');
        
        // Extract userName from cardHolderShortName (first card with non-null name)
        String userName = currentState is HomeLoaded ? currentState.userName : 'User';
        if (response.bankCards!.isNotEmpty) {
          final cardWithName = response.bankCards!.firstWhere(
            (card) => card.cardHolderShortName != null && card.cardHolderShortName!.isNotEmpty,
            orElse: () => response.bankCards!.first,
          );
          if (cardWithName.cardHolderShortName != null && cardWithName.cardHolderShortName!.isNotEmpty) {
            userName = cardWithName.cardHolderShortName!;
            debugPrint('_onLoadBankCards - Extracted userName from card: $userName');
          }
        }
        
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: response.bankCards!,
            bankAccounts: currentState.bankAccounts,
            transactions: currentState.transactions,
            totalBalance: currentState.totalBalance,
            userName: userName,
            userAvatarUrl: currentState.userAvatarUrl,
          ));
        } else {
          emit(HomeLoaded(
            bankCards: response.bankCards!,
            bankAccounts: const [],
            userName: userName,
          ));
        }
      } else {
        debugPrint('_onLoadBankCards - Response failed or bankCards is null');
        emit(HomeError(
          response.responseInfo.errorMessage ??
              response.responseInfo.responseMessage ??
              'Kartlar yüklənə bilmədi',
        ));
      }
    } catch (e, stackTrace) {
      debugPrint('_onLoadBankCards - Error: $e');
      debugPrint('_onLoadBankCards - StackTrace: $stackTrace');
      emit(HomeError(e.toString()));
    }
  }

  Future<void> _onLoadBankAccounts(
    LoadBankAccountsEvent event,
    Emitter<HomeState> emit,
  ) async {
    final currentState = state;
    if (currentState is! HomeLoading) {
      emit(const HomeLoading());
    }

    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final response = await _bankAccountsRepository.getBankAccounts(
        requestInfo: requestInfo,
      );

      if (response.responseInfo.isSuccess && response.bankAccounts != null) {
        final currentState = state;
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: currentState.bankCards,
            bankAccounts: response.bankAccounts!,
          ));
        } else {
          emit(HomeLoaded(
            bankCards: const [],
            bankAccounts: response.bankAccounts!,
          ));
        }
      } else {
        emit(HomeError(
          response.responseInfo.errorMessage ??
              response.responseInfo.responseMessage ??
              'Hesablar yüklənə bilmədi',
        ));
      }
    } catch (e) {
      emit(HomeError(e.toString()));
    }
  }

  Future<void> _onRefreshHomeData(
    RefreshHomeDataEvent event,
    Emitter<HomeState> emit,
  ) async {
    emit(const HomeLoading());
    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final cardsResponse = await _bankAccountsRepository.getBankCards(
        requestInfo: requestInfo,
      );
      final accountsResponse = await _bankAccountsRepository.getBankAccounts(
        requestInfo: requestInfo,
      );

      if (cardsResponse.responseInfo.isSuccess &&
          accountsResponse.responseInfo.isSuccess) {
        // Calculate total balance
        final cards = cardsResponse.bankCards ?? const [];
        final accounts = accountsResponse.bankAccounts ?? const [];
        double totalBalance = 0.0;
        
        for (var card in cards) {
          totalBalance += card.cardBalance;
        }
        for (var account in accounts) {
          totalBalance += account.balanceInLC ?? 0.0;
        }

        // Extract userName from cardHolderShortName (first card with non-null name)
        String userName = 'User';
        if (cards.isNotEmpty) {
          final cardWithName = cards.firstWhere(
            (card) => card.cardHolderShortName != null && card.cardHolderShortName!.isNotEmpty,
            orElse: () => cards.first,
          );
          if (cardWithName.cardHolderShortName != null && cardWithName.cardHolderShortName!.isNotEmpty) {
            userName = cardWithName.cardHolderShortName!;
            debugPrint('_onRefreshHomeData - Extracted userName from card: $userName');
          }
        }
        
        final currentState = state;
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: cards,
            bankAccounts: accounts,
            transactions: currentState.transactions,
            totalBalance: totalBalance,
            userName: userName,
            userAvatarUrl: currentState.userAvatarUrl,
          ));
        } else {
          emit(HomeLoaded(
            bankCards: cards,
            bankAccounts: accounts,
            totalBalance: totalBalance,
          ));
        }
      } else {
        emit(HomeError(
          cardsResponse.responseInfo.errorMessage ??
              accountsResponse.responseInfo.errorMessage ??
              'Məlumat yüklənə bilmədi',
        ));
      }
    } catch (e) {
      emit(HomeError(e.toString()));
    }
  }

  Future<void> _onLoadUserData(
    LoadUserDataEvent event,
    Emitter<HomeState> emit,
  ) async {
    try {
      final requestInfo = await _requestBuilder.getCommonRequest();
      
      // Get stored mobile user from secure storage
      final mobileUser = await _requestBuilder.getStoredMobileUser();
      
      if (mobileUser == null || mobileUser.username == null || mobileUser.passwordHash == null) {
        // Use default values if not available
        final currentState = state;
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: currentState.bankCards,
            bankAccounts: currentState.bankAccounts,
            transactions: currentState.transactions,
            totalBalance: currentState.totalBalance,
            userName: 'User',
            userAvatarUrl: null,
          ));
        }
        return;
      }

      final userData = await _authRepository.getMobileUserData(
        requestInfo: requestInfo,
      );

      String userName = 'User';
      String? userAvatarUrl;

      if (userData['mobileUserData'] != null) {
        final userDataMap = userData['mobileUserData'] as Map<String, dynamic>;
        userName = userDataMap['customerName'] as String? ?? 'User';
        userAvatarUrl = userDataMap['avatarUrl'] as String?;
      }

      final currentState = state;
      if (currentState is HomeLoaded) {
        emit(HomeLoaded(
          bankCards: currentState.bankCards,
          bankAccounts: currentState.bankAccounts,
          transactions: currentState.transactions,
          totalBalance: currentState.totalBalance,
          userName: userName,
          userAvatarUrl: userAvatarUrl,
        ));
      } else {
        emit(HomeLoaded(
          bankCards: const [],
          bankAccounts: const [],
          userName: userName,
          userAvatarUrl: userAvatarUrl,
        ));
      }
    } catch (e) {
      // Silently fail - user data is not critical
      debugPrint('Failed to load user data: $e');
    }
  }

  Future<void> _onLoadTransactions(
    LoadTransactionsEvent event,
    Emitter<HomeState> emit,
  ) async {
    try {
      // For now, generate sample transactions
      // TODO: Replace with actual API call when transaction endpoint is available
      final transactions = _generateSampleTransactions(event.localizations);

      final currentState = state;
      // Only update if already loaded - don't emit HomeLoaded if still loading
      if (currentState is HomeLoaded) {
        emit(HomeLoaded(
          bankCards: currentState.bankCards,
          bankAccounts: currentState.bankAccounts,
          transactions: transactions,
          totalBalance: currentState.totalBalance,
          userName: currentState.userName,
          userAvatarUrl: currentState.userAvatarUrl,
        ));
      }
      // If state is HomeLoading or HomeInitial, don't emit HomeLoaded
      // Wait for InitializeHomeEvent to complete first
    } catch (e) {
      debugPrint('Failed to load transactions: $e');
    }
  }

  /// Initialize home page with full API sequence
  Future<void> _onInitializeHome(
    InitializeHomeEvent event,
    Emitter<HomeState> emit,
  ) async {
    debugPrint('=== _onInitializeHome START ===');
    emit(const HomeLoading());
    try {
      // Get stored credentials
      final username = await _secureStorage.read(key: AppConstants.username);
      final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      final signInTypeStr = await _secureStorage.read(key: AppConstants.signInType);
      final existingSessionKey = await _secureStorage.read(key: AppConstants.sessionKey);
      
      if (username == null || passwordHash == null) {
        emit(const HomeError('User credentials not found. Please sign in again.'));
        return;
      }

      final signInType = int.tryParse(signInTypeStr ?? '2') ?? 2;

      String sessionKey2;
      
      // Always perform ChangeKeystore after SignInNew (even if session exists from PIN verification)
      // This ensures the keystore is properly set up for subsequent API calls
      if (existingSessionKey != null && existingSessionKey.isNotEmpty) {
        debugPrint('=== Session exists from PIN verification - calling ChangeKeystore ===');
        
        // Step 1: ChangeKeystore using existing session key
        final deviceSpecs = await _getDeviceSpecifications();
        final mobileUserForChangeKeystore = MobileUser(
          username: username,
          passwordHash: passwordHash, // Original password hash
          sessionKey: null, // Don't include sessionKey in request body
          saltSignature: existingSessionKey, // Use existing sessionKey as SaltSignature
        );
        final requestInfoForChangeKeystore = await _requestBuilder.buildRequestInfo(
          mobileUser: mobileUserForChangeKeystore,
        );

        final changeKeystoreResponse = await _authRepository.changeKeystore(
          requestInfo: requestInfoForChangeKeystore,
          keystoreType: 1,
          deviceSpecs: deviceSpecs,
        );

        if (!changeKeystoreResponse.responseInfo.isSuccess || changeKeystoreResponse.passwordHash == null) {
          emit(HomeError(
            changeKeystoreResponse.responseInfo.errorMessage ??
                changeKeystoreResponse.responseInfo.responseMessage ??
                'Change keystore failed',
          ));
          return;
        }

        final newPasswordHash = changeKeystoreResponse.passwordHash!;
        // IMPORTANT: Do NOT overwrite passwordHash here
        // Keep the original password hash (from user input) that was saved during sign-in
        // The new password hash from ChangeKeystore is only used for this sign-in call, not saved

        // Step 2: SignInNew again with keystoreType: 1 using new password hash
        final mobileUserForSignIn = MobileUser(
          username: username,
          passwordHash: newPasswordHash,
          sessionKey: null,
          saltSignature: null,
        );
        final requestInfoForSignIn = await _requestBuilder.buildRequestInfo(
          mobileUser: mobileUserForSignIn,
        );

        final signInResponse = await _authRepository.signIn(
          requestInfo: requestInfoForSignIn,
          keystoreType: 1,
          signInType: signInType,
        );

        if (!signInResponse.isSuccess || signInResponse.sessionKey == null) {
          emit(HomeError(
            signInResponse.responseInfo.errorMessage ??
                signInResponse.responseInfo.responseMessage ??
                'Sign-in after ChangeKeystore failed',
          ));
          return;
        }

        sessionKey2 = signInResponse.sessionKey!;
        await _secureStorage.write(key: AppConstants.sessionKey, value: sessionKey2);
      } else {
        debugPrint('=== No existing session - performing full sign-in sequence ===');

      // Step 1: Initial SignIn (Password-based) - KeystoreType: 0, SignInType: 1
      final mobileUser1 = MobileUser(
        username: username,
        passwordHash: passwordHash,
        sessionKey: null,
        saltSignature: null,
      );
      final requestInfo1 = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser1,
      );
      
      final signInResponse1 = await _authRepository.signIn(
        requestInfo: requestInfo1,
        keystoreType: 0,
        signInType: 1, // Password-based sign-in
      );

      if (!signInResponse1.isSuccess || signInResponse1.sessionKey == null) {
        emit(HomeError(
          signInResponse1.responseInfo.errorMessage ??
              signInResponse1.responseInfo.responseMessage ??
              'Initial sign-in failed',
        ));
        return;
      }

      final sessionKey1 = signInResponse1.sessionKey!;
      await _secureStorage.write(key: AppConstants.sessionKey, value: sessionKey1);

      // Step 2: ChangeKeystore (Switch to Biometric) - KeystoreType: 1
      final deviceSpecs = await _getDeviceSpecifications();
      final mobileUser2 = MobileUser(
        username: username,
        passwordHash: passwordHash, // Original password hash
        sessionKey: null, // Don't include sessionKey in request body
        saltSignature: sessionKey1, // Use sessionKey from Step 1 as SaltSignature
      );
      final requestInfo2 = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser2,
      );

      final changeKeystoreResponse = await _authRepository.changeKeystore(
        requestInfo: requestInfo2,
        keystoreType: 1,
        deviceSpecs: deviceSpecs,
      );

      if (!changeKeystoreResponse.responseInfo.isSuccess || changeKeystoreResponse.passwordHash == null) {
        emit(HomeError(
          changeKeystoreResponse.responseInfo.errorMessage ??
              changeKeystoreResponse.responseInfo.responseMessage ??
              'Change keystore failed',
        ));
        return;
      }

      final newPasswordHash = changeKeystoreResponse.passwordHash!;

      // Step 3: Second SignIn (Keystore-based) - KeystoreType: 1, SignInType: 2
      final mobileUser3 = MobileUser(
        username: username,
        passwordHash: newPasswordHash,
        sessionKey: null,
        saltSignature: null,
      );
      final requestInfo3 = await _requestBuilder.buildRequestInfo(
        mobileUser: mobileUser3,
      );

      final signInResponse2 = await _authRepository.signIn(
        requestInfo: requestInfo3,
        keystoreType: 1,
        signInType: 2, // Keystore-based sign-in (as per API requirements)
        mobileNumber: '',
        mobileNumberSecretCode: '',
      );

      if (!signInResponse2.isSuccess || signInResponse2.sessionKey == null) {
        emit(HomeError(
          signInResponse2.responseInfo.errorMessage ??
              signInResponse2.responseInfo.responseMessage ??
              'Second sign-in failed',
        ));
        return;
      }

        sessionKey2 = signInResponse2.sessionKey!;
      await _secureStorage.write(key: AppConstants.sessionKey, value: sessionKey2);
      // IMPORTANT: Do NOT overwrite passwordHash here
      // Keep the original password hash (from user input) that was saved during sign-in
      // The new password hash from ChangeKeystore is only used for this sign-in call, not saved

      // Step 4: Change Device Push Token (Optional)
      try {
        final prefs = await SharedPreferences.getInstance();
        final fcmToken = prefs.getString(AppConstants.fcmNotificationToken);
        if (fcmToken != null && fcmToken.isNotEmpty) {
          final mobileUser4 = MobileUser(
            username: '',
            passwordHash: '',
            sessionKey: sessionKey2,
            saltSignature: sessionKey2,
          );
          final requestInfo4 = await _requestBuilder.buildRequestInfo(
            mobileUser: mobileUser4,
          );
          await _authRepository.sendFCMToken(
            requestInfo: requestInfo4,
            fcmToken: fcmToken,
          );
        }
      } catch (e) {
        // Ignore FCM token errors - not critical
        debugPrint('Failed to send FCM token: $e');
        }
      }

      // Step 5: List Bank Cards
      debugPrint('=== Step 5: Loading Bank Cards ===');
      final requestInfo5 = await _requestBuilder.getCommonRequest();
      BankCardsResponse cardsResponse;
      try {
        debugPrint('Calling getBankCards...');
        cardsResponse = await _bankAccountsRepository.getBankCards(
          requestInfo: requestInfo5,
        );
        debugPrint('Cards response received successfully');
      } catch (e, stackTrace) {
        debugPrint('ERROR loading bank cards: $e');
        debugPrint('StackTrace: $stackTrace');
        rethrow;
      }

      // Step 6: List Bank Accounts
      final accountsResponse = await _bankAccountsRepository.getBankAccounts(
        requestInfo: requestInfo5,
      );

      // Step 7: Extract userName from cardHolderShortName (primary source)
      String userName = 'User';
      if (cardsResponse.bankCards != null && cardsResponse.bankCards!.isNotEmpty) {
        final cardWithName = cardsResponse.bankCards!.firstWhere(
          (card) => card.cardHolderShortName != null && card.cardHolderShortName!.isNotEmpty,
          orElse: () => cardsResponse.bankCards!.first,
        );
        if (cardWithName.cardHolderShortName != null && cardWithName.cardHolderShortName!.isNotEmpty) {
          userName = cardWithName.cardHolderShortName!;
          debugPrint('_onInitializeHome - Extracted userName from card: $userName');
        }
      }
      
      // Step 7b: Get Mobile User Data (optional - for avatar, userName as fallback)
      String? userAvatarUrl;
      try {
        final userData = await _authRepository.getMobileUserData(
          requestInfo: requestInfo5,
        );
        if (userData['mobileUserData'] != null) {
          final userDataMap = userData['mobileUserData'] as Map<String, dynamic>;
          // Use cardHolderShortName as primary, fallback to customerName if card name not available
          if (userName == 'User' || userName.isEmpty) {
            userName = userDataMap['customerName'] as String? ?? 'User';
          }
          userAvatarUrl = userDataMap['avatarUrl'] as String?;
        }
      } catch (e) {
        // Silently fail - user data is not critical
        debugPrint('Failed to load user data: $e');
      }

      // Step 8: Get Card Statements (ListBankCardStatement)
      debugPrint('=== Step 8: Starting ListBankCardStatement ===');
      List<TransactionData> transactions = [];
      try {
        // Only fetch statements if cards were loaded successfully
        if (cardsResponse.responseInfo.isSuccess && 
            cardsResponse.bankCards != null && 
            cardsResponse.bankCards!.isNotEmpty) {
          // Get the first card's idCard for the statement request
          final firstCardId = cardsResponse.bankCards!.first.idCard;
          
          // Format dates: FromDate = 01-09-2025, ToDate = current date (DD-MM-YYYY format)
          final now = DateTime.now();
          final toDate = '${now.day.toString().padLeft(2, '0')}-${now.month.toString().padLeft(2, '0')}-${now.year}';
          const fromDate = '01-09-2025';
          
          debugPrint('=== Calling ListBankCardStatement ===');
          debugPrint('FromIdCard: $firstCardId');
          debugPrint('FromDate: $fromDate');
          debugPrint('ToDate: $toDate');
          
          final statementsResponse = await _bankAccountsRepository.getCardStatements(
            requestInfo: requestInfo5,
            fromIdCard: firstCardId,
            fromDate: fromDate,
            toDate: toDate,
          );
          
          debugPrint('Card statements response - isSuccess: ${statementsResponse.responseInfo.isSuccess}');
          debugPrint('Card statements response - responseType: ${statementsResponse.responseInfo.responseType}');
          debugPrint('Card statements response - responseMessage: ${statementsResponse.responseInfo.responseMessage}');
          debugPrint('Card statements response - data is null: ${statementsResponse.data == null}');
          if (statementsResponse.data != null) {
            debugPrint('Card statements response - data type: ${statementsResponse.data.runtimeType}');
            debugPrint('Card statements response - data: ${statementsResponse.data}');
          }
          
          // Parse the response and convert to TransactionData
          // The API response has bankCardStatement at root level, not in a Data field
          // So we need to access it from the data field if it's a Map
          if (statementsResponse.responseInfo.isSuccess) {
            List<dynamic>? bankCardStatements;
            
            // The response structure is: {"responceInfo": {...}, "bankCardStatement": [...]}
            // ApiResponse expects {"ResponseInfo": {...}, "Data": {...}}
            // Since the keys don't match exactly, data might contain the whole response or be null
            if (statementsResponse.data != null) {
              if (statementsResponse.data is Map) {
                final responseData = statementsResponse.data as Map<String, dynamic>;
                // Try to get bankCardStatement from the data map
                bankCardStatements = responseData['bankCardStatement'] as List<dynamic>?;
                debugPrint('Found bankCardStatement in data map: ${bankCardStatements != null}');
                if (bankCardStatements != null) {
                  debugPrint('bankCardStatement count: ${bankCardStatements.length}');
                }
              } else if (statementsResponse.data is List) {
                // If data is directly a list, use it
                bankCardStatements = statementsResponse.data as List<dynamic>;
                debugPrint('Data is directly a list with ${bankCardStatements.length} items');
              }
            }
            
            if (bankCardStatements != null && bankCardStatements.isNotEmpty) {
              debugPrint('Parsing ${bankCardStatements.length} card statements');
              transactions = _parseCardStatementsToTransactions(bankCardStatements);
              debugPrint('Converted to ${transactions.length} transactions');
              for (var i = 0; i < transactions.length && i < 3; i++) {
                debugPrint('Transaction $i: ${transactions[i].merchant} - ${transactions[i].amount}');
              }
            } else {
              debugPrint('No card statements found in response');
              debugPrint('statementsResponse.data: ${statementsResponse.data}');
            }
          }
        } else {
          debugPrint('No cards available to fetch statements - condition check failed');
          debugPrint('  - isSuccess: ${cardsResponse.responseInfo.isSuccess}');
          debugPrint('  - bankCards != null: ${cardsResponse.bankCards != null}');
          debugPrint('  - bankCards.isNotEmpty: ${cardsResponse.bankCards?.isNotEmpty ?? false}');
        }
      } catch (e, stackTrace) {
        // Silently fail - card statements are not critical
        debugPrint('Failed to load card statements: $e');
        debugPrint('StackTrace: $stackTrace');
      }

      // Debug logging
      debugPrint('=== Cards Response Debug ===');
      debugPrint('Cards response - isSuccess: ${cardsResponse.responseInfo.isSuccess}');
      debugPrint('Cards response - responseType: ${cardsResponse.responseInfo.responseType}');
      debugPrint('Cards response - responseMessage: ${cardsResponse.responseInfo.responseMessage}');
      debugPrint('Cards response - errorCode: ${cardsResponse.responseInfo.errorCode}');
      debugPrint('Cards response - errorMessage: ${cardsResponse.responseInfo.errorMessage}');
      debugPrint('Cards response - bankCards is null: ${cardsResponse.bankCards == null}');
      debugPrint('Cards response - bankCards count: ${cardsResponse.bankCards?.length ?? 0}');
      if (cardsResponse.bankCards != null) {
        if (cardsResponse.bankCards!.isEmpty) {
          debugPrint('Cards response - bankCards is an EMPTY list!');
        } else {
          debugPrint('Cards response - bankCards has ${cardsResponse.bankCards!.length} items');
          for (int i = 0; i < cardsResponse.bankCards!.length; i++) {
            final card = cardsResponse.bankCards![i];
            debugPrint('Card $i:');
            debugPrint('  - idCard: ${card.idCard}');
            debugPrint('  - cardNumber: ${card.cardNumber}');
            debugPrint('  - cardBalance: ${card.cardBalance}');
            debugPrint('  - currency: ${card.currency}');
            debugPrint('  - cardColor: ${card.cardColor}');
            debugPrint('  - cardAltName: ${card.cardAltName}');
          }
        }
      }
      debugPrint('=== End Cards Response Debug ===');

      // Get cards - only proceed if cards response is successful
      final cards = cardsResponse.responseInfo.isSuccess 
          ? (cardsResponse.bankCards ?? const <BankCard>[])
          : const <BankCard>[];
      
      // Get accounts - proceed even if accounts response failed
      final accounts = accountsResponse.responseInfo.isSuccess
          ? (accountsResponse.bankAccounts ?? const <BankAccount>[])
          : const <BankAccount>[];
      
      // If cards loaded successfully, emit HomeLoaded even if accounts failed
      if (cardsResponse.responseInfo.isSuccess) {
        debugPrint('Cards loaded successfully, emitting HomeLoaded with ${cards.length} cards and ${accounts.length} accounts');
        
        // Calculate total balance
        double totalBalance = 0.0;
        for (var card in cards) {
          totalBalance += card.cardBalance;
        }
        for (var account in accounts) {
          totalBalance += account.balanceInLC ?? 0.0;
        }

        debugPrint('=== Emitting HomeLoaded ===');
        debugPrint('Cards count: ${cards.length}');
        debugPrint('Accounts count: ${accounts.length}');
        debugPrint('Transactions count: ${transactions.length}');
        debugPrint('Total balance: $totalBalance');
        emit(HomeLoaded(
          bankCards: cards,
          bankAccounts: accounts,
          transactions: transactions,
          totalBalance: totalBalance,
          userName: userName,
          userAvatarUrl: userAvatarUrl,
        ));
        debugPrint('=== HomeLoaded emitted successfully ===');
      } else {
        debugPrint('Cards response failed - error: ${cardsResponse.responseInfo.errorMessage}');
        emit(HomeError(
          cardsResponse.responseInfo.errorMessage ??
              cardsResponse.responseInfo.responseMessage ??
              'Failed to load bank cards',
        ));
      }
    } catch (e, stackTrace) {
      debugPrint('=== ERROR in _onInitializeHome ===');
      debugPrint('Error: $e');
      debugPrint('StackTrace: $stackTrace');
      emit(HomeError(e.toString()));
    }
    debugPrint('=== _onInitializeHome END ===');
  }

  /// Get device specifications for biometric authentication
  Future<MobileDeviceSpecifications> _getDeviceSpecifications() async {
    String faceID = 'NotAvailable';
    String touchID = 'NotAvailable';
    String nfc = 'NotAvailable';

    try {
      final isAvailable = await _localAuth.canCheckBiometrics ||
          await _localAuth.isDeviceSupported();
      
      if (isAvailable) {
        final availableBiometrics = await _localAuth.getAvailableBiometrics();
        
        if (Platform.isIOS) {
          faceID = availableBiometrics.contains(BiometricType.face)
              ? 'Available'
              : 'NotAvailable';
          touchID = availableBiometrics.contains(BiometricType.fingerprint)
              ? 'Available'
              : 'NotAvailable';
        } else if (Platform.isAndroid) {
          touchID = availableBiometrics.contains(BiometricType.fingerprint)
              ? 'Available'
              : 'NotAvailable';
          faceID = availableBiometrics.contains(BiometricType.face)
              ? 'Available'
              : 'NotAvailable';
        }
      }

      // NFC is typically available on most modern devices
      // In a real implementation, you might want to check NFC availability
      nfc = 'Available';
    } catch (e) {
      debugPrint('Error checking biometric availability: $e');
    }

    return MobileDeviceSpecifications(
      faceID: faceID,
      touchID: touchID,
      nfc: nfc,
    );
  }

  /// Parse card statements from API response to TransactionData
  List<TransactionData> _parseCardStatementsToTransactions(List<dynamic> statements) {
    final transactions = <TransactionData>[];
    
    for (var statement in statements) {
      try {
        final operationDate = statement['operationDate'] as String? ?? '';
        final operationDescription = statement['operationDescription'] as String? ?? '';
        final amount = (statement['amount'] as num?)?.toDouble() ?? 0.0;
        final paymentType = statement['paymentType'] as int? ?? 0;
        
        // Parse date: "2026-01-15 11:54:31" -> "Jan 15, 2026"
        String formattedDate = '';
        if (operationDate.isNotEmpty) {
          try {
            final dateTime = DateTime.parse(operationDate.split(' ').first);
            final months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
                           'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
            formattedDate = '${months[dateTime.month - 1]} ${dateTime.day},${dateTime.year}';
          } catch (e) {
            formattedDate = operationDate.split(' ').first;
          }
        }
        
        // Parse operationDescription to extract only one language
        // API returns text like "Account Credit Hesaba medaxil" (both languages)
        String merchant = operationDescription;
        if (operationDescription.isNotEmpty) {
          // Check if description contains Azerbaijani characters (ə, ş, ı, etc.)
          final hasAzerbaijaniChars = operationDescription.contains(RegExp(r'[əıöüşğç]', caseSensitive: false));
          
          if (hasAzerbaijaniChars) {
            // Split by common separators and take the English part (first part usually)
            final parts = operationDescription.split(RegExp(r'\s+'));
            final englishParts = <String>[];
            
            for (var part in parts) {
              // If part doesn't contain Azerbaijani characters, it's likely English
              if (!part.contains(RegExp(r'[əıöüşğç]', caseSensitive: false))) {
                englishParts.add(part);
              }
            }
            
            // If we found English parts, use them; otherwise use the category
            merchant = englishParts.isNotEmpty ? englishParts.join(' ') : operationDescription;
          }
          
          // If merchant is still empty or same as original, use category-based name
          if (merchant.isEmpty || merchant == operationDescription) {
            // Determine category based on paymentType and description
            if (paymentType == 2 && (operationDescription.toLowerCase().contains('credit') ||
                operationDescription.toLowerCase().contains('medaxil'))) {
              merchant = 'Account Credit';
            } else if (paymentType == 1) {
              merchant = 'Payment';
            } else {
              merchant = 'Transaction';
            }
          }
        } else {
          merchant = 'Transaction';
        }
        
        // Determine category based on paymentType and description
        String category = '';
        IconData icon = Icons.receipt;
        
        if (paymentType == 1) {
          // Withdrawal
          if (operationDescription.toLowerCase().contains('atm') || 
              operationDescription.toLowerCase().contains('cash')) {
            category = 'ATM Withdrawal';
            icon = Icons.atm;
          } else {
            category = 'Payment';
            icon = Icons.payment;
          }
        } else if (paymentType == 2) {
          // Credit/Deposit
          if (operationDescription.toLowerCase().contains('credit') ||
              operationDescription.toLowerCase().contains('medaxil')) {
            category = 'Account Credit';
            icon = Icons.account_balance_wallet;
          } else {
            category = 'Deposit';
            icon = Icons.add_circle;
          }
        } else {
          category = 'Transaction';
          icon = Icons.receipt;
        }
        
        // Format amount: positive amounts show +, negative show -
        final isPositive = amount > 0;
        final formattedAmount = isPositive 
            ? '+${amount.toStringAsFixed(2)}' 
            : amount.toStringAsFixed(2);
        
        transactions.add(TransactionData(
          merchant: merchant,
          dateAndCategory: formattedDate.isNotEmpty 
              ? '$formattedDate · $category'
              : category,
          amount: formattedAmount,
          icon: icon,
          isPositive: isPositive,
        ));
      } catch (e) {
        debugPrint('Error parsing statement: $e');
      }
    }
    
    return transactions;
  }

  /// Generate sample transactions for demo purposes
  /// TODO: Replace with actual API call
  List<TransactionData> _generateSampleTransactions(dynamic l10n) {
    return [
      // Today's transactions
      TransactionData(
        merchant: 'Bravo Supermarket',
        dateAndCategory: 'Jan 8,2026 · ${l10n.groceryShopping}',
        amount: '-85.00',
        icon: Icons.shopping_basket,
        isPositive: false,
      ),
      TransactionData(
        merchant: 'Socar Petroleum',
        dateAndCategory: 'Jan 8,2026 · ${l10n.groceryShopping}',
        amount: '-25.00',
        icon: Icons.local_gas_station,
        isPositive: false,
      ),
      TransactionData(
        merchant: 'Starbucks',
        dateAndCategory: 'Jan 8,2026 · ${l10n.foodDining}',
        amount: '-22.60',
        icon: Icons.restaurant_menu,
        isPositive: false,
      ),
      // Jan 6 transactions
      TransactionData(
        merchant: l10n.creditCardPayment,
        dateAndCategory: 'Jan 6,2026 · ${l10n.creditCardPayment}',
        amount: '+150.00',
        icon: Icons.credit_card,
        isPositive: true,
      ),
      TransactionData(
        merchant: 'Park Cinema',
        dateAndCategory: 'Jan 6,2026 · ${l10n.movie}',
        amount: '-28.00',
        icon: Icons.local_movies,
        isPositive: false,
      ),
      // Jan 5 transactions
      TransactionData(
        merchant: 'Fuzzy Coffee & Wine',
        dateAndCategory: 'Jan 5,2026 · ${l10n.foodDining}',
        amount: '-36.80',
        icon: Icons.restaurant_menu,
        isPositive: false,
      ),
      TransactionData(
        merchant: 'Adel Samedli',
        dateAndCategory: 'Jan 5,2026 · ${l10n.moneyReceived}',
        amount: '+360.00',
        icon: Icons.person,
        isPositive: true,
      ),
      TransactionData(
        merchant: 'ZARA',
        dateAndCategory: 'Jan 5,2026 · ${l10n.shopping}',
        amount: '-219.00',
        icon: Icons.shopping_bag,
        isPositive: false,
      ),
      TransactionData(
        merchant: 'Samir Gasanov',
        dateAndCategory: 'Jan 5,2026 · ${l10n.transferTransaction}',
        amount: '-55.00',
        icon: Icons.person,
        isPositive: false,
      ),
      TransactionData(
        merchant: 'Bravo Supermarket',
        dateAndCategory: 'Jan 5,2026 · ${l10n.groceryShopping}',
        amount: '-26.40',
        icon: Icons.shopping_basket,
        isPositive: false,
      ),
    ];
  }
}

