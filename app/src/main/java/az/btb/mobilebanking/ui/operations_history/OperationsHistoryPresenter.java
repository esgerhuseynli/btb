package az.btb.mobilebanking.ui.operations_history;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class OperationsHistoryPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject OperationsHistoryPresenter(Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }
    void goToFromCardToCardHistoryScreen() { router.navigateTo(new MainScreens.CardToCardHistoryScreen()); }
    void goToPaymentsHistoryScreen() { router.navigateTo(new MainScreens.PaymentHistoryScreen()); }
    void goToInternationalTransfersHistoryScreen() { router.navigateTo(new MainScreens.InternationalTransfersHistoryScreen()); }
    void goToLocalTransfersHistoryScreen() { router.navigateTo(new MainScreens.LocalTransfersHistoryScreen()); }
}
