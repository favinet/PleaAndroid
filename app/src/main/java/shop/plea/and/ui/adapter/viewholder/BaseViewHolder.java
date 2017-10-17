package shop.plea.and.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by kwon7575 on 2017-10-13.
 */

public abstract class BaseViewHolder<ITEM> extends RecyclerView.ViewHolder{

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void onBindView(ITEM item);

    public void refresh(){};
}
