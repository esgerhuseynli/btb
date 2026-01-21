// import 'package:flutter/material.dart';
// import 'package:flutter_bloc/flutter_bloc.dart';
// import 'package:go_router/go_router.dart';
// import '../../../core/theme/app_theme.dart';
// import '../../../core/widgets/error_snackbar.dart';
// import '../../core/widgets/app_app_bar.dart';
// import '../../core/widgets/loading_widget.dart';
// import '../../core/widgets/error_widget.dart';
// import '../../core/widgets/logout_confirmation_dialog.dart';
// import '../../auth/bloc/auth_bloc.dart';
// import '../../auth/bloc/auth_event.dart';
// import '../../auth/bloc/auth_state.dart';
// import '../bloc/home_bloc.dart';
// import '../bloc/home_event.dart';
// import '../bloc/home_state.dart';
// import '../widgets/bank_card_account_item.dart';
// import '../widgets/home_item_view.dart';
// import '../widgets/page_indicator.dart';
// import '../widgets/custom_bottom_nav_bar.dart';
// import '../../payments/screens/payments_screen.dart';
// import '../../transfers/screens/transfers_screen.dart';
// import '../../core/widgets/home_app_bar.dart';
// import '../../core/widgets/app_drawer.dart';
// import '../../../injection/injection.dart';
// import '../../../data/models/bank_card.dart';
// import '../../../data/models/bank_account.dart';
//
// class HomeScreen extends StatefulWidget {
//   const HomeScreen({super.key});
//
//   @override
//   State<HomeScreen> createState() => _HomeScreenState();
// }
//
// class _HomeScreenState extends State<HomeScreen> {
//   // Navigation indices: 0 = Payments, 1 = Home, 2 = Transfers (matching Android)
//   int _selectedIndex = 1; // Start with Home tab (center button)
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: _selectedIndex == 1
//           ? PreferredSize(
//               preferredSize: const Size.fromHeight(kToolbarHeight + 8.0),
//               child: Container(
//                 padding: const EdgeInsets.only(top: 8.0),
//                 child: const HomeAppBar(),
//               ),
//             )
//           : null, // Payments and Transfers tabs have their own app bars
//       drawer: AppDrawer(
//         userName: 'Elşad', // TODO: Get from user data
//         onItemSelected: (item) {
//           _handleDrawerItemSelected(context, item);
//         },
//       ),
//       body: IndexedStack(
//         index: _selectedIndex,
//         children: const [
//           _PaymentsTab(), // Index 0
//           _HomeTab(),     // Index 1
//           _TransfersTab(), // Index 2
//         ],
//       ),
//       bottomNavigationBar: CustomBottomNavBar(
//         currentIndex: _selectedIndex,
//         onTap: (index) {
//           setState(() {
//             _selectedIndex = index;
//           });
//         },
//       ),
//     );
//   }
//
//   void _handleDrawerItemSelected(BuildContext context, String item) {
//     switch (item) {
//       case 'mainPage':
//         setState(() {
//           _selectedIndex = 1; // Home tab
//         });
//         break;
//       case 'payments':
//         setState(() {
//           _selectedIndex = 0; // Payments tab
//         });
//         break;
//       case 'transfers':
//         setState(() {
//           _selectedIndex = 2; // Transfers tab
//         });
//         break;
//       case 'logout':
//         // TODO: Show logout confirmation dialog
//         break;
//       // TODO: Handle other menu items
//       default:
//         break;
//     }
//   }
// }
//
// class _HomeTab extends StatefulWidget {
//   const _HomeTab();
//
//   @override
//   State<_HomeTab> createState() => _HomeTabState();
// }
//
// class _HomeTabState extends State<_HomeTab> {
//   final PageController _pageController = PageController();
//   int _currentPage = 0;
//
//   @override
//   void dispose() {
//     _pageController.dispose();
//     super.dispose();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return BlocProvider(
//       create: (_) => getIt<HomeBloc>()
//         ..add(const InitializeHomeEvent()),
//       child: BlocBuilder<HomeBloc, HomeState>(
//         builder: (context, state) {
//           if (state is HomeLoading) {
//             return const LoadingWidget();
//           }
//
//           if (state is HomeError) {
//             return ErrorDisplayWidget(
//               message: state.message,
//               onRetry: () {
//                 context.read<HomeBloc>().add(const RefreshHomeDataEvent());
//               },
//             );
//           }
//
//           if (state is HomeLoaded) {
//             // Combine cards and accounts into a single list
//             final List<dynamic> allItems = [
//               ...state.bankCards,
//               ...state.bankAccounts,
//             ];
//
//             if (allItems.isEmpty) {
//               return Center(
//                 child: Text(
//                   'Kart və hesab tapılmadı',
//                   style: Theme.of(context).textTheme.bodyMedium,
//                 ),
//               );
//             }
//
//             // Determine if current item is a card
//             final currentItem = allItems[_currentPage];
//             final isCard = currentItem is BankCard;
//
//             return Column(
//               children: [
//                 // Card layout section (matching Android cardLayout)
//                 Container(
//                   margin: const EdgeInsets.only(top: 50),
//                   color: AppTheme.white,
//                   child: Column(
//                     children: [
//                       // Horizontal PageView for cards/accounts
//                       SizedBox(
//                         height: 180,
//                         child: PageView.builder(
//                           controller: _pageController,
//                           onPageChanged: (index) {
//                             setState(() {
//                               _currentPage = index;
//                             });
//                           },
//                           itemCount: allItems.length,
//                           itemBuilder: (context, index) {
//                             final item = allItems[index];
//                             return Center(
//                               child: BankCardAccountItem(
//                                 item: item,
//                                 isCardItem: item is BankCard,
//                               ),
//                             );
//                           },
//                         ),
//                       ),
//                       // Page indicator
//                       PageIndicator(
//                         currentPage: _currentPage,
//                         pageCount: allItems.length,
//                       ),
//                       // Action buttons
//                       Padding(
//                         padding: const EdgeInsets.symmetric(vertical: 10),
//                         child: Row(
//                           mainAxisAlignment: MainAxisAlignment.center,
//                           children: [
//                             Expanded(
//                               child: _buildActionButton(
//                                 context,
//                                 icon: 'assets/images/ic_card_details.png',
//                                 label: 'Ətraflı məlumat',
//                                 onTap: () {
//                                   // Navigate to detailed info
//                                 },
//                               ),
//                             ),
//                             if (isCard)
//                               Expanded(
//                                 child: _buildActionButton(
//                                   context,
//                                   icon: 'assets/images/ic_card_statement.png',
//                                   label: 'Kartdan çıxarış',
//                                   onTap: () {
//                                     // Navigate to card statement
//                                   },
//                                 ),
//                               ),
//                           ],
//                         ),
//                       ),
//                     ],
//                   ),
//                 ),
//                 // Scrollable operations section
//                 Expanded(
//                   child: Container(
//                     color: AppTheme.mainBackground,
//                     margin: const EdgeInsets.only(bottom: 72),
//                     child: SingleChildScrollView(
//                       child: Column(
//                         crossAxisAlignment: CrossAxisAlignment.start,
//                         children: [
//                           // Operations title
//                           Padding(
//                             padding: const EdgeInsets.only(
//                               top: 10,
//                               left: 20,
//                             ),
//                             child: Text(
//                               'Əməliyyatlar',
//                               style: TextStyle(
//                                 fontSize: 17,
//                                 color: AppTheme.colorPrimaryDark,
//                                 fontWeight: FontWeight.w600,
//                               ),
//                             ),
//                           ),
//                           // Operations list
//                           if (isCard) ...[
//                             HomeItemView(
//                               text: 'QR Pay',
//                               iconPath: 'assets/images/ic_qr_code.png',
//                               backgroundColor: const Color(0xFF757575),
//                               onTap: () {
//                                 // Navigate to QR payment
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Ödənişlər',
//                               iconPath: 'assets/images/ic_payments.png',
//                               onTap: () {
//                                 // Navigate to payments
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Kart və hesablarım arasında',
//                               iconPath: 'assets/images/ic_between_mine.png',
//                               onTap: () {
//                                 // Navigate to transfers
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Ölkədaxili istənilən bank kartına köçürmə',
//                               iconPath: 'assets/images/ic_other_card.png',
//                               onTap: () {
//                                 // Navigate to card transfer
//                               },
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.only(
//                                 top: 10,
//                                 left: 20,
//                               ),
//                               child: Text(
//                                 'Ölkə xarici',
//                                 style: TextStyle(
//                                   fontSize: 17,
//                                   color: AppTheme.colorPrimaryDark,
//                                   fontWeight: FontWeight.w600,
//                                 ),
//                               ),
//                             ),
//                             HomeItemView(
//                               text: 'Təcili pul köçürmələri',
//                               iconPath: 'assets/images/ic_money_transfer.png',
//                               onTap: () {
//                                 // Navigate to money transfer
//                               },
//                             ),
//                           ] else ...[
//                             HomeItemView(
//                               text: 'QR Pay',
//                               iconPath: 'assets/images/ic_qr_code.png',
//                               onTap: () {
//                                 // Navigate to QR payment
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Ödənişlər',
//                               iconPath: 'assets/images/ic_payments.png',
//                               onTap: () {
//                                 // Navigate to payments
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Kart və hesablarım arasında',
//                               iconPath: 'assets/images/ic_between_mine.png',
//                               onTap: () {
//                                 // Navigate to transfers
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Ölkə daxili köçürmələr',
//                               iconPath: 'assets/images/ic_history_local_transfers_gray.png',
//                               onTap: () {
//                                 // Navigate to local transfers
//                               },
//                             ),
//                             HomeItemView(
//                               text: 'Xarici valyutada köçürmələr',
//                               iconPath: 'assets/images/ic_history_international_transfers_gray.png',
//                               onTap: () {
//                                 // Navigate to international transfers
//                               },
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.only(
//                                 top: 10,
//                                 left: 20,
//                               ),
//                               child: Text(
//                                 'Ölkə xarici',
//                                 style: TextStyle(
//                                   fontSize: 17,
//                                   color: AppTheme.colorPrimaryDark,
//                                   fontWeight: FontWeight.w600,
//                                 ),
//                               ),
//                             ),
//                             HomeItemView(
//                               text: 'Təcili pul köçürmələri',
//                               iconPath: 'assets/images/ic_money_transfer.png',
//                               onTap: () {
//                                 // Navigate to money transfer
//                               },
//                             ),
//                           ],
//                         ],
//                       ),
//                     ),
//                   ),
//                 ),
//               ],
//             );
//           }
//
//           return const SizedBox.shrink();
//         },
//       ),
//     );
//   }
//
//   Widget _buildActionButton(
//       BuildContext context, {
//         required String icon,
//         required String label,
//         required VoidCallback onTap,
//       }) {
//     return InkWell(
//       onTap: onTap,
//       child: Container(
//         padding: const EdgeInsets.symmetric(horizontal: 24),
//         child: Column(
//           children: [
//             Container(
//               width: 45,
//               height: 45,
//               decoration: BoxDecoration(
//                 color: AppTheme.mainColor,
//                 borderRadius: BorderRadius.circular(8),
//               ),
//               padding: const EdgeInsets.all(11),
//               child: Image.asset(
//                 icon,
//                 errorBuilder: (context, error, stackTrace) {
//                   return const Icon(
//                     Icons.info_outline,
//                     color: Colors.white,
//                     size: 20,
//                   );
//                 },
//               ),
//             ),
//             const SizedBox(height: 4),
//             Text(
//               label,
//               style: const TextStyle(
//                 fontSize: 13,
//                 color: AppTheme.textColor,
//               ),
//               textAlign: TextAlign.center,
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }
//
// class _TransfersTab extends StatelessWidget {
//   const _TransfersTab();
//
//   @override
//   Widget build(BuildContext context) {
//     return const TransfersScreen();
//   }
// }
//
// class _PaymentsTab extends StatelessWidget {
//   const _PaymentsTab();
//
//   @override
//   Widget build(BuildContext context) {
//     return const PaymentsScreen();
//   }
// }
//
// class _OperationsTab extends StatelessWidget {
//   const _OperationsTab();
//
//   @override
//   Widget build(BuildContext context) {
//     return const Center(child: Text('Əməliyyatlar'));
//   }
// }
//
// class _MoreTab extends StatelessWidget {
//   const _MoreTab();
//
//   @override
//   Widget build(BuildContext context) {
//     return BlocListener<AuthBloc, AuthState>(
//       listener: (context, state) {
//         if (state is AuthUnauthenticated) {
//           // Navigate to phone entry screen and clear navigation stack
//           context.go('/phone-entry');
//         } else if (state is AuthError) {
//           // Show error but still navigate to phone entry (Android behavior)
//           ErrorSnackBar.show(context, state.message);
//           context.go('/phone-entry');
//         }
//       },
//       child: SingleChildScrollView(
//         padding: const EdgeInsets.all(16.0),
//         child: Column(
//           crossAxisAlignment: CrossAxisAlignment.start,
//           children: [
//             Text(
//               'Parametrlər',
//               style: Theme.of(context).textTheme.headlineSmall,
//             ),
//             const SizedBox(height: 24),
//             _buildMenuItem(
//               context,
//               icon: Icons.settings,
//               title: 'Tənzimləmələr',
//               onTap: () {
//                 // Navigate to settings screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.person,
//               title: 'Profil',
//               onTap: () {
//                 // Navigate to profile screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.credit_card,
//               title: 'Kartlarım',
//               onTap: () {
//                 // Navigate to cards screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.account_balance,
//               title: 'Hesablarım',
//               onTap: () {
//                 // Navigate to accounts screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.history,
//               title: 'Əməliyyat tarixçəsi',
//               onTap: () {
//                 // Navigate to operations history screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.help_outline,
//               title: 'Kömək',
//               onTap: () {
//                 // Navigate to help screen
//               },
//             ),
//             const Divider(),
//             _buildMenuItem(
//               context,
//               icon: Icons.info_outline,
//               title: 'Haqqında',
//               onTap: () {
//                 // Navigate to about screen
//               },
//             ),
//             const SizedBox(height: 24),
//             const Divider(),
//             _buildLogoutButton(context),
//           ],
//         ),
//       ),
//     );
//   }
//
//   Widget _buildMenuItem(
//       BuildContext context, {
//         required IconData icon,
//         required String title,
//         required VoidCallback onTap,
//       }) {
//     return ListTile(
//       leading: Icon(icon, color: AppTheme.mainColor),
//       title: Text(title),
//       trailing: const Icon(Icons.chevron_right),
//       onTap: onTap,
//     );
//   }
//
//   Widget _buildLogoutButton(BuildContext context) {
//     return BlocBuilder<AuthBloc, AuthState>(
//       builder: (context, state) {
//         final isLoading = state is AuthLoading;
//
//         return ListTile(
//           leading: Icon(
//             Icons.logout,
//             color: Colors.red,
//           ),
//           title: Text(
//             'Çıxış',
//             style: TextStyle(color: Colors.red),
//           ),
//           enabled: !isLoading,
//           onTap: () => _showLogoutConfirmationDialog(context),
//         );
//       },
//     );
//   }
//
//   void _showLogoutConfirmationDialog(BuildContext context) {
//     showDialog(
//       context: context,
//       barrierDismissible: false, // Cannot dismiss by tapping outside
//       builder: (context) => LogoutConfirmationDialog(
//         onYes: () {
//           Navigator.of(context).pop(); // Close dialog
//           // Trigger logout
//           context.read<AuthBloc>().add(const SignOutEvent());
//         },
//         onNo: () {
//           Navigator.of(context).pop(); // Close dialog
//         },
//       ),
//     );
//   }
// }
