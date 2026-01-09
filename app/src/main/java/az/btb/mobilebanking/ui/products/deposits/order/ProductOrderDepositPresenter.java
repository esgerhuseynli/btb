package az.btb.mobilebanking.ui.products.deposits.order;

import java.math.BigDecimal;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.OrderDepositProduct;
import az.btb.mobilebanking.models.ProductDepositOrderRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductOrderDepositPresenter extends MvpPresenter<ProductOrderDepositView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ProductOrderDepositPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() { router.exit(); }

    void makeDepositOrder(int productId, BigDecimal amount, int duration, float percent, int selectedCurrency) {
        ProductDepositOrderRequest request = new ProductDepositOrderRequest(
            Utils.getCommonRequest(),
            new OrderDepositProduct(
                productId,
                amount,
                duration,
                percent,
                selectedCurrency
            )
        );

        compositeDisposable.add(
            authService
                .orderDeposit(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showOrderResult(response.getDepositReferenceOrderStatus());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToProductsScreen() {
        router.backTo(new MainScreens.ProductsScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
