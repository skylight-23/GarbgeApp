package aclass.com.example.garbgeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import aclass.com.example.garbgeapp.GarbgeActivity.BadGarbgeActivity;
import aclass.com.example.garbgeapp.GarbgeActivity.QitaGarbgeActivity;
import aclass.com.example.garbgeapp.GarbgeActivity.RecycleGarbgeActivity;
import aclass.com.example.garbgeapp.GarbgeActivity.YifuGarbgeActivity;
//okhttp3的东西
import aclass.com.example.garbgeapp.Untils.OkhttpUntils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/*各种资源文件百度搜索的，放在了res/raw文件夹下面，APP用到的图片资源放在了res/mipmap文件夹下，还有的再res/drawable文件夹下
APP是网络访问API来检索数据，需要导入okhttp3的包才能正确运行网络访问的内容
导入这个包的方式为在Gradle.Scripts下的build.grade(app)里面的第34行代码
*/

public class MainActivity extends AppCompatActivity {
private EditText edtGarbge;
private ImageButton btnSearch,btnQitaGarbge,btnYifuGarbge,btnRecycleGarbge,btnBadGarbge,btnExit;
private TextView garbgeResult;
private ProgressDialog progressDialog;
private ImageView imageSearch,imageIcon,btnspeech;//44-47行声明主页面中使用的控件
    public static List<Activity> activityList = new LinkedList();//主页面有一个退出按钮，这里就是为这个退出按钮声明
    private SoundPool soundPool;//声明一个SoundPool
    private int soundID;//创建某个声音对应的音频ID
    private final int REQ_CODE_SPEECH_INPUT=222;

    String url = "https://api.66mz8.com/api/garbage.php?name=";//APP中搜索数据采用的是访问网络API的方式查找数据，这里的url是api的地址，必须先声明
    Handler handler = new Handler(new Handler.Callback() {//网络访问数据通过handler来传递检索到的数据,这里代码顺序应先看141-182行
        @Override
        public boolean handleMessage(@NonNull Message msg) {//给传递的信息设置了标签,用switc来对应不同标签的结果
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                    break;
                case 100:
                    progressDialog=ProgressDialog.show(MainActivity.this,//显示等待的转圈效果
                            null,
                            "正在拼命加载...",false,true);
                    break;
                case 1:
                    progressDialog.dismiss();//收到信息哦取消等待效果
                    String json = (String) msg.obj;//定义一个json来接收传递的数据
                    String name=edtGarbge.getText().toString();//为了让现实更完善，这里再次获取输入框里面的内容
                    try {
                        garbgeResult.setVisibility(View.VISIBLE);
                        imageSearch.setVisibility(View.VISIBLE);//这两个按钮实例化的时候都设置为不可见，以便界面更美观，当收到数据以后就设置为可见
                        garbgeResult.setText(name+"查询不到该结果！");
                        JSONObject js = new JSONObject(json);//网络请求返回的是json数据,这里对json数据进行解析
                        String n = js.getString("data");
                        garbgeResult.setText(name+":"+n);//设置结果框现实内容为搜索框的内容加上检索到的数据
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            return false;
        }
    });
    protected void onCreate(Bundle savedInstanceState) {//实例化主页面中所有的控件
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setStatusBar();
        edtGarbge=findViewById(R.id.edt_garbge);
        btnSearch=findViewById(R.id.btn_search);
        btnQitaGarbge=findViewById(R.id.btn_qitaGarbge);
        btnYifuGarbge=findViewById(R.id.brn_yifuGarbge);
        btnRecycleGarbge=findViewById(R.id.btn_recycleGarbge);
        btnBadGarbge=findViewById(R.id.btn_badGarbge);
        imageSearch=findViewById(R.id.img_search);
        imageSearch.setVisibility(View.INVISIBLE);
        btnSearch.setOnClickListener(new MyGarbge());
        btnQitaGarbge.setOnClickListener(new MyGarbge());
        btnYifuGarbge.setOnClickListener(new MyGarbge());
        btnRecycleGarbge.setOnClickListener(new MyGarbge());
        btnBadGarbge.setOnClickListener(new MyGarbge());
        btnspeech=findViewById(R.id.btn_speech);
        btnspeech.setOnClickListener(new View.OnClickListener() {//语音按钮交互事件
            @Override
            public void onClick(View v) {
                promptSpeechInput();//调用下面写的函数
            }
        });
        imageIcon=findViewById(R.id.imageIcon);
        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent=new Intent();
              intent.setClass(MainActivity.this,AboutActivity.class);
              startActivity(intent);
            }
        });
        btnExit=findViewById(R.id.imgBtnExit);
        activityList.add(this);//直接退出程序按钮要用的，如果在别的界面使用这个功能，就需要加上这一句
        btnExit.setOnClickListener(new View.OnClickListener() {//主页面点击退出按钮的监听事件
            @Override
            public void onClick(View v) {
                commonDialog();//直接调用198-221行的对话框函数
            }
        });
        initSound();//调用声明音乐的方法

        initUi();//调用网络访问初始化的方法，注意网络访问必须在manifests文件中提供网络访问的权限，具体代码见AndroidManifest里面
    }

    private class MyGarbge implements View.OnClickListener {//因为按钮比较多，这里就直接用监听事件的方法来写,
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            switch (v.getId()) {//获取ID
                case R.id.btn_qitaGarbge://对应的id做对应的事情
                    playSound();//调用播放音乐的函数，当点击对应按钮的时候就会有音乐效果,如果想让别的按钮有这个效果，就调用这个方法,下面几个按钮方法一样
                    intent.setClass(MainActivity.this, QitaGarbgeActivity.class);
                    startActivity(intent);//页面跳转
                    break;
                case R.id.brn_yifuGarbge:
                    playSound();
                    intent.setClass(MainActivity.this, YifuGarbgeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_recycleGarbge:
                    playSound();
                    intent.setClass(MainActivity.this, RecycleGarbgeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_badGarbge:
                    playSound();
                    intent.setClass(MainActivity.this, BadGarbgeActivity.class);
                    startActivity(intent);
                    break;


            }
        }
    }

    private void initUi() {//初始化网络
        btnSearch=findViewById(R.id.btn_search);
        garbgeResult=findViewById(R.id.garbgeResule);
    garbgeResult.setVisibility(View.INVISIBLE);//设置显示结果的文本框不可见
        btnSearch.setOnClickListener(new View.OnClickListener() {//监听搜索按钮
            @Override
            public void onClick(View v) {
                View view = getWindow().peekDecorView();
                if (view != null) {//这里是网络访问的代码，获取服务
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                initData();//调用下面的方法

            }

            private void initData() {

                 String data = edtGarbge.getText().toString();//获取搜索框里面的内容
                handler.sendEmptyMessage(100);//一开始发送一条标签为100的空消息，用来实现等待的效果
                 if(TextUtils.isEmpty(data)){//如果内容为空，弹出toast消息，不为空则执行164行以后的代码
                     Toast.makeText(MainActivity.this,"垃圾名称不能为空！",Toast.LENGTH_SHORT).show();
                 }

                OkhttpUntils.OkHttpGet(url + data, new Callback() {//进行网络访问，检索，url+data表示带上data数据去检索url这个网址里面的内容
                    @Override
                    public void onFailure(Call call, IOException e) {//没有内容就什么都不执行

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String n = response.body().string();//定义一个n来接收返回的数据
                        Message message = new Message();
                        message.what = 1;//相当于标签等于1
                        message.obj = n;
                        handler.sendMessageDelayed(message,1000);//延时发送一message信息
                    }
                });
            }
        });
    }

    @SuppressLint("NewApi")
    private void initSound() {//音乐初始化方法
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this, R.raw.click_music, 1);//加载对应的因为文件
    }
    private void playSound() {
        soundPool.play(
                soundID,
                0.1f,   //左耳道音量【0~1】
                0.5f,   //右耳道音量【0~1】
                0,     //播放优先级【0表示最低优先级】
                1,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1     //播放速度【1是正常，范围从0~2】
        );
    }

    private void commonDialog() {//对话框方法
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setIcon(android.R.drawable.star_on);//设置图标
        alert.setTitle("退出？");//设置标题
        alert.setMessage("确定要退出程序吗？");//设置显示信息
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit();//点击确定按钮调用退出方法

            }
        });
        //添加"确定"按钮
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {//点击取消按钮什么事也不用做，返回当前页面即可

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alert.show();

    }
    private void setStatusBar() {//一体化状态栏，让状态栏的颜色跟背景色匹配

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public void exit()//退出方法，可在任何页面直接结束整个程序
    {

        for(Activity act:activityList)
        {
            act.finish();
        }

        System.exit(0);

    }
    private void promptSpeechInput(){//语音识别功能的实现，只支持有谷歌语音服务的手机，否则弹出toast消息，不支持语音输入
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//开启语音识别调用
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//语音识别的模式，在一种模式上的自由语音
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));//设置用户可以开始语音的提示语
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);}
        catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK&&null!=data){//回结果是一个list，我们一般取的是第一个最匹配的结果
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   edtGarbge.setText(result.get(0));//设置搜索框的内容为语音识别的结果
                }
                break;
            }
        }
    }
}
