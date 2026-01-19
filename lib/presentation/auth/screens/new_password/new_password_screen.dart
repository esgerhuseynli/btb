import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/localization/app_localizations_ext.dart';
import '../../../../core/constants/app_dimensions.dart';
import '../../../../core/constants/password_rules.dart';
import '../../../../core/widgets/error_snackbar.dart';
import '../../../../l10n/app_localizations.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../../core/widgets/app_text_field.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

class NewPasswordScreen extends StatefulWidget {
  final String? verificationCode;

  const NewPasswordScreen({
    super.key,
    this.verificationCode,
  });

  @override
  State<NewPasswordScreen> createState() => _NewPasswordScreenState();
}

class _NewPasswordScreenState extends State<NewPasswordScreen> {
  final _newPasswordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  final _newPasswordFocusNode = FocusNode();
  final _confirmPasswordFocusNode = FocusNode();
  final _isButtonEnabled = ValueNotifier<bool>(false);
  final _formKey = GlobalKey<FormState>();
  bool _obscurePasswords = true;

  @override
  void initState() {
    super.initState();
    _newPasswordController.addListener(_onFormChanged);
    _confirmPasswordController.addListener(_onFormChanged);
    // Request focus after the first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _newPasswordFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _newPasswordController.removeListener(_onFormChanged);
    _confirmPasswordController.removeListener(_onFormChanged);
    _newPasswordController.dispose();
    _confirmPasswordController.dispose();
    _newPasswordFocusNode.dispose();
    _confirmPasswordFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
  }

  /// Validates password length using PasswordRules
  bool _isPasswordValid(String password) {
    return password.length >= PasswordRules.minLength &&
        password.length <= PasswordRules.maxLength;
  }

  void _onFormChanged() {
    final newPassword = _newPasswordController.text;
    final confirmPassword = _confirmPasswordController.text;
    _isButtonEnabled.value = _isPasswordValid(newPassword) &&
        _isPasswordValid(confirmPassword) &&
        newPassword == confirmPassword;
  }

  void _togglePasswordVisibility() {
    setState(() {
      _obscurePasswords = !_obscurePasswords;
    });
  }

  String? _validateNewPassword(String? value) {
    if (value == null || value.isEmpty) {
      return context.l10n.passwordRequired;
    }
    if (!_isPasswordValid(value)) {
      return context.l10n.passwordMinLength;
    }
    return null;
  }

  String? _validateConfirmPassword(String? value) {
    if (value == null || value.isEmpty) {
      return context.l10n.pleaseConfirmPassword;
    }
    if (value != _newPasswordController.text) {
      return context.l10n.passwordsDoNotMatch;
    }
    return null;
  }

  void _handleContinue() {
    if (_formKey.currentState?.validate() ?? false) {
      final newPassword = _newPasswordController.text.trim();

      if (widget.verificationCode != null) {
        context.read<AuthBloc>().add(
              ChangeForgotPasswordEvent(
                verificationCode: widget.verificationCode!,
                newPassword: newPassword,
              ),
            );
      }
    }
  }

  Widget _buildTitle(BuildContext context) {
    return _NewPasswordTitle(l10n: context.l10n);
  }

  Widget _buildSubtitle(BuildContext context) {
    return _NewPasswordSubtitle(l10n: context.l10n);
  }

  Widget _buildNewPasswordField(BuildContext context) {
    return _NewPasswordField(
      controller: _newPasswordController,
      focusNode: _newPasswordFocusNode,
      obscureText: _obscurePasswords,
      validator: _validateNewPassword,
      onToggleVisibility: _togglePasswordVisibility,
      onSubmitted: (_) {
        _confirmPasswordFocusNode.requestFocus();
      },
    );
  }

  Widget _buildConfirmPasswordField(BuildContext context) {
    return _ConfirmPasswordField(
      controller: _confirmPasswordController,
      focusNode: _confirmPasswordFocusNode,
      obscureText: _obscurePasswords,
      validator: _validateConfirmPassword,
      onToggleVisibility: _togglePasswordVisibility,
      onSubmitted: (_) => _handleContinue(),
    );
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
        if (state is AuthError) {
          ErrorSnackBar.show(context, state.message);
        } else if (state is PasswordChangedSuccess) {
          // Navigate to sign-in screen after successful password change
          context.go('/sign-in-selection');
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
                child: SingleChildScrollView(
                  padding: AppDimensions.paddingHorizontal24,
                  child: Form(
                    key: _formKey,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        AppDimensions.sizedBox40,
                        _buildTitle(context),
                        AppDimensions.sizedBox16,
                        _buildSubtitle(context),
                        AppDimensions.sizedBox32,
                        _buildNewPasswordField(context),
                        AppDimensions.sizedBox24,
                        _buildConfirmPasswordField(context),
                        AppDimensions.sizedBox32,
                        _buildContinueButton(context),
                        AppDimensions.sizedBox24,
                      ],
                    ),
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

// Stateless Widgets

class _NewPasswordTitle extends StatelessWidget {
  final AppLocalizations l10n;

  const _NewPasswordTitle({required this.l10n});

  @override
  Widget build(BuildContext context) {
    return Text(
      l10n.setNewPassword,
      style: AppTextStyles.screenTitle(context),
      textAlign: TextAlign.left,
    );
  }
}

class _NewPasswordSubtitle extends StatelessWidget {
  final AppLocalizations l10n;

  const _NewPasswordSubtitle({required this.l10n});

  @override
  Widget build(BuildContext context) {
    return Text(
      l10n.enterNewPassword,
      style: AppTextStyles.buttonSubtitle(context, color: AppTheme.textDark),
      textAlign: TextAlign.left,
    );
  }
}

class _NewPasswordField extends StatelessWidget {
  final TextEditingController controller;
  final FocusNode focusNode;
  final bool obscureText;
  final String? Function(String?)? validator;
  final VoidCallback onToggleVisibility;
  final void Function(String)? onSubmitted;

  const _NewPasswordField({
    required this.controller,
    required this.focusNode,
    required this.obscureText,
    required this.validator,
    required this.onToggleVisibility,
    required this.onSubmitted,
  });

  @override
  Widget build(BuildContext context) {
    return AppTextField(
      label: context.l10n.newPassword,
      hint: context.l10n.enterNewPasswordHint,
      controller: controller,
      focusNode: focusNode,
      obscureText: obscureText,
      keyboardType: TextInputType.text,
      textInputAction: TextInputAction.next,
      validator: validator,
      onSubmitted: onSubmitted,
      suffixIcon: IconButton(
        icon: Icon(
          obscureText ? Icons.visibility_off : Icons.visibility,
          color: AppTheme.textDark,
        ),
        onPressed: onToggleVisibility,
      ),
      inputFormatters: [
        LengthLimitingTextInputFormatter(PasswordRules.maxLength),
      ],
    );
  }
}

class _ConfirmPasswordField extends StatelessWidget {
  final TextEditingController controller;
  final FocusNode focusNode;
  final bool obscureText;
  final String? Function(String?)? validator;
  final VoidCallback onToggleVisibility;
  final void Function(String)? onSubmitted;

  const _ConfirmPasswordField({
    required this.controller,
    required this.focusNode,
    required this.obscureText,
    required this.validator,
    required this.onToggleVisibility,
    required this.onSubmitted,
  });

  @override
  Widget build(BuildContext context) {
    return AppTextField(
      label: context.l10n.confirmPassword,
      hint: context.l10n.confirmPasswordHint,
      controller: controller,
      focusNode: focusNode,
      obscureText: obscureText,
      keyboardType: TextInputType.text,
      textInputAction: TextInputAction.done,
      validator: validator,
      onSubmitted: onSubmitted,
      suffixIcon: IconButton(
        icon: Icon(
          obscureText ? Icons.visibility_off : Icons.visibility,
          color: AppTheme.textDark,
        ),
        onPressed: onToggleVisibility,
      ),
      inputFormatters: [
        LengthLimitingTextInputFormatter(PasswordRules.maxLength),
      ],
    );
  }
}

