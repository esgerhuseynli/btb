package az.btb.mobilebanking.ui.my_items.my_deposit_info;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMyDepositInfoBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankDeposit;
import az.btb.mobilebanking.utils.Fragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MyDepositInfoFragment extends Fragment<FragmentMyDepositInfoBinding> implements MyDepositInfoView {

    private BankDeposit bankDeposit;

    public MyDepositInfoFragment() {
        super(R.layout.fragment_my_deposit_info);
    }

    @NonNull
    public static MyDepositInfoFragment getInstance(BankDeposit bankDeposit) {
        Bundle args = new Bundle();
        args.putSerializable("bankDeposit", bankDeposit);
        MyDepositInfoFragment fragment = new MyDepositInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter MyDepositInfoPresenter presenter;

    @ProvidePresenter MyDepositInfoPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(MyDepositInfoPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankDeposit = (BankDeposit) getArguments().getSerializable("bankDeposit");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setBankDeposit(bankDeposit);
        binding.executePendingBindings();

        binding.goBack.setOnClickListener(v -> presenter.goBack());
    }
}
