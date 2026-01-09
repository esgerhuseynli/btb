package az.btb.mobilebanking.ui.money_transfers.receive.step2;

import javax.inject.Inject;

import az.btb.mobilebanking.models.CheckTransferBeforeReceiveInfo;
import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransferReceiveStep2Presenter extends MvpPresenter<MoneyTransferReceiveStep2View> {

    private final Router router;

    @Inject MoneyTransferReceiveStep2Presenter(final Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }

    void goToStep3(CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo, int transferPaymentType, String id, String formatted) {
        router.navigateTo(new MainScreens.MoneyTransferReceiveStep3Screen(checkTransferBeforeReceiveInfo, transferPaymentType, id, formatted));
    }
}
