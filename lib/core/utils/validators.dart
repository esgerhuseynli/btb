class Validators {
  static String? validateEmail(String? value) {
    if (value == null || value.isEmpty) {
      return 'Email ünvanı daxil edin';
    }
    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(value)) {
      return 'Email ünvanı formatı düzgün deyil';
    }
    return null;
  }

  static String? validatePhoneNumber(String? value) {
    if (value == null || value.isEmpty) {
      return 'Mobil nömrə daxil edin';
    }
    final cleaned = value.replaceAll(RegExp(r'\D'), '');
    if (cleaned.length != 9 && !(cleaned.length == 12 && cleaned.startsWith('994'))) {
      return 'Mobil nömrə formatı düzgün deyil';
    }
    return null;
  }

  static String? validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Şifrə boş ola bilməz';
    }
    if (value.length < 6) {
      return 'Şifrə ən azı 6 simvol olmalıdır';
    }
    return null;
  }

  static String? validatePin(String? value) {
    if (value == null || value.isEmpty) {
      return 'PIN şifrə daxil edin';
    }
    if (value.length != 4) {
      return 'PIN şifrə 4 rəqəm olmalıdır';
    }
    if (!RegExp(r'^\d{4}$').hasMatch(value)) {
      return 'PIN şifrə yalnız rəqəmlərdən ibarət olmalıdır';
    }
    return null;
  }

  static String? validateRequired(String? value, String fieldName) {
    if (value == null || value.trim().isEmpty) {
      return '$fieldName daxil edin';
    }
    return null;
  }

  static String? validateCardNumber(String? value) {
    if (value == null || value.isEmpty) {
      return 'Kart nömrəsi daxil edin';
    }
    final cleaned = value.replaceAll(RegExp(r'\D'), '');
    if (cleaned.length < 16) {
      return 'Kart nömrəsi 16 rəqəm olmalıdır';
    }
    return null;
  }

  static String? validateCif(String? value) {
    if (value == null || value.isEmpty) {
      return 'Müştəri kodu (CIF) daxil edin';
    }
    return null;
  }

  static String? validateAmount(String? value) {
    if (value == null || value.isEmpty) {
      return 'Məbləğ daxil edin';
    }
    final amount = double.tryParse(value.replaceAll(',', '.'));
    if (amount == null || amount <= 0) {
      return 'Düzgün məbləğ daxil edin';
    }
    return null;
  }

  static String? validateIban(String? value) {
    if (value == null || value.isEmpty) {
      return 'IBAN daxil edin';
    }
    final cleaned = value.replaceAll(RegExp(r'\s'), '').toUpperCase();
    if (cleaned.length < 16) {
      return 'IBAN formatı düzgün deyil';
    }
    return null;
  }
}



