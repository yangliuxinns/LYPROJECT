package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.turings.investigationapplicqation.DialogAdapter.CustomGridAdapter;
import org.turings.investigationapplicqation.DialogAdapter.PicGridAdapter;
import org.turings.investigationapplicqation.Entity.Photo;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomGridView;

import java.util.ArrayList;
import java.util.List;

//外观设置
public class AppearanceSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomGridView customGridView;

    private PicGridAdapter picGridAdapter;
    private List<Photo> ps = new ArrayList<>();
    private TextView cancel;//取消
    private TextView ok;//使用
    private String postion;//选中了第几个
    private Questionnaire questionnaire;//问卷
    private Questionnaire questionnaire2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance_settings);
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("q_data");
        questionnaire2 = (Questionnaire) getIntent().getSerializableExtra("q_data");
        Photo photo = new Photo("第一张","abg1",1);
        Photo photo1 = new Photo("第2张","abg2",-1);
        Photo photo2 = new Photo("第3张","abg3",-1);
        Photo photo3 = new Photo("第4张","abg4",-1);
        Photo photo4 = new Photo("第5张","abg5",-1);
        Photo photo5 = new Photo("第6张","abg6",-1);
        Photo photo6= new Photo("第7张","abg7",-1);
        Photo photo7= new Photo("第8张","abg8",-1);
        Photo photo8= new Photo("第9张","abg9",-1);
        Photo photo9= new Photo("第10张","abg1",-1);
        Photo photo10= new Photo("第11张","abg2",-1);
        Photo photo11= new Photo("第12张","abg3",-1);
        Photo photo12= new Photo("第13张","abg4",-1);
        List<Photo> l = new ArrayList<>();
        l.add(photo);l.add(photo1);l.add(photo2);l.add(photo3);l.add(photo4);l.add(photo5);l.add(photo6);l.add(photo7);
        l.add(photo8);
        l.add(photo9);
        l.add(photo10);l.add(photo11);
        ps.addAll(l);
        getViews();
        picGridAdapter = new PicGridAdapter(l,getApplicationContext(),R.layout.ape_grid_item);
        customGridView.setAdapter(picGridAdapter);
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0;i<l.size();i++){
                    if(i == position){
                        l.get(i).setPos(1);
                    }else {
                        l.get(i).setPos(-1);
                    }
                }
                postion = position+"";
                ps.clear();
                ps.addAll(l);
                picGridAdapter.notifyDataSetChanged();
            }
        });
        picGridAdapter.setmOnItemClickListener(new PicGridAdapter.onItemClickListener() {
            @Override
            public void onNumClick(int position) {
                Toast.makeText(getApplicationContext(),"选中了第"+position,Toast.LENGTH_SHORT).show();
                postion = position+"";

                //调用预览
            }
        });

    }

    private void getViews() {
        customGridView = findViewById(R.id.grid_option);
        cancel = findViewById(R.id.cancel1);
        ok = findViewById(R.id.btn_ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel1://不设定返回
                Intent intent1 = new Intent();
                //把返回数据存入Intent
                intent1.putExtra("q_data",questionnaire2);
                //设置返回数据
                setResult(6, intent1);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case R.id.btn_ok:
                questionnaire.setAppearance(postion);
                Log.i("rrr", "onClick: 外观"+questionnaire.getAppearance());
                //直接设定返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("q_data",questionnaire);
                //设置返回数据
                setResult(6, intent);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
        }
    }
}
