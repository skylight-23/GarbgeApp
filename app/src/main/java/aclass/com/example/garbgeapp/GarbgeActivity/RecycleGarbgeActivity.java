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

public class RecycleGarbgeActivity extends AppCompatActivity {
    private ImageButton btnExit;
    private TextView textView1,textView2;
    private GridView gridView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;
    public String[] RecyclableGarbage = new String[] {
            "报纸", "纸箱", "书本", "广告单", "塑料瓶",
            "塑料玩具", "油桶", "酒瓶", "玻璃杯", "易拉罐",
            "旧铁锅", "旧衣服", "包",  "旧玩偶", "旧数码产品",
            "旧家电"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_garbge);
        getSupportActionBar().hide();
        setStatusBar();
        btnExit=findViewById(R.id.btn_recycle_exit);
        textView1=findViewById(R.id.tv_recycle_1);
        textView2=findViewById(R.id.tv_recycle_2);
        gridView=findViewById(R.id.gv_recycle);
        data=new ArrayList<>();

        for(int i=0;i<RecyclableGarbage.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("text",RecyclableGarbage[i]);
            data.add(map);

        }

        adapter=new SimpleAdapter(RecycleGarbgeActivity.this,data,R.layout.activity_adapter,
                new String[]{"text"},
                new int[]{R.id.detail_name});//上下文,数据源，布局
        gridView.setAdapter(adapter);
        textView1.setText("再生利用价值较高，能够入废品回收渠道的垃圾，可投入蓝色垃圾桶");
        textView2.setText("轻投轻放，清洁干燥、避免污染，废纸尽量平整"
        );
        btnExit.setOnClickListener(new View.OnClickListener() {
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
