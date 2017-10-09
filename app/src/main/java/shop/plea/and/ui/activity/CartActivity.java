package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import shop.plea.and.R;
import butterknife.BindView;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.CartViewResponse;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

/**
 * Created by master on 2017-10-09.
 */

public class CartActivity extends PleaActivity {

    @BindView(R.id.textView1) TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String cartId = "59cb0c8fea06a117020a72f0";

        DataManager.getInstance(context).api.callCartView(context, cartId, new DataInterface.ResponseCallback<CartViewResponse>() {
            @Override
            public void onSuccess(CartViewResponse response) {
                if(response.getResult().equalsIgnoreCase(Constants.API_SUCCESS))
                {
                    Gson gson = new Gson();
                    String responseStr = gson.toJson(response);
                   textView1.setText(responseStr);
                }
                else
                {
                    textView1.setText("서버에러발생");
                }
            }

            @Override
            public void onError() {
                Toast.makeText(context, "네트워크가 불안정해 광고리스트를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
