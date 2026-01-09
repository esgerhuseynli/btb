package az.btb.mobilebanking.ui.products.loans.order;

import java.math.BigDecimal;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.OrderLoanProduct;
import az.btb.mobilebanking.models.ProductLoanOrderRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductOrderLoanPresenter extends MvpPresenter<ProductOrderLoanView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ProductOrderLoanPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() { router.exit(); }

    void makeLoanOrder(int productId, BigDecimal amount, int duration, float percent, String purpose, int selectedCurrency) {
        ProductLoanOrderRequest request = new ProductLoanOrderRequest(
            Utils.getCommonRequest(),
            new OrderLoanProduct(
                productId,
                amount,
                duration,
                percent,
                purpose.isEmpty() ? " " : purpose,
                selectedCurrency)
        );

        compositeDisposable.add(
            authService
                .orderLoan(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showOrderResult(response.getLoanReferenceOrderStatus());
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
