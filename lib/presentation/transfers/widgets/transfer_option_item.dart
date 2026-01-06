import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class TransferOptionItem extends StatelessWidget {
  final String name;
  final IconData icon;
  final VoidCallback? onTap;

  const TransferOptionItem({
    super.key,
    required this.name,
    required this.icon,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.all(5.0),
        padding: const EdgeInsets.symmetric(
          horizontal: 24.0,
          vertical: 24.0,
        ),
        decoration: BoxDecoration(
          color: AppTheme.white,
          borderRadius: BorderRadius.circular(8.0),
          border: Border.all(
            color: AppTheme.borderColor,
            width: 1.0,
          ),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            // Icon
            Icon(
              icon,
              size: 48.0,
              color: AppTheme.colorPrimaryDark, // Red icon
            ),
            const SizedBox(height: 6.0),
            // Text - wrapped in Flexible to prevent overflow
            Flexible(
              child: Text(
                name,
                style: const TextStyle(
                  fontSize: 13.0,
                  color: AppTheme.textColor, // Black text
                ),
                textAlign: TextAlign.center,
                maxLines: 3,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

