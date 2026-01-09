package az.btb.mobilebanking.ui.own_card_transfers;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class OwnCardTransfersPresenter extends MvpPresenter<OwnCardTransfersView> {

    private final Router router;

    @Inject OwnCardTransfersPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    void makeTransfer(OtherCardTransferData4Accounts data) {
        router.navigateTo(new MainScreens.TransferSubmissionScreen(data));
    }
}
