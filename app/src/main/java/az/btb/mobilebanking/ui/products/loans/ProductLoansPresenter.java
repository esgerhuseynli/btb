package az.btb.mobilebanking.ui.products.loans;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.LoanProduct;
import az.btb.mobilebanking.models.ProductConditions;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Product;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductLoansPresenter extends MvpPresenter<ProductItemsView<LoanProduct>> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ProductLoansPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() { router.exit(); }

    void getProducts() {
        compositeDisposable.add(
            authService
                .getLoanProducts(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showItemsList(response.getProducts());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToProductDetails(@NonNull LoanProduct product) {
        Product p = new Product();
        p.id = product.getProductId();
        p.image = product.getProductLogoImage();
        p.headerName = product.getProductHeaderName();
        p.information = product.getProductInformation();
        p.tariff = product.getProductTarif();
        p.cost = product.getProductCost();
        p.costCurrency = product.getProductCostCurrency();
        p.hasOnlinePayment = product.getOnlinePayment() == 1;
        p.type = Constants.ProductTypes.LOAN;
        p.orderData.availableCurrencies = product.getProductAvailableCurrencies();
        p.orderData.currencyAssociatedProductCondition =
            CollectionsKt.associateBy(product.getProductConditions(), ProductConditions::getCurrency);

        router.navigateTo(new MainScreens.ProductDetailsScreen(p));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
