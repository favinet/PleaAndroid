package shop.plea.and.ui.listener;

import android.support.v4.app.Fragment;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public interface UpdateListener {

    void addFragment(int menuId);

    void fragmentBackPressed();

    void addFragment(Fragment fragment);

    void notifyDataSetChanged();

}
