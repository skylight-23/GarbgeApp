package aclass.com.example.garbgeapp.GarbgeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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


public class QitaGarbgeActivity extends AppCompatActivity {
    private ImageButton btnExit;
    private TextView textView1,textView2;
    private GridView gridView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;
    private String[] DryGarbage = new String[] {
            "餐盒", "餐巾纸", "湿纸巾", "卫生间用纸", "塑料袋",
            "食品包装袋", "污染严重的纸", "烟蒂", "纸尿裤", "一次性杯子",
            "大骨头", "贝壳", "花盆"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qita_garbge);
        getSupportActionBar().hide();
        setStatusBar();
        btnExit=findViewById(R.id.btn_qita_exit);
        textView1=findViewById(R.id.tv_qita_1);
        textView2=findViewById(R.id.tv_qita_2);
        gridView=findViewById(R.id.gv_qita);
        data=new ArrayList<>();

        for(int i=0;i<DryGarbage.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("text",DryGarbage[i]);
            data.add(map);

        }

        adapter=new SimpleAdapter(QitaGarbgeActivity.this,data,R.layout.activity_adapter,
                new String[]{"text"},
                new int[]{R.id.detail_name});//上下文,数据源，布局
        gridView.setAdapter(adapter);
        textView1.setText("即干垃圾，指除可回收物、有害垃圾、湿垃圾、以外的其他生活废弃物");
        textView2.setText("尽量沥干水分,难以辨识类别的生活垃圾投入其他垃圾容器内"
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
