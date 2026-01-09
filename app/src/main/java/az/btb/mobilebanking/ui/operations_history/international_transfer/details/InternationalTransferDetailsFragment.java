package az.btb.mobilebanking.ui.operations_history.international_transfer.details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentInternationalTransferDetailsBinding;
import az.btb.mobilebanking.models.ForeignAccountTransfer;
import az.btb.mobilebanking.utils.Fragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class InternationalTransferDetailsFragment
    extends Fragment<FragmentInternationalTransferDetailsBinding>
    implements MvpView
{
    private ForeignAccountTransfer transferItem;

    @InjectPresenter InternationalTransferDetailsPresenter presenter;

    @ProvidePresenter InternationalTransferDetailsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(InternationalTransferDetailsPresenter.class);
    }

    @NonNull
    public static InternationalTransferDetailsFragment getInstance(ForeignAccountTransfer transferItem) {
        Bundle b = new Bundle();
        b.putSerializable("transferItem", transferItem);

        InternationalTransferDetailsFragment fragment = new InternationalTransferDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public InternationalTransferDetailsFragment() {
        super(R.layout.fragment_international_transfer_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transferItem = (ForeignAccountTransfer) getArguments().getSerializable("transferItem");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.setData(transferItem);
        binding.setStatus(statusByCode(transferItem.getForeignAccountTransferStatus()));
        binding.executePendingBindings();
    }

    @StringRes
    private int statusByCode(int status) {
        // {Entered = 0, Authorized = 2,
        // Declined = 3, Executed = 5}
        switch (status) {
            default:
                return R.string.entered;
            case 2:
                return R.string.authorized;
            case 3:
                return R.string.declined;
            case 5:
                return R.string.executed;
        }
    }
}
