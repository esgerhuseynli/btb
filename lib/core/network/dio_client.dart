import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/app_constants.dart';
import 'interceptors/auth_interceptor.dart';
import 'interceptors/error_interceptor.dart';
import 'interceptors/api_logging_interceptor.dart';

class DioClient {
  late Dio _dio;
  final FlutterSecureStorage _secureStorage;
  SharedPreferences? _prefs;
  bool _initialized = false;

  DioClient(this._secureStorage) {
    _dio = Dio(
      BaseOptions(
        baseUrl: AppConstants.baseUrlProd,
        connectTimeout: const Duration(seconds: 60), // Match Android: 60 seconds
        receiveTimeout: const Duration(seconds: 60), // Match Android: 60 seconds
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        // Ensure request body is JSON-encoded
        contentType: Headers.jsonContentType,
        responseType: ResponseType.json,
      ),
    );
    
    // Initialize interceptors asynchronously (they'll be added before first request)
    _initialize();
  }

  Future<void> _initialize() async {
    if (_initialized) return;
    
    _prefs = await SharedPreferences.getInstance();
    
    // Match Android OkHttpProvider interceptor order:
    // 1. InternetConnectionInterceptor (not implemented in Flutter, handled by Dio)
    // 2. ResponseCookiesInterceptor (add first to process responses before other interceptors)
    // 3. RequestCookiesInterceptor (add second to add cookies to requests)
    // 4. AuthInterceptor (for backward compatibility and sessionKey handling)
    // 5. ErrorInterceptor
    // 6. LogInterceptor
    
    if (_prefs != null) {
      _dio.interceptors.addAll([
        ResponseCookiesInterceptor(_prefs!),
        RequestCookiesInterceptor(_prefs!),
        AuthInterceptor(_secureStorage, _prefs!),
        ErrorInterceptor(),
        ApiLoggingInterceptor(), // Custom logging interceptor for better visibility
        LogInterceptor(requestBody: true, responseBody: true, error: true),
      ]);
      
      _initialized = true;
    }
  }

  Dio get dio {
    // If not initialized yet, return dio (interceptors will be added when first request is made)
    // AuthInterceptor's onRequest is async and will handle SharedPreferences access
    return _dio;
  }
}
