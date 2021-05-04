package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;

import org.turings.investigationapplicqation.Entity.ShowData;

import java.util.List;

//其他题型详看
public class SeeFillInTheBlanksActtivity extends AppCompatActivity {

    private SmartTable table;
    private ImageView back;//返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_fill_in_the_blanks_acttivity);
        Intent intent = getIntent();
        List<ShowData> list = (List<ShowData>) intent.getSerializableExtra("data");
        table = findViewById(R.id.table);
        table.getConfig().setContentBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>(){
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if(cellInfo.position%2 ==0){
                    return ContextCompat.getColor(getBaseContext(),R.color.colorMain);
                }else {
                    return ContextCompat.getColor(getBaseContext(),R.color.colorWhite);
                }
            }

        });
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setMinTableWidth(500);
        table.getConfig().setShowTableTitle(false);
        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith);
        table.setData(list);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
