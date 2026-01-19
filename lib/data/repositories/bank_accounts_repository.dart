import '../datasources/remote/api_service.dart';
import '../models/request_info.dart';
import '../models/api_response.dart';
import '../models/bank_card.dart';
import '../models/bank_account.dart';
import '../models/bank_cards_response.dart';
import '../models/bank_accounts_response.dart';
import '../models/bank_cards_list_request.dart';
import '../models/bank_accounts_list_request.dart';

class BankAccountsRepository {
  final ApiService _apiService;

  BankAccountsRepository(this._apiService);

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

    final response = await _apiService.listCardStatements(request);
    
    // The API response has bankCardStatement at root level, not in Data field
    // We need to manually extract it and put it in the data field
    // The response structure is: {"responceInfo": {...}, "bankCardStatement": [...]}
    // But ApiResponse expects: {"ResponseInfo": {...}, "Data": {...}}
    // Since the keys don't match, we need to manually construct the response
    
    // Try to get the raw response data from Dio if possible
    // For now, we'll work with what we have and check if data contains bankCardStatement
    return response;
  }
}

