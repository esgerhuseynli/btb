import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_state.dart';

// ---- FORMATTER ----
class AzPhoneFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(
      TextEditingValue oldValue,
      TextEditingValue newValue,
      ) {
    final digits = newValue.text.replaceAll(RegExp(r'\D'), '');

    final buffer = StringBuffer();

    for (int i = 0; i < digits.length && i < 9; i++) {
      buffer.write(digits[i]);

      if (i == 1 || i == 4 || i == 6) {
        buffer.write(' ');
      }
    }

    final formatted = buffer.toString().trimRight();

    return TextEditingValue(
      text: formatted,
      selection: TextSelection.collapsed(offset: formatted.length),
    );
  }
}

// ---- SCREEN ----
class PhoneInputScreen extends StatefulWidget {
  const PhoneInputScreen({super.key});

  @override
  State<PhoneInputScreen> createState() => _PhoneInputScreenState();
}

class _PhoneInputScreenState extends State<PhoneInputScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _phoneController = TextEditingController();
  final FocusNode _phoneFocusNode = FocusNode();
  final ValueNotifier<bool> _isButtonEnabled = ValueNotifier(false);

  @override
  void initState() {
    super.initState();

    _phoneController.addListener(() {
      final cleaned = _phoneController.text.replaceAll(RegExp(r'\D'), '');
      _isButtonEnabled.value = cleaned.length == 9;
    });
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _phoneFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
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
        body: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: () {
            FocusManager.instance.primaryFocus?.unfocus();
          },
          child: SafeArea(
            child: Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(height: 50.h),

                  // ---- TITLE ----
                  Text(
                    'Enter phone number',
                    style: AppTextStyles.screenTitle(context),
                    textAlign: TextAlign.center,
                  ),

                  SizedBox(height: 40.h),
                  // ---- PHONE INPUT ROW ----
                  Padding(
                    padding: const EdgeInsets.only(left: 48.0, right: 48.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Text(
                          '+994 ',
                          style: AppTextStyles.phoneNumberDisplay(context),
                        ),

                        // ⬇️ BURASI ƏSAS DÜZƏLİŞ
                        Flexible(
                          child: TextFormField(
                            controller: _phoneController,
                            focusNode: _phoneFocusNode,
                            autofocus: true,
                            keyboardType: TextInputType.number,
                            textInputAction: TextInputAction.done,
                            showCursor: true,
                            inputFormatters: [
                              FilteringTextInputFormatter.digitsOnly,
                              AzPhoneFormatter(),
                            ],
                            style: AppTextStyles.phoneNumberInput(context),
                            decoration: const InputDecoration(
                              border: InputBorder.none,
                              enabledBorder: InputBorder.none,
                              focusedBorder: InputBorder.none,
                              fillColor: Colors.transparent,
                              isDense: true,
                            ),
                            onFieldSubmitted: (_) {
                              FocusManager.instance.primaryFocus?.unfocus();
                            },
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
                          ),
                        ),
                      ],
                    ),
                  ),

                  const Spacer(),

                  // ---- DISCLAIMER ----
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 16.w),
                    child: Text(
                      '"By pressing "Continue" I accept the BTB Bank Licence Agreement conditions"',
                      style: AppTextStyles.buttonTitle(context),
                      textAlign: TextAlign.center,
                    ),
                  ),

                  SizedBox(height: 16.h),

                  // ---- BUTTON ----
                  ValueListenableBuilder<bool>(
                    valueListenable: _isButtonEnabled,
                    builder: (context, isEnabled, child) {
                      return Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: PrimaryActionButton(
                          text: 'Continue',
                          onPressed: isEnabled ? _handleContinue : null,
                          isEnabled: isEnabled,
                        ),
                      );
                    },
                  ),

                  SizedBox(height: 20.h),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
