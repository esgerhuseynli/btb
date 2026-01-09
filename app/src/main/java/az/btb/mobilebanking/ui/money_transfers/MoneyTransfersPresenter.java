package az.btb.mobilebanking.ui.money_transfers;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransfersPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject MoneyTransfersPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }

    void goToMoneyTransferring() {
        router.navigateTo(new MainScreens.MoneyTransferringStep1Screen());
    }
    
    void goToReceiveScreen() {
        router.navigateTo(new MainScreens.MoneyTransferReceiveStep1Screen());
    }
    
    void goToHistoryScreen() {
        router.navigateTo(new MainScreens.MoneyTransferHistoryScreen());
    }
    
    void goToSearchScreen() {
        router.navigateTo(new MainScreens.MoneyTransferSearchScreen());
    }
}
