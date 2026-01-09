import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class PaymentProviderGroupItem extends StatelessWidget {
  final String name;
  final IconData icon;
  final VoidCallback? onTap;

  const PaymentProviderGroupItem({
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
        margin: const EdgeInsets.all(2.0),
        padding: const EdgeInsets.symmetric(
          horizontal: 8.0,
          vertical: 12.0,
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
            Container(
              width: 64.0,
              height: 64.0,
              margin: const EdgeInsets.only(bottom: 8.0),
              child: Icon(
                icon,
                size: 40.0,
                color: AppTheme.colorPrimaryDark, // Red icon
              ),
            ),
            // Text - wrapped in Flexible to prevent overflow
            Flexible(
              child: Text(
                name,
                style: const TextStyle(
                  fontSize: 13.0,
                  color: AppTheme.textColor, // Black text
                ),
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

