package shop.plea.and.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;

/**
 * Created by kwon7575 on 2017-11-10.
 */

public class CustomFontBtn extends Button {

    private boolean stroke = true;
    private String strokColor = "#222222";

    public CustomFontBtn(Context context) {
        super(context);
    }

    public CustomFontBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIncludeFontPadding(false);
        setCustomFont(context, attrs);
    }

    public CustomFontBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setIncludeFontPadding(false);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontBtn);
        String customFont = a.getString(R.styleable.CustomFontBtn_customFontBtn);
        this.stroke = a.getBoolean(R.styleable.CustomFontBtn_customFontBtn, false);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {

        if(asset == null)
        {
            return false;
        }
        else
        {
            Typeface tf;
            try {
                tf = Typeface.createFromAsset(ctx.getAssets(), asset);

                //  Log.e(TAG, "tf typeface: "+ tf.isBold());
            } catch (Exception e) {

                e.printStackTrace();
                return false;
            }

            setTypeface(tf);
            return true;
        }

    }

    public void isStroke(boolean stroke)
    {
        this.stroke = stroke;

    }

    public void setStrokeColor(String color)
    {
        strokColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        ColorStateList states = getTextColors(); // text color 값 저장

        // stroke 그리기
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(18.0f);
        setTextColor(Color.parseColor(strokColor));
        if(this.stroke)
            super.onDraw(canvas);

        getPaint().setStyle(Paint.Style.FILL);
        setTextColor(states);
        super.onDraw(canvas);
    }
}
