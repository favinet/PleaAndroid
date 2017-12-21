package shop.plea.and.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.view.ProgressWheel;

/**
 * Created by master on 2017-10-09.
 */

public class ProgressDialog extends Dialog {

    private ImageView progressWheel;
    private AnimationDrawable frameAnimation;

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

        progressWheel = (ImageView) this.findViewById(R.id.progress_wheel);
        frameAnimation = (AnimationDrawable) progressWheel.getBackground();

        Logger.log(Logger.LogState.D, "progressWheel setContentView: " + progressWheel);
        // here you can get your drawer buttons and define how they
        // should behave and what must they do, so you won't be
        // needing to repeat it in every activity class
    }

    @Override
    public void show() {
        frameAnimation.start();
        super.show();
    }

    @Override
    public void dismiss() {
        frameAnimation.stop();
        super.dismiss();
    }
}
