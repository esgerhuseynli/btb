package az.btb.mobilebanking.ui.home;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BankCardAndAccount;
import az.btb.mobilebanking.models.PaymentValidationRequest;
import az.btb.mobilebanking.models.QrCodeValidationInfo;
import az.btb.mobilebanking.models.QrCodeValidationRequest;
import az.btb.mobilebanking.models.ValidatePayment;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Constants;
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
public class HomePresenter extends MvpPresenter<HomeView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject HomePresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goToCardStatementsScreen(final String cardId, final String cardName) {
        router.navigateTo(new MainScreens.CardStatementsScreen(cardId, cardName));
    }

    void goToOwnCardTransfersScreen() {
        router.navigateTo(new MainScreens.OwnCardTransfersScreen());
    }

    void goToOtherCardTransfersScreen(boolean isToAccount) {
        router.navigateTo(new MainScreens.OtherCardTransfersScreen(isToAccount));
    }

    void goToMoneyTransfersScreen() {
        router.navigateTo(new MainScreens.MoneyTransfersScreen());
    }

    void goToPaymentsScreen() {
        router.navigateTo(new MainScreens.PaymentsScreen(false));
    }

    void goToLocalTransfers() {
        router.navigateTo(new MainScreens.LocalTransfersScreen());
    }

    void goToInternationalTransfers() {
        router.navigateTo(new MainScreens.InternationalTransfersScreen());
    }

    void goToCardDetailsScreen(Object bankCardObject) {
        router.navigateTo(new MainScreens.MyCardInfoScreen((BankCard) bankCardObject));
    }

    void goToAccountDetailsScreen(Object bankAccountObject) {
        router.navigateTo(new MainScreens.MyAccountInfoScreen((BankAccount) bankAccountObject));
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
                            if (response.getQrCodeValidationInfo().getQrCodeValidationResultType() == Constants.QrCodeValidationResults.QR_CODE_SUCCESS)
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

    @NonNull List<BankCardAndAccount> getMappedCards(@NonNull List<BankCard> cards) {
        return CollectionsKt.map(
            cards,
            item ->
                new BankCardAndAccount.Builder(true)
                    .setItemBalance(item.getCardBalance())
                    .setItemCurrency(Utils.getCurrency(item.getCurrency()))
                    .setItemColor(item.getCardColor())
                    .setItemNumber(item.getCardNumber())
                    .setItemAltName(item.getCardAltName())
                    .setCardId(item.getIdCard())
                    .setCardFormattedName(
                        item.getCardServiceName().substring(
                            0,
                            item.getCardServiceName().indexOf(' ')
                        )
                    )
                    .setCardNumber(item.getCardNumber())
                    .setCardExpireDate(item.getCardExpiryDate())
                    .setCardType(item.getBankCardType())
                    .setObject(item)
                    .build()
        );
    }

    @NonNull List<BankCardAndAccount> getMappedAccounts(@NonNull List<BankAccount> accounts) {
        return CollectionsKt.map(
            accounts,
            item ->
                new BankCardAndAccount.Builder(false)
                    .setItemBalance(item.getCurrency() == 0 ? item.getBalanceInLC() : item.getBalanceInFC())
                    .setItemCurrency(Utils.getCurrency(item.getCurrency()))
                    .setItemColor(item.getAccountColor())
                    .setItemNumber(item.getAccountNumber())
                    .setItemAltName(item.getAccountAltName())
                    .setObject(item)
                    .build()
        );
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
