package az.btb.mobilebanking.ui.payments.obsiy;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.ObsiyPaymentFragmentBinding;
import az.btb.mobilebanking.databinding.QrCodeVerificationResultDialogBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.Item;
import az.btb.mobilebanking.models.PaymentProviderJSONParameter;
import az.btb.mobilebanking.models.PaymentUIJasonParameter;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Constants.PaymentUiType;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ObsiyPaymentFragment extends Fragment<ObsiyPaymentFragmentBinding> implements ObsiyPaymentView {

    private int providerGroupId;
    private int providerId;
    private String paymentProviderName;

    private final ArrayList<InteractiveView> viewsToBeUsed = new ArrayList<>();

    @InjectPresenter ObsiyPaymentPresenter presenter;

    @ProvidePresenter ObsiyPaymentPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ObsiyPaymentPresenter.class);
    }

    public ObsiyPaymentFragment() {
        super(R.layout.obsiy_payment_fragment);
    }

    @NonNull
    public static ObsiyPaymentFragment getInstance(int providerGroupId, int providerId, String paymentProviderName) {
        Bundle b = new Bundle();
        b.putInt("providerGroupId", providerGroupId);
        b.putInt("providerId", providerId);
        b.putString("paymentProviderName", paymentProviderName);

        ObsiyPaymentFragment fragment = new ObsiyPaymentFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providerGroupId = requireArguments().getInt("providerGroupId");
        providerId = requireArguments().getInt("providerId");
        paymentProviderName = requireArguments().getString("paymentProviderName");

        presenter.getInteractiveViewDataFor(providerId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.pageTitle.setText(paymentProviderName);

        binding.scanQrCode.setOnClickListener(v -> initiateScan());

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        presenter.uiData.observe(getViewLifecycleOwner(), paymentProviderJSONParameter -> {
            // generate and show views
            generateViews(paymentProviderJSONParameter);

            binding.next.setVisibility(View.VISIBLE);
        });

        binding.next.setOnClickListener(v -> {
            for (InteractiveView interactiveView : viewsToBeUsed) {
                if (interactiveView.viewType == PaymentUiType.EDIT_TEXT)
                    interactiveView.setViewData(((EditText) view.findViewById(interactiveView.viewId)).getText().toString());
                else if (interactiveView.viewType == PaymentUiType.SPINNER)
                    interactiveView.setViewData(((Item) ((AppCompatSpinner) view.findViewById(interactiveView.viewId)).getSelectedItem()).getValue());
                else {
                    throw new UnsupportedOperationException("3cu tip view support olunmur hele ki");
                }
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            Utils.modifyChildrenEnableStatus(binding.root, false);
            presenter.validate(providerGroupId, providerId, paymentProviderName, viewsToBeUsed);
        });
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
    public void showQrCodeErrorResult(@Constants.QrCodeValidationResults int qrCodeValidationResultCode) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        switch (qrCodeValidationResultCode) {
            case Constants.QrCodeValidationResults.QR_CODE_FAILED:
                showQrCodeResultWindow(R.string.wrong_qr_code_input);
                break;
            case Constants.QrCodeValidationResults.NO_SUCH_PAYMENT_PROVIDER:
                showQrCodeResultWindow(R.string.payment_provider_not_exists);
                break;
            case Constants.QrCodeValidationResults.NONE:
            case Constants.QrCodeValidationResults.QR_CODE_SUCCESS:
            default:
                break;
        }
    }

    @Override
    public void showError(@NonNull String message) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    @Override
    public void showError(@StringRes int message) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        Utils.snackbar(binding.getRoot(), message);
    }

    private void generateViews(@NonNull PaymentProviderJSONParameter paymentProviderJSONParameter) {
        // IMPORTANT: Clear before addition!
        viewsToBeUsed.clear();

        for (PaymentUIJasonParameter data : paymentProviderJSONParameter.getPaymentUIJasonParameters()) {
            // put extra space between items.
            binding.viewHolder.addView(generateSpace());

            binding.viewHolder.addView(generateViewLabel(data.getCaption()));

            if (data.getType() == PaymentUiType.EDIT_TEXT) {
                binding.viewHolder.addView(
                    generateEditText(
                        data.getParameterName(),
                        data.getCaption(),
                        data.getMaxLength(),
                        data.getValue(),
                        data.getValueType() == 2
                    )
                );
            } else if (data.getType() == PaymentUiType.SPINNER) {
                binding.viewHolder.addView(
                    generateSpinnerWithHolder(
                        data.getParameterName(),
                        data.getItems()
                        //CollectionsKt.map(data.getItems(), Item::getText)
                    )
                );
            }
        }
    }

    /**
     * Generates TextView for each input with `text` as label.
     *
     * @param text data to show.
     * @return TextView view.
     */
    @NonNull
    private TextView generateViewLabel(String text) {
        TextView label = new TextView(getContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(0,24,0,0);
//        label.setLayoutParams(params);
        label.setTextSize(15f);
        label.setText(text);
        return label;
    }

    /**
     * Generates an EditText view to be displayed.
     *
     * @param tag       is parameterName.
     * @param hint      is caption.
     * @param maxLength allowed text max length.
     * @param value     used IF AND ONLY IF providerGroup = 1 .
     * @param shouldOpenQwertyKeyboard true if value type parameter equals to 1. { None = 0, Integer = 1, String = 2 }
     * @return EditText view.
     */
    @NonNull
    private EditText generateEditText(String tag, String hint, int maxLength, String value, boolean shouldOpenQwertyKeyboard) {
        final int viewId = EditText.generateViewId();

        InteractiveView view = new InteractiveView(viewId, tag, PaymentUiType.EDIT_TEXT);
        viewsToBeUsed.add(view);

        EditText editText = new EditText(getContext());
        editText.setId(viewId);
        editText.setTag(tag);
        editText.setHint(hint);
        editText.setMaxLines(1);
        editText.setBackgroundResource(R.drawable.shape_edit_text_selector);
        if (providerGroupId == 1) // eger mobil providers qrupudursa
            editText.setText(value);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,16,0,0);
        editText.setLayoutParams(params);

        int px16 = Utils.dp2Px(16);
        editText.setPadding(px16, px16, px16, px16);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);

        editText.setFilters(filters);

        if (shouldOpenQwertyKeyboard)
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        else
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        return editText;
    }

    /**
     *
     * @param tag  is `parameterName`.
     * @param data list of selections.
     * @return a decorated with custom view LinearLayout which holds AppCompatSpinner.
     */
    @NonNull
    private LinearLayout generateSpinnerWithHolder(String tag, List<Item> data) {
        final int viewId = AppCompatSpinner.generateViewId();

        InteractiveView view = new InteractiveView(viewId, tag, PaymentUiType.SPINNER);
        viewsToBeUsed.add(view);

        LinearLayout spinnerHolder = new LinearLayout(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );

        AppCompatSpinner spinner = new AppCompatSpinner(requireContext());
        spinner.setId(viewId);
        spinner.setAdapter(
            new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                data
            )
        );
        spinner.setLayoutParams(params);

        params.setMargins(12,12,12,12);
        spinnerHolder.setLayoutParams(params);
        spinnerHolder.setBackgroundResource(R.drawable.gradient_spinner);
        spinnerHolder.addView(spinner);

        return spinnerHolder;
    }

    @NonNull
    private Space generateSpace() {
        final Space space = new Space(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.height = Utils.dp2Px(24);
        space.setLayoutParams(params);
        return space;
    }

    public static final class InteractiveView {
        private final int viewId;
        private final @PaymentUiType int viewType;

        private final String viewTag;
        private Object viewData;

        public InteractiveView(int id, String tag, @PaymentUiType int type) {
            viewId = id;
            viewTag = tag;
            viewType = type;
        }

        public String getParameterName() {
            return viewTag;
        }

        public @PaymentUiType int getViewType() {
            return viewType;
        }

        public void setViewData(Object viewData) {
            this.viewData = viewData;
        }

        public Object getParameterValue() {
            return viewData;
        }
    }
}
