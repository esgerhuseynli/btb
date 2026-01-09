# Login Flow Differences: Android vs Flutter

## Executive Summary

The Flutter login flow is **significantly different** from the Android implementation. The Android app has a **multi-step, multi-screen** login process, while Flutter currently has a **simplified single-screen** approach that **skips critical steps**.

---

## 🔴 Critical Differences

### 1. **Screen Structure - MAJOR DIFFERENCE**

#### Android Flow:
```
Intro Screen
    ↓
Sign-In Screen (Selection Screen)
    ├─→ Sign-In by Number Screen (separate screen)
    └─→ Sign-In by Email Screen (separate screen)
            ↓
        Sign-In API Call
            ↓
        ChangeKeystore API Call
            ↓
        PIN Setup Screen (SignUpPinScreen)
            ↓
        Final Sign-In with PIN
            ↓
        Load Bank Cards
            ↓
        Load Bank Accounts
            ↓
        Fingerprint Screen (optional)
            ↓
        Home Screen
```

#### Flutter Current Flow:
```
Intro Screen
    ↓
Sign-In Screen (Combined - phone/email in one screen)
    ↓
Sign-In API Call
    ↓
Home Screen (❌ Missing steps!)
```

**Missing in Flutter:**
- ❌ Separate `SignInByNumberScreen`
- ❌ Separate `SignInByEmailScreen`
- ❌ `ChangeKeystore` API call after first sign-in
- ❌ `SignUpPinScreen` (PIN setup)
- ❌ Final sign-in with PIN (keystoreType=1)
- ❌ Loading bank cards/accounts before navigation
- ❌ `SignInPinFingerprintScreen` (PIN/Fingerprint authentication)
- ❌ `FingerprintScreen` (fingerprint setup)

---

### 2. **Sign-In Screen Structure**

#### Android:
- **SignInFragment**: Selection screen with 2 buttons
  - "Sign in by mobile number" button
  - "Sign in by email" button
  - "Sign up" button
  - Back button
- **SignInByNumberFragment**: Separate screen for phone login
  - Pre-filled phone number (if provided, disabled)
  - Password field
  - "Forgot password" button
  - "Sign in" button
- **SignInByEmailFragment**: Separate screen for email login
  - Pre-filled email (if provided, disabled)
  - Password field
  - "Forgot password" button
  - "Sign in" button

#### Flutter:
- **SignInScreen**: Combined screen with toggle
  - Phone/Email toggle buttons
  - Single form (phone OR email)
  - Password field
  - "Forgot password" button
  - "Sign in" button
  - "Sign up" link

**Problem**: Flutter combines what Android has as 3 separate screens into 1 screen.

---

### 3. **Sign-In Process Flow**

#### Android Sign-In Process:

**Step 1: Initial Sign-In** (`SignInByNumberPresenter.java` / `SignInByEmailPresenter.java`)
```java
// 1. Create SignInRequest with keystoreType=0, signInType=1
SignInRequest signInRequest = new SignInRequest(
    requestInfo,
    0,  // keystoreType = 0 (no keystore yet)
    1,  // signInType = 1 (sign-in, not sign-up)
    null, null
);

// 2. Call Sign-In API
authService.signIn(signInRequest)
    .flatMap(signInResponse -> {
        if (signInResponse.getResponseInfo().getResponseType() == 0) {
            // 3. Save session data
            editor.putBoolean(HAS_ACTIVE_SESSION, true);
            editor.putString(SESSION_KEY, signInResponse.getSessionKey());
            editor.putInt(SIGN_IN_TYPE, SIGN_IN_UP_TYPE_NUMBER);
            
            // 4. Set saltSignature for ChangeKeystore
            mobileUser.setSaltSignature(signInResponse.getSessionKey());
            
            // 5. Call ChangeKeystore API
            ChangeKeystoreRequest changeKeystoreRequest = new ChangeKeystoreRequest(
                requestInfo, 
                1,  // keystoreType = 1
                new MobileDeviceSpecifications("Available", "Available", "NotAvailable")
            );
            return authService.changeKeystore(changeKeystoreRequest);
        }
    })
    .subscribe(changeKeystoreResponse -> {
        if (changeKeystoreResponse.getResponseInfo().getResponseType() == 0) {
            // 6. Navigate to PIN Setup Screen
            router.replaceScreen(new AuthScreens.SignUpPinScreen(
                SIGN_IN_UP_TYPE_NUMBER,
                username,
                changeKeystoreResponse.getPasswordHash(),
                true  // isComingFromSignInScreen = true
            ));
        }
    });
```

**Step 2: PIN Setup** (`SignUpPinPresenter.java`)
```java
// User enters PIN (4 digits, confirmed)
// Then call final Sign-In with keystoreType=1

SignInRequest signInRequest = new SignInRequest(
    requestInfo,
    1,  // keystoreType = 1 (keystore is set up)
    signUpType,  // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL
    "", ""
);

authService.signIn(signInRequest)
    .flatMap(signInResponse -> {
        if (signInResponse.getResponseInfo().getResponseType() == 0) {
            // Save PIN hash, username, password hash, session key
            editor.putString(PIN_HASH, Utils.passwordHash(pin));
            editor.putString(USERNAME, username);
            editor.putString(PASSWORD_HASH, passwordHash);
            editor.putString(SESSION_KEY, signInResponse.getSessionKey());
            
            // Load bank cards
            return authService.listBankCards(requestInfo);
        }
    })
    .flatMap(bankCardsResponse -> {
        // Load bank accounts
        return authService.listBankAccounts(requestInfo);
    })
    .subscribe(bankAccountsResponse -> {
        // Navigate to Fingerprint screen or Home
        if (Utils.isFingerprintServiceAvailable(activity))
            router.navigateTo(new AuthScreens.FingerprintScreen(true));
        else
            router.newRootScreen(new MainScreens.HomeNavScreen());
    });
```

#### Flutter Current Process:

**Current Flow** (`auth_bloc.dart`):
```dart
// 1. Create SignInRequest with keystoreType=0, signInType=1
final response = await _authRepository.signIn(
  requestInfo: requestInfo,
  keystoreType: 0,  // ✅ Correct
  signInType: 1,    // ✅ Correct
);

// 2. If success, save session data
if (response.isSuccess) {
  await _secureStorage.write(key: AppConstants.sessionKey, value: response.sessionKey!);
  await _secureStorage.write(key: AppConstants.hasActiveSession, value: 'true');
  
  // ❌ MISSING: ChangeKeystore API call
  // ❌ MISSING: Navigate to PIN Setup Screen
  // ❌ MISSING: Final Sign-In with PIN (keystoreType=1)
  // ❌ MISSING: Load Bank Cards
  // ❌ MISSING: Load Bank Accounts
  
  emit(const AuthAuthenticated());  // ❌ Too early!
  // Navigate directly to home
}
```

**Problems:**
1. ❌ **No ChangeKeystore call** after initial sign-in
2. ❌ **No PIN setup screen** - user never sets PIN
3. ❌ **No final sign-in with keystoreType=1**
4. ❌ **No bank cards/accounts loading** before navigation
5. ❌ **No fingerprint screen** option
6. ❌ **Emits AuthAuthenticated too early** - should only emit after PIN setup and final sign-in

---

### 4. **PIN/Fingerprint Authentication Screen**

#### Android:
- **SignInPinFingerprintFragment**: Screen shown when user has active session
  - Shows customer name
  - PIN pad (4 digits)
  - Fingerprint authentication (if enabled)
  - Logout button
  - After PIN verification:
    1. Check PIN hash matches stored PIN_HASH
    2. Sign-In with keystoreType=1
    3. Load bank cards
    4. Load bank accounts
    5. Navigate to HomeNavScreen

#### Flutter:
- ❌ **Completely missing** - No PIN/Fingerprint screen
- ❌ No PIN authentication for returning users
- ❌ No fingerprint authentication

---

### 5. **Navigation After Sign-In**

#### Android:
After successful sign-in (with PIN):
1. Load bank cards API
2. Load bank accounts API
3. Save cards/accounts to app state
4. Check if fingerprint is available
5. If yes → Navigate to `FingerprintScreen`
6. If no → Navigate to `HomeNavScreen`

#### Flutter:
After sign-in:
1. ❌ No bank cards loading
2. ❌ No bank accounts loading
3. ❌ No fingerprint check
4. ✅ Navigate directly to `HomeScreen` (but data not loaded)

---

### 6. **Error Handling**

#### Android:
- **SignInActionCode = 3**: User registered but device not registered
  - Shows dialog: "Yenidən qeydiyyatdan keçin" (Re-register)
  - User clicks OK → Navigate to SignUpTypesScreen

#### Flutter:
- ✅ Handles `signInActionCode == 3`
- ✅ Shows dialog
- ✅ Navigates to SignUpTypesScreen
- **BUT**: Missing the exact Android dialog UI

---

### 7. **Session Management**

#### Android:
After PIN setup, saves:
- `HAS_ACTIVE_SESSION = true`
- `PIN_HASH = SHA-512(pin)`
- `USERNAME = username`
- `PASSWORD_HASH = passwordHash`
- `SESSION_KEY = sessionKey`
- `SIGN_IN_TYPE = SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL`
- `IS_FINGERPRINT_ENABLED = true/false`

#### Flutter:
Currently saves:
- ✅ `HAS_ACTIVE_SESSION = true`
- ✅ `SESSION_KEY = sessionKey`
- ✅ `USERNAME = username`
- ✅ `PASSWORD_HASH = passwordHash`
- ✅ `SIGN_IN_TYPE = number/email`
- ❌ **Missing**: `PIN_HASH` (because no PIN setup)
- ❌ **Missing**: `IS_FINGERPRINT_ENABLED`

---

## 📋 Complete Missing Features

### Screens:
1. ❌ `SignInByNumberScreen` - Separate phone login screen
2. ❌ `SignInByEmailScreen` - Separate email login screen
3. ❌ `SignUpPinScreen` - PIN setup screen (used for both sign-up and sign-in)
4. ❌ `SignInPinFingerprintScreen` - PIN/Fingerprint authentication for returning users
5. ❌ `FingerprintScreen` - Fingerprint setup screen

### API Calls:
1. ❌ `ChangeKeystore` API call after initial sign-in
2. ❌ Final `SignIn` with `keystoreType=1` after PIN setup
3. ❌ `ListBankCards` API call before navigation
4. ❌ `ListBankAccounts` API call before navigation
5. ❌ `SendFCMToken` API call (configured but not called)

### Flow Steps:
1. ❌ PIN setup after initial sign-in
2. ❌ Final sign-in with PIN
3. ❌ Bank cards/accounts loading
4. ❌ Fingerprint setup/authentication
5. ❌ PIN authentication for returning users

### Data Storage:
1. ❌ `PIN_HASH` storage
2. ❌ `IS_FINGERPRINT_ENABLED` storage
3. ❌ Bank cards/accounts in app state

---

## 🔧 Required Fixes

### Priority 1: Critical (Must Fix)

1. **Split Sign-In Screen**
   - Create `SignInByNumberScreen`
   - Create `SignInByEmailScreen`
   - Keep `SignInScreen` as selection screen

2. **Add ChangeKeystore Flow**
   - After initial sign-in success
   - Call `ChangeKeystore` API
   - Navigate to PIN setup screen

3. **Add PIN Setup Screen**
   - Create `SignUpPinScreen`
   - User enters 4-digit PIN (twice for confirmation)
   - After PIN setup, call final Sign-In with `keystoreType=1`
   - Save PIN_HASH

4. **Add Bank Data Loading**
   - After final sign-in, load bank cards
   - After bank cards, load bank accounts
   - Save to app state
   - Only then emit `AuthAuthenticated`

5. **Add Fingerprint Screen**
   - After bank data loaded
   - Check if fingerprint available
   - Navigate to FingerprintScreen or HomeScreen

### Priority 2: Important (Should Fix)

6. **Add SignInPinFingerprintScreen**
   - For returning users with active session
   - PIN pad + fingerprint authentication
   - After verification, load bank data and go to home

7. **Fix Navigation Flow**
   - Update `app_router.dart` with all missing routes
   - Implement proper navigation stack management

8. **Update AuthBloc**
   - Add events for ChangeKeystore, PIN setup, final sign-in
   - Add states for PIN setup, fingerprint setup
   - Fix `AuthAuthenticated` emission timing

---

## 📊 Comparison Table

| Feature | Android | Flutter | Status |
|---------|---------|---------|--------|
| Sign-In Selection Screen | ✅ | ✅ | ✅ Working |
| Sign-In by Number Screen | ✅ | ❌ | ❌ Missing |
| Sign-In by Email Screen | ✅ | ❌ | ❌ Missing |
| Combined Sign-In Screen | ❌ | ✅ | ⚠️ Different approach |
| ChangeKeystore API | ✅ | ❌ | ❌ Missing |
| PIN Setup Screen | ✅ | ❌ | ❌ Missing |
| Final Sign-In with PIN | ✅ | ❌ | ❌ Missing |
| Bank Cards Loading | ✅ | ❌ | ❌ Missing |
| Bank Accounts Loading | ✅ | ❌ | ❌ Missing |
| Fingerprint Setup | ✅ | ❌ | ❌ Missing |
| PIN/Fingerprint Auth Screen | ✅ | ❌ | ❌ Missing |
| PIN_HASH Storage | ✅ | ❌ | ❌ Missing |
| FCM Token Sending | ✅ | ❌ | ❌ Missing |

---

## 🎯 Recommended Implementation Order

1. **Step 1**: Split Sign-In screen into 3 screens (selection, number, email)
2. **Step 2**: Add ChangeKeystore API call after initial sign-in
3. **Step 3**: Create PIN Setup screen
4. **Step 4**: Add final sign-in with PIN (keystoreType=1)
5. **Step 5**: Add bank cards/accounts loading
6. **Step 6**: Add fingerprint screen
7. **Step 7**: Add SignInPinFingerprintScreen for returning users
8. **Step 8**: Update navigation routes
9. **Step 9**: Test complete flow

---

## ⚠️ Important Notes

1. **PIN is REQUIRED**: Android app always requires PIN setup after first sign-in. Flutter currently skips this entirely.

2. **Two Sign-In Calls**: Android makes TWO sign-in API calls:
   - First: `keystoreType=0` (initial sign-in)
   - Second: `keystoreType=1` (after PIN setup)

3. **Bank Data Must Load**: Android loads bank cards and accounts BEFORE navigating to home. Flutter navigates immediately without data.

4. **Fingerprint is Optional**: If fingerprint is not available, Android goes directly to home. But PIN is always required.

5. **Session Check**: Android checks for active session on app start. If session exists, shows PIN/Fingerprint screen instead of intro.

---

## Conclusion

The Flutter login flow is **incomplete** and **significantly different** from Android. The current implementation:
- ✅ Has basic sign-in functionality
- ❌ Missing critical security steps (PIN setup)
- ❌ Missing proper flow (ChangeKeystore, final sign-in)
- ❌ Missing data loading (bank cards/accounts)
- ❌ Missing authentication screens (PIN/Fingerprint)

**The Flutter app cannot be considered feature-complete until these differences are addressed.**









