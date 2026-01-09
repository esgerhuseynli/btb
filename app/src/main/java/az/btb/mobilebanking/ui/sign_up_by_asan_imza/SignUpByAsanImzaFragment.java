package az.btb.mobilebanking.ui.sign_up_by_asan_imza;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByAsanImzaBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpByAsanImzaFragment extends Fragment<FragmentSignUpByAsanImzaBinding> implements SignUpByAsanImzaView {

    @InjectPresenter SignUpByAsanImzaPresenter presenter;

    @ProvidePresenter SignUpByAsanImzaPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByAsanImzaPresenter.class);
    }

    public SignUpByAsanImzaFragment() {
        super(R.layout.fragment_sign_up_by_asan_imza, false);
    }

    @NonNull
    public static SignUpByAsanImzaFragment getInstance() {
        return new SignUpByAsanImzaFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.asanImzaCode.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.next.performClick();

            return false;
        });
        binding.next.setOnClickListener(v -> {
            showLoading(true);

            final String phoneNumberData = binding.mobileNumber.getText().toString();
            final String passwordData = binding.asanImzaCode.getText().toString();

            if (isFormValid(phoneNumberData, passwordData))
                presenter.goNext(phoneNumberData.replace(" ", ""), passwordData);
            else
                showLoading(false);
        });
    }

    private boolean isFormValid(@Nullable String numberData, String passwordData) {
        if (numberData == null || numberData.length() != 17) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_mobile_number);
            return false;
        }

        if (passwordData.length() != 6) {
            Utils.snackbar(binding.getRoot(), R.string.code_should_be_six);
            return false;
        }

        return true;
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.next.setEnabled(!check);
        binding.next.setClickable(!check);
    }

    @Override
    public void showError(String msg) {
        showLoading(false);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showError(int asanImzaAuthCode) {
        showLoading(false);
        int msgResId = R.string.unknown_error_occurred; // case 0
        switch (asanImzaAuthCode) {
            case 3:
                msgResId = R.string.NOT_VALID;
                break;
            case 4:
                msgResId = R.string.EXPIRED_TRANSACTION;
                break;
            case 5:
                msgResId = R.string.USER_CANCEL;
                break;
            case 6:
                msgResId = R.string.MID_NOT_READY;
                break;
            case 7:
                msgResId = R.string.SENDING_ERROR;
                break;
            case 8:
                msgResId = R.string.SIM_ERROR;
                break;
            case 9:
                msgResId = R.string.INTERNAL_ERROR;
                break;
        }
        Utils.snackbar(binding.getRoot(), msgResId);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
