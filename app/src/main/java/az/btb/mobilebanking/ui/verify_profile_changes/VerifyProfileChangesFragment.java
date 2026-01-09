package az.btb.mobilebanking.ui.verify_profile_changes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentVerifyProfileChangesBinding;
import az.btb.mobilebanking.databinding.ProfileUpdateSuccessDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.NewMobileUserData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.PROFILE_UPDATE_TYPE_EMAIL;
import static az.btb.mobilebanking.utils.Constants.PROFILE_UPDATE_TYPE_MOBILE_NUMBER;
import static az.btb.mobilebanking.utils.Constants.PROFILE_UPDATE_VERIFICATION_TYPE_EMAIL;
import static az.btb.mobilebanking.utils.Constants.PROFILE_UPDATE_VERIFICATION_TYPE_MOBILE_NUMBER;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_UP_TYPE_EMAIL;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_UP_TYPE_NUMBER;

public class VerifyProfileChangesFragment extends Fragment<FragmentVerifyProfileChangesBinding> implements VerifyProfileChangesView {

    private NewMobileUserData newMobileUserData;

    private CountDownTimer timer;

    public VerifyProfileChangesFragment() {
        super(R.layout.fragment_verify_profile_changes, false);
    }

    public static VerifyProfileChangesFragment getInstance(NewMobileUserData newMobileUserData) {
        Bundle args = new Bundle();
        args.putParcelable("newMobileUserData", newMobileUserData);

        VerifyProfileChangesFragment fragment = new VerifyProfileChangesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter VerifyProfileChangesPresenter presenter;

    @ProvidePresenter VerifyProfileChangesPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(VerifyProfileChangesPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newMobileUserData = requireArguments().getParcelable("newMobileUserData");

        startTimer();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.hideSubmitButton(binding.getRoot(), binding.nextButton);

        checkCode();
        showLoading(false);

        binding.second.setOnClickListener(v -> {
            if (binding.second.getText() == getResources().getString(R.string.send_again)) {
               // binding.password.setText("");

                startTimer();

                presenter.sendAgain(newMobileUserData);
            }
        });

        final int mode = newMobileUserData.getChangeMobileUserDataMode();
        if (mode == PROFILE_UPDATE_TYPE_EMAIL)
            binding.textNumber.setText(String.format(getResources().getString(R.string.verify_code_sent_to_email), newMobileUserData.getEmail()));
        else if (mode == PROFILE_UPDATE_TYPE_MOBILE_NUMBER)
            binding.textNumber.setText(String.format(getResources().getString(R.string.verify_code_sent_to_number), newMobileUserData.getMobileNumber()));
        else
            binding.textNumber.setText(getString(R.string.verification) + " " + getString(R.string.verification_text));

        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.nextButton.performClick();

            return false;
        });
        binding.nextButton.setOnClickListener(v -> {
            binding.error.setVisibility(View.GONE);

            presenter.completeVerification(
                newMobileUserData.getChangeMobileUserDataMode(),
                binding.password.getText().toString()
            );
        });
    }

    private void checkCode() {
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 7)
                    showLoading(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showLoading(s.length() == 7);
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
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.GONE);
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
    public void clearCode() {
        binding.second.setTextColor(getResources().getColor(R.color.mainColor));
        binding.second.setText(getResources().getString(R.string.send_again));
    }

    @Override
    public void showSuccessDialog(final int type, int signInUpType) {
        boolean shouldBeRedirectedToSignIn = false;

        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final ProfileUpdateSuccessDialogBinding successDialogBinding = ProfileUpdateSuccessDialogBinding.inflate(getLayoutInflater());

        if (type == PROFILE_UPDATE_VERIFICATION_TYPE_EMAIL) {
            successDialogBinding.setUpdatedItem(getString(R.string.email_update_finish));
            if (signInUpType == SIGN_IN_UP_TYPE_EMAIL) {
                shouldBeRedirectedToSignIn = true;
                successDialogBinding.setMessageEnding(getString(R.string.going_to_sign_in_page));
            }
        } else if (type == PROFILE_UPDATE_VERIFICATION_TYPE_MOBILE_NUMBER) {
            successDialogBinding.setUpdatedItem(getString(R.string.mobile_number_update_finish));
            if (signInUpType == SIGN_IN_UP_TYPE_NUMBER) {
                shouldBeRedirectedToSignIn = true;
                successDialogBinding.setMessageEnding(getString(R.string.going_to_sign_in_page));
            }
        } else
            successDialogBinding.setUpdatedItem(getString(R.string.password_update_finish));

        final boolean finalShouldBeRedirectedToSignIn = shouldBeRedirectedToSignIn;
        successDialogBinding.finish.setOnClickListener(v -> {
            dialog.dismiss();
            if (finalShouldBeRedirectedToSignIn) {
                showLoading(true);
                refreshBankCards(new ArrayList<>());
                refreshBankAccounts(new ArrayList<>());
                presenter.signOut();
            } else
                presenter.goHome();
        });

        dialog.setView(successDialogBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void clearAccountData() {
        Utils.postSignOutCleanUp(requireActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
