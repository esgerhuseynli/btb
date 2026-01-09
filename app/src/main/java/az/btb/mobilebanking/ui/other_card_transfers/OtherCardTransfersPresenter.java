package az.btb.mobilebanking.ui.other_card_transfers;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class OtherCardTransfersPresenter extends MvpPresenter<OtherCardTransfersView> {

    private final Router router;

    @Inject OtherCardTransfersPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    void submitTransfer(OtherCardTransferData4Accounts data) {
        router.navigateTo(new MainScreens.TransferSubmissionScreen(data));
    }
}
