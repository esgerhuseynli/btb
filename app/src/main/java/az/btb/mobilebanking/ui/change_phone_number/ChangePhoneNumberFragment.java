package az.btb.mobilebanking.ui.change_phone_number;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentChangePhoneNumberBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ChangePhoneNumberFragment extends Fragment<FragmentChangePhoneNumberBinding> implements ChangePhoneNumberView {

    public ChangePhoneNumberFragment() {
        super(R.layout.fragment_change_phone_number);
    }

    public static ChangePhoneNumberFragment getInstance() {
        return new ChangePhoneNumberFragment();
    }

    @InjectPresenter ChangePhoneNumberPresenter presenter;

    @ProvidePresenter ChangePhoneNumberPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ChangePhoneNumberPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.requestChange);

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.newNumber.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.requestChange.performClick();

            return false;
        });
        binding.requestChange.setOnClickListener(v -> {
            final String newPhoneNumberData = binding.newNumber.getText().toString();

            if (isFormValid(newPhoneNumberData)) {
                showLoading(true);
                presenter.updateProfileData(newPhoneNumberData);
            }
        });
    }

    private boolean isFormValid(String newPhoneNumberData) {
        if (newPhoneNumberData == null || newPhoneNumberData.length() != 17) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_new_phone_number_format);
            return false;
        }

        return true;
    }

    @Override
    public void showError(String msg) {
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showLoading(boolean isLoading) {
        Utils.modifyChildrenEnableStatus(binding.root, !isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        binding.requestChange.setEnabled(!isLoading);
        binding.requestChange.setClickable(!isLoading);
    }
}
