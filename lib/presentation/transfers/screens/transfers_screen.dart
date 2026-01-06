import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';
import '../widgets/transfer_option_item.dart';

class TransfersScreen extends StatelessWidget {
  const TransfersScreen({super.key});

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
                    'Köçürmələr',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontSize: 20.0,
                      fontWeight: FontWeight.w600,
                      color: AppTheme.textColor,
                    ),
                  ),
                ),
              ),
              // Bell/Notification icon
              IconButton(
                icon: const Icon(Icons.notifications_outlined),
                color: AppTheme.bottomBarMenuItemTint,
                onPressed: () {
                  // Navigate to notifications
                  // TODO: Implement notifications
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
                crossAxisCount: 2,
                crossAxisSpacing: 10.0,
                mainAxisSpacing: 10.0,
                childAspectRatio: 1.1, // Adjusted for card height
              ),
              itemCount: _transferOptions.length,
              itemBuilder: (context, index) {
                final option = _transferOptions[index];
                return TransferOptionItem(
                  name: option['name'] as String,
                  icon: option['icon'] as IconData,
                  onTap: () {
                    // Navigate to specific transfer screen
                    // TODO: Implement navigation
                  },
                );
              },
            ),
          ),
        ),
      ],
    );
  }

  // Transfer options data matching the image
  static final List<Map<String, dynamic>> _transferOptions = [
    {
      'name': 'Kart və hesablarım arasında',
      'icon': Icons.swap_horiz,
    },
    {
      'name': 'Ölkədaxili istənilən bank kartına köçürmə',
      'icon': Icons.credit_card,
    },
    {
      'name': 'Təcili pul köçürmələri',
      'icon': Icons.check_circle,
    },
    {
      'name': 'Ölkə daxili köçürmələr',
      'icon': Icons.language,
    },
    {
      'name': 'Xarici valyutada köçürmələr',
      'icon': Icons.attach_money,
    },
  ];
}

