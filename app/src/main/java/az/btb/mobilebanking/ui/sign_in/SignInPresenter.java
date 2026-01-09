package az.btb.mobilebanking.ui.sign_in;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.AuthScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

@InjectViewState
public class SignInPresenter extends MvpPresenter<SignInView> {

    private final Router router;

    @Inject
    SignInPresenter(Router router) {
        this.router = router;
    }

    void signInByNumber(@Nullable String phone) {
        router.replaceScreen(new AuthScreens.SignInByNumberScreen(phone));
    }

    void signInByEmail(@Nullable String email) {
        router.replaceScreen(new AuthScreens.SignInByEmailScreen(email));
    }

    void signUp() {
        router.navigateTo(new AuthScreens.SignUpByTypesScreen(SIGN_UP_TYPE_PAN, null, null, null, null));
    }

    void goBack() {
        router.exit();
    }
}
