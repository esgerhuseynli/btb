package az.btb.mobilebanking.ui.home_nav;

import android.content.SharedPreferences;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R.id;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.KeystoreIncidentRequest;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.MobileUserDataRequest;
import az.btb.mobilebanking.models.RequestInfo;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.CUSTOMER_NAME;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_HASH;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_TYPE;
import static az.btb.mobilebanking.utils.Constants.USERNAME;

@InjectViewState
public class HomeNavPresenter extends MvpPresenter<HomeNavView> {

    private Router localRouter;
    private final Router globalRouter;

    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final SharedPreferences preferences;

    @Inject HomeNavPresenter(Router globalRouter, AuthService authService, SharedPreferences preferences) {
        this.globalRouter = globalRouter;
        this.authService = authService;
        this.preferences = preferences;
    }

    void setLocalRouter(Router localRouter) {
        this.localRouter = localRouter;
    }

    void goToNotification() {
        globalRouter.navigateTo(new MainScreens.NotificationsScreen());
    }

    void sideMenuItemClicked(int itemId) {
        /* BEGIN: BottomMenu items */
        if (itemId == id.mainPage)
            localRouter.navigateTo(new MainScreens.HomeNavScreen());
        else if (itemId == id.paymentsPage)
            localRouter.navigateTo(new MainScreens.PaymentsScreen(false));
        else if (itemId == id.notificationsPage)
            localRouter.navigateTo(new MainScreens.NotificationsScreen());
        else if (itemId == id.transfersPage)
            localRouter.navigateTo(new MainScreens.TransfersScreen(false));
        /* END: BottomMenu items */

        /* SideMenu items */
        else if (itemId == id.cif)
            globalRouter.navigateTo(new MainScreens.ProfileScreen());

        else if (itemId == id.cards)
            globalRouter.navigateTo(new MainScreens.MyCardsScreen());

        else if (itemId == id.accounts)
            globalRouter.navigateTo(new MainScreens.MyAccountsScreen());

        else if (itemId == id.loans)
            globalRouter.navigateTo(new MainScreens.MyLoansScreen());

        else if (itemId == id.deposits)
            globalRouter.navigateTo(new MainScreens.MyDepositsScreen());

        else if (itemId == id.payments)
            globalRouter.navigateTo(new MainScreens.PaymentsScreen(false));

        else if (itemId == id.transfers)
            globalRouter.navigateTo(new MainScreens.TransfersScreen(false));

        else if (itemId == id.operations_history)
            globalRouter.navigateTo(new MainScreens.OperationsHistoryScreen());

        else if (itemId == id.products)
            globalRouter.navigateTo(new MainScreens.ProductsScreen());

        else if (itemId == id.exchangeRates)
            globalRouter.navigateTo(new MainScreens.ExchangeRatesScreen());

        else if (itemId == id.news)
            globalRouter.navigateTo(new MainScreens.NewsScreen());

//            else if (itemId == id.proposals)
//                globalRouter.navigateTo(new MainScreens.ProposalsScreen());

        else if (itemId == id.servicePoints)
            globalRouter.navigateTo(new MainScreens.ServicePointsScreen());

        else if (itemId == id.contacts)
            globalRouter.navigateTo(new MainScreens.ContactsScreen());

        else if (itemId == id.settings)
            globalRouter.navigateTo(new MainScreens.SettingsScreen());

        // Handled in fragment...
//        else if (itemId == id.logout)
//            this.signOut();
    }

    void getAndShowCustomerName() {
        final int signInType = preferences.getInt(SIGN_IN_TYPE, 0);
        final String username = preferences.getString(USERNAME, "");
        final String password = preferences.getString(PASSWORD_HASH, "");

        RequestInfo requestInfo = AppData.getInstance().getRequestInfo();
        requestInfo.getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey());

        compositeDisposable.add(
            authService
                .getMobileUserData(new MobileUserDataRequest(requestInfo, signInType, username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        preferences.edit().putString(CUSTOMER_NAME, response.getMobileUserData().getCustomerName()).apply();
                        getViewState().showCustomerName(response.getMobileUserData().getCustomerName());
                    },
                    error -> { }
                )
        );
    }

    void signOut() {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setUsername(preferences.getString(USERNAME,"").replace(" ", ""));
        mobileUser.setPasswordHash(preferences.getString(PASSWORD_HASH,""));

        AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

        SignInRequest signInRequestForSignOut = new SignInRequest(AppData.getInstance().getRequestInfo(),1,1,"","");

        compositeDisposable.add(
            authService
                .signIn(signInRequestForSignOut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    signInResponse -> {
                        if (signInResponse.getResponseInfo().getResponseType() == 0) {
                            AppData.getInstance().setSessionKey(signInResponse.getSessionKey());
                            performSignOut();
                        } else {
                            if (signInResponse.getResponseInfo().getResponseType() == 2)
                                reportKeystoreIncident();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        //reportKeystoreIncident();
                        performSignOutActions();
                    }
                )
        );
    }

    private void reportKeystoreIncident() {
        compositeDisposable.add(
            authService
                .keystoreIncident(
                    // "incidentType = 1" means "OpenFaultAttempt"
                    new KeystoreIncidentRequest(Utils.getCommonRequest(), 1, 0)
                )
                .subscribeOn(Schedulers.io())
                .subscribe()
        );
    }

    private void performSignOut() {
        compositeDisposable.add(
            authService
                .signOut(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    signOutResponse -> performSignOutActions(),
                    error -> performSignOutActions()
                )
        );
    }

    private void performSignOutActions() {
        getViewState().clearAccountData();
        getViewState().showLoading(false);
        globalRouter.newRootScreen(new MainScreens.IntroScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
