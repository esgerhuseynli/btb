package az.btb.mobilebanking.ui.fingerprint;

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
import androidx.fragment.app.FragmentActivity;

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

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static android.content.Context.FINGERPRINT_SERVICE;
import static az.btb.mobilebanking.utils.Constants.IS_FINGERPRINT_ENABLED;

@TargetApi(23)
@InjectViewState
public class FingerprintPresenter extends MvpPresenter<FingerprintView> {

    private final Router router;
    private FragmentActivity activity;
    private final SharedPreferences preferences;

    private final FingerprintHandler fingerprintHandler = new FingerprintHandler();
    
    @Inject FingerprintPresenter(Router router, SharedPreferences preferences) {
        this.router = router;
        this.preferences = preferences;
    }

    private KeyStore keyStore;
    private Cipher cipher;

    void listen(final boolean isSignInScreen, @NonNull FragmentActivity activity) {
        this.activity = activity;
        
        FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(FINGERPRINT_SERVICE);

        generateKey();

        if (cipherInit()) {
            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
            fingerprintHandler.startAuth(isSignInScreen, activity, fingerprintManager, cryptoObject);
        } else
            goToHome(false, isSignInScreen);
    }
    
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(
                new KeyGenParameterSpec.Builder("3cowsatthesea", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            );
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
            InvalidAlgorithmParameterException
            | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
            NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey("3cowsatthesea", null);
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

    void goToHome(boolean fingerprintStatus, boolean isSignInScreen) {
        fingerprintHandler.stopAuth();
        
        preferences.edit().putBoolean(IS_FINGERPRINT_ENABLED, fingerprintStatus).apply();
    
        Utils.stopForceBypassPinFingerprintScreen(activity);
        
        if (isSignInScreen)
            router.newRootScreen(new MainScreens.HomeNavScreen());
        else
            router.replaceScreen(new MainScreens.SettingsScreen());
    }

    void goBack() {
        fingerprintHandler.stopAuth();
        router.exit();
    }

    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
        
        private boolean isSignInScreen;
        private final CancellationSignal cancellationSignal = new CancellationSignal();
        
        void startAuth(boolean isSignInScreen, Context context, FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            this.isSignInScreen = isSignInScreen;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                return;
            
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        void stopAuth() {
            cancellationSignal.cancel();
        }
        
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            // keep this empty...
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            // keep this empty...
        }

        @Override
        public void onAuthenticationFailed() {
            getViewState().showFingerprintMsg(R.string.wrong_fingerprint_data);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            goToHome(true, isSignInScreen);
        }
    }
}
