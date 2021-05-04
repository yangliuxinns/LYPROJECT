package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.turings.investigationapplicqation.Entity.Questionnaire;

import java.util.Date;

/**
 * 创建空白问卷
 */
public class CreateBlankQuestionnaire extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;//返回/关闭
    private EditText etTitle;//问卷名称
    private EditText etInstruction;//问卷说明
    private Button btnAdd;//创建问卷
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blank_questionnaire);
        getViews();
        getRegister();
    }

    //注册控件
    private void getRegister() {
        back.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    //获取控件
    private void getViews() {
        SharedPreferences sharedPreferences= getSharedPreferences("userInfo",MODE_PRIVATE);
        uid= Integer.parseInt(sharedPreferences.getString("uId",""));
        back = findViewById(R.id.back);
        etTitle = findViewById(R.id.et_title);
        etInstruction = findViewById(R.id.et_content);
        btnAdd = findViewById(R.id.btn_add);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_add:
                //判断问卷标题是否编辑
                if (etTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(this,"请输入标题", Toast.LENGTH_LONG).show();
                }else if (null == etTitle.getText().toString().trim() || " ".equals(etTitle.getText().toString().trim())){
                    Toast.makeText(this,"标题不能为空", Toast.LENGTH_LONG).show();
                }else {
                    Questionnaire questionnaire;
                    if(etInstruction.getText().toString().trim().isEmpty() ||null == etInstruction.getText().toString().trim() || " ".equals(etInstruction.getText().toString().trim())){
                        questionnaire = new Questionnaire(0, etTitle.getText().toString().trim(), getString(R.string.welcome), false,null,0,false,false,false,null,uid,false);
                    }else {
                        questionnaire = new Questionnaire(0, etTitle.getText().toString().trim(), etInstruction.getText().toString().trim(), false,null,0,false,false,false,null,uid,false);
                    }
                    Intent intent = new Intent(this, EditQuestionnaire.class);
                    intent.putExtra("questionnaire_data", questionnaire);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
}
