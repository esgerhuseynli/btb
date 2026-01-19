import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_svg/flutter_svg.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/theme/app_text_styles.dart';
import '../../../../core/localization/app_localizations_ext.dart';
import '../../../../l10n/app_localizations.dart';
import '../../../../presentation/core/widgets/error_widget.dart';
import '../../widgets/bank_card_account_item.dart';
import '../../widgets/page_indicator.dart';
import '../../../../data/models/bank_card.dart';
import '../../../../data/models/bank_account.dart';
import '../../../../data/models/transaction_data.dart';
import '../../bloc/home_bloc.dart';
import '../../bloc/home_event.dart';
import '../../bloc/home_state.dart';
import '../../../../injection/injection.dart';

class HomePageScreen extends StatefulWidget {
  const HomePageScreen({super.key});

  @override
  State<HomePageScreen> createState() => _HomePageScreenState();
}

class _HomePageScreenState extends State<HomePageScreen> {
  final PageController _pageController = PageController(viewportFraction: 0.85);
  int _currentPage = 0;
  bool _isBalanceVisible = true;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final localizations = context.l10n;
    return BlocProvider(
      create: (_) {
        final bloc = getIt<HomeBloc>();
        // Initialize home immediately - don't wait for postFrameCallback
        // InitializeHomeEvent already loads all data including transactions
        bloc.add(const InitializeHomeEvent());
        return bloc;
      },
      child: BlocBuilder<HomeBloc, HomeState>(
        builder: (context, state) {
          // Debug: Log state changes
          debugPrint('BlocBuilder rebuild - State: ${state.runtimeType}');
          if (state is HomeLoaded) {
            debugPrint('BlocBuilder - HomeLoaded: ${state.bankCards.length} cards, ${state.bankAccounts.length} accounts, balance: ${state.totalBalance}');
          }
          
          final isLoading = state is HomeLoading || state is HomeInitial;
          
          // Show loading indicator until API response is received
          if (isLoading) {
            return Scaffold(
              backgroundColor: AppTheme.homeBackground,
              body: Center(
                child: CircularProgressIndicator(
                  valueColor: AlwaysStoppedAnimation<Color>(AppTheme.mainColor),
                  strokeWidth: 3.0,
                ),
              ),
            );
          }
          
          // Handle error state
          if (state is HomeError) {
            return Scaffold(
              backgroundColor: AppTheme.homeBackground,
              body: ErrorDisplayWidget(
                message: state.message,
                onRetry: () {
                  context.read<HomeBloc>().add(const InitializeHomeEvent());
                },
              ),
            );
          }
          
          return Scaffold(
            backgroundColor: AppTheme.homeBackground,
            body: SafeArea(
              child: CustomScrollView(
                slivers: [
                  // Top section with user info and balance
                  SliverToBoxAdapter(
                    child: _buildTopSection(context, state),
                  ),
                  
                  // Cards section
                  SliverToBoxAdapter(
                    child: Padding(
                      padding: EdgeInsets.only(top: 8.h),
                      child: _buildCardsSection(context, state),
                    ),
                  ),
                  
                  // Action buttons
                  SliverToBoxAdapter(
                    child: _buildActionButtons(context),
                  ),
                  
                  // Transactions section
                  _buildTransactionsSliver(context, state),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildTopSection(BuildContext context, HomeState state) {
    final userName = state is HomeLoaded ? state.userName : '';
    final userAvatarUrl = state is HomeLoaded ? state.userAvatarUrl : null;
    final totalBalance = state is HomeLoaded ? state.totalBalance : 0.0;

    return Padding(
      padding: EdgeInsets.only(
        left: 16.w,
        right: 16.w,
        top: 10.h,
        bottom: 24.h,
      ),
      child: Column(
        children: [
          // User profile row
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              // User info with welcome message
              Row(
                children: [
                  // Avatar
                  Container(
                    width: 44.w,
                    height: 44.w,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: Colors.grey[300],
                    ),
                    child: userAvatarUrl != null && userAvatarUrl.isNotEmpty
                        ? ClipOval(
                            child: Image.network(
                              userAvatarUrl,
                              fit: BoxFit.cover,
                              errorBuilder: (context, error, stackTrace) {
                                return Icon(
                                  Icons.person,
                                  size: 24.sp,
                                  color: Colors.grey[600],
                                );
                              },
                            ),
                          )
                        : Icon(
                            Icons.person,
                            size: 24.sp,
                            color: Colors.grey[600],
                          ),
                  ),
                  SizedBox(width: 12.w),
                  // Welcome message and name
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        context.l10n.welcomeBack,
                        style: AppTextStyles.welcomeText(context),
                      ),
                      SizedBox(height: 2.h),
                      Text(
                        userName.isNotEmpty ? userName : 'User',
                        style: AppTextStyles.userName(context),
                      ),
                    ],
                  ),
                ],
              ),
              // Notification icon
              Stack(
                clipBehavior: Clip.none,
                children: [
                  Icon(
                    Icons.notifications_outlined,
                    size: 24.sp,
                    color: AppTheme.textColor,
                  ),
                  Positioned(
                    right: -2,
                    top: -2,
                    child: Container(
                      width: 8.w,
                      height: 8.w,
                      decoration: const BoxDecoration(
                        color: AppTheme.mainColor,
                        shape: BoxShape.circle,
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
          
          SizedBox(height: 24.h),
          
          // Balance section
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Text(
                    context.l10n.yourBalance,
                    style: AppTextStyles.balanceLabel(context),
                  ),
                  SizedBox(width: 8.w),
                  Semantics(
                    label: _isBalanceVisible
                        ? 'Hide balance'
                        : 'Show balance',
                    button: true,
                    child: GestureDetector(
                      onTap: () {
                        setState(() {
                          _isBalanceVisible = !_isBalanceVisible;
                        });
                      },
                      child: Icon(
                        _isBalanceVisible
                            ? Icons.visibility_outlined
                            : Icons.visibility_off_outlined,
                        size: 24.sp,
                        color: AppTheme.iconSecondary,
                      ),
                    ),
                  ),
                ],
              ),
              SizedBox(height: 8.h),
              Text(
                _isBalanceVisible
                    ? '₼${totalBalance.toStringAsFixed(2)}'
                    : '₼••••',
                style: AppTextStyles.balanceAmount(context),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildCardsSection(BuildContext context, HomeState state) {
    // Get cards and accounts from state
    List<BankCard> cards = const [];
    List<BankAccount> accounts = const [];
    bool isLoading = state is HomeLoading || state is HomeInitial;
    bool isLoaded = state is HomeLoaded;
    
    if (state is HomeLoaded) {
      cards = state.bankCards;
      accounts = state.bankAccounts;
      // Debug logging
      debugPrint('_buildCardsSection - State: HomeLoaded, cards: ${cards.length}, accounts: ${accounts.length}');
      if (cards.isNotEmpty) {
        debugPrint('_buildCardsSection - First card: ${cards.first.cardNumber}, balance: ${cards.first.cardBalance}');
      }
    } else if (state is HomeLoading) {
      debugPrint('_buildCardsSection - State: HomeLoading');
    } else if (state is HomeError) {
      debugPrint('_buildCardsSection - State: HomeError - ${(state as HomeError).message}');
    } else {
      debugPrint('_buildCardsSection - State: ${state.runtimeType}');
    }
    
    final allItems = [...cards, ...accounts];
    debugPrint('_buildCardsSection - allItems.length: ${allItems.length}, isLoading: $isLoading, isLoaded: $isLoaded');
    // Don't show sample cards - show loading indicator instead
    final itemCount = allItems.isEmpty ? 0 : allItems.length;
    debugPrint('_buildCardsSection - itemCount: $itemCount');

    return Column(
      children: [
        if (itemCount > 0)
          SizedBox(
            height: 157.39.h,
            child: PageView.builder(
              controller: _pageController,
              onPageChanged: (index) {
                setState(() {
                  _currentPage = index;
                });
              },
              itemCount: itemCount,
              itemBuilder: (context, index) {
                final item = allItems[index];
                return Center(
                  child: Padding(
                    padding: EdgeInsets.symmetric(horizontal: 8.w),
                    child: BankCardAccountItem(
                      item: item,
                      isCardItem: item is BankCard,
                    ),
                  ),
                );
              },
            ),
          )
        else if (isLoaded && allItems.isEmpty)
          SizedBox(
            height: 157.39.h,
            child: Center(
              child: Text(
                'No cards or accounts available',
                style: AppTextStyles.emptyStateText(context),
              ),
            ),
          ),
        if (itemCount > 0) ...[
          SizedBox(height: 20.h),
          PageIndicator(
            currentPage: _currentPage,
            pageCount: itemCount,
          ),
        ],
      ],
    );
  }


  /// Reusable card container widget to reduce duplication
  Widget _buildCardContainer({
    required Gradient gradient,
    required Widget child,
    double? width,
  }) {
    return Container(
      width: width ?? 276.w,
      height: 157.39.h,
      padding: EdgeInsets.only(
        top: 25.36.h,
        right: 18.65.w,
        bottom: 25.36.h,
        left: 18.65.w,
      ),
      decoration: BoxDecoration(
        gradient: gradient,
        borderRadius: BorderRadius.circular(12.r),
      ),
      child: child,
    );
  }

  /// Common header row for bank cards (BTB BANK + WiFi icon)
  Widget _buildCardHeader() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'BTB BANK',
          style: AppTextStyles.cardBankName(context),
        ),
        Transform.rotate(
          angle: 1.51, // ~86.5 degrees in radians
          child: Icon(
            Icons.wifi,
            size: 20.sp,
            color: Colors.white,
          ),
        ),
      ],
    );
  }

  Widget _buildSampleCard(bool isFirst) {
    if (isFirst) {
      // Red card (AZN) - matching Figma design
      return _buildCardContainer(
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            AppTheme.cardRedGradientStart,
            AppTheme.cardRedGradientEnd,
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            _buildCardHeader(),
            SizedBox(height: 7.46.h), // Gap from layout
            // Card number
            Row(
              children: [
                Text(
                  '4725',
                  style: AppTextStyles.cardNumber(context),
                ),
                SizedBox(width: 8.w),
                Text(
                  '3428',
                  style: AppTextStyles.cardNumber(context),
                ),
              ],
            ),
            SizedBox(height: 7.46.h), // Gap from layout
            // Bottom row: Balance and VISA logo
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  '452.40 AZN',
                  style: AppTextStyles.cardBalance(context),
                ),
                Container(
                  width: 50.w,
                  height: 20.h,
                  decoration: BoxDecoration(
                    color: Colors.white.withValues(alpha: 0.2),
                    borderRadius: BorderRadius.circular(4.r),
                  ),
                  child: Center(
                    child: Text(
                      'VISA',
                      style: AppTextStyles.cardVisaLabel(context),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      );
    } else {
      // Purple card (USD) - matching Figma design
      return _buildCardContainer(
        width: 200.w,
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            AppTheme.cardPurpleGradientStart,
            AppTheme.cardPurpleGradientEnd,
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            _buildCardHeader(),
            SizedBox(height: 7.46.h), // Gap from layout
            // Balance (center)
            Text(
              '0,00 USD',
              style: AppTextStyles.cardBalance(context, fontFamily: 'SF Pro').copyWith(
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 7.46.h), // Gap from layout
            // Account number and type (bottom)
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Flexible(
                  child: Text(
                    '41020840030558008000',
                    style: AppTextStyles.cardNumber(context, fontFamily: 'SF Pro'),
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
                SizedBox(width: 8.w),
                Text(
                  context.l10n.currentAccountUsd,
                  style: AppTextStyles.cardNumber(context, fontFamily: 'SF Pro').copyWith(
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ],
            ),
          ],
        ),
      );
    }
  }

  Widget _buildActionButtons(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 16.w, vertical: 36.h),
      padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 20.h),
      decoration: BoxDecoration(
        color: Colors.transparent,
        borderRadius: BorderRadius.circular(14.r),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Expanded(
            child: _buildActionButton(
              iconPath: 'assets/icons/payments.svg',
              label: context.l10n.payments,
              onTap: () {
                // Navigate to payments
              },
            ),
          ),
          SizedBox(width: 12.w),
          Expanded(
            child: _buildActionButton(
              iconPath: 'assets/icons/transfer.svg',
              label: context.l10n.transfers,
              onTap: () {
                // Navigate to transfers
              },
            ),
          ),
          SizedBox(width: 12.w),
          Expanded(
            child: _buildActionButton(
              iconPath: 'assets/icons/statements.svg',
              label: context.l10n.statement,
              onTap: () {
                // Navigate to statement
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildActionButton({
    required String iconPath,
    required String label,
    required VoidCallback onTap,
  }) {
    return Semantics(
      label: label,
      button: true,
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          height: 104.h,
          padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 14.h),
          decoration: BoxDecoration(
            gradient: const LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [
                Colors.white,
                AppTheme.buttonGradientEnd,
              ],
            ),
            border: Border.all(color: AppTheme.borderLightGray),
            borderRadius: BorderRadius.circular(20.r),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.08),
                blurRadius: 8,
                offset: const Offset(0, 2),
              ),
            ],
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            mainAxisSize: MainAxisSize.min,
            children: [
              Builder(
                builder: (context) {
                  if (iconPath.endsWith('.svg')) {
                    try {
                      return SvgPicture.asset(
                        iconPath,
                        width: 32.w,
                        height: 32.h,
                        fit: BoxFit.contain,
                      );
                    } catch (e) {
                      return Icon(
                        Icons.error_outline,
                        size: 32.sp,
                        color: AppTheme.mainColor,
                      );
                    }
                  } else {
                    return Image.asset(
                      iconPath,
                      width: 32.w,
                      height: 32.h,
                      errorBuilder: (context, error, stackTrace) {
                        return Icon(
                          Icons.error_outline,
                          size: 32.sp,
                          color: AppTheme.mainColor,
                        );
                      },
                    );
                  }
                },
              ),
              SizedBox(height: 8.h),
              FittedBox(
                fit: BoxFit.scaleDown,
                child: Text(
                  label.trim().replaceAll('.', ''),
                  style: AppTextStyles.actionButtonLabel(context),
                  textAlign: TextAlign.center,
                  maxLines: 1,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTransactionsSliver(BuildContext context, HomeState state) {
    final transactions = state is HomeLoaded ? state.transactions : <TransactionData>[];

    if (transactions.isEmpty) {
      return SliverFillRemaining(
        hasScrollBody: false,
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Transactions header
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    context.l10n.transactions,
                    style: AppTextStyles.transactionsHeader(context),
                  ),
                  Semantics(
                    label: 'View all transactions',
                    button: true,
                    child: GestureDetector(
                      onTap: () {
                        // Navigate to all transactions
                      },
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Text(
                            context.l10n.seeAll,
                            style: AppTextStyles.seeAllLink(context),
                          ),
                          Container(
                            width: 38.w,
                            height: 1.h,
                            color: AppTheme.textSecondaryGray,
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              SizedBox(height: 32.h),
              Center(
                child: Text(
                  'No transactions',
                  style: AppTextStyles.emptyStateText(context),
                ),
              ),
            ],
          ),
        ),
      );
    }

    // Group transactions by date
    final Map<String, List<TransactionData>> grouped = {};
    
    for (var transaction in transactions) {
      // Extract date from dateAndCategory (format: "Jan 8,2026 · Category")
      final datePart = transaction.dateAndCategory.split(' · ').first;
      final dateKey = datePart.contains('Jan 8') 
          ? context.l10n.today 
          : datePart;
      
      if (!grouped.containsKey(dateKey)) {
        grouped[dateKey] = [];
      }
      grouped[dateKey]!.add(transaction);
    }

    return SliverPadding(
      padding: EdgeInsets.symmetric(horizontal: 16.w),
      sliver: SliverList(
        delegate: SliverChildBuilderDelegate(
          (context, index) {
            final entry = grouped.entries.elementAt(index);
            final isLast = index == grouped.length - 1;
            
            return Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (index == 0) ...[
                  // Transactions header
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        context.l10n.transactions,
                        style: AppTextStyles.transactionsHeader(context),
                      ),
                      GestureDetector(
                        onTap: () {
                          // Navigate to all transactions
                        },
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.end,
                          children: [
                            Text(
                              context.l10n.seeAll,
                              style: AppTextStyles.seeAllLink(context),
                            ),
                            Container(
                              width: 38.w,
                              height: 1.h,
                              color: AppTheme.textSecondaryGray,
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  SizedBox(height: 32.h),
                ],
                _buildTransactionGroup(
                  dateLabel: entry.key,
                  transactions: entry.value,
                ),
                if (!isLast) SizedBox(height: 40.h),
              ],
            );
          },
          childCount: grouped.length,
        ),
      ),
    );
  }

  Widget _buildTransactionGroup({
    required String dateLabel,
    required List<TransactionData> transactions,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          dateLabel,
          style: AppTextStyles.dateLabel(context),
        ),
        SizedBox(height: 12.h),
        ...transactions.map((transaction) => _buildTransactionItem(transaction)),
      ],
    );
  }

  Widget _buildTransactionItem(TransactionData transaction) {
    return Semantics(
      label: 'Transaction: ${transaction.merchant}, ${transaction.amount}',
      child: Container(
        margin: EdgeInsets.only(bottom: 16.h),
        padding: EdgeInsets.all(8.w),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16.r),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.06),
              blurRadius: 8,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Row(
        children: [
          // Icon container
          Container(
            width: 44.w,
            height: 44.w,
            decoration: BoxDecoration(
              color: AppTheme.transactionIconBackground.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(32.r),
            ),
            child: Center(
              child: Icon(
                transaction.icon,
                size: 28.sp,
                color: AppTheme.mainColor,
              ),
            ),
          ),
          SizedBox(width: 32.w),
          // Transaction details
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  transaction.merchant,
                  style: AppTextStyles.transactionMerchant(context),
                ),
                SizedBox(height: 4.h),
                Text(
                  transaction.dateAndCategory,
                  style: AppTextStyles.transactionDate(context),
                ),
              ],
            ),
          ),
          // Amount
          Padding(
            padding: EdgeInsets.all(10.w),
            child: Text(
              transaction.amount,
              style: AppTextStyles.transactionAmount(
                context,
                color: transaction.isPositive
                    ? AppTheme.transactionPositive
                    : AppTheme.transactionNegative,
              ),
            ),
          ),
        ],
      ),
    ),
    );
  }
}