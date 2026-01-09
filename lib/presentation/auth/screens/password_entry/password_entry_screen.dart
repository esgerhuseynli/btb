import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/localization/app_localizations_ext.dart';
import '../../../../core/constants/app_dimensions.dart';
import '../../../../core/utils/phone_utils.dart';
import '../../../../core/widgets/error_snackbar.dart';
import '../../../../l10n/app_localizations.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

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

  /// Validates password length (8-16 characters)
  bool _isPasswordValid(String password) {
    return password.length >= 8 && password.length <= 16;
  }

  void _onPasswordChanged() {
    final password = _passwordController.text;
    // Enable button when password is valid
    _isButtonEnabled.value = _isPasswordValid(password);
  }

  void _togglePasswordVisibility() {
    setState(() {
      _obscureText = !_obscureText;
    });
  }

  void _handleContinue() {
    final password = _passwordController.text;
    // Validate password and phone
    if (_isPasswordValid(password) && widget.phone != null) {
      // Normalize phone number to username format (business logic in PhoneUtils)
      final username = PhoneUtils.normalizeToUsername(widget.phone!);

      // Call SignInEvent - this will trigger sign-in API, then SendOtp API
      context.read<AuthBloc>().add(
            SignInEvent(
              username: username,
              password: password,
            ),
          );
    }
  }

  Widget _buildTitle(BuildContext context) {
    return _PasswordTitle(l10n: context.l10n);
  }

  Widget _buildPasswordField(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // Password label
        Padding(
          padding: AppDimensions.paddingLeft8Bottom8,
          child: Text(
            context.l10n.password,
            style: AppTextStyles.inputLabel(context),
          ),
        ),
        // Password input container
        Container(
          decoration: BoxDecoration(
            color: AppTheme.white,
            border: const Border.fromBorderSide(
              BorderSide(
                color: Color(0xFFE5E7EB),
                width: 1,
              ),
            ),
            borderRadius: AppDimensions.radius20,
          ),
          child: Padding(
            padding: AppDimensions.padding16,
            child: Row(
              children: [
                _buildLockIcon(),
                AppDimensions.sizedBoxWidth12,
                _buildPasswordInput(context),
                AppDimensions.sizedBoxWidth12,
                _buildVisibilityToggle(),
              ],
            ),
          ),
        ),
        AppDimensions.sizedBox8,
        // Helper text placeholder
        Padding(
          padding: AppDimensions.paddingLeft8,
        ),
      ],
    );
  }

  Widget _buildLockIcon() {
    return const _LockIcon();
  }

  Widget _buildPasswordInput(BuildContext context) {
    return Expanded(
      child: TextFormField(
        controller: _passwordController,
        focusNode: _passwordFocusNode,
        keyboardType: TextInputType.text,
        obscureText: _obscureText,
        autofocus: true,
        inputFormatters: [
          LengthLimitingTextInputFormatter(16),
        ],
        style: AppTextStyles.inputText(context, color: AppTheme.textDark),
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
    );
  }

  Widget _buildVisibilityToggle() {
    return Container(
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
              Color(0xFFC4C4C4),
              BlendMode.srcIn,
            ),
          ),
        ),
        onPressed: _togglePasswordVisibility,
        padding: AppDimensions.padding8,
        constraints: const BoxConstraints(),
      ),
    );
  }

  Widget _buildLicenseAgreement(BuildContext context) {
    return _LicenseAgreementText(l10n: context.l10n);
  }

  Widget _buildContinueButton(BuildContext context) {
    return BlocBuilder<AuthBloc, AuthState>(
      builder: (context, state) {
        final isLoading = state is AuthLoading;
        return ValueListenableBuilder<bool>(
          valueListenable: _isButtonEnabled,
          builder: (context, isEnabled, child) {
            return PrimaryActionButton(
              text: context.l10n.continueButton,
              onPressed: isLoading ? null : _handleContinue,
              isEnabled: isEnabled && !isLoading,
            );
          },
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthAuthenticated) {
          context.go('/home');
        } else if (state is OtpSent) {
          // Navigate to SMS verification screen after OTP is sent
          context.push(
            '/sms-verification',
            extra: {
              'phone': widget.phone,
            },
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
        } else if (state is DeviceNeedsRegistration) {
          // User registered but current device needs to be registered
          // Show dialog like Android, then navigate to sign-up types
          _showSignUpDialog(context, state);
        } else if (state is AuthError) {
          ErrorSnackBar.show(context, state.message);
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        resizeToAvoidBottomInset: true,
        body: SafeArea(
          child: Column(
            children: [
              const BackButtonWidget(),
              Expanded(
                child: LayoutBuilder(
                  builder: (context, constraints) {
                    return SingleChildScrollView(
                      padding: AppDimensions.paddingHorizontal24,
                      child: ConstrainedBox(
                        constraints: BoxConstraints(
                          minHeight: constraints.maxHeight,
                        ),
                        child: IntrinsicHeight(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              AppDimensions.sizedBox40,
                              _buildTitle(context),
                              AppDimensions.sizedBox40,
                              _buildPasswordField(context),
                              const Spacer(),
                              _buildLicenseAgreement(context),
                              AppDimensions.sizedBox16,
                              _buildContinueButton(context),
                              AppDimensions.sizedBox24,
                            ],
                          ),
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
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
        content: _buildDialogContent(context, dialogContext),
      ),
    );
  }

  Widget _buildDialogContent(BuildContext context, BuildContext dialogContext) {
    return Container(
      padding: AppDimensions.padding24,
          decoration: BoxDecoration(
            color: AppTheme.mainBackground,
        borderRadius: AppDimensions.radius12,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
            context.l10n.youNeedToSignUp,
                style: Theme.of(context).textTheme.titleLarge,
                textAlign: TextAlign.center,
              ),
          AppDimensions.sizedBox24,
              ElevatedButton(
                onPressed: () {
                  Navigator.of(dialogContext).pop();
                },
            child: Text(context.l10n.ok),
          ),
        ],
      ),
    );
  }
}

// Stateless Widgets

class _PasswordTitle extends StatelessWidget {
  final AppLocalizations l10n;

  const _PasswordTitle({required this.l10n});

  @override
  Widget build(BuildContext context) {
    return Text(
      l10n.enterPassword,
      style: AppTextStyles.screenTitle(context),
      textAlign: TextAlign.center,
    );
  }
}

class _LockIcon extends StatelessWidget {
  const _LockIcon();

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: 24.w,
      height: 24.h,
      child: SvgPicture.asset(
        'assets/icons/lock.svg',
        width: 24.w,
        height: 24.h,
        colorFilter: const ColorFilter.mode(
          Color(0xFFC4C4C4),
          BlendMode.srcIn,
        ),
      ),
    );
  }
}

class _LicenseAgreementText extends StatelessWidget {
  final AppLocalizations l10n;

  const _LicenseAgreementText({required this.l10n});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: AppDimensions.paddingHorizontal16,
      child: Text(
        l10n.licenseAgreement,
        style: AppTextStyles.buttonTitle(context, color: AppTheme.textDark),
        textAlign: TextAlign.center,
      ),
    );
  }
}

