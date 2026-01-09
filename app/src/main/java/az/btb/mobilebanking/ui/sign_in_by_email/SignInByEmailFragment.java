package az.btb.mobilebanking.ui.sign_in_by_email;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignInByEmailBinding;
import az.btb.mobilebanking.databinding.GoToSignUpScreenDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignInByEmailFragment extends Fragment<FragmentSignInByEmailBinding> implements SignInByEmailView {

    private @Nullable String email;

    @InjectPresenter SignInByEmailPresenter presenter;

    @ProvidePresenter SignInByEmailPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignInByEmailPresenter.class);
    }
    
    public SignInByEmailFragment() {
        super(R.layout.fragment_sign_in_by_email, false);
    }
    
    public static SignInByEmailFragment getInstance(@Nullable String email) {
        Bundle b = new Bundle(1);
        b.putString("email", email);

        SignInByEmailFragment fragment = new SignInByEmailFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = requireArguments().getString("email");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.signIn);

        if (email != null && !email.trim().isEmpty()) {
            binding.email.setText(email);
            binding.email.setEnabled(false);
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

            final String emailData = binding.email.getText().toString();
            final String passwordData = binding.password.getText().toString();

            if (isFormValid(emailData, passwordData)) {
                MobileUser mobileUser = new MobileUser(
                    emailData.replace(" ", ""),
                    Utils.passwordHash(passwordData),
                    null, null
                );
                AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

                SignInRequest signInRequest = new SignInRequest(
                    AppData.getInstance().getRequestInfo(),
                    0,
                    1,
                    null, null
                );

                presenter.signIn(signInRequest);
            } else
                showLoading(false);
        });
    }

    private boolean isFormValid(String emailData, String passwordData) {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_email);
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
