package shop.plea.and.ui.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by kwon7575 on 2017-11-08.
 */

public class TextClickSpan extends ClickableSpan{

    String clicked;
    public TextClickSpan(String string)
    {
        super();
        clicked = string;
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }
}
