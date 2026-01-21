import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/localization/app_localizations_ext.dart';
import '../../../core/widgets/primary_action_button.dart';
import '../../../core/widgets/back_button_widget.dart';
import '../../bloc/auth_bloc.dart';
import '../../bloc/auth_event.dart';
import '../../bloc/auth_state.dart';

class ForgotPasswordScreen extends StatefulWidget {
  final String? phone;

  const ForgotPasswordScreen({super.key, this.phone});

  @override
  State<ForgotPasswordScreen> createState() => _ForgotPasswordScreenState();
}

class _ForgotPasswordScreenState extends State<ForgotPasswordScreen> {
  final _finCodeController = TextEditingController();
  final _dateOfBirthController = TextEditingController();
  final _finCodeFocusNode = FocusNode();
  final _dateOfBirthFocusNode = FocusNode();
  final _isButtonEnabled = ValueNotifier<bool>(false);
  DateTime? _selectedDate;
  bool _hasNavigated = false;

  @override
  void initState() {
    super.initState();
    _finCodeController.addListener(_onFormChanged);
    _dateOfBirthController.addListener(_onFormChanged);
    // Request focus after the first frame to ensure keyboard opens
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _finCodeFocusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _finCodeController.removeListener(_onFormChanged);
    _dateOfBirthController.removeListener(_onFormChanged);
    _finCodeController.dispose();
    _dateOfBirthController.dispose();
    _finCodeFocusNode.dispose();
    _dateOfBirthFocusNode.dispose();
    _isButtonEnabled.dispose();
    super.dispose();
  }

  void _onFormChanged() {
    final finCode = _finCodeController.text.trim();
    final dateOfBirth = _dateOfBirthController.text.trim();
    _isButtonEnabled.value = widget.phone != null &&
        widget.phone!.isNotEmpty &&
        (finCode.length == 7 || dateOfBirth.isNotEmpty);
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime now = DateTime.now();
    final DateTime firstDate = DateTime(now.year - 100);
    final DateTime lastDate = DateTime(now.year - 18);

    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _selectedDate ?? lastDate,
      firstDate: firstDate,
      lastDate: lastDate,
    );

    if (picked != null && picked != _selectedDate) {
      setState(() {
        _selectedDate = picked;
        // Format: dd-MM-yyyy (e.g., "15-03-1990")
        _dateOfBirthController.text = DateFormat('dd-MM-yyyy').format(picked);
      });
      _onFormChanged();
    }
  }

  void _handleContinue() {
    final finCode = _finCodeController.text.trim();
    final dateOfBirth = _dateOfBirthController.text.trim();

    // Validate - phone is required, and either finCode (7 chars) or dateOfBirth must be provided
    if (widget.phone != null &&
        widget.phone!.isNotEmpty &&
        (finCode.length == 7 || dateOfBirth.isNotEmpty)) {
      context.read<AuthBloc>().add(
            ForgotPasswordEvent(
              username: widget.phone!,
              finCode: finCode,
              birthDate: dateOfBirth,
            ),
          );
    }
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is AuthError) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: AppTheme.red,
            ),
          );
        } else if (state is ForgotPasswordSuccess && !_hasNavigated) {
          _hasNavigated = true;
          // Navigate to OTP verification screen for forgot password flow
          context.push(
            '/otp-verification?phoneNumber=${state.mobileNumber ?? ''}&flowType=forgotPassword',
          );
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        resizeToAvoidBottomInset: true,
        body: SafeArea(
          child: Column(
            children: [
              const BackButtonWidget(),
              Expanded(
                child: LayoutBuilder(
                  builder: (context, constraints) {
                    return SingleChildScrollView(
                      padding: EdgeInsets.symmetric(horizontal: 24.w),
                      child: ConstrainedBox(
                        constraints: BoxConstraints(
                          minHeight: constraints.maxHeight,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          mainAxisSize: MainAxisSize.min,
                          children: [
                              // SizedBox(height: 40.h),
                              // Title - H2/Medium style
                              Text(
                                context.l10n.passwordReset,
                                style: AppTextStyles.screenTitle(context),
                                textAlign: TextAlign.center,
                              ),
                              SizedBox(height: 16.h),
                              // "Forgot password?" text
                              Align(
                                alignment: Alignment.centerLeft,
                                child: Padding(
                                  padding: EdgeInsets.only(left: 8.w),
                                  child: Text(
                                    context.l10n.forgotPassword,
                                    style: AppTextStyles.inputText(context, color: AppTheme.textDark),
                                    textAlign: TextAlign.left,
                                  ),
                                ),
                              ),
                              SizedBox(height: 8.h),
                              // Instruction text
                              Align(
                                alignment: Alignment.centerLeft,
                                child: Padding(
                                  padding: EdgeInsets.only(left: 8.w),
                                  child: Text(
                                    context.l10n.enterDateOfBirthOrFin,
                                    style: Theme.of(context).textTheme.bodySmall!.copyWith(
                                      fontFamily: 'SFPro',
                                      fontWeight: FontWeight.w500,
                                      color: AppTheme.textDark,
                                    ),
                                    textAlign: TextAlign.left,
                                  ),
                                ),
                              ),
                              SizedBox(height: 24.h),
                              // Fin code input field
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  // Fin code label
                                  Padding(
                                    padding: EdgeInsets.only(left: 8.w, bottom: 8.h),
                                    child: Text(
                                      context.l10n.finCode,
                                      style: AppTextStyles.inputLabel(context),
                                    ),
                                  ),
                                  // Fin code input container
                                  Container(
                                    decoration: BoxDecoration(
                                      color: AppTheme.white, // White / Base
                                      border: Border.all(
                                        color: const Color(0xFFE5E7EB), // Stroke gray
                                        width: 1,
                                      ),
                                      borderRadius: BorderRadius.circular(20.r), // Radius: 20px
                                    ),
                                    child: Padding(
                                      padding: EdgeInsets.all(16.w), // Padding: 16px
                                      child: Row(
                                        children: [
                                          // Lock icon
                                          GestureDetector(
                                            onTap: () {
                                              _finCodeFocusNode.requestFocus();
                                            },
                                            child: SizedBox(
                                              width: 24.w,
                                              height: 24.h,
                                              child: SvgPicture.asset(
                                                'assets/icons/lock.svg',
                                                width: 24.w,
                                                height: 24.h,
                                                colorFilter: const ColorFilter.mode(
                                                  Color(0xFFC4C4C4), // ICON DEFAULT / Disabled background
                                                  BlendMode.srcIn,
                                                ),
                                              ),
                                            ),
                                          ),
                                          SizedBox(width: 12.w), // Gap: 12px
                                          // Fin code input field
                                          Expanded(
                                            child: TextFormField(
                                              controller: _finCodeController,
                                              focusNode: _finCodeFocusNode,
                                              keyboardType: TextInputType.text,
                                              textCapitalization: TextCapitalization.characters,
                                              inputFormatters: [
                                                FilteringTextInputFormatter.allow(
                                                  RegExp(r'[0-9A-Za-z]'),
                                                ),
                                                LengthLimitingTextInputFormatter(7), // Max 7 characters
                                              ],
                                              style: AppTextStyles.inputText(context, color: AppTheme.textDark), // Body/Large
                                              decoration: InputDecoration(
                                                border: InputBorder.none,
                                                enabledBorder: InputBorder.none,
                                                focusedBorder: InputBorder.none,
                                                errorBorder: InputBorder.none,
                                                focusedErrorBorder: InputBorder.none,
                                                contentPadding: EdgeInsets.zero,
                                                isDense: true,
                                                hintText: '',
                                                hintStyle: AppTextStyles.inputHint(context),
                                              ),
                                              onFieldSubmitted: (_) {
                                                _dateOfBirthFocusNode.requestFocus();
                                              },
                                            ),
                                          ),
                                          SizedBox(width: 12.w), // Gap: 12px
                                        ],
                                      ),
                                    ),
                                  ),
                                  SizedBox(height: 24.h),
                                  // Date of birth label
                                  Padding(
                                    padding: EdgeInsets.only(left: 8.w, bottom: 8.h),
                                    child: Text(
                                      context.l10n.dateOfBirth,
                                      style: AppTextStyles.inputLabel(context),
                                    ),
                                  ),
                                  // Date of birth input container
                                  GestureDetector(
                                    behavior: HitTestBehavior.opaque,
                                    onTap: () => _selectDate(context),
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: AppTheme.white, // White / Base
                                        border: Border.all(
                                          color: const Color(0xFFE5E7EB), // Stroke gray
                                          width: 1,
                                        ),
                                        borderRadius: BorderRadius.circular(20.r), // Radius: 20px
                                      ),
                                      child: Padding(
                                        padding: EdgeInsets.all(16.w), // Padding: 16px
                                        child: Row(
                                          children: [
                                            // Lock icon
                                            SizedBox(
                                              width: 24.w,
                                              height: 24.h,
                                              child: SvgPicture.asset(
                                                'assets/icons/lock.svg',
                                                width: 24.w,
                                                height: 24.h,
                                                colorFilter: const ColorFilter.mode(
                                                  Color(0xFFC4C4C4), // ICON DEFAULT / Disabled background
                                                  BlendMode.srcIn,
                                                ),
                                              ),
                                            ),
                                            SizedBox(width: 12.w), // Gap: 12px
                                            // Date of birth input field
                                            Expanded(
                                              child: TextFormField(
                                                controller: _dateOfBirthController,
                                                focusNode: _dateOfBirthFocusNode,
                                                keyboardType: TextInputType.datetime,
                                                enabled: false, // Disable text input, only allow date picker
                                                style: AppTextStyles.inputText(context, color: AppTheme.textDark), // Body/Large
                                                decoration: InputDecoration(
                                                  border: InputBorder.none,
                                                  enabledBorder: InputBorder.none,
                                                  focusedBorder: InputBorder.none,
                                                  errorBorder: InputBorder.none,
                                                  focusedErrorBorder: InputBorder.none,
                                                  contentPadding: EdgeInsets.zero,
                                                  isDense: true,
                                                  hintText: context.l10n.dateOfBirthHint,
                                                  hintStyle: AppTextStyles.inputHint(context),
                                                ),
                                              ),
                                            ),
                                            SizedBox(width: 12.w), // Gap: 12px
                                            // Calendar icon
                                            Container(
                                              width: 40.w,
                                              height: 40.h,
                                              alignment: Alignment.center,
                                              child: Icon(
                                                Icons.calendar_today,
                                                size: 24.w,
                                                color: const Color(0xFFC4C4C4),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                  SizedBox(height: 8.h),
                                ],
                              ),
                              SizedBox(height: 24.h),
                            // License agreement text
                              Padding(
                                padding: EdgeInsets.symmetric(horizontal: 16.w),
                                child: Text(
                                  context.l10n.licenseAgreement,
                                  style: AppTextStyles.legalDisclaimer(context),
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            SizedBox(height: 16.h),
                            // Continue button
                            BlocBuilder<AuthBloc, AuthState>(
                              builder: (context, state) {
                                final isLoading = state is AuthLoading;
                                return ValueListenableBuilder<bool>(
                                  valueListenable: _isButtonEnabled,
                                  builder: (context, isEnabled, child) {
                                    return PrimaryActionButton(
                                      text: context.l10n.continueButton,
                                      onPressed: isLoading ? null : _handleContinue,
                                      isEnabled: isEnabled && !isLoading,
                                    );
                                  },
                                );
                              },
                              ),
                              SizedBox(height: 24.h),
                            ],
                          ),
                        ),
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

