import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/theme/app_text_styles.dart';
import '../../core/widgets/primary_action_button.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SmsVerificationScreen extends StatefulWidget {
  final String? phone;
  final String? password;
  final String? username;

  const SmsVerificationScreen({
    super.key,
    this.phone,
    this.password,
    this.username,
  });

  @override
  State<SmsVerificationScreen> createState() => _SmsVerificationScreenState();
}

class _SmsVerificationScreenState extends State<SmsVerificationScreen> {
  final List<TextEditingController> _controllers = List.generate(
    6,
        (_) => TextEditingController(),
  );
  final List<FocusNode> _focusNodes = List.generate(
    6,
        (_) => FocusNode(),
  );
  Timer? _timer;
  final ValueNotifier<int> _remainingMinutes = ValueNotifier<int>(5);
  final ValueNotifier<int> _remainingSeconds = ValueNotifier<int>(5);
  final ValueNotifier<bool> _canResend = ValueNotifier<bool>(false);
  final ValueNotifier<int> _codeLength = ValueNotifier<int>(0);

  @override
  void initState() {
    super.initState();
    _startTimer();
    // Add listeners to all controllers to track code length
    for (var controller in _controllers) {
      controller.addListener(_updateCodeLength);
    }
    // Auto-focus first field
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _focusNodes[0].requestFocus();
    });
  }

  void _updateCodeLength() {
    final length = _getCode().length;
    _codeLength.value = length;
  }

  @override
  void dispose() {
    _timer?.cancel();
    _remainingMinutes.dispose();
    _remainingSeconds.dispose();
    _canResend.dispose();
    _codeLength.dispose();
    for (var controller in _controllers) {
      controller.removeListener(_updateCodeLength);
      controller.dispose();
    }
    for (var focusNode in _focusNodes) {
      focusNode.dispose();
    }
    super.dispose();
  }

  void _startTimer() {
    _remainingMinutes.value = 5;
    _remainingSeconds.value = 5;
    _canResend.value = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_remainingSeconds.value > 0) {
        _remainingSeconds.value--;
      } else if (_remainingMinutes.value > 0) {
        _remainingMinutes.value--;
        _remainingSeconds.value = 59;
      } else {
        _canResend.value = true;
        timer.cancel();
      }
    });
  }

  String _formatTimer() {
    return '${_remainingMinutes.value.toString().padLeft(2, '0')}:${_remainingSeconds.value.toString().padLeft(2, '0')}';
  }

  String _getMaskedPhone() {
    if (widget.phone == null || widget.phone!.isEmpty) return '+994 50 *** ** 00';
    final phone = widget.phone!.replaceAll(RegExp(r'\D'), '');
    if (phone.length >= 9) {
      final lastTwo = phone.substring(phone.length - 2);
      return '+994 50 *** ** $lastTwo';
    }
    return '+994 50 *** ** 00';
  }

  void _onCodeChanged(int index, String value) {
    if (value.isEmpty) {
      return;
    }

    // Handle paste - multiple characters
    if (value.length > 1) {
      final digitsOnly = value.replaceAll(RegExp(r'\D'), '');
      for (int i = 0; i < digitsOnly.length && i < 6; i++) {
        if (index + i < 6) {
          _controllers[index + i].text = digitsOnly[i];
        }
      }
      // Move focus to last filled field or 6th field
      final lastIndex = (index + digitsOnly.length - 1).clamp(0, 5);
      if (lastIndex < 5) {
        _focusNodes[lastIndex + 1].requestFocus();
      } else {
        _focusNodes[lastIndex].unfocus();
      }
      return;
    }

    // Single character input
    if (value.length == 1) {
      _controllers[index].text = value;
      if (index < 5) {
        _focusNodes[index + 1].requestFocus();
      } else {
        _focusNodes[index].unfocus();
      }
    }
  }

  String _getCode() {
    return _controllers.map((c) => c.text).join();
  }

  void _handleVerify() {
    final code = _getCode();
    if (code.length == 6) {
      // After SMS verification, proceed with sign-in if password and username are provided
      if (widget.password != null && widget.username != null) {
        context.read<AuthBloc>().add(
          SignInEvent(
            username: widget.username!,
            password: widget.password!,
          ),
        );
      } else {
        // If no password/username, just verify the code
        context.read<AuthBloc>().add(
          VerifyCodeEvent(
            requestType: 1,
            verificationCode: code,
            phone: widget.phone,
            email: null,
          ),
        );
      }
    }
  }

  void _handleResend() {
    if (_canResend.value) {
      // Clear all fields
      for (var controller in _controllers) {
        controller.clear();
      }
      // Focus first field
      _focusNodes[0].requestFocus();
      // Restart timer
      _startTimer();
      // TODO: Implement actual resend API call
      // context.read<AuthBloc>().add(ResendCodeEvent(phone: widget.phone));
    }
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthError) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        } else if (state is AuthAuthenticated) {
          context.go('/home');
        } else if (state is PinSetupRequired) {
          context.push(
            '/sign-up-pin',
            extra: {
              'username': state.username,
              'passwordHash': state.passwordHash,
              'signInType': state.signInType,
              'isComingFromSignIn': state.isComingFromSignIn,
            },
          );
        } else if (state is CodeVerified) {
          if (widget.password == null || widget.username == null) {
            context.go('/home');
          }
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.white,
        resizeToAvoidBottomInset: true,
        body: SafeArea(
          child: Column(
            children: [
              // Back arrow
              Padding(
                padding: EdgeInsets.only(left: 16.w, top: 10.h),
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: IconButton(
                    icon: Icon(
                      Icons.arrow_back,
                      size: 20.w,
                      color: AppTheme.textDark,
                    ),
                    onPressed: () => context.pop(),
                  ),
                ),
              ),
              // Main content
              Expanded(
                child: SingleChildScrollView(
                  padding: EdgeInsets.symmetric(horizontal: 16.w),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(height: 40.h),
                      // Title
                      Text(
                        'Enter the SMS code',
                        style: AppTextStyles.screenTitle(context),
                        textAlign: TextAlign.center,
                      ),
                      SizedBox(height: 32.h),
                      // Description
                      RichText(
                        textAlign: TextAlign.center,
                        text: TextSpan(
                          style: AppTextStyles.phoneNumberBold(context).copyWith(
                            color: AppTheme.textSecondary,
                          ),
                          children: [
                            const TextSpan(text: 'We sent a 6-digit code to '),
                            TextSpan(
                              text: _getMaskedPhone(),
                            ),
                            const TextSpan(text: '. Enter it to continue'),
                          ],
                        ),
                      ),
                      SizedBox(height: 56.h),
                      // OTP Input Section
                      SizedBox(
                        width: double.infinity,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            // Label
                            Text(
                              'Enter the verification code',
                              style: AppTextStyles.inputLabel(context).copyWith(
                                color: AppTheme.textTertiary,
                              ),
                            ),
                            SizedBox(height: 8.h),
                            // OTP Fields
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: List.generate(6, (index) {
                                return Container(
                                  width: 44.w,
                                  height: 52.h,
                                  decoration: BoxDecoration(
                                    color: AppTheme.white,
                                    border: Border.all(
                                      color: AppTheme.borderOtpField,
                                      width: 1,
                                    ),
                                    borderRadius: BorderRadius.circular(12.r),
                                  ),
                                  child: TextField(
                                    controller: _controllers[index],
                                    focusNode: _focusNodes[index],
                                    textAlign: TextAlign.center,
                                    textAlignVertical: TextAlignVertical.center,
                                    keyboardType: TextInputType.number,
                                    maxLength: 6, // Allow paste of all digits
                                    inputFormatters: [
                                      FilteringTextInputFormatter.digitsOnly,
                                    ],
                                    style: AppTextStyles.otpFieldText(context),
                                    cursorColor: AppTheme.textPlaceholder,
                                    selectionControls: MaterialTextSelectionControls(),
                                    decoration: InputDecoration(
                                      counterText: '',
                                      border: InputBorder.none,
                                      enabledBorder: InputBorder.none,
                                      focusedBorder: InputBorder.none,
                                      disabledBorder: InputBorder.none,
                                      errorBorder: InputBorder.none,
                                      focusedErrorBorder: InputBorder.none,
                                      contentPadding: EdgeInsets.symmetric(
                                        horizontal: 0,
                                        vertical: 16.h, // Center vertically in 52h field
                                      ),
                                      isDense: false, // Set to false for better centering
                                      filled: false,
                                    ),
                                    onChanged: (value) => _onCodeChanged(index, value),
                                    onTap: () {
                                      // Select all text when tapped
                                      _controllers[index].selection = TextSelection(
                                        baseOffset: 0,
                                        extentOffset: _controllers[index].text.length,
                                      );
                                    },
                                  ),
                                );
                              }),
                            ),
                            SizedBox(height: 8.h),
                            // Timer and Resend
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                ValueListenableBuilder<int>(
                                  valueListenable: _remainingMinutes,
                                  builder: (context, minutes, _) {
                                    return ValueListenableBuilder<int>(
                                      valueListenable: _remainingSeconds,
                                      builder: (context, seconds, _) {
                                        return Text(
                                          '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}',
                                          style: AppTextStyles.inputLabel(context).copyWith(
                                            color: AppTheme.textTertiary,
                                          ),
                                        );
                                      },
                                    );
                                  },
                                ),
                                ValueListenableBuilder<bool>(
                                  valueListenable: _canResend,
                                  builder: (context, canResend, _) {
                                    return GestureDetector(
                                      onTap: canResend ? _handleResend : null,
                                      child: Text(
                                        'Resend OTP',
                                        style: AppTextStyles.inputLabel(context).copyWith(
                                          color: canResend
                                              ? AppTheme.mainColor
                                              : AppTheme.textTertiary,
                                        ),
                                      ),
                                    );
                                  },
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: 56.h),
                      // Verify Button
                      ValueListenableBuilder<int>(
                        valueListenable: _codeLength,
                        builder: (context, codeLength, _) {
                          return BlocBuilder<AuthBloc, AuthState>(
                            builder: (context, state) {
                              final isLoading = state is AuthLoading;
                              final isEnabled = codeLength == 6;
                              // Show "Continue" when code is complete, even during loading
                              return PrimaryActionButton(
                                text: isEnabled ? 'Continue' : 'Verify',
                                onPressed: (isEnabled && !isLoading) ? _handleVerify : null,
                                isEnabled: isEnabled,
                              );
                            },
                          );
                        },
                      ),
                      SizedBox(height: 56.h),
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
}