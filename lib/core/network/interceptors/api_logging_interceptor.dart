import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';

/// Custom API logging interceptor that prints requests and responses
/// to the console in a format visible in Xcode console
class ApiLoggingInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    debugPrint('\n*** Request ***');
    debugPrint('uri: ${options.uri}');
    debugPrint('method: ${options.method}');
    debugPrint('responseType: ${options.responseType}');
    debugPrint('followRedirects: ${options.followRedirects}');
    debugPrint('persistentConnection: ${options.persistentConnection}');
    debugPrint('connectTimeout: ${options.connectTimeout}');
    debugPrint('sendTimeout: ${options.sendTimeout}');
    debugPrint('receiveTimeout: ${options.receiveTimeout}');
    debugPrint('receiveDataWhenStatusError: ${options.receiveDataWhenStatusError}');
    debugPrint('extra: ${options.extra}');
    debugPrint('headers:');
    options.headers.forEach((key, value) {
      debugPrint('  $key: $value');
    });
    debugPrint('data:');
    if (options.data != null) {
      if (options.data is Map) {
        // For SIMA certificate requests, verify certificate is complete
        if (options.uri.toString().contains('verifycertificate')) {
          final dataMap = options.data as Map;
          final cert = dataMap['certificate'] as String?;
          if (cert != null) {
            debugPrint('  (Map with certificate: ${cert.length} chars)');
            debugPrint('  Certificate ends with Q==: ${cert.endsWith('Q==')}');
            debugPrint('  First 100 chars: ${cert.substring(0, cert.length > 100 ? 100 : cert.length)}...');
            debugPrint('  Last 100 chars: ...${cert.length > 100 ? cert.substring(cert.length - 100) : cert}');
            debugPrint('  pinCode: ${dataMap['pinCode']}');
          } else {
            debugPrint('  ${options.data}');
          }
        } else {
          debugPrint('  ${options.data}');
        }
      } else if (options.data is List<int>) {
        // Handle bytes (UTF-8 encoded JSON)
        final bytes = options.data as List<int>;
        final dataString = utf8.decode(bytes);
        if (options.uri.toString().contains('verifycertificate')) {
          debugPrint('  (Bytes length: ${bytes.length}, String length: ${dataString.length})');
          debugPrint('  First 200 chars: ${dataString.substring(0, dataString.length > 200 ? 200 : dataString.length)}...');
          debugPrint('  Last 200 chars: ...${dataString.length > 200 ? dataString.substring(dataString.length - 200) : dataString}');
          // Verify certificate is in the string
          if (dataString.contains('Q=="')) {
            debugPrint('  Certificate ends with Q==: true');
          }
        } else {
          debugPrint('  (Bytes: ${bytes.length} bytes)');
        }
      } else if (options.data is String) {
        final dataString = options.data as String;
        // For SIMA certificate requests, show length and preview instead of full data
        if (options.uri.toString().contains('verifycertificate')) {
          debugPrint('  (String length: ${dataString.length} chars)');
          debugPrint('  First 200 chars: ${dataString.substring(0, dataString.length > 200 ? 200 : dataString.length)}...');
          debugPrint('  Last 200 chars: ...${dataString.length > 200 ? dataString.substring(dataString.length - 200) : dataString}');
        } else {
          debugPrint('  ${dataString}');
        }
      } else {
        debugPrint('  ${options.data.toString()}');
      }
    } else {
      debugPrint('  null');
    }
    debugPrint('');
    super.onRequest(options, handler);
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    debugPrint('\n*** Response ***');
    debugPrint('uri: ${response.requestOptions.uri}');
    debugPrint('statusCode: ${response.statusCode}');
    debugPrint('headers:');
    response.headers.forEach((key, values) {
      debugPrint('  $key: ${values.join(", ")}');
    });
    debugPrint('Response Text:');
    if (response.data != null) {
      if (response.data is Map) {
        debugPrint('  ${response.data}');
      } else if (response.data is String) {
        debugPrint('  ${response.data}');
      } else {
        debugPrint('  ${response.data.toString()}');
      }
    } else {
      debugPrint('  null');
    }
    debugPrint('');
    super.onResponse(response, handler);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) {
    debugPrint('\n*** Error ***');
    debugPrint('uri: ${err.requestOptions.uri}');
    debugPrint('method: ${err.requestOptions.method}');
    debugPrint('type: ${err.type}');
    debugPrint('message: ${err.message}');
    if (err.response != null) {
      debugPrint('statusCode: ${err.response!.statusCode}');
      debugPrint('Response Text:');
      if (err.response!.data != null) {
        if (err.response!.data is Map) {
          debugPrint('  ${err.response!.data}');
        } else if (err.response!.data is String) {
          debugPrint('  ${err.response!.data}');
        } else {
          debugPrint('  ${err.response!.data.toString()}');
        }
      }
    }
    debugPrint('');
    super.onError(err, handler);
  }
}

