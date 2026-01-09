package az.btb.mobilebanking.ui.payments.payments_source_selection;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.SpinnerBankAccountsAdapter;
import az.btb.mobilebanking.adapters.SpinnerBankCardsAdapter;
import az.btb.mobilebanking.databinding.FragmentPaymentsSourceSelectionBinding;
import az.btb.mobilebanking.databinding.MultiInvoiceItemFieldsBinding;
import az.btb.mobilebanking.databinding.PaymentResultWindowBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.Invoice;
import az.btb.mobilebanking.models.PaymentCommonInvoiceInfo;
import az.btb.mobilebanking.utils.Constants.MoneySourceTypes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.PaymentInfoData;
import az.btb.mobilebanking.utils.Utils;
import kotlin.Triple;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PaymentsSourceSelectionFragment extends Fragment<FragmentPaymentsSourceSelectionBinding> implements PaymentsSourceSelectionView {

    private List<BankAccount> accounts = null;
    private boolean isAccountsSelected = false;

    private PaymentCommonInvoiceInfo paymentCommonInvoiceInfo;
    private int providerId;
    private String paymentProviderName;
    private boolean isQrCodeScanned;
    private String qrCodeValue;
    private PaymentInfoData pid;

    private final List<MultiInvoiceItem> multiInvoiceItems = new ArrayList<>();
    private final List<Integer> amountFieldsIds = new ArrayList<>();

    @InjectPresenter PaymentsSourceSelectionPresenter presenter;

    @ProvidePresenter PaymentsSourceSelectionPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PaymentsSourceSelectionPresenter.class);
    }

    public PaymentsSourceSelectionFragment() {
        super(R.layout.fragment_payments_source_selection);
    }

    @NonNull
    public static PaymentsSourceSelectionFragment getInstance(
        String paymentProviderName,
        PaymentCommonInvoiceInfo paymentCommonInvoiceInfo,
        int providerId,
        boolean isQrCodeScanned,
        String qrCodeValue,
        PaymentInfoData pid
    ) {
        Bundle b = new Bundle();
        b.putString("paymentProviderName", paymentProviderName);
        b.putInt("providerId", providerId);
        b.putSerializable("paymentCommonInvoiceInfo", paymentCommonInvoiceInfo);
        b.putBoolean("isQrCodeScanned", isQrCodeScanned);
        b.putString("qrCodeValue", qrCodeValue);
        b.putParcelable("pid", pid);

        PaymentsSourceSelectionFragment fragment = new PaymentsSourceSelectionFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentCommonInvoiceInfo = (PaymentCommonInvoiceInfo) getArguments().getSerializable("paymentCommonInvoiceInfo");
        providerId = requireArguments().getInt("providerId");
        paymentProviderName = requireArguments().getString("paymentProviderName");
        isQrCodeScanned = requireArguments().getBoolean("isQrCodeScanned");
        qrCodeValue = requireArguments().getString("qrCodeValue");
        pid = requireArguments().getParcelable("pid");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("PaymentsSourceSelectionFragment.onDestroy");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding.setPersonInitials(paymentCommonInvoiceInfo.getPayerName());
        binding.setDebtAmount(paymentCommonInvoiceInfo.getCommonPaymentAmount());

        if (paymentCommonInvoiceInfo.getInvoices().size() > 1) {
            binding.singleInvoiceItems.setVisibility(View.GONE);
            binding.multiInvoiceItems.setVisibility(View.VISIBLE);

            createMultiInvoiceItems(paymentCommonInvoiceInfo.getInvoices());
            for (MultiInvoiceItem multiInvoiceItem : multiInvoiceItems) {
                MultiInvoiceItemFieldsBinding multiInvoiceItemBinding = MultiInvoiceItemFieldsBinding.inflate(getLayoutInflater());

                multiInvoiceItemBinding.transferAmount.setId(multiInvoiceItem.AMOUNT_FIELD_ID);

                if (!multiInvoiceItem.mustBePaid) {
                    multiInvoiceItemBinding.mustBePayedLayout.setVisibility(View.GONE);
                    multiInvoiceItemBinding.payLater.setVisibility(View.VISIBLE);

                    /*
                     * 1) mustBePayed false, isPartial true:
                     *      10 azn qayidib invoice amount, check box var (check elese 0 gonderirik meblegde),
                     *      odemek istese  min max araliginda olan mebleg odeye biler (tutaq 4, 7 fln)
                     */
                    if (multiInvoiceItem.isPartialPayment) {
                        multiInvoiceItemBinding.payLater.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            changeStateOf(multiInvoiceItemBinding.transferAmount, !isChecked);

                            if (isChecked)
                                multiInvoiceItemBinding.transferAmount.setText("0.00");
                            else
                                multiInvoiceItemBinding.transferAmount.setText("");
                        });
                    }
                    /*
                     * 2) mustBePayed false, isPartial false:
                     *      10 azn qayidib, checkbox var, check elese 0 gedir.
                     *      checkbox check elemese input disabled olur ve full amount gonderilir
                     */
                    else {
                        changeStateOf(multiInvoiceItemBinding.transferAmount, false);

                        multiInvoiceItemBinding.payLater.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked)
                                multiInvoiceItemBinding.transferAmount.setText("0.00");
                            else
                                multiInvoiceItemBinding.transferAmount.setText(String.format(getString(R.string.my_items_item_balance), multiInvoiceItem.amount, "").trim());
                        });
                    }
                } else {
                    multiInvoiceItemBinding.mustBePayedLayout.setVisibility(View.VISIBLE);
                    multiInvoiceItemBinding.payLater.setVisibility(View.GONE);
                    multiInvoiceItemBinding.payLater.setOnCheckedChangeListener(null); // set null in any case...

                    /*
                     * 3) mustBePayed true, isPartial true:
                     *      10 azn qayidib, check box yoxdu, ulduz var (mutleq odeme olmalidi yeni),
                     *      user min max arasinda olan istenilen mebleg yaza biler (3,5,7)
                     */
                    if (multiInvoiceItem.isPartialPayment) {
                        changeStateOf(multiInvoiceItemBinding.transferAmount, true); // reset in any case...
                    }
                    /*
                     * 4) mustBePayed true, isPartial false:
                     *      10 azn qayidib, checkbox yoxdu, ulduz var, input disabled di, ve full amount (10azn)
                     *      gonderirik
                     */
                    else {
                        changeStateOf(multiInvoiceItemBinding.transferAmount, false);
                        multiInvoiceItemBinding.transferAmount.setText(String.format(getString(R.string.my_items_item_balance), multiInvoiceItem.amount, "").trim());
                    }
                }

                multiInvoiceItemBinding.transferAmount.setHint(multiInvoiceItem.minPayableAmount + " - " + multiInvoiceItem.maxPayableAmount);

                if (multiInvoiceItem.mustBePaid)
                    multiInvoiceItemBinding.setItemName(Utils.getMustBePayedItemTitle(multiInvoiceItem.itemName));
                else
                    multiInvoiceItemBinding.setItemName(multiInvoiceItem.itemName);

                multiInvoiceItemBinding.setAmount(multiInvoiceItem.amount);
                multiInvoiceItemBinding.setCurrency(multiInvoiceItem.currency);

                binding.generatedMultiInvoiceItemsHolder.addView(multiInvoiceItemBinding.getRoot());
            }
        }
        else {
            final Invoice invoice = paymentCommonInvoiceInfo.getInvoices().get(0);
            binding.singleInvoiceName.setText(invoice.getInvoiceName());
            binding.singleInvoiceAmountWithCurrency.setText(
                String.format(
                    getString(R.string.my_items_item_balance),
                    invoice.getInvoiceAmount(),
                    Utils.getCurrency(invoice.getCurrency())
                )
            );

            // isPartial = false
            if (invoice.getPartialPayment() == 0) {
                changeStateOf(binding.transferAmount, false);
                binding.transferAmount.setText(String.format(getString(R.string.my_items_item_balance), invoice.getInvoiceAmount(), "").trim());
            }
        }

        setupViewParts();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isAccountsSelected = false;

        binding.fromCards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.transferCurrency.setText(
                    Utils.getCurrency(
                        ((BankCard) binding.fromCards.getSelectedItem()).getCurrency()
                    )
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.fromAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.transferCurrency.setText(
                    Utils.getCurrency(
                        ((BankAccount) binding.fromAccounts.getSelectedItem()).getCurrency()
                    )
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Utils.modifyChildrenEnableStatus(binding.root, false);

        presenter.getCards();

        binding.pageTitle.setText(paymentProviderName);

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.next.setOnClickListener(v -> {
            if (isAccountsSelected) {
//                if (binding.fromAccounts.getSelectedItemPosition() > 0) {
                BankAccount account = (BankAccount) binding.fromAccounts.getSelectedItem();
                pid.fromCardIdOrAccountIban = account.getIbanAccount();
//                } else {
//                    Utils.showSnackbar(binding.getRoot(), R.string.choose_account);
//                    return;
//                }
            } else {
//                if (binding.fromCards.getSelectedItemPosition() > 0) {
                BankCard card = (BankCard) binding.fromCards.getSelectedItem();
                pid.fromCardIdOrAccountIban = String.format(
                    getString(R.string.operation_type_card_number),
                    card.getCardServiceName().substring(0, card.getCardServiceName().indexOf(' ')),
                    card.getCardNumber().substring(0, 4),
                    card.getCardNumber().substring(card.getCardNumber().length() - 4)
                );
//                } else {
//                    Utils.showSnackbar(binding.getRoot(), R.string.choose_card);
//                    return;
//                }
            }

            pid.nameSurname = paymentCommonInvoiceInfo.getPayerName();

            // MULTI INVOICE-LU ODENISLER UCUN OLAN SUBMISSION HISSESI
            if (paymentCommonInvoiceInfo.getInvoices().size() > 1) {
                if (areMultiInvoiceItemInputFieldsAreValid()) {
                    pid.amount = sumInvoiceAmounts(paymentCommonInvoiceInfo.getInvoices());

                    if (Utils.eq(pid.amount, BigDecimal.ZERO))
                        Utils.snackbar(binding.root, R.string.enter_amount_properly);
                    else {
                        Utils.modifyChildrenEnableStatus(binding.root, false);

                        presenter.goNext(
                            paymentCommonInvoiceInfo,
                            providerId,
                            isAccountsSelected ? MoneySourceTypes.ACCOUNT : MoneySourceTypes.CARD,
                            isAccountsSelected ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                            isAccountsSelected ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                            pid.amount,
                            isQrCodeScanned,
                            qrCodeValue,
                            pid,
                            true
                        );
                    }
                }
            }
            // DIGER PROVIDERLAR UCUN OLAN HISSE
            else {
                BigDecimal amount = BigDecimal.ZERO;

                try {
                    amount = new BigDecimal(binding.transferAmount.getText().toString());
                } catch (NumberFormatException nfe) {
                    //  amount = new BigDecimal("0.00");
                }

                if (isAmountSetToInvoice(amount, paymentCommonInvoiceInfo.getInvoices().get(0))) {
                    pid.amount = amount;

                    Utils.modifyChildrenEnableStatus(binding.root, false);

                    if (!isQrCodeScanned) {
                        presenter.goNext(
                            paymentCommonInvoiceInfo,
                            providerId,
                            isAccountsSelected ? MoneySourceTypes.ACCOUNT : MoneySourceTypes.CARD,
                            isAccountsSelected ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                            isAccountsSelected ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                            amount,
                            isQrCodeScanned,
                            qrCodeValue,
                            pid,
                            false
                        );
                    } else {
                        binding.progressBar.setVisibility(View.VISIBLE);

                        presenter.doPayment(
                            paymentCommonInvoiceInfo,
                            providerId,
                            isAccountsSelected ? MoneySourceTypes.ACCOUNT : MoneySourceTypes.CARD,
                            isAccountsSelected ? "" : ((BankCard) binding.fromCards.getSelectedItem()).getIdCard(),
                            isAccountsSelected ? ((BankAccount) binding.fromAccounts.getSelectedItem()).getIbanAccount() : "",
                            amount,
                            qrCodeValue,
                            false
                        );
                    }
                }
            }
        });
    }

    private void setupViewParts() {
        /* BEGIN: From items */
        binding.fromCard.setOnClickListener(v -> {
            isAccountsSelected = false;

            binding.fromCardsParent.setVisibility(View.VISIBLE);
            binding.fromAccountsParent.setVisibility(View.GONE);

            binding.transferCurrency.setText(
                Utils.getCurrency(
                    ((BankCard) binding.fromCards.getSelectedItem()).getCurrency()
                )
            );

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_cards, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer, 0, 0);
        });

        binding.fromAccount.setOnClickListener(v -> {
            isAccountsSelected = true;

            binding.fromCardsParent.setVisibility(View.GONE);
            binding.fromAccountsParent.setVisibility(View.VISIBLE);

            if (accounts == null) {
                Utils.modifyChildrenEnableStatus(binding.root, false);

                binding.progressBar.setVisibility(View.VISIBLE);
                presenter.getBankAccounts();
            } else {
                BankAccount a = (BankAccount) binding.fromAccounts.getSelectedItem();
                binding.transferCurrency.setText(Utils.getCurrency(a.getCurrency()));
            }

            binding.fromCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_card, 0, 0);
            binding.fromAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bank_transfer_red, 0, 0);
        });
        /* END: From items */
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showCards(List<BankCard> bankCards) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        binding.fromCards.setAdapter(new SpinnerBankCardsAdapter(requireContext(), bankCards));
    }

    @Override
    public void showAccounts(List<BankAccount> bankAccounts) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        accounts = bankAccounts;

        binding.progressBar.setVisibility(View.GONE);
        binding.fromAccounts.setAdapter(new SpinnerBankAccountsAdapter(requireContext(), bankAccounts));
    }

    private void createMultiInvoiceItems(List<Invoice> invoices) {
        multiInvoiceItems.clear();
        multiInvoiceItems.addAll(
            CollectionsKt.map(
                invoices,
                invoice ->
                    new MultiInvoiceItem(
                        invoice.getInvoiceName(),
                        invoice.getProviderInvoicePaymentMode() == 1,
                        invoice.getInvoiceAmount(),
                        invoice.getMinAmount(),
                        invoice.getMaxAmount(),
                        Utils.getCurrency(invoice.getCurrency()),
                        invoice.getPartialPayment() == 1
                    )
            )
        );

        amountFieldsIds.clear();
        amountFieldsIds.addAll(
            CollectionsKt.map(
                multiInvoiceItems,
                multiInvoiceItem -> multiInvoiceItem.AMOUNT_FIELD_ID
            )
        );
    }

    private void changeStateOf(@NonNull final EditText editText, final boolean state) {
        if (state)
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        else
            editText.setInputType(InputType.TYPE_NULL);
    }

    private boolean areMultiInvoiceItemInputFieldsAreValid() {
        List<Boolean> validator = CollectionsKt.mutableListOf();

        int generatedMultiInvoiceItemsCount = binding.generatedMultiInvoiceItemsHolder.getChildCount();
        for (int i = 0; i < generatedMultiInvoiceItemsCount; i++) {
            View generatedRootViewItem = binding.generatedMultiInvoiceItemsHolder.getChildAt(i);

            EditText enteredAmountField = generatedRootViewItem.findViewById(amountFieldsIds.get(i));
            MaterialCheckBox payLaterCheckBoxField = generatedRootViewItem.findViewById(R.id.pay_later);

            MultiInvoiceItem predefinedMultiInvoiceItemsValues = multiInvoiceItems.get(i);

            if (!predefinedMultiInvoiceItemsValues.mustBePaid) {
                /*
                 * 1) mustBePayed false, isPartial true:
                 *      10 azn qayidib invoice amount, check box var (check elese 0 gonderirik meblegde),
                 *      odemek istese  min max araliginda olan mebleg odeye biler (tutaq 4, 7 fln)
                 */
                if (predefinedMultiInvoiceItemsValues.isPartialPayment) {
                    if (payLaterCheckBoxField.isChecked())
                        paymentCommonInvoiceInfo.getInvoices().get(i).setInvoiceAmount(BigDecimal.ZERO);
                    else {
                        BigDecimal userEnteredAmount = BigDecimal.valueOf(-1);
                        try {
                            userEnteredAmount = new BigDecimal(enteredAmountField.getText().toString());
                        } catch (NumberFormatException ignored) {
                        }

                        validator.add(isAmountSetToInvoice(userEnteredAmount, paymentCommonInvoiceInfo.getInvoices().get(i)));
                    }
                }
                /*
                 * 2) mustBePayed false, isPartial false:
                 *      10 azn qayidib, checkbox var, check elese 0 gedir. checkbox check elemese input
                 *      disabled olur ve full amount gonderilir (bu casede 10)
                 */
                else {
                    if (payLaterCheckBoxField.isChecked())
                        paymentCommonInvoiceInfo.getInvoices().get(i).setInvoiceAmount(BigDecimal.ZERO);
                    else
                        paymentCommonInvoiceInfo.getInvoices().get(i).setInvoiceAmount(predefinedMultiInvoiceItemsValues.amount);
                }
            } else {
                /*
                 * 3) mustBePayed true, isPartial true:
                 *      10 azn qayidib, check box yoxdu, ulduz var (mutleq odeme olmalidi yeni),
                 *      user min max arasinda olan istenilen mebleg yaza biler (3,5,7)
                 */
                if (predefinedMultiInvoiceItemsValues.isPartialPayment) {
                    BigDecimal userEnteredAmount = BigDecimal.valueOf(-1);
                    try {
                        userEnteredAmount = new BigDecimal(enteredAmountField.getText().toString());
                    } catch (NumberFormatException ignored) {
                    }

                    validator.add(isAmountSetToInvoice(userEnteredAmount, paymentCommonInvoiceInfo.getInvoices().get(i)));
                }
                /*
                 * 4) mustBePayed true, isPartial false:
                 *      10 azn qayidib, checkbox yoxdu, ulduz var, input disabled di, ve full amount (10azn)
                 *      gonderirik
                 */
                else
                    paymentCommonInvoiceInfo.getInvoices().get(i).setInvoiceAmount(predefinedMultiInvoiceItemsValues.amount);
            }
        }

        return CollectionsKt.all(validator, bool -> bool);
    }

    private boolean isAmountSetToInvoice(@NonNull final BigDecimal userEnteredAmount, @NonNull final Invoice invoice) {
        if (Utils.lt(userEnteredAmount, invoice.getMinAmount())) {
            Utils.snackbar(
                binding.root,
                String.format(
                    getString(R.string.min_transfer_amount_error),
                    invoice.getMinAmount(),
                    Utils.getCurrency(invoice.getCurrency())
                )
            );
            return false;
        } else {
            if (Utils.gt(userEnteredAmount, invoice.getMaxAmount())) {
                Utils.snackbar(
                    binding.root,
                    String.format(
                        getString(R.string.max_transfer_amount_error),
                        invoice.getMaxAmount(),
                        Utils.getCurrency(invoice.getCurrency())
                    )
                );
                return false;
            } else {
                invoice.setInvoiceAmount(userEnteredAmount);
                return true;
            }
        }
    }

    private BigDecimal sumInvoiceAmounts(@NonNull final List<Invoice> invoices) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Invoice i : invoices)
            sum = sum.add(i.getInvoiceAmount());
        return sum.setScale(2, RoundingMode.HALF_UP);
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

    private static final class MultiInvoiceItem {
        private final int AMOUNT_FIELD_ID = EditText.generateViewId();
        private final String itemName;
        private final boolean mustBePaid;
        private final BigDecimal amount;
        private final BigDecimal minPayableAmount;
        private final BigDecimal maxPayableAmount;
        private final String currency;
        private final boolean isPartialPayment;

        private MultiInvoiceItem(String itemName, boolean mustBePaid, BigDecimal amount, BigDecimal minPayableAmount, BigDecimal maxPayableAmount, String currency, boolean isPartialPayment) {
            this.itemName = itemName;
            this.mustBePaid = mustBePaid;
            this.amount = amount;
            this.minPayableAmount = minPayableAmount;
            this.maxPayableAmount = maxPayableAmount;
            this.currency = currency;
            this.isPartialPayment = isPartialPayment;
        }
    }
}
