package apretaste.Helper;

/**
 * Created by Raymond Arteaga on 06/07/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.apretaste.R;

public class AssetDrawableCompatEditText extends AppCompatEditText {
    public AssetDrawableCompatEditText(Context context) {
        super(context);
    }

    public AssetDrawableCompatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public AssetDrawableCompatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.demo);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.demo_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.demo_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.demo_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.demo_drawableTopCompat);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.demo_drawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.demo_drawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.demo_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.demo_drawableTopCompat, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false;
    }

}
