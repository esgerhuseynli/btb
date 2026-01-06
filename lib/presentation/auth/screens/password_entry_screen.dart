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

class PasswordEntryScreen extends StatefulWidget {
  final String? phone;

  const PasswordEntryScreen({super.key, this.phone});

  @override
  State<PasswordEntryScreen> createState() => _PasswordEntryScreenState();
}

class _PasswordEntryScreenState extends State<PasswordEntryScreen> {
  final _passwordController = TextEditingController();
  final _passwordFocusNode = FocusNode();
  final _isButtonEnabled = ValueNotifier<bool>(false);
  bool _obscureText = true;

  @override
  void initState() {
    super.initState();
    _passwordController.addListener(_onPasswordChanged);
    // Request focus after the first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _passwordFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _passwordController.removeListener(_onPasswordChanged);
    _passwordController.dispose();
    _passwordFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
  }

  void _onPasswordChanged() {
    final password = _passwordController.text;
    _isButtonEnabled.value = password.length == 8;
  }

  void _togglePasswordVisibility() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  void _handleContinue() {
    final password = _passwordController.text;
    if (password.length == 8 && widget.phone != null) {
      // Normalize phone number: remove all non-digits, ensure it's 9 digits
      final phoneDigits = widget.phone!.replaceAll(RegExp(r'\D'), '');
      String username;
      if (phoneDigits.length == 9) {
        username = phoneDigits;
      } else if (phoneDigits.length == 12 && phoneDigits.startsWith('994')) {
        username = phoneDigits.substring(3);
      } else {
        username = phoneDigits;
      }

      // Trigger sign-in event
      context.read<AuthBloc>().add(
            SignInEvent(
              username: username,
              password: password,
            ),
          );
    }
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthAuthenticated) {
          context.go('/home');
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
        } else if (state is DeviceNeedsRegistration) {
          // User registered but current device needs to be registered
          // Show dialog like Android, then navigate to sign-up types
          _showSignUpDialog(context, state);
        } else if (state is AuthError) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        resizeToAvoidBottomInset: true,
        body: SafeArea(
        child: SingleChildScrollView(
          padding: EdgeInsets.symmetric(horizontal: 24.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(height: 60.h),
              // Title - H2/Medium style
              Text(
                'Enter password',
                style: AppTextStyles.screenTitle(context),
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 56.h),
              // Password input field
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Password label
                  Padding(
                    padding: EdgeInsets.only(left: 8.w, bottom: 8.h),
                    child: Text(
                      'Password',
                      style: AppTextStyles.inputLabel(context),
                    ),
                  ),
                  // Password input container
                  Container(
                    decoration: BoxDecoration(
                      color: AppTheme.white,
                      border: Border.all(
                        color: const Color(0xFFE5E7EB),
                        width: 1,
                      ),
                      borderRadius: BorderRadius.circular(20.r),
                    ),
                    child: TextFormField(
                      controller: _passwordController,
                      focusNode: _passwordFocusNode,
                      keyboardType: TextInputType.number,
                      obscureText: _obscureText,
                      autofocus: true,
                      inputFormatters: [
                        FilteringTextInputFormatter.digitsOnly,
                        LengthLimitingTextInputFormatter(8),
                      ],
                      style: AppTextStyles.inputText(context, color: AppTheme.textDark),
                      decoration: InputDecoration(
                        border: InputBorder.none,
                        enabledBorder: InputBorder.none,
                        focusedBorder: InputBorder.none,
                        errorBorder: InputBorder.none,
                        focusedErrorBorder: InputBorder.none,
                        contentPadding: EdgeInsets.only(
                          left: 12.w, // Gap after lock icon
                          right: 12.w, // Gap before eye icon
                          top: 16.h,
                          bottom: 16.h,
                        ),
                        prefixIcon: Padding(
                          padding: EdgeInsets.only(left: 16.w, right: 0),
                          child: Icon(
                            Icons.lock,
                            size: 24.sp,
                            color: const Color(0xFFC4C4C4),
                          ),
                        ),
                        prefixIconConstraints: BoxConstraints(
                          minWidth: 24.w,
                          minHeight: 24.h,
                        ),
                        suffixIcon: Padding(
                          padding: EdgeInsets.only(right: 8.w),
                          child: Container(
                            width: 40.w,
                            height: 40.h,
                            alignment: Alignment.center,
                            child: IconButton(
                              icon: Icon(
                                _obscureText ? Icons.visibility_off : Icons.visibility,
                                size: 24.sp,
                                color: const Color(0xFFC4C4C4),
                              ),
                              onPressed: _togglePasswordVisibility,
                              padding: EdgeInsets.all(8.w),
                              constraints: const BoxConstraints(),
                            ),
                          ),
                        ),
                        suffixIconConstraints: BoxConstraints(
                          minWidth: 40.w,
                          minHeight: 40.h,
                        ),
                        hintText: '••••••••',
                        hintStyle: AppTextStyles.inputHint(context),
                      ),
                    ),
                  ),
                  SizedBox(height: 8.h),
                  // Helper text
                  Padding(
                    padding: EdgeInsets.only(left: 8.w),
                    child: Text(
                      'Enter your 8-digit password to access BTB Bank',
                      style: AppTextStyles.buttonSubtitle(context, color: AppTheme.textDark),
                    ),
                  ),
                ],
              ),
              SizedBox(height: 132.h),
              // License agreement text
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 16.w),
                child: Text(
                  'By pressing "Continue" I accept the BTB Bank Licence Agreement conditions',
                  style: AppTextStyles.inputText(context, color: AppTheme.textDark),
                  textAlign: TextAlign.center,
                ),
              ),
              SizedBox(height: 16.h),
              // Continue button
              BlocBuilder<AuthBloc, AuthState>(
                builder: (context, state) {
                  final isLoading = state is AuthLoading;
                  return ValueListenableBuilder<bool>(
                    valueListenable: _isButtonEnabled,
                    builder: (context, isEnabled, child) {
                      return PrimaryActionButton(
                        text: 'Continue',
                        onPressed: isLoading ? null : _handleContinue,
                        isEnabled: isEnabled && !isLoading,
                      );
                    },
                  );
                },
              ),
              SizedBox(height: 32.h),
            ],
          ),
        ),
      ),
      ),
    );
  }

  void _showSignUpDialog(BuildContext context, DeviceNeedsRegistration state) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (dialogContext) => AlertDialog(
        backgroundColor: Colors.transparent,
        contentPadding: EdgeInsets.zero,
        content: Container(
          padding: const EdgeInsets.all(24.0),
          decoration: BoxDecoration(
            color: AppTheme.mainBackground,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'You need to sign up',
                style: Theme.of(context).textTheme.titleLarge,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: () {
                  Navigator.of(dialogContext).pop();
                  // Navigate to sign-up types screen
                  context.push(
                    '/sign-up-types',
                    extra: {
                      'screenType': -1, // No specific type when coming from sign-in
                      'verifyCode': null,
                      'phone': state.isEmail ? null : state.username,
                      'email': state.isEmail ? state.username : null,
                    },
                  );
                },
                child: const Text('OK'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

