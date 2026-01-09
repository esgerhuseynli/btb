package az.btb.mobilebanking.ui.operations_history.local_transfer.details;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LocalTransferDetailsPresenter extends MvpPresenter<MvpView> {
    private final Router router;

    @Inject LocalTransferDetailsPresenter(final Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }
}
