import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class AppDrawer extends StatelessWidget {
  final String? userName;
  final Function(String)? onItemSelected;

  const AppDrawer({
    super.key,
    this.userName,
    this.onItemSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: AppTheme.white,
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          // Header with BANK BTB logo
          Container(
            height: 120.0,
            padding: const EdgeInsets.only(top: 50.0, bottom: 16.0),
            child: Center(
              child: _BankBtbLogoWidget(),
            ),
          ),
          // User profile section
          if (userName != null)
            ListTile(
              leading: Icon(
                Icons.person,
                color: AppTheme.colorPrimaryDark,
                size: 24.0,
              ),
              title: Text(
                userName!,
                style: const TextStyle(
                  fontSize: 16.0,
                  fontWeight: FontWeight.w600,
                  color: AppTheme.textColor,
                ),
              ),
              onTap: () => onItemSelected?.call('profile'),
            ),
          const Divider(),
          // Main page
          _DrawerMenuItem(
            icon: Icons.home,
            title: 'Əsas səhifə',
            onTap: () => onItemSelected?.call('mainPage'),
          ),
          const Divider(),
          // Mənim vəsaitlərim section
          _DrawerSectionHeader(title: 'Mənim vəsaitlərim'),
          _DrawerMenuItem(
            icon: Icons.credit_card,
            title: 'Kartlar',
            onTap: () => onItemSelected?.call('cards'),
          ),
          _DrawerMenuItem(
            icon: Icons.account_balance,
            title: 'Hesablar',
            onTap: () => onItemSelected?.call('accounts'),
          ),
          _DrawerMenuItem(
            icon: Icons.percent,
            title: 'Kreditlər',
            onTap: () => onItemSelected?.call('loans'),
          ),
          _DrawerMenuItem(
            icon: Icons.account_balance_wallet,
            title: 'Əmanətlər',
            onTap: () => onItemSelected?.call('deposits'),
          ),
          const Divider(),
          // Ödənişlər və köçürmələr section
          _DrawerSectionHeader(title: 'Ödənişlər və köçürmələr'),
          _DrawerMenuItem(
            icon: Icons.account_balance_wallet,
            title: 'Ödənişlər',
            onTap: () => onItemSelected?.call('payments'),
          ),
          _DrawerMenuItem(
            icon: Icons.swap_horiz,
            title: 'Köçürmələr',
            onTap: () => onItemSelected?.call('transfers'),
          ),
          _DrawerMenuItem(
            icon: Icons.history,
            title: 'Tarixçə',
            onTap: () => onItemSelected?.call('history'),
          ),
          const Divider(),
          // Bank haqqında section
          _DrawerSectionHeader(title: 'Bank haqqında'),
          _DrawerMenuItem(
            icon: Icons.business,
            title: 'Bank məhsulları',
            onTap: () => onItemSelected?.call('products'),
          ),
          _DrawerMenuItem(
            icon: Icons.currency_exchange,
            title: 'Valyuta məzənnələri',
            onTap: () => onItemSelected?.call('exchangeRates'),
          ),
          _DrawerMenuItem(
            icon: Icons.article,
            title: 'Bank xəbərləri',
            onTap: () => onItemSelected?.call('news'),
          ),
          _DrawerMenuItem(
            icon: Icons.location_on,
            title: 'Xidmət nöqtələrimiz',
            onTap: () => onItemSelected?.call('servicePoints'),
          ),
          _DrawerMenuItem(
            icon: Icons.phone,
            title: 'Bankla əlaqə',
            onTap: () => onItemSelected?.call('contacts'),
          ),
          const Divider(),
          // Digər section
          _DrawerSectionHeader(title: 'Digər'),
          _DrawerMenuItem(
            icon: Icons.settings,
            title: 'Tənzimləmələr',
            onTap: () => onItemSelected?.call('settings'),
          ),
          _DrawerMenuItem(
            icon: Icons.logout,
            title: 'Çıxış',
            textColor: Colors.red,
            iconColor: Colors.red,
            onTap: () => onItemSelected?.call('logout'),
          ),
        ],
      ),
    );
  }
}

class _DrawerSectionHeader extends StatelessWidget {
  final String title;

  const _DrawerSectionHeader({required this.title});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
      child: Text(
        title,
        style: const TextStyle(
          fontSize: 14.0,
          fontWeight: FontWeight.w600,
          color: AppTheme.colorPrimaryDark, // Red
        ),
      ),
    );
  }
}

class _DrawerMenuItem extends StatelessWidget {
  final IconData icon;
  final String title;
  final VoidCallback onTap;
  final Color? iconColor;
  final Color? textColor;

  const _DrawerMenuItem({
    required this.icon,
    required this.title,
    required this.onTap,
    this.iconColor,
    this.textColor,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(
        icon,
        color: iconColor ?? AppTheme.colorPrimaryDark, // Red icon
        size: 24.0,
      ),
      title: Text(
        title,
        style: TextStyle(
          fontSize: 16.0,
          color: textColor ?? AppTheme.textColor, // Black text
        ),
      ),
      onTap: () {
        Navigator.pop(context); // Close drawer
        onTap();
      },
    );
  }
}

class _BankBtbLogoWidget extends StatelessWidget {
  const _BankBtbLogoWidget();

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

