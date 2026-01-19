import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../data/services/sima_service.dart';
import '../../../../data/models/sima_response.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

class FinCodeScreen extends StatefulWidget {
  final String? phoneNumber;

  const FinCodeScreen({
    super.key,
    this.phoneNumber,
  });

  @override
  State<FinCodeScreen> createState() => _FinCodeScreenState();
}

class _FinCodeScreenState extends State<FinCodeScreen> {
  final _finController = TextEditingController();
  final _finFocusNode = FocusNode();
  final _isButtonEnabled = ValueNotifier<bool>(false);
  final _simaService = GetIt.instance<SimaService>();
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _finController.addListener(_onFinChanged);
    // Request focus after the first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _finFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _finController.removeListener(_onFinChanged);
    _finController.dispose();
    _finFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
  }

  void _onFinChanged() {
    final fin = _finController.text.trim();
    _isButtonEnabled.value = fin.isNotEmpty && !_isLoading;
  }

  Future<void> _handleContinue() async {
    final fin = _finController.text.trim();
    if (fin.isEmpty) {
      return;
    }

    // Validate FIN code format (typically 7 alphanumeric characters)
    if (fin.length < 7) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('FIN code must be at least 7 characters'),
          backgroundColor: AppTheme.red,
        ),
      );
      return;
    }

    setState(() {
      _isLoading = true;
      _isButtonEnabled.value = false;
    });

    try {
      // Check if SIMA is installed
      final isInstalled = await _simaService.isSimaInstalled();
      if (!isInstalled) {
        // Show dialog to install SIMA
        final shouldInstall = await showDialog<bool>(
          context: context,
          builder: (context) => AlertDialog(
            title: const Text('SIMA Required'),
            content: const Text(
              'SIMA app is required for identity verification. Would you like to install it from Play Store?',
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(false),
                child: const Text('Cancel'),
              ),
              TextButton(
                onPressed: () => Navigator.of(context).pop(true),
                child: const Text('Install'),
              ),
            ],
          ),
        );

        if (shouldInstall == true) {
          await _simaService.openPlayStore();
        }
        setState(() {
          _isLoading = false;
          _isButtonEnabled.value = fin.isNotEmpty;
        });
        return;
      }

      // Generate challenge
      final challenge = _simaService.generateChallenge();

      // Sign challenge using SIMA
      // This will open SIMA app and wait for the result
      debugPrint('=== FIN Code Screen: Calling signChallenge ===');
      debugPrint('FIN Code: $fin');
      debugPrint('Challenge length: ${challenge.length}');
      
      final response = await _simaService.signChallenge(
        challenge: challenge,
        userFinCode: fin,
      );

      debugPrint('=== FIN Code Screen: Response Received ===');
      debugPrint('Response status: ${response.status}');
      debugPrint('Response isSuccess: ${response.isSuccess}');
      debugPrint('Response message: ${response.message}');
      debugPrint('Response signatureBytes: ${response.signatureBytes != null ? "${response.signatureBytes!.length} bytes" : "NULL"}');
      debugPrint('Response certificateBytes: ${response.certificateBytes != null ? "${response.certificateBytes!.length} bytes" : "NULL"}');
      debugPrint('=== FIN Code Screen: Response End ===');

      if (response.isSuccess) {
        // Challenge signed successfully
        // If phone number is provided, this is part of SIMA sign-in flow
        if (widget.phoneNumber != null && widget.phoneNumber!.isNotEmpty) {
          // Check if we have valid certificate and signature bytes
          if (response.certificateBytes != null && response.certificateBytes!.isNotEmpty) {
            debugPrint('=== FIN Code Screen: SIMA Success - Dispatching Auth Event ===');
            debugPrint('Phone Number: ${widget.phoneNumber}');
            debugPrint('Certificate bytes: ${response.certificateBytes!.length}');
            debugPrint('Signature bytes: ${response.signatureBytes?.length ?? 0}');
            
            // Dispatch SIMA authentication event - this will navigate to PIN setup
            context.read<AuthBloc>().add(
                  SimaAuthenticateEvent(
                    phoneNumber: widget.phoneNumber!,
                    finCode: fin,
                    signatureBytes: response.signatureBytes,
                    certificateBytes: response.certificateBytes,
                  ),
                );
            debugPrint('=== FIN Code Screen: Auth Event Dispatched, waiting for state ===');
            // Don't reset loading state here - let the BlocListener handle navigation
            // The loading will be cleared when PinSetupRequired state is emitted
            return;
          } else {
            // SIMA response successful but missing certificate
            if (mounted) {
              setState(() {
                _isLoading = false;
                _isButtonEnabled.value = _finController.text.trim().isNotEmpty;
              });
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('SIMA verification succeeded but certificate is missing'),
                  backgroundColor: AppTheme.red,
                ),
              );
            }
            return;
          }
        } else {
          // Original flow - just show success message
          if (mounted) {
            setState(() {
              _isLoading = false;
              _isButtonEnabled.value = _finController.text.trim().isNotEmpty;
            });
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(
                  Platform.isAndroid
                      ? 'Identity verified. Please ensure you entered the correct FIN code.'
                      : 'Identity verified successfully with SIMA',
                ),
                backgroundColor: Colors.green,
                duration: const Duration(seconds: 4),
              ),
            );
          }
        }
      } else {
        // Handle error
        final errorMessage = response.message ?? 'Unknown error';
        final translatedMessage = SimaError.getErrorMessage(errorMessage);
        
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('SIMA Error: $translatedMessage'),
              backgroundColor: AppTheme.red,
              duration: const Duration(seconds: 5),
            ),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error opening SIMA: $e'),
            backgroundColor: AppTheme.red,
            duration: const Duration(seconds: 5),
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
          _isButtonEnabled.value = _finController.text.trim().isNotEmpty;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthError) {
          // Clear loading state on error
          if (mounted) {
            setState(() {
              _isLoading = false;
              _isButtonEnabled.value = _finController.text.trim().isNotEmpty;
            });
          }
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        } else if (state is PinSetupRequired) {
          debugPrint('=== FIN Code Screen: PinSetupRequired state received ===');
          debugPrint('Username: ${state.username}');
          debugPrint('Navigating to PIN setup...');
          
          // Clear loading state before navigation
          if (mounted) {
            setState(() {
              _isLoading = false;
            });
          }
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
          debugPrint('=== FIN Code Screen: Navigation to PIN setup completed ===');
        } else if (state is AuthAuthenticated) {
          // Clear loading state before navigation
          if (mounted) {
            setState(() {
              _isLoading = false;
            });
          }
          // Navigate to home after successful authentication
          context.go('/home');
        }
      },
      child: Scaffold(
      backgroundColor: AppTheme.white,
      resizeToAvoidBottomInset: true,
      body: SafeArea(
        child: Column(
          children: [
            const BackButtonWidget(),
            Expanded(
              child: SingleChildScrollView(
                padding: EdgeInsets.symmetric(horizontal: 24.w),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(height: 20.h),
                    // Title - H2/Medium style
                    Text(
                      'Identity Verification',
                      style: AppTextStyles.screenTitle(context),
                      textAlign: TextAlign.center,
                    ),
                    SizedBox(height: 8.h),
                    // Subtitle
                    Text(
                      'Verify your identity securely with SIMA',
                      style: AppTextStyles.buttonSubtitle(
                        context,
                        color: AppTheme.textSecondary,
                      ),
                      textAlign: TextAlign.center,
                    ),
                    SizedBox(height: 40.h),
                    // FIN code input field
                    TextField(
                      controller: _finController,
                      focusNode: _finFocusNode,
                      decoration: InputDecoration(
                        hintText: 'FIN code',
                        hintStyle: AppTextStyles.inputHint(context),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(12.r),
                          borderSide: BorderSide(
                            color: AppTheme.borderLight,
                            width: 1,
                          ),
                        ),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(12.r),
                          borderSide: BorderSide(
                            color: AppTheme.borderLight,
                            width: 1,
                          ),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(12.r),
                          borderSide: BorderSide(
                            color: AppTheme.mainColor,
                            width: 2,
                          ),
                        ),
                        filled: true,
                        fillColor: AppTheme.white,
                        contentPadding: EdgeInsets.symmetric(
                          horizontal: 16.w,
                          vertical: 16.h,
                        ),
                      ),
                      style: AppTextStyles.inputText(context),
                      keyboardType: TextInputType.text,
                      textCapitalization: TextCapitalization.characters,
                    ),
                  ],
                ),
              ),
            ),
            // Continue button at the bottom
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 24.w, vertical: 24.h),
              child: ValueListenableBuilder<bool>(
                valueListenable: _isButtonEnabled,
                builder: (context, isEnabled, _) {
                  return PrimaryActionButton(
                    text: _isLoading ? 'Verifying...' : 'Continue',
                    onPressed: (isEnabled && !_isLoading) ? _handleContinue : null,
                    isEnabled: isEnabled && !_isLoading,
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
}

