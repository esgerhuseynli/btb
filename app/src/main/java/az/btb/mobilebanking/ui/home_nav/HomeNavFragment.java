package az.btb.mobilebanking.ui.home_nav;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentHomeNavBinding;
import az.btb.mobilebanking.databinding.SignOutConfirmerDialogBinding;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.LocalRouter;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.APP_SCOPE;
import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class HomeNavFragment extends Fragment<FragmentHomeNavBinding>
    implements HomeNavView, NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter HomeNavPresenter presenter;

    private String customerName = null;

    @Inject LocalRouter localRouter;

    @ProvidePresenter HomeNavPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(HomeNavPresenter.class);
    }

    public HomeNavFragment() {
        super(R.layout.fragment_home_nav);
    }

    @NonNull
    public static HomeNavFragment getInstance() {
        return new HomeNavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        presenter.setLocalRouter(getCicerone().getRouter());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (customerName == null)
            presenter.getAndShowCustomerName();
        else
            showCustomerName(customerName);

        initBottomNavBar();

        if (savedInstanceState == null)
            getCicerone().getRouter().replaceScreen(new MainScreens.HomeScreen());

        binding.drawer.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        binding.notificationsPage.setOnClickListener(v -> presenter.goToNotification());
        binding.sideNav.setNavigationItemSelectedListener(this);

        // https://www.semicolonworld.com/question/48905/changing-text-color-of-menu-item-in-navigation-drawer
        changeNavMenuColor(binding.sideNav.getMenu().getItem(2)); // Menim vesaitlerim
        changeNavMenuColor(binding.sideNav.getMenu().getItem(3)); // Ödənişlər və köçürmələr
        changeNavMenuColor(binding.sideNav.getMenu().getItem(4)); // Bank haqqinda
        changeNavMenuColor(binding.sideNav.getMenu().getItem(5)); // Diger
    }

    private void initBottomNavBar() {
        binding.mainPageIcon.getDrawable().setTint(getResources().getColor(R.color.colorPrimary));
        binding.mainPageLayout.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.colorAccent));
        binding.paymentsPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
        binding.transfersPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));

        binding.mainPage.setOnClickListener(v -> {
            getCicerone().getRouter().navigateTo(new MainScreens.HomeScreen());
            binding.notificationsPage.setVisibility(View.VISIBLE);
            binding.mainPageIcon.getDrawable().setTint(getResources().getColor(R.color.colorPrimary));
            binding.topCenterLogo.setVisibility(View.VISIBLE);
            binding.mainPageLayout.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.colorAccent));
            binding.paymentsPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
            binding.transfersPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
        });
        binding.mainPageLayout.setOnClickListener(v -> {
            getCicerone().getRouter().navigateTo(new MainScreens.HomeScreen());
            binding.notificationsPage.setVisibility(View.VISIBLE);
            binding.mainPageIcon.getDrawable().setTint(getResources().getColor(R.color.colorPrimary));
            binding.topCenterLogo.setVisibility(View.VISIBLE);
            binding.mainPageLayout.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.colorAccent));
            binding.paymentsPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
            binding.transfersPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
        });
        binding.paymentsPage.setOnClickListener(v -> {
            binding.notificationsPage.setVisibility(View.GONE);
            binding.topCenterLogo.setVisibility(View.GONE);
            binding.mainPageIcon.getDrawable().setTint(getResources().getColor(R.color.colorAccent));
            binding.mainPageLayout.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            binding.paymentsPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.colorPrimaryDark));
            binding.transfersPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
            getCicerone().getRouter().navigateTo(new MainScreens.PaymentsScreen(true));
        });
        binding.transfersPage.setOnClickListener(v -> {
            binding.notificationsPage.setVisibility(View.VISIBLE);
            binding.topCenterLogo.setVisibility(View.GONE);
            binding.mainPageIcon.getDrawable().setTint(getResources().getColor(R.color.colorAccent));
            binding.mainPageLayout.getBackground().setTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            binding.paymentsPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.bottomBarMenuItemTint));
            binding.transfersPage.getCompoundDrawables()[1].setTint(getResources().getColor(R.color.colorPrimaryDark));
            getCicerone().getRouter().navigateTo(new MainScreens.TransfersScreen(true));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getCicerone().getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
        super.onPause();
        getCicerone().getNavigatorHolder().removeNavigator();
    }

    private Cicerone<Router> getCicerone() {
        return localRouter.getCicerone(Constants.HOME_CICERONE_NAME);
    }

    private Navigator getNavigator() {
        return new SupportAppNavigator(getActivity(), getChildFragmentManager(), R.id.fragment_main_container);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            final AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            final SignOutConfirmerDialogBinding dialogBinding =
                    SignOutConfirmerDialogBinding.inflate(getLayoutInflater());

            dialogBinding.no.setOnClickListener(v -> dialog.dismiss());
            dialogBinding.yes.setOnClickListener(v -> {
                showLoading(true);
                dialog.dismiss();
                presenter.signOut();
            });

            dialog.setView(dialogBinding.getRoot());
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            if (item.getItemId() == R.id.paymentsPage || item.getItemId() == R.id.transfers) {
                binding.notificationsPage.setVisibility(View.GONE);
            } else {
                binding.notificationsPage.setVisibility(View.VISIBLE);
            }
            presenter.sideMenuItemClicked(item.getItemId());
            item.setChecked(true);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        return false;
    }

    @Override
    public void showCustomerName(@Nullable final String customerName) {
        if (customerName != null) {
            this.customerName = customerName;
            final int lastSpaceIndex = customerName.lastIndexOf(' ');
            if (lastSpaceIndex != -1)

                binding.sideNav.getMenu().getItem(0).getSubMenu().getItem(0).setTitle(customerName.substring(0, customerName.lastIndexOf(' ')));
            else
                binding.sideNav.getMenu().getItem(0).getSubMenu().getItem(0).setTitle(customerName);
        }
    }

    @Override
    public void showError(@NonNull String msg) {
        if (!msg.isEmpty())
            Utils.snackbar(binding.getRoot(), msg);
    }

    @Override
    public void showLoading(boolean shouldShow) {
        binding.progressBar.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void clearAccountData() {
        refreshBankCards(new ArrayList<>());
        refreshBankAccounts(new ArrayList<>());
        Utils.postSignOutCleanUp(requireActivity());
    }

    private void changeNavMenuColor(@NonNull MenuItem menuItem) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, spanString.length(), 0); // fix the color to white
        menuItem.setTitle(spanString);
    }
}
