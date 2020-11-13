package aclass.com.example.garbgeapp.Untils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUntils {//封装的网络访问的代码
    public static void  OkHttpPost(String url, String json, Callback callback){//这个是post方法访问数据，这里没有使用，不需要管
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),json);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(callback);
    }
    public static void OkHttpGet(String url, Callback callback){//这里APP访问采用的是get获取数据的方法，向网络发送请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
