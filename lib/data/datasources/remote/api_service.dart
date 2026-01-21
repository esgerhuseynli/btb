import 'package:dio/dio.dart' hide Headers;
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
import '../../models/otp_request.dart';
import '../../models/otp_verify_request.dart';
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

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/SingInUp/SignInNew')
  Future<SignInResponse> signIn(@Body() SignInRequest request);

  @POST(ApiEndpoints.signOut)
  Future<ApiResponse<EmptyResponse>> signOut(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.keystoreIncident)
  Future<ApiResponse<EmptyResponse>> reportKeystoreIncident(
      @Body() Map<String, dynamic> request);

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/SingInUp/UserForgotPassword')
  Future<CardSendResponse> forgotPassword(
      @Body() Map<String, dynamic> request);

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/SingInUp/SubmitUserForgotPassword')
  Future<ApiResponse<EmptyResponse>> changeForgotPassword(
      @Body() Map<String, dynamic> request);

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/MobileUser/ChangeKeystore')
  Future<ChangeKeystoreResponse> changeKeystore(
      @Body() ChangeKeystoreRequest request);

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/MobileUser/ChangeDevicePushInfoToken')
  Future<ApiResponse<EmptyResponse>> sendFCMToken(
      @Body() FcmTokenRequest request);

  // OTP (using full URLs for different server)
  @POST('http://94.20.61.252:8088/api/Otp/SendOtpMobile')
  Future<ApiResponse<dynamic>> sendOtp(@Body() OtpRequest request);

  @POST('http://94.20.61.252:8088/api/Otp/VerifyOtp')
  Future<ApiResponse<dynamic>> verifyOtp(@Body() OtpVerifyRequest request);

  // Bank Accounts
  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST('http://94.20.61.252:8087/api/BankAccounts/ListBankCards')
  Future<BankCardsResponse> listBankCards(
      @Body() Map<String, dynamic> request);

  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST(ApiEndpoints.listBankAccounts)
  Future<BankAccountsResponse> listBankAccounts(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.listBankLoans)
  Future<ApiResponse<dynamic>> listBankLoans(
      @Body() Map<String, dynamic> request);

  @POST(ApiEndpoints.listBankDeposits)
  Future<ApiResponse<dynamic>> listBankDeposits(
      @Body() Map<String, dynamic> request);

  // Operations
  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
  @POST(ApiEndpoints.listCardStatements)
  Future<ApiResponse<dynamic>> listCardStatements(
      @Body() Map<String, dynamic> request);

  // Mobile User
  @Headers({'x-api-key': 'MOBILE_SUPER_SECRET_KEY_1215489789744153153'})
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
