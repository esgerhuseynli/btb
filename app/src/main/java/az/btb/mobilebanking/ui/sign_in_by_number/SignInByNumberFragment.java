package az.btb.mobilebanking.ui.sign_in_by_number;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignInByNumberBinding;
import az.btb.mobilebanking.databinding.GoToSignUpScreenDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignInByNumberFragment extends Fragment<FragmentSignInByNumberBinding> implements SignInByNumberView {

    private @Nullable String phone;

    @InjectPresenter SignInByNumberPresenter presenter;

    @ProvidePresenter SignInByNumberPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignInByNumberPresenter.class);
    }
    
    public SignInByNumberFragment() {
        super(R.layout.fragment_sign_in_by_number, false);
    }
    
    @NonNull
    public static SignInByNumberFragment getInstance(@Nullable String phone) {
        Bundle b = new Bundle(1);
        b.putString("phone", phone);

        SignInByNumberFragment fragment = new SignInByNumberFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = requireArguments().getString("phone");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.signIn);

        if (phone != null && !phone.trim().isEmpty()) {
            binding.mobileNumber.setText(phone.substring(4));
            binding.mobileNumber.setEnabled(false);
        }

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.forgetPasswordButton.setOnClickListener(v -> presenter.goToForgotPassword());
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.signIn.performClick();

            return false;
        });
        binding.signIn.setOnClickListener(v -> {
            showLoading(true);

            final String phoneNumberData = binding.mobileNumber.getText().toString();
            final String passwordData = binding.password.getText().toString();

            if (isFormValid(phoneNumberData, passwordData))
                presenter.signIn(phoneNumberData, passwordData);
            else
                showLoading(false);
        });
    }

    private boolean isFormValid(@Nullable String numberData, String passwordData) {
        if (numberData == null || numberData.length() != 17) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_mobile_number);
            return false;
        }

        if (passwordData.length() == 0) {
            Utils.snackbar(binding.getRoot(), R.string.empty_password);
            return false;
        }

        return true;
    }

    @Override
    public void killActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void showSignUpInfo() {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final GoToSignUpScreenDialogBinding successDialogBinding = GoToSignUpScreenDialogBinding.inflate(getLayoutInflater());
        successDialogBinding.finish.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goToSignUp();
        });

        dialog.setView(successDialogBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.signIn.setEnabled(!check);
        binding.signIn.setClickable(!check);
    }

    @Override
    public void showError(String message) {
        if (!message.isEmpty())
        Utils.snackbar(binding.getRoot(), message);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
