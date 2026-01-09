package az.btb.mobilebanking.ui.operations_history.payment;

import java.util.ArrayList;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.PaymentOperationPayerInfo;
import az.btb.mobilebanking.models.PaymentProvidersRequest;
import az.btb.mobilebanking.models.PaymentsHistoryRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.utils.Constants.MoneySourceTypes;
import az.btb.mobilebanking.utils.PaymentHistoryProviderItem;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PaymentHistoryPresenter extends MvpPresenter<PaymentHistoryView> {
    
    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    @Inject PaymentHistoryPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }
    
    void goBack() {
        router.exit();
    }
    
    void getCards() {
        RequestInfoRequest request = new RequestInfoRequest(Utils.getCommonRequest());
        request.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
        
        compositeDisposable.add(
            authService
                .listBankCards(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showCards(response.getBankCards());
                        else {
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
//                            System.out.println("getCardsResponseError: " + new Gson().toJson(response));
                        }
                    },
                    error -> {
//                        System.out.println("getCardsError: " + new Gson().toJson(error));
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }
    
    void getBankAccounts() {
        RequestInfoRequest request = new RequestInfoRequest(Utils.getCommonRequest());
        request.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
        
        compositeDisposable.add(
            authService
                .listBankAccounts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showAccounts(response.getBankAccounts());
                        else {
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
//                            System.out.println("getBankAccountsResponseError: " + new Gson().toJson(response));
                        }
                    },
                    error -> {
//                        System.out.println("getBankAccountsError: " + new Gson().toJson(error));
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }
    
    void getPaymentProviderGroups() {
        RequestInfoRequest request = new RequestInfoRequest(Utils.getCommonRequest());
        request.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
        
        compositeDisposable.add(
            authService
                .getPaymentProviderGroups(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showPaymentProviderGroups(
                                CollectionsKt.map(
                                    response.getPaymentProviderGroups(),
                                    paymentProvider ->
                                        new PaymentHistoryProviderItem(
                                            paymentProvider.getIdPaymentProviderGroup(),
                                            paymentProvider.getPaymentProviderGroupName()
                                        )
                                )
                            );
                        else {
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
//                            System.out.println("getPaymentProviderGroupsResponseError: " + new Gson().toJson(response));
                        }
                    },
                    error -> {
//                        System.out.println("getPaymentProviderGroupsError: " + new Gson().toJson(error));
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }
    
    void getPaymentProviders(final int groupId) {
        if (groupId != 0) {
            PaymentProvidersRequest request = new PaymentProvidersRequest(Utils.getCommonRequest(), groupId);
            request.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
    
            compositeDisposable.add(
                authService
                    .getPaymentProviders(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        response -> {
                            if (response.getResponseInfo().getResponseType() == 0) {
                                getViewState().showPaymentProviders(
                                    CollectionsKt.map(
                                        response.getPaymentProviders(),
                                        paymentProvider ->
                                            new PaymentHistoryProviderItem(
                                                paymentProvider.getIdPaymentProvider(),
                                                paymentProvider.getPaymentProviderName()
                                            )
                                    )
                                );
                            } else {
                                getViewState().showError(response.getResponseInfo().getResponseMessage());
//                                System.out.println("getPaymentProvidersResponseError: " + new Gson().toJson(response));
                            }
                        },
                        error -> {
//                        System.out.println("getPaymentProvidersError: " + new Gson().toJson(error));
                            getViewState().showError(error.getMessage());
                        }
                    )
            );
        } else
            getViewState().showPaymentProviders(new ArrayList<>());
    }
    
    void getHistory(@MoneySourceTypes int paymentType, String cardId, String accountIban, String fromDate, String toDate, int providerGroupId, int providerId, int status, int fillingMethod) {
        PaymentsHistoryRequest request = new PaymentsHistoryRequest(
            Utils.getCommonRequest(),
            fromDate,
            toDate,
//            "01-01-2019",
//            "30-11-2020",
            0, // hele ki 0 olmalidir
            providerGroupId,
            providerId,
            status,
            fillingMethod,
            new PaymentOperationPayerInfo(
                paymentType,
                cardId, accountIban,
                "", ""
            )
        );
//        System.out.println("getHistoryRequest: " + new Gson().toJson(request));
        request.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
        
        compositeDisposable.add(
            authService
                .getPaymentsHistory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showHistory(response.getMobilePayments());
                        else {
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
//                            System.out.println("getHistoryResponseError: " + new Gson().toJson(response));
                        }
                    },
                    error -> {
//                        System.out.println("getHistoryError: " + new Gson().toJson(error));
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

//    void showDetails(transferItem) {
//        router.navigateTo(new MainScreens.LocalTransferDetailsScreen(transferItem));
//    }
    
    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
