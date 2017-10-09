package shop.plea.and.ui.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;

import java.util.ArrayList;

import shop.plea.and.R;

/**
 * Created by kwon7575 on 2017-10-09.
 */

public class TransFormerActivity extends PleaActivity implements ActionBar.OnNavigationListener{

    private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
    private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();
        TRANSFORM_CLASSES.add(new TransformerItem(DefaultTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(AccordionTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(BackgroundToForegroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(DepthPageTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipHorizontalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipVerticalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateDownTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateUpTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ScaleInOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(StackTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(TabletTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutSlideTransformer.class));
    }

    private int mSelectedItem;
    private ViewPager mPager;
    private PagerAdapter mAdapter;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int selectedPage = 0;
        if(savedInstanceState != null)
        {
            mSelectedItem = savedInstanceState.getInt(KEY_SELECTED_CLASS);
            selectedPage = savedInstanceState.getInt(KEY_SELECTED_PAGE);
        }

        final ArrayAdapter<TransformerItem> actionBarAdapter = new ArrayAdapter<TransformerItem>(getApplicationContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, TRANSFORM_CLASSES);

        setContentView(R.layout.activity_transformer);

        mAdapter = new PageAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(selectedPage);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setListNavigationCallbacks(actionBarAdapter, this);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            /**
             * ^ 연산자 비트 단위 Exclusive OR
             c = a ^ b;
             a 와 b를 비트단위 XOR 연산 후 c에 대입
             출처: http://jhrun.tistory.com/133 [JHRunning]
             */
            actionBar.setDisplayOptions(actionBar.getDisplayOptions() ^ ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setSelectedNavigationItem(mSelectedItem);
        }

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mSelectedItem = itemPosition;
        try
        {
            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(itemPosition).clazz.newInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(KEY_SELECTED_CLASS, mSelectedItem);
        outState.putInt(KEY_SELECTED_PAGE, mPager.getCurrentItem());
    }

    public static class PlaceHolderFragment extends Fragment
    {
        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        private static final int[] IMGS = new int[] {R.drawable.main_01, R.drawable.follower_list_,
        R.drawable.follower_list_03comment, R.drawable.follower_list_view, R.drawable.follower_list_05more, R.drawable.myplea_list,
        R.drawable.newpass, R.drawable.recommend_list, R.drawable.resetpass_, R.drawable.signup_};

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            final int position = getArguments().getInt(EXTRA_POSITION);
            final ScrollView scroll = (ScrollView) inflater.inflate(R.layout.item_transformer, container, false);

            final ImageView image = (ImageView) scroll.findViewById(R.id.image);
            image.setImageResource(IMGS[position-1]);
            //테스트 master
            return scroll;
        }
    }

    private static final class PageAdapter extends FragmentStatePagerAdapter
    {

        public PageAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceHolderFragment.EXTRA_POSITION, position+1);

            final PlaceHolderFragment placeHolderFragment = new PlaceHolderFragment();
            placeHolderFragment.setArguments(bundle);
            return placeHolderFragment;
        }

        @Override
        public int getCount() {
            return PlaceHolderFragment.IMGS.length;
        }
    }


    private static final class TransformerItem {
        final String title;
        final Class<? extends ViewPager.PageTransformer> clazz;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz){
            this.clazz = clazz;
            this.title = clazz.getSimpleName();
        }

        @Override
        public String toString() {
            return this.title;
        }
    }
}
