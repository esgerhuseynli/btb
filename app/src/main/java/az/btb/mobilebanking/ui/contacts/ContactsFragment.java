package az.btb.mobilebanking.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.FragmentContactsBinding;
import az.btb.mobilebanking.utils.Fragment;
import moxy.MvpView;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class ContactsFragment extends Fragment<FragmentContactsBinding> implements MvpView {

    @InjectPresenter ContactsPresenter presenter;

    @ProvidePresenter ContactsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(ContactsPresenter.class);
    }

    @NonNull public static ContactsFragment getInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
        super(R.layout.fragment_contacts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());
        binding.callServicePointCallCenter.setOnClickListener(v -> openDialPad());
    }

    private void openDialPad() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:946"));
        startActivity(intent);
    }
}
