package shop.plea.and.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwon7575 on 2017-10-28.
 */

public class DrawerLayoutHorizontalSupport extends DrawerLayout {

    private RecyclerView mRecyclerView;
    private List<View> ignoredViews;

    public DrawerLayoutHorizontalSupport(Context context) {
        super(context);
        ignoredViews = new ArrayList<>();
    }

    public DrawerLayoutHorizontalSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
        ignoredViews = new ArrayList<>();
    }

    public DrawerLayoutHorizontalSupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ignoredViews = new ArrayList<>();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInside(ev) && isDrawerOpen(Gravity.RIGHT))
            return false;
        try {
            return super.onInterceptTouchEvent(ev);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    private boolean isInside(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY()))
                return true;
        }
        return false;
    }

    public void addIgnoredViews(View view) {
        ignoredViews.add(view);
    }
}