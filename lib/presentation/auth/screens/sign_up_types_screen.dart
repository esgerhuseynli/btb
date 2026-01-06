import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../../core/constants/app_constants.dart';
import '../../core/widgets/app_app_bar.dart';

class SignUpTypesScreen extends StatelessWidget {
  final int? screenType;
  final String? verifyCode;
  final String? phone;
  final String? email;

  const SignUpTypesScreen({
    super.key,
    this.screenType,
    this.verifyCode,
    this.phone,
    this.email,
  });

  @override
  Widget build(BuildContext context) {
    final hasVerifyCode = verifyCode != null;

    return Scaffold(
      backgroundColor: AppTheme.mainBackground,
      appBar: AppAppBar(
        title: 'Qeydiyyat',
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => context.pop(),
        ),
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const SizedBox(height: 32),
              if (hasVerifyCode) ...[
                // After verification code - show phone/email options
                _buildSignUpOption(
                  context,
                  icon: Icons.phone,
                  title: 'Mobil nömrə ilə qeydiyyat',
                  onTap: () {
                    context.push(
                      '/sign-up-number',
                      extra: {
                        'signUpType': screenType ?? AppConstants.signUpTypePan,
                        'verifyCode': verifyCode!,
                        'phone': phone,
                      },
                    );
                  },
                ),
                const SizedBox(height: 16),
                _buildSignUpOption(
                  context,
                  icon: Icons.email,
                  title: 'Email ilə qeydiyyat',
                  enabled: email != null && email!.isNotEmpty,
                  onTap: email != null && email!.isNotEmpty
                      ? () {
                          context.push(
                            '/sign-up-email',
                            extra: {
                              'signUpType': screenType ?? AppConstants.signUpTypePan,
                              'verifyCode': verifyCode!,
                              'email': email,
                            },
                          );
                        }
                      : null,
                ),
              ] else ...[
                // Initial sign-up options
                _buildSignUpOption(
                  context,
                  icon: Icons.badge,
                  title: 'Müştəri kodu ilə (CIF) qeydiyyat',
                  onTap: () {
                    context.push('/sign-up-cif');
                  },
                ),
                const SizedBox(height: 16),
                _buildSignUpOption(
                  context,
                  icon: Icons.credit_card,
                  title: 'Kart üzrə qeydiyyat',
                  onTap: () {
                    context.push('/sign-up-card');
                  },
                ),
              ],
              const Spacer(),
              OutlinedButton(
                onPressed: () {
                  context.go('/sign-in');
                },
                style: OutlinedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  side: const BorderSide(color: AppTheme.mainColor, width: 2),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                child: const Text(
                  'Giriş',
                  style: TextStyle(
                    color: AppTheme.mainColor,
                    fontSize: 16,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildSignUpOption(
    BuildContext context, {
    required IconData icon,
    required String title,
    bool enabled = true,
    VoidCallback? onTap,
  }) {
    return Card(
      color: enabled ? Colors.white : Colors.grey[100],
      elevation: 2,
      child: InkWell(
        onTap: enabled ? onTap : null,
        borderRadius: BorderRadius.circular(12),
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Row(
            children: [
              Icon(
                icon,
                color: enabled ? AppTheme.mainColor : Colors.grey,
                size: 32,
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Text(
                  title,
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w600,
                    color: enabled ? AppTheme.textColor : Colors.grey,
                  ),
                ),
              ),
              Icon(
                Icons.arrow_forward_ios,
                size: 16,
                color: enabled ? AppTheme.textColor : Colors.grey,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

