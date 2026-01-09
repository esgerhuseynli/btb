package az.btb.mobilebanking.ui.international_transfers;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentInternationalTransfersBinding;
import az.btb.mobilebanking.databinding.TransactionResultWindowBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.ForeignReceiverInfo;
import az.btb.mobilebanking.models.InternationalTransferPayerInfo;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class InternationalTransfersFragment extends Fragment<FragmentInternationalTransfersBinding> implements InternationalTransfersView {

    private boolean isAccountsLoaded = false;
    private boolean isFromAccount = false;
    private boolean hasIntermediateAgent = false;

    @InjectPresenter InternationalTransfersPresenter presenter;

    @ProvidePresenter InternationalTransfersPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(InternationalTransfersPresenter.class);
    }

    public InternationalTransfersFragment() {
        super(R.layout.fragment_international_transfers);
    }

    @NonNull
    public static InternationalTransfersFragment getInstance() {
        return new InternationalTransfersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupViewParts();

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.hasBankAgentFields.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hasIntermediateAgent = isChecked;
            binding.bankAgentFields.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        Utils.modifyChildrenEnableStatus(binding.root, false);

        binding.makeTransfer.setEnabled(false);
        presenter.getCards();

        binding.makeTransfer.setOnClickListener(v -> {
            final String transferNumber = binding.transferNumber.getText().toString();
            final String countryName = binding.countryName.getText().toString();
            final String cityName = binding.cityName.getText().toString();
            final String bankName = binding.bankName.getText().toString();
            final String swiftCode = binding.swiftCode.getText().toString();
            final String ibanCode = binding.ibanCode.getText().toString();
            final String accountName = binding.accountName.getText().toString();
            final String accountNumber = binding.accountNumber.getText().toString();

            if (!transferNumber.isEmpty()) {
                if (!countryName.isEmpty()) {
                    if (!cityName.isEmpty()) {
                        if (!bankName.isEmpty()) {
                            if (!swiftCode.isEmpty()) {
                                if (!ibanCode.isEmpty()) {
                                    if (!accountName.isEmpty()) {
                                        if (!accountNumber.isEmpty()) {
                                            String swiftCodeAgent = binding.swiftCodeAgent.getText().toString();
                                            String bankNameAgent = binding.bankNameAgent.getText().toString();
                                            String countryNameAgent = binding.countryNameAgent.getText().toString();
                                            String cityNameAgent = binding.cityNameAgent.getText().toString();
                                            String ibanCodeAgent = binding.ibanCodeAgent.getText().toString();

                                            if (hasIntermediateAgent) {
                                                if (!swiftCodeAgent.isEmpty()) {
                                                    if (!bankNameAgent.isEmpty()) {
                                                        if (!countryNameAgent.isEmpty()) {
                                                            if (!cityNameAgent.isEmpty()) {
                                                                if (!ibanCodeAgent.isEmpty()) {
                                                                    try {
                                                                        final BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString());

                                                                        Utils.modifyChildrenEnableStatus(binding.root, false);

                                                                        final String description = binding.operationDescription.getText().toString();
                                                                        final String additionalDescription = binding.additionalInfo.getText().toString();

                                                                        presenter.makeInternationalTransfer(
                                                                            new InternationalTransferPayerInfo(
                                                                                isFromAccount ? 1 : 2,
                                                                                isFromAccount ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                                                                                isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                                                                                ""
                                                                            ),
                                                                            transferNumber,
                                                                            new ForeignReceiverInfo(
                                                                                swiftCode,
                                                                                bankName,
                                                                                countryName,
                                                                                cityName,
                                                                                ibanCode,
                                                                                accountNumber,
                                                                                accountName,
                                                                                swiftCodeAgent,
                                                                                bankNameAgent,
                                                                                countryNameAgent,
                                                                                cityNameAgent,
                                                                                ibanCodeAgent
                                                                            ),
                                                                            amount,
                                                                            isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getCurrency() : ((BankCard) binding.fromCards.getSelectedItem()).getCurrency(), // "+1" cunki, 1ci item AZN deyil.
                                                                            description.isEmpty() ? " " : description,
                                                                            additionalDescription.isEmpty() ? " " : additionalDescription
                                                                        );
                                                                    } catch (NumberFormatException ignored) {
                                                                        Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                                                                    }
                                                                } else
                                                                    Utils.snackbar(binding.getRoot(), R.string.enter_iban);
                                                            } else
                                                                Utils.snackbar(binding.getRoot(), R.string.enter_city_name);
                                                        } else
                                                            Utils.snackbar(binding.getRoot(), R.string.enter_country_name);
                                                    } else
                                                        Utils.snackbar(binding.getRoot(), R.string.enter_bank_name);
                                                } else
                                                    Utils.snackbar(binding.getRoot(), R.string.enter_swift);
                                            } else {
                                                swiftCodeAgent = "";
                                                bankNameAgent = "";
                                                countryNameAgent = "";
                                                cityNameAgent = "";
                                                ibanCodeAgent = "";

                                                try {
                                                    final BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString());

                                                    Utils.modifyChildrenEnableStatus(binding.root, false);

                                                    final String description = binding.operationDescription.getText().toString();
                                                    final String additionalDescription = binding.additionalInfo.getText().toString();

                                                    presenter.makeInternationalTransfer(
                                                        new InternationalTransferPayerInfo(
                                                            isFromAccount ? 1 : 2,
                                                            isFromAccount ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                                                            isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                                                            ""
                                                        ),
                                                        transferNumber,
                                                        new ForeignReceiverInfo(
                                                            swiftCode,
                                                            bankName,
                                                            countryName,
                                                            cityName,
                                                            ibanCode,
                                                            accountNumber,
                                                            accountName,
                                                            swiftCodeAgent,
                                                            bankNameAgent,
                                                            countryNameAgent,
                                                            cityNameAgent,
                                                            ibanCodeAgent
                                                        ),
                                                        amount,
                                                        isFromAccount ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getCurrency() : ((BankCard) binding.fromCards.getSelectedItem()).getCurrency(), // "+1" cunki, 1ci item AZN deyil.
                                                        description.isEmpty() ? " " : description,
                                                        additionalDescription.isEmpty() ? " " : additionalDescription
                                                    );
                                                } catch (NumberFormatException ignored) {
                                                    Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                                                }
                                            }
                                        } else
                                            Utils.snackbar(binding.getRoot(), R.string.enter_account_number);
                                    } else
                                        Utils.snackbar(binding.getRoot(), R.string.enter_account_name);
                                } else
                                    Utils.snackbar(binding.getRoot(), R.string.enter_iban);
                            } else
                                Utils.snackbar(binding.getRoot(), R.string.enter_swift);
                        } else
                            Utils.snackbar(binding.getRoot(), R.string.enter_bank_name);
                    } else
                        Utils.snackbar(binding.getRoot(), R.string.enter_city_name);
                } else
                    Utils.snackbar(binding.getRoot(), R.string.enter_country_name);
            } else
                Utils.snackbar(binding.getRoot(), R.string.enter_transfer_number);
        });
    }

    private void setupViewParts() {
        /* BEGIN: From items */
        binding.fromCard.setOnClickListener(v -> {
            isFromAccount = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isFromAccount = true;

            binding.fromCardsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);

            if (!isAccountsLoaded) {
                binding.makeTransfer.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.getBankAccounts();
            } else
                binding.fromAccountsParent.setVisibility(View.VISIBLE);
        });
        /* END: From items */
    }

    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showCards(@NonNull List<BankCard> bankCards) {
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.transferCurrency.setText(Utils.getCurrency(bankCards.get(position).getCurrency()));
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
        
        binding.makeTransfer.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
    }

    @Override
    public void showAccounts(@NonNull List<BankAccount> bankAccounts) {
        isAccountsLoaded = true;

        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.transferCurrency.setText(Utils.getCurrency(bankAccounts.get(position).getCurrency()));
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            
            }
        });
        
        binding.progressBar.setVisibility(View.GONE);
        binding.makeTransfer.setEnabled(true);
        binding.fromAccountsParent.setVisibility(View.VISIBLE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
    }

    @Override
    public void showSuccessResult(String transferNumber) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        TransactionResultWindowBinding windowBinding =
            TransactionResultWindowBinding.inflate(getLayoutInflater());

        windowBinding.setWasSucceeded(true);
        windowBinding.secondLine.setVisibility(View.GONE);
        windowBinding.accountOrCard.setText(R.string.money_transfer_number);
        windowBinding.setDestinationCardFormattedNumber(transferNumber);

        Utils.showAlertDialogWith(windowBinding.getRoot(), requireContext(), windowBinding.closeDialog);
    }
}
