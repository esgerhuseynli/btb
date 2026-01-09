package az.btb.mobilebanking.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import az.btb.mobilebanking.models.ForgotPasswordRequest;
import az.btb.mobilebanking.ui.fingerprint.FingerprintFragment;
import az.btb.mobilebanking.ui.password_recovery.PasswordRecoveryFragment;
import az.btb.mobilebanking.ui.password_recovery_by_types.PasswordRecoveryByTypesFragment;
import az.btb.mobilebanking.ui.password_recovery_change.PasswordRecoveryChangeFragment;
import az.btb.mobilebanking.ui.sign_in.SignInFragment;
import az.btb.mobilebanking.ui.sign_in_by_email.SignInByEmailFragment;
import az.btb.mobilebanking.ui.sign_in_by_number.SignInByNumberFragment;
import az.btb.mobilebanking.ui.sign_in_pin_fingerprint.SignInPinFingerprintFragment;
import az.btb.mobilebanking.ui.sign_up_by_asan_imza.SignUpByAsanImzaFragment;
import az.btb.mobilebanking.ui.sign_up_by_asan_imza.step2.SignUpByAsanImzaStep2Fragment;
import az.btb.mobilebanking.ui.sign_up_by_card.SignUpByCardFragment;
import az.btb.mobilebanking.ui.sign_up_by_cif.SignUpByCifFragment;
import az.btb.mobilebanking.ui.sign_up_by_email.SignUpByEmailFragment;
import az.btb.mobilebanking.ui.sign_up_by_number.SignUpByNumberFragment;
import az.btb.mobilebanking.ui.sign_up_pin.SignUpPinFragment;
import az.btb.mobilebanking.ui.sign_up_types.SignUpTypesFragment;
import az.btb.mobilebanking.ui.verify_code.VerificationFragment;
import az.btb.mobilebanking.utils.AsanImzaData;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class AuthScreens {
    public static final class VerificationScreen extends SupportAppScreen {

        private final int requestType;
        @Nullable private final String phone;
        @Nullable private final String email;

        public VerificationScreen(int requestType, @Nullable  String phone, @Nullable String email) {
            this.requestType = requestType;
            this.phone = phone;
            this.email = email;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return VerificationFragment.getInstance(requestType, phone, email);
        }
    }

    public static final class SignInScreen extends SupportAppScreen {

        private final @Nullable String phone;
        private final @Nullable String email;

        public SignInScreen(@Nullable String phone, @Nullable String email) {
            this.phone = phone;
            this.email = email;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignInFragment.getInstance(phone, email);
        }
    }

    public static final class SignInByNumberScreen extends SupportAppScreen {
        private final @Nullable String phone;

        public SignInByNumberScreen(@Nullable String phone) {
            this.phone = phone;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignInByNumberFragment.getInstance(phone);
        }
    }

    public static final class SignInByEmailScreen extends SupportAppScreen {
        private final  @Nullable String email;

        public SignInByEmailScreen(@Nullable String email) {
            this.email = email;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignInByEmailFragment.getInstance(email);
        }
    }

    public static final class SignUpByTypesScreen extends SupportAppScreen {

        private final int screenType;
        @Nullable private final String verifyCode;
        @Nullable private final String phone;
        @Nullable private final String email;
        @Nullable private final AsanImzaData data;

        public SignUpByTypesScreen(
            int screenType, @Nullable String verifyCode,
            @Nullable String phone, @Nullable String email,
            @Nullable AsanImzaData data
        ) {
            this.screenType = screenType;
            this.verifyCode = verifyCode;
            this.phone = phone;
            this.email = email;
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpTypesFragment.getInstance(screenType, verifyCode, phone, email, data);
        }
    }

    public static final class SignUpByCifScreen extends SupportAppScreen {
        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByCifFragment.getInstance();
        }
    }

    public static final class SignUpByCardScreen extends SupportAppScreen {
        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByCardFragment.getInstance();
        }
    }

    public static final class SignUpByNumberScreen extends SupportAppScreen {
        private final int signUpType;
        private final String verifyCode;
        private final @Nullable String phone;
        private final @Nullable AsanImzaData data;

        public SignUpByNumberScreen(int signUpType, String verifyCode, @Nullable String phone, @Nullable AsanImzaData data) {
            this.signUpType = signUpType;
            this.verifyCode = verifyCode;
            this.phone = phone;
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByNumberFragment.getInstance(signUpType, verifyCode, phone, data);
        }
    }

    public static final class SignUpByEmailScreen extends SupportAppScreen {
        private final int signUpType;
        private final String verifyCode;
        private final @Nullable String email;
        private final @Nullable AsanImzaData data;

        public SignUpByEmailScreen(int signUpType, String verifyCode, @Nullable String email, @Nullable AsanImzaData data) {
            this.signUpType = signUpType;
            this.verifyCode = verifyCode;
            this.email = email;
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByEmailFragment.getInstance(signUpType, verifyCode, email, data);
        }
    }

    public static final class SignUpByAsanImzaScreen extends SupportAppScreen {
        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByAsanImzaFragment.getInstance();
        }
    }

    public static final class SignUpByAsanImzaStep2Screen extends SupportAppScreen {
        private final AsanImzaData data;

        public SignUpByAsanImzaStep2Screen(AsanImzaData data) {
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpByAsanImzaStep2Fragment.getInstance(data);
        }
    }

    public static final class PasswordRecoveryByTypesScreen extends SupportAppScreen {
        @NonNull
        @Override
        public Fragment getFragment() {
            return PasswordRecoveryByTypesFragment.getInstance();
        }
    }

    public static final class PasswordRecoveryScreen extends SupportAppScreen {
        private final int passwordRecoveryType;

        public PasswordRecoveryScreen(int passwordRecoveryType) {
            this.passwordRecoveryType = passwordRecoveryType;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return PasswordRecoveryFragment.getInstance(passwordRecoveryType);
        }
    }

    public static final class PasswordRecoveryChangeScreen extends SupportAppScreen {
        private final ForgotPasswordRequest forgotPasswordRequest;
        private final String phone;

        public PasswordRecoveryChangeScreen(ForgotPasswordRequest forgotPasswordRequest, String phone) {
            this.forgotPasswordRequest = forgotPasswordRequest;
            this.phone = phone;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return PasswordRecoveryChangeFragment.getInstance(forgotPasswordRequest,phone);
        }
    }

    public static final class SignInPinFingerprintScreen extends SupportAppScreen {
        @NonNull
        @Override
        public Fragment getFragment() {
            return SignInPinFingerprintFragment.getInstance();
        }
    }

    public static final class SignUpPinScreen extends SupportAppScreen {

        private final int signUpType;
        private final String username;
        private final String password;
        private final boolean isComingFromSignInScreen;

        public SignUpPinScreen(int signUpType, String username, String password, boolean isComingFromSignInScreen) {
            this.signUpType = signUpType;
            this.username = username;
            this.password = password;
            this.isComingFromSignInScreen = isComingFromSignInScreen;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return SignUpPinFragment.getInstance(signUpType, username, password, isComingFromSignInScreen);
        }
    }

    public static final class FingerprintScreen extends SupportAppScreen {
        private final boolean isSignInScreen;

        public FingerprintScreen(boolean isSignInScreen) {
            this.isSignInScreen = isSignInScreen;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return FingerprintFragment.getInstance(isSignInScreen);
        }
    }


//    public static final class WebViewScreen extends SupportAppScreen {
//        private final String url;
//
//        public WebViewScreen(String url) {
//            this.url = url;
//        }
//
//        @NonNull
//        @Override
//        public Fragment getFragment() {
//            return WebViewFragment.newInstance(url);
//        }
//    }
}
