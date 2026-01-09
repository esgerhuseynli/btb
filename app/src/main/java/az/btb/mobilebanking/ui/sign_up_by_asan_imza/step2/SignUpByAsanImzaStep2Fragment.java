package az.btb.mobilebanking.ui.sign_up_by_asan_imza.step2;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentSignUpByAsanImzaStep2Binding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.AsanImzaData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SignUpByAsanImzaStep2Fragment extends Fragment<FragmentSignUpByAsanImzaStep2Binding> implements SignUpByAsanImzaStep2View {

    private AsanImzaData data;

    @InjectPresenter SignUpByAsanImzaStep2Presenter presenter;

    @ProvidePresenter SignUpByAsanImzaStep2Presenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(SignUpByAsanImzaStep2Presenter.class);
    }

    public SignUpByAsanImzaStep2Fragment() {
        super(R.layout.fragment_sign_up_by_asan_imza_step2, false);
    }

    @NonNull
    public static SignUpByAsanImzaStep2Fragment getInstance(AsanImzaData data) {
        Bundle b = new Bundle();
        b.putParcelable("asanImzaData", data);

        SignUpByAsanImzaStep2Fragment fragment = new SignUpByAsanImzaStep2Fragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = requireArguments().getParcelable("asanImzaData");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.citizenTypes.setAdapter(
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.citizen_types,
                android.R.layout.simple_list_item_1
            )
        );
        binding.citizenTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    binding.asanImzaCode.getText().clear();
                    binding.asanImzaCode.setHint(R.string.enter_asan_pin);
                    InputFilter[] filters = new InputFilter[2];
                    filters[0] = new InputFilter.LengthFilter(7);
                    filters[1] = new InputFilter.AllCaps();
                    binding.asanImzaCode.setFilters(filters);
                    binding.asanImzaCode.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    binding.asanImzaCode.getText().clear();
                    binding.asanImzaCode.setHint(R.string.enter_asan_tax_number);
                    InputFilter[] filters = new InputFilter[1];
                    filters[0] = new InputFilter.LengthFilter(10);
                    binding.asanImzaCode.setFilters(filters);
                    binding.asanImzaCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }

                // ona gore +1 edirem ki, { 1 - for physic, 2 - non-physic }
                data.citizenType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        binding.asanImzaCode.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                binding.next.performClick();

            return false;
        });
        binding.next.setOnClickListener(v -> {
            showLoading(true);

            final String pinCodeOrTaxNumber = binding.asanImzaCode.getText().toString();

            if (!pinCodeOrTaxNumber.trim().isEmpty()) {
                data.pinCodeOrTaxNumber = pinCodeOrTaxNumber.toUpperCase();
                presenter.goNext(data);
            } else {
                showError(getString(R.string.fill_all_fields));
                showLoading(false);
            }
        });
    }

    private boolean isFormValid(@Nullable String numberData, String passwordData) {
        if (numberData == null || numberData.length() != 17) {
            Utils.snackbar(binding.getRoot(), R.string.invalid_mobile_number);
            return false;
        }

        if (passwordData.length() != 6) {
            Utils.snackbar(binding.getRoot(), R.string.code_should_be_six);
            return false;
        }

        return true;
    }

    @Override
    public void showLoading(boolean check) {
        Utils.modifyChildrenEnableStatus(binding.root, !check);
        binding.progressBar.setVisibility(check ? View.VISIBLE : View.INVISIBLE);
        binding.next.setEnabled(!check);
        binding.next.setClickable(!check);
    }

    @Override
    public void showError(String msg) {
        showLoading(false);
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        Utils.forceBypassPinFingerprintScreen(requireActivity());
    }
}
