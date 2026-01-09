package az.btb.mobilebanking.ui.my_items.my_accounts;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentMyItemsBinding;
import az.btb.mobilebanking.databinding.MyAccountsListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.ui.my_items.MyItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyAccountsFragment extends Fragment<FragmentMyItemsBinding> implements MyItemsView<BankAccount> {

    public MyAccountsFragment() {
        super(R.layout.fragment_my_items);
    }

    public static MyAccountsFragment getInstance() {
        return new MyAccountsFragment();
    }

    @InjectPresenter MyAccountsPresenter presenter;

    @ProvidePresenter MyAccountsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyAccountsPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setPageTitle(getString(R.string.nav_menu_item_my_accounts));

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        presenter.getMyAccountsData();
    }

    @Override
    public void showItemsList(List<BankAccount> list) {
        if (list.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.myItemsList.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            binding.setMissingItem(getString(R.string.no_my_accounts));
            return;
        }

        refreshBankAccounts(list);

        final ItemPropsBinder<MyAccountsListItemBinding, BankAccount> itemPropsBinder = (binding, bankAccount) -> {
            binding.setAccountAltName(bankAccount.getAccountAltName());
            binding.setAccountNumber(bankAccount.getIbanAccount());
            binding.setAccountBalance(
                bankAccount.getCurrency() == 0 ? bankAccount.getBalanceInLC() : bankAccount.getBalanceInFC()
            );
            binding.setAccountBalanceCurrency(Utils.getCurrency(bankAccount.getCurrency()));

            binding.getRoot().setOnClickListener(v -> presenter.showMyAccountInfo(bankAccount));
        };

        final ItemsAdapter<MyAccountsListItemBinding, BankAccount> adapter = new ItemsAdapter<>(
            R.layout.my_accounts_list_item, list, itemPropsBinder
        );

        binding.myItemsList.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.myItemsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }
}
