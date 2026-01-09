package az.btb.mobilebanking.ui.products;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductsPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject ProductsPresenter (Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }
    void goToProductOrdersScreen() { router.navigateTo(new MainScreens.ProductOrdersScreen()); }
    void goToProductCardsScreen() { router.navigateTo(new MainScreens.ProductPlasticCardsScreen()); }
    void goToProductLoansScreen() { router.navigateTo(new MainScreens.ProductLoansScreen()); }
    void goToProductDepositsScreen() { router.navigateTo(new MainScreens.ProductDepositsScreen()); }
    void goToProductReferencesScreen() { router.navigateTo(new MainScreens.ProductReferencesScreen()); }
}
