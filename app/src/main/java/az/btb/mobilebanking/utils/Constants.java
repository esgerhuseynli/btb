package az.btb.mobilebanking.utils;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import az.btb.mobilebanking.BuildConfig;

public class Constants {

    public static final String HOME_CICERONE_NAME = "CICERONE_HOME";

    private static final int PORT = BuildConfig.FLAVOR == "dev" ? 4444 : 4445;
    public static final String BASE_URL = "https://mobilebanking.btb.az:4445/";
    public static final String COOKIE_KEY = "Cookie";

    public static final String KEY_PIN_FINGERPRINT_SCREEN_BYPASS = "k0";

    /**
     * Only hashed 4-digit PIN value should be stored with this key.
     * Use {@link Utils#passwordHash(String)} to has PINs.
     */
    public static final String PIN_HASH = "d3";

    public static final String PASSWORD_HASH = "d0";
    public static final String USERNAME = "d1";

    public static final String SESSION_KEY = "d2";

    /**
     * A shared preference key which identifies user sign in/out status.
     * As its value it could keep two different values:
     * `true` if user successfully signed in, `false` otherwise.
     */
    public static final String HAS_ACTIVE_SESSION = "d5";

    /**
     * An integer value which determines email or phone number used
     * during sign in process.
     */
    public static final String SIGN_IN_TYPE = "d4";

    /**
     * A shared preference key which identifies user sign has enabled
     * fingerprint login option or not.
     * As its value it could keep two different values:
     * `true` if user enabled it, `false` otherwise.
     */
    public static final String IS_FINGERPRINT_ENABLED = "d6";

    public static final String FCM_NOTIFICATION_TOKEN = "d7";

    public static final String CUSTOMER_NAME = "d8";

    public static final String LAST_LOGIN = "d9";

    public static final String LANGUAGE_CHANGE_EVENT_TOKEN = "s1";

    /**
     * DO NOT USE TO GET SELECTED LANGUAGE. INSTEAD, USE Lingver APIs.
     */
    public static final String APP_LANGUAGE = "d11";

    public static final long ALLOWED_LOGIN_TIMEOUT = TimeUnit.SECONDS.toMillis(120);

    /*
     **************************************
     * When R8 is enabled in gradle file, *
     * you can safely use these enums.    *
     * Otherwise, performance issues may  *
     * raise.                             *
     **************************************
     */
//    public enum SignUpScreenTypes {
//        TYPE_PAN_CIF,
//        TYPE_NUMBER_EMAIL
//    }

//    public enum SignUpTypes {
//        TYPE_PAN,
//        TYPE_CIF,
//        TYPE_PHONE,
//        TYPE_EMAIL
//    }

    public static final int SIGN_UP_SCREEN_TYPE_PAN_CIF = 123;
    public static final int SIGN_UP_SCREEN_TYPE_NUMBER_EMAIL = 234;

    public static final int SIGN_UP_TYPE_PAN = 1;
    public static final int SIGN_UP_TYPE_CIF = 2;

    public static final int SIGN_IN_UP_TYPE_EMAIL = 1;
    public static final int SIGN_IN_UP_TYPE_NUMBER = 2;

    public static final int PASSWORD_RECOVERY_TYPE_PAN = 1;
    public static final int PASSWORD_RECOVERY_TYPE_FIN = 2;

    public static final int PROFILE_UPDATE_TYPE_NONE = 0;
    public static final int PROFILE_UPDATE_TYPE_EMAIL = 1;
    public static final int PROFILE_UPDATE_TYPE_MOBILE_NUMBER = 2;
    public static final int PROFILE_UPDATE_TYPE_PASSWORD = 3;

    public static final int PROFILE_UPDATE_VERIFICATION_TYPE_EMAIL = PROFILE_UPDATE_TYPE_EMAIL;
    public static final int PROFILE_UPDATE_VERIFICATION_TYPE_MOBILE_NUMBER = PROFILE_UPDATE_TYPE_MOBILE_NUMBER;
    public static final int PROFILE_UPDATE_VERIFICATION_TYPE_PASSWORD = PROFILE_UPDATE_TYPE_PASSWORD;

    public static final int DEVICE_STATUS_DISABLE = 0;
    public static final int DEVICE_STATUS_ENABLE = 1;

    public static final int EXCHANGE_RATE_CASH = -1;
    public static final int EXCHANGE_RATE_NON_CASH = 1;

    public static final int EXCHANGE_RATE_TYPE_BUY = 0;
    public static final int EXCHANGE_RATE_TYPE_SELL = 1;

    public static final int SERVICE_POINT_TYPE_ATM = 727;
    public static final int SERVICE_POINT_TYPE_BRANCH = 272;

    public static final String SIGN_IN_SCREEN_FAKE_TOKEN = "-_-";

    @IntDef({
        Currency.AZN,
        Currency.USD,
        Currency.EUR,
        Currency.RUB,
        Currency.GBP,
        Currency.TRY
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Currency {
        int AZN = 0;
        int USD = 1;
        int EUR = 2;
        int RUB = 3;
        int GBP = 4;
        int TRY = 5;
    }

    @StringDef({
        MoneyTransferUniqueCodes.ZOLOTAYA_KORONA,
        MoneyTransferUniqueCodes.MONEX
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoneyTransferUniqueCodes {
        String ZOLOTAYA_KORONA = "ZK";
        String MONEX = "MX";
    }

    @IntDef({
        MoneySourceTypes.NONE,
        MoneySourceTypes.ACCOUNT,
        MoneySourceTypes.CARD
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoneySourceTypes {
        int NONE = 0;
        int ACCOUNT = 1;
        int CARD = 2;
    }

    @IntDef({
        MoneyTransferPointTypes.ALL,
        MoneyTransferPointTypes.CITY
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoneyTransferPointTypes {
        int ALL = 1;
        int CITY = 2;
    }

    @IntDef({
        QrCodeValidationResults.NONE,
        QrCodeValidationResults.QR_CODE_FAILED,
        QrCodeValidationResults.QR_CODE_SUCCESS,
        QrCodeValidationResults.NO_SUCH_PAYMENT_PROVIDER
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface QrCodeValidationResults {
        int NONE = 0;
        int QR_CODE_FAILED = 1;
        int QR_CODE_SUCCESS = 2;
        int NO_SUCH_PAYMENT_PROVIDER = 3;
    }

    @IntDef({
        PaymentDataFillingMethod.MANUAL,
        PaymentDataFillingMethod.FROM_QR_CODE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PaymentDataFillingMethod {
        int MANUAL = 1;
        int FROM_QR_CODE = 2;
    }

    @IntDef({
        ProductTypes.PLASTIC_CARD,
        ProductTypes.LOAN,
        ProductTypes.DEPOSIT,
        ProductTypes.EMBASSY_REFERENCE,
        ProductTypes.FINANCIAL_REFERENCE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProductTypes {
        int PLASTIC_CARD = 3;
        int LOAN = 1;
        int DEPOSIT = 2;
        int EMBASSY_REFERENCE = 4;
        int FINANCIAL_REFERENCE = 5;
    }

    @IntDef({
        PaymentUiType.SPINNER,
        PaymentUiType.EDIT_TEXT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PaymentUiType {
        int SPINNER = 1;
        int EDIT_TEXT = 2;
    }
}
