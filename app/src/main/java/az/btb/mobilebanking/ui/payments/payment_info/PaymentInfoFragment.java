package az.btb.mobilebanking.ui.payments.payment_info;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentPaymentInfoBinding;
import az.btb.mobilebanking.databinding.PaymentResultWindowBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.PaymentCommonInvoiceInfo;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.PaymentInfoData;
import az.btb.mobilebanking.utils.Utils;
import kotlin.Triple;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PaymentInfoFragment extends Fragment<FragmentPaymentInfoBinding> implements PaymentInfoView {

    private PaymentCommonInvoiceInfo paymentCommonInvoiceInfo;
    private int providerId;
    private @Constants.MoneySourceTypes int sourceType;
    private String fromIdCard;
    private String fromIbanAccount;
    private BigDecimal amount;
    private boolean isQrCodeScanned;
    private String qrCodeValue;
    private PaymentInfoData pid;
    private boolean isMultiInvoicePayment;

    @InjectPresenter PaymentInfoPresenter presenter;

    @ProvidePresenter PaymentInfoPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PaymentInfoPresenter.class);
    }

    public PaymentInfoFragment() {
        super(R.layout.fragment_payment_info);
    }

    public static PaymentInfoFragment getInstance(
        @NonNull PaymentCommonInvoiceInfo paymentCommonInvoiceInfo, int providerId,
        @Constants.MoneySourceTypes int sourceType, String fromIdCard, String fromIbanAccount,
        BigDecimal amount, boolean isQrCodeScanned, String qrCodeValue, PaymentInfoData pid,
        boolean isMultiInvoicePayment
    ) {
        Bundle b = new Bundle();
        b.putSerializable("paymentCommonInvoiceInfo", paymentCommonInvoiceInfo);
        b.putInt("providerId", providerId);
        b.putInt("sourceType", sourceType);
        b.putString("fromIdCard", fromIdCard);
        b.putString("fromIbanAccount", fromIbanAccount);
        b.putSerializable("amount", amount);
        b.putBoolean("isQrCodeScanned", isQrCodeScanned);
        b.putString("qrCodeValue", qrCodeValue);
        b.putParcelable("pid", pid);
        b.putBoolean("isMultiInvoicePayment", isMultiInvoicePayment);

        PaymentInfoFragment fragment = new PaymentInfoFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paymentCommonInvoiceInfo = (PaymentCommonInvoiceInfo) requireArguments().getSerializable("paymentCommonInvoiceInfo");
        providerId = requireArguments().getInt("providerId");
        sourceType = requireArguments().getInt("sourceType");
        fromIdCard = requireArguments().getString("fromIdCard");
        fromIbanAccount = requireArguments().getString("fromIbanAccount");
        amount = (BigDecimal) requireArguments().getSerializable("amount");
        isQrCodeScanned = requireArguments().getBoolean("isQrCodeScanned");
        qrCodeValue = requireArguments().getString("qrCodeValue");
        pid = requireArguments().getParcelable("pid");
        isMultiInvoicePayment = requireArguments().getBoolean("isMultiInvoicePayment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding.setProviderName(pid.providerName);
        binding.setCardNumberOrAccountIban(pid.fromCardIdOrAccountIban);
        binding.setAmount(pid.amount);
        binding.setIsFromAccount(!fromIbanAccount.isEmpty());
        binding.setPhoneNumber(pid.phoneNumber);
        binding.setPaymentType(pid.paymentType + "");
        binding.setNameSurname(paymentCommonInvoiceInfo.getPayerName());
        binding.setAbonentKodu(pid.abonentKodu);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.next.setOnClickListener(v -> {
            Utils.modifyChildrenEnableStatus(binding.root, false);
            binding.progressBar.setVisibility(View.VISIBLE);

            presenter.doPayment(
                paymentCommonInvoiceInfo,
                providerId,
                sourceType,
                fromIdCard,
                fromIbanAccount,
                amount,
                isQrCodeScanned,
                qrCodeValue,
                isMultiInvoicePayment
            );
        });
    }

    @Override
    public void showError(String responseMessage) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!responseMessage.isEmpty())
            Utils.snackbar(binding.getRoot(), responseMessage);
    }

    @Override
    public void showPaymentResult(int paidInvoiceStatus, String paidInvoiceOperationDateTime, BigDecimal paidInvoicePaymentAmount) {
        refreshBankCardsAndAccounts();

        binding.progressBar.setVisibility(View.GONE);

        final Triple<Integer, Integer, Integer> props = getPropsBy(paidInvoiceStatus);

        final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        PaymentResultWindowBinding windowBinding = PaymentResultWindowBinding.inflate(getLayoutInflater());

        windowBinding.paymentStatusIcon.setImageResource(props.getFirst());
        windowBinding.setPaymentStatusText(getString(props.getSecond()));
        windowBinding.setStatus(getString(props.getThird()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            windowBinding.setPaymentDate(format.format(format.parse(paidInvoiceOperationDateTime)));
        } catch (ParseException e) {
            windowBinding.setPaymentDate(paidInvoiceOperationDateTime);
        }
        windowBinding.setAmount(paidInvoicePaymentAmount);

        windowBinding.closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
            presenter.goHome();
        });

        dialog.setView(windowBinding.getRoot());
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    // burda yashil icon, status=2 (paid olanda olur).
    // status=1,4 (entered & waiting) olanda, sari icon olsun.
    // Status=3 (declined) qirmizi icon olsun
    @NonNull
    private Triple<Integer, Integer, Integer> getPropsBy(final int status) {
        switch (status) {
            case 1:
            case 4:
                return new Triple<>(R.drawable.ic_pending_big, R.string.payment_result_waiting, R.string.money_transfer_status_pending);
            case 2:
                return new Triple<>(R.drawable.ic_success_big,R.string.payment_result_success, R.string.payment_success);
            default:
                return new Triple<>(R.drawable.ic_failure_big,R.string.payment_result_not_success, R.string.money_transfer_status_failure);
        }
    }
}
