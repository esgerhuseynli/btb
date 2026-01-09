package az.btb.mobilebanking.ui.payments.payments_providers;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentPaymentProvidersBinding;
import az.btb.mobilebanking.databinding.PaymentProvidersItemBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.PaymentProvider;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PaymentProvidersFragment extends Fragment<FragmentPaymentProvidersBinding> implements PaymentProvidersView {

    private int paymentProviderGroupId;
    private String paymentProviderGroupName;

    @InjectPresenter PaymentProvidersPresenter presenter;

    @ProvidePresenter PaymentProvidersPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PaymentProvidersPresenter.class);
    }

    private final ItemPropsBinder<PaymentProvidersItemBinding, PaymentProvider> itemPropsBinder =
        (binding, paymentProvider) -> {
            Glide
                .with(this)
                .load(paymentProvider.getPaymentProviderImageUrl())
                .into(binding.providerIcon);

            /*if (paymentProvider.getPaymentProviderImage() != null)
                Utils.setImageToImageView(binding.providerIcon, paymentProvider.getPaymentProviderImage());
            else
                binding.providerName.setCompoundDrawablesWithIntrinsicBounds(getByType(paymentProvider.getIdPaymentProvider()), 0, 0, 0);*/

            binding.providerName.setText(paymentProvider.getPaymentProviderName());

            // statusu 1 olmayanlar disabled olmalidir.
            if (paymentProvider.getPaymentProviderStatus() != 1) {
                binding.root.setDisabled(true);
                binding.root.setEnabled(false);
                binding.root.setClickable(false);
                binding.root.setFocusable(false);
            }

            binding.root.setOnClickListener(v ->
                presenter.goToObsiyPaymentsScreen(
                    paymentProviderGroupId,
                    paymentProvider.getIdPaymentProvider(),
                    paymentProvider.getPaymentProviderName()
                )
            );
        };

    public PaymentProvidersFragment() {
        super(R.layout.fragment_payment_providers);
    }

    @NonNull
    public static PaymentProvidersFragment getInstance(final int paymentProviderGroupId, final String paymentProviderGroupName) {
        Bundle b = new Bundle();
        b.putInt("paymentProviderGroupId", paymentProviderGroupId);
        b.putString("paymentProviderGroupName", paymentProviderGroupName);

        PaymentProvidersFragment fragment = new PaymentProvidersFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentProviderGroupId = requireArguments().getInt("paymentProviderGroupId");
        paymentProviderGroupName = requireArguments().getString("paymentProviderGroupName");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.pageTitle.setText(paymentProviderGroupName);
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        Utils.modifyChildrenEnableStatus(binding.root, false);
        presenter.getPaymentProviders(paymentProviderGroupId);
    }

    @Override
    public void showError(String msg) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!msg.isEmpty())
            Utils.snackbar(binding.root, msg);
    }

    @Override
    public void showPaymentProviders(List<PaymentProvider> providers) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);

        ItemsAdapter<PaymentProvidersItemBinding, PaymentProvider> adapter =
            new ItemsAdapter<>(R.layout.payment_providers_item, providers, itemPropsBinder);
        binding.providerGroups.setAdapter(adapter);
    }

    private @DrawableRes int getByType(int providerId) {
        switch (providerId) {
            case 1:
                return R.drawable.ic_provider_azercell;
            case 2:
                return R.drawable.ic_provider_bakcell;
            case 3:
                return R.drawable.ic_provider_nar;
            case 4:
                return R.drawable.ic_provider_azersu;
            case 5:
                return R.drawable.ic_provider_azeriqaz;
            case 6:
                return R.drawable.ic_provider_azerisiq;
            case 7:
                return R.drawable.ic_provider_ipoteka_ve_kredit_zemaneti_fondu;
            default:
                return R.drawable.gradient_spinner;
        }
    }
}
