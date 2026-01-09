package az.btb.mobilebanking.ui.sign_in;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignInBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_SCREEN_FAKE_TOKEN;

public class SignInFragment extends Fragment<FragmentSignInBinding> implements SignInView {

    @Nullable private String phone;
    @Nullable private String email;

    @InjectPresenter SignInPresenter presenter;

    @ProvidePresenter SignInPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP_SCOPE).getInstance(SignInPresenter.class);
    }

    public SignInFragment() {
        super(R.layout.fragment_sign_in, false);
    }
    
    @NonNull
    public static SignInFragment getInstance(@Nullable String phone, @Nullable String email) {
        Bundle b = new Bundle(2);
        b.putString("phone", phone);
        b.putString("email", email);

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = requireArguments().getString("phone");
        email = requireArguments().getString("email");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        final boolean isEmailValid = email != null && !email.trim().isEmpty();
        final boolean isPhoneValid = phone != null && !phone.trim().isEmpty();

        binding.signInByEmail.setEnabled(isEmailValid);
        binding.signInByNumber.setEnabled(isPhoneValid);
        if (email.length() > SIGN_IN_SCREEN_FAKE_TOKEN.length() || phone.length() > SIGN_IN_SCREEN_FAKE_TOKEN.length())
            binding.registerSection.setVisibility(View.GONE);

        binding.signInByNumber.setOnClickListener(v -> {
            if (phone.equals(SIGN_IN_SCREEN_FAKE_TOKEN))
                presenter.signInByNumber(null);
            else
                presenter.signInByNumber(phone);
        });
        binding.signInByEmail.setOnClickListener(v -> {
            if (email.equals(SIGN_IN_SCREEN_FAKE_TOKEN))
                presenter.signInByEmail(null);
            else
                presenter.signInByEmail(email);
        });

        binding.signUp.setOnClickListener(v -> presenter.signUp());
    }
    
    @Override
    public void onPause() {
        super.onPause();

        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
