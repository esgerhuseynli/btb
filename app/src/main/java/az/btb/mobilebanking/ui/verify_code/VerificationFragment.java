package az.btb.mobilebanking.ui.verify_code;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentVerificationBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.models.VerifyCodeRequest;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_CIF;
import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

public class VerificationFragment extends Fragment<FragmentVerificationBinding> implements VerificationView {

    private int requestType;
    @Nullable private String phone;
    @Nullable private String email;

    private CountDownTimer timer;

    private CardSendRequest cardSendRequest;
    
    public VerificationFragment() {
        super(R.layout.fragment_verification, false);
    }
    
    @NonNull
    public static VerificationFragment getInstance(int requestType, @Nullable String phone, @Nullable String email) {
        Bundle args = new Bundle();
        args.putInt("type", requestType);
        args.putString("phone", phone);
        args.putString("email", email);
        VerificationFragment fragment = new VerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter VerificationPresenter presenter;

    @ProvidePresenter VerificationPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(VerificationPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimer();
        requestType = requireArguments().getInt("type");
        phone = requireArguments().getString("phone");
        email = requireArguments().getString("email");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        checkCode();
        showLoading(false);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.second.setOnClickListener(v -> {
            if (binding.second.getText() == getResources().getString(R.string.send_again)) {
                startTimer();

                if (requestType == SIGN_UP_TYPE_PAN)
                    cardSendRequest = new CardSendRequest(
                        AppData.getInstance().getRequestInfo(),
                        SIGN_UP_TYPE_PAN,
                        AppData.getInstance().getSignUpPan(),
                        "", "", "", ""
                    );
                else
                    cardSendRequest = new CardSendRequest(
                        AppData.getInstance().getRequestInfo(),
                        SIGN_UP_TYPE_CIF, "",
                        AppData.getInstance().getSignUpCif(),
                        AppData.getInstance().getSignUpDateOfBirth(),
                        "", ""
                    );

                presenter.sendAgain(cardSendRequest);
            }
        });
        binding.textNumber.setText(getResources().getString(R.string.verification) + " " + phone + " " + getResources().getString(R.string.verification_text));
        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });
        binding.nextButton.setOnClickListener(v -> {
            showLoading(true);
            binding.error.setVisibility(View.GONE);

            VerifyCodeRequest verifyCodeRequest;
            if (requestType == SIGN_UP_TYPE_PAN)
                verifyCodeRequest = new VerifyCodeRequest(
                    AppData.getInstance().getRequestInfo(),
                    SIGN_UP_TYPE_PAN,
                    AppData.getInstance().getSignUpPan(),
                    "", "",
                    binding.password.getText().toString()
                );
            else
                verifyCodeRequest = new VerifyCodeRequest(
                    AppData.getInstance().getRequestInfo(),
                    SIGN_UP_TYPE_CIF,
                    "",
                    AppData.getInstance().getSignUpCif(),
                    AppData.getInstance().getSignUpDateOfBirth(),
                    binding.password.getText().toString()
                );

            presenter.goToSignIn(requestType, verifyCodeRequest, phone, email);
        });
    }

    private void checkCode() {
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 7) {
                    binding.nextButton.setEnabled(true);
                    binding.nextButton.setClickable(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.nextButton.setEnabled(s.length() == 7);
                binding.nextButton.setClickable(s.length() == 7);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("-") && s.length() > 3)
                    s.insert(3, "-");
            }
        });
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.second.setTextColor(getResources().getColor(R.color.textColorLight));
                binding.second.setText(millisUntilFinished / 1000 + " " + getResources().getString(R.string.second));
            }

            public void onFinish() {
                binding.second.setTextColor(getResources().getColor(R.color.mainColor));
                binding.second.setText(getResources().getString(R.string.send_again));
            }
        }.start();
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);

        binding.nextButton.setEnabled(check);
        binding.nextButton.setClickable(check);
    }

    @Override
    public void showError(String error) {
        if (!error.isEmpty()) {
            binding.error.setVisibility(View.VISIBLE);
            binding.error.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.error.setText(error);
        }
        else
            binding.error.setVisibility(View.GONE);
    }
    
    @Override
    public void showError(@StringRes int error) {
        binding.error.setVisibility(View.VISIBLE);
        binding.error.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        binding.error.setText(error);
    }

    @Override
    public void clearCode() {
        binding.second.setTextColor(getResources().getColor(R.color.mainColor));
        binding.second.setText(getResources().getString(R.string.send_again));
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
