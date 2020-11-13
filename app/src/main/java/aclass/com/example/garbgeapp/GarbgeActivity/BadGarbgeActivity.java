package aclass.com.example.garbgeapp.GarbgeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aclass.com.example.garbgeapp.R;
//这个文件夹里面的四个代码基本上是一样的，所以只注释了第一个代码
public class BadGarbgeActivity extends AppCompatActivity {
    private ImageButton btnExit;
    private TextView textView1,textView2;
    private GridView gridView;
    private SimpleAdapter adapter;//因为现实的内容比较多，需要用到适配器，这里声明一个适配器
    private List<Map<String,Object>> data;
    public String[] HazardousWaste = new String[] {
            "废电池", "废油漆", "消毒剂", "荧光灯管", "含汞温度计",
            "废药品及其包装物"
    };//先声明内容数组，就不用一个一个去添加了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_garbge);
        getSupportActionBar().hide();
        setStatusBar();
        btnExit=findViewById(R.id.btn_youhai_exit);
        textView1=findViewById(R.id.tv_youhai_1);
        textView2=findViewById(R.id.tv_youhai_2);
        gridView=findViewById(R.id.gv_youhai);//实例化各种控件
        data=new ArrayList<>();//写一个循环来向适配器添加内容

        for(int i=0;i<HazardousWaste.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("text",HazardousWaste[i]);//添加的内容用数组下标做索引
            data.add(map);

        }

        adapter=new SimpleAdapter(BadGarbgeActivity.this,data,R.layout.activity_adapter,//绑定适配器，这里的适配器是一个布局文件
                new String[]{"text"},
                new int[]{R.id.detail_name});//上下文,数据源，布局
        gridView.setAdapter(adapter);
        textView1.setText("含有毒有害化学物质的垃圾，应投入红色垃圾桶，分选后回收利用或安全填埋");
        textView2.setText("投放时请注意轻放，已破损的请连带包装或包裹后轻放"
        );//设置两个文本框的显示内容
        btnExit.setOnClickListener(new View.OnClickListener() {//右上角有一个返回按钮，点击返回按钮可结束当前页面，返回上一层页面
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setStatusBar() {//一体化状态栏，让状态栏的颜色跟背景色匹配

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
