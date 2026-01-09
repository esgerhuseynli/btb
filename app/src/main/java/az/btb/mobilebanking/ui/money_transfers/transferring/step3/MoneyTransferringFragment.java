package az.btb.mobilebanking.ui.money_transfers.transferring.step3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.util.Objects;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentMoneyTransferringStep3Binding;
import az.btb.mobilebanking.databinding.MoneyTransferResultWindowBinding;
import az.btb.mobilebanking.models.SendTransferInfo;
import az.btb.mobilebanking.ui.money_transfers.MoneyTransfersFragment;
import az.btb.mobilebanking.utils.Constants.MoneyTransferUniqueCodes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class MoneyTransferringFragment extends Fragment<FragmentMoneyTransferringStep3Binding> implements MoneyTransferringStep3View {

    private MoneyTransfersFragment.MoneyTransferData moneyTransferData;
    private MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData;
    private BigDecimal calculatedCommission;

    @InjectPresenter MoneyTransferringPresenter presenter;

    @ProvidePresenter MoneyTransferringPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(MoneyTransferringPresenter.class);
    }

    public MoneyTransferringFragment() {
        super(R.layout.fragment_money_transferring_step3);
    }

    @NonNull
    public static MoneyTransferringFragment getInstance(
        final MoneyTransfersFragment.MoneyTransferData moneyTransferData,
        final MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData,
        final BigDecimal calculatedCommission
    ) {
        Bundle b = new Bundle();
        b.putSerializable("moneyTransferData", moneyTransferData);
        b.putSerializable("moneyTransferReceiverData", moneyTransferReceiverData);
        b.putSerializable("calculatedCommission", calculatedCommission);

        MoneyTransferringFragment fragment = new MoneyTransferringFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moneyTransferData = (MoneyTransfersFragment.MoneyTransferData) getArguments().getSerializable("moneyTransferData");
        moneyTransferReceiverData = (MoneyTransfersFragment.MoneyTransferReceiverData) getArguments().getSerializable("moneyTransferReceiverData");
        calculatedCommission = (BigDecimal) getArguments().getSerializable("calculatedCommission");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setMoneyTransferData(moneyTransferData);
        binding.setMoneyTransferReceiverData(moneyTransferReceiverData);
        binding.setCommission(calculatedCommission);
        binding.setSum(moneyTransferReceiverData.getAmount().add(calculatedCommission));

        binding.goBack.setOnClickListener(v -> presenter.goBack(moneyTransferData));

        binding.operationTypeImage.setBackgroundResource(
            Objects.equals(moneyTransferData.getTransferUniqueName(), MoneyTransferUniqueCodes.MONEX)
                ? R.drawable.ic_monex_selected
                : R.drawable.ic_zolotaya_korona_selected
        );
        binding.icon.setBackgroundResource(moneyTransferData.getCardOrAccountData().getIcon());

        binding.sendTransfer.setOnClickListener(v -> {
            Utils.modifyChildrenEnableStatus(binding.root, false);
            presenter.doMoneyTransfer(moneyTransferData, moneyTransferReceiverData);
        });
    }

    @Override
    public void showResult(SendTransferInfo sendTransferInfo) {
        refreshBankCardsAndAccounts();

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final MoneyTransferResultWindowBinding windowBinding = MoneyTransferResultWindowBinding.inflate(getLayoutInflater());
        windowBinding.setWasSucceeded(sendTransferInfo.getMtTransferStatus() == 2);
        windowBinding.setTransferNumber(sendTransferInfo.getTransferNumber());
        windowBinding.setOperationType(sendTransferInfo.getMtSystemName());
        windowBinding.setCountry(sendTransferInfo.getCountryName());
        windowBinding.setAmount(sendTransferInfo.getTransferAmount());
        windowBinding.setCommission(sendTransferInfo.getCalculatedCommission());
        windowBinding.setCurrency(sendTransferInfo.getTransferCurrency());

        windowBinding.closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goHome();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showError(String responseMessage) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.getRoot(), responseMessage);
    }
}
