package az.btb.mobilebanking.ui.fingerprint;

import androidx.annotation.StringRes;

import moxy.MvpView;

interface FingerprintView extends MvpView {
    void showFingerprintMsg(@StringRes int msg);
}
