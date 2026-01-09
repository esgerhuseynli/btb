package az.btb.mobilebanking.ui.fingerprint;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentFingerprintBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class FingerprintFragment extends Fragment<FragmentFingerprintBinding> implements FingerprintView {

    private boolean isSignInScreen;

    @InjectPresenter FingerprintPresenter presenter;

    @ProvidePresenter FingerprintPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP_SCOPE).getInstance(FingerprintPresenter.class);
    }
    
    public FingerprintFragment(final boolean isSignInScreen) {
        super(R.layout.fragment_fingerprint, !isSignInScreen);
    }
    
    @NonNull
    public static FingerprintFragment getInstance(final boolean isSignInScreen) {
        Bundle b = new Bundle();
        b.putBoolean("isSignInScreen", isSignInScreen);

        FingerprintFragment fragment = new FingerprintFragment(isSignInScreen);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSignInScreen = requireArguments().getBoolean("isSignInScreen", true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter.listen(isSignInScreen, requireActivity());
        
        if (!isSignInScreen) {
            binding.topPanel.setVisibility(View.VISIBLE);
            binding.goBack.setOnClickListener(v -> presenter.goBack());
            binding.fingerprintText.setVisibility(View.GONE);
            binding.later.setVisibility(View.GONE);
        }
        binding.later.setOnClickListener(v -> presenter.goToHome(false, isSignInScreen));
    }

    @Override
    public void showFingerprintMsg(@StringRes int msg) {
        Utils.snackbar(binding.getRoot(), msg);
    }
    
    @Override
    public void onPause() {
        super.onPause();

        if (isSignInScreen)
            Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
