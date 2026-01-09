import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class IntroPageView extends StatelessWidget {
  final String title;
  final String description;
  final IconData icon;

  const IntroPageView({
    super.key,
    required this.title,
    required this.description,
    required this.icon,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(32.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 120, color: AppTheme.mainColor),
          const SizedBox(height: 48),
          Text(
            title,
            style: Theme.of(context).textTheme.displayMedium?.copyWith(
              color: AppTheme.textColor,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          Text(
            description,
            style: Theme.of(
              context,
            ).textTheme.bodyLarge?.copyWith(color: AppTheme.hintColor),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }
}


