package az.btb.mobilebanking.ui.sign_up_by_number;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByNumberBinding;
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

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_UP_TYPE_NUMBER;
import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

public class SignUpByNumberFragment extends Fragment<FragmentSignUpByNumberBinding> implements SignUpByNumberView {

    private int signUpType;
    private String verifyCode;
    private @Nullable String phone;
    private @Nullable AsanImzaData data;

    @InjectPresenter SignUpByNumberPresenter presenter;

    @ProvidePresenter SignUpByNumberPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByNumberPresenter.class);
    }
    
    public SignUpByNumberFragment() {
        super(R.layout.fragment_sign_up_by_number, false);
    }
    
    @NonNull
    public static SignUpByNumberFragment getInstance(
        int signUpType, String verifyCode, @Nullable String phone, @Nullable AsanImzaData data
    ) {
        Bundle args = new Bundle();
        args.putInt("signUpType", signUpType);
        args.putString("verifyCode", verifyCode);
        args.putString("phone", phone);
        args.putParcelable("data", data);

        SignUpByNumberFragment fragment = new SignUpByNumberFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpType = requireArguments().getInt("signUpType");
        verifyCode = requireArguments().getString("verifyCode");
        phone = requireArguments().getString("phone");
        data = requireArguments().getParcelable("data");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        if (phone != null && !phone.trim().isEmpty()) {
            binding.mobileNumber.setText(phone.substring(4));
            binding.mobileNumber.setEnabled(false);
        }

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.passwordRepeat.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });
        binding.nextButton.setOnClickListener(v -> {
            showLoading(true);

            final String phoneNumberData = binding.mobileNumber.getText().toString();
            final String passwordData = binding.password.getText().toString();
            final String passwordRepeatData = binding.passwordRepeat.getText().toString();

            if (isFormValid(phoneNumberData, passwordData, passwordRepeatData)) {
                final String passwordHash = Utils.passwordHash(passwordData);
                final MobileUser mobileUser = new MobileUser(
                    phoneNumberData.replace(" ", ""), passwordHash, null, null
                );
                final RequestInfo requestInfo = new RequestInfo(
                    mobileUser,
                    AppData.getInstance().getRequestInfo().getDeviceInfo(),
                    AppData.getInstance().getRequestInfo().getAppInfo(),
                    1
                );
                final SignUpRequest request;

                if (signUpType == SIGN_UP_TYPE_PAN)
                    request = new SignUpRequest(
                        requestInfo,
                        SIGN_IN_UP_TYPE_NUMBER,
                        signUpType,
                        AppData.getInstance().getSignUpPan(),
                        "", "",
                        verifyCode
                    );
                else {
                    request = new SignUpRequest(
                        requestInfo,
                        SIGN_IN_UP_TYPE_NUMBER,
                        signUpType,
                        "",
                        AppData.getInstance().getSignUpCif(),
                        AppData.getInstance().getSignUpDateOfBirth(),
                        verifyCode
                    );
                    if (signUpType == 3) {
                        request.setMobileNumber(data.mobileNumber);
                        request.setMobileNumberSecretNumberCode(data.mobileNumberSecretCode);
                    }
                }

                presenter.signUp(request, SIGN_IN_UP_TYPE_NUMBER, phoneNumberData, passwordHash);
            } else
                showLoading(false);
        });
    }

    private boolean isFormValid(@Nullable String numberData, String passwordData, String passwordRepeatData) {
        if (numberData == null || numberData.length() != 17) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_mobile_number);
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
