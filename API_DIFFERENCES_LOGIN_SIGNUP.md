# API Differences: Login & Sign Up Flows

## Executive Summary

This document compares the API calls used in **Login** and **Sign Up** flows between Android and Flutter implementations. The Flutter implementation is **missing critical API calls** and has **incorrect API call sequences**.

---

## 🔴 Critical API Differences

### 1. **ChangeKeystore API - MISSING IN FLUTTER**

#### Android:
- ✅ **Endpoint**: `api/MobileUser/ChangeKeystore`
- ✅ **Called after**: Initial Sign-In (keystoreType=0)
- ✅ **Purpose**: Set up keystore for device security
- ✅ **Request Model**: `ChangeKeystoreRequest`
- ✅ **Response Model**: `ChangeKeystoreResponse`

#### Flutter:
- ❌ **NOT IMPLEMENTED** - Endpoint exists in constants but no API service method
- ❌ **NOT CALLED** - Missing from sign-in flow
- ❌ **NO MODEL** - No `ChangeKeystoreRequest` or `ChangeKeystoreResponse` models

**Impact**: Flutter cannot set up device keystore, which is required for PIN authentication.

---

### 2. **SendFCMToken API - MISSING IN FLUTTER**

#### Android:
- ✅ **Endpoint**: `api/MobileUser/ChangeDevicePushInfoToken`
- ✅ **Called after**: Successful sign-in (after PIN setup)
- ✅ **Purpose**: Register Firebase Cloud Messaging token
- ✅ **Request Model**: `FcmTokenRequest`

#### Flutter:
- ❌ **NOT IMPLEMENTED** - Endpoint exists in constants but no API service method
- ❌ **NOT CALLED** - Missing from sign-in flow
- ❌ **NO MODEL** - No `FcmTokenRequest` model

**Impact**: Flutter cannot register push notification tokens.

---

### 3. **ListBankCards API - MISSING IN FLUTTER FLOW**

#### Android:
- ✅ **Endpoint**: `api/BankAccounts/ListBankCards`
- ✅ **Called after**: Final sign-in (keystoreType=1) and PIN setup
- ✅ **Purpose**: Load user's bank cards
- ✅ **Request Model**: `RequestInfoRequest`
- ✅ **Response Model**: `BankCardsResponse`

#### Flutter:
- ✅ **Endpoint exists**: `api/BankAccounts/ListBankCards`
- ✅ **API method exists**: `listBankCards()`
- ❌ **NOT CALLED** - Missing from sign-in flow
- ❌ **Called separately** - Only called from HomeBloc, not during authentication

**Impact**: Bank cards are not loaded before navigating to home screen.

---

### 4. **ListBankAccounts API - MISSING IN FLUTTER FLOW**

#### Android:
- ✅ **Endpoint**: `api/BankAccounts/ListBankAccounts`
- ✅ **Called after**: ListBankCards succeeds
- ✅ **Purpose**: Load user's bank accounts
- ✅ **Request Model**: `RequestInfoRequest`
- ✅ **Response Model**: `BankAccountsResponse`

#### Flutter:
- ✅ **Endpoint exists**: `api/BankAccounts/ListBankAccounts`
- ✅ **API method exists**: `listBankAccounts()`
- ❌ **NOT CALLED** - Missing from sign-in flow
- ❌ **Called separately** - Only called from HomeBloc, not during authentication

**Impact**: Bank accounts are not loaded before navigating to home screen.

---

## 📋 Complete API Call Sequences

### Android Login Flow (Sign-In by Number/Email)

```
1. Sign-In API
   POST: api/SingInUp/SignIn
   Request: SignInRequest {
     RequestInfo: { MobileUser: { username, passwordHash } },
     KeystoreType: 0,  // No keystore yet
     SignInType: 1,    // Sign-in (not sign-up)
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignInResponse {
     SessionKey: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
2. ChangeKeystore API
   POST: api/MobileUser/ChangeKeystore
   Request: ChangeKeystoreRequest {
     RequestInfo: { MobileUser: { saltSignature: sessionKey } },
     KeystoreType: 1,  // Set up keystore
     MobileDeviceSpecifications: {
       Fingerprint: "Available",
       Face: "Available",
       Iris: "NotAvailable"
     }
   }
   Response: ChangeKeystoreResponse {
     PasswordHash: "...",  // New password hash for final sign-in
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (Navigate to PIN Setup Screen)
   
3. Final Sign-In API (After PIN Setup)
   POST: api/SingInUp/SignIn
   Request: SignInRequest {
     RequestInfo: { MobileUser: { username, passwordHash: newHash } },
     KeystoreType: 1,  // Keystore is set up
     SignInType: SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL,
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignInResponse {
     SessionKey: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
4. ListBankCards API
   POST: api/BankAccounts/ListBankCards
   Request: RequestInfoRequest {
     RequestInfo: { MobileUser: { sessionKey } }
   }
   Response: BankCardsResponse {
     BankCards: [...],
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
5. ListBankAccounts API
   POST: api/BankAccounts/ListBankAccounts
   Request: RequestInfoRequest {
     RequestInfo: { MobileUser: { sessionKey } }
   }
   Response: BankAccountsResponse {
     BankAccounts: [...],
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
6. SendFCMToken API (Optional, fire-and-forget)
   POST: api/MobileUser/ChangeDevicePushInfoToken
   Request: FcmTokenRequest {
     RequestInfo: { ... },
     FCMToken: "..."
   }
   
   ↓ (Navigate to Fingerprint Screen or Home)
```

### Flutter Current Login Flow

```
1. Sign-In API
   POST: api/SingInUp/SignIn
   Request: SignInRequest {
     RequestInfo: { MobileUser: { username, passwordHash } },
     KeystoreType: 0,
     SignInType: 1,
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignInResponse {
     SessionKey: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
   ❌ MISSING: ChangeKeystore API
   ❌ MISSING: PIN Setup Screen
   ❌ MISSING: Final Sign-In API (keystoreType=1)
   ❌ MISSING: ListBankCards API
   ❌ MISSING: ListBankAccounts API
   ❌ MISSING: SendFCMToken API
   
   ↓ (Navigate directly to Home)
```

**Problems:**
1. ❌ No ChangeKeystore call
2. ❌ No PIN setup
3. ❌ No final sign-in with keystoreType=1
4. ❌ No bank data loading
5. ❌ No FCM token registration

---

### Android Sign-Up Flow (By Number)

```
1. SendCardNumber API (if needed)
   POST: api/SingInUp/SignUp
   Request: CardSendRequest {
     RequestInfo: { ... },
     SignUpType: SIGN_UP_TYPE_NUMBER,
     PAN: "",
     CustomerNumber: "",
     CustomerBirthdate: "",
     MobileNumber: "+994501234567",
     MobileNumberSecretCode: ""
   }
   Response: CardSendResponse {
     MobileNumber: "+994501234567",
     Email: "user@example.com",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (Navigate to Verification Screen)
   
2. VerifyCode API
   POST: api/SingInUp/VerifyCode
   Request: VerifyCodeRequest {
     RequestInfo: { ... },
     SignUpType: SIGN_UP_TYPE_NUMBER,
     VerificationCode: "123456",
     PAN: null,
     CustomerNumber: null,
     CustomerBirthdate: null
   }
   Response: VerifyCodeResponse {
     MobileUserSignUpStatus: 0,  // 0 = need to complete sign-up
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If MobileUserSignUpStatus == 0, navigate to Sign-Up Screen)
   
3. RegisterMobileUser API
   POST: api/SingInUp/RegisterMobileUser
   Request: SignUpRequest {
     RequestInfo: { MobileUser: { username, passwordHash } },
     UsernameType: SIGN_IN_UP_TYPE_NUMBER,
     SignUpType: SIGN_UP_TYPE_NUMBER,
     PAN: null,
     CustomerNumber: null,
     CustomerBirthdate: null,
     VerificationCode: "123456",
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignUpResponse {
     ResponseInfo: { ResponseType: 0, ErrorCode: 0 }
   }
   
   ↓ (If ResponseType == 0 && ErrorCode == 0, navigate to PIN Setup)
   
4. Sign-In API (First - for ChangeKeystore)
   POST: api/SingInUp/SignIn
   Request: SignInRequest {
     RequestInfo: { MobileUser: { username, passwordHash } },
     KeystoreType: 0,  // No keystore yet
     SignInType: SIGN_IN_UP_TYPE_NUMBER,
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignInResponse {
     SessionKey: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
5. ChangeKeystore API
   POST: api/MobileUser/ChangeKeystore
   Request: ChangeKeystoreRequest {
     RequestInfo: { MobileUser: { saltSignature: sessionKey } },
     KeystoreType: 1,
     MobileDeviceSpecifications: { ... }
   }
   Response: ChangeKeystoreResponse {
     PasswordHash: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
6. Final Sign-In API (After PIN Setup)
   POST: api/SingInUp/SignIn
   Request: SignInRequest {
     RequestInfo: { MobileUser: { username, passwordHash: newHash } },
     KeystoreType: 1,  // Keystore is set up
     SignInType: SIGN_IN_UP_TYPE_NUMBER,
     MobileNumber: null,
     MobileNumberSecretCode: null
   }
   Response: SignInResponse {
     SessionKey: "...",
     ResponseInfo: { ResponseType: 0 }
   }
   
   ↓ (If ResponseType == 0)
   
7. ListBankCards API
   POST: api/BankAccounts/ListBankCards
   ...
   
8. ListBankAccounts API
   POST: api/BankAccounts/ListBankAccounts
   ...
   
9. SendFCMToken API
   POST: api/MobileUser/ChangeDevicePushInfoToken
   ...
```

### Flutter Current Sign-Up Flow

```
1. SendCardNumber API (if needed)
   ✅ Implemented
   ✅ Called correctly
   
2. VerifyCode API
   ✅ Implemented
   ✅ Called correctly
   
3. RegisterMobileUser API
   ✅ Implemented
   ✅ Called correctly
   
   ↓ (After RegisterMobileUser success)
   
   ❌ MISSING: Sign-In API (keystoreType=0)
   ❌ MISSING: ChangeKeystore API
   ❌ MISSING: PIN Setup Screen
   ❌ MISSING: Final Sign-In API (keystoreType=1)
   ❌ MISSING: ListBankCards API
   ❌ MISSING: ListBankAccounts API
   ❌ MISSING: SendFCMToken API
   
   ↓ (Navigate directly - incomplete flow)
```

---

## 📊 API Comparison Table

| API Endpoint | Android | Flutter | Status |
|--------------|---------|---------|--------|
| **Login Flow** |
| `api/SingInUp/SignIn` (keystoreType=0) | ✅ | ✅ | ✅ Working |
| `api/MobileUser/ChangeKeystore` | ✅ | ❌ | ❌ **MISSING** |
| `api/SingInUp/SignIn` (keystoreType=1) | ✅ | ❌ | ❌ **MISSING** |
| `api/BankAccounts/ListBankCards` | ✅ | ⚠️ | ⚠️ Not in flow |
| `api/BankAccounts/ListBankAccounts` | ✅ | ⚠️ | ⚠️ Not in flow |
| `api/MobileUser/ChangeDevicePushInfoToken` | ✅ | ❌ | ❌ **MISSING** |
| **Sign-Up Flow** |
| `api/SingInUp/SignUp` (SendCardNumber) | ✅ | ✅ | ✅ Working |
| `api/SingInUp/VerifyCode` | ✅ | ✅ | ✅ Working |
| `api/SingInUp/RegisterMobileUser` | ✅ | ✅ | ✅ Working |
| `api/SingInUp/SignIn` (keystoreType=0) | ✅ | ❌ | ❌ **MISSING** |
| `api/MobileUser/ChangeKeystore` | ✅ | ❌ | ❌ **MISSING** |
| `api/SingInUp/SignIn` (keystoreType=1) | ✅ | ❌ | ❌ **MISSING** |
| `api/BankAccounts/ListBankCards` | ✅ | ⚠️ | ⚠️ Not in flow |
| `api/BankAccounts/ListBankAccounts` | ✅ | ⚠️ | ⚠️ Not in flow |
| `api/MobileUser/ChangeDevicePushInfoToken` | ✅ | ❌ | ❌ **MISSING** |

---

## 🔧 Missing API Implementations

### 1. ChangeKeystore API

**Required in ApiService:**
```dart
@POST(ApiEndpoints.changeKeystore)
Future<ChangeKeystoreResponse> changeKeystore(
  @Body() ChangeKeystoreRequest request
);
```

**Required Models:**
- `ChangeKeystoreRequest`
- `ChangeKeystoreResponse`
- `MobileDeviceSpecifications`

**Required in AuthRepository:**
```dart
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
  return await _apiService.changeKeystore(request);
}
```

### 2. SendFCMToken API

**Required in ApiService:**
```dart
@POST(ApiEndpoints.sendFCMToken)
Future<ApiResponse<EmptyResponse>> sendFCMToken(
  @Body() FcmTokenRequest request
);
```

**Required Models:**
- `FcmTokenRequest`

**Required in AuthRepository:**
```dart
Future<void> sendFCMToken({
  required RequestInfo requestInfo,
  required String fcmToken,
}) async {
  final request = FcmTokenRequest(
    requestInfo: requestInfo,
    fcmToken: fcmToken,
  );
  // Fire and forget
  try {
    await _apiService.sendFCMToken(request);
  } catch (e) {
    // Ignore errors
  }
}
```

### 3. ListBankCards in Auth Flow

**Already exists but needs to be called:**
```dart
// In AuthRepository or AuthBloc
Future<List<BankCard>> loadBankCards({
  required RequestInfo requestInfo,
}) async {
  final request = {'RequestInfo': requestInfo.toJson()};
  final response = await _apiService.listBankCards(request);
  if (response.responseInfo.responseType == 0) {
    return response.data ?? [];
  }
  throw Exception(response.responseInfo.responseMessage);
}
```

### 4. ListBankAccounts in Auth Flow

**Already exists but needs to be called:**
```dart
// In AuthRepository or AuthBloc
Future<List<BankAccount>> loadBankAccounts({
  required RequestInfo requestInfo,
}) async {
  final request = {'RequestInfo': requestInfo.toJson()};
  final response = await _apiService.listBankAccounts(request);
  if (response.responseInfo.responseType == 0) {
    return response.data ?? [];
  }
  throw Exception(response.responseInfo.responseMessage);
}
```

---

## 📋 Required Model Classes

### 1. ChangeKeystoreRequest
```dart
@JsonSerializable(explicitToJson: true)
class ChangeKeystoreRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;
  
  @JsonKey(name: 'KeystoreType')
  final int keystoreType;
  
  @JsonKey(name: 'MobileDeviceSpecifications')
  final MobileDeviceSpecifications mobileDeviceSpecifications;
  
  // ... constructors, fromJson, toJson
}
```

### 2. ChangeKeystoreResponse
```dart
@JsonSerializable(explicitToJson: true)
class ChangeKeystoreResponse {
  @JsonKey(name: 'ResponseInfo')
  final ResponseInfo responseInfo;
  
  @JsonKey(name: 'PasswordHash')
  final String passwordHash;
  
  // ... constructors, fromJson, toJson
}
```

### 3. MobileDeviceSpecifications
```dart
@JsonSerializable()
class MobileDeviceSpecifications {
  @JsonKey(name: 'Fingerprint')
  final String fingerprint;  // "Available" or "NotAvailable"
  
  @JsonKey(name: 'Face')
  final String face;  // "Available" or "NotAvailable"
  
  @JsonKey(name: 'Iris')
  final String iris;  // "Available" or "NotAvailable"
  
  // ... constructors, fromJson, toJson
}
```

### 4. FcmTokenRequest
```dart
@JsonSerializable(explicitToJson: true)
class FcmTokenRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;
  
  @JsonKey(name: 'FCMToken')
  final String fcmToken;
  
  // ... constructors, fromJson, toJson
}
```

---

## 🎯 Corrected API Call Sequence

### Login Flow (Corrected)

```dart
// Step 1: Initial Sign-In
final signInResponse = await authRepository.signIn(
  requestInfo: requestInfo,
  keystoreType: 0,
  signInType: 1,
);

if (signInResponse.responseInfo.responseType == 0) {
  // Save session key
  await secureStorage.write(
    key: AppConstants.sessionKey,
    value: signInResponse.sessionKey!,
  );
  
  // Set saltSignature for ChangeKeystore
  requestInfo.mobileUser.saltSignature = signInResponse.sessionKey;
  
  // Step 2: ChangeKeystore
  final changeKeystoreResponse = await authRepository.changeKeystore(
    requestInfo: requestInfo,
    keystoreType: 1,
    deviceSpecs: MobileDeviceSpecifications(
      fingerprint: "Available",
      face: "Available",
      iris: "NotAvailable",
    ),
  );
  
  if (changeKeystoreResponse.responseInfo.responseType == 0) {
    // Navigate to PIN Setup Screen
    // User enters PIN (4 digits, confirmed)
    
    // Step 3: Final Sign-In with PIN
    final finalSignInResponse = await authRepository.signIn(
      requestInfo: requestInfo.copyWith(
        mobileUser: requestInfo.mobileUser.copyWith(
          passwordHash: changeKeystoreResponse.passwordHash,
        ),
      ),
      keystoreType: 1,  // Keystore is set up
      signInType: signInType,  // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL
    );
    
    if (finalSignInResponse.responseInfo.responseType == 0) {
      // Save PIN hash, username, password hash, session key
      await secureStorage.write(
        key: AppConstants.pinHash,
        value: AppUtils.passwordHash(pin),
      );
      await secureStorage.write(
        key: AppConstants.username,
        value: username,
      );
      await secureStorage.write(
        key: AppConstants.passwordHash,
        value: changeKeystoreResponse.passwordHash,
      );
      await secureStorage.write(
        key: AppConstants.sessionKey,
        value: finalSignInResponse.sessionKey!,
      );
      
      // Step 4: Load Bank Cards
      final bankCards = await authRepository.loadBankCards(
        requestInfo: requestInfo,
      );
      
      // Step 5: Load Bank Accounts
      final bankAccounts = await authRepository.loadBankAccounts(
        requestInfo: requestInfo,
      );
      
      // Step 6: Send FCM Token (optional)
      final fcmToken = await getFCMToken();
      if (fcmToken != null) {
        await authRepository.sendFCMToken(
          requestInfo: requestInfo,
          fcmToken: fcmToken,
        );
      }
      
      // Now emit AuthAuthenticated and navigate to home
      emit(AuthAuthenticated());
    }
  }
}
```

---

## ⚠️ Critical Issues

1. **ChangeKeystore is REQUIRED**: Without this API call, the device cannot set up keystore for PIN authentication.

2. **Two Sign-In Calls**: Android makes TWO sign-in calls:
   - First: `keystoreType=0` (before ChangeKeystore)
   - Second: `keystoreType=1` (after PIN setup)

3. **Bank Data Must Load**: Bank cards and accounts must be loaded BEFORE navigating to home.

4. **FCM Token Registration**: Should be called after successful authentication (fire-and-forget).

5. **API Call Order Matters**: The sequence must match Android exactly:
   - Sign-In (0) → ChangeKeystore → PIN Setup → Sign-In (1) → Load Data → Home

---

## 📝 Summary

**Missing APIs in Flutter:**
- ❌ `ChangeKeystore` API (endpoint exists, but no implementation)
- ❌ `SendFCMToken` API (endpoint exists, but no implementation)
- ⚠️ `ListBankCards` (exists but not called in auth flow)
- ⚠️ `ListBankAccounts` (exists but not called in auth flow)

**Missing Models:**
- ❌ `ChangeKeystoreRequest`
- ❌ `ChangeKeystoreResponse`
- ❌ `MobileDeviceSpecifications`
- ❌ `FcmTokenRequest`

**Incorrect Flow:**
- ❌ Flutter skips ChangeKeystore
- ❌ Flutter skips PIN setup
- ❌ Flutter skips final sign-in
- ❌ Flutter doesn't load bank data before navigation

**The Flutter authentication flow is incomplete and does not match the Android implementation.**









