package az.btb.mobilebanking.ui.news_details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.BankNewsImageBinding;
import az.btb.mobilebanking.databinding.FragmentNewsDetailsBinding;
import az.btb.mobilebanking.models.BankNews;
import az.btb.mobilebanking.utils.CenterScrollListener;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.ScrollZoomLayoutManager;
import az.btb.mobilebanking.utils.Utils;
import kotlin.collections.CollectionsKt;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class NewsDetailsFragment extends Fragment<FragmentNewsDetailsBinding> implements NewsDetailsView {

    private BankNews bankNews;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ROOT);

    @InjectPresenter NewsDetailsPresenter presenter;

    @ProvidePresenter NewsDetailsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(NewsDetailsPresenter.class);
    }

    public NewsDetailsFragment() {
        super(R.layout.fragment_news_details);
    }

    @NonNull public static NewsDetailsFragment getInstance(BankNews bankNews) {
        Bundle b = new Bundle();
        b.putSerializable("bankNews", bankNews);
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bankNews = (BankNews) getArguments().getSerializable("bankNews");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.goBack.setOnClickListener(v -> presenter.goBack());

        final ScrollZoomLayoutManager scrollZoomLayoutManager = new ScrollZoomLayoutManager(getContext(), Utils.dp2Px(10));
        binding.newsList.addOnScrollListener(new CenterScrollListener());
        binding.newsList.setLayoutManager(scrollZoomLayoutManager);

        ItemPropsBinder<BankNewsImageBinding, String> itemPropsBinder = (binding, imageBase64) -> {
            Utils.setImageToImageView(binding.newsImage, imageBase64);
        };

        ItemsAdapter<BankNewsImageBinding, String> adapter = new ItemsAdapter<>(
            R.layout.bank_news_image, CollectionsKt.filterNotNull(bankNews.getNewsImages()), itemPropsBinder
        );

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.newsList);

        binding.newsList.setAdapter(adapter);
        binding.indicator.attachToRecyclerView(binding.newsList, pagerSnapHelper);

        binding.title.setText(bankNews.getHeader());
        binding.date.setText(formatter.format(new Date(bankNews.getPublishTimestamp())));
        binding.category.setText(bankNews.getCategoryName());
        binding.content.setText(bankNews.getText());
    }
}
