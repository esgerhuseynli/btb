import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../../auth/screens/intro_screen.dart';
import '../../auth/screens/sign_in_screen.dart';
import '../../auth/screens/sign_up_types_screen.dart';
import '../../auth/screens/sign_up_by_number_screen.dart';
import '../../auth/screens/sign_up_by_cif_screen.dart';
import '../../auth/screens/verification_screen.dart';
import '../../auth/screens/sign_up_pin_code/sign_up_pin_screen.dart';
import '../../auth/screens/sign_in_pin_code/sign_in_pin_screen.dart';
import '../../auth/screens/phone_number_entry/phone_number_entry_screen.dart';
import '../../auth/screens/sign_in_selection_screen.dart';
import '../../auth/screens/password_entry/password_entry_screen.dart';
import '../../auth/screens/forgot_password/forgot_password_screen.dart';
import '../../auth/screens/fin_code/fin_code_screen.dart';
import '../../auth/screens/new_password/new_password_screen.dart';
import '../../auth/screens/otp_verification/otp_verification_screen.dart';
import '../../home/screens/home_screen.dart';
import '../../home/screens/home/home_page_screen.dart';
import '../../core/widgets/splash_screen.dart';
import '../../../data/models/card_send_request.dart';
import '../../auth/bloc/auth_event.dart';
import '../../../core/theme/app_theme.dart';

class AppRouter {
  static final GoRouter router = GoRouter(
    initialLocation: '/splash',
    redirect: (context, state) {
      // Prevent SIMA callback URLs from being treated as navigation routes
      // These URLs should be processed by the sima package, not by the router
      final location = state.uri.toString();
      if (location.contains('status=success') && 
          (location.contains('signature=') || location.contains('certificate='))) {
        // This is a SIMA callback URL - ignore it and stay on current route
        debugPrint('=== GoRouter: Redirecting SIMA callback URL ===');
        debugPrint('Location: $location');
        debugPrint('This URL should be handled by sima package, not router');
        debugPrint('Staying on current route to allow sima package to process the callback');
        // Return null to stay on current route - this prevents navigation error
        // The sima package should complete Sima.loginSafe() Future
        return null; // Stay on current route
      }
      return null; // No redirect needed
    },
    errorBuilder: (context, state) {
      // Handle SIMA callback URLs that the sima package tries to navigate to
      // These URLs should be processed by the sima package, not by the router
      final location = state.uri.toString();
      if (location.contains('status=success') && 
          (location.contains('signature=') || location.contains('certificate='))) {
        // This is a SIMA callback URL - ignore it and stay on current route
        debugPrint('=== GoRouter: Ignoring SIMA callback URL in errorBuilder ===');
        debugPrint('Location: $location');
        debugPrint('This URL should be handled by sima package, not router');
        // Return the current route or a safe fallback
        return const Scaffold(
          body: Center(
            child: CircularProgressIndicator(),
          ),
        );
      }
      
      // For other errors, show error page
      return Scaffold(
        body: Center(
          child: Text('Route not found: ${state.uri}'),
        ),
      );
    },
    routes: [
      GoRoute(
        path: '/splash',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const SplashScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return FadeTransition(
              opacity: animation,
              child: child,
            );
          },
        ),
      ),
      GoRoute(
        path: '/intro',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const IntroScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/phone-entry',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const PhoneInputScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/sign-in-selection',
        pageBuilder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          return CustomTransitionPage(
            key: state.pageKey,
            child: SignInSelectionScreen(phone: phone),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/password-entry',
        pageBuilder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          return CustomTransitionPage(
            key: state.pageKey,
            child: PasswordEntryScreen(phone: phone),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/sign-in',
        pageBuilder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          final email = state.uri.queryParameters['email'];
          return CustomTransitionPage(
            key: state.pageKey,
            child: SignInScreen(
              phone: phone,
              email: email,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/home',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const HomePageScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/forgot-password',
        pageBuilder: (context, state) {
          final phone = state.uri.queryParameters['phone'];
          return CustomTransitionPage(
            key: state.pageKey,
            child: ForgotPasswordScreen(phone: phone),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/sign-up-types',
        pageBuilder: (context, state) {
          final extra = state.extra as Map<String, dynamic>?;
          return CustomTransitionPage(
            key: state.pageKey,
            child: SignUpTypesScreen(
              screenType: extra?['screenType'] as int?,
              verifyCode: extra?['verifyCode'] as String?,
              phone: extra?['phone'] as String?,
              email: extra?['email'] as String?,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/sign-up-number',
        pageBuilder: (context, state) {
          final extra = state.extra as Map<String, dynamic>;
          return CustomTransitionPage(
            key: state.pageKey,
            child: SignUpByNumberScreen(
              signUpType: extra['signUpType'] as int,
              verifyCode: extra['verifyCode'] as String,
              phone: extra['phone'] as String?,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/sign-up-email',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const Scaffold(
            body: Center(child: Text('Email ilə qeydiyyat səhifəsi (hələ hazırlanmayıb)')),
          ),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/sign-up-cif',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const SignUpByCifScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/sign-up-card',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const Scaffold(
            body: Center(child: Text('Kart ilə qeydiyyat səhifəsi (hələ hazırlanmayıb)')),
          ),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/sign-up-pin',
        pageBuilder: (context, state) {
          final extra = state.extra as Map<String, dynamic>;
          return CustomTransitionPage(
            key: state.pageKey,
            child: SignUpPinScreen(
              username: extra['username'] as String,
              passwordHash: extra['passwordHash'] as String,
              signInType: extra['signInType'] as int,
              isComingFromSignIn: extra['isComingFromSignIn'] as bool? ?? false,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/sign-in-pin',
        pageBuilder: (context, state) => CustomTransitionPage(
          key: state.pageKey,
          child: const SignInPinScreen(),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return _buildPageTransition(animation, secondaryAnimation, child);
          },
        ),
      ),
      GoRoute(
        path: '/verification',
        pageBuilder: (context, state) {
          final extra = state.extra as Map<String, dynamic>?;
          return CustomTransitionPage(
            key: state.pageKey,
            child: VerificationScreen(
              requestType: extra?['requestType'] as int? ?? 1,
              phone: extra?['phone'] as String?,
              email: extra?['email'] as String?,
              cardSendRequest: extra?['cardSendRequest'] as CardSendRequest?,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/fin-code',
        pageBuilder: (context, state) {
          final phoneNumber = state.uri.queryParameters['phoneNumber'];
          return CustomTransitionPage(
            key: state.pageKey,
            child: FinCodeScreen(phoneNumber: phoneNumber),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/new-password',
        pageBuilder: (context, state) {
          final extra = state.extra as Map<String, dynamic>?;
          return CustomTransitionPage(
            key: state.pageKey,
            child: NewPasswordScreen(
              verificationCode: extra?['verificationCode'] as String?,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
      GoRoute(
        path: '/otp-verification',
        pageBuilder: (context, state) {
          final phoneNumber = state.uri.queryParameters['phoneNumber'] ?? '';
          final flowTypeStr = state.uri.queryParameters['flowType'];
          OtpFlowType? flowType;
          if (flowTypeStr != null) {
            try {
              flowType = OtpFlowType.values.firstWhere(
                (e) => e.name == flowTypeStr,
              );
            } catch (e) {
              flowType = null;
            }
          }
          return CustomTransitionPage(
            key: state.pageKey,
            child: OtpVerificationScreen(
              phoneNumber: phoneNumber,
              flowType: flowType,
            ),
            transitionsBuilder: (context, animation, secondaryAnimation, child) {
              return _buildPageTransition(animation, secondaryAnimation, child);
            },
          );
        },
      ),
    ],
  );

  // Helper function to build page transitions with loading indicator
  static Widget _buildPageTransition(
    Animation<double> animation,
    Animation<double> secondaryAnimation,
    Widget child,
  ) {
    return AnimatedBuilder(
      animation: animation,
      builder: (context, _) {
        return Stack(
          children: [
            // Fade transition for the page
            FadeTransition(
              opacity: animation,
              child: SlideTransition(
                position: Tween<Offset>(
                  begin: const Offset(0.1, 0),
                  end: Offset.zero,
                ).animate(CurvedAnimation(
                  parent: animation,
                  curve: Curves.easeOut,
                )),
                child: child,
              ),
            ),
            // Loading indicator overlay during transition
            if (animation.value < 1.0)
              Container(
                color: Colors.white.withOpacity(0.3),
                child: const Center(
                  child: CircularProgressIndicator(
                    valueColor: AlwaysStoppedAnimation<Color>(AppTheme.mainColor),
                  ),
                ),
              ),
          ],
        );
      },
    );
  }
}

