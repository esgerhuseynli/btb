package az.btb.mobilebanking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.models.WelcomePagerModel;

public class WelcomePagerAdapter extends PagerAdapter {

    private final List<WelcomePagerModel> model;
    private final LayoutInflater mLayoutInflater;

    public WelcomePagerAdapter(Context context, List<WelcomePagerModel> model) {
        this.model = model;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_welcome_pager, container, false);

        ImageView logo = itemView.findViewById(R.id.logo);
        TextView title = itemView.findViewById(R.id.title);
        TextView description = itemView.findViewById(R.id.description);

        logo.setImageResource(model.get(position).getImage());
        title.setText(model.get(position).getTitle());
        description.setText(model.get(position).getDescription());

        if (position == 0)
            itemView.findViewById(R.id.icon).setVisibility(View.VISIBLE);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
