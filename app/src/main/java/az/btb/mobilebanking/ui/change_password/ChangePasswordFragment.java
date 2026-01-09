package az.btb.mobilebanking.ui.change_password;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentChangePasswordBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ChangePasswordFragment extends Fragment<FragmentChangePasswordBinding> implements ChangePasswordView {

    public ChangePasswordFragment() {
        super(R.layout.fragment_change_password);
    }

    public static ChangePasswordFragment getInstance() {
        return new ChangePasswordFragment();
    }

    @InjectPresenter ChangePasswordPresenter presenter;

    @ProvidePresenter ChangePasswordPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ChangePasswordPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.requestChange);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.newPasswordRepeat.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.requestChange.performClick();

            return false;
        });
        binding.requestChange.setOnClickListener(v -> {
            final String currentPasswordData = binding.currentPassword.getText().toString();
            final String newPasswordData = binding.newPassword.getText().toString();
            final String newPasswordRepeatData = binding.newPasswordRepeat.getText().toString();

            if (isFormValid(currentPasswordData, newPasswordData, newPasswordRepeatData)) {
                showLoading(true);
                presenter.updateProfileData(currentPasswordData, newPasswordData);
            }
        });
    }

    private boolean isFormValid(String currentPassword, String newPassword, String newPasswordRepeat) {
        if (currentPassword.trim().length() == 0) {
            showError(getString(R.string.enter_current_password));
            return false;
        }

        if (newPassword.trim().length() == 0) {
            showError(getString(R.string.enter_new_password));
            return false;
        }

        if (newPasswordRepeat.trim().length() == 0) {
            showError(getString(R.string.enter_new_password_repeat));
            return false;
        }

        if (!newPassword.equals(newPasswordRepeat)) {
            showError(getString(R.string.passwords_not_match));
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
