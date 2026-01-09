# Logout Implementation - Flutter

This document describes the logout functionality implementation in Flutter, following the exact Android source code flow.

## Implementation Summary

The logout functionality has been fully implemented in Flutter, matching the Android source code behavior:

1. ✅ **Logout Button** - Added to the "More" tab (`_MoreTab` widget)
2. ✅ **Confirmation Dialog** - Created `LogoutConfirmationDialog` widget
3. ✅ **Full Logout Flow** - Implemented in `AuthBloc._onSignOut()`:
   - Sign-In first to get fresh sessionKey
   - Call SignOut API
   - Report Keystore Incident (if needed)
   - Cleanup all data (except FCM token)
   - Navigate to Intro Screen
4. ✅ **API Methods** - Added `reportKeystoreIncident` to `AuthRepository` and `ApiService`

## Files Modified/Created

### Modified Files

1. **`lib/presentation/auth/bloc/auth_bloc.dart`**
   - Updated `_onSignOut()` method to implement full Android logout flow
   - Added `_performSignOutActions()` helper method for cleanup
   - Added import for `foundation.dart` for `debugPrint`

2. **`lib/data/repositories/auth_repository.dart`**
   - Added `reportKeystoreIncident()` method
   - Removed unused import

3. **`lib/data/datasources/remote/api_service.dart`**
   - Added `reportKeystoreIncident()` endpoint method

4. **`lib/presentation/home/screens/home_screen.dart`**
   - Updated `_MoreTab` widget to include logout button and menu items
   - Added `BlocListener` for `AuthBloc` to handle navigation
   - Added logout confirmation dialog integration

### Created Files

1. **`lib/presentation/core/widgets/logout_confirmation_dialog.dart`**
   - New widget for logout confirmation dialog
   - Matches Android dialog behavior (non-dismissible, Yes/No buttons)

## Logout Flow

```
User clicks "Çıxış" (Logout) button
    ↓
Show confirmation dialog
    ↓
User confirms (Bəli)
    ↓
Sign-In API Call (to get fresh sessionKey)
    ├─ Success (responseType == 0)
    │   └─ Save sessionKey → SignOut API
    ├─ Keystore Incident (responseType == 2)
    │   └─ Report Keystore Incident → SignOut API
    └─ Error
        └─ Skip to cleanup
    ↓
SignOut API Call
    ├─ Success → Cleanup
    └─ Error → Cleanup (always cleanup regardless)
    ↓
Clear SharedPreferences (except FCM token)
    ↓
Clear Secure Storage (except FCM token)
    ↓
Navigate to Intro Screen
    ↓
Emit AuthUnauthenticated()
```

## Key Features

### 1. Logout Button Location
- Located in the "More" tab (`_MoreTab`)
- Red color to indicate destructive action
- Disabled during logout process (loading state)

### 2. Confirmation Dialog
- Non-dismissible (cannot tap outside to close)
- Azerbaijani text: "Çıxış etmək istədiyinizə əminsiniz?"
- Two buttons: "Xeyr" (No) and "Bəli" (Yes)

### 3. Sign-In Before Sign-Out
- Retrieves stored `USERNAME` and `PASSWORD_HASH` from secure storage
- Calls SignIn API with `keystoreType: 1` and `signInType: 1`
- Gets fresh `sessionKey` for SignOut API call

### 4. Keystore Incident Reporting
- If SignIn returns `responseType == 2`, reports keystore incident
- Incident type: `1` (OpenFaultAttempt)
- Incident count: `0`
- Fire-and-forget (errors are ignored)

### 5. Data Cleanup
- Clears all SharedPreferences **except** `FCM_NOTIFICATION_TOKEN`
- Clears all Secure Storage **except** `FCM_NOTIFICATION_TOKEN`
- Always performs cleanup, even if API calls fail

### 6. Navigation
- Uses `context.go('/intro')` to navigate to intro screen
- Clears navigation stack (GoRouter handles this automatically)
- Emits `AuthUnauthenticated()` state

## Usage

### In More Tab
The logout button is automatically available in the "More" tab. When clicked:
1. Shows confirmation dialog
2. On "Bəli" (Yes), triggers logout flow
3. Navigates to intro screen on completion

### Programmatic Logout
```dart
// Trigger logout from anywhere
context.read<AuthBloc>().add(const SignOutEvent());
```

### Listening to Logout State
```dart
BlocListener<AuthBloc, AuthState>(
  listener: (context, state) {
    if (state is AuthUnauthenticated) {
      // Handle logout completion
      context.go('/intro');
    }
  },
  child: YourWidget(),
)
```

## API Endpoints Used

1. **SignIn** - `POST api/SingInUp/SignIn`
   - Used to get fresh sessionKey before logout
   - Request: `SignInRequest` with `keystoreType: 1`, `signInType: 1`

2. **SignOut** - `POST api/SingInUp/SignOut`
   - Used to sign out from server
   - Request: `RequestInfoRequest` (just RequestInfo)

3. **KeystoreIncident** - `POST api/MobileUser/KeystoreSecurityIncident`
   - Used to report keystore security incidents
   - Request: `{RequestInfo, KeystoreSecurityIncidentType: 1, KeystoreSecurityIncidentCount: 0}`

## Error Handling

- **Sign-In fails**: Still proceeds with cleanup and navigation
- **SignOut API fails**: Still proceeds with cleanup and navigation
- **Keystore Incident fails**: Ignored, continues with logout flow
- **Any other error**: Still proceeds with cleanup and navigation

This matches Android behavior where cleanup always happens regardless of API call success/failure.

## Testing Checklist

- [ ] Logout button appears in More tab
- [ ] Confirmation dialog shows on logout button click
- [ ] Dialog cannot be dismissed by tapping outside
- [ ] "Xeyr" (No) button closes dialog without logout
- [ ] "Bəli" (Yes) button triggers logout flow
- [ ] Loading state disables logout button during process
- [ ] Sign-In API is called before SignOut
- [ ] SignOut API is called after successful Sign-In
- [ ] Keystore incident is reported if SignIn returns responseType == 2
- [ ] All SharedPreferences are cleared (except FCM token)
- [ ] All Secure Storage is cleared (except FCM token)
- [ ] Navigation to intro screen works
- [ ] AuthUnauthenticated state is emitted
- [ ] Logout works even if Sign-In fails
- [ ] Logout works even if SignOut API fails

## Next Steps

1. **Regenerate API Service**: Run `flutter pub run build_runner build` to regenerate `api_service.g.dart` with the new `reportKeystoreIncident` method.

2. **Test the Implementation**: Test the logout flow in the app to ensure everything works correctly.

3. **Add Loading Indicator**: Consider adding a loading indicator in the More tab during logout (currently handled by BlocBuilder).

4. **Notification Cancellation**: If using `flutter_local_notifications`, add notification cancellation in `_performSignOutActions()`:
   ```dart
   await flutterLocalNotificationsPlugin.cancelAll();
   ```

## Notes

- The implementation strictly follows the Android source code flow
- All error cases are handled gracefully (cleanup always happens)
- FCM token is preserved during logout (required for push notifications)
- The logout button is styled in red to indicate a destructive action
- Azerbaijani language is used for UI text to match the app's language

