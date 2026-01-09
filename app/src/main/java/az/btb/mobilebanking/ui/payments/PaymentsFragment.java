package az.btb.mobilebanking.ui.payments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentPaymentsBinding;
import az.btb.mobilebanking.databinding.PaymentProviderGroupsItemBinding;
import az.btb.mobilebanking.databinding.QrCodeVerificationResultDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.PaymentProviderGroup;
import az.btb.mobilebanking.utils.Constants.QrCodeValidationResults;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PaymentsFragment extends Fragment<FragmentPaymentsBinding> implements PaymentsView {

    private boolean isComeFromBottomMenu;

    @InjectPresenter PaymentsPresenter presenter;

    @ProvidePresenter PaymentsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PaymentsPresenter.class);
    }

    private final ItemPropsBinder<PaymentProviderGroupsItemBinding, PaymentProviderGroup> itemPropsBinder =
        (binding, paymentProviderGroup) -> {
//            if (paymentProviderGroup.getPaymentProviderGroupImage() != null)
//                Utils.setImageToImageView(binding.providerGroupIcon, paymentProviderGroup.getPaymentProviderGroupImage());
//            else
//                binding.providerGroupName.setCompoundDrawablesWithIntrinsicBounds(0, getByType(paymentProviderGroup.getIdPaymentProviderGroup()), 0, 0);
            Glide
                .with(this)
                .load(paymentProviderGroup.getPaymentProviderGroupImageUrl())
                .into(binding.providerGroupIcon);

            binding.providerGroupName.setText(paymentProviderGroup.getPaymentProviderGroupName());
            binding.getRoot().setOnClickListener(
                v -> presenter.goToProvidersScreen(
                    paymentProviderGroup.getIdPaymentProviderGroup(),
                    paymentProviderGroup.getPaymentProviderGroupName()
                )
            );
        };

    @NonNull
    public static PaymentsFragment getInstance(boolean isComeFromBottomMenu) {
        Bundle b = new Bundle();
        b.putBoolean("isComeFromBottomMenu", isComeFromBottomMenu);

        PaymentsFragment fragment = new PaymentsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public PaymentsFragment() {
        super(R.layout.fragment_payments);
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
        binding.scanQrCodeTop.setOnClickListener(v -> initiateScan());
    
        presenter.getPaymentProviderGroups();
    }
    
    private void initiateScan() {
        Utils.forceBypassPinFingerprintScreen(requireActivity());
        
        IntentIntegrator.forSupportFragment(this)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setPrompt("QR Pay")
            .setCameraId(0)
            .setOrientationLocked(false)
            .setBeepEnabled(false)
            .setBarcodeImageEnabled(false) // set to true to enable saving the barcode image and sending its path in the result Intent
            .initiateScan();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null)
                Utils.snackbar(binding.getRoot(), R.string.qr_code_scan_cancelled);
            else {
                binding.progressBar.setVisibility(View.VISIBLE);
                Utils.modifyChildrenEnableStatus(binding.root, false);
                presenter.validateQrCode(result.getContents());
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void showQrCodeResultWindow(@StringRes int resultMsg) {
        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        
        final QrCodeVerificationResultDialogBinding resultDialogBinding =
            QrCodeVerificationResultDialogBinding.inflate(getLayoutInflater());
        
        resultDialogBinding.setMessage(getString(resultMsg));
        resultDialogBinding.closeDialog.setOnClickListener(close -> dialog.dismiss());
        resultDialogBinding.rescanQrCode.setOnClickListener(confirm -> {
            dialog.dismiss();
            initiateScan();
        });
        
        dialog.setView(resultDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    
    @Override
    public void showQrCodeErrorResult(@QrCodeValidationResults int qrCodeValidationResultCode) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        switch (qrCodeValidationResultCode) {
            case QrCodeValidationResults.QR_CODE_FAILED:
                showQrCodeResultWindow(R.string.wrong_qr_code_input);
                break;
            case QrCodeValidationResults.NO_SUCH_PAYMENT_PROVIDER:
                showQrCodeResultWindow(R.string.payment_provider_not_exists);
                break;
            case QrCodeValidationResults.NONE:
            case QrCodeValidationResults.QR_CODE_SUCCESS:
            default:
                break;
        }
    }
    
    @Override
    public void showError(String msg) {
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showError(int msg) {
        Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showPaymentProviders(List<PaymentProviderGroup> providerGroups) {
        binding.progressBar.setVisibility(View.GONE);

//        if (providerGroups.size() > 2)
//            providerGroups.remove(2);

        ItemsAdapter<PaymentProviderGroupsItemBinding, PaymentProviderGroup> adapter =
            new ItemsAdapter<>(R.layout.payment_provider_groups_item, providerGroups, itemPropsBinder);
        binding.providerGroups.setAdapter(adapter);
    }

    private @DrawableRes int getByType(int groupId) {
        switch (groupId) {
            case 1:
                return R.drawable.ic_provider_group_telephony;
            case 2:
                return R.drawable.ic_provider_group_utility;
            default:
                return R.drawable.ic_provider_group_bank;
        }
    }
}
