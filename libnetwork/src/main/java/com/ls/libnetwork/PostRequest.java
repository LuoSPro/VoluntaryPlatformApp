package com.ls.libnetwork;

import android.text.TextUtils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PostRequest<T> extends Request<T, PostRequest> {


    public PostRequest(String url) {
        super(url);
    }

    /**
     * Post的请求不像Get直接在url上携带参数，而是需要把携带的数据放在请求头里面传给后台
     * @param builder
     * @return
     */
    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        //把params里面的数据添加到requestBody里面
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            bodyBuilder.add(entry.getKey(),String.valueOf(entry.getValue()));
        }
        //Json数据的构造
        if (!TextUtils.isEmpty(mJsonBody)){
            //指定媒体类型
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonBody);
            builder.post(requestBody);
        }else{
            builder.post(bodyBuilder.build());
        }
        //构造请求
        okhttp3.Request request = builder.url(mUrl).build();
        return request;
    }

    @Override
    protected void addJsonBody(okhttp3.Request.Builder builder,String json) {

    }
}
