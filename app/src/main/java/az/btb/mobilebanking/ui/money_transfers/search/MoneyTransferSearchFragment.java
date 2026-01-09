package az.btb.mobilebanking.ui.money_transfers.search;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferSearchBinding;
import az.btb.mobilebanking.databinding.MoneyTransferSearchResultItemWindowBinding;
import az.btb.mobilebanking.models.TransferStatusInfo;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferSearchFragment extends Fragment<FragmentMoneyTransferSearchBinding> implements MoneyTransferSearchView {

    private String searchText;

    @InjectPresenter MoneyTransferSearchPresenter presenter;

    @ProvidePresenter MoneyTransferSearchPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferSearchPresenter.class);
    }

    public MoneyTransferSearchFragment() {
        super(R.layout.fragment_money_transfer_search);
    }

    @NonNull
    public static MoneyTransferSearchFragment getInstance() {
        return new MoneyTransferSearchFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.setVisibility(View.GONE);

        binding.search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchText = binding.search.getText().toString().trim();
                if (searchText.length() > 2) {
                    Utils.modifyChildrenEnableStatus(binding.root, false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    presenter.checkOperationStatusBy(searchText);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        binding.setVisibility(View.GONE);

        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showMoneyTransferResult(@NonNull TransferStatusInfo object) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        binding.setVisibility(View.VISIBLE);
        binding.setTransferNumber(searchText);
        binding.setTransferDate(object.getTransferDate());
        binding.transferStatusIndicator.setBackgroundResource(getIcon(object.getTransferStatus()));
        binding.transferItem.setOnClickListener(v -> showTransferResultDetails(object));
    }

    private void showTransferResultDetails(TransferStatusInfo object) {
        final MoneyTransferSearchResultItemWindowBinding windowBinding =
            MoneyTransferSearchResultItemWindowBinding.inflate(getLayoutInflater());

        windowBinding.setTransferStatus(object.getTransferStatus());
        windowBinding.setTransferStatusString(getStatusString(object.getTransferStatus()));
        windowBinding.setTransferDate(object.getTransferDate());
        windowBinding.setFinishDate(object.getDeclineDate());

        Utils.showAlertDialogWith(windowBinding.getRoot(), requireContext(), windowBinding.closeDialog);
    }

    // InProcess = 1, Sent = 2,
    // Received = 3, Declined = 4
    private int getIcon(int status) {
        if (status == 1) return R.drawable.ic_pending;
        else if (status == 2) return R.drawable.ic_success;
        else if (status == 3) return R.drawable.ic_received;
        else /*if (status == 4)*/ return R.drawable.ic_failure;
    }
// MX704624
    private String getStatusString(final int status) {
        switch (status) {
            case 1: return getString(R.string.money_transfer_status_pending);
            case 2: return getString(R.string.money_transfer_status_success);
            case 3: return getString(R.string.money_transfer_status_received);
            case 4: return getString(R.string.money_transfer_status_failure);
            default: return "";
        }
    }
}
