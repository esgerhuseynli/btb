import 'dart:io';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter/services.dart';
import '../constants/app_constants.dart';
import '../utils/app_utils.dart';
import '../../data/models/request_info.dart';
import '../../data/models/device_info.dart' as model;
import '../../data/models/app_info.dart';
import '../../data/models/mobile_user.dart';

class RequestBuilder {
  final FlutterSecureStorage _secureStorage;
  final DeviceInfoPlugin _deviceInfo;
  static const MethodChannel _channel = MethodChannel('az.btb.btb_mobile_flutter/device');
  PackageInfo? _packageInfo;

  RequestBuilder(
    this._secureStorage,
    this._deviceInfo,
  );

  /// Get language code for API requests
  /// Returns: 1 for Azerbaijani (az), 2 for English (en), 3 for Russian (ru)
  /// Defaults to 2 (English)
  /// Android code: getAppLanguageReversed(Lingver.getInstance().getLanguage()) + 1
  /// If stored index is 0 (az), we return 1
  /// If stored index is 1 (en), we return 2
  /// If stored index is 2 (ru), we return 3
  Future<int> _getLanguageCode() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final langIndex = prefs.getInt(AppConstants.appLanguage) ?? 1;
      // API expects: 1=az, 2=en, 3=ru (stored index + 1)
      return langIndex + 1;
    } catch (e) {
      // Default to English (2) - API requirement
      return 2;
    }
  }

  Future<void> _initPackageInfo() async {
    _packageInfo ??= await PackageInfo.fromPlatform();
  }

  /// Format app version to match Android format: "1.0.8-411" (version-buildNumber)
  /// Flutter pubspec uses "1.0.8+411" but Android uses "1.0.8-411"
  String _formatAppVersion() {
    if (_packageInfo == null) return '1.0.0-0';
    final version = _packageInfo!.version;
    final buildNumber = _packageInfo!.buildNumber;
    return '$version-$buildNumber';
  }

  Future<RequestInfo> buildRequestInfo({
    int? language,
    MobileUser? mobileUser,
  }) async {
    await _initPackageInfo();
    
    // Get language code (default to 1 if not provided)
    final languageCode = language ?? await _getLanguageCode();
    
    // Get device info
    model.DeviceInfo deviceInfo;
    if (Platform.isAndroid) {
      final androidInfo = await _deviceInfo.androidInfo;
      // Android uses Build.VERSION.SDK_INT (SDK version number), not release version
      // Example: SDK 34 for Android 14, SDK 35 for Android 15
      final sdkInt = androidInfo.version.sdkInt;
      // Get ANDROID_ID using platform channel (matches Android Settings.Secure.ANDROID_ID)
      String androidId;
      try {
        androidId = await _channel.invokeMethod('getAndroidId') as String;
      } catch (e) {
        // Fallback to androidInfo.id if platform channel fails
        androidId = androidInfo.id;
      }
      deviceInfo = model.DeviceInfo(
        deviceID: androidId,
        vendor: androidInfo.manufacturer,
        model: androidInfo.model,
        osName: 'android',
        osVersion: sdkInt.toString(), // Use SDK_INT like Android does
      );
    } else if (Platform.isIOS) {
      final iosInfo = await _deviceInfo.iosInfo;
      deviceInfo = model.DeviceInfo(
        deviceID: iosInfo.identifierForVendor ?? '',
        vendor: 'Apple',
        model: iosInfo.model,
        osName: 'ios',
        osVersion: iosInfo.systemVersion,
      );
    } else {
      deviceInfo = model.DeviceInfo(
        deviceID: 'unknown',
        vendor: 'unknown',
        model: 'unknown',
        osName: Platform.operatingSystem,
        osVersion: Platform.operatingSystemVersion,
      );
    }

    // Get app info (with apiHash - required by API)
    // Android uses R.string.app_name which is "BTB Mobile"
    // Android version format: "1.0.8-411" (version-buildNumber with hyphen)
    final appInfo = AppInfo(
      appFor: 1,
      appName: 'BTB Mobile', // Must match Android R.string.app_name
      appVersion: _formatAppVersion(), // Format: "1.0.8-411" to match Android
      apiHash: AppUtils.appHash(),
    );

    return RequestInfo(
      mobileUser: mobileUser,
      deviceInfo: deviceInfo,
      appInfo: appInfo,
      language: languageCode,
    );
  }

  Future<MobileUser?> getStoredMobileUser() async {
    final username = await _secureStorage.read(key: AppConstants.username);
    final passwordHash = await _secureStorage.read(key: AppConstants.passwordHash);
    final sessionKey = await _secureStorage.read(key: AppConstants.sessionKey);

    if (username == null || passwordHash == null) {
      return null;
    }

    return MobileUser(
      username: username,
      passwordHash: passwordHash,
      sessionKey: sessionKey,
      saltSignature: sessionKey, // SaltSignature should be set to sessionKey
    );
  }

  /// Get common request for authenticated API calls
  /// Matches Android Utils.getCommonRequest() behavior
  /// Sets SaltSignature to sessionKey and clears Username/PasswordHash
  Future<RequestInfo> getCommonRequest() async {
    // Build base RequestInfo
    final baseRequestInfo = await buildRequestInfo();
    
    // Get sessionKey from storage
    final sessionKey = await _secureStorage.read(key: AppConstants.sessionKey);
    
    // Android: requestInfo.getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey())
    // Android: requestInfo.getMobileUser().setUsername("")
    // Android: requestInfo.getMobileUser().setPasswordHash("")
    // Create new MobileUser with sessionKey as SaltSignature and empty Username/PasswordHash
    // Note: sessionKey should NOT be included in request body, only SaltSignature
    MobileUser? updatedMobileUser;
    if (sessionKey != null) {
      updatedMobileUser = MobileUser(
        username: '', // Android: setUsername("")
        passwordHash: '', // Android: setPasswordHash("")
        sessionKey: null, // Don't include sessionKey in request body
        saltSignature: sessionKey, // Android: setSaltSignature(AppData.getInstance().getSessionKey())
      );
    }
    
    // Create new RequestInfo with updated MobileUser (models are immutable)
    return RequestInfo(
      mobileUser: updatedMobileUser,
      deviceInfo: baseRequestInfo.deviceInfo,
      appInfo: baseRequestInfo.appInfo,
      language: baseRequestInfo.language,
    );
  }

  /// Build RequestInfo with SaltSignature set from sessionKey (for authenticated requests)
  /// This is used when making authenticated API calls after sign-in
  Future<RequestInfo> buildAuthenticatedRequestInfo({
    int? language,
    MobileUser? mobileUser,
  }) async {
    // Build base RequestInfo
    final baseRequestInfo = await buildRequestInfo(
      language: language,
      mobileUser: mobileUser,
    );
    
    // Get sessionKey from storage
    final sessionKey = await _secureStorage.read(key: AppConstants.sessionKey);
    
    // Set SaltSignature to sessionKey (like Android Utils.getCommonRequest())
    MobileUser? updatedMobileUser;
    if (sessionKey != null) {
      updatedMobileUser = MobileUser(
        username: mobileUser?.username ?? '',
        passwordHash: mobileUser?.passwordHash ?? '',
        sessionKey: sessionKey,
        saltSignature: sessionKey, // Set SaltSignature to sessionKey
      );
    } else {
      updatedMobileUser = mobileUser;
    }
    
    // Create new RequestInfo with updated MobileUser (models are immutable)
    return RequestInfo(
      mobileUser: updatedMobileUser,
      deviceInfo: baseRequestInfo.deviceInfo,
      appInfo: baseRequestInfo.appInfo,
      language: baseRequestInfo.language,
    );
  }
}
