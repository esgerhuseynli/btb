# Logout Button and Logic Implementation - Following Android Source Code

This document provides the exact logout flow from Android source code for implementing in Flutter.

## Complete Logout Flow

### Step 1: Logout Button Click

**Android File:** `HomeNavFragment.java` lines 160-177, `SignInPinFingerprintFragment.java` lines 59-79

**Location:** Logout button in navigation drawer/menu

**UI Action:**
- User clicks "Logout" button
- Show confirmation dialog with "Yes" and "No" buttons

**Flutter Implementation:**
```dart
// In your home/drawer widget
MenuItem(
  title: 'Logout',
  onTap: () => _showLogoutConfirmationDialog(context),
),

void _showLogoutConfirmationDialog(BuildContext context) {
  showDialog(
    context: context,
    barrierDismissible: false, // Cannot dismiss by tapping outside
    builder: (context) => AlertDialog(
      content: SignOutConfirmationDialog(
        onYes: () {
          Navigator.pop(context); // Close dialog
          _performLogout(context);
        },
        onNo: () => Navigator.pop(context), // Close dialog
      ),
    ),
  );
}
```

---

### Step 2: Initial Sign-In for Logout

**Android File:** `HomeNavPresenter.java` lines 140-171

**Why Sign-In First?**
- Need a fresh sessionKey to call SignOut API
- SessionKey might have expired

**API Endpoint:** `POST api/SingInUp/SignIn`

**Request Preparation:**
```dart
// Get stored credentials from SharedPreferences/local storage
String username = await prefs.getString('USERNAME') ?? '';
String passwordHash = await prefs.getString('PASSWORD_HASH') ?? '';

// Remove spaces from username
username = username.replaceAll(' ', '');

// Create MobileUser
MobileUser mobileUser = MobileUser(
  username: username,
  passwordHash: passwordHash, // Already stored hash (from ChangeKeystore)
  sessionKey: null,
  saltSignature: null,
);

// Set MobileUser in RequestInfo
RequestInfo requestInfo = getRequestInfo(); // Get existing RequestInfo
requestInfo.mobileUser = mobileUser;

// Create SignInRequest for sign-out
SignInRequest signInRequestForSignOut = SignInRequest(
  requestInfo: requestInfo,
  keystoreType: 1,  // 1 because keystore is already set up
  signInType: 1,    // Always 1 in SignInRequest
  mobileNumber: null,
  mobileNumberSecretCode: null,
);
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
    'KeystoreType': 1,
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
  // Success - got new sessionKey
  String newSessionKey = signInResponse.sessionKey;
  appState.sessionKey = newSessionKey; // Update app state
  
  // Proceed to SignOut API
  await _callSignOutAPI(requestInfo);
  
} else if (signInResponse.responseInfo.responseType == 2) {
  // Keystore incident - report it
  await _reportKeystoreIncident();
  
  // Still proceed with cleanup (performSignOutActions)
  _performSignOutActions();
  
} else {
  // Error - still proceed with cleanup
  _performSignOutActions();
}
```

**On Error:**
```dart
catch (error) {
  // Even if sign-in fails, proceed with cleanup
  print('Sign-in for logout failed: $error');
  _performSignOutActions();
}
```

---

### Step 3: SignOut API Call

**Android File:** `HomeNavPresenter.java` lines 185-196

**API Endpoint:** `POST api/SingInUp/SignOut`

**Request Model (`RequestInfoRequest`):**
```dart
// RequestInfo with current sessionKey
RequestInfo requestInfo = getRequestInfo();
requestInfo.appInfo.apiHash = generateAppHash(); // Update app hash

RequestInfoRequest signOutRequest = RequestInfoRequest(
  requestInfo: requestInfo,
);

// API Request
final response = await http.post(
  Uri.parse('$baseUrl/api/SingInUp/SignOut'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'RequestInfo': {
      'MobileUser': {
        'Username': requestInfo.mobileUser?.username,
        'PasswordHash': requestInfo.mobileUser?.passwordHash,
        'SessionKey': requestInfo.mobileUser?.sessionKey,
        'SaltSignature': requestInfo.mobileUser?.saltSignature,
      },
      'DeviceInfo': requestInfo.deviceInfo.toJson(),
      'AppInfo': requestInfo.appInfo.toJson(),
      'Language': requestInfo.language,
    },
  }),
);

final signOutResponse = SignOutResponse.fromJson(jsonDecode(response.body));
```

**Response Handling:**
```dart
// SignOut API response doesn't matter - always proceed with cleanup
// Even if API call fails, we still perform cleanup
_performSignOutActions();
```

---

### Step 4: Report Keystore Incident (if needed)

**Android File:** `HomeNavPresenter.java` lines 173-183

**When to Call:** If SignIn response has `responseType == 2` (keystore security incident)

**API Endpoint:** `POST api/MobileUser/KeystoreSecurityIncident`

**Request:**
```dart
KeystoreIncidentRequest keystoreIncidentRequest = KeystoreIncidentRequest(
  requestInfo: getRequestInfo(),
  keystoreSecurityIncidentType: 1,  // 1 = "OpenFaultAttempt"
  keystoreSecurityIncidentCount: 0,
);

final response = await http.post(
  Uri.parse('$baseUrl/api/MobileUser/KeystoreSecurityIncident'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'RequestInfo': {...},
    'KeystoreSecurityIncidentType': 1,
    'KeystoreSecurityIncidentCount': 0,
  }),
);

// No response handling needed - fire and forget
```

---

### Step 5: Perform Sign-Out Actions (Cleanup)

**Android File:** `HomeNavPresenter.java` lines 198-202, `HomeNavFragment.java` lines 216-220

**Actions:**
1. Clear bank cards (set to empty list)
2. Clear bank accounts (set to empty list)
3. Clear all SharedPreferences (except FCM token)
4. Cancel all notifications
5. Hide loading indicator
6. Navigate to Intro Screen
7. Emit AuthUnauthenticated event

**Flutter Implementation:**
```dart
void _performSignOutActions() {
  // 1. Clear bank cards and accounts from app state
  appState.bankCards = [];
  appState.bankAccounts = [];
  
  // 2. Clear all SharedPreferences except FCM token
  await _clearSharedPreferences();
  
  // 3. Cancel all notifications
  await _cancelAllNotifications();
  
  // 4. Hide loading
  setState(() => isLoading = false);
  
  // 5. Navigate to Intro Screen
  Navigator.of(context).pushNamedAndRemoveUntil(
    '/intro',
    (route) => false, // Remove all previous routes
  );
  
  // 6. Emit AuthUnauthenticated event
  emit(AuthUnauthenticated());
}

Future<void> _clearSharedPreferences() async {
  final prefs = await SharedPreferences.getInstance();
  final allKeys = prefs.getKeys();
  
  // Remove all keys except FCM token
  for (String key in allKeys) {
    if (key != 'FCM_NOTIFICATION_TOKEN') { // Keep FCM token
      await prefs.remove(key);
    }
  }
}

Future<void> _cancelAllNotifications() async {
  // Cancel all notifications
  // In Flutter, use flutter_local_notifications package
  await flutterLocalNotificationsPlugin.cancelAll();
}
```

---

## Complete Flow Diagram

```
User clicks Logout button
    ↓
Show confirmation dialog
    ↓
User confirms (Yes)
    ↓
Show loading indicator
    ↓
Sign-In API Call (for fresh sessionKey)
    ├─ Success (responseType == 0)
    │   └─ Save new sessionKey → SignOut API
    ├─ Keystore Incident (responseType == 2)
    │   └─ Report Keystore Incident → SignOut API
    └─ Error
        └─ Skip to cleanup
    ↓
SignOut API Call
    ├─ Success → Cleanup
    └─ Error → Cleanup (always cleanup regardless)
    ↓
Clear bank cards & accounts
    ↓
Clear SharedPreferences (except FCM token)
    ↓
Cancel all notifications
    ↓
Hide loading indicator
    ↓
Navigate to Intro Screen
    ↓
Emit AuthUnauthenticated()
```

---

## SharedPreferences Keys to Clear

**Keys to REMOVE:**
- `HAS_ACTIVE_SESSION` (d5)
- `SESSION_KEY` (d2)
- `PIN_HASH` (d3)
- `USERNAME` (d1)
- `PASSWORD_HASH` (d0)
- `SIGN_IN_TYPE` (d4)
- `IS_FINGERPRINT_ENABLED` (d6)
- `CUSTOMER_NAME` (d8)
- `LAST_LOGIN` (d9)
- Any other app preferences

**Keys to KEEP:**
- `FCM_NOTIFICATION_TOKEN` (d7) - Must preserve FCM token

---

## Flutter Implementation Example

```dart
class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final AuthRepository authRepository;
  final SharedPreferences prefs;
  final FlutterLocalNotificationsPlugin notifications;
  
  Future<void> signOut(BuildContext context) async {
    emit(AuthLoading());
    
    try {
      // Step 1: Sign-In to get fresh sessionKey
      final signInResponse = await authRepository.signInForLogout();
      
      if (signInResponse.responseInfo.responseType == 0) {
        // Save new sessionKey
        appState.sessionKey = signInResponse.sessionKey;
        
        // Step 2: Call SignOut API
        try {
          await authRepository.signOut();
        } catch (e) {
          // SignOut API error is ignored - proceed with cleanup
          print('SignOut API error: $e');
        }
      } else if (signInResponse.responseInfo.responseType == 2) {
        // Report keystore incident
        await authRepository.reportKeystoreIncident();
        
        // Still call SignOut API
        try {
          await authRepository.signOut();
        } catch (e) {
          print('SignOut API error: $e');
        }
      }
      
      // Step 3: Cleanup (always executed)
      await _performSignOutActions();
      
    } catch (error) {
      // Even if sign-in fails, still perform cleanup
      print('Sign-out error: $error');
      await _performSignOutActions();
    }
  }
  
  Future<void> _performSignOutActions() async {
    // Clear app state
    appState.bankCards = [];
    appState.bankAccounts = [];
    
    // Clear SharedPreferences (except FCM token)
    final allKeys = prefs.getKeys();
    for (String key in allKeys) {
      if (key != 'FCM_NOTIFICATION_TOKEN') {
        await prefs.remove(key);
      }
    }
    
    // Cancel notifications
    await notifications.cancelAll();
    
    // Emit unauthenticated state
    emit(AuthUnauthenticated());
  }
}
```

---

## UI Components

### Logout Confirmation Dialog

```dart
class SignOutConfirmationDialog extends StatelessWidget {
  final VoidCallback onYes;
  final VoidCallback onNo;
  
  const SignOutConfirmationDialog({
    required this.onYes,
    required this.onNo,
  });
  
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      backgroundColor: Colors.transparent,
      contentPadding: EdgeInsets.zero,
      content: Container(
        padding: EdgeInsets.all(20),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              'Are you sure you want to sign out?',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                ElevatedButton(
                  onPressed: onNo,
                  child: Text('No'),
                ),
                ElevatedButton(
                  onPressed: onYes,
                  child: Text('Yes'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
```

### Logout Button in Drawer/Menu

```dart
DrawerTile(
  icon: Icons.logout,
  title: 'Logout',
  onTap: () => _showLogoutConfirmationDialog(context),
),

// Or in menu
PopupMenuItem(
  child: Row(
    children: [
      Icon(Icons.logout),
      SizedBox(width: 8),
      Text('Logout'),
    ],
  ),
  onTap: () => _showLogoutConfirmationDialog(context),
),
```

---

## API Models

### SignOutRequest
```dart
class SignOutRequest {
  final RequestInfo requestInfo;
  
  SignOutRequest({required this.requestInfo});
  
  Map<String, dynamic> toJson() => {
    'RequestInfo': requestInfo.toJson(),
  };
}
```

### SignOutResponse
```dart
class SignOutResponse {
  final ResponseInfo responseInfo;
  
  SignOutResponse({required this.responseInfo});
  
  factory SignOutResponse.fromJson(Map<String, dynamic> json) => SignOutResponse(
    responseInfo: ResponseInfo.fromJson(json['responceInfo']),
  );
}
```

### KeystoreIncidentRequest
```dart
class KeystoreIncidentRequest {
  final RequestInfo requestInfo;
  final int keystoreSecurityIncidentType; // 1 = OpenFaultAttempt
  final int keystoreSecurityIncidentCount; // 0
  
  KeystoreIncidentRequest({
    required this.requestInfo,
    this.keystoreSecurityIncidentType = 1,
    this.keystoreSecurityIncidentCount = 0,
  });
  
  Map<String, dynamic> toJson() => {
    'RequestInfo': requestInfo.toJson(),
    'KeystoreSecurityIncidentType': keystoreSecurityIncidentType,
    'KeystoreSecurityIncidentCount': keystoreSecurityIncidentCount,
  };
}
```

---

## Key Points

1. **Always show confirmation dialog** before logout
2. **Sign-In first** to get fresh sessionKey (even if logout, need valid session)
3. **SignOut API** - call it but don't fail if it errors (always proceed with cleanup)
4. **Keystore Incident** - if SignIn returns responseType == 2, report it
5. **Cleanup is mandatory** - always clear data regardless of API call success/failure
6. **Preserve FCM token** - don't delete it during cleanup
7. **Navigate to Intro Screen** - use pushNamedAndRemoveUntil to clear navigation stack
8. **Emit AuthUnauthenticated** - after cleanup is complete

---

## Android Code References

- `HomeNavFragment.java`: Lines 160-177 (logout button click handler)
- `HomeNavPresenter.java`: Lines 140-202 (complete sign-out flow)
- `SignInPinFingerprintFragment.java`: Lines 59-79 (logout from PIN screen)
- `Utils.java`: Lines 545-558 (postSignOutCleanUp, clearSharedPrefs)

---

## Flutter Implementation Checklist

- [ ] Add logout button to drawer/menu
- [ ] Create logout confirmation dialog
- [ ] Implement Sign-In for logout (using stored credentials)
- [ ] Implement SignOut API call
- [ ] Implement Keystore Incident reporting
- [ ] Implement cleanup (clear SharedPreferences except FCM)
- [ ] Clear bank cards and accounts from app state
- [ ] Cancel all notifications
- [ ] Navigate to Intro Screen
- [ ] Emit AuthUnauthenticated event
- [ ] Handle errors gracefully (always cleanup)

