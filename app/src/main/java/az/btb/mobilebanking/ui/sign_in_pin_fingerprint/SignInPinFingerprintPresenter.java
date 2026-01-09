package az.btb.mobilebanking.ui.sign_in_pin_fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.FcmTokenRequest;
import az.btb.mobilebanking.models.KeystoreIncidentRequest;
import az.btb.mobilebanking.models.MobileUser;
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

import static android.content.Context.FINGERPRINT_SERVICE;
import static az.btb.mobilebanking.utils.Constants.CUSTOMER_NAME;
import static az.btb.mobilebanking.utils.Constants.FCM_NOTIFICATION_TOKEN;
import static az.btb.mobilebanking.utils.Constants.HAS_ACTIVE_SESSION;
import static az.btb.mobilebanking.utils.Constants.IS_FINGERPRINT_ENABLED;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_HASH;
import static az.btb.mobilebanking.utils.Constants.PIN_HASH;
import static az.btb.mobilebanking.utils.Constants.USERNAME;

@InjectViewState
public class SignInPinFingerprintPresenter extends MvpPresenter<SignInPinFingerprintView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Context context;
    private final SharedPreferences preferences;

    @Inject SignInPinFingerprintPresenter(Router router, AuthService authService, Context context, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.context = context;
        this.preferences = preferences;
    }

    void checkPin(@NonNull String hashedPin) {
        getViewState().showLoading(true);

        if (hashedPin.equals(preferences.getString(PIN_HASH, ""))) {
            MobileUser mobileUser = new MobileUser();
            mobileUser.setUsername(preferences.getString(USERNAME,"").replace(" ", ""));
            mobileUser.setPasswordHash(preferences.getString(PASSWORD_HASH,""));

            AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

            SignInRequest signInRequest = new SignInRequest(AppData.getInstance().getRequestInfo(),1,1,"","");
            signIn(signInRequest);
        } else {
            getViewState().showLoading(false);

            getViewState().showError(null);
        }
    }

    private void signIn(SignInRequest signInRequest) {
        compositeDisposable.add(
            authService
                .signIn(signInRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(
                    signInResponse -> {
                        if (signInResponse.getResponseInfo().getResponseType() == 0) {
                            preferences.edit().putBoolean(HAS_ACTIVE_SESSION, true).apply();

                            AppData.getInstance().setSessionKey(signInResponse.getSessionKey());

                            sendFCMToken();

                            return authService
                                .listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else {
                            if (signInResponse.getResponseInfo().getResponseType() == 2)
                                reportKeystoreIncident();

                            getViewState().showLoading(false);

                            throw new UnsupportedOperationException(signInResponse.getResponseInfo().getResponseMessage());
                        }
                    }
                )
                .flatMap(
                    bankCardsResponse -> {
                        if (bankCardsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankCards(bankCardsResponse.getBankCards());

                            return authService
                                .listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else
                            throw new UnsupportedOperationException(bankCardsResponse.getResponseInfo().getResponseMessage());
                    }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    bankAccountsResponse -> {
                        if (bankAccountsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankAccounts(bankAccountsResponse.getBankAccounts());

                            router.newRootScreen(new MainScreens.HomeNavScreen());
                        } else
                            getViewState().showError(bankAccountsResponse.getResponseInfo().getResponseMessage());
                    },
                    error -> {
                        getViewState().showLoading(false);
                        error.printStackTrace();
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

    private void sendFCMToken() {
        final String fcmToken = preferences.getString(FCM_NOTIFICATION_TOKEN, "");

        if (fcmToken.length() != 0) {
            FcmTokenRequest tokenRequest = new FcmTokenRequest(Utils.getCommonRequest(), fcmToken);
            compositeDisposable.add(
                authService
                    .sendFCMToken(tokenRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            );
        }
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
                        //reportKeystoreIncident();
                        performSignOutActions();
                    }
                )
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
        router.newRootScreen(new MainScreens.IntroScreen());
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

    String getCustomerFullName() {
        return preferences.getString(CUSTOMER_NAME, "");
    }

    private KeyStore keyStore;
    private Cipher cipher;

    @TargetApi(23)
    void listen() {
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

        generateKey();

        if (cipherInit()) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            new FingerprintHandler().startAuth(context, fingerprintManager, cryptoObject);
        } else
            getViewState().showError(context.getString(R.string.enter_pin));
    }

    @TargetApi(23)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
            NoSuchProviderException e) {
            throw new RuntimeException(
                "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                KeyGenParameterSpec.Builder(
                    "KEY_NAME",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
            InvalidAlgorithmParameterException
            | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(23)
    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" +
                KeyProperties.BLOCK_MODE_CBC    + "/" +
                KeyProperties.ENCRYPTION_PADDING_PKCS7
            );
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey("KEY_NAME", null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
            | UnrecoverableKeyException | IOException
            | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    boolean hasFingerprintLogin() {
        return preferences.getBoolean(IS_FINGERPRINT_ENABLED, false);
    }

    @TargetApi(23)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
        void startAuth(Context context, FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                return;

            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            getViewState().showError(context.getString(R.string.enter_pin));
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            // keep this empty...
        }

        @Override
        public void onAuthenticationFailed() {
            getViewState().showError(context.getString(R.string.enter_pin));
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            checkPin(preferences.getString(PIN_HASH, ""));
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
