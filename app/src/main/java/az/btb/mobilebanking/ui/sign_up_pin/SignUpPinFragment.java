package az.btb.mobilebanking.ui.sign_up_pin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpPinBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpPinFragment extends Fragment<FragmentSignUpPinBinding> implements SignUpPinView, View.OnTouchListener {

    private String pin = "";
    private String pin1 = "";
    private String pin2 = "";

    private boolean buttons = true;

    private int signUpType;
    private String username;
    private String password;
    private boolean isComingFromSignInScreen;

    @InjectPresenter SignUpPinPresenter presenter;

    @ProvidePresenter SignUpPinPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpPinPresenter.class);
    }

    public SignUpPinFragment() {
        super(R.layout.fragment_sign_up_pin, false);
    }

    public static SignUpPinFragment getInstance(int signUpType, String username, String password, boolean isComingFromSignInScreen) {
        final Bundle args = new Bundle();
        args.putInt("signUpType", signUpType);
        args.putString("username", username);
        args.putString("password", password);
        args.putBoolean("isComingFromSignInScreen", isComingFromSignInScreen);

        final SignUpPinFragment fragment = new SignUpPinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpType = requireArguments().getInt("signUpType");
        username = requireArguments().getString("username");
        password = requireArguments().getString("password");
        isComingFromSignInScreen = requireArguments().getBoolean("isComingFromSignInScreen");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.numberX.setOnClickListener(v -> {
            binding.pinText.setText(getResources().getString(R.string.put_pin_code));

            pin = "";
            pin1 = "";
            pin2 = "";
            clearPins();
        });

        binding.number0.setOnTouchListener(this);
        binding.number1.setOnTouchListener(this);
        binding.number2.setOnTouchListener(this);
        binding.number3.setOnTouchListener(this);
        binding.number4.setOnTouchListener(this);
        binding.number5.setOnTouchListener(this);
        binding.number6.setOnTouchListener(this);
        binding.number7.setOnTouchListener(this);
        binding.number8.setOnTouchListener(this);
        binding.number9.setOnTouchListener(this);
    }

    private void clearPins() {
        binding.pin1.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin2.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin3.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
    }

    @Override
    public void progressBarState(boolean isLoading) {
        new Handler(Looper.getMainLooper()).post(
            () -> binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );
    }

    @Override
    public void showError(String error) {
        new Handler(Looper.getMainLooper()).post(() -> {
            binding.pinText2.setVisibility(View.VISIBLE);
            binding.pinText2.setText(error);
        });
    }

    @Override
    public void disableButtons(boolean disabled) {
        buttons = false;
    }

    @Override
    public void setAppBankCards(List<BankCard> bankCards) {
        refreshBankCards(bankCards);
    }

    @Override
    public void setAppBankAccounts(List<BankAccount> bankAccounts) {
        refreshBankAccounts(bankAccounts);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (buttons) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_selected));
                pin = pin + v.getTag();
                final int pinLength = pin.length();
                if (pinLength == 1)
                    binding.pin1.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 2)
                    binding.pin2.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 3)
                    binding.pin3.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                else if (pinLength == 4) {
                    if (!pin1.isEmpty()) {
                        pin2 = pin;
                        pin = "";
                        binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                    }
                    if (pin1.isEmpty()) {
                        pin1 = pin;
                        pin = "";
                        binding.pinText.setText(R.string.put_pin_code_again);
                        clearPins();
                    }
                }

                if (pin1.length() == 4 && pin2.length() == 4) {
                    if (!pin1.equals(pin2)) {
                        binding.numberX.performClick();
                        binding.pinText.setText(R.string.put_pin_wrong);
                    } else {
                        binding.pinText2.setText(R.string.put_pin_correct);

                        presenter.finishSignUp(signUpType, username, password, pin2, isComingFromSignInScreen, requireActivity());

                        progressBarState(true);

                        v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP)
                v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
        }
        return true;
    }
}
