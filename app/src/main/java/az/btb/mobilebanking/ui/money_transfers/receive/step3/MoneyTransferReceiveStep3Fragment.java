package az.btb.mobilebanking.ui.money_transfers.receive.step3;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferReceiveStep3Binding;
import az.btb.mobilebanking.databinding.MoneyTransferReceiveResultWindowBinding;
import az.btb.mobilebanking.models.CheckTransferBeforeReceiveInfo;
import az.btb.mobilebanking.models.MoneyTransferReceiverInfo;
import az.btb.mobilebanking.utils.Constants.MoneyTransferUniqueCodes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferReceiveStep3Fragment extends Fragment<FragmentMoneyTransferReceiveStep3Binding> implements MoneyTransferReceiveStep3View {

    private CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo;
    private int transferPaymentType;
    private String id;
    private String formatted;

    @InjectPresenter MoneyTransferReceiveStep3Presenter presenter;

    @ProvidePresenter MoneyTransferReceiveStep3Presenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferReceiveStep3Presenter.class);
    }

    public MoneyTransferReceiveStep3Fragment() {
        super(R.layout.fragment_money_transfer_receive_step3);
    }

    @NonNull
    public static MoneyTransferReceiveStep3Fragment getInstance(CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo, int transferPaymentType, String id, String formatted) {
        Bundle b = new Bundle();
        b.putSerializable("checkTransferBeforeReceiveInfo", checkTransferBeforeReceiveInfo);
        b.putInt("transferPaymentType", transferPaymentType);
        b.putString("id", id);
        b.putString("formatted", formatted);

        MoneyTransferReceiveStep3Fragment fragment = new MoneyTransferReceiveStep3Fragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTransferBeforeReceiveInfo = (CheckTransferBeforeReceiveInfo) getArguments().getSerializable("checkTransferBeforeReceiveInfo");
        transferPaymentType = requireArguments().getInt("transferPaymentType");
        id = requireArguments().getString("id");
        formatted = requireArguments().getString("formatted");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.transferType.setBackgroundResource(
            checkTransferBeforeReceiveInfo.getMtUniqueName().equals(MoneyTransferUniqueCodes.MONEX)
                ? R.drawable.ic_monex_selected
                : R.drawable.ic_zolotaya_korona_selected
        );
        binding.cardOrAccount.setText(formatted);
        binding.amount.setText(
            String.format(
                getString(R.string.my_items_item_balance),
                checkTransferBeforeReceiveInfo.getTransferAmount(),
                Utils.getCurrency(checkTransferBeforeReceiveInfo.getTransferCurrency())
            )
        );

        binding.receiveMoney.setOnClickListener(v -> {
            Utils.modifyChildrenEnableStatus(binding.root, false);
            binding.progressBar.setVisibility(View.VISIBLE);
            presenter.receiveMoney(checkTransferBeforeReceiveInfo, transferPaymentType, id);
        });
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showResult(MoneyTransferReceiverInfo object) {
        refreshBankCardsAndAccounts();

        Utils.modifyChildrenEnableStatus(binding.root, true);

        final MoneyTransferReceiveResultWindowBinding windowBinding =
            MoneyTransferReceiveResultWindowBinding.inflate(getLayoutInflater());

        windowBinding.setTransferType(object.getMtSystemName());
        windowBinding.setTransferStatus(object.getMtTransferStatus());
        windowBinding.setTransferStatusString(getStatusString(object.getMtTransferStatus()));
        windowBinding.setTransferDate(object.getTransferDate());
        windowBinding.setFinishDate(object.getReceiveDate());
        windowBinding.setTransferNumber(object.getTransferNumber());
        windowBinding.setAmountWithCurrency(
            String.format(
                getString(R.string.my_items_item_balance),
                object.getReceiveAmount(),
                Utils.getCurrency(object.getReceiveAmountCurrency())
            )
        );

        Utils.showAlertDialogWith(windowBinding.getRoot(), requireContext(), windowBinding.closeDialog);
    }

    private String getStatusString(final int status) {
        switch (status) {
            case 1:
                return getString(R.string.money_transfer_status_pending);
            case 2:
                return getString(R.string.money_transfer_status_success);
            case 3:
                return getString(R.string.money_transfer_status_received);
            case 4:
                return getString(R.string.money_transfer_status_failure);
            default:
                return "";
        }
    }
}
