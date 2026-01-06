import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../auth/screens/intro_screen.dart';
import '../../auth/screens/sign_in_screen.dart';
import '../../auth/screens/sign_up_types_screen.dart';
import '../../auth/screens/sign_up_by_number_screen.dart';
import '../../auth/screens/sign_up_by_cif_screen.dart';
import '../../auth/screens/verification_screen.dart';
import '../../auth/screens/sign_up_pin_screen.dart';
import '../../auth/screens/sign_in_pin_screen.dart';
import '../../auth/screens/phone_number_entry_screen.dart';
import '../../auth/screens/sign_in_selection_screen.dart';
import '../../home/screens/home_screen.dart';
import '../../../data/models/card_send_request.dart';

class AppRouter {
  static final GoRouter router = GoRouter(
    initialLocation: '/phone-entry',
    routes: [
      GoRoute(
        path: '/intro',
        builder: (context, state) => const IntroScreen(),
      ),
      GoRoute(
        path: '/phone-entry',
        builder: (context, state) => const PhoneNumberEntryScreen(),
      ),
      GoRoute(
        path: '/sign-in-selection',
        builder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          return SignInSelectionScreen(phone: phone);
        },
      ),
      GoRoute(
        path: '/sign-in',
        builder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          final email = state.uri.queryParameters['email'];
          return SignInScreen(
            phone: phone,
            email: email,
          );
        },
      ),
      GoRoute(
        path: '/home',
        builder: (context, state) => const HomeScreen(),
      ),
      GoRoute(
        path: '/forgot-password',
        builder: (context, state) => const Scaffold(
          body: Center(child: Text('Şifrə bərpası səhifəsi (hələ hazırlanmayıb)')),
        ),
      ),
      GoRoute(
        path: '/sign-up-types',
        builder: (context, state) {
          final extra = state.extra as Map<String, dynamic>?;
          return SignUpTypesScreen(
            screenType: extra?['screenType'] as int?,
            verifyCode: extra?['verifyCode'] as String?,
            phone: extra?['phone'] as String?,
            email: extra?['email'] as String?,
          );
        },
      ),
      GoRoute(
        path: '/sign-up-number',
        builder: (context, state) {
          final extra = state.extra as Map<String, dynamic>;
          return SignUpByNumberScreen(
            signUpType: extra['signUpType'] as int,
            verifyCode: extra['verifyCode'] as String,
            phone: extra['phone'] as String?,
          );
        },
      ),
      GoRoute(
        path: '/sign-up-email',
        builder: (context, state) => const Scaffold(
          body: Center(child: Text('Email ilə qeydiyyat səhifəsi (hələ hazırlanmayıb)')),
        ),
      ),
      GoRoute(
        path: '/sign-up-cif',
        builder: (context, state) => const SignUpByCifScreen(),
      ),
      GoRoute(
        path: '/sign-up-card',
        builder: (context, state) => const Scaffold(
          body: Center(child: Text('Kart ilə qeydiyyat səhifəsi (hələ hazırlanmayıb)')),
        ),
      ),
      GoRoute(
        path: '/sign-up-pin',
        builder: (context, state) {
          final extra = state.extra as Map<String, dynamic>;
          return SignUpPinScreen(
            username: extra['username'] as String,
            passwordHash: extra['passwordHash'] as String,
            signInType: extra['signInType'] as int,
            isComingFromSignIn: extra['isComingFromSignIn'] as bool? ?? false,
          );
        },
      ),
      GoRoute(
        path: '/sign-in-pin',
        builder: (context, state) => const SignInPinScreen(),
      ),
      GoRoute(
        path: '/verification',
        builder: (context, state) {
          final extra = state.extra as Map<String, dynamic>?;
          return VerificationScreen(
            requestType: extra?['requestType'] as int? ?? 1,
            phone: extra?['phone'] as String?,
            email: extra?['email'] as String?,
            cardSendRequest: extra?['cardSendRequest'] as CardSendRequest?,
          );
        },
      ),
    ],
  );
}

