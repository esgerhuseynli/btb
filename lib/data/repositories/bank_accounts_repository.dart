import 'package:dio/dio.dart';
import '../datasources/remote/api_service.dart';
import '../models/request_info.dart';
import '../models/api_response.dart';
import '../models/bank_card.dart';
import '../models/bank_account.dart';
import '../models/bank_cards_response.dart';
import '../models/bank_accounts_response.dart';
import '../models/bank_cards_list_request.dart';
import '../models/bank_accounts_list_request.dart';
import '../models/response_info.dart';
import '../../core/constants/api_endpoints.dart';

class BankAccountsRepository {
  final ApiService _apiService;
  final Dio _dio;

  BankAccountsRepository(this._apiService, this._dio);

  Future<BankCardsResponse> getBankCards({
    required RequestInfo requestInfo,
  }) async {
    final request = BankCardsListRequest(
      requestInfo: requestInfo,
    );

    return await _apiService.listBankCards(request.toJson());
  }

  Future<BankAccountsResponse> getBankAccounts({
    required RequestInfo requestInfo,
  }) async {
    final request = BankAccountsListRequest(
      requestInfo: requestInfo,
    );

    return await _apiService.listBankAccounts(request.toJson());
  }

  Future<ApiResponse<dynamic>> getBankLoans({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    return await _apiService.listBankLoans(request);
  }

  Future<ApiResponse<dynamic>> getBankDeposits({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    return await _apiService.listBankDeposits(request);
  }

  Future<ApiResponse<dynamic>> getCardStatements({
    required RequestInfo requestInfo,
    required String fromIdCard,
    required String fromDate,
    required String toDate,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
      'FromIdCard': fromIdCard,
      'FromDate': fromDate,
      'ToDate': toDate,
    };

    // The API response has bankCardStatement at root level, not in Data field
    // The response structure is: {"responceInfo": {...}, "bankCardStatement": [...]}
    // But ApiResponse expects: {"ResponseInfo": {...}, "Data": {...}}
    // Since the keys don't match, we need to make a direct Dio call to get raw response
    try {
      final response = await _dio.post<Map<String, dynamic>>(
        ApiEndpoints.listCardStatements,
        data: request,
        options: Options(
          headers: {
            'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153',
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          },
        ),
      );

      if (response.data != null) {
        final rawData = response.data!;
        
        // Extract responceInfo (note: API misspells it as "responceInfo" not "responseInfo")
        final responceInfoData = rawData['responceInfo'] as Map<String, dynamic>?;
        final bankCardStatements = rawData['bankCardStatement'] as List<dynamic>?;
        
        // Parse ResponseInfo from responceInfo
        ResponseInfo? responseInfo;
        if (responceInfoData != null) {
          responseInfo = ResponseInfo(
            responseType: (responceInfoData['responseType'] as num?)?.toInt(),
            responseMessage: responceInfoData['responseMessage'] as String?,
            errorCode: (responceInfoData['errorCode'] as num?)?.toInt(),
            errorMessage: responceInfoData['errorMessage'] as String?,
            saltSignature: responceInfoData['saltSignature'] as String?,
          );
        }
        
        // Return ApiResponse with bankCardStatement in the data field
        return ApiResponse<dynamic>(
          responseInfo: responseInfo ?? ResponseInfo(),
          data: bankCardStatements,
        );
      }
      
      // Fallback to API service if direct call fails
      return await _apiService.listCardStatements(request);
    } catch (e) {
      // Fallback to API service if direct call fails
      return await _apiService.listCardStatements(request);
    }
  }
}

