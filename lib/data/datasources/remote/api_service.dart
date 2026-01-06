import 'package:dio/dio.dart';
import 'package:retrofit/retrofit.dart';
import '../../models/api_response.dart';
import '../../models/mobile_user.dart';
import '../../models/bank_card.dart';
import '../../models/bank_account.dart';
import '../../models/bank_cards_response.dart';
import '../../models/bank_accounts_response.dart';
import '../../models/sign_in_request.dart';
import '../../models/sign_in_response.dart';
import '../../models/sign_up_request.dart';
import '../../models/sign_up_response.dart';
import '../../models/card_send_request.dart';
import '../../models/card_send_response.dart';
import '../../models/verify_code_request.dart';
import '../../models/verify_code_response.dart';
import '../../models/empty_response.dart';
import '../../models/change_keystore_request.dart';
import '../../models/change_keystore_response.dart';
import '../../models/fcm_token_request.dart';
import '../../../core/constants/api_endpoints.dart';

part 'api_service.g.dart';

// ParseErrorLogger class to match retrofit_generator signature
// The generator calls with 3 args (error, stackTrace, options), but newer versions may expect 4
abstract class ParseErrorLogger {
  void logError(
    Object error,
    StackTrace stackTrace,
    RequestOptions requestOptions, [
    Response<dynamic>? response,
  ]);
}

@RestApi()
abstract class ApiService {
  factory ApiService(Dio dio, {String baseUrl, ParseErrorLogger? errorLogger}) =
      _ApiService;

  // Authentication
  @POST(ApiEndpoints.sendCardNumber)
  Future<CardSendResponse> sendCardNumber(@Body() CardSendRequest request);

  @POST(ApiEndpoints.verifyCode)
  Future<VerifyCodeResponse> verifyCode(@Body() VerifyCodeRequest request);

  @POST(ApiEndpoints.signUp)
  Future<SignUpResponse> registerMobileUser(@Body() SignUpRequest request);

  @POST(ApiEndpoints.signIn)
  Future<SignInResponse> signIn(@Body() SignInRequest request);

  @POST(ApiEndpoints.signOut)
  Future<ApiResponse<EmptyResponse>> signOut(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.keystoreIncident)
  Future<ApiResponse<EmptyResponse>> reportKeystoreIncident(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.forgotPassword)
  Future<ApiResponse<EmptyResponse>> forgotPassword(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.changeKeystore)
  Future<ChangeKeystoreResponse> changeKeystore(
      @Body() ChangeKeystoreRequest request);

  @POST(ApiEndpoints.sendFCMToken)
  Future<ApiResponse<EmptyResponse>> sendFCMToken(
      @Body() FcmTokenRequest request);

  // Bank Accounts
  @POST(ApiEndpoints.listBankCards)
  Future<BankCardsResponse> listBankCards(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.listBankAccounts)
  Future<BankAccountsResponse> listBankAccounts(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.listBankLoans)
  Future<ApiResponse<dynamic>> listBankLoans(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.listBankDeposits)
  Future<ApiResponse<dynamic>> listBankDeposits(
      @Body() Map<String, dynamic> request);

  // Mobile User
  @POST(ApiEndpoints.mobileUserData)
  Future<ApiResponse<dynamic>> getMobileUserData(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.changeMobileUserData)
  Future<ApiResponse<EmptyResponse>> changeMobileUserData(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.verifyMobileUserDataChange)
  Future<ApiResponse<EmptyResponse>> verifyMobileUserDataChange(
      @Body() Map<String, dynamic> request);

  // Exchange Rates
  @POST(ApiEndpoints.exchangeRates)
  Future<ApiResponse<dynamic>> getExchangeRates(
      @Body() Map<String, dynamic> request);

  // News
  @POST(ApiEndpoints.news)
  Future<ApiResponse<dynamic>> getNews(@Body() Map<String, dynamic> request);

  // Notifications
  @POST(ApiEndpoints.notifications)
  Future<ApiResponse<dynamic>> getNotifications(
      @Body() Map<String, dynamic> request);

  // Service Points
  @POST(ApiEndpoints.atms)
  Future<ApiResponse<dynamic>> getAtms(@Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.branches)
  Future<ApiResponse<dynamic>> getBranches(
      @Body() Map<String, dynamic> request);
}
