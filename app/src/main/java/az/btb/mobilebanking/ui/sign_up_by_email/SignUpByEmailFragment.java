package az.btb.mobilebanking.ui.sign_up_by_email;

import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByEmailBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.RequestInfo;
import az.btb.mobilebanking.models.SignUpRequest;
import az.btb.mobilebanking.utils.AsanImzaData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_UP_TYPE_EMAIL;
import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

public class SignUpByEmailFragment extends Fragment<FragmentSignUpByEmailBinding> implements SignUpByEmailView {

    private int signUpType;
    private String verifyCode;
    private @Nullable String email;
    private @Nullable AsanImzaData data;

    @InjectPresenter SignUpByEmailPresenter presenter;

    @ProvidePresenter SignUpByEmailPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByEmailPresenter.class);
    }
    
    public SignUpByEmailFragment() {
        super(R.layout.fragment_sign_up_by_email, false);
    }
    
    @NonNull
    public static SignUpByEmailFragment getInstance(
        int signUpType, String verifyCode, @Nullable String email, @Nullable AsanImzaData data
    ) {
        Bundle args = new Bundle();
        args.putInt("signUpType", signUpType);
        args.putString("verifyCode", verifyCode);
        args.putString("email", email);
        args.putParcelable("data", data);

        SignUpByEmailFragment fragment = new SignUpByEmailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpType = requireArguments().getInt("signUpType");
        verifyCode = requireArguments().getString("verifyCode");
        email = requireArguments().getString("email");
        data = requireArguments().getParcelable("data");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        if (email != null && !email.trim().isEmpty()) {
            binding.email.setText(email);
            binding.email.setEnabled(false);
        }

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.passwordRepeat.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });
        binding.nextButton.setOnClickListener(v -> {
            final String emailData = binding.email.getText().toString();
            final String passwordData = binding.password.getText().toString();
            final String passwordRepeatData = binding.passwordRepeat.getText().toString();

            if (isFormValid(emailData, passwordData, passwordRepeatData)) {
                showLoading(true);

                final String passwordHash = Utils.passwordHash(binding.password.getText().toString());
                final MobileUser mobileUser = new MobileUser(
                    emailData.replace(" ", ""),
                    passwordHash,
                    null, null
                );
                final RequestInfo requestInfo = new RequestInfo(
                    mobileUser,
                    AppData.getInstance().getRequestInfo().getDeviceInfo(),
                    AppData.getInstance().getRequestInfo().getAppInfo(),
                    1
                );
                final SignUpRequest request;

                // usernameType = 1 it means it is a registration request with an email
                if (signUpType == SIGN_UP_TYPE_PAN)
                    request = new SignUpRequest(
                        requestInfo,
                        SIGN_IN_UP_TYPE_EMAIL,
                        signUpType,
                        AppData.getInstance().getSignUpPan(),
                        "", "",
                        verifyCode
                    );
                else {
                    request = new SignUpRequest(
                        requestInfo,
                        SIGN_IN_UP_TYPE_EMAIL,
                        signUpType, "",
                        AppData.getInstance().getSignUpCif(),
                        AppData.getInstance().getSignUpDateOfBirth(),
                        verifyCode
                    );
                    if (signUpType == 3) {
                        request.setMobileNumber(data.mobileNumber);
                        request.setMobileNumberSecretNumberCode(data.mobileNumberSecretCode);
                    }
                }

                presenter.signUp(request, SIGN_IN_UP_TYPE_EMAIL, emailData, passwordHash);
            } else
                showLoading(false);
        });
    }

    private boolean isFormValid(String emailData, String passwordData, String passwordRepeatData) {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_email);
            return false;
        }

        if (passwordData.length() == 0 && passwordRepeatData.length() == 0) {
            Utils.snackbar(binding.getRoot(), R.string.empty_password);
            return false;
        }

        if (!passwordData.equals(passwordRepeatData)) {
            Utils.snackbar(binding.getRoot(), R.string.passwords_not_match);
            return false;
        }

        return true;
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.nextButton.setEnabled(!check);
        binding.nextButton.setClickable(!check);
    }

    @Override
    public void showError(String error) {
        if (!error.isEmpty())
            Utils.snackbar(binding.getRoot(), error);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
