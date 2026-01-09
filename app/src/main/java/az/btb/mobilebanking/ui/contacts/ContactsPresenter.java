package az.btb.mobilebanking.ui.contacts;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ContactsPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject public ContactsPresenter(final Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }
}
