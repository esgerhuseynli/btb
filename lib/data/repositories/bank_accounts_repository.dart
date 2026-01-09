import '../datasources/remote/api_service.dart';
import '../models/request_info.dart';
import '../models/api_response.dart';
import '../models/bank_card.dart';
import '../models/bank_account.dart';
import '../models/bank_cards_response.dart';
import '../models/bank_accounts_response.dart';

class BankAccountsRepository {
  final ApiService _apiService;

  BankAccountsRepository(this._apiService);

  Future<BankCardsResponse> getBankCards({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    return await _apiService.listBankCards(request);
  }

  Future<BankAccountsResponse> getBankAccounts({
    required RequestInfo requestInfo,
  }) async {
    final request = {
      'RequestInfo': requestInfo.toJson(),
    };

    return await _apiService.listBankAccounts(request);
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
}

