import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/utils/phone_utils.dart';
import '../../../../core/localization/app_localizations_ext.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

class OtpVerificationScreen extends StatefulWidget {
  final String phoneNumber;
  final OtpFlowType? flowType;

  const OtpVerificationScreen({
    super.key,
    required this.phoneNumber,
    this.flowType,
  });

  @override
  State<OtpVerificationScreen> createState() => _OtpVerificationScreenState();
}

class _OtpVerificationScreenState extends State<OtpVerificationScreen> {
  final List<TextEditingController> _controllers = List.generate(
    6,
    (_) => TextEditingController(),
  );
  final List<FocusNode> _focusNodes = List.generate(
    6,
    (_) => FocusNode(),
  );
  // Hidden TextField for SMS autofill
  final TextEditingController _autofillController = TextEditingController();
  final FocusNode _autofillFocusNode = FocusNode();

  Timer? _timer;
  int _remainingMinutes = 5;
  int _remainingSeconds = 0;
  bool _canResend = false;
  bool _isHandlingBackspace = false;
  final List<String> _previousValues = List.filled(6, '');

  @override
  void initState() {
    super.initState();
    _startTimer();
    // Listen to autofill controller changes
    _autofillController.addListener(_handleAutofillChange);
    // Auto-focus first field
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _focusNodes[0].requestFocus();
    });
  }

  @override
  void dispose() {
    _timer?.cancel();
    _autofillController.removeListener(_handleAutofillChange);
    _autofillController.dispose();
    _autofillFocusNode.dispose();
    for (var controller in _controllers) {
      controller.dispose();
    }
    for (var focusNode in _focusNodes) {
      focusNode.dispose();
    }
    super.dispose();
  }

  void _startTimer() {
    _remainingMinutes = 5;
    _remainingSeconds = 0;
    _canResend = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_remainingSeconds > 0) {
        setState(() {
          _remainingSeconds--;
        });
      } else if (_remainingMinutes > 0) {
        setState(() {
          _remainingMinutes--;
          _remainingSeconds = 59;
        });
      } else {
        setState(() {
          _canResend = true;
        });
        timer.cancel();
      }
    });
  }


  void _handleAutofillChange() {
    final code = _autofillController.text.replaceAll(RegExp(r'[^\d]'), '');
    if (code.length == 6) {
      // Distribute the code to all fields
      for (int i = 0; i < 6 && i < code.length; i++) {
        _controllers[i].text = code[i];
        _previousValues[i] = code[i];
      }
      // Clear autofill controller
      _autofillController.clear();
      // Auto-submit when all 6 digits are filled
      if (_isAllDigitsFilled()) {
        _handleVerify();
      }
    }
  }

  void _onDigitChanged(int index, String value) {
    // Check if this is a paste operation (multiple characters detected)
    // This happens when user pastes text into any field
    if (value.length > 1) {
      // Extract only digits from pasted text
      final code = value.replaceAll(RegExp(r'[^\d]'), '');
      if (code.length >= 6) {
        // Full 6-digit code pasted - distribute to all fields
        _handlePaste(code.substring(0, 6));
        return;
      } else if (code.length > 1) {
        // Partial paste - fill from current index onwards
        for (int i = 0; i < code.length && (index + i) < 6; i++) {
          _controllers[index + i].text = code[i];
          _previousValues[index + i] = code[i];
        }
        // Move focus to the next empty field or last field
        final nextIndex = (index + code.length).clamp(0, 5);
        _focusNodes[nextIndex].requestFocus();
        // Auto-submit if all filled
        if (_isAllDigitsFilled()) {
          _handleVerify();
        }
        return;
      }
    }

    // Normal single character input
    // Limit to single digit
    if (value.length > 1) {
      _controllers[index].text = value.substring(value.length - 1);
      _controllers[index].selection = TextSelection.collapsed(
        offset: _controllers[index].text.length,
      );
      value = _controllers[index].text;
    }

    // Update previous value
    _previousValues[index] = value;

    // Move to next field if digit entered
    if (value.isNotEmpty && index < 5) {
      _focusNodes[index + 1].requestFocus();
    }

    // Auto-submit when all 6 digits are filled
    if (_isAllDigitsFilled()) {
      _handleVerify();
    }
  }

  void _handlePaste(String pastedText) {
    // Extract only digits from pasted text
    final code = pastedText.replaceAll(RegExp(r'[^\d]'), '');
    if (code.length == 6) {
      // Distribute the code to all fields
      for (int i = 0; i < 6 && i < code.length; i++) {
        _controllers[i].text = code[i];
        _previousValues[i] = code[i];
      }
      // Focus the last field
      _focusNodes[5].requestFocus();
      // Auto-submit when all 6 digits are filled
      if (_isAllDigitsFilled()) {
        _handleVerify();
      }
    }
  }


  bool _isAllDigitsFilled() {
    return _controllers.every((controller) => controller.text.isNotEmpty);
  }

  String _getOtpCode() {
    return _controllers.map((controller) => controller.text).join();
  }

  void _handleVerify() {
    final otpCode = _getOtpCode();
    if (otpCode.length != 6) {
      return;
    }

    context.read<AuthBloc>().add(
          VerifyOtpEvent(
            otpCode: otpCode,
            phoneNumber: widget.phoneNumber,
            flowType: widget.flowType,
          ),
        );
  }

  void _handleResend() {
    if (!_canResend) return;

    // Resend OTP with appropriate parameters based on flow type
    final text = widget.flowType == OtpFlowType.simaSignIn
        ? context.l10n.simaSignIn
        : widget.flowType == OtpFlowType.forgotPassword
            ? 'Password reset' // OTP text for password reset
            : context.l10n.resendOtp;
    
    final otpType = widget.flowType == OtpFlowType.forgotPassword ? 2 : 1; // Type 2 for password reset, 1 for sign-in/verification
    
    context.read<AuthBloc>().add(
          SendOtpEvent(
            phoneNumber: widget.phoneNumber,
            text: text,
            type: otpType,
            userId: widget.phoneNumber, // Use phone number as userId
            flowType: widget.flowType,
          ),
        );
    _startTimer();
  }

  String _formatTimer() {
    return '${_remainingMinutes.toString().padLeft(2, '0')}:${_remainingSeconds.toString().padLeft(2, '0')}';
  }

  @override
  Widget build(BuildContext context) {
    final maskedPhone = PhoneUtils.maskPhoneNumber(widget.phoneNumber);

    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthError) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        } else if (state is PinSetupRequired) {
          // Navigate to PIN setup screen
          context.push(
            '/sign-up-pin',
            extra: {
              'username': state.username,
              'passwordHash': state.passwordHash,
              'signInType': state.signInType,
              'isComingFromSignIn': state.isComingFromSignIn,
            },
          );
        } else if (state is OtpVerifiedForForgotPassword) {
          // Navigate to new password screen with verification code
          context.push(
            '/new-password',
            extra: {
              'verificationCode': state.verificationCode,
              'phone': state.phone,
            },
          );
        } else if (state is OtpVerified) {
          // Navigate based on flow type
          if (widget.flowType == OtpFlowType.regularSignIn) {
            context.go('/home');
          } else if (widget.flowType == OtpFlowType.simaSignIn) {
            // Navigate to FIN code screen for SIMA authentication
            context.push('/fin-code?phoneNumber=${widget.phoneNumber}');
          } else {
            // Default navigation
            context.go('/home');
          }
        } else if (state is OtpSent) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(context.l10n.otpSentSuccessfully),
              backgroundColor: AppTheme.green,
            ),
          );
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        body: SafeArea(
          child: Column(
            children: [
              BackButtonWidget(
                onPressed: () => context.pop(),
              ),
              Expanded(
                child: SingleChildScrollView(
                  padding: EdgeInsets.symmetric(horizontal: 24.w),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      // SizedBox(height: 12.h),
                      // Title - H2/Medium
                      Text(
                        context.l10n.enterSmsCode,
                        style: AppTextStyles.screenTitle(context),
                        textAlign: TextAlign.center,
                      ),
                      SizedBox(height: 32.h),
                      // Subtitle - Body/Large with semibold phone number
                      Text(
                        '${context.l10n.weSentCodeTo}$maskedPhone${context.l10n.enterItToContinue}',
                        style: AppTextStyles.inputText(context, color: AppTheme.textDark).copyWith(
                          fontWeight: FontWeight.w600,
                        ),
                        textAlign: TextAlign.center,
                      ),
                      SizedBox(height: 12.h),
                      // OTP Input Section
                      AutofillGroup(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            // Label - Body/Small/Inter
                            Text(
                              context.l10n.enterVerificationCode,
                              style: AppTextStyles.inputLabel(context).copyWith(
                                color: AppTheme.textTertiary,
                              ),
                            ),
                            SizedBox(height: 8.h),
                            // Hidden TextField for SMS autofill
                            Opacity(
                              opacity: 0,
                              child: SizedBox(
                                height: 0,
                                width: 0,
                                child: TextField(
                                  controller: _autofillController,
                                  focusNode: _autofillFocusNode,
                                  keyboardType: TextInputType.number,
                                  autofillHints: const [AutofillHints.oneTimeCode],
                                  textInputAction: TextInputAction.done,
                                  inputFormatters: [
                                    FilteringTextInputFormatter.digitsOnly,
                                    LengthLimitingTextInputFormatter(6),
                                  ],
                                ),
                              ),
                            ),
                            // OTP Fields
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: List.generate(6, (index) {
                                return Padding(
                                  padding: EdgeInsets.only(
                                    right: index < 5 ? 12.w : 0,
                                  ),
                                  child: _buildOtpField(index),
                                );
                              }),
                            ),
                            SizedBox(height: 8.h),
                            // Timer and Resend
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                // Timer
                                Text(
                                  _formatTimer(),
                                  style: AppTextStyles.buttonSubtitle(context, color: AppTheme.textDark),
                                ),
                                // Resend OTP
                                GestureDetector(
                                  onTap: _canResend ? _handleResend : null,
                                  child: Text(
                                    context.l10n.resendOtp,
                                    style: AppTextStyles.buttonSubtitle(
                                      context,
                                      color: _canResend
                                          ? AppTheme.textDark
                                          : AppTheme.textTertiary,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: 56.h),
                      // Continue/Verify Button
                      BlocBuilder<AuthBloc, AuthState>(
                        builder: (context, state) {
                          final isLoading = state is AuthLoading;
                          final allDigitsFilled = _isAllDigitsFilled();
                          final isEnabled = allDigitsFilled && !isLoading;
                          final buttonText = allDigitsFilled 
                              ? context.l10n.continueButton 
                              : context.l10n.verify;

                          return PrimaryActionButton(
                            text: buttonText,
                            onPressed: isEnabled ? _handleVerify : null,
                            isEnabled: isEnabled,
                          );
                        },
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildOtpField(int index) {
    return Container(
      width: 44.w,
      height: 52.h,
      decoration: BoxDecoration(
        color: Colors.transparent,
        borderRadius: BorderRadius.circular(12.r),
        border: Border.all(
          color: AppTheme.borderOtpField,
          width: 1.0,
        ),
        boxShadow: const [],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(12.r),
        child: Focus(
          onKeyEvent: (node, event) {
            if (event is KeyDownEvent && 
                event.logicalKey == LogicalKeyboardKey.backspace) {
              // Handle backspace key press
              if (_controllers[index].text.isNotEmpty) {
                // Field has text - clear it
                _controllers[index].clear();
                _previousValues[index] = '';
                return KeyEventResult.handled;
              } else if (index > 0) {
                // Field is empty - move to previous and clear it
                _focusNodes[index - 1].requestFocus();
                Future.microtask(() {
                  if (mounted && _focusNodes[index - 1].hasFocus) {
                    _controllers[index - 1].clear();
                    _previousValues[index - 1] = '';
                  }
                });
                return KeyEventResult.handled;
              }
            }
            return KeyEventResult.ignored;
          },
          child: TextField(
          controller: _controllers[index],
          focusNode: _focusNodes[index],
          textAlign: TextAlign.center,
          keyboardType: TextInputType.number,
          style: AppTextStyles.otpFieldText(context).copyWith(
            color: AppTheme.textDark,
          ),
          inputFormatters: [
            FilteringTextInputFormatter.digitsOnly,
            // Allow up to 6 characters to detect paste events
            // The onChanged handler will process paste vs normal input
            LengthLimitingTextInputFormatter(6),
          ],
          decoration: InputDecoration(
            counterText: '',
            border: InputBorder.none,
            enabledBorder: InputBorder.none,
            focusedBorder: InputBorder.none,
            disabledBorder: InputBorder.none,
            errorBorder: InputBorder.none,
            focusedErrorBorder: InputBorder.none,
            filled: true,
            fillColor: Colors.transparent,
            contentPadding: const EdgeInsets.all(10),
            isDense: true,
          ),
          onChanged: (value) {
            if (_isHandlingBackspace) {
              return;
            }
            
            if (value.isNotEmpty) {
              _onDigitChanged(index, value);
            }
          },
          onTap: () {
            // Select all text when tapped
            _controllers[index].selection = TextSelection(
              baseOffset: 0,
              extentOffset: _controllers[index].text.length,
            );
          },
          onSubmitted: (_) {
            if (index < 5) {
              _focusNodes[index + 1].requestFocus();
            } else {
              _handleVerify();
            }
          },
          onEditingComplete: () {
            if (index < 5) {
              _focusNodes[index + 1].requestFocus();
            }
          },
          ),
        ),
      ),
    );
  }
}
