package az.btb.mobilebanking.ui.my_items.my_deposits;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentMyItemsBinding;
import az.btb.mobilebanking.databinding.MyDepositsListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankDeposit;
import az.btb.mobilebanking.ui.my_items.MyItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyDepositsFragment extends Fragment<FragmentMyItemsBinding> implements MyItemsView<BankDeposit> {

    public MyDepositsFragment() {
        super(R.layout.fragment_my_items);
    }

    public static MyDepositsFragment getInstance() {
        return new MyDepositsFragment();
    }

    @InjectPresenter MyDepositsPresenter presenter;

    @ProvidePresenter MyDepositsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyDepositsPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setPageTitle(getString(R.string.nav_menu_item_my_deposits));

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        presenter.getMyDepositsData();
    }

    @Override
    public void showItemsList(List<BankDeposit> list) {
        if (list.size() == 0) {
            binding.myItemsList.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.setMissingItem(getString(R.string.no_my_deposits));
            return;
        }

        final ItemPropsBinder<MyDepositsListItemBinding, BankDeposit> itemPropsBinder = (binding, bankDeposit) -> {
            binding.setDepositType(bankDeposit.getDepositType());
            binding.setDepositAmount(bankDeposit.getDepositAmount());
            binding.setDepositCurrency(Utils.getCurrency(bankDeposit.getCurrency()));
            binding.setDepositPercent(bankDeposit.getDepositPercent());
            binding.setDepositAccountNumber(bankDeposit.getDepositAccountNumber());

            binding.getRoot().setOnClickListener(v -> presenter.showMyDepositInfo(bankDeposit));
        };

        final ItemsAdapter<MyDepositsListItemBinding, BankDeposit> adapter = new ItemsAdapter<>(
            R.layout.my_deposits_list_item, list, itemPropsBinder
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
