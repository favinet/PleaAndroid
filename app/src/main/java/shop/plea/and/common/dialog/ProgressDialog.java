package shop.plea.and.common.dialog;

import android.app.Dialog;
import android.content.Context;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.view.ProgressWheel;

/**
 * Created by master on 2017-10-09.
 */

public class ProgressDialog extends Dialog {

    private ProgressWheel progressWheel;

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setContentView(final int layoutResID) {
        // Your base layout here
        //fullLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.act_layout, null);
        //actContent= (FrameLayout) fullLayout.findViewById(R.id.act_content);

        // Setting the content of layout your provided to the act_content frame
        //getLayoutInflater().inflate(layoutResID, actContent, true);

        super.setContentView(layoutResID);

        progressWheel = (ProgressWheel) this.findViewById(R.id.progress_wheel);

        Logger.log(Logger.LogState.D, "progressWheel setContentView: " + progressWheel);
        // here you can get your drawer buttons and define how they
        // should behave and what must they do, so you won't be
        // needing to repeat it in every activity class
    }

    @Override
    public void show() {
        progressWheel.spin();
        super.show();
    }

    @Override
    public void dismiss() {
        progressWheel.stopSpinning();
        super.dismiss();
    }
}
