package az.btb.mobilebanking.ui.own_card_transfers;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentOwnCardTransfersBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.utils.Constants.Currency;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.OtherCardTransferData4Accounts;
import az.btb.mobilebanking.utils.Utils;
import az.btb.mobilebanking.utils.card_edittext.OtherCardTextWatcher;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.Currency.AZN;
import static az.btb.mobilebanking.utils.Constants.Currency.EUR;
import static az.btb.mobilebanking.utils.Constants.Currency.USD;

public class OwnCardTransfersFragment extends Fragment<FragmentOwnCardTransfersBinding> implements OwnCardTransfersView {

    private final List<BankCard> filteredToCards = new ArrayList<>();

    private List<BankCard> cards = null;
    private List<BankAccount> accounts = null;
    private boolean isFromAccount, isToAccount, isToNewCard;

    private final MutableLiveData<List<BankCard>> fromCardsMutableLiveData = new MutableLiveData<>();
    private SpinnerBankCardsAdapter fromCardsAdapter;

    private final MutableLiveData<List<BankCard>> toCardsMutableLiveData = new MutableLiveData<>();
    private BankCard unnecessaryItemForToCards;
    private SpinnerBankCardsAdapter toCardsAdapter;

    @NonNull
    public static OwnCardTransfersFragment getInstance() {
        return new OwnCardTransfersFragment();
    }

    public OwnCardTransfersFragment() {
        super(R.layout.fragment_own_card_transfers);
    }

    @InjectPresenter OwnCardTransfersPresenter presenter;

    @ProvidePresenter OwnCardTransfersPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(OwnCardTransfersPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isFromAccount = false;
        isToAccount = false;
        isToNewCard = false;

        setupViewParts();

        showCards(obtainBankCards());

        fromCardsMutableLiveData.observe(getViewLifecycleOwner(), bankCards -> {
            fromCardsAdapter = new SpinnerBankCardsAdapter(requireContext(), bankCards);
            binding.fromCards.setAdapter(fromCardsAdapter);
        });
        toCardsMutableLiveData.observe(getViewLifecycleOwner(), bankCards -> {
            toCardsAdapter = new SpinnerBankCardsAdapter(requireContext(), bankCards);
            binding.toCards.setAdapter(toCardsAdapter);
        });

        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != -1 && unnecessaryItemForToCards != null) {
                final BankCard selectedItem = fromCardsAdapter.getItem(position);

                binding.transferCurrency.setText(Utils.getCurrency(selectedItem.getCurrency()));

                if (!selectedItem.getIdCard().equals(unnecessaryItemForToCards.getIdCard()))
                    filteredToCards.add(unnecessaryItemForToCards);

                filteredToCards.remove(selectedItem);

                if (selectedItem.getCurrency() == AZN) {
                    toCardsMutableLiveData.setValue(CollectionsKt.filter(filteredToCards, bc -> bc.getCurrency() == AZN && bc.getIdCard() != selectedItem.getIdCard()));
    
                    if (accounts != null)
                        binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(accounts, AZN));
                } else {
                    toCardsMutableLiveData.setValue(
                        CollectionsKt.filter(
                            filteredToCards,
                            bc -> (bc.getIdCard() != selectedItem.getIdCard() && bc.getCurrency() == AZN || bc.getCurrency() == USD || bc.getCurrency() == EUR)
                        )
                    );
    
                    if (accounts != null)
                        binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(accounts, AZN, USD, EUR));
                }
                unnecessaryItemForToCards = selectedItem;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.makeTransfer.setOnClickListener(v -> {
            try {
                BigDecimal amount = new BigDecimal(binding.transferAmount.getText().toString());

                OtherCardTransferData4Accounts data = new OtherCardTransferData4Accounts();
                data.amount = amount;
                data.notes = binding.transferNote.getText().toString();

                if (!isFromAccount) {
                    final BankCard sourceCard = (BankCard) binding.fromCards.getSelectedItem();
                    data.sourceCardId = sourceCard.getIdCard();
                    data.sourceCardAltName = sourceCard.getCardAltName();
                    data.sourceCardFormattedNumber = String.format(
                        getString(R.string.operation_type_card_number),
                        sourceCard.getCardServiceName().substring(0, sourceCard.getCardServiceName().indexOf(' ')),
                        sourceCard.getCardNumber().substring(0, 4),
                        sourceCard.getCardNumber().substring(sourceCard.getCardNumber().length() - 4)
                    );
                    data.sourceCardBalance = sourceCard.getCardBalance();
                    data.amountCurrency = Utils.getCurrency(sourceCard.getCurrency());

                    if (!isToAccount && !isToNewCard) {
                        final BankCard destinationCard = (BankCard) binding.toCards.getSelectedItem();

                        String destinationCardId = destinationCard.getIdCard();
                        String destinationCardNumber = destinationCard.getCardNumber();

                        data.isFromCard = true;
                        data.isToCard = true;
                        data.operationType = "CustomerCards";
                        data.destinationCardId = destinationCardId;
                        data.destinationCardNumber = destinationCardNumber;

                        Utils.modifyChildrenEnableStatus(binding.root, false);
                        presenter.makeTransfer(data);

                        return;
                    }

                    if (isToAccount) {
                        final BankAccount destinationAccount = (BankAccount) binding.toAccounts.getSelectedItem();

                        data.isFromCard = true;
                        data.isToCard = false;
                        data.operationType = "CardToAccount";
                        data.destinationAccountIban = destinationAccount.getIbanAccount();

                        Utils.modifyChildrenEnableStatus(binding.root, false);
                        presenter.makeTransfer(data);

                        return;
                    }

                    if (isToNewCard) {
                        String destinationCardNumber = binding.toNewCardNumber.getText().toString().replace(" ", "");

                        if (destinationCardNumber.length() == 16) {
                            data.isToCard = true;
                            data.isFromCard = true;
                            data.operationType = "OtherCustomerCard";
                            data.destinationCardNumber = destinationCardNumber;
                            data.sourceCardFormattedNumber = String.format(
                                getString(R.string.operation_type_card_number),
                                "",
                                destinationCardNumber.substring(0, 4),
                                destinationCardNumber.substring(destinationCardNumber.length() - 4)
                            );

                            Utils.modifyChildrenEnableStatus(binding.root, false);
                            presenter.makeTransfer(data);
                        } else
                            Utils.snackbar(binding.getRoot(), R.string.pan_is_wrong);
                    }
                } else {
                    final BankAccount sourceAccount = (BankAccount) binding.fromAccounts.getSelectedItem();

                    data.isToCard = true;
                    data.isFromCard = false;
                    data.sourceAccountIban = sourceAccount.getIbanAccount();
                    data.sourceAccountAltName = sourceAccount.getAccountAltName();
                    data.sourceAccountBalance = sourceAccount.getCurrency() == AZN ? sourceAccount.getBalanceInLC() : sourceAccount.getBalanceInFC();
                    data.amountCurrency = Utils.getCurrency(sourceAccount.getCurrency());

                    if (isToNewCard) { // Account to new card
                        String destinationCardNumber = binding.toNewCardNumber.getText().toString().replace(" ", "");
                        if (destinationCardNumber.length() == 16) {
                            data.operationType = "AccountToOtherCard";
                            data.destinationCardNumber = destinationCardNumber;

                            Utils.modifyChildrenEnableStatus(binding.root, false);
                            presenter.makeTransfer(data);
                        } else
                            Utils.snackbar(binding.getRoot(), R.string.pan_is_wrong);
                    } else { // Account to existing card
                        final BankCard destinationCard = (BankCard) binding.toCards.getSelectedItem();

                        data.operationType = "AccountToCard";
                        data.destinationCardId = destinationCard.getIdCard();
                        data.destinationCardNumber = destinationCard.getCardNumber();

                        Utils.modifyChildrenEnableStatus(binding.root, false);
                        presenter.makeTransfer(data);
                    }
                }

            } catch (NumberFormatException nfe) {
                Utils.snackbar(binding.getRoot(), R.string.enter_amount_properly);
            }
        });
    }

    private void setupViewParts() {
        /* BEGIN: From items */
        binding.fromCard.setOnClickListener(v -> {
            isFromAccount = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);
            binding.toAccount.setVisibility(View.VISIBLE);

            final int selectedItemCurrency = ((BankCard) binding.fromCards.getSelectedItem()).getCurrency();
            if (selectedItemCurrency == AZN)
                binding.toCards.setAdapter(getFilteredToCardsAdapter(filteredToCards, AZN));
            else
                binding.toCards.setAdapter(getFilteredToCardsAdapter(filteredToCards, AZN, USD, EUR));
            
            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isFromAccount = true;
            isToAccount = false;  // IMPORTANT!

            binding.fromCardsParent.setVisibility(View.GONE);
            binding.toAccount.setVisibility(View.VISIBLE);

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);

            if (accounts == null) {
                showAccounts(obtainBankAccounts(), false);
            } else {
                int selectedItemCurrency = ((BankAccount) binding.fromAccounts.getSelectedItem()).getCurrency();
                if (selectedItemCurrency == AZN)
                    binding.toCards.setAdapter(getFilteredToCardsAdapter(cards, AZN));
                else
                    binding.toCards.setAdapter(getFilteredToCardsAdapter(cards, AZN, USD, EUR));
            }
            
            binding.fromAccountsParent.setVisibility(View.VISIBLE);

            binding.toAccount.setVisibility(View.GONE);
            binding.toAccountsParent.setVisibility(View.GONE);
        });
        /* END: From items */

        /* BEGIN: To items */
        binding.toCard.setOnClickListener(v -> {
            isToAccount = false;
            isToNewCard = false;

            binding.toCardsParent.setVisibility(View.VISIBLE);
            binding.toAccountsParent.setVisibility(View.GONE);
            binding.toNewCardNumber.setVisibility(View.GONE);

            binding.toCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.toAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
            binding.toAddNewCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_new_card, 0, 0);

            binding.fromAccount.setVisibility(View.VISIBLE);
        });

        binding.toAccount.setOnClickListener(v -> {
            isToAccount = true;
            isFromAccount = false; // IMPORTANT!
            isToNewCard = false;

            binding.toCardsParent.setVisibility(View.GONE);
            binding.toNewCardNumber.setVisibility(View.GONE);

            binding.toCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.toAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
            binding.toAddNewCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_new_card, 0, 0);

            if (accounts == null) {
                showAccounts(obtainBankAccounts(), true);
            } else {
                int selectedItemCurrency = ((BankCard) binding.fromCards.getSelectedItem()).getCurrency();
                if (selectedItemCurrency == AZN)
                    binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(accounts, AZN));
                else
                    binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(accounts, AZN, USD, EUR));
            }
            
            binding.toAccountsParent.setVisibility(View.VISIBLE);

            binding.fromAccount.setVisibility(View.GONE);
            binding.fromAccountsParent.setVisibility(View.GONE);
        });

        binding.toNewCardNumber.addTextChangedListener(new OtherCardTextWatcher(binding.toNewCardNumber));
        binding.toAddNewCard.setOnClickListener(v -> {
            isToAccount = false;
            isToNewCard = true;

            binding.toCardsParent.setVisibility(View.GONE);
            binding.toAccountsParent.setVisibility(View.GONE);
            binding.toNewCardNumber.setVisibility(View.VISIBLE);

            binding.toCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.toAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
            binding.toAddNewCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_new_card_red, 0, 0);

            binding.fromAccount.setVisibility(View.VISIBLE);
        });
        /* END: To items */
    }

    @Override
    public void showError(@NonNull String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    private void showCards(@NonNull List<BankCard> itemList) {
        cards = itemList;
        binding.progressBar.setVisibility(View.GONE);

        filteredToCards.addAll(itemList);

        unnecessaryItemForToCards = itemList.get(0);

        fromCardsMutableLiveData.setValue(itemList);
        toCardsMutableLiveData.setValue(itemList);

        binding.makeTransfer.setEnabled(true);
    }

    private void showAccounts(@NonNull List<BankAccount> bankAccounts, boolean isToAccountClicked) {
        accounts = bankAccounts;
        
        SpinnerBankAccountsAdapter fromAccountsAdapter = new SpinnerBankAccountsAdapter(requireContext(), bankAccounts);
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final BankAccount selectedItem = fromAccountsAdapter.getItem(position);
                binding.transferCurrency.setText(Utils.getCurrency(selectedItem.getCurrency()));
                
                if (selectedItem.getCurrency() == AZN)
                    binding.toCards.setAdapter(getFilteredToCardsAdapter(cards, AZN));
                else
                    binding.toCards.setAdapter(getFilteredToCardsAdapter(cards, AZN, USD, EUR));
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        
        binding.fromAccounts.setAdapter(fromAccountsAdapter);
        if (isToAccountClicked) {
            int selectedItemCurrency = ((BankCard) binding.fromCards.getSelectedItem()).getCurrency();
            if (selectedItemCurrency == AZN)
                binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(bankAccounts, AZN));
            else
                binding.toAccounts.setAdapter(getFilteredToAccountsAdapter(bankAccounts, AZN, USD, EUR));
        }

        binding.makeTransfer.setEnabled(true);

        binding.progressBar.setVisibility(View.GONE);
    }
    
    @NonNull
    private SpinnerBankCardsAdapter getFilteredToCardsAdapter(@NonNull List<BankCard> originalList, @Currency int... currencyExclusions) {
        List<Integer> exclusions = ArraysKt.asList(currencyExclusions);
//        if (unnecessaryItemForToCards == null)
//            return new SpinnerBankCardsAdapter(
//                requireContext(),
//                CollectionsKt.filter(
//                    originalList,
//                    bc -> CollectionsKt.contains(exclusions, bc.getCurrency()) || bc.getCurrency() == AZN
//                )
//            );
//        else
        return new SpinnerBankCardsAdapter(
            requireContext(),
            CollectionsKt.filter(
                originalList,
                bc ->
                    CollectionsKt.contains(exclusions, bc.getCurrency()) ||
                    bc.getCurrency() == AZN && bc.getIdCard() != unnecessaryItemForToCards.getIdCard()
            )
        );
    }
    
    @NonNull
    private SpinnerBankAccountsAdapter getFilteredToAccountsAdapter(@NonNull List<BankAccount> originalList, @Currency int... currencyExclusions) {
        List<Integer> exclusions = ArraysKt.asList(currencyExclusions);
        return new SpinnerBankAccountsAdapter(
            requireContext(),
            CollectionsKt.filter(
                originalList,
                ba -> CollectionsKt.contains(exclusions, ba.getCurrency()) || ba.getCurrency() == AZN
            )
        );
    }
}
