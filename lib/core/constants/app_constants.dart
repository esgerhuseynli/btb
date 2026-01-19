class AppConstants {
  // Base URLs
  static const String baseUrlDev = 'http://94.20.61.252:8087/';
  static const String baseUrlProd = 'http://94.20.61.252:8087/';
  
  // OTP API Base URL (different server)
  static const String otpApiBaseUrl = 'http://94.20.61.252:8088/';

  // Storage Keys
  static const String keyPinFingerprintScreenBypass = 'k0';
  static const String pinHash = 'd3';
  static const String passwordHash = 'd0';
  static const String username = 'd1';
  static const String sessionKey = 'd2';
  static const String hasActiveSession = 'd5';
  static const String signInType = 'd4';
  static const String isFingerprintEnabled = 'd6';
  static const String fcmNotificationToken = 'd7';
  static const String customerName = 'd8';
  static const String lastLogin = 'd9';
  static const String appLanguage = 'd11';

  // Cookie Key (matches Android Constants.COOKIE_KEY)
  static const String cookieKey = 'Cookie';

  // Timeouts
  static const int allowedLoginTimeoutSeconds = 120;

  // Sign Up Screen Types
  static const int signUpScreenTypePanCif = 123;
  static const int signUpScreenTypeNumberEmail = 234;

  // Sign Up Types
  static const int signUpTypePan = 1;
  static const int signUpTypeCif = 2;

  // Sign In/Up Types
  static const int signInUpTypeEmail = 1;
  static const int signInUpTypeNumber = 2;

  // Password Recovery Types
  static const int passwordRecoveryTypePan = 1;
  static const int passwordRecoveryTypeFin = 2;

  // Profile Update Types
  static const int profileUpdateTypeNone = 0;
  static const int profileUpdateTypeEmail = 1;
  static const int profileUpdateTypeMobileNumber = 2;
  static const int profileUpdateTypePassword = 3;

  // Device Status
  static const int deviceStatusDisable = 0;
  static const int deviceStatusEnable = 1;

  // Exchange Rate Types
  static const int exchangeRateCash = -1;
  static const int exchangeRateNonCash = 1;
  static const int exchangeRateTypeBuy = 0;
  static const int exchangeRateTypeSell = 1;

  // Service Point Types
  static const int servicePointTypeAtm = 727;
  static const int servicePointTypeBranch = 272;

  // Currencies
  static const int currencyAzn = 0;
  static const int currencyUsd = 1;
  static const int currencyEur = 2;
  static const int currencyRub = 3;
  static const int currencyGbp = 4;
  static const int currencyTry = 5;

  // Money Source Types
  static const int moneySourceTypeNone = 0;
  static const int moneySourceTypeAccount = 1;
  static const int moneySourceTypeCard = 2;

  // Money Transfer Point Types
  static const int moneyTransferPointTypeAll = 1;
  static const int moneyTransferPointTypeCity = 2;

  // QR Code Validation Results
  static const int qrCodeValidationResultNone = 0;
  static const int qrCodeValidationResultFailed = 1;
  static const int qrCodeValidationResultSuccess = 2;
  static const int qrCodeValidationResultNoSuchProvider = 3;

  // Payment Data Filling Method
  static const int paymentDataFillingMethodManual = 1;
  static const int paymentDataFillingMethodFromQrCode = 2;

  // Product Types
  static const int productTypePlasticCard = 3;
  static const int productTypeLoan = 1;
  static const int productTypeDeposit = 2;
  static const int productTypeEmbassyReference = 4;
  static const int productTypeFinancialReference = 5;

  // Payment UI Types
  static const int paymentUiTypeSpinner = 1;
  static const int paymentUiTypeEditText = 2;

  // Money Transfer Unique Codes
  static const String moneyTransferCodeZolotayaKorona = 'ZK';
  static const String moneyTransferCodeMonex = 'MX';
}
