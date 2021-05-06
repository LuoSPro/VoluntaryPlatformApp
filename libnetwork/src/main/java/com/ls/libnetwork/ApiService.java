package com.ls.libnetwork;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 主入口：
 * 使用流程：
 * 1. 在ApiService中去处理网络请求的基本配置，如baseUrl、Convert等。并且向外暴露方法，让外界使用Request这个请求的封装类
 * 2. 在Request中做了请求头和请求体的封装。然后开始使用execute()方法去进行网络请求
 * 3. 在execute()方法中，先去构造了OkHttp的Call对象，并把之前的请求头和请求体放了进去，然后调用子类实现的generateRequest去
 *    生成对应的Get/Post()请求，这里就用到了Request的子类：GetRequest/PostRequest类
 * 4. 在使用GetRequest的时候，遵循单一职责的原则，创建了一个UrlCreator类去专门做URL的封装
 * 5. 请求完成后，根据请求的结果进行对应方法的回调，这里使用到了JsonCallback去对结果进行回调，并且把返回体封装成
 *    一个ApiResponse对象，然后在回调方法中传出去
 * 6. 在把结果封装成一个ApiResponse对象的过程中，需要根据返回的数据的类型，将数据转化成我们需要的对象，这里就涉及到
 *    Convert接口去定义转换的方式，以及具体的子类JsonConvert去真正转化数据。同时，Convert接口去定义转化方式，有利于
 *    数据转换方式的拓展
 */
public class ApiService {

    protected static String sBaseUrl;

    protected static okhttp3.OkHttpClient okHttpClient;
    protected static Convert sConvert;

    public static boolean isRestfulStyle;

    /**
     * 对okHttp的一些配置进行初始化
     */
    static {
        //网络请求的日志
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)//超时的时间配置
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)//添加日志打印
                .build();

        //Https接口请求证书信任的问题
        TrustManager[] trustManagers = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            //Http的握手的过程
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null,trustManagers,new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
        }
    }

    //初始化URL的域名，并传入Convert，以便Request里面用来转换返回的结果
    public static void init(String baseUrl,Convert convert){
        sBaseUrl = baseUrl;
        if (convert == null){
            convert = new JsonConvert();
        }
        sConvert = convert;
    }

    /**
     * 暴露接口，方便外部直接使用GET请求
     */
    public static <T> GetRequest<T> get(String url){
        return new GetRequest<>(sBaseUrl+url);
    }

    /**
     * 暴露接口，方便外部直接使用POST请求
     */
    public static <T> PostRequest<T> post(String url){
        return new PostRequest<>(sBaseUrl+url);
    }

}
