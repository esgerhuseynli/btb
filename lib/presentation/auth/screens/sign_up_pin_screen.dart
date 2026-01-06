import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../../../core/theme/app_theme.dart';
import '../../core/widgets/app_app_bar.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SignUpPinScreen extends StatefulWidget {
  final String username;
  final String passwordHash;
  final int signInType;
  final bool isComingFromSignIn;

  const SignUpPinScreen({
    super.key,
    required this.username,
    required this.passwordHash,
    required this.signInType,
    required this.isComingFromSignIn,
  });

  @override
  State<SignUpPinScreen> createState() => _SignUpPinScreenState();
}

class _SignUpPinScreenState extends State<SignUpPinScreen> {
  String _pin1 = '';
  String _pin2 = '';
  String _currentPin = '';
  bool _isConfirming = false;
  bool _isLoading = false;

  void _onNumberPressed(String number) {
    if (_isLoading) return;

    setState(() {
      _currentPin += number;
      if (_currentPin.length == 4) {
        if (!_isConfirming) {
          // First PIN entry complete
          _pin1 = _currentPin;
          _currentPin = '';
          _isConfirming = true;
        } else {
          // Second PIN entry complete
          _pin2 = _currentPin;
          if (_pin1 == _pin2) {
            // PINs match - proceed with setup
            _isLoading = true;
            context.read<AuthBloc>().add(
                  SetupPinEvent(
                    pin: _pin2,
                    username: widget.username,
                    passwordHash: widget.passwordHash,
                    signInType: widget.signInType,
                    isComingFromSignIn: widget.isComingFromSignIn,
                  ),
                );
          } else {
            // PINs don't match - reset
            _showError('PIN kodları uyğun deyil');
            _resetPin();
          }
        }
      }
    });
  }

  void _onDeletePressed() {
    if (_isLoading) return;

    setState(() {
      if (_currentPin.isNotEmpty) {
        _currentPin = _currentPin.substring(0, _currentPin.length - 1);
      } else if (_isConfirming && _pin1.isNotEmpty) {
        _isConfirming = false;
        _currentPin = _pin1;
        _pin1 = '';
      }
    });
  }

  void _onClearPressed() {
    if (_isLoading) return;
    _resetPin();
  }

  void _resetPin() {
    setState(() {
      _pin1 = '';
      _pin2 = '';
      _currentPin = '';
      _isConfirming = false;
    });
  }

  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: AppTheme.red,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthAuthenticated) {
          // Navigate to home
          Navigator.of(context).pushReplacementNamed('/home');
        } else if (state is AuthError) {
          _isLoading = false;
          _showError(state.message);
          _resetPin();
        } else if (state is AuthLoading) {
          setState(() {
            _isLoading = true;
          });
        }
      },
      child: Scaffold(
        appBar: const AppAppBar(title: 'PIN qurulumu'),
        body: SafeArea(
          child: Column(
            children: [
              const SizedBox(height: 48),
              // Instruction text
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24.0),
                child: Text(
                  _isConfirming
                      ? 'PIN kodunu yenidən daxil edin'
                      : 'PIN kodunuzu daxil edin',
                  style: Theme.of(context).textTheme.titleLarge,
                  textAlign: TextAlign.center,
                ),
              ),
              const SizedBox(height: 48),
              // PIN indicators
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(4, (index) {
                  final isFilled = index < _currentPin.length;
                  return Container(
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    width: 16,
                    height: 16,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: isFilled ? AppTheme.mainColor : AppTheme.borderColor,
                    ),
                  );
                }),
              ),
              const Spacer(),
              // Number pad
              Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  children: [
                    // Row 1-3
                    for (int row = 0; row < 3; row++)
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          for (int col = 1; col <= 3; col++)
                            _buildNumberButton((row * 3 + col).toString()),
                        ],
                      ),
                    // Row 0 and X
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        _buildActionButton('X', _onClearPressed, Colors.grey),
                        _buildNumberButton('0'),
                        _buildActionButton('⌫', _onDeletePressed, Colors.grey),
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
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNumberButton(String number) {
    return _PinButton(
      label: number,
      onPressed: () => _onNumberPressed(number),
      enabled: !_isLoading,
    );
  }

  Widget _buildActionButton(String label, VoidCallback onPressed, Color color) {
    return _PinButton(
      label: label,
      onPressed: onPressed,
      enabled: !_isLoading,
      color: color,
    );
  }
}

class _PinButton extends StatelessWidget {
  final String label;
  final VoidCallback onPressed;
  final bool enabled;
  final Color? color;

  const _PinButton({
    required this.label,
    required this.onPressed,
    this.enabled = true,
    this.color,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: enabled ? onPressed : null,
      borderRadius: BorderRadius.circular(50),
      child: Container(
        width: 70,
        height: 70,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: enabled
              ? (color ?? AppTheme.mainColor.withOpacity(0.1))
              : Colors.grey.withOpacity(0.1),
          border: Border.all(
            color: enabled
                ? (color ?? AppTheme.mainColor)
                : Colors.grey,
            width: 2,
          ),
        ),
        child: Center(
          child: Text(
            label,
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: enabled
                  ? (color ?? AppTheme.mainColor)
                  : Colors.grey,
            ),
          ),
        ),
      ),
    );
  }
}








