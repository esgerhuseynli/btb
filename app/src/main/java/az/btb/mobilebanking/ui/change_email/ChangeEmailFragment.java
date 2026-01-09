package az.btb.mobilebanking.ui.change_email;

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentChangeEmailBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ChangeEmailFragment extends Fragment<FragmentChangeEmailBinding> implements ChangeEmailView {

    public ChangeEmailFragment() {
        super(R.layout.fragment_change_email);
    }

    public static ChangeEmailFragment getInstance() {
        return new ChangeEmailFragment();
    }

    @InjectPresenter ChangeEmailPresenter presenter;

    @ProvidePresenter ChangeEmailPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ChangeEmailPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.requestChange);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.newEmail.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.requestChange.performClick();

            return false;
        });
        binding.requestChange.setOnClickListener(v -> {
            final String emailData = binding.newEmail.getText().toString();
            if (Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
                showLoading(true);
                presenter.updateProfileData(emailData);
            } else
                Utils.snackbar(binding.getRoot(), R.string.invalid_email);
        });
    }

    @Override
    public void showLoading(boolean isLoading) {
        Utils.modifyChildrenEnableStatus(binding.root, !isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        binding.requestChange.setEnabled(!isLoading);
        binding.requestChange.setClickable(!isLoading);
    }

    @Override
    public void showError(String msg) {
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }
}
