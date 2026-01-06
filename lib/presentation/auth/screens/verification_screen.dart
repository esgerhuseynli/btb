import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/constants/app_constants.dart';
import '../../core/widgets/app_button.dart';
import '../../core/widgets/app_text_field.dart';
import '../../core/widgets/app_app_bar.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';
import '../../../data/models/card_send_request.dart';

class VerificationScreen extends StatefulWidget {
  final int requestType;
  final String? phone;
  final String? email;
  final CardSendRequest? cardSendRequest; // For resending code

  const VerificationScreen({
    super.key,
    required this.requestType,
    this.phone,
    this.email,
    this.cardSendRequest,
  });

  @override
  State<VerificationScreen> createState() => _VerificationScreenState();
}

class _VerificationScreenState extends State<VerificationScreen> {
  final _formKey = GlobalKey<FormState>();
  final _codeController = TextEditingController();
  Timer? _timer;
  int _remainingSeconds = 60;
  bool _canResend = false;

  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  @override
  void dispose() {
    _timer?.cancel();
    _codeController.dispose();
    super.dispose();
  }

  void _startTimer() {
    _remainingSeconds = 60;
    _canResend = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_remainingSeconds > 0) {
        setState(() {
          _remainingSeconds--;
        });
      } else {
        setState(() {
          _canResend = true;
        });
        timer.cancel();
      }
    });
  }

  void _formatCode(String value) {
    // Remove all non-digits
    final digitsOnly = value.replaceAll(RegExp(r'\D'), '');
    
    // Limit to 7 digits
    final limited = digitsOnly.length > 7 
        ? digitsOnly.substring(0, 7) 
        : digitsOnly;
    
    // Format as XXX-XXXX
    String formatted = limited;
    if (limited.length > 3) {
      formatted = '${limited.substring(0, 3)}-${limited.substring(3)}';
    }
    
    if (_codeController.text != formatted) {
      _codeController.value = TextEditingValue(
        text: formatted,
        selection: TextSelection.collapsed(offset: formatted.length),
      );
    }
  }

  void _handleVerify() {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final code = _codeController.text.toString();


    context.read<AuthBloc>().add(
          VerifyCodeEvent(
            requestType: widget.requestType,
            verificationCode: code,
            phone: widget.phone,
            email: widget.email,
          ),
        );
  }

  Future<void> _handleResend() async {
    // Rebuild CardSendRequest if needed
    if (widget.cardSendRequest != null) {
      context.read<AuthBloc>().add(
            SendCardNumberEvent(
              request: widget.cardSendRequest!,
            ),
          );
      _startTimer();
    } else {
      // Rebuild request from stored data (for CIF/Card)
      final prefs = await SharedPreferences.getInstance();
      if (widget.requestType == AppConstants.signUpTypeCif) {
        final cif = prefs.getString('signUpCif');
        final birthdate = prefs.getString('signUpDateOfBirth');
        if (cif != null && birthdate != null) {
          context.read<AuthBloc>().add(
                SendCardNumberForCifEvent(
                  cif: cif,
                  birthdate: birthdate,
                ),
              );
          _startTimer();
        }
      }
      // TODO: Handle Card resend similarly
    }
  }

  @override
  Widget build(BuildContext context) {
    final displayText = widget.phone != null
        ? 'Təsdiq kodu ${widget.phone} SMS - məktub vasitəsilə göndərildi.'
        : widget.email != null
            ? 'Təsdiq kodu ${widget.email} email vasitəsilə göndərildi.'
            : 'Təsdiq kodu göndərildi.';

    return Scaffold(
      backgroundColor: AppTheme.mainBackground,
      appBar: AppAppBar(
        title: 'Təsdiq',
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => context.pop(),
        ),
      ),
      body: SafeArea(
        child: BlocListener<AuthBloc, AuthState>(
          listener: (context, state) {
            if (state is AuthError) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(state.message),
                  backgroundColor: AppTheme.red,
                ),
              );
            } else if (state is CodeVerified) {
              // Navigate to sign-up screen with verification code
              final verifyCode = _codeController.text.replaceAll('-', '');
              if (widget.phone != null) {
                context.push(
                  '/sign-up-number',
                  extra: {
                    'signUpType': widget.requestType,
                    'verifyCode': verifyCode,
                    'phone': widget.phone,
                  },
                );
              } else if (widget.email != null) {
                context.push(
                  '/sign-up-email',
                  extra: {
                    'signUpType': widget.requestType,
                    'verifyCode': verifyCode,
                    'email': widget.email,
                  },
                );
              }
            } else if (state is CodeSent) {
              // Code resent successfully
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Təsdiq kodu yenidən göndərildi'),
                ),
              );
            }
          },
          child: BlocBuilder<AuthBloc, AuthState>(
            builder: (context, state) {
              final isLoading = state is AuthLoading;

              return SingleChildScrollView(
                padding: const EdgeInsets.all(24.0),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      const SizedBox(height: 32),
                      Text(
                        'Təsdiq',
                        style: Theme.of(context).textTheme.headlineMedium,
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 36),
                      Text(
                        displayText,
                        style: Theme.of(context).textTheme.bodyLarge,
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 30),
                      AppTextField(
                        controller: _codeController,
                        label: 'Təsdiq kodu',
                        hint: 'XXX-XXXX',
                        keyboardType: TextInputType.number,
                        maxLength: 7, // XXX-XXXX = 8 characters
                        inputFormatters: [
                          FilteringTextInputFormatter.digitsOnly,
                          LengthLimitingTextInputFormatter(8),
                          TextInputFormatter.withFunction((oldValue, newValue) {
                            final text = newValue.text.replaceAll('-', '');
                            if (text.length > 7) {
                              return oldValue;
                            }
                            String formatted = text;
                            if (text.length > 3) {
                              formatted = '${text.substring(0, 3)}-${text.substring(3)}';
                            }
                            return TextEditingValue(
                              text: formatted,
                              selection: TextSelection.collapsed(
                                offset: formatted.length,
                              ),
                            );
                          }),
                        ],
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Təsdiq kodu daxil edin';
                          }
                          final code = value.replaceAll('-', '');
                          if (code.length != 6) {
                            return 'Təsdiq kodu 6 rəqəm olmalıdır';
                          }
                          return null;
                        },
                        onChanged: (value) {
                          _formatCode(value);
                        },
                        onSubmitted: (_) {
                          if (_codeController.text.replaceAll('-', '').length == 7) {
                            _handleVerify();
                          }
                        },
                      ),
                      const SizedBox(height: 10),
                      if (state is AuthError)
                        Text(
                          state.message,
                          style: TextStyle(
                            color: AppTheme.red,
                            fontSize: 14,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      const SizedBox(height: 10),
                      TextButton(
                        onPressed: _canResend && !isLoading ? _handleResend : null,
                        child: Text(
                          _canResend
                              ? 'Yenidən göndər'
                              : '$_remainingSeconds saniyə',
                          style: TextStyle(
                            color: _canResend
                                ? AppTheme.mainColor
                                : AppTheme.textColorLight,
                            fontSize: 14,
                          ),
                        ),
                      ),
                      const SizedBox(height: 32),
                      AppButton(
                        text: 'Təsdiq et',
                        onPressed: isLoading ? null : _handleVerify,
                        isLoading: isLoading,
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}

