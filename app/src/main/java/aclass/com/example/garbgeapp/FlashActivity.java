package aclass.com.example.garbgeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


public class FlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);//绑定布局文件
        getSupportActionBar().hide();//隐藏标题栏
        setStatusBar();//主函数调用下面的setStatusBar();函数，使手机上的状态栏的颜色跟页面背景色相同，
        // 并且隐藏状态栏，达到全屏显示的效果

        new Thread(){//新建一个线程来延时展示界面，这里不需要用handler来传递信息，只需要在延时结束后跳转页面
            @Override
            public void run() {
                Intent intent=new Intent();//为跳转页面做准备intent方法
                super.run();
                    try {
                        Thread.sleep(1000);//延时1000即当前页面延时1秒，2000即2秒,这里必须有一个异常处理，直接alt+enter可解决
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 intent.setClass(FlashActivity.this,MainActivity.class);
                    startActivity(intent);//跳转到主页面
                    finish();//因为这是进入主页面前的等待界面，所以必须要finish这个页面，之后就不会再返回这个页面了
            }
        }.start();
    }




    private void setStatusBar() {//一体化状态栏，让状态栏的颜色跟背景色匹配

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
