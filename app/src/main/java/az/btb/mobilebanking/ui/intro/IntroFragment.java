package az.btb.mobilebanking.ui.intro;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.WelcomePagerAdapter;
import az.btb.mobilebanking.databinding.FragmentIntroBinding;
import az.btb.mobilebanking.di.Scopes;
import az.btb.mobilebanking.models.WelcomePagerModel;
import moxy.MvpAppCompatFragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class IntroFragment extends MvpAppCompatFragment implements MvpView {

    private FragmentIntroBinding binding;

    private final List<WelcomePagerModel> list = new ArrayList<>();

    public static IntroFragment getInstance() {
        return new IntroFragment();
    }

    @InjectPresenter IntroPresenter presenter;

    @ProvidePresenter IntroPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP_SCOPE).getInstance(IntroPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpannableString firstPageTitle = new SpannableString("BTB Mobile\n" + getString(R.string.intro_first_page_title));
//        firstPageTitle.setSpan(new AbsoluteSizeSpan(30, true), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        firstPageTitle.setSpan(
            new StyleSpan(Typeface.BOLD),
            0, 10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        SpannableString firstPageDesc = new SpannableString("BTB Mobile" + getString(R.string.intro_first_page_desc));
//        firstPageDesc.setSpan(new AbsoluteSizeSpan(35, true), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        firstPageDesc.setSpan(
//            new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor)),
//            4, 5,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );

        SpannableString secondPageTitle = new SpannableString(getString(R.string.nav_menu_item_payments_and_transactions));
        SpannableString secondPageDesc = new SpannableString("BTB Mobile" + getString(R.string.intro_second_page_desc));
//        secondPageDesc.setSpan(new AbsoluteSizeSpan(35, true), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        secondPageDesc.setSpan(
//            new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor)),
//            4, 5,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );

        SpannableString thirdPageTitle = new SpannableString(getString(R.string.third_page_title));
        SpannableString thirdPageDesc = new SpannableString(getString(R.string.intro_third_page_desc));
//        thirdPageDesc.setSpan(new AbsoluteSizeSpan(35, true), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        thirdPageDesc.setSpan(
//            new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor)),
//            4, 5,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );

        list.add(new WelcomePagerModel(R.drawable.welcome_logo1, firstPageTitle, firstPageDesc));
        list.add(new WelcomePagerModel(R.drawable.welcome_logo2, secondPageTitle, secondPageDesc));
        list.add(new WelcomePagerModel(R.drawable.welcome_logo3, thirdPageTitle, thirdPageDesc));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIntroBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WelcomePagerAdapter adapter = new WelcomePagerAdapter(requireContext(), list);
        binding.welcomeViewPager.setAdapter(adapter);
        binding.indicator.setViewPager(binding.welcomeViewPager);

        binding.signInButton.setOnClickListener(v -> presenter.goToSignIn());
        binding.signUpButton.setOnClickListener(v -> presenter.goToSignUp());
    }
}
