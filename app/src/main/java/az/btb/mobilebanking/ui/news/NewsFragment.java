package az.btb.mobilebanking.ui.news;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.adapters.ItemsAdapter;
import az.btb.mobilebanking.databinding.FragmentNewsBinding;
import az.btb.mobilebanking.databinding.NewsListItemBinding;
import az.btb.mobilebanking.models.BankNews;
import az.btb.mobilebanking.utils.DatePickerFragment;
import az.btb.mobilebanking.utils.Fragment;
import az.btb.mobilebanking.utils.ItemPropsBinder;
import az.btb.mobilebanking.utils.Utils;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.btb.mobilebanking.di.Scopes.SERVER_SCOPE;

public class NewsFragment extends Fragment<FragmentNewsBinding> implements NewsView {

    @InjectPresenter NewsPresenter presenter;

    @ProvidePresenter NewsPresenter providePresenter() {
        return Toothpick.openScope(SERVER_SCOPE).getInstance(NewsPresenter.class);
    }

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    @NonNull
    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        Utils.setDateField(y, m, d, binding.fromDate);
        Utils.setDateField(y, m, d, binding.toDate);

        setFromToDateClickListener(binding.fromDate);
        setFromToDateClickListener(binding.toDate);

        binding.fromDate.setText("01-01-2019");

        binding.goBack.setOnClickListener(v -> presenter.goBack());

        presenter.getNews(binding.fromDate.getText().toString(), binding.toDate.getText().toString());
    }

    @Override
    public void showError(@NonNull String message) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        if (!message.isEmpty())
            Utils.snackbar(binding.getRoot(), message);
    }

    @Override
    public void showNews(@NonNull List<BankNews> bankNewsList) {
        if (bankNewsList.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            binding.groupedNews.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.VISIBLE);
            return;
        }

        //Your RecyclerView
        binding.groupedNews.setHasFixedSize(true);

//        List<BankNews> allNews = new ArrayList<>();
//        for (List<BankNews> news : dateSortedMap.values())
//            allNews.addAll(news);

        ItemPropsBinder<NewsListItemBinding, BankNews> binder = (binding, bankNews) -> {
            if (bankNews.getNewsLogoImage() != null)
                Utils.setImageToImageView(binding.newsImage, bankNews.getNewsLogoImage());
            binding.newsHeader.setText(bankNews.getHeader());
            binding.newsDate.setText(bankNews.getPublishingDate());
            binding.newsCat.setText(bankNews.getCategoryName());

            binding.getRoot().setOnClickListener(v -> presenter.showNewsDetails(bankNews));
        };

        //Your RecyclerView.Adapter
        ItemsAdapter<NewsListItemBinding, BankNews> mAdapter = new ItemsAdapter<>(
            R.layout.news_list_item, bankNewsList, binder
        );

        // This is the code to provide a sectioned list
//        List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

//        List<Long> allDates = new ArrayList<>(dateSortedMap.keySet());
//        final int count = allDates.size();

        //Sections
//        sections.add(
//            new SectionedRecyclerViewAdapter.Section(
//                0,
//                Utils.dateFormatter.format(new Date(allDates.get(0)))
//            )
//        );

//        int previousPosition = 0;
//        for (int i = 1; i < count; i++) {
//            int position = previousPosition + dateSortedMap.get(allDates.get(i - 1)).size();
//            previousPosition = position;
//
//            sections.add(
//                new SectionedRecyclerViewAdapter.Section(
//                    position,
//                    Utils.dateFormatter.format(new Date(allDates.get(i)))
//                )
//            );
//        }

        //Add your adapter to the sectionAdapter
//        SectionedRecyclerViewAdapter.Section[] sectionsData = new SectionedRecyclerViewAdapter.Section[sections.size()];
//        SectionedRecyclerViewAdapter mSectionedAdapter = new
//            SectionedRecyclerViewAdapter(
//            requireContext(),
//            R.layout.notifications_list_header,
//            R.id.notification_date,
//            mAdapter
//        );
//        mSectionedAdapter.setSections(sections.toArray(sectionsData));

        // Apply this adapter to the RecyclerView
        binding.groupedNews.setAdapter(mAdapter);

        binding.progressBar.setVisibility(View.GONE);
        binding.noItem.setVisibility(View.GONE);
        binding.groupedNews.setVisibility(View.VISIBLE);
    }

    private void setFromToDateClickListener(@NonNull final TextView field) {
        final DatePickerDialog.OnDateSetListener listener = (view1, year, month, day) -> {
            Utils.setDateField(year, month, day, field);

            binding.progressBar.setVisibility(View.VISIBLE);

            // get exchange rates for selected date immediately
            presenter.getNews(
                binding.fromDate.getText().toString(),
                binding.toDate.getText().toString()
            );
        };

        field.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(field.getText().toString(), listener);
            newFragment.show(getParentFragmentManager(), "notificationsDatePicker");
        });
    }
}
