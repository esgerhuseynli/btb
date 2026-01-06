import 'package:equatable/equatable.dart';
import '../../../data/models/card_send_request.dart';

abstract class AuthEvent extends Equatable {
  const AuthEvent();

  @override
  List<Object?> get props => [];
}

class CheckAuthStatusEvent extends AuthEvent {
  const CheckAuthStatusEvent();
}

class SignInEvent extends AuthEvent {
  final String username;
  final String password;

  const SignInEvent({
    required this.username,
    required this.password,
  });

  @override
  List<Object?> get props => [username, password];
}

class SignOutEvent extends AuthEvent {
  const SignOutEvent();
}

class SignUpEvent extends AuthEvent {
  final int usernameType; // 1 = email, 2 = number
  final int signUpType; // 1 = PAN, 2 = CIF
  final String verificationCode;
  final String phoneNumber; // or email
  final String password;
  final String? pan;
  final String? customerNumber;
  final String? customerBirthdate;
  final String? mobileNumber; // For AsanImza
  final String? mobileNumberSecretCode; // For AsanImza

  const SignUpEvent({
    required this.usernameType,
    required this.signUpType,
    required this.verificationCode,
    required this.phoneNumber,
    required this.password,
    this.pan,
    this.customerNumber,
    this.customerBirthdate,
    this.mobileNumber,
    this.mobileNumberSecretCode,
  });

  @override
  List<Object?> get props => [
        usernameType,
        signUpType,
        verificationCode,
        phoneNumber,
        password,
        pan,
        customerNumber,
        customerBirthdate,
        mobileNumber,
        mobileNumberSecretCode,
      ];
}

class VerifyCodeEvent extends AuthEvent {
  final int requestType;
  final String verificationCode;
  final String? phone;
  final String? email;

  const VerifyCodeEvent({
    required this.requestType,
    required this.verificationCode,
    this.phone,
    this.email,
  });

  @override
  List<Object?> get props => [requestType, verificationCode, phone, email];
}

class SendCardNumberEvent extends AuthEvent {
  final CardSendRequest request;

  const SendCardNumberEvent({
    required this.request,
  });

  @override
  List<Object?> get props => [request];
}

class SendCardNumberForSignInEvent extends AuthEvent {
  final String? phone;
  final String? email;

  const SendCardNumberForSignInEvent({
    this.phone,
    this.email,
  });

  @override
  List<Object?> get props => [phone, email];
}

class SendCardNumberForCifEvent extends AuthEvent {
  final String cif;
  final String birthdate;

  const SendCardNumberForCifEvent({
    required this.cif,
    required this.birthdate,
  });

  @override
  List<Object?> get props => [cif, birthdate];
}

class ChangeKeystoreEvent extends AuthEvent {
  final String sessionKey;
  final int signInType; // SIGN_IN_UP_TYPE_NUMBER or SIGN_IN_UP_TYPE_EMAIL

  const ChangeKeystoreEvent({
    required this.sessionKey,
    required this.signInType,
  });

  @override
  List<Object?> get props => [sessionKey, signInType];
}

class SetupPinEvent extends AuthEvent {
  final String pin;
  final String username;
  final String passwordHash; // From ChangeKeystore response
  final int signInType;
  final bool isComingFromSignIn; // true if from sign-in, false if from sign-up

  const SetupPinEvent({
    required this.pin,
    required this.username,
    required this.passwordHash,
    required this.signInType,
    required this.isComingFromSignIn,
  });

  @override
  List<Object?> get props => [pin, username, passwordHash, signInType, isComingFromSignIn];
}

class VerifyPinEvent extends AuthEvent {
  final String pin;

  const VerifyPinEvent({
    required this.pin,
  });

  @override
  List<Object?> get props => [pin];
}

