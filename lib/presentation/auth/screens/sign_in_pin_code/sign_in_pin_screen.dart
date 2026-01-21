import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:local_auth/local_auth.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/constants/app_constants.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../core/widgets/app_app_bar.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

class SignInPinScreen extends StatefulWidget {
  const SignInPinScreen({super.key});

  @override
  State<SignInPinScreen> createState() => _SignInPinScreenState();
}

class _SignInPinScreenState extends State<SignInPinScreen> {
  String _currentPin = '';
  bool _isLoading = false;
  bool _showError = false;
  final LocalAuthentication _localAuth = LocalAuthentication();

  void _onNumberPressed(String number) {
    if (_isLoading) return;

    setState(() {
      _showError = false;
      _currentPin += number;
      if (_currentPin.length == 4) {
        // PIN entry complete - verify
        _isLoading = true;
        context.read<AuthBloc>().add(VerifyPinEvent(pin: _currentPin));
      }
    });
  }

  void _onDeletePressed() {
    if (_isLoading) return;

    setState(() {
      if (_currentPin.isNotEmpty) {
        _currentPin = _currentPin.substring(0, _currentPin.length - 1);
        _showError = false;
      }
    });
  }

  void _onClearPressed() {
    if (_isLoading) return;
    setState(() {
      _currentPin = '';
      _showError = false;
    });
  }

  Future<void> _onBiometricPressed() async {
    if (_isLoading) return;

    try {
      // Check if device supports biometric authentication
      final bool isAvailable = await _localAuth.canCheckBiometrics ||
          await _localAuth.isDeviceSupported();
      
      if (!isAvailable) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Biometric authentication is not available on this device'),
              backgroundColor: AppTheme.red,
            ),
          );
        }
        return;
      }

      // Check available biometrics
      final List<BiometricType> availableBiometrics =
          await _localAuth.getAvailableBiometrics();

      if (availableBiometrics.isEmpty) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('No biometric authentication methods are enrolled'),
              backgroundColor: AppTheme.red,
            ),
          );
        }
        return;
      }

      // For iOS, check if Face ID is available
      final bool isFaceIdAvailable = Platform.isIOS &&
          availableBiometrics.contains(BiometricType.face);

      // Authenticate with biometrics
      final bool didAuthenticate = await _localAuth.authenticate(
        localizedReason: isFaceIdAvailable
            ? 'Use Face ID to sign in'
            : 'Use biometric authentication to sign in',
        options: const AuthenticationOptions(
          biometricOnly: true,
          stickyAuth: true,
        ),
      );

      if (didAuthenticate && mounted) {
        // Biometric authentication successful - trigger sign in
        setState(() {
          _isLoading = true;
          _showError = false;
        });
        context.read<AuthBloc>().add(const VerifyBiometricEvent());
      } else if (mounted) {
        // User cancelled or authentication failed
        setState(() {
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Biometric authentication failed: ${e.toString()}'),
            backgroundColor: AppTheme.red,
          ),
        );
      }
    }
  }

  void _onLogoutPressed() {
    if (_isLoading) return;
    // Navigate back to phone entry screen
    context.go('/phone-entry');
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthAuthenticated) {
          // Navigate to home
          context.go('/home');
        } else if (state is AuthError) {
          setState(() {
            _isLoading = false;
            _showError = true;
            _currentPin = '';
          });
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        } else if (state is AuthLoading) {
          setState(() {
            _isLoading = true;
          });
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.white,
        body: SafeArea(
          child: Column(
            children: [
              const BackButtonWidget(),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      // const SizedBox(height: 20),
                      // Customer name (if available)
                      FutureBuilder<String?>(
                future: _getCustomerName(),
                builder: (context, snapshot) {
                  if (snapshot.hasData && snapshot.data != null && snapshot.data!.isNotEmpty) {
                    return Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 24.0),
                      child: Text(
                        snapshot.data!,
                        style: Theme.of(context).textTheme.titleLarge?.copyWith(
                              color: AppTheme.textColor,
                              fontWeight: FontWeight.bold,
                            ),
                        textAlign: TextAlign.center,
                      ),
                    );
                  }
                      return const SizedBox.shrink();
                    },
                  ),
                      const SizedBox(height: 40),
                      // PIN dots
                      Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(4, (index) {
                  return Container(
                    width: 16,
                    height: 16,
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: index < _currentPin.length
                          ? AppTheme.mainColor
                          : AppTheme.borderColor,
                        ),
                      );
                    }),
                      ),
                      if (_showError) ...[
                        const SizedBox(height: 16),
                        Text(
                          'Yanlış PIN kodu',
                          style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                                color: AppTheme.red,
                              ),
                        ),
                      ],
                      const SizedBox(height: 60),
                      // Number pad
                      Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _buildNumberButton('1'),
                        _buildNumberButton('2'),
                        _buildNumberButton('3'),
                      ],
                    ),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _buildNumberButton('4'),
                        _buildNumberButton('5'),
                        _buildNumberButton('6'),
                      ],
                    ),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _buildNumberButton('7'),
                        _buildNumberButton('8'),
                        _buildNumberButton('9'),
                      ],
                    ),
                    const SizedBox(height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _buildEmptyButton(),
                        _buildNumberButton('0'),
                        _buildBiometricButton(),
                      ],
                    ),
                      ],
                    ),
                      ),
                      if (_isLoading)
                        const Padding(
                          padding: EdgeInsets.all(16.0),
                          child: CircularProgressIndicator(),
                        ),
                      const SizedBox(height: 20),
                      // Bottom Actions
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          TextButton(
                            onPressed: _onLogoutPressed,
                            child: Text(
                              'Log out',
                              style: AppTextStyles.caption(context, color: AppTheme.textDark),
                            ),
                          ),
                          TextButton(
                            onPressed: _onDeletePressed,
                            child: Text(
                              'Delete',
                              style: AppTextStyles.caption(context, color: AppTheme.textDark),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 20),
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

  Future<String?> _getCustomerName() async {
    try {
      const storage = FlutterSecureStorage();
      return await storage.read(key: AppConstants.customerName);
    } catch (e) {
      return null;
    }
  }

  Widget _buildNumberButton(String number) {
    return InkWell(
      onTap: () => _onNumberPressed(number),
      borderRadius: BorderRadius.circular(40),
      child: Container(
        width: 80,
        height: 80,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: AppTheme.borderColor,
        ),
        child: Center(
          child: Text(
            number,
            style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                  color: AppTheme.textColor,
                  fontWeight: FontWeight.bold,
                ),
          ),
        ),
      ),
    );
  }

  Widget _buildBiometricButton() {
    return InkWell(
      onTap: _onBiometricPressed,
      borderRadius: BorderRadius.circular(40),
      child: Container(
        width: 80,
        height: 80,
        decoration: const BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.transparent,
        ),
        child: Center(
          child: SvgPicture.asset(
            'assets/icons/face-id.svg',
            width: 64,
            height: 64,
            colorFilter: ColorFilter.mode(
              AppTheme.mainColor,
              BlendMode.srcIn,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildEmptyButton() {
    return Container(
      width: 80,
      height: 80,
    );
  }
}

