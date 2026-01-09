package az.btb.mobilebanking.ui.my_items.my_loans;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentMyItemsBinding;
import az.btb.mobilebanking.databinding.MyLoansListItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankLoan;
import az.btb.mobilebanking.ui.my_items.MyItemsView;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyLoansFragment extends Fragment<FragmentMyItemsBinding> implements MyItemsView<BankLoan> {

    public MyLoansFragment() {
        super(R.layout.fragment_my_items);
    }

    public static MyLoansFragment getInstance() {
        return new MyLoansFragment();
    }

    @InjectPresenter MyLoansPresenter presenter;

    @ProvidePresenter MyLoansPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyLoansPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setPageTitle(getString(R.string.nav_menu_item_my_loans));
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        presenter.getMyLoansData();
    }

    @Override
    public void showItemsList(List<BankLoan> list) {
        if (list.size() == 0) {
            binding.myItemsList.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            binding.setMissingItem(getString(R.string.no_my_loans));
            return;
        }

        final ItemPropsBinder<MyLoansListItemBinding, BankLoan> itemPropsBinder = (binding, bankLoan) -> {
            binding.setLoanType(bankLoan.getLoanType());
            binding.setLoanInterest(bankLoan.getLoanPercent());
            binding.setLoanBalance(bankLoan.getLoanBalance());
            binding.setLoanCurrency(Utils.getCurrency(bankLoan.getCurrency()));
            binding.setLoanAccountNumber(bankLoan.getLoanAccountNumber());
            binding.setIsOverdueLoan(bankLoan.getLoanOverdueBalance().doubleValue() > 0.00);

            binding.getRoot().setOnClickListener(v -> presenter.showMyLoanInfo(bankLoan));
        };

        final ItemsAdapter<MyLoansListItemBinding, BankLoan> adapter = new ItemsAdapter<>(
            R.layout.my_loans_list_item, list, itemPropsBinder
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
