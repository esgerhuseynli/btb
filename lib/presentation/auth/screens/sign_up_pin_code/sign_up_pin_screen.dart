import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/svg.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:go_router/go_router.dart';
import '../../../../core/constants/app_constants.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/utils/app_utils.dart';

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
  final _secureStorage = const FlutterSecureStorage();
  bool _isSaving = false;

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

  Future<void> _handlePinSuccess() async {
    if (_isSaving) return;
    
    setState(() {
      _isSaving = true;
    });

    try {
      // Hash the PIN using the same method as the app
      final pinHash = AppUtils.passwordHash(pin);
      
      // Save PIN hash to secure storage
      await _secureStorage.write(
        key: AppConstants.pinHash,
        value: pinHash,
      );
      
      // Ensure username, passwordHash, and signInType are saved
      // (they should already be saved from login, but ensure they're there)
      if (widget.username.isNotEmpty) {
        await _secureStorage.write(
          key: AppConstants.username,
          value: widget.username,
        );
      }
      
      // IMPORTANT: Do NOT overwrite passwordHash here
      // The original password hash (from user input) was already saved during sign-in
      // widget.passwordHash is the NEW hash from ChangeKeystore, which should NOT be saved
      // We keep the original password hash for PIN verification
      // Only save if passwordHash is not already in storage (for edge cases)
      final existingPasswordHash = await _secureStorage.read(key: AppConstants.passwordHash);
      if (existingPasswordHash == null || existingPasswordHash.isEmpty) {
        // Only save if storage is empty (shouldn't happen in normal flow)
        // In this case, use the original hash if available, not the new one from ChangeKeystore
        if (widget.passwordHash.isNotEmpty) {
          // This is a fallback - but ideally we should have the original hash already saved
          await _secureStorage.write(
            key: AppConstants.passwordHash,
            value: widget.passwordHash,
          );
        }
      }
      // Otherwise, keep the existing password hash (original from user input)
      
      await _secureStorage.write(
        key: AppConstants.signInType,
        value: widget.signInType.toString(),
      );
      
      // Ensure hasActiveSession is set
      await _secureStorage.write(
        key: AppConstants.hasActiveSession,
        value: 'true',
      );
      
      // Navigate directly to home - no API calls needed
      if (mounted) {
        context.go('/home');
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error saving PIN: $e'),
            backgroundColor: AppTheme.red,
          ),
        );
        setState(() {
          _isSaving = false;
        });
      }
    }
  }

  void _showErrorAndReset() {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('PIN kodlar uyğun gəlmir. Yenidən cəhd edin.'),
        backgroundColor: AppTheme.red,
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
        backgroundColor: AppTheme.white,
        body: SafeArea(
        child: Padding(
          padding: EdgeInsets.all(24.w),
          child: Column(
            children: [
              SizedBox(height: 40.h),

              // Title
              Text(
                isConfirmingPin ? 'Re-enter PIN code' : 'Set a PIN code',
                style: AppTextStyles.screenTitle(context),
                textAlign: TextAlign.center,
              ),

              SizedBox(height: 24.h),

              // PIN Indicators
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(
                  pinLength,
                      (index) => Container(
                    margin: EdgeInsets.symmetric(horizontal: 8.w),
                    width: 16.w,
                    height: 16.w,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      border: Border.all(
                        color: AppTheme.textDark,
                        width: 2,
                      ),
                      color: index < currentPin.length
                          ? AppTheme.textDark
                          : Colors.transparent,
                    ),
                  ),
                ),
              ),

              SizedBox(height: 60.h),

              // Number Pad
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(24.0),
                        child: Column(
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                              children: [
                                _buildNumberButton(context, '1'),
                                _buildNumberButton(context, '2'),
                                _buildNumberButton(context, '3'),
                              ],
                            ),
                            const SizedBox(height: 16),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                              children: [
                                _buildNumberButton(context, '4'),
                                _buildNumberButton(context, '5'),
                                _buildNumberButton(context, '6'),
                              ],
                            ),
                            const SizedBox(height: 16),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                              children: [
                                _buildNumberButton(context, '7'),
                                _buildNumberButton(context, '8'),
                                _buildNumberButton(context, '9'),
                              ],
                            ),
                            const SizedBox(height: 16),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                              children: [
                                _buildEmptyButton(),
                                _buildNumberButton(context, '0'),
                                _buildBiometricButton(),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),

              SizedBox(height: 20.h),

              // Bottom Actions
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  TextButton(
                    onPressed: onLogout,
                    child: Text(
                      'Log out',
                      style: AppTextStyles.caption(context, color: AppTheme.textDark),
                    ),
                  ),
                  TextButton(
                    onPressed: onDelete,
                    child: Text(
                      'Delete',
                      style: AppTextStyles.caption(context, color: AppTheme.textDark),
                    ),
                  ),
                ],
              ),

              SizedBox(height: 20.h),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNumberButton(BuildContext context, String number) {
    return GestureDetector(
      onTap: () => onNumberPressed(number),
      child: Container(
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: AppTheme.borderColor,
        ),
        child: Center(
          child: Text(
            number,
            style: AppTextStyles.display(context, color: AppTheme.textDark),
          ),
        ),
      ),
    );
  }

  Widget _buildEmptyButton() {
    return Container(
      decoration: const BoxDecoration(
        shape: BoxShape.circle,
        color: Colors.transparent,
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
            width: 64.w,
            height: 64.w,
            colorFilter: ColorFilter.mode(
              AppTheme.mainColor,
              BlendMode.srcIn,
            ),
          ),
        ),
      ),
    );
  }
}