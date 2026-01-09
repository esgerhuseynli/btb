package az.btb.mobilebanking.ui.news_details;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NewsDetailsPresenter extends MvpPresenter<NewsDetailsView> {

    private final Router router;

    @Inject NewsDetailsPresenter(Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }
}
