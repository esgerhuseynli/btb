import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../core/widgets/app_button.dart';
import '../bloc/auth_bloc.dart';
import '../bloc/auth_state.dart';
import '../widgets/intro_page_view.dart';

class IntroScreen extends StatefulWidget {
  const IntroScreen({super.key});

  @override
  State<IntroScreen> createState() => _IntroScreenState();
}

class _IntroScreenState extends State<IntroScreen> {
  final PageController _pageController = PageController();
  int _currentPage = 0;

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is PinVerificationRequired) {
          // Navigate to PIN verification screen
          context.go('/sign-in-pin');
        } else if (state is AuthAuthenticated) {
          // Already authenticated - navigate to home
          context.go('/home');
        } else if (state is AuthUnauthenticated) {
          // User not logged in - navigate to phone number entry screen
          context.go('/phone-entry');
        }
      },
      child: Scaffold(
      backgroundColor: AppTheme.white,
      body: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: PageView(
                controller: _pageController,
                onPageChanged: (index) {
                  setState(() {
                    _currentPage = index;
                  });
                },
                children: const [
                  IntroPageView(
                    title: 'BTB Mobil Bankçılıq',
                    description: 'Bank xidmətlərinə rahatlıqla çıxış əldə edin',
                    icon: Icons.account_balance,
                  ),
                  IntroPageView(
                    title: 'Təhlükəsiz Ödənişlər',
                    description: 'Təhlükəsiz və sürətli ödənişlər edin',
                    icon: Icons.payment,
                  ),
                  IntroPageView(
                    title: 'Köçürmələr',
                    description: 'Pul köçürmələri asanlıqla həyata keçirin',
                    icon: Icons.send,
                  ),
                ],
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(3, (index) => _buildDot(index)),
            ),
            const SizedBox(height: 32),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24.0),
              child: AppButton(
                text: _currentPage == 2 ? 'Başla' : 'Davam et',
                onPressed: () {
                  if (_currentPage < 2) {
                    _pageController.nextPage(
                      duration: const Duration(milliseconds: 300),
                      curve: Curves.easeInOut,
                    );
                  } else {
                    // Navigate to sign in
                    context.go('/sign-in');
                  }
                },
                width: double.infinity,
              ),
            ),
            const SizedBox(height: 32),
          ],
        ),
      ),
      ),
    );
  }

  Widget _buildDot(int index) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 4),
      width: _currentPage == index ? 24 : 8,
      height: 8,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(4),
        color:
            _currentPage == index ? AppTheme.mainColor : AppTheme.borderColor,
      ),
    );
  }
}
