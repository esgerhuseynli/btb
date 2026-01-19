import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/constants/app_constants.dart';
import '../../../core/utils/validators.dart' as validators;
import '../../../core/utils/app_utils.dart';
import '../../core/widgets/app_button.dart';
import '../../core/widgets/app_text_field.dart';
import '../../core/widgets/app_app_bar.dart';
import '../../core/widgets/phone_text_field.dart';
import '../../core/widgets/back_button_widget.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SignUpByNumberScreen extends StatefulWidget {
  final int signUpType;
  final String verifyCode;
  final String? phone;

  const SignUpByNumberScreen({
    super.key,
    required this.signUpType,
    required this.verifyCode,
    this.phone,
  });

  @override
  State<SignUpByNumberScreen> createState() => _SignUpByNumberScreenState();
}

class _SignUpByNumberScreenState extends State<SignUpByNumberScreen> {
  final _formKey = GlobalKey<FormState>();
  final _phoneController = TextEditingController();
  final _passwordController = TextEditingController();
  final _passwordRepeatController = TextEditingController();
  bool _obscurePassword = true;
  bool _obscurePasswordRepeat = true;

  @override
  void initState() {
    super.initState();
    if (widget.phone != null) {
      // Remove +994 prefix for display
      final phoneWithoutPrefix = widget.phone!.replaceAll('+994', '').trim();
      _phoneController.text = phoneWithoutPrefix;
    }
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _passwordController.dispose();
    _passwordRepeatController.dispose();
    super.dispose();
  }

  bool _isFormValid() {
    final phone = _phoneController.text.trim();
    final password = _passwordController.text;
    final passwordRepeat = _passwordRepeatController.text;

    if (phone.length != 9) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Mobil nömrə düzgün deyil')),
      );
      return false;
    }

    if (password.length < 6) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Şifrə ən azı 6 simvol olmalıdır')),
      );
      return false;
    }

    if (password != passwordRepeat) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Şifrələr uyğun gəlmir')),
      );
      return false;
    }

    return true;
  }

  void _handleSignUp() {
    if (!(_formKey.currentState?.validate() ?? false) || !_isFormValid()) {
      return;
    }

    final phoneNumber = AppUtils.normalizePhoneNumber(_phoneController.text);
    final password = _passwordController.text;

    context.read<AuthBloc>().add(
          SignUpEvent(
            usernameType: AppConstants.signInUpTypeNumber,
            signUpType: widget.signUpType,
            verificationCode: widget.verifyCode,
            phoneNumber: phoneNumber,
            password: password,
            pan: widget.signUpType == AppConstants.signUpTypePan
                ? '' // Will be set from AppData in Android
                : null,
            customerNumber: widget.signUpType == AppConstants.signUpTypeCif
                ? '' // Will be set from AppData in Android
                : null,
            customerBirthdate: widget.signUpType == AppConstants.signUpTypeCif
                ? '' // Will be set from AppData in Android
                : null,
          ),
        );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.mainBackground,
      body: SafeArea(
        child: Column(
          children: [
            const BackButtonWidget(),
            Expanded(
              child: BlocListener<AuthBloc, AuthState>(
                listener: (context, state) {
            if (state is AuthError) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text(state.message)),
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
                        'Mobil nömrə və şifrə daxil edin',
                        style: Theme.of(context).textTheme.titleLarge,
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 32),
                      AbsorbPointer(
                        absorbing: widget.phone != null,
                        child: PhoneTextField(
                          controller: _phoneController,
                          label: 'Mobil nömrə',
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Mobil nömrə daxil edin';
                            }
                            final cleaned = value.replaceAll(RegExp(r'\D'), '');
                            if (cleaned.length != 9) {
                              return 'Mobil nömrə düzgün deyil';
                            }
                            return null;
                          },
                        ),
                      ),
                      const SizedBox(height: 16),
                      AppTextField(
                        controller: _passwordController,
                        label: 'Şifrə',
                        obscureText: _obscurePassword,
                        validator: validators.Validators.validatePassword,
                        suffixIcon: IconButton(
                          icon: Icon(
                            _obscurePassword
                                ? Icons.visibility_off
                                : Icons.visibility,
                          ),
                          onPressed: () {
                            setState(() {
                              _obscurePassword = !_obscurePassword;
                            });
                          },
                        ),
                        inputFormatters: [
                          FilteringTextInputFormatter.deny(RegExp(r'\s')),
                        ],
                      ),
                      const SizedBox(height: 16),
                      AppTextField(
                        controller: _passwordRepeatController,
                        label: 'Şifrəni təkrarlayın',
                        obscureText: _obscurePasswordRepeat,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Şifrəni təkrarlayın';
                          }
                          if (value != _passwordController.text) {
                            return 'Şifrələr uyğun gəlmir';
                          }
                          return null;
                        },
                        suffixIcon: IconButton(
                          icon: Icon(
                            _obscurePasswordRepeat
                                ? Icons.visibility_off
                                : Icons.visibility,
                          ),
                          onPressed: () {
                            setState(() {
                              _obscurePasswordRepeat = !_obscurePasswordRepeat;
                            });
                          },
                        ),
                        inputFormatters: [
                          FilteringTextInputFormatter.deny(RegExp(r'\s')),
                        ],
                      ),
                      const SizedBox(height: 32),
                      AppButton(
                        text: 'Davam et',
                        onPressed: isLoading ? null : _handleSignUp,
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
          ],
        ),
      ),
    );
  }
}

