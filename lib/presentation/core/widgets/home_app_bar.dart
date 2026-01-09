import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class HomeAppBar extends StatelessWidget implements PreferredSizeWidget {
  final VoidCallback? onMenuTap;
  final VoidCallback? onNotificationTap;

  const HomeAppBar({
    super.key,
    this.onMenuTap,
    this.onNotificationTap,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: kToolbarHeight,
      color: AppTheme.white,
      child: Row(
        children: [
          // Hamburger menu (left)
          IconButton(
            icon: const Icon(Icons.menu),
            color: AppTheme.bottomBarMenuItemTint, // Gray
            onPressed: onMenuTap ?? () {
              // Default: open drawer if available
              Scaffold.of(context).openDrawer();
            },
          ),
          // BANK BTB Logo (center)
          const Expanded(
            child: Center(
              child: _BankBtbLogo(),
            ),
          ),
          // Bell/Notification icon (right)
          IconButton(
            icon: const Icon(Icons.notifications_outlined),
            color: AppTheme.bottomBarMenuItemTint, // Gray
            onPressed: onNotificationTap ?? () {
              // TODO: Navigate to notifications
            },
          ),
        ],
      ),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}

class _BankBtbLogo extends StatelessWidget {
  const _BankBtbLogo();

  @override
  Widget build(BuildContext context) {
    // Try to use the logo image first (it should contain the full BANK BTB logo)
    return Image.asset(
      'assets/images/logo.png',
      height: 24.0,
      fit: BoxFit.contain,
      errorBuilder: (context, error, stackTrace) {
        // Fallback: Build logo from components (flame icon + text)
        return Row(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            // Red flame/teardrop icon (BTB logo)
            Image.asset(
              'assets/images/ic_btb.png',
              width: 20.0,
              height: 20.0,
              color: AppTheme.colorPrimaryDark,
              errorBuilder: (context, error, stackTrace) {
                // Final fallback: use a flame icon
                return Icon(
                  Icons.local_fire_department,
                  size: 20.0,
                  color: AppTheme.colorPrimaryDark,
                );
              },
            ),
            const SizedBox(width: 4.0),
            // "BANK" text in black
            const Text(
              'BANK',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w600,
                color: AppTheme.textColor, // Black
                letterSpacing: 0.5,
              ),
            ),
            const SizedBox(width: 2.0),
            // "BTB" text in red
            const Text(
              'BTB',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.w600,
                color: AppTheme.colorPrimaryDark, // Red
                letterSpacing: 0.5,
              ),
            ),
          ],
        );
      },
    );
  }
}

