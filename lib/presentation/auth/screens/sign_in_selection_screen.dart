import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/theme/app_text_styles.dart';
import '../../../core/utils/phone_utils.dart';
import '../../core/widgets/back_button_widget.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_event.dart';
import '../bloc/auth_state.dart';

class SignInSelectionScreen extends StatelessWidget {
  final String? phone;

  const SignInSelectionScreen({super.key, this.phone});

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
        } else if (state is OtpSent) {
          // Navigate to OTP verification screen when OTP is sent
          // Use phone number from state (already normalized by the repository)
          context.push(
            '/otp-verification?phoneNumber=${state.phoneNumber}&flowType=simaSignIn',
          );
        }
      },
      child: Scaffold(
        backgroundColor: AppTheme.mainBackground,
        body: SafeArea(
          child: Column(
            children: [
              BackButtonWidget(
                onPressed: () => context.go('/phone-entry'),
              ),
              Expanded(
                child: SingleChildScrollView(
                  padding: EdgeInsets.symmetric(horizontal: 24.w),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(height: 10.h),
                      // Title - H2/Medium style
                      Text(
                        'Sign in',
                        style: AppTextStyles.screenTitle(context),
                        textAlign: TextAlign.center,
                      ),
                      SizedBox(height: 56.h),
                      // Sign in options
                      Column(
                        children: [
                          // Sign in with password button
                          _buildSignInButton(
                            context: context,
                            backgroundColor: AppTheme.buttonSignInBackground,
                            iconPath: 'assets/icons/lock.svg',
                            iconColor: AppTheme.white,
                            title: 'Sign in with password',
                            subtitle: 'Secure login to your account',
                            titleColor: AppTheme.white,
                            subtitleColor: AppTheme.white.withOpacity(0.8),
                            onTap: () {
                              // Navigate to password entry screen
                              context.push('/password-entry?phone=$phone');
                            },
                          ),
                          SizedBox(height: 20.h),
                          // Continue with SİMA button
                          BlocBuilder<AuthBloc, AuthState>(
                            builder: (context, state) {
                              final isLoading = state is AuthLoading;
                              return _buildSignInButton(
                                context: context,
                                backgroundColor: AppTheme.white,
                                borderColor: AppTheme.borderLight,
                                title: 'Continue with SİMA',
                                subtitle: isLoading ? 'Sending OTP...' : 'Secure digital verification',
                                titleColor: AppTheme.textDark,
                                subtitleColor: AppTheme.textDark,
                                showSimaLogo: true,
                                isLoading: isLoading,
                                onTap: isLoading ? null : () {
                                  if (phone == null || phone!.isEmpty) {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                        content: Text('Phone number is required'),
                                        backgroundColor: AppTheme.red,
                                      ),
                                    );
                                    return;
                                  }
                                  
                                  // Normalize phone number
                                  final normalizedPhone = PhoneUtils.normalizeToFullFormat(phone!);
                                  
                                  // Send OTP for SIMA sign-in
                                  context.read<AuthBloc>().add(
                                        SendOtpEvent(
                                          phoneNumber: normalizedPhone,
                                          text: 'SIMA Sign In',
                                          type: 1, // OTP type for sign-in
                                          userId: normalizedPhone, // Use phone number as userId
                                          flowType: OtpFlowType.simaSignIn,
                                        ),
                                      );
                                },
                              );
                            },
                          ),
                        ],
                      ),
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

  Widget _buildSignInButton({
    required BuildContext context,
    required Color backgroundColor,
    Color? borderColor,
    String? iconPath,
    Color? iconColor,
    required String title,
    required String subtitle,
    required Color titleColor,
    required Color subtitleColor,
    bool showSimaLogo = false,
    bool isLoading = false,
    required VoidCallback? onTap,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(36.r),
        child: Opacity(
          opacity: (onTap == null || isLoading) ? 0.6 : 1.0,
          child: Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 16.h),
            decoration: BoxDecoration(
              color: backgroundColor,
              border: borderColor != null
                  ? Border.all(color: borderColor, width: 1)
                  : null,
              borderRadius: BorderRadius.circular(28.r),
            ),
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 24.w),
              child: Row(
                children: [
                  // Icon or SİMA logo or loading indicator
                  if (isLoading)
                    SizedBox(
                      width: 32.sp,
                      height: 32.sp,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        valueColor: AlwaysStoppedAnimation<Color>(titleColor),
                      ),
                    )
                  else if (showSimaLogo)
                    _buildSimaLogo()
                  else if (iconPath != null && iconColor != null)
                    SizedBox(
                      width: 32.sp,
                      height: 32.sp,
                      child: SvgPicture.asset(
                        iconPath,
                        width: 32.sp,
                        height: 32.sp,
                        colorFilter: ColorFilter.mode(
                          iconColor,
                          BlendMode.srcIn,
                        ),
                      ),
                    ),
                  SizedBox(width: 12.w),
                  // Text content
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        // Title - Body/Large (using inputText)
                        Text(
                          title,
                          style: AppTextStyles.inputText(context, color: titleColor).copyWith(height: 1.2),
                        ),
                        // Subtitle - Body/Small with negative margin to reduce spacing
                        Transform.translate(
                          offset: Offset(0, -4.h),
                          child: Text(
                            subtitle,
                            style: AppTextStyles.buttonSubtitle(context, color: subtitleColor).copyWith(height: 1.5),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildSimaLogo() {
    return SizedBox(
      width: 48.w,
      height: 20.h,
      child: SvgPicture.asset(
        'assets/images/SIMA_logo.svg',
        width: 48.w,
        height: 20.h,
        fit: BoxFit.contain,
      ),
    );
  }
}

