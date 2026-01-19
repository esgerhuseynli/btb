class SimaSignPdfResponse {
  final String status;
  final String? message;
  final String? signedDocumentPath;

  SimaSignPdfResponse({
    required this.status,
    this.message,
    this.signedDocumentPath,
  });

  bool get isSuccess => status == 'success';
}

class SimaSignChallengeResponse {
  final String status;
  final String? message;
  final List<int>? signatureBytes;
  final List<int>? certificateBytes;

  SimaSignChallengeResponse({
    required this.status,
    this.message,
    this.signatureBytes,
    this.certificateBytes,
  });

  bool get isSuccess => status == 'success';
}

class SimaError {
  final String code;
  final String message;

  SimaError({
    required this.code,
    required this.message,
  });

  static String getErrorMessage(String errorCode) {
    switch (errorCode) {
      case 'operation-canceled':
        return 'İstifadəçi əməliyyatı ləğv etdi';
      case 'wrong-operation-type':
        return 'Boş və ya naməlum əməliyyat növü';
      case 'empty-data':
        return 'Boş imzalama məlumatı (sənəd və ya challenge)';
      case 'empty-service':
        return 'Boş xidmət adı';
      case 'empty-client-id':
        return 'Boş client id';
      case 'empty-signature':
        return 'Boş imza';
      case 'empty-user-code':
        return 'Boş istifadəçi kodu (FIN)';
      case 'wrong-user-code':
        return 'Yanlış istifadəçi kodu (FIN)';
      case 'wrong-logo-format':
        return 'Yanlış logo formatı';
      case 'wrong-logo-size':
        return 'Logo ölçüsü çox böyükdür (>500KB)';
      case 'document-processing-error':
        return 'Sənəd məlumatlarının işlənməsi zamanı xəta';
      case 'challenge-processing-error':
        return 'Challenge məlumatlarının işlənməsi zamanı xəta';
      case 'validate-request-error':
        return 'İmzalama sorğusunun yoxlanılması zamanı xəta (yanlış client id və ya imza)';
      case 'timestamp-request-error':
        return 'Sənəd imzalama üçün timestamp sorğusu zamanı xəta';
      case 'approve-request-error':
        return 'İmzalama sorğusunun təsdiqlənməsi zamanı xəta';
      case 'sign-document-error':
        return 'Sənədin imzalanması zamanı xəta';
      case 'sign-challenge-error':
        return 'Challenge-in imzalanması zamanı xəta';
      case 'internal-error':
        return 'Daxili Sima xətası';
      case 'empty-response':
        return 'Sima-dan boş cavab';
      default:
        return 'Naməlum xəta: $errorCode';
    }
  }
}

