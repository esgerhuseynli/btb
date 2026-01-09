# Flutter Implementation Following Android Source Code Exactly

This document provides the exact flow from Android source code for implementing in Flutter.

## Complete Flow After VerifyCode Success (mobileUserSignUpStatus == 1)

### Step 1: Navigate to Sign-In Screen

**Location:** `VerificationPresenter.java` line 59-61

```dart
// After VerifyCode success with mobileUserSignUpStatus == 1
if (verifyCodeResponse.mobileUserSignUpStatus == 1) {
  // Navigate to Sign-In screen
  Navigator.pushReplacementNamed(
    context,
    '/sign-in',
    arguments: {
      'phone': phone,  // e.g., "+994501234567"
      'email': email,  // e.g., "user@example.com"
    },
  );
}
```

---

### Step 2: Sign-In Screen (Initial Screen)

**Android File:** `SignInFragment.java`

**UI:**
- "Sign in by mobile number" button (enabled if phone != null && !phone.isEmpty)
- "Sign in by email" button (enabled if email != null && !email.isEmpty)
- "Sign up" button (optional)
- Back button

**User Action:**
- Click "Sign in by mobile number" → Navigate to Sign-In by Number screen with phone parameter
- Click "Sign in by email" → Navigate to Sign-In by Email screen with email parameter

**Flutter Implementation:**
```dart
// sign_in_screen.dart
ElevatedButton(
  onPressed: phone != null && phone.isNotEmpty
      ? () => Navigator.pushNamed(
            context,
            '/sign-in-by-number',
            arguments: {'phone': phone},
          )
      : null,
  child: Text('Sign in by mobile number'),
),

ElevatedButton(
  onPressed: email != null && email.isNotEmpty
      ? () => Navigator.pushNamed(
            context,
            '/sign-in-by-email',
            arguments: {'email': email},
          )
      : null,
  child: Text('Sign in by email'),
),
```

---

### Step 3: Sign-In by Number Screen

**Android File:** `SignInByNumberFragment.java`, `SignInByNumberPresenter.java`

**UI Elements:**
- Mobile number field (pre-filled with `phone.substring(4)` if phone provided, disabled)
- Password field (editable)
- "Sign in" button
- "Forgot password" button
- Back button

**Form Validation:**
- Mobile number must be 17 characters (includes spaces in format)
- Password must not be empty

**User Action:** User enters password and clicks "Sign in"

**Flutter Implementation:**
```dart
// sign_in_by_number_screen.dart
String? phone = ModalRoute.of(context)!.settings.arguments as String?;
String phoneDisplay = phone != null && phone.length > 4 
    ? phone.substring(4)  // Remove "+994"
    : '';

TextFormField(
  initialValue: phoneDisplay,
  enabled: false,
  decoration: InputDecoration(labelText: 'Mobile Number'),
),

TextFormField(
  obscureText: true,
  decoration: InputDecoration(labelText: 'Password'),
  onChanged: (value) => password = value,
),

ElevatedButton(
  onPressed: _handleSignIn,
  child: Text('Sign in'),
),
```

---

### Step 4: Sign-In API Call

**Android File:** `SignInByNumberPresenter.java` lines 45-107

**API Endpoint:** `POST api/SingInUp/SignIn`

**Request Preparation:**
```dart
// 1. Create MobileUser with phone and password hash
String phoneNumberWithoutSpaces = phoneNumber.replaceAll(' ', '');
String passwordHash = _passwordHash(password); // SHA-512 uppercase hex

MobileUser mobileUser = MobileUser(
  username: phoneNumberWithoutSpaces,  // e.g., "+994501234567"
  passwordHash: passwordHash,
  sessionKey: null,
  saltSignature: null,
);

// 2. Set MobileUser in RequestInfo
RequestInfo requestInfo = getRequestInfo(); // Get existing RequestInfo
requestInfo.mobileUser = mobileUser;

// 3. Create SignInRequest
SignInRequest signInRequest = SignInRequest(
  requestInfo: requestInfo,
  keystoreType: 0,  // 0 for first sign-in
  signInType: 1,    // Always 1 in SignInRequest
  mobileNumber: null,
  mobileNumberSecretCode: null,
);
```

**Password Hashing (SHA-512):**
```dart
import 'dart:convert';
import 'package:crypto/crypto.dart';

String passwordHash(String password) {
  var bytes = utf8.encode(password);
  var digest = sha512.convert(bytes);
  return digest.toString().toUpperCase(); // Must be uppercase
}
```

**API Request:**
```dart
final response = await http.post(
  Uri.parse('$baseUrl/api/SingInUp/SignIn'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'RequestInfo': {
      'MobileUser': {
        'Username': mobileUser.username,
        'PasswordHash': mobileUser.passwordHash,
        'SessionKey': null,
        'SaltSignature': null,
      },
      'DeviceInfo': requestInfo.deviceInfo.toJson(),
      'AppInfo': requestInfo.appInfo.toJson(),
      'Language': requestInfo.language,
    },
    'KeystoreType': 0,
    'SignInType': 1,
    'MobileNumber': null,
    'MobileNumberSecretCode': null,
  }),
);

final signInResponse = SignInResponse.fromJson(jsonDecode(response.body));
```

**Response Handling:**
```dart
if (signInResponse.responseInfo.responseType == 0) {
  // Success
  String sessionKey = signInResponse.sessionKey;
  
  // Save to local storage
  await prefs.setBool('HAS_ACTIVE_SESSION', true);
  await prefs.setString('SESSION_KEY', sessionKey);
  await prefs.setInt('SIGN_IN_TYPE', 2); // SIGN_IN_UP_TYPE_NUMBER = 2
  
  // Set SaltSignature for next API call
  mobileUser.saltSignature = sessionKey;
  requestInfo.mobileUser = mobileUser;
  
  // Proceed to ChangeKeystore
  await _callChangeKeystore(requestInfo);
  
} else if (signInResponse.signInActionCode == 3) {
  // Device needs registration
  _showSignUpDialog();
} else {
  // Show error
  _showError(signInResponse.responseInfo.responseMessage);
}
```

---

### Step 5: ChangeKeystore API Call

**Android File:** `SignInByNumberPresenter.java` lines 72-74, 83-96

**API Endpoint:** `POST api/MobileUser/ChangeKeystore`

**Request:**
```dart
ChangeKeystoreRequest changeKeystoreRequest = ChangeKeystoreRequest(
  requestInfo: requestInfo, // With MobileUser.SaltSignature set to sessionKey
  keystoreType: 1,  // 1 for device keystore
  mobileDeviceSpecifications: MobileDeviceSpecifications(
    nfc: 'Available',
    faceID: 'Available',
    touchID: 'NotAvailable',
  ),
);

final response = await http.post(
  Uri.parse('$baseUrl/api/MobileUser/ChangeKeystore'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'RequestInfo': {
      'MobileUser': {
        'Username': requestInfo.mobileUser.username,
        'PasswordHash': requestInfo.mobileUser.passwordHash,
        'SessionKey': null,
        'SaltSignature': requestInfo.mobileUser.saltSignature, // sessionKey from Step 4
      },
      'DeviceInfo': requestInfo.deviceInfo.toJson(),
      'AppInfo': requestInfo.appInfo.toJson(),
      'Language': requestInfo.language,
    },
    'KeystoreType': 1,
    'MobileDeviceSpecifications': {
      'NFC': 'Available',
      'FaceID': 'Available',
      'TouchID': 'NotAvailable',
    },
  }),
);

final changeKeystoreResponse = ChangeKeystoreResponse.fromJson(jsonDecode(response.body));
```

**Response Handling:**
```dart
if (changeKeystoreResponse.responseInfo.responseType == 0) {
  // Success - Navigate to PIN Setup Screen
  Navigator.pushReplacementNamed(
    context,
    '/pin-setup',
    arguments: {
      'signUpType': 2, // SIGN_IN_UP_TYPE_NUMBER
      'username': signInRequest.requestInfo.mobileUser.username,
      'passwordHash': changeKeystoreResponse.passwordHash, // Important: use this passwordHash
      'isComingFromSignInScreen': true,
    },
  );
} else {
  _showError(changeKeystoreResponse.responseInfo.responseMessage);
}
```

---

### Step 6: PIN Setup Screen

**Android File:** `SignUpPinFragment.java`, `SignUpPinPresenter.java`

**Parameters:**
- `signUpType`: int (2 for phone, 1 for email)
- `username`: String (phone or email)
- `passwordHash`: String (from ChangeKeystore response)
- `isComingFromSignInScreen`: bool (true)

**UI:**
- PIN entry field (4 digits, masked)
- Confirm PIN field (4 digits, masked)
- Number pad (0-9)
- Clear button
- Back button

**Validation:**
- Both PINs must be 4 digits
- PINs must match

**User Action:** User enters PIN twice and clicks continue

**Flutter Implementation:**
```dart
// pin_setup_screen.dart
String pin1 = '';
String pin2 = '';
bool isComingFromSignInScreen = arguments['isComingFromSignInScreen'] as bool;
String username = arguments['username'] as String;
String passwordHash = arguments['passwordHash'] as String; // From ChangeKeystore
int signUpType = arguments['signUpType'] as int;

void _handlePinComplete() {
  if (pin1.length == 4 && pin2.length == 4) {
    if (pin1 == pin2) {
      // PINs match - proceed to final sign-in
      _finishSignUp(pin2);
    } else {
      // PINs don't match
      _showError('PIN does not match');
      _clearPins();
    }
  }
}

void _finishSignUp(String pin) {
  // This will call singleSignIn() since isComingFromSignInScreen == true
  _callFinalSignInWithPin(pin, username, passwordHash, signUpType);
}
```

---

### Step 7: Final Sign-In with PIN

**Android File:** `SignUpPinPresenter.java` lines 70-72, 76-141

**Since `isComingFromSignInScreen == true`, use `singleSignIn()` method:**

**API Endpoint:** `POST api/SingInUp/SignIn`

**Request:**
```dart
// Create MobileUser with passwordHash from ChangeKeystore response
MobileUser mobileUser = MobileUser(
  username: username.replaceAll(' ', ''),
  passwordHash: passwordHash, // From ChangeKeystore response (Step 5)
  sessionKey: null,
  saltSignature: null,
);

RequestInfo requestInfo = getRequestInfo();
requestInfo.mobileUser = mobileUser;

SignInRequest signInRequest = SignInRequest(
  requestInfo: requestInfo,
  keystoreType: 1,  // 1 because keystore already set up
  signInType: signUpType, // 2 for phone, 1 for email
  mobileNumber: null,
  mobileNumberSecretCode: null,
);

final response = await http.post(
  Uri.parse('$baseUrl/api/SingInUp/SignIn'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'RequestInfo': {
      'MobileUser': {
        'Username': mobileUser.username,
        'PasswordHash': mobileUser.passwordHash, // From ChangeKeystore
        'SessionKey': null,
        'SaltSignature': null,
      },
      'DeviceInfo': requestInfo.deviceInfo.toJson(),
      'AppInfo': requestInfo.appInfo.toJson(),
      'Language': requestInfo.language,
    },
    'KeystoreType': 1,
    'SignInType': signUpType, // 2 for phone, 1 for email
    'MobileNumber': null,
    'MobileNumberSecretCode': null,
  }),
);

final signInResponse = SignInResponse.fromJson(jsonDecode(response.body));
```

**Response Handling:**
```dart
if (signInResponse.responseInfo.responseType == 0) {
  // Save all session data
  await prefs.setBool('HAS_ACTIVE_SESSION', true);
  await prefs.setString('PIN_HASH', passwordHash(pin)); // SHA-512 hash of PIN
  await prefs.setString('USERNAME', mobileUser.username);
  await prefs.setString('PASSWORD_HASH', mobileUser.passwordHash);
  await prefs.setString('SESSION_KEY', signInResponse.sessionKey);
  
  // Save session key to app state
  appState.sessionKey = signInResponse.sessionKey;
  
  // Send FCM token (if available)
  await _sendFCMToken();
  
  // Load bank cards
  final bankCardsResponse = await _listBankCards();
  
  if (bankCardsResponse.responseInfo.responseType == 0) {
    // Load bank accounts
    final bankAccountsResponse = await _listBankAccounts();
    
    if (bankAccountsResponse.responseInfo.responseType == 0) {
      // Save bank data to app state
      appState.bankCards = bankCardsResponse.bankCards;
      appState.bankAccounts = bankAccountsResponse.bankAccounts;
      
      // ✅ NOW emit AuthAuthenticated() - user is fully authenticated
      emit(AuthAuthenticated());
      
      // Navigate to Fingerprint screen (if available) or Home screen
      if (await _isFingerprintAvailable()) {
        Navigator.pushReplacementNamed(context, '/fingerprint', arguments: {'isSignInScreen': true});
      } else {
        await prefs.setBool('IS_FINGERPRINT_ENABLED', false);
        Navigator.pushReplacementNamed(context, '/home');
      }
    } else {
      _showError(bankAccountsResponse.responseInfo.responseMessage);
    }
  } else {
    _showError(bankCardsResponse.responseInfo.responseMessage);
  }
} else {
  _showError(signInResponse.responseInfo.responseMessage);
}
```

---

## Complete Flow Diagram

```
VerifyCode Success (mobileUserSignUpStatus == 1)
    ↓
Navigate to Sign-In Screen (with phone/email)
    ↓
User clicks "Sign in by number" or "Sign in by email"
    ↓
Sign-In by Number/Email Screen
    ↓
User enters password
    ↓
SignIn API Call (KeystoreType: 0, SignInType: 1)
    ↓
Save sessionKey, set SaltSignature
    ↓
ChangeKeystore API Call (KeystoreType: 1)
    ↓
Get passwordHash from ChangeKeystore response
    ↓
Navigate to PIN Setup Screen
    (❌ DO NOT emit AuthAuthenticated() yet)
    ↓
User sets PIN (4 digits, confirmed)
    ↓
Final Sign-In API Call (KeystoreType: 1, SignInType: 2 or 1)
    ↓
Save PIN_HASH, USERNAME, PASSWORD_HASH, SESSION_KEY
    ↓
Load BankCards API
    ↓
Load BankAccounts API
    ↓
✅ NOW emit AuthAuthenticated()
    ↓
Navigate to Fingerprint/Home Screen
```

---

## Key Points from Android Code

1. **SignInRequest.SignInType:** Always `1` in the API request (regardless of phone/email)
2. **SignUpPinScreen.signUpType:** Use `SIGN_IN_UP_TYPE_NUMBER` (2) for phone or `SIGN_IN_UP_TYPE_EMAIL` (1) for email
3. **Final SignInRequest.SignInType:** Use the `signUpType` value (2 for phone, 1 for email)
4. **Password Hash:** Use `changeKeystoreResponse.passwordHash` in final sign-in, NOT original password hash
5. **AuthAuthenticated:** Emit ONLY after PIN is set, final sign-in succeeds, and bank data is loaded
6. **SaltSignature:** Set to `sessionKey` from first SignIn response, used in ChangeKeystore request

---

## Android Code References

- `VerificationPresenter.java`: Lines 59-61 (navigate to SignInScreen)
- `SignInFragment.java`: Complete file (initial sign-in screen)
- `SignInByNumberPresenter.java`: Lines 45-107 (sign-in flow)
- `SignUpPinPresenter.java`: Lines 56-141 (PIN setup and final sign-in)

