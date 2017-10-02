package shop.plea.and.data.tool;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.service.HttpService;

/**
 * Created by master on 2017-10-02.
 */

public class BaseDataInterface {
    public HttpService service;

    public BaseDataInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HttpService.class);
    }
}
