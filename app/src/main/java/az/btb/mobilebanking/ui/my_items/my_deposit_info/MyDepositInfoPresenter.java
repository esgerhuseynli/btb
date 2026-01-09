package az.btb.mobilebanking.ui.my_items.my_deposit_info;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MyDepositInfoPresenter extends MvpPresenter<MyDepositInfoView> {

    private final Router router;

    @Inject
    MyDepositInfoPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }

    void changeCardAccountAltName() {

    }

    void changeCardAtmWithdrawLimit() {

    }
}
