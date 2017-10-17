package shop.plea.and.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import shop.plea.and.ui.adapter.multiAdapter.MultiItemAdapter;
import shop.plea.and.ui.adapter.viewholder.BaseViewHolder;
import shop.plea.and.ui.adapter.viewholder.TagListHolder;

/**
 * Created by kwon7575 on 2017-10-13.
 */

public class SimpleItemAdapter extends MultiItemAdapter {

    public static final int VIEW_TYPE_LIST = 0;
    private onClickCallback mClickCallback;

    public interface onClickCallback{
        void onClick(View view);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_LIST)
            return TagListHolder.newInstance(parent, mClickCallback);
        else
            return null;
    }

    public void setClickCallback(onClickCallback callback)
    {
        this.mClickCallback = callback;
    }
}
