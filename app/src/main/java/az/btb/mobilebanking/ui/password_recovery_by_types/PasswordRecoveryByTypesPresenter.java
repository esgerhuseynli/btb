package az.btb.mobilebanking.ui.password_recovery_by_types;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.AuthScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.PASSWORD_RECOVERY_TYPE_FIN;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_RECOVERY_TYPE_PAN;

@InjectViewState
public class PasswordRecoveryByTypesPresenter extends MvpPresenter<PasswordRecoveryByTypesView> {

    private final Router router;

    @Inject PasswordRecoveryByTypesPresenter(Router router) {
        this.router = router;
    }

    void passwordRecoveryByFin() {
        router.navigateTo(new AuthScreens.PasswordRecoveryScreen(PASSWORD_RECOVERY_TYPE_FIN));
    }

    void passwordRecoveryByPan() {
        router.navigateTo(new AuthScreens.PasswordRecoveryScreen(PASSWORD_RECOVERY_TYPE_PAN));
    }

    void goBack() {
        router.exit();
    }
}
