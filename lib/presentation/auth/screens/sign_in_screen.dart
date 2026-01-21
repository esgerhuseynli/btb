import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/utils/validators.dart';
import '../../../core/widgets/error_snackbar.dart';
import '../../core/widgets/app_button.dart';
import '../../core/widgets/app_text_field.dart';
import '../../core/widgets/app_app_bar.dart';
import '../../core/widgets/back_button_widget.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SignInScreen extends StatefulWidget {
  final String? phone;
  final String? email;

  const SignInScreen({super.key, this.phone, this.email});

  @override
  State<SignInScreen> createState() => _SignInScreenState();
}

class _SignInScreenState extends State<SignInScreen> {
  final _formKey = GlobalKey<FormState>();
  final _phoneController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _obscurePassword = true;
  bool _isPhoneLogin = true; // Default to phone login

  @override
  void initState() {
    super.initState();
    if (widget.phone != null) {
      _phoneController.text = widget.phone!.replaceAll('+994', '').trim();
      _isPhoneLogin = true;
    } else if (widget.email != null) {
      _emailController.text = widget.email!;
      _isPhoneLogin = false;
    }
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _handleSignIn() {
    if (_formKey.currentState?.validate() ?? false) {
      String username;
      if (_isPhoneLogin) {
        // Format phone number: remove all non-digits, ensure it's 9 digits
        final phoneDigits =
            _phoneController.text.trim().replaceAll(RegExp(r'\D'), '');
        if (phoneDigits.length == 9) {
          username = phoneDigits;
        } else if (phoneDigits.length == 12 && phoneDigits.startsWith('994')) {
          username = phoneDigits.substring(3);
        } else {
          username = phoneDigits;
        }
      } else {
        username = _emailController.text.trim();
      }

      context.read<AuthBloc>().add(
            SignInEvent(
              username: username,
              password: _passwordController.text,
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
        } else if (state is OtpSent) {
          // Navigate to OTP verification screen when OTP is sent
          context.push(
            '/otp-verification?phoneNumber=${state.phoneNumber}&flowType=regularSignIn',
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
        } else if (state is DeviceNeedsRegistrationDialog) {
          // This state is handled in the dialog callback
        } else if (state is AuthError) {
          ErrorSnackBar.show(context, state.message);
        }
      },
      child: Scaffold(
        body: SafeArea(
          child: Column(
            children: [
              const BackButtonWidget(),
              Expanded(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(24.0),
                  child: Form(
                    key: _formKey,
                    child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  const SizedBox(height: 32),
                  // Login type selector
                  Row(
                    children: [
                      Expanded(
                        child: _buildLoginTypeButton(
                          'Mobil nömrə',
                          Icons.phone,
                          true,
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: _buildLoginTypeButton(
                          'Email',
                          Icons.email,
                          false,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 24),
                  // Phone or Email field
                  if (_isPhoneLogin)
                    TextFormField(
                      controller: _phoneController,
                      keyboardType: TextInputType.phone,
                      validator: Validators.validatePhoneNumber,
                      inputFormatters: [
                        FilteringTextInputFormatter.digitsOnly,
                        LengthLimitingTextInputFormatter(9),
                      ],
                      decoration: InputDecoration(
                        labelText: 'Mobil nömrə',
                        hintText: 'XX XXX XX XX',
                        prefixIcon: const Icon(
                          Icons.phone,
                          color: AppTheme.hintColor,
                        ),
                        prefixText: '+994 ',
                        prefixStyle: TextStyle(
                          color: AppTheme.textColor,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    )
                  else
                    AppTextField(
                      label: 'Email',
                      hint: 'Email ünvanınızı daxil edin',
                      controller: _emailController,
                      keyboardType: TextInputType.emailAddress,
                      validator: Validators.validateEmail,
                      prefixIcon: const Icon(
                        Icons.email,
                        color: AppTheme.hintColor,
                      ),
                    ),
                  const SizedBox(height: 24),
                  AppTextField(
                    label: 'Şifrə',
                    hint: 'Şifrənizi daxil edin',
                    controller: _passwordController,
                    obscureText: _obscurePassword,
                    validator: Validators.validatePassword,
                    prefixIcon: const Icon(
                      Icons.lock_outline,
                      color: AppTheme.hintColor,
                    ),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword
                            ? Icons.visibility_outlined
                            : Icons.visibility_off_outlined,
                        color: AppTheme.hintColor,
                      ),
                      onPressed: () {
                        setState(() {
                          _obscurePassword = !_obscurePassword;
                        });
                      },
                    ),
                  ),
                  const SizedBox(height: 16),
                  Align(
                    alignment: Alignment.centerRight,
                    child: TextButton(
                      onPressed: () {
                        final phone = widget.phone ?? _phoneController.text.trim();
                        if (phone.isNotEmpty) {
                          context.push('/forgot-password?phone=$phone');
                        } else {
                          context.push('/forgot-password');
                        }
                      },
                      child: const Text('Şifrəni unutmusunuz?'),
                    ),
                  ),
                  const SizedBox(height: 32),
                  BlocBuilder<AuthBloc, AuthState>(
                    builder: (context, state) {
                      return AppButton(
                        text: 'Daxil ol',
                        onPressed: state is AuthLoading ? null : _handleSignIn,
                        isLoading: state is AuthLoading,
                      );
                    },
                  ),
                  const SizedBox(height: 24),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text('Hesabınız yoxdur? '),
                      TextButton(
                        onPressed: () {
                          context.push('/sign-up-types');
                        },
                        child: const Text('Qeydiyyatdan keç'),
                      ),
                    ],
                  ),
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

  Widget _buildLoginTypeButton(String label, IconData icon, bool isPhone) {
    final isSelected = _isPhoneLogin == isPhone;
    return InkWell(
      onTap: () {
        setState(() {
          _isPhoneLogin = isPhone;
        });
      },
      borderRadius: BorderRadius.circular(8),
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
        decoration: BoxDecoration(
          color:
              isSelected ? AppTheme.mainColor.withOpacity(0.1) : AppTheme.white,
          border: Border.all(
            color: isSelected ? AppTheme.mainColor : AppTheme.borderColor,
            width: isSelected ? 2 : 1,
          ),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              color: isSelected ? AppTheme.mainColor : AppTheme.hintColor,
              size: 20,
            ),
            const SizedBox(width: 8),
            Text(
              label,
              style: TextStyle(
                color: isSelected ? AppTheme.mainColor : AppTheme.textColor,
                fontWeight: isSelected ? FontWeight.w600 : FontWeight.normal,
                fontSize: 14,
              ),
            ),
          ],
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
              AppButton(
                text: 'OK',
                onPressed: () {
                  Navigator.of(dialogContext).pop();
                  // Don't navigate anywhere - just close the dialog
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
