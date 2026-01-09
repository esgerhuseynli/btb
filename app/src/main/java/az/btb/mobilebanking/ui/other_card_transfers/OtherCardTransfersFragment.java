package az.btb.mobilebanking.ui.other_card_transfers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentOtherCardTransfersBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import az.btb.mobilebanking.utils.Utils;
import az.btb.mobilebanking.utils.card_edittext.OtherCardTextWatcher;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class OtherCardTransfersFragment extends Fragment<FragmentOtherCardTransfersBinding> implements OtherCardTransfersView {
    
    private static final int CARD_SCAN_REQUEST_CODE = 407;
    
    private boolean isAccountsSelected = false;
    private boolean isToAccount;

    @NonNull
    public static OtherCardTransfersFragment getInstance(boolean isToAccount) {
        Bundle b = new Bundle();
        b.putBoolean("isToAccount", isToAccount);

        OtherCardTransfersFragment fragment = new OtherCardTransfersFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public OtherCardTransfersFragment() {
        super(R.layout.fragment_other_card_transfers);
    }

    @InjectPresenter OtherCardTransfersPresenter presenter;

    @ProvidePresenter OtherCardTransfersPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(OtherCardTransfersPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isToAccount = requireArguments().getBoolean("isToAccount");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isAccountsSelected = false;

        if (isToAccount) {
            binding.title.setText(R.string.to_other_account);
            binding.toCardNumber.setHint(R.string.enter_account_number);
        }

        setupViewParts();

        binding.toCardNumber.addTextChangedListener(new OtherCardTextWatcher(binding.toCardNumber));

        binding.makeTransfer.setEnabled(false);

        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final BankCard selectedItem = (BankCard) binding.fromCards.getSelectedItem();
                binding.transferCurrency.setText(Utils.getCurrency(selectedItem.getCurrency()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final BankAccount selectedItem = (BankAccount) binding.fromAccounts.getSelectedItem();
                binding.transferCurrency.setText(Utils.getCurrency(selectedItem.getCurrency()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        showCards();

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.scanCard.setOnClickListener(v -> scanCard());
        
        binding.makeTransfer.setOnClickListener(v -> {
            final String toCardNumber = binding.toCardNumber.getText().toString();

            if (toCardNumber.trim().length() == 19) {
                try {
                    BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString());

                    Utils.modifyChildrenEnableStatus(binding.root, false);

                    OtherCardTransferData4Accounts data = new OtherCardTransferData4Accounts();
                    data.isToCard = true;
                    data.destinationCardNumber = toCardNumber;

                    if (!isAccountsSelected) {
                        final BankCard sourceCard = (BankCard) binding.fromCards.getSelectedItem();

                        data.isFromCard = true;
                        data.operationType = "OtherCustomerCard";
                        data.sourceCardId = sourceCard.getIdCard();
                        data.sourceCardAltName = sourceCard.getCardAltName();
                        data.sourceCardFormattedNumber = String.format(
                            getString(R.string.operation_type_card_number),
                            sourceCard.getCardServiceName().substring(0, sourceCard.getCardServiceName().indexOf(' ')),
                            sourceCard.getCardNumber().substring(0, 4),
                            sourceCard.getCardNumber().substring(sourceCard.getCardNumber().length() - 4)
                        );
                        data.sourceCardBalance = sourceCard.getCardBalance();
                        data.destinationCardId = "";
                        data.amount = amount;
                        data.amountCurrency = Utils.getCurrency(sourceCard.getCurrency());
                    } else {
                        final BankAccount sourceAccount = (BankAccount) binding.fromAccounts.getSelectedItem();

                        data.isFromCard = false;
                        data.operationType = "AccountToOtherCard";
                        data.sourceAccountIban = sourceAccount.getIbanAccount();
                        data.sourceAccountAltName = sourceAccount.getAccountAltName();
                        data.sourceAccountBalance = sourceAccount.getCurrency() == 0 ? sourceAccount.getBalanceInLC() : sourceAccount.getBalanceInFC();
                        data.amount = amount;
                        data.amountCurrency = Utils.getCurrency(sourceAccount.getCurrency());
                    }
                    data.isToCard = true;
                    data.notes = binding.transferNote.getText().toString();

                    presenter.submitTransfer(data);
                } catch (NumberFormatException nfe) {
                    Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
                }
            } else
                Utils.snackbar(binding.getRoot(), R.string.pan_is_wrong);
        });
    }

    private void setupViewParts() {
        /* BEGIN: From items */
        binding.fromCard.setOnClickListener(v -> {
            isAccountsSelected = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isAccountsSelected = true;

            showAccounts();
            binding.fromCardsParent.setVisibility(View.GONE);
            binding.fromAccountsParent.setVisibility(View.VISIBLE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });

        if (!isToAccount)
            binding.fromAccount.setVisibility(View.VISIBLE);
        /* END: From items */
    }

    @Override
    public void showError(@NonNull String msg) {
        binding.progressBar.setVisibility(View.GONE);
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    private void showCards() {
        binding.progressBar.setVisibility(View.GONE);

        binding.fromCards.setAdapter(
            new SpinnerBankCardsAdapter(
                requireContext(),
                obtainBankCards()
            )
        );

        binding.makeTransfer.setEnabled(true);
    }

    private void showAccounts() {
        binding.fromAccountsParent.setVisibility(View.VISIBLE);

        binding.fromAccounts.setAdapter(
            new SpinnerBankAccountsAdapter(
                requireContext(),
                obtainBankAccounts()
            )
        );

        binding.makeTransfer.setEnabled(true);
    }

    private void scanCard() {
        Utils.forceBypassPinFingerprintScreen(requireActivity());
        
        Intent scanIntent = new Intent(requireActivity(), CardIOActivity.class);
    
        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        //scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
    
        startActivityForResult(scanIntent, CARD_SCAN_REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == CARD_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                final String cardNumber = scanResult.getFormattedCardNumber();
                binding.toCardNumber.setText(cardNumber == null ? "" : cardNumber);
            }
            else
                Utils.snackbar(binding.getRoot(), R.string.card_scan_cancelled);
        }
    }
}
