import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/theme/app_text_styles.dart';
import '../../core/widgets/primary_action_button.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_state.dart';

class PhoneNumberEntryScreen extends StatefulWidget {
  const PhoneNumberEntryScreen({super.key});

  @override
  State<PhoneNumberEntryScreen> createState() => _PhoneNumberEntryScreenState();
}

class _PhoneNumberEntryScreenState extends State<PhoneNumberEntryScreen> {
  final _formKey = GlobalKey<FormState>();
  final _phoneController = TextEditingController();
  final _phoneFocusNode = FocusNode();
  final _isButtonEnabled = ValueNotifier<bool>(false);

  @override
  void initState() {
    super.initState();
    _phoneController.addListener(_onPhoneChanged);
    // Request focus after the first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _phoneFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _phoneController.removeListener(_onPhoneChanged);
    _phoneController.dispose();
    _phoneFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
  }

  void _onPhoneChanged() {
    final cleaned = _phoneController.text.replaceAll(RegExp(r'\D'), '');
    _isButtonEnabled.value = cleaned.length == 9;
  }

  void _handleContinue() {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final phone = _phoneController.text.replaceAll(RegExp(r'\D'), '');
    // Navigate to sign-in selection screen with phone number
    context.go('/sign-in-selection?phone=$phone');
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthAuthenticated) {
          context.go('/home');
        } else if (state is PinVerificationRequired) {
          context.go('/sign-in-pin');
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        resizeToAvoidBottomInset: true,
        body: SafeArea(
          child: Form(
            key: _formKey,
            child: SingleChildScrollView(
              padding: EdgeInsets.symmetric(horizontal: 24.w),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(height: 60.h),
                  // Title - H2/Medium style
                  Text(
                    'Enter phone number',
                    style: AppTextStyles.screenTitle(context),
                    textAlign: TextAlign.center,
                  ),
                  SizedBox(height: 40.h),
                  // Phone number display with country code
                  Center(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        // Country code display
                        Text(
                          '+994 ',
                          style: AppTextStyles.phoneNumberDisplay(context),
                        ),
                        // Phone number input - inline with country code
                        IntrinsicWidth(
                          child: TextFormField(
                            controller: _phoneController,
                            focusNode: _phoneFocusNode,
                            keyboardType: TextInputType.number,
                            autofocus: true,
                            textAlign: TextAlign.left,
                            enableInteractiveSelection: true,
                            showCursor: true,
                            inputFormatters: [
                              FilteringTextInputFormatter.digitsOnly,
                              LengthLimitingTextInputFormatter(9),
                            ],
                            style: AppTextStyles.phoneNumberInput(context),
                            decoration: InputDecoration(
                              border: InputBorder.none,
                              enabledBorder: InputBorder.none,
                              focusedBorder: InputBorder.none,
                              errorBorder: InputBorder.none,
                              focusedErrorBorder: InputBorder.none,
                              contentPadding: EdgeInsets.symmetric(horizontal: 2.w),
                              isDense: true,
                            ),
                            validator: (value) {
                              if (value == null || value.isEmpty) {
                                return 'Telefon nömrəsi daxil edin';
                              }
                              final cleaned = value.replaceAll(RegExp(r'\D'), '');
                              if (cleaned.length != 9) {
                                return 'Telefon nömrəsi düzgün deyil';
                              }
                              return null;
                            },
                            onChanged: (value) {
                              // Format phone number as user types
                              final cleaned = value.replaceAll(RegExp(r'\D'), '');
                              if (cleaned.length <= 9) {
                                String formatted = cleaned;
                                if (cleaned.length > 2) {
                                  formatted = '${cleaned.substring(0, 2)} ${cleaned.substring(2)}';
                                }
                                if (cleaned.length > 5) {
                                  formatted =
                                      '${cleaned.substring(0, 2)} ${cleaned.substring(2, 5)} ${cleaned.substring(5)}';
                                }
                                if (cleaned.length > 7) {
                                  formatted =
                                      '${cleaned.substring(0, 2)} ${cleaned.substring(2, 5)} ${cleaned.substring(5, 7)} ${cleaned.substring(7)}';
                                }
                                if (_phoneController.text != formatted) {
                                  _phoneController.value = TextEditingValue(
                                    text: formatted,
                                    selection: TextSelection.collapsed(
                                      offset: formatted.length,
                                    ),
                                  );
                                }
                              }
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                  SizedBox(height: 100.h),
                  // Legal disclaimer
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 16.w),
                    child: Text(
                      '"By pressing "Continue" I accept the BTB Bank Licence Agreement conditions"',
                      style: AppTextStyles.legalDisclaimer(context),
                      textAlign: TextAlign.center,
                    ),
                  ),
                  SizedBox(height: 24.h),
                  // Continue button
                  ValueListenableBuilder<bool>(
                    valueListenable: _isButtonEnabled,
                    builder: (context, isEnabled, child) {
                      return PrimaryActionButton(
                        text: 'Continue',
                        onPressed: _handleContinue,
                        isEnabled: isEnabled,
                      );
                    },
                  ),
                  SizedBox(height: 32.h),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

