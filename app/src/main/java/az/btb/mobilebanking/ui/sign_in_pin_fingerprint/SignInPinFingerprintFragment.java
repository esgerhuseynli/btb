package az.btb.mobilebanking.ui.sign_in_pin_fingerprint;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignInPinFingerprintBinding;
import az.btb.mobilebanking.databinding.SignOutConfirmerDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignInPinFingerprintFragment extends Fragment<FragmentSignInPinFingerprintBinding> implements SignInPinFingerprintView, View.OnTouchListener {

    private String pin = "";

    @InjectPresenter SignInPinFingerprintPresenter presenter;

    @ProvidePresenter SignInPinFingerprintPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignInPinFingerprintPresenter.class);
    }

    public SignInPinFingerprintFragment() {
        super(R.layout.fragment_sign_in_pin_fingerprint, false);
    }

    public static SignInPinFingerprintFragment getInstance() {
        return new SignInPinFingerprintFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setCustomerName(presenter.getCustomerFullName());

        final boolean hasFingerprintLogin = presenter.hasFingerprintLogin();
        binding.fingerprint.setVisibility(hasFingerprintLogin ? View.VISIBLE : View.GONE);

        // read shared prefs data and send them to 'listen'
        if (hasFingerprintLogin && Utils.isFingerprintServiceAvailable(getContext()))
            presenter.listen();

        binding.logout.setOnClickListener(v -> {
            final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            final SignOutConfirmerDialogBinding dialogBinding =
                SignOutConfirmerDialogBinding.inflate(getLayoutInflater());

            dialogBinding.no.setOnClickListener(dv -> dialog.dismiss());
            dialogBinding.yes.setOnClickListener(dv -> {
                showLoading(true);
                dialog.dismiss();
                refreshBankCards(new ArrayList<>());
                refreshBankAccounts(new ArrayList<>());
                presenter.signOut();
            });

            dialog.setView(dialogBinding.getRoot());
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });

        binding.numberX.setOnClickListener(v -> {
            pin = "";
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
        pin = "";
        binding.pin1.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin2.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin3.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
        binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_not_selected));
    }

    @Override
    public void showError(@Nullable String msg) {
        clearPins();
        if (msg == null)
            binding.pinText2.setText(R.string.put_pin_wrong);
        else {
            binding.progressBar.setVisibility(View.GONE);
            if (!msg.isEmpty())
                Utils.snackbar(binding.getRoot(), msg);
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        Utils.modifyChildrenEnableStatus(binding.root, !isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void clearAccountData() {
        refreshBankCards(new ArrayList<>());
        refreshBankAccounts(new ArrayList<>());
        Utils.postSignOutCleanUp(requireActivity());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
                binding.pin4.setBackground(Utils.getDrawable(requireContext(), R.drawable.pin_count_selected));
                v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
                presenter.checkPin(Utils.passwordHash(pin));
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP)
            v.setBackground(Utils.getDrawable(requireContext(), R.drawable.shape_pin_number_not_selected));
        return true;
    }

    @Override
    public void setAppBankCards(List<BankCard> bankCards) {
        refreshBankCards(bankCards);
    }

    @Override
    public void setAppBankAccounts(List<BankAccount> bankAccounts) {
        refreshBankAccounts(bankAccounts);
    }
}
