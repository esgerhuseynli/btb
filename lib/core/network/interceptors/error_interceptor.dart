import 'package:dio/dio.dart';

class ErrorInterceptor extends Interceptor {
  @override
  void onError(DioException err, ErrorInterceptorHandler handler) {
    String errorMessage = 'Xəta baş verdi';

    switch (err.type) {
      case DioExceptionType.connectionTimeout:
      case DioExceptionType.sendTimeout:
      case DioExceptionType.receiveTimeout:
        errorMessage = 'Bağlantı zaman aşımı';
        break;
      case DioExceptionType.badResponse:
        if (err.response != null) {
          final statusCode = err.response!.statusCode;
          if (statusCode == 401) {
            errorMessage = 'Giriş təsdiqlənmədi';
          } else if (statusCode == 403) {
            errorMessage = 'Giriş qadağandır';
          } else if (statusCode == 404) {
            errorMessage = 'Məlumat tapılmadı';
          } else if (statusCode == 500) {
            errorMessage = 'Server xətası';
          } else {
            errorMessage = err.response?.data?['errorMessage'] ?? 'Xəta baş verdi';
          }
        }
        break;
      case DioExceptionType.cancel:
        errorMessage = 'Sorğu ləğv edildi';
        break;
      case DioExceptionType.unknown:
        errorMessage = 'İnternet bağlantısını yoxlayın';
        break;
      default:
        errorMessage = 'Naməlum xəta';
    }

    final customError = DioException(
      requestOptions: err.requestOptions,
      response: err.response,
      type: err.type,
      error: errorMessage,
    );

    handler.next(customError);
  }
}



