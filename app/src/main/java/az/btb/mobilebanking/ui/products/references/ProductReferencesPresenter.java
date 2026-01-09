package az.btb.mobilebanking.ui.products.references;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ReferenceProduct;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.ui.products.ProductItemsView;
import az.btb.mobilebanking.utils.Product;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.ProductTypes.EMBASSY_REFERENCE;
import static az.btb.mobilebanking.utils.Constants.ProductTypes.FINANCIAL_REFERENCE;

@InjectViewState
public class ProductReferencesPresenter extends MvpPresenter<ProductItemsView<ReferenceProduct>> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ProductReferencesPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void getReferenceProducts() {
        compositeDisposable.add(
            authService
                .getEmbassyReferenceProducts(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            for (ReferenceProduct product : response.getProducts())
                                product.setReferenceType(EMBASSY_REFERENCE);
                            getFinancialProducts(response.getProducts());
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    private void getFinancialProducts(List<ReferenceProduct> embassyProducts) {
        compositeDisposable.add(
            authService
                .getFinancialReferenceProducts(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            for (ReferenceProduct product : response.getProducts())
                                product.setReferenceType(FINANCIAL_REFERENCE);
                            embassyProducts.addAll(response.getProducts());
                            getViewState().showItemsList(embassyProducts);
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToProductDetails(@NonNull ReferenceProduct product) {
        final Product p = new Product();
        p.id = product.getProductId();
        p.image = product.getProductLogoImage();
        p.headerName = product.getProductHeaderName();
        p.information = product.getProductInformation();
        p.tariff = product.getProductTariff();
        p.cost = product.getProductCost();
        p.costCurrency = product.getProductCostCurrency();
        p.hasOnlinePayment = product.getOnlinePayment() == 1;
        p.type = product.getReferenceType();

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
