package az.btb.mobilebanking.ui.password_recovery_by_types;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentRecoveryPasswordByTypesBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class PasswordRecoveryByTypesFragment extends Fragment<FragmentRecoveryPasswordByTypesBinding> implements PasswordRecoveryByTypesView {

    @InjectPresenter PasswordRecoveryByTypesPresenter presenter;

    @ProvidePresenter PasswordRecoveryByTypesPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(PasswordRecoveryByTypesPresenter.class);
    }
    
    public PasswordRecoveryByTypesFragment() {
        super(R.layout.fragment_recovery_password_by_types, false);
    }
    
    public static PasswordRecoveryByTypesFragment getInstance() {
        return new PasswordRecoveryByTypesFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.forgotPwdTypeCif.setOnClickListener(v -> presenter.passwordRecoveryByFin());
        binding.forgotPwdTypeCard.setOnClickListener(v -> presenter.passwordRecoveryByPan());
    }
    
    @Override
    public void onPause() {
        super.onPause();

        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
