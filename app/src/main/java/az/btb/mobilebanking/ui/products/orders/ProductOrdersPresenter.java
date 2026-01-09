package az.btb.mobilebanking.ui.products.orders;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.models.ProductOrdersRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductOrdersPresenter extends MvpPresenter<ProductItemsView<ProductOrder>> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ProductOrdersPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() { router.exit(); }

    void getProductOrders(int productType, int orderStatus) {
        ProductOrdersRequest request = new ProductOrdersRequest(
            Utils.getCommonRequest(),
            0,
            productType,
            orderStatus
        );

        compositeDisposable.add(
            authService
                .getProductOrders(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showItemsList(response.getProductOrders());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToOrderDetailsScreen(ProductOrder product) {
        router.navigateTo(new MainScreens.ProductOrderDetailsScreen(product));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
