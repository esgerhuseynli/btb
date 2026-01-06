# Data Models and API Integration Summary

## Models Created

### Core Models
1. **MobileUser** - User authentication data
2. **DeviceInfo** - Device information for API requests
3. **AppInfo** - Application information
4. **RequestInfo** - Wrapper for all API requests
5. **ResponseInfo** - API response metadata
6. **ApiResponse<T>** - Generic API response wrapper

### Business Models
1. **BankCard** - Bank card information
2. **BankAccount** - Bank account information
3. **SignInRequest** - Sign in request model

## API Service

### ApiService (Retrofit)
- Authentication endpoints (sign in, sign up, verify code, sign out)
- Bank accounts endpoints (cards, accounts, loans, deposits)
- User data endpoints
- Exchange rates, news, notifications
- Service points (ATMs, branches)

## Repositories

1. **AuthRepository** - Authentication operations
2. **BankAccountsRepository** - Bank accounts, cards, loans, deposits

## Network Layer

### Interceptors
1. **AuthInterceptor** - Handles session key from cookies
2. **ErrorInterceptor** - Standardizes error messages

### DioClient
- Configured with base URL, timeouts
- Interceptors for auth and error handling
- Logging interceptor for debugging

## Utilities

### RequestBuilder
- Builds RequestInfo with device and app information
- Retrieves stored mobile user data
- Handles language settings

## Next Steps

1. Run `flutter pub run build_runner build` to generate JSON serialization code
2. Integrate repositories with BLoC
3. Add more models as needed (loans, deposits, transfers, payments, etc.)
4. Implement local data sources for caching
5. Add error handling in repositories



