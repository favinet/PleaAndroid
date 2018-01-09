package shop.plea.and.ui.adapter.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.ui.adapter.SimpleItemAdapter;

/**
 * Created by kwon7575 on 2017-10-13.
 */

public class TagListHolder extends BaseViewHolder<String> implements View.OnClickListener{

    @BindView(R.id.text) TextView mTextView;

    private Context mContext;
    private View mView;
    private SimpleItemAdapter.onClickCallback mOnClickCallback;

    public static TagListHolder newInstance(ViewGroup parent, SimpleItemAdapter.onClickCallback callback)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staggered_horizontal, parent, false);
        return new TagListHolder(view, parent.getContext(), callback);
    }

    public TagListHolder(View itemView, Context context, SimpleItemAdapter.onClickCallback callback) {
        super(itemView);

        this.mContext = context;
        this.mView = itemView;
        this.mOnClickCallback = callback;

        mView.setOnClickListener(this);
    }

    @Override
    public void onBindView(String strings) {
        if(mTextView == null)
            Log.d("mTextView : ", "NULL");
        else
        {
            if(strings == null)
                mTextView.setText("");
            else
                mTextView.setText(strings);
        }
    }

    @Override
    public void onClick(View v) {
        mOnClickCallback.onClick(v);
    }
}
