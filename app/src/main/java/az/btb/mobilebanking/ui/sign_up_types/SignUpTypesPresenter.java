package az.btb.mobilebanking.ui.sign_up_types;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.utils.AsanImzaData;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_SCREEN_FAKE_TOKEN;

@InjectViewState
public class SignUpTypesPresenter extends MvpPresenter<SignUpTypesView> {

    private final Router router;

    @Inject SignUpTypesPresenter(Router router) {
        this.router = router;
    }

    void signUpByCif() {
        router.navigateTo(new AuthScreens.SignUpByCifScreen());
    }

    void signUpByCard() {
        router.navigateTo(new AuthScreens.SignUpByCardScreen());
    }

    void signUpByNumber(int signUpType, String verifyCode, @Nullable String phone, @Nullable AsanImzaData data) {
        router.navigateTo(new AuthScreens.SignUpByNumberScreen(signUpType, verifyCode, phone, data));
    }

    void signUpByEmail(int signUpType, String verifyCode, @Nullable String email, @Nullable AsanImzaData data) {
        router.navigateTo(new AuthScreens.SignUpByEmailScreen(signUpType, verifyCode, email, data));
    }

    void signUpByAsanImza() {
        router.navigateTo(new AuthScreens.SignUpByAsanImzaScreen());
    }

    void signIn() {
        router.navigateTo(new AuthScreens.SignInScreen(SIGN_IN_SCREEN_FAKE_TOKEN, SIGN_IN_SCREEN_FAKE_TOKEN));
    }

    void goBack() {
        router.exit();
    }
}
