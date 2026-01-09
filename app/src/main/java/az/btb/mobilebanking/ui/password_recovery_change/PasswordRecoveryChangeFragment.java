package az.btb.mobilebanking.ui.password_recovery_change;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentRecoveryPasswordChangeBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.ChangeForgotPasswordRequest;
import az.btb.mobilebanking.models.ForgotPasswordRequest;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PasswordRecoveryChangeFragment extends Fragment<FragmentRecoveryPasswordChangeBinding> implements PasswordRecoveryChangeView {

    private String phone;
    private ForgotPasswordRequest forgotPasswordRequest;

    private CountDownTimer timer;

    @InjectPresenter PasswordRecoveryChangePresenter presenter;

    @ProvidePresenter PasswordRecoveryChangePresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PasswordRecoveryChangePresenter.class);
    }
    
    public PasswordRecoveryChangeFragment() {
        super(R.layout.fragment_recovery_password_change, false);
    }
    
    public static PasswordRecoveryChangeFragment getInstance(ForgotPasswordRequest forgotPasswordRequest, String phone) {
        Bundle args = new Bundle();
        args.putParcelable("forgotPasswordRequest", forgotPasswordRequest);
        args.putString("phone", phone);
        
        PasswordRecoveryChangeFragment fragment = new PasswordRecoveryChangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordRequest = requireArguments().getParcelable("forgotPasswordRequest");
        phone = requireArguments().getString("phone");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        startTimer();
        
        binding.verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("-") && s.length() > 3)
                    s.insert(3, "-");
            }
        });
        binding.codeError.setOnClickListener(v-> {
            if (binding.codeError.getText() == getResources().getString(R.string.send_again)){
                startTimer();
                forgotPasswordRequest.getRequestInfo().getAppInfo().setApiHash(Utils.appHash());
                presenter.sendAgain(forgotPasswordRequest);
            }
        });
        binding.textNumberVerification.setText(getResources().getString(R.string.verification) + " " + phone + " " + getResources().getString(R.string.verification_text));

        binding.passwordAgain.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });

        binding.nextButton.setOnClickListener(v -> {
            if (isFormValid()) {
                ChangeForgotPasswordRequest request = new ChangeForgotPasswordRequest(AppData.getInstance().getRequestInfo(), binding.verifyCode.getText().toString(), Utils.passwordHash(binding.passwordAgain.getText().toString()));
                presenter.sendNewPassword(request);
            }
        });
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(binding.password.getText().toString()) && TextUtils.isEmpty(binding.passwordAgain.getText().toString())) {
            showPasswordError(getString(R.string.empty_password));
            return false;
        } else {
            if (!binding.passwordAgain.getText().toString().equals(binding.password.getText().toString())) {
                showPasswordError(getString(R.string.passwords_not_match));
                return false;
            }
            return true;
        }
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.nextButton.setEnabled(check);
        binding.nextButton.setClickable(check);
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.codeError.setTextColor(getResources().getColor(R.color.textColorLight));
                binding.codeError.setText(millisUntilFinished / 1000 + " " + getResources().getString(R.string.second));
            }

            public void onFinish() {
                binding.codeError.setTextColor(getResources().getColor(R.color.mainColor));
                binding.codeError.setText(getResources().getString(R.string.send_again));
            }
        }.start();
    }

    @Override
    public void showPasswordError(String error) {
        Utils.snackbar(binding.getRoot(), error);
    }

    @Override
    public void showCodeError(String error) {
        binding.codeError.setVisibility(View.VISIBLE);
        binding.codeError.setText(error);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
//        Utils.stopForceBypassPinFingerprintScreen(requireActivity());
        timer.cancel();
    }
}
