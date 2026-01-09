package az.btb.mobilebanking.ui.sign_up_types;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpTypesBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.AsanImzaData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpTypesFragment extends Fragment<FragmentSignUpTypesBinding> implements SignUpTypesView {

    private int screenType;
    @Nullable private String verifyCode;
    @Nullable private String phone;
    @Nullable private String email;
    @Nullable private AsanImzaData data;

    @InjectPresenter SignUpTypesPresenter presenter;

    @ProvidePresenter SignUpTypesPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP_SCOPE).getInstance(SignUpTypesPresenter.class);
    }
    
    public SignUpTypesFragment() {
        super(R.layout.fragment_sign_up_types, false);
    }
    
    @NonNull
    public static SignUpTypesFragment getInstance(
        int screenType, @Nullable String verifyCode,
        @Nullable String phone, @Nullable String email,
        @Nullable AsanImzaData data
    ) {
        final Bundle b = new Bundle();
        b.putInt("screenType", screenType);
        b.putString("verifyCode", verifyCode);
        b.putString("phone", phone);
        b.putString("email", email);
        b.putParcelable("data", data);

        final SignUpTypesFragment fragment = new SignUpTypesFragment();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenType = requireArguments().getInt("screenType");
        verifyCode = requireArguments().getString("verifyCode");
        phone = requireArguments().getString("phone");
        email = requireArguments().getString("email");
        data = requireArguments().getParcelable("data");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (verifyCode != null) {
            binding.signUpTypeCif.setText(R.string.register_by_number);
            binding.signUpTypeCif.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_phone_red, 0, 0);

            binding.signUpTypeCard.setText(R.string.register_by_email);
            binding.signUpTypeCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_email_red, 0, 0);

            binding.signUpTypeCard.setEnabled(email != null && !email.trim().isEmpty());

            binding.signUpAsanImza.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        binding.signUpTypeCif.setOnClickListener(v -> presenter.signUpByCif());
        binding.signUpTypeCard.setOnClickListener(v -> presenter.signUpByCard());

        binding.signUpAsanImza.setOnClickListener(v -> presenter.signUpByAsanImza());

        if (screenType == 3)
            binding.signUpAsanImza.setVisibility(View.GONE);

        if (verifyCode != null) {
            binding.signUpTypeCif.setOnClickListener(v -> presenter.signUpByNumber(screenType, verifyCode, phone, data));
            binding.signUpTypeCard.setOnClickListener(v -> presenter.signUpByEmail(screenType, verifyCode, email, data));
        }

        binding.signIn.setOnClickListener(v -> presenter.signIn());
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
