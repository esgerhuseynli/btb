package az.btb.mobilebanking.ui.transfers;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.BuildConfig;
import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentTransfersBinding;
import az.btb.mobilebanking.utils.Fragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class TransfersFragment extends Fragment<FragmentTransfersBinding> implements MvpView {
    
    private boolean isComeFromBottomMenu;
    
    @InjectPresenter TransfersPresenter presenter;

    @ProvidePresenter TransfersPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(TransfersPresenter.class);
    }

    @NonNull
    public static TransfersFragment getInstance(boolean isComeFromBottomMenu) {
        Bundle b = new Bundle();
        b.putBoolean("isComeFromBottomMenu", isComeFromBottomMenu);
    
        TransfersFragment fragment = new TransfersFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public TransfersFragment() {
        super(R.layout.fragment_transfers);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isComeFromBottomMenu = requireArguments().getBoolean("isComeFromBottomMenu");
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setVisibility(isComeFromBottomMenu ? View.GONE : View.VISIBLE);
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.betweenMineTransfers.setOnClickListener(v -> presenter.goToBetweenMineTransfersScreen());
        binding.otherCardTransfers.setOnClickListener(v -> presenter.goToOtherCardTransfersScreen());
        binding.otherAccountTransfers.setOnClickListener(v -> presenter.goToOtherAccountTransfersScreen());

        binding.fastMoneyTransfers.setVisibility(BuildConfig.FLAVOR == "dev" ? View.VISIBLE : View.GONE);
        binding.fastMoneyTransfers.setOnClickListener(v -> presenter.goToMoneyTransfersScreen());

        binding.localTransfers.setOnClickListener(v -> presenter.goToLocalTransfersScreen());
        binding.internationalTransfers.setOnClickListener(v -> presenter.goToInternationalTransfersScreen());
    }
}
