package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mightyfrog.widget.CenteringRecyclerView;

import shop.plea.and.R;

/**
 * Created by kwon7575 on 2017-10-12.
 */

public class ListSampleActivity extends PleaActivity{

    private CenteringRecyclerView mCenteringRecyclerView;
    public String[] mTagList = new String[] {"#test","#testtest", "#test000999", "#test2222", "#test2ddd", "#testsfwaerr2", "#test234szz", "#test2djfjhsl",
            "#testsafkjrar", "#test3aw2", "#test2zsdfhh2", "#testzfdzghgy", "#testcftguvd", "#test234yxdzr", "#test2cvhkok", "#testxcvtyc", "#testxrdct"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sample);

        mCenteringRecyclerView = (CenteringRecyclerView) findViewById(R.id.recycler_view);
        mCenteringRecyclerView.setAdapter(new StaggeredDemoAdapter(R.layout.item_staggered_horizontal));
        mCenteringRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
    }

    private class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        public final int ITEM_COUNT = mTagList.length;

        protected  final int mLayout;

        public DemoAdapter(int layout)
        {
            mLayout = layout;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
            return new DemoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            DemoViewHolder demoViewHolder = (DemoViewHolder) holder;
            demoViewHolder.mTextView.setText(String.valueOf(position));

        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }

        protected class DemoViewHolder extends RecyclerView.ViewHolder
        {
            private TextView mTextView;

            public DemoViewHolder(View itemView)
            {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }

    private class StaggeredDemoAdapter extends DemoAdapter
    {

        public StaggeredDemoAdapter(int layout) {
            super(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DemoViewHolder demoViewHolder = (DemoViewHolder) holder;

            if(demoViewHolder.mTextView != null)
            {
                demoViewHolder.mTextView.setText(String.valueOf(mTagList[position]));
            }

        }
    }
}
