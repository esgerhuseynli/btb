package az.btb.mobilebanking.ui.intro;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.AuthScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_SCREEN_FAKE_TOKEN;
import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

@InjectViewState
public class IntroPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject
    IntroPresenter(Router router) {
        this.router = router;
    }

    void goToSignIn() {
        router.navigateTo(new AuthScreens.SignInScreen(SIGN_IN_SCREEN_FAKE_TOKEN, SIGN_IN_SCREEN_FAKE_TOKEN));
    }

    void goToSignUp() {
        // verify kodu gondermeye ehtiyac yoxdur. cunki hele ki, user-in pan/cif qeydiyyatinin
        // neticesini bilmirik.
        router.navigateTo(new AuthScreens.SignUpByTypesScreen(SIGN_UP_TYPE_PAN, null, null, null, null));
    }
}
