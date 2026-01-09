# JWT/Session Token Implementation - Flutter

This document describes the JWT/session token storage and usage implementation in Flutter, following the exact Android source code behavior.

## Overview

The Android app uses two mechanisms for authentication:
1. **Session Key** (`sessionKey`) - Stored in SharedPreferences and used as `SaltSignature` in `MobileUser`
2. **ASP.NET Core Session Cookie** (`.AspNetCore.Session`) - Extracted from response headers and stored in SharedPreferences, then added to all request headers

## Android Implementation

### Storage
- **Session Key**: Stored in SharedPreferences with key `SESSION_KEY = "d2"` and in `AppData.getInstance().setSessionKey()`
- **Cookie**: Stored in SharedPreferences with key `COOKIE_KEY = "Cookie"` (contains `.AspNetCore.Session=...`)

### Request Flow
1. **ResponseCookiesInterceptor**: Extracts `.AspNetCore.Session` cookie from `Set-Cookie` headers and stores in SharedPreferences
2. **RequestCookiesInterceptor**: Adds stored cookie to request headers
3. **Utils.getCommonRequest()**: Creates RequestInfo with:
   - `MobileUser.SaltSignature` = `AppData.getInstance().getSessionKey()`
   - `MobileUser.Username` = "" (empty)
   - `MobileUser.PasswordHash` = "" (empty)

## Flutter Implementation

### Files Modified

1. **`lib/core/constants/app_constants.dart`**
   - Added `cookieKey = 'Cookie'` constant (matches Android `Constants.COOKIE_KEY`)

2. **`lib/core/network/interceptors/auth_interceptor.dart`**
   - Created `RequestCookiesInterceptor` class - Adds stored cookie to request headers
   - Created `ResponseCookiesInterceptor` class - Extracts and stores `.AspNetCore.Session` cookie
   - Updated `AuthInterceptor` to use SharedPreferences for cookie storage
   - Maintains backward compatibility with sessionKey extraction

3. **`lib/core/network/dio_client.dart`**
   - Updated to use `ResponseCookiesInterceptor` and `RequestCookiesInterceptor`
   - Matches Android interceptor order
   - Timeouts set to 60 seconds (matching Android)

4. **`lib/core/utils/request_builder.dart`**
   - Added `getCommonRequest()` method - Matches Android `Utils.getCommonRequest()` behavior
   - Creates RequestInfo with `SaltSignature` set to `sessionKey` and empty `Username`/`PasswordHash`
   - Added `buildAuthenticatedRequestInfo()` method for authenticated requests

5. **`lib/presentation/home/bloc/home_bloc.dart`**
   - Updated to use `getCommonRequest()` for all authenticated API calls (listBankCards, listBankAccounts, refresh)

### Cookie Handling Flow

```
Response from Server
    ↓
Set-Cookie: .AspNetCore.Session=... header
    ↓
ResponseCookiesInterceptor extracts cookie
    ↓
Store in SharedPreferences (key: "Cookie")
    ↓
Next Request
    ↓
RequestCookiesInterceptor reads from SharedPreferences
    ↓
Add Cookie header to request
    ↓
Request sent to server
```

### SaltSignature Handling Flow

```
User Signs In
    ↓
SignInResponse contains sessionKey
    ↓
Store sessionKey in SecureStorage (key: "d2")
    ↓
For Authenticated Requests:
    ↓
getCommonRequest() called
    ↓
Read sessionKey from SecureStorage
    ↓
Create RequestInfo with:
    - MobileUser.SaltSignature = sessionKey
    - MobileUser.Username = ""
    - MobileUser.PasswordHash = ""
    ↓
Request sent to server
```

## Key Differences from Android

1. **Immutable Models**: Flutter uses immutable models (final fields), so we create new instances instead of modifying existing ones
2. **Storage Split**: 
   - SessionKey stored in `FlutterSecureStorage` (secure)
   - Cookie stored in `SharedPreferences` (like Android)
3. **AppData Singleton**: Android uses `AppData.getInstance()`, Flutter uses `RequestBuilder.getCommonRequest()` which reads from storage directly

## Usage

### For Authenticated Requests (Home Screen, etc.)

```dart
// In HomeBloc or any authenticated API call
final requestInfo = await _requestBuilder.getCommonRequest();

final response = await _repository.someAuthenticatedMethod(
  requestInfo: requestInfo,
);
```

### For Sign-In Requests

```dart
// Create MobileUser with username and passwordHash
final mobileUser = MobileUser(
  username: username,
  passwordHash: passwordHash,
  sessionKey: null,
  saltSignature: null,
);

final requestInfo = await _requestBuilder.buildRequestInfo(
  mobileUser: mobileUser,
);

final response = await _authRepository.signIn(...);
```

## Testing Checklist

- [ ] Sign-in stores sessionKey in SecureStorage
- [ ] ResponseCookiesInterceptor extracts `.AspNetCore.Session` cookie from responses
- [ ] Cookie is stored in SharedPreferences with key "Cookie"
- [ ] RequestCookiesInterceptor adds cookie to request headers
- [ ] `getCommonRequest()` sets SaltSignature to sessionKey
- [ ] `getCommonRequest()` clears Username and PasswordHash
- [ ] Home screen API calls use `getCommonRequest()`
- [ ] All authenticated requests include cookie in headers
- [ ] All authenticated requests have SaltSignature set correctly

## API Request Format

### Authenticated Request (Home Screen, etc.)

```json
{
  "RequestInfo": {
    "MobileUser": {
      "Username": "",
      "PasswordHash": "",
      "SessionKey": null,
      "SaltSignature": "<sessionKey from storage>"
    },
    "DeviceInfo": {...},
    "AppInfo": {...},
    "Language": 1
  }
}
```

### Request Headers

```
Cookie: .AspNetCore.Session=<cookie value from SharedPreferences>
Content-Type: application/json
Accept: application/json
```

## Notes

- Both cookie and sessionKey are used for authentication
- Cookie is automatically handled by interceptors
- SaltSignature must be set for authenticated requests
- Username and PasswordHash should be empty for authenticated requests (except during sign-in)
- SessionKey is stored securely in FlutterSecureStorage
- Cookie is stored in SharedPreferences (as per Android implementation)

