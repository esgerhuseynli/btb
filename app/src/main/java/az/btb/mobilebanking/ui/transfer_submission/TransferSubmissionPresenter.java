package az.btb.mobilebanking.ui.transfer_submission;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.OperationCard2AccountRequest;
import az.btb.mobilebanking.models.OperationCard2Card;
import az.btb.mobilebanking.models.OperationCard2CardRequest;
import az.btb.mobilebanking.models.OperationCardToAccount;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class TransferSubmissionPresenter extends MvpPresenter<TransferSubmissionView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject TransferSubmissionPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void goHome() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    void makeTransfer(@NonNull OtherCardTransferData4Accounts data) {
        getViewState().showLoading(true);

//        if (!data.destinationCardId.equals(""))
//            data.destinationCardNumber = "";

        final String transferNote = data.notes.length() == 0 ? " " : data.notes;

        switch (data.operationType) {
            case "CustomerCards": // evvelki api --> type 1
                OperationCard2CardRequest card2OwnCardRequest = new OperationCard2CardRequest(
                    Utils.getCommonRequest(),
                    new OperationCard2Card(
                        1,
                        data.sourceCardId,
                        data.destinationCardId,
                        data.destinationCardNumber.replace(" ", ""),
                        data.amount,
                        transferNote
                    )
                );

                compositeDisposable.add(
                    authService
                        .doC2CTransfer(card2OwnCardRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            response -> {
                                if (response.getResponseInfo().getResponseType() == 0)
                                    getViewState().showTransferResult(
                                        response.getBankCardOperationResult().getBankCardOperationStatus() == 1
                                    );
                                else
                                    getViewState().showError(response.getResponseInfo().getResponseMessage());
                            },
                            error -> getViewState().showError(error.getMessage())
                        )
                );
                break;

            case "OtherCustomerCard": // evvelki api --> type 2
                OperationCard2CardRequest card2OtherCardRequest = new OperationCard2CardRequest(
                    Utils.getCommonRequest(),
                    new OperationCard2Card(
                        2,
                        data.sourceCardId,
                        data.destinationCardId,
                        data.destinationCardNumber.replace(" ", ""),
                        data.amount,
                        transferNote
                    )
                );
                compositeDisposable.add(
                    authService
                        .doC2CTransfer(card2OtherCardRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            response -> {
                                if (response.getResponseInfo().getResponseType() == 0)
                                    getViewState().showTransferResult(
                                        response.getBankCardOperationResult().getBankCardOperationStatus() == 1
                                    );
                                else
                                    getViewState().showError(response.getResponseInfo().getResponseMessage());
                            },
                            error -> getViewState().showError(error.getMessage())
                        )
                );
                break;

            case "CardToAccount": // yeni api --> type 1
                OperationCard2AccountRequest cardToAccountRequest = new OperationCard2AccountRequest(
                    Utils.getCommonRequest(),
                    new OperationCardToAccount(
                        1,
                        data.sourceCardId,
                        "",
                        "",
                        "",
                        data.destinationAccountIban,
                        data.amount,
                        transferNote
                    )
                );
                compositeDisposable.add(
                    authService
                        .doC2AccountTransfer(cardToAccountRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            response -> {
                                if (response.getResponseInfo().getResponseType() == 0)
                                    getViewState().showTransferResult(
                                        response.getBankCardOperationResult().getBankCardOperationStatus() == 1
                                    );
                                else
                                    getViewState().showError(response.getResponseInfo().getResponseMessage());
                            },
                            error -> getViewState().showError(error.getMessage())
                        )
                );
                break;

            case "AccountToCard": // yeni api --> type 2
                OperationCard2AccountRequest accountToOwnCardRequest = new OperationCard2AccountRequest(
                    Utils.getCommonRequest(),
                    new OperationCardToAccount(
                        2,
                        "",
                        data.destinationCardId,
                        "",
                        data.sourceAccountIban,
                        "",
                        data.amount,
                        transferNote
                    )
                );
                compositeDisposable.add(
                    authService
                        .doC2AccountTransfer(accountToOwnCardRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            response -> {
                                if (response.getResponseInfo().getResponseType() == 0)
                                    getViewState().showTransferResult(
                                        response.getBankCardOperationResult().getBankCardOperationStatus() == 1
                                    );
                                else
                                    getViewState().showError(response.getResponseInfo().getResponseMessage());
                            },
                            error -> getViewState().showError(error.getMessage())
                        )
                );
                break;

            case "AccountToOtherCard": // yeni api --> type 3
                OperationCard2AccountRequest account2OtherCardRequest = new OperationCard2AccountRequest(
                    Utils.getCommonRequest(),
                    new OperationCardToAccount(
                        3,
                        "",
                        "",
                        data.destinationCardNumber.replace(" ", ""),
                        data.sourceAccountIban,
                        "",
                        data.amount,
                        transferNote
                    )
                );
                compositeDisposable.add(
                    authService
                        .doC2AccountTransfer(account2OtherCardRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            response -> {
                                if (response.getResponseInfo().getResponseType() == 0)
                                    getViewState().showTransferResult(
                                        response.getBankCardOperationResult().getBankCardOperationStatus() == 1
                                    );
                                else
                                    getViewState().showError(response.getResponseInfo().getResponseMessage());
                            },
                            error -> getViewState().showError(error.getMessage())
                        )
                );
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
