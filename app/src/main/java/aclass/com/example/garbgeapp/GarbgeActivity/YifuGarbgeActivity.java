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


public class YifuGarbgeActivity extends AppCompatActivity {
    private ImageButton btnExit;
    private TextView textView1,textView2;
    private GridView gridView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> data;
    private String[] WetGarbage = new String[] {
            "食材废料", "剩饭剩菜", "过期食品", "蔬菜水果", "瓜皮果核",
            "花卉绿植", "中药残渣"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yifu_garbge);
        setStatusBar();
        getSupportActionBar().hide();
        btnExit=findViewById(R.id.btn_qita_exit);
        textView1=findViewById(R.id.tv_yifu_1);
        textView2=findViewById(R.id.tv_yifu_2);
        gridView=findViewById(R.id.gv_yifu);
        data=new ArrayList<>();

        for(int i=0;i<WetGarbage.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("text",WetGarbage[i]);
            data.add(map);

        }

        adapter=new SimpleAdapter(YifuGarbgeActivity.this,data,R.layout.activity_adapter,
                new String[]{"text"},
                new int[]{R.id.detail_name});//上下文,数据源，布局
        gridView.setAdapter(adapter);
        textView1.setText("容易腐烂的失误类垃圾及果皮等，投入绿色垃圾桶，可生态掩埋处理，用于沼气发电");
        textView2.setText("纯流质的失误垃圾，如牛奶等，应直接倒进下水口"
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
