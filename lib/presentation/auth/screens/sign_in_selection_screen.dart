import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/theme/app_text_styles.dart';

class SignInSelectionScreen extends StatelessWidget {
  final String? phone;

  const SignInSelectionScreen({super.key, this.phone});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppTheme.mainBackground,
      body: SafeArea(
        child: SingleChildScrollView(
          padding: EdgeInsets.symmetric(horizontal: 24.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(height: 60.h),
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
                    icon: Icons.lock,
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
                  _buildSignInButton(
                    context: context,
                    backgroundColor: AppTheme.white,
                    borderColor: AppTheme.borderLight,
                    icon: null, // SİMA logo instead
                    title: 'Continue with SİMA',
                    subtitle: 'Secure digital verification',
                    titleColor: AppTheme.textDark,
                    subtitleColor: AppTheme.textDark.withOpacity(0.8),
                    showSimaLogo: true,
                    onTap: () {
                      // TODO: Implement SİMA authentication
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('SİMA authentication coming soon'),
                        ),
                      );
                    },
                  ),
                ],
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
    IconData? icon,
    Color? iconColor,
    required String title,
    required String subtitle,
    required Color titleColor,
    required Color subtitleColor,
    bool showSimaLogo = false,
    required VoidCallback onTap,
  }) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(28.r),
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
                // Icon or SİMA logo
                if (showSimaLogo)
                  _buildSimaLogo()
                else if (icon != null && iconColor != null)
                  Icon(
                    icon,
                    color: iconColor,
                    size: 32.sp,
                  ),
                SizedBox(width: 12.w),
                // Text content
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Title - Body/Large (using inputText)
                      Text(
                        title,
                        style: AppTextStyles.inputText(context, color: titleColor),
                      ),
                      SizedBox(height: 4.h),
                      // Subtitle - Body/Small
                      Text(
                        subtitle,
                        style: AppTextStyles.buttonSubtitle(context, color: subtitleColor),
                      ),
                    ],
                  ),
                ),
              ],
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

