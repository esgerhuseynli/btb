import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
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
    // Enable button when password is between 8 and 16 characters
    _isButtonEnabled.value = password.length >= 8 && password.length <= 16;
  }

  void _togglePasswordVisibility() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  void _handleContinue() {
    final password = _passwordController.text;
    // Validate password is between 8 and 16 characters
    if (password.length >= 8 && password.length <= 16 && widget.phone != null) {
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

      // Navigate to SMS verification screen after correct password
      context.push(
        '/sms-verification',
        extra: {
          'phone': widget.phone,
          'password': password,
          'username': username,
        },
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
          child: LayoutBuilder(
            builder: (context, constraints) {
              return SingleChildScrollView(
                padding: EdgeInsets.symmetric(horizontal: 24.w),
                child: ConstrainedBox(
                  constraints: BoxConstraints(
                    minHeight: constraints.maxHeight,
                  ),
                  child: IntrinsicHeight(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        SizedBox(height: 40.h),
                        // Title - H2/Medium style
                        Text(
                          'Enter password',
                          style: AppTextStyles.screenTitle(context),
                          textAlign: TextAlign.center,
                        ),
                        SizedBox(height: 40.h),
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
                                color: AppTheme.white, // White / Base
                                border: Border.all(
                                  color: const Color(0xFFE5E7EB), // Stroke gray
                                  width: 1,
                                ),
                                borderRadius: BorderRadius.circular(20.r), // Radius: 20px
                              ),
                              child: Padding(
                                padding: EdgeInsets.all(16.w), // Padding: 16px
                                child: Row(
                                  children: [
                                    // Lock icon - clickable to toggle password visibility
                                    GestureDetector(
                                      onTap: _togglePasswordVisibility,
                                      child: SizedBox(
                                        width: 24.w,
                                        height: 24.h,
                                        child: SvgPicture.asset(
                                          'assets/icons/lock.svg',
                                          width: 24.w,
                                          height: 24.h,
                                          colorFilter: const ColorFilter.mode(
                                            Color(0xFFC4C4C4), // ICON DEFAULT / Disabled background
                                            BlendMode.srcIn,
                                          ),
                                        ),
                                      ),
                                    ),
                                    SizedBox(width: 12.w), // Gap: 12px
                                    // Password input field
                                    Expanded(
                                      child: TextFormField(
                                        controller: _passwordController,
                                        focusNode: _passwordFocusNode,
                                        keyboardType: TextInputType.text, // Normal keyboard
                                        obscureText: _obscureText, // Show dots when typing
                                        autofocus: true,
                                        inputFormatters: [
                                          LengthLimitingTextInputFormatter(16), // Max 16 characters
                                        ],
                                        style: AppTextStyles.inputText(context, color: AppTheme.textDark), // Body/Large
                                        decoration: InputDecoration(
                                          border: InputBorder.none,
                                          enabledBorder: InputBorder.none,
                                          focusedBorder: InputBorder.none,
                                          errorBorder: InputBorder.none,
                                          focusedErrorBorder: InputBorder.none,
                                          contentPadding: EdgeInsets.zero,
                                          isDense: true,
                                          hintText: '••••••••',
                                          hintStyle: AppTextStyles.inputHint(context),
                                        ),
                                      ),
                                    ),
                                    SizedBox(width: 12.w), // Gap: 12px
                                    // Eye icon button
                                    Container(
                                      width: 40.w,
                                      height: 40.h,
                                      alignment: Alignment.center,
                                      child: IconButton(
                                        icon: SizedBox(
                                          width: 24.w,
                                          height: 24.h,
                                          child: SvgPicture.asset(
                                            'assets/icons/iconstack.io.svg',
                                            width: 24.w,
                                            height: 24.h,
                                            colorFilter: const ColorFilter.mode(
                                              Color(0xFFC4C4C4), // ICON DEFAULT / Disabled background
                                              BlendMode.srcIn,
                                            ),
                                          ),
                                        ),
                                        onPressed: _togglePasswordVisibility,
                                        padding: EdgeInsets.all(8.w),
                                        constraints: const BoxConstraints(),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(height: 8.h),
                            // Helper text
                            Padding(
                              padding: EdgeInsets.only(left: 8.w),
                              child: Text(
                                'Enter your password 8-to access BTB Bank',
                                style: AppTextStyles.buttonSubtitle(context, color: AppTheme.textDark),
                              ),
                            ),
                          ],
                        ),
                        const Spacer(),
                        // License agreement text
                        Padding(
                          padding: EdgeInsets.symmetric(horizontal: 16.w),
                          child: Text(
                            'By pressing "Continue" I accept the BTB Bank Licence Agreement conditions',
                            style: AppTextStyles.buttonTitle(context, color: AppTheme.textDark),
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
                        SizedBox(height: 24.h),
                      ],
                    ),
                  ),
                ),
              );
            },
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

