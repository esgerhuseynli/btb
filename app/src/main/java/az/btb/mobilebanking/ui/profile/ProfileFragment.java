package az.btb.mobilebanking.ui.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentProfileBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.MobileUserData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ProfileFragment extends Fragment<FragmentProfileBinding> implements ProfileView {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @InjectPresenter ProfilePresenter presenter;

    @ProvidePresenter ProfilePresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ProfilePresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.editEmail.setOnClickListener(v -> presenter.editEmail());
        binding.editPhoneNumber.setOnClickListener(v -> presenter.editPhoneNumber());
        binding.changePassword.setOnClickListener(v -> presenter.changePassword());

        presenter.getProfileData();
    }

    @Override
    public void setProfileInfo(@NonNull MobileUserData profileInfo) {
        showLoading(false);
        binding.personalCif.setText(profileInfo.getCustomerCIFNumber());
        binding.email.setText(profileInfo.getEmail());
        binding.phoneNumber.setText(
            String.format(
                getString(R.string.hidden_mobile_number),
                profileInfo.getMobileNumber().substring(profileInfo.getMobileNumber().length() - 2)
            )
        );
    }

    @Override
    public void showLoading(boolean isLoading) {
        Utils.modifyChildrenEnableStatus(binding.root, !isLoading);

        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
