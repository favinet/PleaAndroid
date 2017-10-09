package shop.plea.and.ui.activity;

import butterknife.ButterKnife;
import shop.plea.and.common.activity.BaseActivity;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class PleaActivity extends BaseActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

}
