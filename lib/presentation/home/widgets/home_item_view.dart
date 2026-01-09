import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class HomeItemView extends StatelessWidget {
  final String text;
  final String iconPath;
  final VoidCallback? onTap;
  final Color? backgroundColor;

  const HomeItemView({
    super.key,
    required this.text,
    required this.iconPath,
    this.onTap,
    this.backgroundColor,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        height: 60,
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Row(
          children: [
            // Icon
            Image.asset(
              iconPath,
              width: 50,
              height: 23,
              errorBuilder: (context, error, stackTrace) {
                return const Icon(
                  Icons.credit_card,
                  size: 23,
                  color: AppTheme.textColor,
                );
              },
            ),
            const SizedBox(width: 6),
            // Text
            Expanded(
              child: Text(
                text,
                style: const TextStyle(
                  fontSize: 17,
                  color: AppTheme.textColor,
                ),
              ),
            ),
            // Arrow icon
            Image.asset(
              'assets/images/ic_next_arrow.png',
              width: 40,
              height: 60,
              color: const Color(0xFF757575),
              errorBuilder: (context, error, stackTrace) {
                return const Icon(
                  Icons.chevron_right,
                  color: Color(0xFF757575),
                  size: 24,
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}

