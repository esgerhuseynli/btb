package az.btb.mobilebanking.ui.exchange_rates;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ExchangeRatesPagerAdapter;
import az.btb.mobilebanking.databinding.FragmentExchangeRatesBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.ExchangeRateData;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ExchangeRatesFragment extends Fragment<FragmentExchangeRatesBinding> implements ExchangeRatesView {

    private int selectedPagePos = 0;

    public ExchangeRatesFragment() {
        super(R.layout.fragment_exchange_rates);
    }

    public static ExchangeRatesFragment getInstance() {
        return new ExchangeRatesFragment();
    }

    @InjectPresenter ExchangeRatesPresenter presenter;

    @ProvidePresenter ExchangeRatesPresenter providePresenter() {
        return Toothpick.openScope(Scopes.SERVER_SCOPE).getInstance(ExchangeRatesPresenter.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        Utils.setDateField(y, m, d, binding.fromToDate);

        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, binding.fromToDate);

            binding.progressBar.setVisibility(View.VISIBLE);

            // get exchange rates for selected date immediately
            presenter.getExchangeRates(binding.fromToDate.getText().toString());
        };
        binding.fromToDate.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(binding.fromToDate.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "exchangeRatesDatePicker");
        });

        binding.exchangeRatesPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedPagePos = position;
            }
        });

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        Utils.modifyChildrenEnableStatus(binding.root, false);

        presenter.getExchangeRates(binding.fromToDate.getText().toString());
    }

    @Override
    public void showError(@NonNull String message) {
        Utils.modifyChildrenEnableStatus(binding.root, true);
        binding.progressBar.setVisibility(View.GONE);
        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    @Override
    public void setExchangeRatesData(@NonNull Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates) {
        Utils.modifyChildrenEnableStatus(binding.root, true);

        final ExchangeRatesPagerAdapter pagerAdapter = new ExchangeRatesPagerAdapter(this, cashNonCashRates);
        binding.exchangeRatesPager.setAdapter(pagerAdapter);

        if (selectedPagePos == 0)
            new TabLayoutMediator(binding.exchangeRatesTab, binding.exchangeRatesPager, (tab, position) -> {
                if (position == 0)
                    tab.setText(R.string.exchange_rate_type_cash);
                else
                    tab.setText(R.string.exchange_rate_type_non_cash);
            }).attach();
        else
            binding.exchangeRatesPager.setCurrentItem(selectedPagePos);

        binding.progressBar.setVisibility(View.GONE);
    }
}
