package az.btb.mobilebanking.ui.money_transfers.receive.step2;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferReceiveStep2Binding;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.CheckTransferBeforeReceiveInfo;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferReceiveStep2Fragment extends Fragment<FragmentMoneyTransferReceiveStep2Binding> implements MoneyTransferReceiveStep2View {

    private List<BankAccount> accounts = null;
    private boolean isAccountsSelected = false;

    private CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo;

    @InjectPresenter MoneyTransferReceiveStep2Presenter presenter;

    @ProvidePresenter MoneyTransferReceiveStep2Presenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferReceiveStep2Presenter.class);
    }

    public MoneyTransferReceiveStep2Fragment() {
        super(R.layout.fragment_money_transfer_receive_step2);
    }

    @NonNull
    public static MoneyTransferReceiveStep2Fragment getInstance(CheckTransferBeforeReceiveInfo data) {
        Bundle b = new Bundle();
        b.putSerializable("checkTransferBeforeReceiveInfo", data);

        MoneyTransferReceiveStep2Fragment fragment = new MoneyTransferReceiveStep2Fragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTransferBeforeReceiveInfo = (CheckTransferBeforeReceiveInfo) getArguments().getSerializable("checkTransferBeforeReceiveInfo");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupViewParts();

        isAccountsSelected = false;

        showCards(obtainBankCards());

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        if (!isAccountsSelected /*&& binding.fromCards.getSelectedItemPosition() > 0*/) {
            final BankCard card = ((BankCard) binding.fromCards.getSelectedItem());
            final String cardName = card.getCardServiceName();
            presenter.goToStep3(
                checkTransferBeforeReceiveInfo,
                2,
                card.getIdCard(),
                String.format(
                    getString(R.string.operation_type_card_number),
                    cardName.substring(0, cardName.indexOf(' ')),
                    card.getCardNumber().substring(0, 4),
                    card.getCardNumber().substring(card.getCardNumber().length() - 4)
                )
            );
//            checkGate2(
//                card.getIdCard(),
//                "",
//                cardName.substring(0, cardName.length() - 4),
//                String.format(
//                    getString(R.string.operation_type_card_number),
//                    cardName.substring(0, cardName.indexOf(' ')),
//                    card.getCardNumber().substring(0, 4),
//                    card.getCardNumber().substring(card.getCardNumber().length() - 4)
//                ),
//                card.getCardBalance(),
//                card.getCurrency()
//            );
        } else if (isAccountsSelected /*&& binding.fromAccounts.getSelectedItemPosition() > 0*/) {
            final BankAccount account = ((BankAccount) binding.fromAccounts.getSelectedItem());
            presenter.goToStep3(checkTransferBeforeReceiveInfo, 1, account.getIbanAccount(), account.getAccountNumber());
//                "",
//                account.getIbanAccount(),
//                Utils.getCurrency(account.getCurrency()),
//                account.getAccountNumber(),
//                account.getCurrency() == 0 ? account.getBalanceInLC() : account.getBalanceInFC(),
//                account.getCurrency()
//            );
        } else
            Utils.snackbar(binding.getRoot(), R.string.choose_card_or_account);
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
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

            binding.fromCardsParent.setVisibility(View.GONE);
            binding.fromAccountsParent.setVisibility(View.VISIBLE);

            if (accounts == null)
                showAccounts(obtainBankAccounts());

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });
        /* END: From items */
    }

    @Override
    public void showCards(List<BankCard> bankCards) {
        binding.progressBar.setVisibility(View.GONE);

        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
    }

    @Override
    public void showAccounts(List<BankAccount> bankAccounts) {
        accounts = bankAccounts;

        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
    }
}
