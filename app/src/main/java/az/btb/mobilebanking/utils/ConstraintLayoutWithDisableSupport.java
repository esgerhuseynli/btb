package az.btb.mobilebanking.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * https://proandroiddev.com/the-easiest-way-to-toggle-complete-layout-to-grayscale-on-android-fa86ac0a754
 */
public class ConstraintLayoutWithDisableSupport extends ConstraintLayout {

    private boolean disabled = false;

    private static final Paint paint = new Paint();

    static {
        ColorMatrix cm = new ColorMatrix(
            new float[]{
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0f,    0f,    0f,    1f, 0f
            }
        );
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    public ConstraintLayoutWithDisableSupport(Context context) {
        super(context);
    }

    public ConstraintLayoutWithDisableSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintLayoutWithDisableSupport(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        requestLayout();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (disabled)
            canvas.saveLayer(null, paint);

        super.dispatchDraw(canvas);

        if (disabled)
            canvas.restore();
    }

    @Override
    public void draw(Canvas canvas) {
        if (disabled)
            canvas.saveLayer(null, paint);

        super.draw(canvas);

        if (disabled)
            canvas.restore();
    }
}
