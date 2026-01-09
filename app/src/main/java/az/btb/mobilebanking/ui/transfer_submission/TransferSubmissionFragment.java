package az.btb.mobilebanking.ui.transfer_submission;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentTransferSubmissionBinding;
import az.btb.mobilebanking.databinding.TransactionResultWindowBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class TransferSubmissionFragment extends Fragment<FragmentTransferSubmissionBinding> implements TransferSubmissionView {

    private OtherCardTransferData4Accounts data;

    @NonNull
    public static TransferSubmissionFragment getInstance(OtherCardTransferData4Accounts data) {
        Bundle b = new Bundle();
        b.putSerializable("data", data);

        TransferSubmissionFragment fragment = new TransferSubmissionFragment();
        fragment.setArguments(b);

        return fragment;
    }

    public TransferSubmissionFragment() {
        super(R.layout.fragment_transfer_submission);
    }

    @InjectPresenter TransferSubmissionPresenter presenter;

    @ProvidePresenter TransferSubmissionPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(TransferSubmissionPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = (OtherCardTransferData4Accounts) getArguments().getSerializable("data");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setAmount(data.amount);
        binding.setAmountCurrency(data.amountCurrency);
        binding.setNotes(data.notes);

        if (!data.isFromCard) {
            binding.itemIcon.setImageResource(R.drawable.ic_my_accounts);
            binding.setSourceCardBalance(data.sourceAccountBalance);
            binding.setSourceCardAltName(data.sourceAccountAltName);
            binding.setSourceCardFormattedNumber(data.sourceAccountIban);
        } else {
            binding.setSourceCardAltName(data.sourceCardAltName);
            binding.setSourceCardFormattedNumber(data.sourceCardFormattedNumber);
            binding.setSourceCardBalance(data.sourceCardBalance);
        }

        if (data.isToCard) {
            binding.setDestinationCardNumber(
                String.format(
                    getString(R.string.operation_type_card_number),
                    "",
                    data.destinationCardNumber.substring(0, 4),
                    data.destinationCardNumber.substring(data.destinationCardNumber.length() - 4)
                )
            );
        } else {
            binding.setDestinationCardNumber(data.destinationAccountIban);
            binding.receiverCardOrAccount.setText(R.string.destination_account);
        }

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.makeTransfer.setOnClickListener(v -> {
            Utils.modifyChildrenEnableStatus(binding.root, false);
            presenter.makeTransfer(data);
        });
    }

    @Override
    public void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        binding.makeTransfer.setEnabled(!isLoading);
    }

    @Override
    public void showTransferResult(boolean wasSucceeded) {
        refreshBankCardsAndAccounts();

        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final TransactionResultWindowBinding windowBinding = TransactionResultWindowBinding.inflate(getLayoutInflater());
        windowBinding.setWasSucceeded(wasSucceeded);
        windowBinding.setAmount(data.amount);
        windowBinding.setAmountCurrency(data.amountCurrency);
        if (data.isToCard) {
            windowBinding.accountOrCard.setText(R.string.destination_card_number);
            windowBinding.setDestinationCardFormattedNumber(
                String.format(
                    getString(R.string.operation_type_card_number),
                    "",
                    data.destinationCardNumber.substring(0, 4),
                    data.destinationCardNumber.substring(data.destinationCardNumber.length() - 4)
                )
            );
        } else {
            windowBinding.accountOrCard.setText(R.string.destination_account);
            windowBinding.setDestinationCardFormattedNumber(data.destinationAccountIban);
        }

        windowBinding.closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goHome();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showError(@NonNull String responseMessage) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.getRoot(), responseMessage);
    }
}
