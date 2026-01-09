// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get appTitle => 'BTB Mobile Banking';

  @override
  String get mobileBanking => 'BTB Mobile Banking';

  @override
  String get accessBankingServices => 'Access banking services with ease';

  @override
  String get securePayments => 'Secure Payments';

  @override
  String get secureAndFastPayments => 'Make secure and fast payments';

  @override
  String get transfers => 'Transfers';

  @override
  String get easyMoneyTransfers => 'Make money transfers easily';

  @override
  String get start => 'Start';

  @override
  String get continueButton => 'Continue';

  @override
  String get next => 'Next';

  @override
  String get phoneNumber => 'Phone Number';

  @override
  String get email => 'Email';

  @override
  String get password => 'Password';

  @override
  String get enterPassword => 'Enter password';

  @override
  String get enterYourPassword => 'Enter your password';

  @override
  String get forgotPassword => 'Forgot password?';

  @override
  String get passwordReset => 'Password reset?';

  @override
  String get enterDateOfBirthOrFin =>
      'Please enter your date of birth or FIN code';

  @override
  String get signIn => 'Sign In';

  @override
  String get signUp => 'Sign Up';

  @override
  String get dontHaveAccount => 'Don\'t have an account? ';

  @override
  String get register => 'Register';

  @override
  String get licenseAgreement =>
      'By pressing \"Continue\" I accept the BTB Bank Licence Agreement conditions';

  @override
  String get youNeedToSignUp => 'You need to sign up';

  @override
  String get ok => 'OK';

  @override
  String get wrongPinCode => 'Wrong PIN code';

  @override
  String get setNewPassword => 'Set new password';

  @override
  String get enterNewPassword => 'Please enter your new password';

  @override
  String get newPassword => 'New password';

  @override
  String get enterNewPasswordHint => 'Enter new password';

  @override
  String get confirmPassword => 'Confirm password';

  @override
  String get confirmPasswordHint => 'Confirm new password';

  @override
  String get passwordRequired => 'Password is required';

  @override
  String get passwordMinLength => 'Password must be 8 characters';

  @override
  String get pleaseConfirmPassword => 'Please confirm your password';

  @override
  String get passwordsDoNotMatch => 'Passwords do not match';

  @override
  String get enterEmailAddress => 'Enter your email address';

  @override
  String get phoneNumberHint => 'XX XXX XX XX';

  @override
  String get mobileNumber => 'Mobile Number';

  @override
  String get errorGeneric => 'An error occurred. Please try again.';

  @override
  String get errorNetwork =>
      'Network connection error. Please check your internet connection.';

  @override
  String get errorInvalidPassword => 'Invalid password. Please try again.';

  @override
  String get errorWrongPassword => 'Incorrect password. Please try again.';

  @override
  String get errorInvalidUsername => 'Invalid username or phone number.';

  @override
  String get errorUserNotFound =>
      'User not found. Please check your credentials.';

  @override
  String get errorAuthenticationFailed =>
      'Authentication failed. Please try again.';

  @override
  String get errorWrongPin => 'Incorrect PIN code. Please try again.';

  @override
  String get errorInvalidCode => 'Invalid verification code. Please try again.';

  @override
  String get errorCodeExpired =>
      'Verification code has expired. Please request a new one.';

  @override
  String get errorInvalidOtp => 'Invalid OTP code. Please try again.';

  @override
  String get errorOtpExpired =>
      'OTP code has expired. Please request a new one.';

  @override
  String get errorDeviceNotRegistered =>
      'Device is not registered. Please complete registration.';

  @override
  String get errorAccountLocked =>
      'Your account has been locked. Please contact support.';

  @override
  String get errorAccountSuspended =>
      'Your account has been suspended. Please contact support.';

  @override
  String get errorServerError => 'Server error. Please try again later.';

  @override
  String get errorServiceUnavailable =>
      'Service is temporarily unavailable. Please try again later.';
}
