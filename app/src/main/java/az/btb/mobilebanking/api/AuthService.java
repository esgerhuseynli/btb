package az.btb.mobilebanking.api;

import az.btb.mobilebanking.models.*;
import az.btb.mobilebanking.utils.Endpoints;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST(Endpoints.sendCardNumber)
    Observable<CardSendResponse> signUp(@Body CardSendRequest cardSendRequest);

    @POST(Endpoints.verifyCode)
    Observable<VerifyCodeResponse> verifyCode(@Body VerifyCodeRequest verifyCodeRequest);

    @POST(Endpoints.signIn)
    Observable<SignInResponse> signIn(@Body SignInRequest signInRequest);

    @POST(Endpoints.keystoreIncident)
    Observable<KeystoreIncidentResponse> keystoreIncident(@Body KeystoreIncidentRequest request);

    @POST(Endpoints.signUp)
    Observable<SignUpResponse> registerMobileUser(@Body SignUpRequest signUpRequest);

    @POST(Endpoints.signOut)
    Observable<SignOutResponse> signOut(@Body RequestInfoRequest signOutRequest);

    @POST(Endpoints.forgotPassword)
    Observable<CardSendResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST(Endpoints.changeForgotPassword)
    Observable<ChangeForgotPasswordResponse> changeForgotPassword(@Body ChangeForgotPasswordRequest changeForgotPasswordRequest);

    @POST(Endpoints.changeKeystore)
    Observable<ChangeKeystoreResponse> changeKeystore(@Body ChangeKeystoreRequest changeKeystoreRequest);

    @POST(Endpoints.listBankCards)
    Observable<BankCardsResponse> listBankCards(@Body RequestInfoRequest requestInfoRequest);

    @POST(Endpoints.listBankAccounts)
    Observable<BankAccountsResponse> listBankAccounts(@Body RequestInfoRequest requestInfoRequest);

    @POST(Endpoints.listBankLoans)
    Observable<BankLoansResponse> listBankLoans(@Body RequestInfoRequest requestInfoRequest);

    @POST(Endpoints.listBankDeposits)
    Observable<BankDepositsResponse> listBankDeposits(@Body RequestInfoRequest requestInfoRequest);

    @POST(Endpoints.listOperationsHistory)
    Observable<OperationsHistoryResponse> listOperationsHistory(@Body OperationsHistoryRequest requestInfoRequest);

    @POST(Endpoints.card2CardOperation)
    Observable<OperationCard2CardResponse> doC2CTransfer(@Body OperationCard2CardRequest requestInfoRequest);

    @POST(Endpoints.card2AccountOperation)
    Observable<OperationCard2AccountResponse> doC2AccountTransfer(@Body OperationCard2AccountRequest request);

    @POST(Endpoints.listCardStatements)
    Observable<BankCardStatementsResponse> listCardStatements(@Body BankCardStatementsRequest requestInfoRequest);

    @POST(Endpoints.changeAccountSettings)
    Observable<UpdateAccountSettingsResponse> updateAccountSettings(@Body UpdateAccountSettingsRequest updateCardSettingsRequest);

    @POST(Endpoints.changeCardSettings)
    Observable<UpdateCardSettingsResponse> updateCardSettings(@Body UpdateCardSettingsRequest updateCardSettingsRequest);

    @POST(Endpoints.exchangeRates)
    Observable<ExchangeRatesResponse> getExchangeRates(@Body ExchangeRatesRequest requestInfoRequest);

    @POST(Endpoints.notifications)
    Observable<UserNotificationsResponse> getUserNotifications(@Body UserNotificationsRequest request);

    @POST(Endpoints.news)
    Observable<BankNewsResponse> getBankNews(@Body BankNewsRequest request);

    @POST(Endpoints.atms)
    Observable<BankATMsResponse> getBankATMs(@Body RequestInfoRequest request);

    @POST(Endpoints.branches)
    Observable<BankBranchesResponse> getBankBranches(@Body RequestInfoRequest request);

    @POST(Endpoints.mobileUserData)
    Observable<MobileUserDataResponse> getMobileUserData(@Body MobileUserDataRequest mobileUserDataRequest);

    @POST(Endpoints.changeMobileUserData)
    Observable<ProfileUpdateResponse> changeMobileUserData(@Body ChangeMobileUserDataRequest changeMobileUserDataRequest);

    @POST(Endpoints.verifyMobileUserDataChange)
    Observable<VerifyCodeResponse> verifyMobileUserDataChange(@Body VerifyMobileUserDataChangeRequest verifyMobileUserDataChangeRequest);

    @POST(Endpoints.listMobileDevices)
    Observable<MobileDevicesListResponse> getMobileDevices(@Body RequestInfoRequest requestInfoRequest);

    @POST(Endpoints.removeDevice)
    Observable<ChangeDeviceSettingsResponse> alterDevice(@Body ChangeDeviceSettingsRequest changeDeviceSettingsRequest);

    @POST(Endpoints.moneyTransferCountries)
    Observable<MoneyTransferCountriesResponse> getCountries(@Body MoneyTransferCountriesRequest moneyTransferCountriesRequest);

    @POST(Endpoints.moneyTransferPaymentPoints)
    Observable<MoneyTransferPaymentPointsResponse> getPaymentPoints(@Body MoneyTransferPaymentPointsRequest moneyTransferPaymentPointsRequest);

    @POST(Endpoints.moneyTransferCommission)
    Observable<MoneyTransferCommissionResponse> getMoneyTransferCommission(@Body MoneyTransferCommissionRequest moneyTransferCommissionRequest);

    @POST(Endpoints.doMoneyTransfer)
    Observable<MoneyTransferResponse> doMoneyTransfer(@Body MoneyTransferRequest moneyTransferRequest);

    @POST(Endpoints.moneyTransferSearch)
    Observable<MoneyTransferOperationStatusCheckResponse> getTransferStatusBy(@Body MoneyTransferOperationStatusCheckRequest statusCheckRequest);

    @POST(Endpoints.moneyTransferReceiveCheck)
    Observable<MoneyTransferReceiveCheckResponse> receiveMoneyTransferCheck(@Body MoneyTransferReceiveCheckRequest request);

    @POST(Endpoints.moneyTransferReceive)
    Observable<MoneyTransferReceiveResponse> receiveMoneyTransfer(@Body MoneyTransferReceiveRequest request);

    @POST(Endpoints.paymentProviderGroups)
    Observable<PaymentProviderGroupsResponse> getPaymentProviderGroups(@Body RequestInfoRequest request);

    @POST(Endpoints.paymentProviders)
    Observable<PaymentProvidersResponse> getPaymentProviders(@Body PaymentProvidersRequest request);

    @POST(Endpoints.paymentValidation)
    Observable<PaymentValidationResponse> validatePayment(@Body PaymentValidationRequest request);

    @POST(Endpoints.qrCodeValidation)
    Observable<QrCodeValidationResponse> validateQrCode(@Body QrCodeValidationRequest request);

    @POST(Endpoints.paymentSubmission)
    Observable<PaymentSubmissionResponse> submitPayment(@Body PaymentSubmissionRequest request);

    @POST(Endpoints.signUpAsanImza)
    Observable<AsanImzaSignUpResponse> signUpWithAsanImza(@Body AsanImzaSignUpRequest request);

    @POST(Endpoints.verifyAsanImzaCode)
    Observable<AsanImzaCodeVerificationResponse> verifyAsanImzaCode(@Body RequestInfoRequest request);

    @POST(Endpoints.cardProducts)
    Observable<PlasticCardProductsResponse> getCardProducts(@Body RequestInfoRequest request);

    @POST(Endpoints.depositProducts)
    Observable<DepositProductsResponse> getDepositProducts(@Body RequestInfoRequest request);

    @POST(Endpoints.loanProducts)
    Observable<LoanProductsResponse> getLoanProducts(@Body RequestInfoRequest request);

    @POST(Endpoints.embassyReferenceProducts)
    Observable<EmbassyReferenceProductsResponse> getEmbassyReferenceProducts(@Body RequestInfoRequest request);

    @POST(Endpoints.financialReferenceProducts)
    Observable<FinancialReferenceProductsResponse> getFinancialReferenceProducts(@Body RequestInfoRequest request);

    @POST(Endpoints.productOrders)
    Observable<ProductOrdersResponse> getProductOrders(@Body ProductOrdersRequest request);

    @POST(Endpoints.orderPlasticCard)
    Observable<ProductPlasticCardOrderResponse> orderPlasticCard(@Body ProductPlasticCardOrderRequest request);

    @POST(Endpoints.orderLoan)
    Observable<ProductLoanOrderResponse> orderLoan(@Body ProductLoanOrderRequest request);

    @POST(Endpoints.orderDeposit)
    Observable<ProductDepositOrderResponse> orderDeposit(@Body ProductDepositOrderRequest request);

    @POST(Endpoints.orderEmbassyReference)
    Observable<ProductEmbassyReferenceOrderResponse> orderEmbassyReference(@Body ProductEmbassyReferenceOrderRequest request);

    @POST(Endpoints.orderFinancialReference)
    Observable<ProductFinancialReferenceOrderResponse> orderFinancialReference(@Body ProductFinancialReferenceOrderRequest request);

    @POST(Endpoints.embassyCountries)
    Observable<EmbassyCountriesResponse> getEmbassyCountries(@Body RequestInfoRequest request);

    @POST(Endpoints.embassyPoints)
    Observable<EmbassyPointsResponse> getEmbassyPoints(@Body EmbassyPointsRequest request);

    @POST(Endpoints.orderPayment)
    Observable<OrderPaymentResponse> payOrder(@Body OrderPaymentRequest request);

    @POST(Endpoints.internationalTransfer)
    Observable<LocalAndInternationalTransferResponse> makeInternationalTransfer(@Body InternationalTransferRequest request);

    @POST(Endpoints.localTransfer)
    Observable<LocalAndInternationalTransferResponse> makeLocalTransfer(@Body LocalTransferRequest request);

    @POST(Endpoints.budgetDestinations)
    Observable<BudgetDestinationsResponse> getBudgetDestinations(@Body BudgetDestinationsRequest request);

    @POST(Endpoints.budgetDestinationLevels)
    Observable<BudgetDestinationLevelsResponse> getBudgetLevels(@Body BudgetLevelsRequest request);

    @POST(Endpoints.localTransferBranches)
    Observable<LocalTransferBranchesResponse> getLocalTransferBranches(@Body LocalTransferBranchesRequest request);

    @POST(Endpoints.internationalTransfersHistory)
    Observable<InternationalTransfersHistoryResponse> getInternationalTransfersHistory(@Body InternationalTransfersHistoryRequest request);

    @POST(Endpoints.localTransfersHistory)
    Observable<LocalTransfersHistoryResponse> getLocalTransfersHistory(@Body InternationalTransfersHistoryRequest request);

    @POST(Endpoints.paymentsHistory)
    Observable<PaymentsHistoryResponse> getPaymentsHistory(@Body PaymentsHistoryRequest request);

    @POST(Endpoints.obsiyPayment)
    Observable<ObsiyPaymentResponse> getObsiyPaymentUi(@Body ObsiyPaymentRequest request);

    @POST(Endpoints.sendFCMToken)
    Observable<Object> sendFCMToken(@Body FcmTokenRequest request);
}
