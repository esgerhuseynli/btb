package az.btb.mobilebanking.ui.operations_history;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentOperationsHistoryBinding;
import az.btb.mobilebanking.utils.Fragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class OperationsHistoryFragment extends Fragment<FragmentOperationsHistoryBinding> implements MvpView {

    @InjectPresenter OperationsHistoryPresenter presenter;

    @ProvidePresenter OperationsHistoryPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(OperationsHistoryPresenter.class);
    }

    @NonNull
    public static OperationsHistoryFragment getInstance() {
        return new OperationsHistoryFragment();
    }

    public OperationsHistoryFragment() {
        super(R.layout.fragment_operations_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.fromCardToCard.setOnClickListener(v -> presenter.goToFromCardToCardHistoryScreen());
        binding.payments.setOnClickListener(v -> presenter.goToPaymentsHistoryScreen());
        binding.internationalTransfers.setOnClickListener(v -> presenter.goToInternationalTransfersHistoryScreen());
        binding.localTransfers.setOnClickListener(v -> presenter.goToLocalTransfersHistoryScreen());
    }
}
