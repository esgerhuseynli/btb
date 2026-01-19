class ApiEndpoints {
  // Authentication
  static const String sendCardNumber = 'api/SingInUp/SignUp';
  static const String verifyCode = 'api/SingInUp/VerifyCode';
  static const String signIn = 'api/SingInUp/SignInNew';
  static const String keystoreIncident = 'api/MobileUser/KeystoreSecurityIncident';
  static const String signUp = 'api/SingInUp/RegisterMobileUser';
  static const String signOut = 'api/SingInUp/SignOut';
  static const String forgotPassword = 'api/SingInUp/UserForgotPassword';
  static const String changeForgotPassword = 'api/SingInUp/ChangeForgottenPassword';
  static const String changeKeystore = 'api/MobileUser/ChangeKeystore';
  static const String signUpAsanImza = 'api/SingInUp/SignUpAsanImza';
  static const String verifyAsanImzaCode = 'api/SingInUp/VerifyAsanImzaCode';
  
  // Bank Accounts
  static const String listBankCards = 'api/BankAccounts/ListBankCards';
  static const String listBankAccounts = 'api/BankAccounts/ListBankAccounts';
  static const String listBankLoans = 'api/BankAccounts/ListBankLoans';
  static const String listBankDeposits = 'api/BankAccounts/ListBankDeposits';
  static const String changeAccountSettings = 'api/BankAccounts/ChangeMobileUserAccountSettings';
  static const String changeCardSettings = 'api/BankAccounts/ChangeMobileUserCardSettings';
  
  // Operations
  static const String listOperationsHistory = 'api/OperationsBankCards/ListBankCardOperations';
  static const String card2CardOperation = 'api/OperationsBankCards/OperationCardToCard';
  static const String card2AccountOperation = 'api/OperationsBankCards/OperationCardToAccount';
  static const String listCardStatements = 'api/OperationsBankCards/ListBankCardStatement';
  
  // Bank News
  static const String exchangeRates = 'api/BankNews/ListBankExchangeRates';
  static const String notifications = 'api/BankNews/ListUserNotifications';
  static const String news = 'api/BankNews/ListBankNews';
  static const String atms = 'api/BankNews/ListBankATMS';
  static const String branches = 'api/BankNews/ListBankBranches';
  
  // Mobile User
  static const String mobileUserData = 'api/MobileUser/GetMobileUserData';
  static const String changeMobileUserData = 'api/MobileUser/ChangeMobileUserData';
  static const String verifyMobileUserDataChange = 'api/MobileUser/VerifyMobileUserData';
  static const String listMobileDevices = 'api/MobileUser/ListMobileUserDevices';
  static const String removeDevice = 'api/MobileUser/ChangeDeviceSettings';
  static const String sendFCMToken = 'api/MobileUser/ChangeDevicePushInfoToken';
  
  // OTP
  static const String sendOtp = 'api/Otp/SendOtpMobile';
  static const String verifyOtp = 'api/Otp/VerifyOtp';
  
  // Money Transfers
  static const String moneyTransferCountries = 'api/MoneyTransfers/ListMTCountries';
  static const String moneyTransferPaymentPoints = 'api/MoneyTransfers/ListMTPaymentPoints';
  static const String moneyTransferCommission = 'api/MoneyTransfers/CalculateMTCommission';
  static const String doMoneyTransfer = 'api/MoneyTransfers/OperationSendTransfer';
  static const String moneyTransferSearch = 'api/MoneyTransfers/OperationCheckTransferStatus';
  static const String moneyTransferReceiveCheck = 'api/MoneyTransfers/OperationCheckBeforeReceiveTransfer';
  static const String moneyTransferReceive = 'api/MoneyTransfers/OperationReceiveTransfer';
  
  // Payments
  static const String paymentProviderGroups = 'api/Payments/ListPaymentProviderGroups';
  static const String paymentProviders = 'api/Payments/ListPaymentProviders';
  static const String paymentValidation = 'api/Payments/ValidatePayment';
  static const String qrCodeValidation = 'api/QRCode/ValidateQRCode';
  static const String paymentSubmission = 'api/Payments/OperationPayment';
  static const String paymentsHistory = 'api/Payments/ListMobilePayments';
  static const String obsiyPayment = 'api/Payments/ListPaymentProviderJSONParameters';
  
  // Product Orders
  static const String cardProducts = 'api/ProductOrders/ListPlasticCardProducts';
  static const String depositProducts = 'api/ProductOrders/ListDepositProducts';
  static const String loanProducts = 'api/ProductOrders/ListLoanProducts';
  static const String embassyReferenceProducts = 'api/ProductOrders/ListEmbasyReferenceProducts';
  static const String financialReferenceProducts = 'api/ProductOrders/ListFinancialReferenceProducts';
  static const String productOrders = 'api/ProductOrders/ListMobileUserProducts';
  static const String orderPlasticCard = 'api/ProductOrders/OrderPlasticCardProduct';
  static const String orderLoan = 'api/ProductOrders/OrderLoanProduct';
  static const String orderDeposit = 'api/ProductOrders/OrderDepositProduct';
  static const String orderEmbassyReference = 'api/ProductOrders/OrderEmbasyReferenceProduct';
  static const String orderFinancialReference = 'api/ProductOrders/OrderFinancialReferenceProduct';
  static const String embassyCountries = 'api/ProductOrders/ListEmbasyCountries';
  static const String embassyPoints = 'api/ProductOrders/ListEmbasyPoints';
  static const String orderPayment = 'api/ProductOrders/ProductOrderPayment';
  
  // Account Transfers
  static const String internationalTransfer = 'api/AccountsTransfers/OperationForeignAccountTransfer';
  static const String localTransfer = 'api/AccountsTransfers/OperationLocalAccountTransfer';
  static const String budgetDestinations = 'api/AccountsTransfers/ListBudgetDestinations';
  static const String budgetDestinationLevels = 'api/AccountsTransfers/ListBudgetLevels';
  static const String localTransferBranches = 'api/AccountsTransfers/ListLocalBranchCodes';
  static const String internationalTransfersHistory = 'api/AccountsTransfers/ListForeignAccountTransfers';
  static const String localTransfersHistory = 'api/AccountsTransfers/ListLocalAccountTransfers';
}



