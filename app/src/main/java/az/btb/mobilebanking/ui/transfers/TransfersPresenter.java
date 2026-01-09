package az.btb.mobilebanking.ui.transfers;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class TransfersPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject
    public TransfersPresenter(final Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }

    void goToBetweenMineTransfersScreen() { router.navigateTo(new MainScreens.OwnCardTransfersScreen()); }
    void goToOtherCardTransfersScreen() { router.navigateTo(new MainScreens.OtherCardTransfersScreen(false)); }
    void goToOtherAccountTransfersScreen() { router.navigateTo(new MainScreens.OtherCardTransfersScreen(true)); }

    void goToMoneyTransfersScreen() { router.navigateTo(new MainScreens.MoneyTransfersScreen()); }
    void goToLocalTransfersScreen() { router.navigateTo(new MainScreens.LocalTransfersScreen()); }
    void goToInternationalTransfersScreen() { router.navigateTo(new MainScreens.InternationalTransfersScreen()); }
}
