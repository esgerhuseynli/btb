package az.btb.mobilebanking.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;

import androidx.appcompat.widget.AppCompatEditText;

import az.btb.mobilebanking.R;

public class OtpEditText extends AppCompatEditText {
    private float mSpace = 16; //24 dp by default, space between the lines
    private float mNumChars = 7;
    private float mLineSpacing = 8; //8dp by default, height of the text from our lines
    private int mMaxLength = 7;
    private float mLineStroke = 1;
    private Paint mLinesPaint;
    private OnClickListener mClickListener;

    public OtpEditText(Context context) {
        super(context);
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OtpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        mLineStroke = multi * mLineStroke;
        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStrokeWidth(mLineStroke);
        mLinesPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        setBackgroundResource(0);
        mSpace = multi * mSpace; //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing; //convert to pixels for our density
        mNumChars = mMaxLength;

        super.setOnClickListener(v -> {
            // When tapped, move cursor to end of text.
            setSelection(getText().length());
            if (mClickListener != null) {
                mClickListener.onClick(v);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        float mCharSize;
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 2 - 1));
        } else {
            mCharSize = (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        //Text Width
        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for (int i = 0; i < mNumChars; i++) {
            if(i!=3){
                canvas.drawLine(startX, bottom, startX + mCharSize, bottom, mLinesPaint);
                if (getText().length() > i) {
                    float middle = (startX-1) + mCharSize / 2;
                    canvas.drawText(text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing, getPaint());
                }
                if (mSpace < 0) {
                    startX += mCharSize * 2;
                } else {
                    startX += mCharSize + mSpace;
                }
            }
            else {
                if (getText().length() > i) {
                    float middle = (startX+5) + mCharSize / 2;
                    canvas.drawText(text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing, getPaint());
                }
                if (mSpace < 0) {
                    startX += mCharSize * 2;
                } else {
                    startX += mCharSize + mSpace;
                }
            }

        }
    }
}