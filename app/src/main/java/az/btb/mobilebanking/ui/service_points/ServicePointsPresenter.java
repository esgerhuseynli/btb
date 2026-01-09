package az.btb.mobilebanking.ui.service_points;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankATM;
import az.btb.mobilebanking.models.BankBranch;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.models.ServicePoint;
import az.btb.mobilebanking.models.ServicePointAddress;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SERVICE_POINT_TYPE_ATM;
import static az.btb.mobilebanking.utils.Constants.SERVICE_POINT_TYPE_BRANCH;

@InjectViewState
public class ServicePointsPresenter extends MvpPresenter<ServicePointsView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ServicePointsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    @NonNull
    private RequestInfoRequest getAtmRequest() {
        return new RequestInfoRequest(Utils.getCommonRequest());
    }

    @NonNull
    private RequestInfoRequest getBranchRequest() {
        return new RequestInfoRequest(Utils.getCommonRequest());
    }

    void getServicePoints() {
        final List<ServicePoint> atms = new ArrayList<>();
        final List<ServicePoint> branches = new ArrayList<>();

        compositeDisposable.add(
            authService
                .getBankATMs(getAtmRequest())
                .subscribeOn(Schedulers.io())
                .flatMap(bankATMsResponse -> {
                    if (bankATMsResponse.getResponseInfo().getResponseType() == 0) {
                        for (BankATM atm : bankATMsResponse.getBankATMs()) {
                            ServicePointAddress atmPointAddress = atm.getBankAtmAddress();

                            if (!Objects.equals(atmPointAddress.getCoordinateX(), "") && !Objects.equals(atmPointAddress.getCoordinateY(), "")) {
                                ServicePoint atmPoint = new ServicePoint(SERVICE_POINT_TYPE_ATM);
                                atmPoint.setName(atm.getAtmName());
                                atmPoint.setAddress(atmPointAddress);
                                atmPoint.setWorkingDays(atm.getWorkingDays());
                                atmPoint.setWorkingHours(atm.getWorkingHours());
                                atmPoint.setStatus(atm.getAtmStatus());

                                atms.add(atmPoint);
                            }
                        }

                        return authService.getBankBranches(getBranchRequest());
                    } else
                        throw new RuntimeException(bankATMsResponse.getResponseInfo().getResponseMessage());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    bankBranchesResponse -> {
                        if (bankBranchesResponse.getResponseInfo().getResponseType() == 0) {
                            for (BankBranch branch : bankBranchesResponse.getBankBranches()) {
                                ServicePointAddress branchPointAddress = branch.getBankBranchAddress();

                                if (!Objects.equals(branchPointAddress.getCoordinateX(), "") && !Objects.equals(branchPointAddress.getCoordinateY(), "")) {
                                    ServicePoint branchPoint = new ServicePoint(SERVICE_POINT_TYPE_BRANCH);
                                    branchPoint.setName(branch.getBranchName());
                                    branchPoint.setAddress(branchPointAddress);
                                    branchPoint.setWorkingDays(branch.getWorkingDays());
                                    branchPoint.setWorkingHours(branch.getWorkingHours());
                                    branchPoint.setStatus(branch.getBranchStatus());
                                    branchPoint.setBranchCode(branch.getCode());
                                    branchPoint.setBranchCorrespondentAccount(branch.getCorrespondentAccount());
                                    branchPoint.setBranchFaxes(branch.getFaxes());
                                    branchPoint.setBranchPhones(branch.getPhones());
                                    branchPoint.setBranchSwift(branch.getSwiftBIC());

                                    branches.add(branchPoint);
                                }
                            }

                            getViewState().setServicePointsData(Pair.create(atms, branches));
                        } else
                            throw new RuntimeException(bankBranchesResponse.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )

            // The idea have taken from:
            // https://proandroiddev.com/rxjava-2-parallel-multiple-network-call-made-easy-1e1f14163eef
//            Observable.zip(
//                authService.getBankATMs(atmz).subscribeOn(Schedulers.io()),
//                authService.getBankBranches(branchz).subscribeOn(Schedulers.io()),
//                Pair::create
//            ).observeOn(AndroidSchedulers.mainThread()).subscribe(bankATMsResponseBankBranchesResponsePair -> {
//                BankATMsResponse atmsResponse = bankATMsResponseBankBranchesResponsePair.first;
//                BankBranchesResponse branchesResponse = bankATMsResponseBankBranchesResponsePair.second;
//
//                if (atmsResponse.getResponseInfo().getResponseType() == 0) {
//                    if (branchesResponse.getResponseInfo().getResponseType() == 0) {
//                        final List<ServicePoint> atms = new ArrayList<>(atmsResponse.getBankATMs().size());
//                        final List<ServicePoint> branches = new ArrayList<>(branchesResponse.getBankBranches().size());
//
//                        for (BankATM atm : atmsResponse.getBankATMs()) {
//                            ServicePoint atmPoint = new ServicePoint(SERVICE_POINT_TYPE_ATM);
//                            atmPoint.setName(atm.getAtmName());
//                            atmPoint.setAddress(atm.getBankAtmAddress());
//                            atmPoint.setWorkingDays(atm.getWorkingDays());
//                            atmPoint.setWorkingHours(atm.getWorkingHours());
//                            atmPoint.setStatus(atm.getAtmStatus());
//
//                            atms.add(atmPoint);
//                        }
//
//                        for (BankBranch branch : branchesResponse.getBankBranches()) {
//                            ServicePoint branchPoint = new ServicePoint(SERVICE_POINT_TYPE_BRANCH);
//                            branchPoint.setName(branch.getBranchName());
//                            branchPoint.setAddress(branch.getBankBranchAddress());
//                            branchPoint.setWorkingDays(branch.getWorkingDays());
//                            branchPoint.setWorkingHours(branch.getWorkingHours());
//                            branchPoint.setStatus(branch.getBranchStatus());
//
//                            branches.add(branchPoint);
//                        }
//
//                        getViewState().setServicePointsData(Pair.create(atms, branches));
//                    } else {
//                        System.out.println("branchError: " + branchesResponse.getResponseInfo().getErrorMessage());
//                        getViewState().showError(branchesResponse.getResponseInfo().getResponseMessage());
//                    }
//                } else {
//                    System.out.println("atmError: " + atmsResponse.getResponseInfo().getErrorMessage());
//                    getViewState().showError(atmsResponse.getResponseInfo().getResponseMessage());
//                }
//            })
        );
    }

    public void goBack() {
        router.exit();
    }

    public void goToAtmDetails(ServicePoint atmPoint) {
        router.navigateTo(new MainScreens.AtmDetailsScreen(atmPoint));
    }

    public void goToBranchDetails(ServicePoint branchPoint) {
        router.navigateTo(new MainScreens.BranchDetailsScreen(branchPoint));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
