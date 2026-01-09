package az.btb.mobilebanking.ui.payments.obsiy;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ObsiyPaymentRequest;
import az.btb.mobilebanking.models.PaymentProviderJSONParameter;
import az.btb.mobilebanking.models.PaymentProviderRequestParameter;
import az.btb.mobilebanking.models.PaymentValidationRequest;
import az.btb.mobilebanking.models.QrCodeValidationInfo;
import az.btb.mobilebanking.models.QrCodeValidationRequest;
import az.btb.mobilebanking.models.ValidatePayment;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Constants.PaymentUiType;
import az.btb.mobilebanking.utils.Constants.QrCodeValidationResults;
import az.btb.mobilebanking.utils.PaymentInfoData;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ObsiyPaymentPresenter extends MvpPresenter<ObsiyPaymentView> {
    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<PaymentProviderJSONParameter> mUiData = new MutableLiveData<>();
    public final LiveData<PaymentProviderJSONParameter> uiData = mUiData;

    @Inject
    public ObsiyPaymentPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void getInteractiveViewDataFor(final int providerId) {
        compositeDisposable.add(
            authService
                .getObsiyPaymentUi(new ObsiyPaymentRequest(Utils.getCommonRequest(), providerId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            mUiData.postValue(response.getPaymentProviderJSONParameters().get(0));
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void validate(
        int providerGroupId,
        int providerId,
        String providerName,
        List<ObsiyPaymentFragment.InteractiveView> previouslyGeneratedViewData
    ) {
        List<PaymentProviderRequestParameter> userEnteredData =
            CollectionsKt.map(
                previouslyGeneratedViewData,
                data -> new PaymentProviderRequestParameter(data.getParameterName(), data.getParameterValue())
            );

        PaymentValidationRequest request =
            new PaymentValidationRequest(
                Utils.getCommonRequest(),
                new ValidatePayment(
                    1,
                    0,
                    providerId,
                    userEnteredData
                )
            );

        compositeDisposable.add(
            authService
                .validatePayment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (response.getValidatePaymentTemplateStatus() < 2) {
                                PaymentInfoData pid = new PaymentInfoData();
                                pid.providerName = providerName;

                                if (providerGroupId == 1) {
                                    // mobile providerlarda ancaq 1 dene telefon nomresi ola bilir deye,
                                    // rahatliqla 0ci indexdeki elementi goturub set ede bilirik.
                                    pid.phoneNumber = userEnteredData.get(0).getParameterValue().toString();
                                } else if (providerGroupId == 2) {
                                    // bele cox eybecer... view-nu runtimeda generate elemeyin zibilleridi hamisi.
                                    //
                                    // (*) ui-ni generate etmek ucun lazim olan JSON datadaki melumatlarin hansi
                                    // ardicilliqla gelmeyine hec kes 100% qarantiya vere bilmez. nece deyerler,
                                    // ishimizi etibarli tutmaq ucun, bele bir check-gate qoymusam.
                                    //
                                    // logic beledir: groupId-si 2 olanlarda (yeni kommunal odenislerde) 2 dene
                                    // field ola biler: biri dropdown-du (ehali ve qeyri-ehali secimi ucun), digeri
                                    // ise edittext (abonent kodu ucun). proqram bu if-in icine girende, bile
                                    // bilmerik ki, hansi field melumatlari 1ci hansi 2ci gelir. bu ardicilliq
                                    // yuxarida yazdigim (*) hissesi ile baglidir. overhead olmasin deye view-nu
                                    // generate edende set etdiyim `viewType` property-sinden istifade edirem.
                                    // ne cure istifade etdiyimin mentiqi ise asagidaki if-else conditionun-dadir.
                                    //
                                    if (previouslyGeneratedViewData.get(0).getViewType() == PaymentUiType.EDIT_TEXT) {
                                        pid.abonentKodu = userEnteredData.get(0).getParameterValue().toString();
                                        pid.paymentType = Integer.parseInt(userEnteredData.get(1).getParameterValue().toString());
                                    } else {
                                        pid.abonentKodu = userEnteredData.get(1).getParameterValue().toString();
                                        pid.paymentType = Integer.parseInt(userEnteredData.get(0).getParameterValue().toString());
                                    }
                                } else if (providerGroupId == 3) {
                                    if (previouslyGeneratedViewData.get(0).getViewType() == PaymentUiType.EDIT_TEXT) {
                                        pid.abonentKodu = userEnteredData.get(0).getParameterValue().toString();
                                        pid.paymentType = Integer.parseInt(userEnteredData.get(1).getParameterValue().toString());
                                    } else {
                                        pid.abonentKodu = userEnteredData.get(1).getParameterValue().toString();
                                        pid.paymentType = Integer.parseInt(userEnteredData.get(0).getParameterValue().toString());
                                    }
                                }
                                
                                router.navigateTo(
                                    new MainScreens.PaymentsSourceSelectionScreen(
                                        providerName,
                                        response.getPaymentCommonInvoiceInfo(),
                                        providerId,
                                        false,
                                        "",
                                        pid
                                    )
                                );
                            } else
                                getViewState().showError(R.string.payment_validation_error);
                        } else {
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
     //                       System.out.println("responseError: " + new Gson().toJson(response));
                        }
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void validateQrCode(final String qrCodeContent) {
        compositeDisposable.add(
            authService
                .validateQrCode(new QrCodeValidationRequest(Utils.getCommonRequest(), qrCodeContent))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (response.getQrCodeValidationInfo().getQrCodeValidationResultType() == QrCodeValidationResults.QR_CODE_SUCCESS)
                                // https://app.clickup.com/t/6hz6dc
                                validateQrCodePaymentRequest(response.getQrCodeValidationInfo(), qrCodeContent);
                            else
                                getViewState().showQrCodeErrorResult(response.getQrCodeValidationInfo().getQrCodeValidationResultType());
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    private void validateQrCodePaymentRequest(@NonNull QrCodeValidationInfo qrCodeValidationInfo, @NonNull String qrCodeValue) {
        PaymentValidationRequest request =
            new PaymentValidationRequest(
                Utils.getCommonRequest(),
                new ValidatePayment(
                    1,
                    0,
                    qrCodeValidationInfo.getIdPaymentProvider(),
                    qrCodeValidationInfo.getPaymentProviderRequestParameters()
                )
            );

        compositeDisposable.add(
            authService
                .validatePayment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (response.getValidatePaymentTemplateStatus() < 2)
                                router.navigateTo(
                                    new MainScreens.PaymentsSourceSelectionScreen(
                                        qrCodeValidationInfo.getPaymentProviderName(),
                                        response.getPaymentCommonInvoiceInfo(),
                                        qrCodeValidationInfo.getIdPaymentProvider(),
                                        true,
                                        qrCodeValue,
                                        new PaymentInfoData()
                                    )
                                );
                            else
                                getViewState().showError(R.string.payment_validation_error);
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }
}
