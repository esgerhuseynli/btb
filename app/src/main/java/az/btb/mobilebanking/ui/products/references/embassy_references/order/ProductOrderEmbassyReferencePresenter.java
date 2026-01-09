package az.btb.mobilebanking.ui.products.references.embassy_references.order;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.EmbassyPointsRequest;
import az.btb.mobilebanking.models.OrderEmbassyReferenceProduct;
import az.btb.mobilebanking.models.ProductEmbassyReferenceOrderRequest;
import az.btb.mobilebanking.models.ProductOrdererInfo;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductOrderEmbassyReferencePresenter extends MvpPresenter<ProductOrderEmbassyReferenceView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ProductOrderEmbassyReferencePresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() { router.exit(); }

    void getEmbassyCountries() {
        compositeDisposable.add(
            authService
                .getEmbassyCountries(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showEmbassyCountries(response.getEmbassyCountries());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void getEmbassyPoints(int countryId) {
        compositeDisposable.add(
            authService
                .getEmbassyPoints(new EmbassyPointsRequest(Utils.getCommonRequest(), countryId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showEmbassyPoints(response.getEmbassyPoints());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void makeEmbassyReferenceOrder(ProductOrdererInfo payerInfo, int orderType, int idEmbassyPoint, int productId, int referenceTerm) {
        ProductEmbassyReferenceOrderRequest request = new ProductEmbassyReferenceOrderRequest(
            Utils.getCommonRequest(),
            new OrderEmbassyReferenceProduct(
                payerInfo,
                orderType,
                idEmbassyPoint,
                productId,
                referenceTerm
            )
        );

        compositeDisposable.add(
            authService
                .orderEmbassyReference(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showOrderResult(response.getEmbassyReferenceOrderStatus());
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
