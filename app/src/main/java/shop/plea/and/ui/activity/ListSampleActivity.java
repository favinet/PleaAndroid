package shop.plea.and.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mightyfrog.widget.CenteringRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.ui.adapter.SimpleItemAdapter;
import shop.plea.and.ui.adapter.multiAdapter.MultiItemAdapter;

/**
 * Created by kwon7575 on 2017-10-12.
 */

public class ListSampleActivity extends PleaActivity{

    @BindView(R.id.recycler_view) CenteringRecyclerView mCenteringRecyclerView;
    private SimpleItemAdapter mTagListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sample);


        String[] TAGLIST= new String[] {"ALL 99","#ASOS 92", "#wish 9", "#Ruffle 32", "#DRESSES 30", "#summer 27", "#Ivory 21", "#like 12",
                "#Ruffle 32", "#red 3", "#summer 20", "#ASOS 91"};

        List<MultiItemAdapter.Row<?>> tagRows = new ArrayList<>();

        for(String tag : TAGLIST)
        {
            tagRows.add(MultiItemAdapter.Row.create(tag, SimpleItemAdapter.VIEW_TYPE_LIST));
        }

        mTagListAdapter = new SimpleItemAdapter();
        mTagListAdapter.setClickCallback(new SimpleItemAdapter.onClickCallback(){

            @Override
            public void onClick(View view) {
                int postion = mCenteringRecyclerView.getChildLayoutPosition(view);
                TextView textView = (TextView) view;
                textView.setBackgroundResource(R.drawable.tag_round_on_txt);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                String tag = mTagListAdapter.getItem(postion);
                Toast.makeText(ListSampleActivity.this, tag, Toast.LENGTH_LONG).show();
            }
        });

        mTagListAdapter.clear();
        mTagListAdapter.setRows(tagRows);
        mCenteringRecyclerView.setAdapter(mTagListAdapter);
        mCenteringRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));


    }


}
