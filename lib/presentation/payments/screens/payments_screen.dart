import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';
import '../../core/widgets/app_app_bar.dart';
import '../widgets/payment_provider_group_item.dart';

class PaymentsScreen extends StatelessWidget {
  const PaymentsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Custom App Bar
        Container(
          padding: const EdgeInsets.only(top: 16.0),
          height: kToolbarHeight + 16.0,
          color: AppTheme.white,
          child: Row(
            children: [
              // Hamburger menu
              IconButton(
                icon: const Icon(Icons.menu),
                color: AppTheme.bottomBarMenuItemTint,
                onPressed: () {
                  Scaffold.of(context).openDrawer();
                },
              ),
              // Title
              const Expanded(
                child: Padding(
                  padding: EdgeInsets.only(top: 4.0),
                  child: Text(
                    'Ödənişlər',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 20.0,
                      fontWeight: FontWeight.w600,
                      color: AppTheme.textColor,
                    ),
                  ),
                ),
              ),
              // QR Code icon
              IconButton(
                icon: const Icon(Icons.qr_code_scanner),
                onPressed: () {
                  // Navigate to QR code scanner
                  // TODO: Implement QR code scanning
                },
              ),
            ],
          ),
        ),
        // Grid content
        Expanded(
          child: Container(
            color: AppTheme.white,
            padding: const EdgeInsets.all(16.0),
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3,
                crossAxisSpacing: 8.0,
                mainAxisSpacing: 8.0,
                childAspectRatio: 0.9, // Increased to give more vertical space
              ),
              itemCount: _paymentProviderGroups.length,
              itemBuilder: (context, index) {
                final group = _paymentProviderGroups[index];
                return PaymentProviderGroupItem(
                  name: group['name'] as String,
                  icon: group['icon'] as IconData,
                  onTap: () {
                    // Navigate to payment providers screen
                    // TODO: Implement navigation to providers
                  },
                );
              },
            ),
          ),
        ),
      ],
    );
  }

  // Payment provider groups data matching the image
  static final List<Map<String, dynamic>> _paymentProviderGroups = [
    {
      'name': 'Mobil',
      'icon': Icons.smartphone,
    },
    {
      'name': 'Kommunal',
      'icon': Icons.home,
    },
    {
      'name': 'Bank',
      'icon': Icons.account_balance,
    },
    {
      'name': 'TV',
      'icon': Icons.tv,
    },
    {
      'name': 'Telefon',
      'icon': Icons.phone,
    },
    {
      'name': 'Internet',
      'icon': Icons.language,
    },
    {
      'name': 'Xeyriyyəçilik',
      'icon': Icons.favorite,
    },
    {
      'name': 'Cərimələr',
      'icon': Icons.gavel,
    },
    {
      'name': 'Büdcə',
      'icon': Icons.account_balance_wallet,
    },
    {
      'name': 'Sığorta',
      'icon': Icons.shield,
    },
    {
      'name': 'Təhsil',
      'icon': Icons.school,
    },
  ];
}

