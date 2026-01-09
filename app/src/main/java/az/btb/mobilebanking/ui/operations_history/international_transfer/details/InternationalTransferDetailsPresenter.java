package az.btb.mobilebanking.ui.operations_history.international_transfer.details;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class InternationalTransferDetailsPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject InternationalTransferDetailsPresenter(final Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }
}
