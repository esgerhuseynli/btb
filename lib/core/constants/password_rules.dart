/// Password validation rules for banking app.
/// 
/// Centralized password rules that can be easily updated when requirements change.
class PasswordRules {
  PasswordRules._();

  /// Minimum password length (inclusive)
  static const int minLength = 8;

  /// Maximum password length (inclusive)
  static const int maxLength = 16;
}

