import 'package:equatable/equatable.dart';

abstract class AuthState extends Equatable {
  const AuthState();

  @override
  List<Object?> get props => [];
}

class AuthInitial extends AuthState {
  const AuthInitial();
}

class AuthLoading extends AuthState {
  const AuthLoading();
}

class AuthAuthenticated extends AuthState {
  const AuthAuthenticated();
}

class AuthUnauthenticated extends AuthState {
  const AuthUnauthenticated();
}

class AuthError extends AuthState {
  final String message;

  const AuthError(this.message);

  @override
  List<Object?> get props => [message];
}

class CodeSent extends AuthState {
  final String? phone;
  final String? email;

  const CodeSent({this.phone, this.email});

  @override
  List<Object?> get props => [phone, email];
}

class CodeVerified extends AuthState {
  final String? phone;
  final String? email;

  const CodeVerified({
    this.phone,
    this.email,
  });

  @override
  List<Object?> get props => [phone, email];
}

class DeviceNeedsRegistration extends AuthState {
  final String username;
  final bool isEmail;

  const DeviceNeedsRegistration({
    required this.username,
    required this.isEmail,
  });

  @override
  List<Object?> get props => [username, isEmail];
}

class DeviceNeedsRegistrationDialog extends AuthState {
  const DeviceNeedsRegistrationDialog();
}

class ChangeKeystoreSuccess extends AuthState {
  final String passwordHash; // New password hash for final sign-in
  final String username;
  final int signInType;
  final bool isComingFromSignIn;

  const ChangeKeystoreSuccess({
    required this.passwordHash,
    required this.username,
    required this.signInType,
    required this.isComingFromSignIn,
  });

  @override
  List<Object?> get props => [passwordHash, username, signInType, isComingFromSignIn];
}

class PinSetupRequired extends AuthState {
  final String username;
  final String passwordHash;
  final int signInType;
  final bool isComingFromSignIn;

  const PinSetupRequired({
    required this.username,
    required this.passwordHash,
    required this.signInType,
    required this.isComingFromSignIn,
  });

  @override
  List<Object?> get props => [username, passwordHash, signInType, isComingFromSignIn];
}

class PinVerificationRequired extends AuthState {
  final String username;
  final String passwordHash;
  final int signInType;

  const PinVerificationRequired({
    required this.username,
    required this.passwordHash,
    required this.signInType,
  });

  @override
  List<Object?> get props => [username, passwordHash, signInType];
}

class OtpSent extends AuthState {
  final String phoneNumber;
  final int remainingMinutes;
  final int remainingSeconds;
  final bool canResend;

  const OtpSent({
    required this.phoneNumber,
    this.remainingMinutes = 5,
    this.remainingSeconds = 0,
    this.canResend = false,
  });

  @override
  List<Object?> get props => [phoneNumber, remainingMinutes, remainingSeconds, canResend];
}

class OtpVerified extends AuthState {
  const OtpVerified();
}

class ForgotPasswordSuccess extends AuthState {
  final String? mobileNumber;

  const ForgotPasswordSuccess({
    this.mobileNumber,
  });

  @override
  List<Object?> get props => [mobileNumber];
}

class OtpVerifiedForForgotPassword extends AuthState {
  final String verificationCode;
  final String? phone;

  const OtpVerifiedForForgotPassword({
    required this.verificationCode,
    this.phone,
  });

  @override
  List<Object?> get props => [verificationCode, phone];
}

class PasswordChangedSuccess extends AuthState {
  final String? phoneNumber;

  const PasswordChangedSuccess({this.phoneNumber});

  @override
  List<Object?> get props => [phoneNumber];
}

