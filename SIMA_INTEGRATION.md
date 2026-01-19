# SIMA Integration Guide

This document describes how to use the SIMA (Digital Signature) integration in the BTB Mobile Flutter app.

## Overview

SIMA is a digital signature application that allows users to sign PDF documents and challenges using their digital certificates. The integration is implemented via platform channels to communicate with the native Android and iOS SIMA apps.

## Configuration

The SIMA service is configured with the following credentials:
- **Client ID**: `1082001`
- **Master Key**: `9569936E-9A4D-4AAC-B286-E6242CD08FA0`
- **Service Name**: `Bank BTB (App2App) (TEST)`
- **Package Name**: `az.dpc.sima`

## Usage

### 1. Get SimaService Instance

The `SimaService` is registered in dependency injection. You can get it using:

```dart
import 'package:get_it/get_it.dart';
import '../data/services/sima_service.dart';

final simaService = GetIt.instance<SimaService>();
```

Or inject it into your BLoC/Repository:

```dart
class MyBloc {
  final SimaService _simaService;
  
  MyBloc(this._simaService);
}
```

### 2. Check if SIMA is Installed

Before using SIMA, check if the app is installed:

```dart
final isInstalled = await simaService.isSimaInstalled();
if (!isInstalled) {
  // Open App Store (iOS) or Play Store (Android) to install SIMA
  await simaService.openPlayStore();
  return;
}
```

### 3. Sign a PDF Document

To sign a PDF document:

```dart
final response = await simaService.signPdf(
  documentPath: '/path/to/document.pdf',
  userFinCode: '1234567', // User's FIN code
  logoDataUri: null, // Optional: custom logo as data URI
);

if (response.isSuccess) {
  // Document signed successfully
  final signedDocumentPath = response.signedDocumentPath;
  // Use the signed document
} else {
  // Handle error
  final errorMessage = response.message ?? 'Unknown error';
  // Show error to user
}
```

### 4. Sign a Challenge

To sign a challenge (for authentication/verification):

```dart
// Generate a random challenge (64 bytes)
final challenge = simaService.generateChallenge();

final response = await simaService.signChallenge(
  challenge: challenge,
  userFinCode: '1234567', // User's FIN code
  logoDataUri: null, // Optional: custom logo as data URI
);

if (response.isSuccess) {
  // Challenge signed successfully
  final signatureBytes = response.signatureBytes;
  final certificateBytes = response.certificateBytes;
  
  // Verify the signature using the certificate
  // (verification logic should be implemented based on your needs)
} else {
  // Handle error
  final errorMessage = response.message ?? 'Unknown error';
  // Show error to user
}
```

## Error Handling

The SIMA service returns error codes that can be translated to user-friendly messages using `SimaError.getErrorMessage()`:

```dart
import '../data/models/sima_response.dart';

final errorMessage = SimaError.getErrorMessage('operation-canceled');
// Returns: "İstifadəçi əməliyyatı ləğv etdi"
```

### Common Error Codes

- `operation-canceled` - User canceled the operation
- `sima_not_installed` - SIMA app is not installed
- `empty-data` - Empty signing data
- `wrong-user-code` - Wrong user code (FIN)
- `validate-request-error` - Error validating signing request
- `sign-document-error` - Error signing document
- `sign-challenge-error` - Error signing challenge

## Implementation Details

### Android Native Code

The native Android implementation is in `MainActivity.kt`:
- Handles method channel calls from Flutter
- Creates Intents to communicate with SIMA app
- Processes activity results from SIMA
- Handles errors and returns appropriate responses

### iOS Native Code

The native iOS implementation consists of:
- **SimaHandler.swift**: Handles method channel calls and URL scheme communication
- **AppDelegate.swift**: Processes URL callbacks from SIMA app
- Uses URL schemes (`sima://`) to communicate with SIMA app
- Returns results via custom URL scheme (`btbmobile://`)

### Flutter Service

The Flutter service (`SimaService`) handles:
- HMAC-SHA256 signature generation
- SHA-256 hash calculation
- Logo loading and conversion to data URI
- Challenge generation
- Error handling and translation
- Works seamlessly on both Android and iOS

### Security

- Documents and challenges are hashed using SHA-256
- Signatures are generated using HMAC-SHA256 with the master key
- Challenges are generated using cryptographically secure random numbers
- Android: All communication with SIMA app is done via secure Intents
- iOS: All communication with SIMA app is done via secure URL schemes

## Platform-Specific Configuration

### Android Manifest

The Android manifest includes a query for the SIMA package (required for Android 10+):

```xml
<queries>
    <package android:name="az.dpc.sima" />
</queries>
```

### iOS Info.plist

The iOS Info.plist includes:
- **CFBundleURLTypes**: Custom URL scheme (`btbmobile`) for receiving SIMA callbacks
- **LSApplicationQueriesSchemes**: Query scheme (`sima`) to check if SIMA is installed

```xml
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>btbmobile</string>
        </array>
    </dict>
</array>
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>sima</string>
</array>
```

## Example: Complete Sign PDF Flow

```dart
import 'package:get_it/get_it.dart';
import '../data/services/sima_service.dart';
import '../data/models/sima_response.dart';

Future<void> signDocument(String documentPath, String userFinCode) async {
  final simaService = GetIt.instance<SimaService>();
  
  // Check if SIMA is installed
  final isInstalled = await simaService.isSimaInstalled();
  if (!isInstalled) {
    // Show dialog to user
    await simaService.openPlayStore();
    return;
  }
  
  // Sign the document
  final response = await simaService.signPdf(
    documentPath: documentPath,
    userFinCode: userFinCode,
  );
  
  if (response.isSuccess && response.signedDocumentPath != null) {
    // Success - use signed document
    print('Document signed: ${response.signedDocumentPath}');
  } else {
    // Error - show message to user
    final errorMessage = response.message ?? 'Unknown error';
    final translatedMessage = SimaError.getErrorMessage(errorMessage);
    print('Error: $translatedMessage');
  }
}
```

## Notes

- The SIMA app must be installed on the device
- User's FIN code is required for all operations
- Logo is optional but recommended (max 500KB)
- All operations are asynchronous and should be handled with proper error handling
- The signed document/challenge is returned via the response object

### Known Issues

#### Android FIN Code Validation Bug

**Issue**: SIMA Android app (version 3.3.19) has a bug where it does not properly validate the FIN code entered by the user against the FIN code registered in the SIMA account. This means SIMA Android may return a successful response even if the entered FIN code doesn't match the user's actual FIN code.

**Impact**: 
- On Android: Users can potentially proceed with authentication even with an incorrect FIN code
- On iOS: FIN code validation works correctly - SIMA returns `wrong-user-code` error if codes don't match

**Workaround**: 
- The app displays a warning message on Android to remind users to ensure they enter the correct FIN code
- Consider implementing backend validation to extract and verify the FIN code from the certificate returned by SIMA
- Users should be aware that they must enter the FIN code that matches their SIMA account

**Status**: This is a bug in the SIMA Android app and should be reported to SIMA developers. The app handles this gracefully by showing appropriate warnings to users.

