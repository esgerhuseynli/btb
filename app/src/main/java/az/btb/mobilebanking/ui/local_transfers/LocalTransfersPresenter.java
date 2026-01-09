package az.btb.mobilebanking.ui.local_transfers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BudgetDestination;
import az.btb.mobilebanking.models.BudgetLevelsRequest;
import az.btb.mobilebanking.models.BudgetDestinationsRequest;
import az.btb.mobilebanking.models.BudgetLevel;
import az.btb.mobilebanking.models.BudgetPaymentInfo;
import az.btb.mobilebanking.models.LocalBankBranch;
import az.btb.mobilebanking.models.LocalReceiverInfo;
import az.btb.mobilebanking.models.LocalTransferBranchesRequest;
import az.btb.mobilebanking.models.LocalTransferRequest;
import az.btb.mobilebanking.models.PayerInfo;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LocalTransfersPresenter extends MvpPresenter<LocalTransfersView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    private List<LocalBankBranch> allBranches = new ArrayList<>();
    private final MutableLiveData<List<LocalBankBranch>> mBranches = new MutableLiveData<>(allBranches);
    public final LiveData<List<LocalBankBranch>> branches = mBranches;
    
    private List<BudgetDestination> allBudgetDestinations = new ArrayList<>();
    private final MutableLiveData<List<BudgetDestination>> mBudgetDestinations = new MutableLiveData<>(allBudgetDestinations);
    public final LiveData<List<BudgetDestination>> budgetDestinations = mBudgetDestinations;
    
    private List<BudgetLevel> allBudgetLevels = new ArrayList<>();
    private final MutableLiveData<List<BudgetLevel>> mBudgetLevels = new MutableLiveData<>(allBudgetLevels);
    public final LiveData<List<BudgetLevel>> budgetLevels = mBudgetLevels;
    
    @Inject LocalTransfersPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.backTo(new MainScreens.TransfersScreen(false));
    }

    void getCards() {
        compositeDisposable.add(
            authService
                .listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        getBranches();
                        
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showCards(
                                CollectionsKt.filter(
                                    response.getBankCards(),
                                    bc -> bc.getCurrency() == 0
                                )
                            );
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void getBankAccounts() {
        compositeDisposable.add(
            authService
                .listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showAccounts(
                                CollectionsKt.filter(
                                    response.getBankAccounts(),
                                    ba -> ba.getCurrency() == 0
                                )
                            );
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }
    
    private void getBranches() {
        compositeDisposable.add(
            authService
                .getLocalTransferBranches(new LocalTransferBranchesRequest(Utils.getCommonRequest(), "", "", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            getBudgetDestinations();
                            
                            allBranches = response.getLocalBranches();
                            mBranches.postValue(allBranches);
                            getViewState().showError("");
                        }/* else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());*/
                    },
                    error -> {}
                )
        );
    }
    
    void searchBranchesByAny(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBranches.postValue(
                CollectionsKt.filter(
                    allBranches,
                    localBranch ->
                        localBranch.getBranchName().toLowerCase().contains(mCriteria) ||
                        localBranch.getBranchCode().toLowerCase().contains(mCriteria) ||
                        localBranch.getBranchTaxNumber().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBranches.postValue(allBranches);
    }
    
    void searchBranchesByName(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBranches.postValue(
                CollectionsKt.filter(
                    allBranches,
                    localBranch -> localBranch.getBranchName().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBranches.postValue(allBranches);
    }
    
    void searchBranchesByCode(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBranches.postValue(
                CollectionsKt.filter(
                    allBranches,
                    localBranch -> localBranch.getBranchCode().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBranches.postValue(allBranches);
    }
    
    void searchBranchesByTaxNumber(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBranches.postValue(
                CollectionsKt.filter(
                    allBranches,
                    localBranch -> localBranch.getBranchTaxNumber().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBranches.postValue(allBranches);
    }
    
    void getBudgetDestinations() {
        compositeDisposable.add(
            authService
                .getBudgetDestinations(new BudgetDestinationsRequest(Utils.getCommonRequest(), ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            getBudgetLevels();
                            
                            allBudgetDestinations = response.getBudgetDestinations();
                            mBudgetDestinations.postValue(allBudgetDestinations);
                            getViewState().showError("");
                        }/* else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());*/
                    },
                    error -> {}
                )
        );
    }

    void searchBudgetDestinationsBy(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBudgetDestinations.postValue(
                CollectionsKt.filter(
                    allBudgetDestinations,
                    budgetDestination ->
                        budgetDestination.getCode().toLowerCase().contains(mCriteria) ||
                        budgetDestination.getDestination().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBudgetDestinations.postValue(allBudgetDestinations);
    }
    
    void getBudgetLevels() {
        compositeDisposable.add(
            authService
                .getBudgetLevels(new BudgetLevelsRequest(Utils.getCommonRequest(), ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            allBudgetLevels = response.getBudgetLevels();
                            mBudgetLevels.postValue(allBudgetLevels);
                            getViewState().showError("");
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }
    
    void searchBudgetLevelsBy(@NonNull final String q) {
        if (!q.isEmpty()) {
            final String mCriteria = q.toLowerCase();
            mBudgetLevels.postValue(
                CollectionsKt.filter(
                    allBudgetLevels,
                    budgetLevel ->
                        budgetLevel.getCode().toLowerCase().contains(mCriteria) ||
                        budgetLevel.getLevelName().toLowerCase().contains(mCriteria)
                )
            );
        } else
            mBudgetLevels.postValue(allBudgetLevels);
    }
    
    void makeLocalTransfer(
        PayerInfo payerInfo,
        String transferNumber,
        LocalReceiverInfo localReceiverInfo,
        BigDecimal transferAmount,
        String operationDescription,
        BudgetPaymentInfo budgetPaymentInfo
    ) {
        LocalTransferRequest request = new LocalTransferRequest(
            Utils.getCommonRequest(),
            payerInfo,
            transferNumber,
            localReceiverInfo,
            transferAmount,
            operationDescription,
            budgetPaymentInfo,
            0
        );

    //    System.out.println(new Gson().toJson(request));

        compositeDisposable.add(
            authService
                .makeLocalTransfer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showSuccessResult(response.getTransferNumber());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goHome() {
//        router.newChain(new MainScreens.HomeNavScreen());
//        router.newRootChain(new MainScreens.HomeNavScreen());
        router.newRootScreen(new MainScreens.HomeNavScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
