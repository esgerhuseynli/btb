package az.btb.mobilebanking.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import az.btb.mobilebanking.R;

public class HomeItemView extends LinearLayout {


    TextView text;
    ImageView icon;

    public HomeItemView(Context context) {
        this(context, null);
    }

    public HomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_home_menu, this);
        text = findViewById(R.id.text);
        icon = findViewById(R.id.icon);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeItemView);
        text.setText(a.getString(R.styleable.HomeItemView_text));
        icon.setImageDrawable(a.getDrawable(R.styleable.HomeItemView_image));
        a.recycle();
    }

}

