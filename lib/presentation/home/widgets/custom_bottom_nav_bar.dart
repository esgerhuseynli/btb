import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class CustomBottomNavBar extends StatelessWidget {
  final int currentIndex;
  final Function(int) onTap;

  const CustomBottomNavBar({
    super.key,
    required this.currentIndex,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    // Convert dp to logical pixels (1 dp ≈ 1 logical pixel on most devices)
    const double bottomNavHeight = 70.0; // 70dp
    const double centerButtonSize = 51.0; // 51dp
    const double centerButtonElevation = 3.0; // 3dp
    const double centerButtonMarginBottom = -36.0; // -36dp
    const double iconSize = 20.0;
    const double fontSize = 13.0; // 13sp

    final bool isHomeSelected = currentIndex == 1;
    final bool isPaymentsSelected = currentIndex == 0;
    final bool isTransfersSelected = currentIndex == 2;

    return Stack(
      clipBehavior: Clip.none,
      children: [
        // Bottom navigation bar background
        Container(
          height: bottomNavHeight,
          color: AppTheme.white,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              // Payments button (left)
              Expanded(
                child: _buildNavButton(
                  context,
                  icon: Icons.account_balance_wallet,
                  label: 'Ödənişlər',
                  isSelected: isPaymentsSelected,
                  onTap: () => onTap(0),
                  iconSize: iconSize,
                  fontSize: fontSize,
                ),
              ),
              // Center text button (no icon, just text)
              Expanded(
                child: _buildCenterTextButton(
                  context,
                  label: 'Əsas səhifə',
                  onTap: () => onTap(1),
                  fontSize: fontSize,
                ),
              ),
              // Transfers button (right)
              Expanded(
                child: _buildNavButton(
                  context,
                  icon: Icons.swap_horiz,
                  label: 'Köçürmələr',
                  isSelected: isTransfersSelected,
                  onTap: () => onTap(2),
                  iconSize: iconSize,
                  fontSize: fontSize,
                ),
              ),
            ],
          ),
        ),
        // Center elevated circular button (above the bottom bar)
        Positioned(
          left: MediaQuery.of(context).size.width / 2 - centerButtonSize / 2,
          bottom: bottomNavHeight + centerButtonMarginBottom,
          child: GestureDetector(
            onTap: () => onTap(1),
            child: Container(
              width: centerButtonSize,
              height: centerButtonSize,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: isHomeSelected
                    ? AppTheme.colorAccent
                    : AppTheme.colorPrimary,
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.3),
                    blurRadius: centerButtonElevation,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              padding: const EdgeInsets.all(11.0), // 11dp padding
              child: Image.asset(
                'assets/images/ic_btb.png',
                width: 29.0,
                height: 29.0,
                color: isHomeSelected
                    ? AppTheme.colorPrimary // White when selected
                    : AppTheme.colorAccent, // Red when not selected
                errorBuilder: (context, error, stackTrace) {
                  // Fallback to icon if image not found
                  return Icon(
                    Icons.local_fire_department,
                    color: isHomeSelected
                        ? AppTheme.colorPrimary // White when selected
                        : AppTheme.colorAccent, // Red when not selected
                    size: 29.0,
                  );
                },
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildNavButton(
    BuildContext context, {
    required IconData icon,
    required String label,
    required bool isSelected,
    required VoidCallback onTap,
    required double iconSize,
    required double fontSize,
  }) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 8.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              size: iconSize,
              color: isSelected
                  ? AppTheme.colorPrimaryDark
                  : AppTheme.bottomBarMenuItemTint,
            ),
            const SizedBox(height: 8.0), // drawablePadding (8dp)
            Text(
              label,
              style: TextStyle(
                fontSize: fontSize,
                color: AppTheme.textColor, // Always black text
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCenterTextButton(
    BuildContext context, {
    required String label,
    required VoidCallback onTap,
    required double fontSize,
  }) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.only(top: 24.0, bottom: 8.0), // Increased top padding to add more space between icon and text
        child: Text(
          label,
          style: TextStyle(
            fontSize: fontSize,
            color: AppTheme.textColor, // Always black text
          ),
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}

