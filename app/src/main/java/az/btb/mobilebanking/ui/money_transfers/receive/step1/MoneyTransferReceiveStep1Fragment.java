package az.btb.mobilebanking.ui.money_transfers.receive.step1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Arrays;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferReceiveStep1Binding;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferReceiveStep1Fragment extends Fragment<FragmentMoneyTransferReceiveStep1Binding> implements MoneyTransferReceiveStep1View {

    @InjectPresenter MoneyTransferReceiveStep1Presenter presenter;

    @ProvidePresenter MoneyTransferReceiveStep1Presenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferReceiveStep1Presenter.class);
    }

    public MoneyTransferReceiveStep1Fragment() {
        super(R.layout.fragment_money_transfer_receive_step1);
    }

    @NonNull
    public static MoneyTransferReceiveStep1Fragment getInstance() {
        return new MoneyTransferReceiveStep1Fragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.currencies.setAdapter(
            new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                Arrays.asList(
                    getString(R.string.choose),
                    "AZN", "USD", "EUR"
                )
            )
        );

        binding.checkTransferNumber.setOnClickListener(v -> {
            final String transferNumber = binding.transferNumber.getText().toString().trim();
            if (transferNumber.length() > 3) {
                try {
                    final BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString().trim());

                    if (binding.currencies.getSelectedItemPosition() > 0) {
                        Utils.modifyChildrenEnableStatus(binding.root, false);
                        presenter.checkTransferNumber(transferNumber, amount, binding.currencies.getSelectedItemPosition());
                    } else
                        Utils.snackbar(binding.getRoot(), R.string.choose_currency);
                } catch (NumberFormatException e) {
                    Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                }
            } else
                Utils.snackbar(binding.getRoot(), R.string.enter_transfer_number);
        });
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }
}
