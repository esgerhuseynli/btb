package az.btb.mobilebanking.ui.my_items.my_account_info;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.ChangeItemAltNameDialogBinding;
import az.btb.mobilebanking.databinding.FragmentMyAccountInfoBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyAccountInfoFragment extends Fragment<FragmentMyAccountInfoBinding> implements MyAccountInfoView {

    private BankAccount bankAccount;

    public MyAccountInfoFragment() {
        super(R.layout.fragment_my_account_info);
    }

    @NonNull
    public static MyAccountInfoFragment getInstance(BankAccount bankAccount) {
        Bundle args = new Bundle();
        args.putSerializable("bankAccount", bankAccount);
        MyAccountInfoFragment fragment = new MyAccountInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter MyAccountInfoPresenter presenter;

    @ProvidePresenter MyAccountInfoPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyAccountInfoPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankAccount = (BankAccount) getArguments().getSerializable("bankAccount");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setBankAccount(bankAccount);
        binding.executePendingBindings();

        /* BEGIN WARNING: KEEP THESE LINES JUST AFTER `binding.executePendingBindings();` LINE! */
        setListenerTo(binding.accountColor1, 1);
        setListenerTo(binding.accountColor2, 2);
        setListenerTo(binding.accountColor3, 4);
        setListenerTo(binding.accountColor4, 3);
        /* ********************************** END OF WARNING ********************************** */

        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.changeAccountAltName.setOnClickListener(v -> {
            final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            final ChangeItemAltNameDialogBinding itemNameChangeDialogBinding =
                ChangeItemAltNameDialogBinding.inflate(getLayoutInflater());

            itemNameChangeDialogBinding.setTitle(getString(R.string.account_name));
            itemNameChangeDialogBinding.setSubTitle(getString(R.string.account));
            itemNameChangeDialogBinding.setItemCurrentAltName(bankAccount.getAccountAltName());
            itemNameChangeDialogBinding.closeDialog.setOnClickListener(close -> dialog.dismiss());
            itemNameChangeDialogBinding.confirm.setOnClickListener(confirm -> {
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.changeAccountData(
                    bankAccount.getAccountNumber(),
                    itemNameChangeDialogBinding.itemNewAltName.getText().toString(),
                    bankAccount.getAccountColor()
                );
                dialog.dismiss();
            });

            dialog.setView(itemNameChangeDialogBinding.getRoot());
            dialog.setCancelable(false);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    public void showResult(String accountNewAltName, int accountNewColor) {
        refreshBankCardsAndAccounts();

        binding.progressBar.setVisibility(View.GONE);

        bankAccount.setAccountAltName(accountNewAltName);
        bankAccount.setAccountColor(accountNewColor);
        binding.setBankAccount(bankAccount);
    }

    @Override
    public void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);

        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    private void setListenerTo(@NonNull final RadioButton viewItem, final int accountNewColor) {
        viewItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // because of the view is RadioButton, there is no way to `uncheck`.
                // call to api ONLY and ONLY IF checked, otherwise do nothing.
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.changeAccountData(
                    bankAccount.getAccountNumber(),
                    bankAccount.getAccountAltName(),
                    accountNewColor
                );
            }
        });
    }
}
