package az.btb.mobilebanking.ui.money_transfers.transferring.step2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.math.BigDecimal;
import java.util.Arrays;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferringStep2Binding;
import az.btb.mobilebanking.ui.money_transfers.MoneyTransfersFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferringFragment extends Fragment<FragmentMoneyTransferringStep2Binding> implements MoneyTransferringStep2View {

    private MoneyTransfersFragment.MoneyTransferData moneyTransferData;

    @InjectPresenter MoneyTransferringPresenter presenter;

    @ProvidePresenter MoneyTransferringPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferringPresenter.class);
    }

    public MoneyTransferringFragment() {
        super(R.layout.fragment_money_transferring_step2);
    }

    @NonNull
    public static MoneyTransferringFragment getInstance(MoneyTransfersFragment.MoneyTransferData moneyTransferData) {
        Bundle b = new Bundle();
        b.putSerializable("moneyTransferData", moneyTransferData);
        MoneyTransferringFragment fragment = new MoneyTransferringFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moneyTransferData = (MoneyTransfersFragment.MoneyTransferData) getArguments().getSerializable("moneyTransferData");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.currencies.setAdapter(
            new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, android.R.id.text1,
                Arrays.asList(
                    "AZN", "USD", "EUR"
                )
            )
        );

        binding.goToStep3.setOnClickListener(v -> {
            final String name = binding.personName.getText().toString();
            final String surname = binding.personSurname.getText().toString();
            final String fatherName = binding.personFatherName.getText().toString();
            final String phoneNumber = binding.receiverNumber.getText().toString();
            BigDecimal amount = new BigDecimal("0.00");
            final int currency = binding.currencies.getSelectedItemPosition();

            if (name.trim().length() == 0) {
                Utils.snackbar(binding.getRoot(), R.string.enter_name);
                return;
            }
            if (surname.trim().length() == 0) {
                Utils.snackbar(binding.getRoot(), R.string.enter_surname);
                return;
            }
            if (fatherName.trim().length() == 0) {
                Utils.snackbar(binding.getRoot(), R.string.enter_fathername);
                return;
            }
            if (!isFormValid(phoneNumber)) {
                Utils.snackbar(binding.getRoot(), R.string.receiver_phone_number_format);
                return;
            }

            try {
                amount = new BigDecimal(binding.transferAmount.getText().toString());
            } catch (NumberFormatException nfe) {
                //  amount = new BigDecimal("0.00");
            }
            if (amount.doubleValue() < 1.00) {
                Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                return;
            }

            final MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData =
                MoneyTransfersFragment.MoneyTransferReceiverData.getInstance()
                    .setName(name)
                    .setSurname(surname)
                    .setFathername(fatherName)
                    .setPhoneNumber(phoneNumber.replace(" ", ""))
                    .setTransferAmount(amount)
                    .setTransferCurrency(currency)
                    .build();

            Utils.modifyChildrenEnableStatus(binding.root, false);
            presenter.goToStep3(
                moneyTransferData,
                moneyTransferReceiverData
            );
        });
    }

    private boolean isFormValid(String newPhoneNumberData) {
        return newPhoneNumberData != null && newPhoneNumberData.trim().length() == 17;
    }

    @Override
    public void showError(String responseMessage) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.getRoot(), responseMessage);
    }

    @Override
    public void setMinAmount(BigDecimal minAmount) {
        showAmountError(R.string.min_transfer_amount_error, minAmount);
    }

    @Override
    public void setMaxAmount(BigDecimal maxAmount) {
        showAmountError(R.string.max_transfer_amount_error, maxAmount);
    }

    private void showAmountError(@StringRes int msg, final BigDecimal amount) {
        Utils.snackbar(
            binding.getRoot(),
            String.format(
                getString(msg),
                amount,
                binding.currencies.getSelectedItem()
            )
        );
    }
}
