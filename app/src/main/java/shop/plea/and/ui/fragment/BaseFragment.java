package shop.plea.and.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import shop.plea.and.R;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.common.view.ProgressWheel;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;
import shop.plea.and.ui.listener.UpdateListener;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class BaseFragment extends Fragment {

    protected UpdateListener mUpdateListenerCallBack;
    protected View mView;
    protected Context mContext;
    protected int mCurrentPosition;
    protected String mUrl;
    protected List<Fragment> mFragments = new ArrayList<>();
    protected Unbinder mUnbinder;
    protected Dialog materiaDg;
    protected ImageView mProgressWheel;
    protected boolean mChecked = false;


    public void init(ViewGroup viewGroup)
    {
        if(viewGroup == null) mContext = getActivity();
        else mContext = viewGroup.getContext();

        mUpdateListenerCallBack = (UpdateListener) mContext;
        mUnbinder = ButterKnife.bind(this, mView);

        materiaDg = new Dialog(mContext, R.style.Theme_CustomProgressDialog);
        materiaDg.setContentView(R.layout.progress_dialog_material);
        mProgressWheel = (ImageView) materiaDg.findViewById(R.id.progress_wheel);

      //  mToolbar = (Toolbar)
    }

    public void setCurrentPosition(int position)
    {
        Bundle args = new Bundle();
        args.putInt("position", position);
        setArguments(args);
        mCurrentPosition = position;
    }

    public void setCurrentImageUrl(String url)
    {
        this.mUrl = url;
    }


    public void showKeyboard(View view, boolean isShow)
    {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(isShow) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        else imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void addFragment(int resId, Fragment fragment)
    {
        mFragments.add(fragment);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//		ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        ft.replace(resId, fragment);

        ft.commitAllowingStateLoss();
    }

    public void replaceFragment(int resId, Fragment fragment)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//		ft.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        ft.add(resId, fragment);
        ft.addToBackStack(null);

        ft.commitAllowingStateLoss();
    }

    public Fragment getFragment(int position)
    {
        try {
            return mFragments.get(position);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public void backFragment()
    {
        FragmentManager fm = getChildFragmentManager();

        int count = fm.getBackStackEntryCount();

        if (count > 0)
        {
            fm.popBackStack();
        }
    }

    public void startIndicator(final String msg) {
        if(getActivity() != null && !getActivity().isFinishing() && materiaDg != null && !materiaDg.isShowing() && isAdded())
        {
            if (msg.length() > 0) {
                materiaDg.setCancelable(false);
            } else {
                materiaDg.setCancelable(true);
            }
            //mProgressWheel.spin();
            materiaDg.show();
        }
    }

    public void stopIndicator() {
        if(getActivity() != null && !getActivity().isFinishing() && materiaDg != null && materiaDg.isShowing())
        {
            //mProgressWheel.stopSpinning();
            materiaDg.dismiss();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null ) mUnbinder.unbind();

    }
}
