package az.btb.mobilebanking.ui.my_items.my_loan_info;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MyLoanInfoPresenter extends MvpPresenter<MyLoanInfoView> {

    private final Router router;

    @Inject
    MyLoanInfoPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }
}
