import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';

import '../../../../core/constants/app_dimensions.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_state.dart';

// ---- FORMATTER (keeps +994, correct spacing, prevents wrong duplication) ----
class AzPhoneFormatter extends TextInputFormatter {
  static const prefix = '+994 ';

  @override
  TextEditingValue formatEditUpdate(
      TextEditingValue oldValue, TextEditingValue newValue) {
    String newText = newValue.text;

    // Ensure prefix exists
    if (!newText.startsWith(prefix)) {
      newText = prefix;
    }

    // Extract only digits after prefix
    final digits = newText.substring(prefix.length).replaceAll(RegExp(r'\D'), '');

    // Build formatted string with correct spaces
    final buffer = StringBuffer();
    for (int i = 0; i < digits.length && i < 9; i++) {
      buffer.write(digits[i]);
      if (i == 1 || i == 4 || i == 6) buffer.write(' ');
    }

    final formatted = prefix + buffer.toString().trimRight();

    // Prevent Flutter from looping
    if (formatted == oldValue.text) return oldValue;

    // Maintain cursor position
    int cursorPosition = formatted.length;

    return TextEditingValue(
      text: formatted,
      selection: TextSelection.collapsed(offset: cursorPosition),
    );
  }
}

// ---- SCREEN ----
class PhoneInputScreen extends StatefulWidget {
  const PhoneInputScreen({super.key});

  @override
  State<PhoneInputScreen> createState() => _PhoneInputScreenState();
}

class _PhoneInputScreenState extends State<PhoneInputScreen>
    with SingleTickerProviderStateMixin {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _phoneController =
  TextEditingController(text: AzPhoneFormatter.prefix);
  final FocusNode _phoneFocusNode = FocusNode();
  final ValueNotifier<bool> _isButtonEnabled = ValueNotifier(false);

  late AnimationController _blinkController;
  late Animation<double> _blinkAnimation;

  @override
  void initState() {
    super.initState();

    _blinkController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 900),
    )..repeat(reverse: true);

    _blinkAnimation = Tween<double>(begin: 0.2, end: 1).animate(
      CurvedAnimation(parent: _blinkController, curve: Curves.easeInOut),
    );

    _phoneController.addListener(() {
      final cleaned = _phoneController.text
          .replaceAll(AzPhoneFormatter.prefix, '')
          .replaceAll(RegExp(r'\D'), '');
      _isButtonEnabled.value = cleaned.length == 9;
    });

    // Request focus after first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _phoneFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _phoneFocusNode.dispose();
    _isButtonEnabled.dispose();
    _blinkController.dispose();
    super.dispose();
  }

  void _handleContinue() {
    if (!(_formKey.currentState?.validate() ?? false)) return;

    final phone = _phoneController.text
        .replaceAll(AzPhoneFormatter.prefix, '')
        .replaceAll(RegExp(r'\D'), '');

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
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(height: 50.h),

                Text(
                  'Enter phone number',
                  style: AppTextStyles.screenTitle(context),
                  textAlign: TextAlign.center,
                ),

                SizedBox(height: 40.h),

                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 48),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Stack(
                        alignment: Alignment.bottomLeft,
                        children: [
                          TextFormField(
                            textAlign: TextAlign.center,
                            textAlignVertical: TextAlignVertical.center,
                            controller: _phoneController,
                            focusNode: _phoneFocusNode,
                            autofocus: true,
                            enabled: true,
                            keyboardType: TextInputType.number,
                            textInputAction: TextInputAction.done,
                            showCursor: true,
                            maxLines: 1,
                            scrollPhysics: const BouncingScrollPhysics(),
                            inputFormatters: [AzPhoneFormatter()],
                            style: AppTextStyles.phoneNumberInput(context),
                            decoration: const InputDecoration(
                              border: InputBorder.none,
                              enabledBorder: InputBorder.none,
                              focusedBorder: InputBorder.none,
                              isDense: true,
                              filled: false,
                              contentPadding: EdgeInsets.only(bottom: 12),
                            ),
                            onFieldSubmitted: (_) =>
                                FocusManager.instance.primaryFocus?.unfocus(),
                            onChanged: (value) {
                              // Ensure field stays focused for input
                              if (!_phoneFocusNode.hasFocus) {
                                _phoneFocusNode.requestFocus();
                              }
                            },
                              validator: (value) {
                                if (value == null || value.isEmpty) {
                                  return 'Telefon nömrəsi daxil edin';
                                }
                                final cleaned = value
                                    .replaceAll(AzPhoneFormatter.prefix, '')
                                    .replaceAll(RegExp(r'\D'), '');
                                if (cleaned.length != 9) {
                                  return 'Telefon nömrəsi düzgün deyil';
                                }
                                return null;
                              },
                            ),

                          ],
                        ),

                      ],
                    ),
                  ),

                  const Spacer(),

                  Padding(
                    padding: AppDimensions.paddingHorizontal8,
                    child: Text(
                      'By pressing "Continue" I accept the BTB Bank Licence Agreement conditions',
                      style: AppTextStyles.buttonTitle(context),
                      textAlign: TextAlign.center,
                    ),
                  ),

                  SizedBox(height: 16.h),

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
    );
  }

  double _calculateCursorOffset(BuildContext context) {
    final text = _phoneController.text;
    final textStyle = AppTextStyles.phoneNumberInput(context);
    final tp = TextPainter(
      text: TextSpan(text: text, style: textStyle),
      maxLines: 1,
      textDirection: TextDirection.ltr,
    )..layout();

    return tp.width + 2;
  }
}