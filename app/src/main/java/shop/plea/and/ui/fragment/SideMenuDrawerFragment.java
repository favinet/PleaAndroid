package shop.plea.and.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.view.ProgressWheel;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.listener.UpdateListener;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-28.
 */

public class SideMenuDrawerFragment extends BaseFragment implements FragmentListener {

    private DrawerLayoutHorizontalSupport drawerLayout;
    private BaseActivity base;

    public static SideMenuDrawerFragment newInstance() {

        SideMenuDrawerFragment sideMenuDrawerFragment = new SideMenuDrawerFragment();
        Bundle bundle = new Bundle();
        sideMenuDrawerFragment.setArguments(bundle);

        return sideMenuDrawerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_sidemenu_drawer, container, false);

        base = (BaseActivity) container.getContext();

        init(container);

        return mView;
    }

    public void setDrawerLayout(DrawerLayoutHorizontalSupport drawerLayout)
    {
        this.drawerLayout = drawerLayout;
        this.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
