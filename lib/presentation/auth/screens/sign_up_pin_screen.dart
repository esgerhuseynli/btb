import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:go_router/go_router.dart';

class SignUpPinScreen extends StatefulWidget {
  final String username;
  final String passwordHash;
  final int signInType;
  final bool isComingFromSignIn;

  const SignUpPinScreen({
    Key? key,
    required this.username,
    required this.passwordHash,
    required this.signInType,
    this.isComingFromSignIn = false,
  }) : super(key: key);

  @override
  State<SignUpPinScreen> createState() => _SignUpPinScreenState();
}

class _SignUpPinScreenState extends State<SignUpPinScreen> {
  String pin = '';
  String confirmPin = '';
  bool isConfirmingPin = false;
  final int pinLength = 4;

  void onNumberPressed(String number) {
    if (!isConfirmingPin) {
      // First PIN entry
      if (pin.length < pinLength) {
        setState(() {
          pin += number;
        });

        if (pin.length == pinLength) {
          // Move to confirmation
          setState(() {
            isConfirmingPin = true;
          });
        }
      }
    } else {
      // Confirming PIN
      if (confirmPin.length < pinLength) {
        setState(() {
          confirmPin += number;
        });

        if (confirmPin.length == pinLength) {
          // Check if PINs match
          if (pin == confirmPin) {
            // PINs match - proceed with registration
            _handlePinSuccess();
          } else {
            // PINs don't match - show error and reset
            _showErrorAndReset();
          }
        }
      }
    }
  }

  void _handlePinSuccess() {
    // TODO: Save PIN and complete registration
    print('PIN set successfully: $pin');
    print('Username: ${widget.username}');
    print('Password Hash: ${widget.passwordHash}');
    print('Sign In Type: ${widget.signInType}');

    // Navigate to home or next screen
    if (widget.isComingFromSignIn) {
      context.go('/home');
    } else {
      // Complete sign up process
      context.go('/home');
    }
  }

  void _showErrorAndReset() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(
        content: Text('PIN kodlar uyğun gəlmir. Yenidən cəhd edin.'),
        backgroundColor: Colors.red,
      ),
    );

    setState(() {
      pin = '';
      confirmPin = '';
      isConfirmingPin = false;
    });
  }

  void onDelete() {
    if (!isConfirmingPin) {
      if (pin.isNotEmpty) {
        setState(() {
          pin = pin.substring(0, pin.length - 1);
        });
      }
    } else {
      if (confirmPin.isNotEmpty) {
        setState(() {
          confirmPin = confirmPin.substring(0, confirmPin.length - 1);
        });
      } else {
        // Go back to first PIN entry
        setState(() {
          isConfirmingPin = false;
          pin = '';
        });
      }
    }
  }

  void onBiometric() {
    // Handle biometric authentication
    print('Biometric authentication requested');
    // TODO: Implement biometric setup
  }

  void onLogout() {
    // Go back to sign in
    context.go('/phone-entry');
  }

  @override
  Widget build(BuildContext context) {
    final currentPin = isConfirmingPin ? confirmPin : pin;

    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            children: [
              const SizedBox(height: 40),

              // Title
              Text(
                isConfirmingPin ? 'Re-enter PIN code' : 'Set a PIN code',
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.w600,
                  color: Colors.black,
                ),
              ),

              const SizedBox(height: 24),

              // PIN Indicators
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(
                  pinLength,
                      (index) => Container(
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    width: 16,
                    height: 16,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      border: Border.all(
                        color: Colors.black,
                        width: 2,
                      ),
                      color: index < currentPin.length
                          ? Colors.black
                          : Colors.transparent,
                    ),
                  ),
                ),
              ),

              const SizedBox(height: 60),

              // Number Pad
              Expanded(
                child: GridView.count(
                  crossAxisCount: 3,
                  childAspectRatio: 1,
                  mainAxisSpacing: 20,
                  crossAxisSpacing: 20,
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  children: [
                    _buildNumberButton('1'),
                    _buildNumberButton('2'),
                    _buildNumberButton('3'),
                    _buildNumberButton('4'),
                    _buildNumberButton('5'),
                    _buildNumberButton('6'),
                    _buildNumberButton('7'),
                    _buildNumberButton('8'),
                    _buildNumberButton('9'),
                    const SizedBox(), // Empty space
                    _buildNumberButton('0'),
                    _buildBiometricButton(),
                  ],
                ),
              ),

              const SizedBox(height: 20),

              // Bottom Actions
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  TextButton(
                    onPressed: onLogout,
                    child: const Text(
                      'Log out',
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.black87,
                      ),
                    ),
                  ),
                  TextButton(
                    onPressed: onDelete,
                    child: const Text(
                      'Delete',
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.black87,
                      ),
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNumberButton(String number) {
    return GestureDetector(
      onTap: () => onNumberPressed(number),
      child: Container(
        decoration: const BoxDecoration(
          shape: BoxShape.circle,
          color: Color(0xFFF5F5F5),
        ),
        child: Center(
          child: Text(
            number,
            style: const TextStyle(
              fontSize: 32,
              fontWeight: FontWeight.w600,
              color: Colors.black,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildBiometricButton() {
    return GestureDetector(
      onTap: onBiometric,
      child: Container(
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
              Colors.red.shade400,
              BlendMode.srcIn,
            ),
          ),
        ),
      ),
    );
  }
}

// Example usage in main.dart:
void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'PIN Code',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: const SignUpPinScreen(
        username: 'test_user',
        passwordHash: 'hash123',
        signInType: 1,
      ),
    );
  }
}