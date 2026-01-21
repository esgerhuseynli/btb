import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';
import '../../auth/bloc/auth_bloc.dart';
import '../../auth/bloc/auth_state.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  bool _hasNavigated = false;

  @override
  void initState() {
    super.initState();
    // Check current state immediately when screen loads
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final currentState = context.read<AuthBloc>().state;
      _navigateBasedOnState(currentState);
    });

    // Fallback timeout: if state doesn't change within 5 seconds, navigate to phone entry
    Future.delayed(const Duration(seconds: 5), () {
      if (!_hasNavigated && mounted) {
        _hasNavigated = true;
        context.go('/phone-entry');
      }
    });
  }

  void _navigateBasedOnState(AuthState state) async {
    if (_hasNavigated || !mounted) return;
    if (state is AuthInitial) return;

    _hasNavigated = true;

    // 👇 Ensure splash is visible at least 0.8 seconds (reduced for faster transition)
    await Future.delayed(const Duration(milliseconds: 800));

    if (!mounted) return;

    if (state is AuthAuthenticated) {
      context.go('/home');
    } else if (state is PinVerificationRequired) {
      context.go('/sign-in-pin');
    } else if (state is AuthError) {
      // On error, still navigate to phone entry (user can try again)
      context.go('/phone-entry');
    } else {
      // AuthUnauthenticated or any other state
      context.go('/phone-entry');
    }
  }


  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        _navigateBasedOnState(state);
      },
      child: Scaffold(
        backgroundColor: Colors.white,
        body: Center(
          child: Image.asset(
            'assets/images/logo.png',
            fit: BoxFit.contain,
            width: 150,
            height: 150,
          ),
        ),
      ),
    );
  }
}

