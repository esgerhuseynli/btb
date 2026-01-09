package az.btb.mobilebanking.ui.my_items.my_loan_info;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMyLoanInfoBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankLoan;
import az.btb.mobilebanking.utils.Fragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyLoanInfoFragment extends Fragment<FragmentMyLoanInfoBinding> implements MyLoanInfoView  {

    private BankLoan bankLoan;

    public MyLoanInfoFragment() {
        super(R.layout.fragment_my_loan_info);
    }

    @NonNull
    public static MyLoanInfoFragment getInstance(BankLoan bankLoan) {
        Bundle args = new Bundle();
        args.putSerializable("bankLoan", bankLoan);
        MyLoanInfoFragment fragment = new MyLoanInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter MyLoanInfoPresenter presenter;

    @ProvidePresenter MyLoanInfoPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyLoanInfoPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankLoan = (BankLoan) getArguments().getSerializable("bankLoan");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.setBankLoan(bankLoan);
        binding.executePendingBindings();
    }
}
