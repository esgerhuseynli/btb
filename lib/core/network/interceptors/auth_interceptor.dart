import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../constants/app_constants.dart';

/// RequestCookiesInterceptor - Adds stored cookie to request headers
/// Matches Android RequestCookiesInterceptor behavior
class RequestCookiesInterceptor extends Interceptor {
  final SharedPreferences _prefs;

  RequestCookiesInterceptor(this._prefs);

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    // Get stored cookie from SharedPreferences (Android uses Constants.COOKIE_KEY)
    final savedCookie = _prefs.getString(AppConstants.cookieKey) ?? '';
    
    if (savedCookie.isNotEmpty) {
      // Add cookie header (Android: builder.addHeader(Constants.COOKIE_KEY, savedCookie))
      options.headers[AppConstants.cookieKey] = savedCookie;
    }

    super.onRequest(options, handler);
  }
}

/// ResponseCookiesInterceptor - Extracts and stores cookie from response headers
/// Matches Android ResponseCookiesInterceptor behavior
class ResponseCookiesInterceptor extends Interceptor {
  final SharedPreferences _prefs;

  ResponseCookiesInterceptor(this._prefs);

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) async {
    // Extract Set-Cookie headers (Android: originalResponse.headers("Set-Cookie"))
    final setCookieHeaders = response.headers['set-cookie'];
    
    if (setCookieHeaders != null && setCookieHeaders.isNotEmpty) {
      for (final header in setCookieHeaders) {
        final trimmedHeader = header.trim();
        
        // Android checks: if (header.trim().contains(";"))
        if (trimmedHeader.contains(';')) {
          // Android splits: String[] cookiesStr = header.trim().split(";")
          final cookiesStr = trimmedHeader.split(';');
          
          // Android checks: if (cookiesStr[0].contains(".AspNetCore.Session"))
          if (cookiesStr.isNotEmpty && cookiesStr[0].contains('.AspNetCore.Session')) {
            // Android stores: preferences.edit().putString(COOKIE_KEY, cookiesStr[0]).apply()
            await _prefs.setString(AppConstants.cookieKey, cookiesStr[0]);
          }
        }
      }
    }

    super.onResponse(response, handler);
  }
}

/// AuthInterceptor - Legacy interceptor for backward compatibility
/// Note: Android doesn't use this pattern - it uses separate RequestCookiesInterceptor and ResponseCookiesInterceptor
class AuthInterceptor extends Interceptor {
  final FlutterSecureStorage _secureStorage;
  final SharedPreferences _prefs;

  AuthInterceptor(this._secureStorage, this._prefs);

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    // First try to get cookie from SharedPreferences (like Android)
    final savedCookie = _prefs.getString(AppConstants.cookieKey) ?? '';
    if (savedCookie.isNotEmpty) {
      options.headers[AppConstants.cookieKey] = savedCookie;
    } else {
      // Fallback: try sessionKey from secure storage (for backward compatibility)
      final sessionKey = await _secureStorage.read(key: AppConstants.sessionKey);
      if (sessionKey != null && sessionKey.isNotEmpty) {
        // Store sessionKey in cookie format (if not already stored as cookie)
        options.headers[AppConstants.cookieKey] = 'SessionKey=$sessionKey';
      }
    }

    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) async {
    // Extract Set-Cookie headers and store .AspNetCore.Session cookie
    final setCookieHeaders = response.headers['set-cookie'];
    
    if (setCookieHeaders != null && setCookieHeaders.isNotEmpty) {
      for (final header in setCookieHeaders) {
        final trimmedHeader = header.trim();
        
        if (trimmedHeader.contains(';')) {
          final cookiesStr = trimmedHeader.split(';');
          
          // Store .AspNetCore.Session cookie in SharedPreferences (like Android)
          if (cookiesStr.isNotEmpty && cookiesStr[0].contains('.AspNetCore.Session')) {
            await _prefs.setString(AppConstants.cookieKey, cookiesStr[0]);
          }
        }
        
        // Also extract sessionKey if present (for backward compatibility)
        if (trimmedHeader.contains('SessionKey=')) {
          final sessionKey = trimmedHeader
              .split('SessionKey=')[1]
              .split(';')[0]
              .trim();
          if (sessionKey.isNotEmpty) {
            await _secureStorage.write(key: AppConstants.sessionKey, value: sessionKey);
          }
        }
      }
    }

    super.onResponse(response, handler);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) {
    super.onError(err, handler);
  }
}



