# Setup Instructions

## 1. Generate Code

After creating the models, you need to generate the JSON serialization code:

```bash
cd flutter_btb_mobile
flutter pub get
flutter pub run build_runner build --delete-conflicting-outputs
```

This will generate:
- `*.g.dart` files for all models with `@JsonSerializable()`
- `api_service.g.dart` for the Retrofit API service

## 2. Update Dependency Injection

Update `lib/injection/injection.dart` to register all dependencies:

```dart
import 'package:get_it/get_it.dart';
import 'package:injectable/injectable.dart';
import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:package_info_plus/package_info_plus.dart';

import '../core/network/dio_client.dart';
import '../data/datasources/remote/api_service.dart';
import '../data/repositories/auth_repository.dart';
import '../data/repositories/bank_accounts_repository.dart';
import '../core/utils/request_builder.dart';

final getIt = GetIt.instance;

@InjectableInit()
void configureDependencies() {
  // Register core dependencies
  getIt.registerLazySingleton(() => FlutterSecureStorage());
  getIt.registerLazySingleton(() => DeviceInfoPlugin());
  getIt.registerLazySingleton(() => DioClient(getIt()));
  
  // Register API Service
  getIt.registerLazySingleton<ApiService>(
    () => ApiService(getIt<DioClient>().dio),
  );
  
  // Register Repositories
  getIt.registerLazySingleton(() => AuthRepository(getIt()));
  getIt.registerLazySingleton(() => BankAccountsRepository(getIt()));
  
  // Register Utilities
  getIt.registerLazySingleton(() => RequestBuilder(
    getIt(),
    getIt(),
  ));
}
```

## 3. Initialize in main.dart

Update `lib/main.dart`:

```dart
import 'package:flutter/material.dart';
import 'injection/injection.dart';
import 'app.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize dependency injection
  configureDependencies();
  
  runApp(const App());
}
```

## 4. Update Auth BLoC

Update the Auth BLoC to use the repository:

```dart
// In auth_bloc.dart
import 'package:injectable/injectable.dart';
import '../../data/repositories/auth_repository.dart';
import '../../core/utils/request_builder.dart';
import '../../core/utils/app_utils.dart';

@injectable
class AuthBloc extends BaseBloc<AuthEvent, AuthState> {
  final AuthRepository _authRepository;
  final RequestBuilder _requestBuilder;

  AuthBloc(this._authRepository, this._requestBuilder) : super(const AuthInitial()) {
    // ... existing code
  }

  Future<void> _onSignIn(
    SignInEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(const AuthLoading());
    try {
      final requestInfo = await _requestBuilder.buildRequestInfo();
      final response = await _authRepository.signIn(
        requestInfo: requestInfo,
        keystoreType: 1, // Adjust as needed
        signInType: event.username.contains('@') 
            ? AppConstants.signInUpTypeEmail 
            : AppConstants.signInUpTypeNumber,
        mobileNumber: !event.username.contains('@') ? event.username : null,
        passwordHash: AppUtils.passwordHash(event.password),
        email: event.username.contains('@') ? event.username : null,
      );

      if (response.responseInfo.isSuccess && response.data != null) {
        // Save user data
        emit(const AuthAuthenticated());
      } else {
        emit(AuthError(response.responseInfo.errorMessage ?? 'Giriş uğursuz oldu'));
      }
    } catch (e) {
      emit(AuthError(e.toString()));
    }
  }
}
```

## 5. Test the Setup

1. Run `flutter pub get`
2. Run `flutter pub run build_runner build --delete-conflicting-outputs`
3. Fix any compilation errors
4. Run the app: `flutter run`

## Notes

- All models use `json_serializable` for JSON conversion
- API service uses Retrofit for type-safe HTTP client
- Repositories abstract the data layer
- RequestBuilder helps create consistent API requests
- Interceptors handle authentication and error handling automatically



