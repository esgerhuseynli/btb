# Sign-In Flow After VerifyCode - Implementation Guide for Flutter

## Overview
After successful VerifyCode with `mobileUserSignUpStatus == 1`, the app should navigate to Sign-In screen, then proceed through sign-in and PIN setup flow.

## Complete Flow

### Step 1: VerifyCode Success
**Response from VerifyCode API:**
```json
{
  "responceInfo": {
    "responseType": 0,
    "responseMessage": "Təsdiqləmə kodunuz doğrudur!",
    ...
  },
  "verificationCodeResult": 1,
  "mobileUserSignUpStatus": 1  // This indicates sign-up is complete
}
```

**Action:** Navigate to Sign-In Screen with phone/email parameters

---

### Step 2: Sign-In Screen (Initial Screen)
**Screen:** `SignInFragment` / `sign_in_screen.dart`

**Parameters:**
- `phone`: String? (e.g., "+994501234567")
- `email`: String? (e.g., "user@example.com")

**UI Elements:**
- "Sign in by mobile number" button (enabled if phone is not null/empty)
- "Sign in by email" button (enabled if email is not null/empty)
- "Sign up" button (optional, for new users)
- Back button

**User Action:**
- User clicks "Sign in by mobile number" → Navigate to Sign-In by Number screen
- User clicks "Sign in by email" → Navigate to Sign-In by Email screen

---

### Step 3: Sign-In by Number Screen
**Screen:** `SignInByNumberFragment` / `sign_in_by_number_screen.dart`

**Parameters:**
- `phone`: String? (e.g., "+994501234567")

**UI Elements:**
- Mobile number field (pre-filled with phone.substring(4) if phone provided, disabled)
- Password field (editable)
- "Sign in" button
- "Forgot password" button
- Back button

**Form Validation:**
- Mobile number must be 17 characters (includes spaces in format)
- Password must not be empty

**User Action:** User enters password and clicks "Sign in"

---

### Step 4: Sign-In API Call

**Endpoint:** `POST api/SingInUp/SignIn`

**Request Model (`SignInRequest`):**
```dart
{
  "RequestInfo": {
    "MobileUser": {
      "Username": "+994501234567",  // Phone number without spaces
      "PasswordHash": "SHA512_HASH_OF_PASSWORD",  // SHA-512 uppercase hex
      "SessionKey": null,
      "SaltSignature": null
    },
    "DeviceInfo": { ... },
    "AppInfo": { ... },
    "Language": 1
  },
  "KeystoreType": 0,  // 0 for first sign-in (before ChangeKeystore)
  "SignInType": 1,    // Always 1 in SignInRequest (for both phone and email)
  "MobileNumber": null,
  "MobileNumberSecretCode": null
}
```

**Note:** The `SignInType` in `SignInRequest` is always `1` regardless of whether it's phone or email. The actual type (phone vs email) is determined by the `Username` field in `MobileUser` (phone number vs email address). When saving to preferences, use `SIGN_IN_UP_TYPE_NUMBER` (2) for phone or `SIGN_IN_UP_TYPE_EMAIL` (1) for email.

**Password Hashing:**
- Algorithm: SHA-512
- Format: Uppercase hexadecimal string
- Implementation (Android):
  ```java
  MessageDigest md = MessageDigest.getInstance("SHA-512");
  byte[] digest = md.digest(password.getBytes());
  StringBuilder sb = new StringBuilder();
  for (byte b : digest)
      sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
  return sb.toString().toUpperCase();
  ```

**Flutter Implementation:**
```dart
import 'dart:convert';
import 'package:crypto/crypto.dart';

String passwordHash(String password) {
  var bytes = utf8.encode(password);
  var digest = sha512.convert(bytes);
  return digest.toString().toUpperCase();
}
```

**Response Model (`SignInResponse`):**
```json
{
  "responceInfo": {
    "responseType": 0,  // 0 = success
    "responseMessage": "...",
    ...
  },
  "sessionKey": "SESSION_KEY_STRING",
  "signInActionCode": 0  // 3 = device needs registration
}
```

**On Success (responseType == 0):**
1. Save `sessionKey` to local storage (SharedPreferences equivalent)
2. Save `SIGN_IN_TYPE` = `SIGN_IN_UP_TYPE_NUMBER` (value: 2) for phone, or `SIGN_IN_UP_TYPE_EMAIL` (value: 1) for email
3. Set `HAS_ACTIVE_SESSION` = `true` in local storage
4. Set `MobileUser.SaltSignature` = `sessionKey` (important: use this in ChangeKeystore request)
5. Proceed to Step 5: ChangeKeystore

**On Error (signInActionCode == 3):**
- Show dialog: "User registered but current device needs registration"
- Navigate to Sign-Up screen

---

### Step 5: ChangeKeystore API Call

**Endpoint:** `POST api/MobileUser/ChangeKeystore`

**Request Model (`ChangeKeystoreRequest`):**
```dart
{
  "RequestInfo": {
    "MobileUser": {
      "Username": "+994501234567",
      "PasswordHash": "SHA512_HASH_OF_PASSWORD",
      "SessionKey": null,
      "SaltSignature": "SESSION_KEY_FROM_PREVIOUS_STEP"  // From SignIn response
    },
    "DeviceInfo": { ... },
    "AppInfo": { ... },
    "Language": 1
  },
  "KeystoreType": 1,  // 1 for device keystore
  "MobileDeviceSpecifications": {
    "NFC": "Available",
    "FaceID": "Available",
    "TouchID": "NotAvailable"
  }
}
```

**Response Model (`ChangeKeystoreResponse`):**
```json
{
  "responceInfo": {
    "responseType": 0,  // 0 = success
    "responseMessage": "...",
    ...
  },
  "passwordHash": "NEW_PASSWORD_HASH_FOR_PIN_SCREEN"
}
```

**On Success:**
- **Navigate to PIN Setup Screen** (user is NOT authenticated yet!)
- **DO NOT emit AuthAuthenticated() here**
- Pass parameters:
  - `signUpType`: `SIGN_IN_UP_TYPE_NUMBER` (value: 2) for phone, or `SIGN_IN_UP_TYPE_EMAIL` (value: 1) for email
  - `username`: MobileUser.username (e.g., "+994501234567" for phone, or "user@example.com" for email)
  - `passwordHash`: `changeKeystoreResponse.passwordHash` (this is different from the original password hash)
  - `isComingFromSignInScreen`: `true`

---

### Step 6: PIN Setup Screen

**Screen:** `SignUpPinFragment` / `sign_up_pin_screen.dart`

**Parameters:**
- `signUpType`: int (SIGN_IN_UP_TYPE_NUMBER = 2 or SIGN_IN_UP_TYPE_EMAIL = 1)
- `username`: String (phone number or email)
- `passwordHash`: String (from ChangeKeystore response)
- `isComingFromSignInScreen`: bool (true in this flow)

**UI Elements:**
- PIN entry field (4 digits, masked)
- "Confirm PIN" field (4 digits, masked)
- Number pad (0-9)
- Clear/Delete button
- Back button

**User Action:**
- User enters 4-digit PIN twice (for confirmation)
- If PINs match, proceed to Step 7: Final Sign-In with PIN

**PIN Validation:**
- Both PIN entries must be 4 digits
- PINs must match
- Hash the PIN using SHA-512 (same as password hash)

---

### Step 7: Final Sign-In with PIN

**When `isComingFromSignInScreen == true`:**
- Uses `singleSignIn()` method
- `KeystoreType` = 1 (device keystore already set up)

**Sign-In Request:**
```dart
{
  "RequestInfo": {
    "MobileUser": {
      "Username": "+994501234567",
      "PasswordHash": "PASSWORD_HASH_FROM_CHANGE_KEYSTORE_RESPONSE",  // From Step 5, NOT original password hash
      "SessionKey": null,
      "SaltSignature": null
    },
    "DeviceInfo": { ... },
    "AppInfo": { ... },
    "Language": 1
  },
  "KeystoreType": 1,  // 1 because keystore already set up in Step 5
  "SignInType": 2,    // Use SIGN_IN_UP_TYPE_NUMBER (2) for phone, or SIGN_IN_UP_TYPE_EMAIL (1) for email
  "MobileNumber": null,
  "MobileNumberSecretCode": null
}
```

**Important:** The `PasswordHash` here uses the `passwordHash` from `ChangeKeystoreResponse`, NOT the original password hash from Step 4.

**On Success:**
1. Save to local storage:
   - `PIN_HASH`: SHA-512 hash of PIN (uppercase hex)
   - `USERNAME`: MobileUser.username
   - `PASSWORD_HASH`: passwordHash (from ChangeKeystore)
   - `SESSION_KEY`: sessionKey from response
   - `HAS_ACTIVE_SESSION`: true

2. Call `listBankCards` API
3. Call `listBankAccounts` API
4. **NOW emit `AuthAuthenticated()` event** (user is fully authenticated)
5. Navigate to Fingerprint Screen (if available) or Home Screen

---

## Constants Reference

```dart
// Sign-In Types
const SIGN_IN_UP_TYPE_EMAIL = 1;
const SIGN_IN_UP_TYPE_NUMBER = 2;

// SharedPreferences Keys (use equivalent in Flutter: SharedPreferences, Hive, etc.)
const PIN_HASH = "d3";
const PASSWORD_HASH = "d0";
const USERNAME = "d1";
const SESSION_KEY = "d2";
const HAS_ACTIVE_SESSION = "d5";
const SIGN_IN_TYPE = "d4";
```

---

## API Endpoints

1. **Sign-In:** `POST api/SingInUp/SignIn`
2. **ChangeKeystore:** `POST api/MobileUser/ChangeKeystore`
3. **ListBankCards:** `POST api/BankAccounts/ListBankCards` (after PIN setup)
4. **ListBankAccounts:** `POST api/BankAccounts/ListBankAccounts` (after PIN setup)

---

## Error Handling

1. **Sign-In fails (responseType != 0):**
   - Show error message from `responseInfo.responseMessage`
   - Allow user to retry

2. **Sign-In Action Code == 3:**
   - Device needs registration
   - Show dialog and navigate to Sign-Up screen

3. **ChangeKeystore fails:**
   - Show error message
   - Allow user to retry

4. **PIN mismatch:**
   - Show error: "PIN does not match"
   - Clear PIN fields and ask user to re-enter

---

## Flutter Implementation Checklist

- [ ] Create Sign-In Screen widget
- [ ] Create Sign-In by Number Screen widget
- [ ] Create Sign-In by Email Screen widget
- [ ] Implement password hashing (SHA-512)
- [ ] Implement SignIn API call
- [ ] Implement ChangeKeystore API call
- [ ] **Navigate to PIN Setup Screen (DO NOT emit AuthAuthenticated yet)**
- [ ] Implement local storage for session data
- [ ] Create PIN Setup Screen widget
- [ ] Implement PIN hashing (SHA-512)
- [ ] Implement final Sign-In with PIN
- [ ] Implement BankCards and BankAccounts API calls
- [ ] **THEN emit AuthAuthenticated() and navigate to Fingerprint/Home screen**
- [ ] Handle all error cases
- [ ] Implement form validation

---

## ⚠️ IMPORTANT: When to Emit AuthAuthenticated()

**DO NOT emit `AuthAuthenticated()` after ChangeKeystore!**

The correct flow is:

1. **After ChangeKeystore success:**
   - Navigate to PIN Setup Screen
   - **DO NOT emit AuthAuthenticated()**
   - User is still in "unauthenticated" state

2. **After PIN is set and final Sign-In completes:**
   - User enters PIN (4 digits, confirmed)
   - Final Sign-In API call (with KeystoreType = 1)
   - Save PIN_HASH, USERNAME, PASSWORD_HASH, SESSION_KEY to local storage
   - Call listBankCards API
   - Call listBankAccounts API
   - **THEN emit AuthAuthenticated()** ✅
   - Navigate to Fingerprint screen (if available) or Home screen

**Flow Summary:**
```
ChangeKeystore Success
    ↓
Navigate to PIN Setup Screen (❌ still unauthenticated)
    ↓
User sets PIN (4 digits, confirmed)
    ↓
Final Sign-In with PIN (KeystoreType = 1)
    ↓
Save session data (PIN_HASH, USERNAME, PASSWORD_HASH, SESSION_KEY)
    ↓
Load BankCards API
    ↓
Load BankAccounts API
    ↓
✅ NOW emit AuthAuthenticated() (user is fully authenticated)
    ↓
Navigate to Home/Fingerprint screen
```

**Flutter Code Example:**
```dart
// ❌ WRONG - After ChangeKeystore
changeKeystoreResponse.then((response) {
  if (response.responseInfo.responseType == 0) {
    // Navigate to PIN screen
    Navigator.pushNamed(context, '/pin-setup', arguments: {
      'username': username,
      'passwordHash': response.passwordHash,
      'isComingFromSignIn': true,
    });
    // DO NOT emit AuthAuthenticated() here!
  }
});

// ✅ CORRECT - After PIN setup and final sign-in
finalSignInWithPin(pin).then((signInResponse) {
  if (signInResponse.responseInfo.responseType == 0) {
    // Save session data
    await saveSessionData(
      pinHash: passwordHash(pin),
      username: username,
      passwordHash: passwordHash,
      sessionKey: signInResponse.sessionKey,
    );
    
    // Load bank data
    final bankCards = await listBankCards();
    final bankAccounts = await listBankAccounts();
    
    // NOW emit authenticated
    emit(AuthAuthenticated()); // ✅ Correct timing
    
    // Navigate to home
    Navigator.pushReplacementNamed(context, '/home');
  }
});
```

---

## Key Points

1. **Password Hashing:** Always use SHA-512, convert to uppercase hexadecimal
2. **Phone Number Format:** Remove spaces before sending (e.g., "+994501234567")
3. **Session Management:** Store sessionKey after first Sign-In, use it in SaltSignature for ChangeKeystore
4. **KeystoreType:** 0 for first sign-in, 1 for subsequent sign-ins with keystore
5. **SignInType:** 1 for phone (in SignInRequest), but SIGN_IN_UP_TYPE_NUMBER = 2 (check Android code for exact mapping)
6. **Flow Order:** Sign-In → ChangeKeystore → PIN Setup → Final Sign-In → Home

---

## Android Code References

- `SignInByNumberPresenter.java`: Lines 45-107
- `SignInByNumberFragment.java`: Complete file
- `SignInFragment.java`: Complete file
- `SignUpPinPresenter.java`: `singleSignIn()` method (lines 76-141)
- `Utils.java`: `passwordHash()` method (lines 92-106)

