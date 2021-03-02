package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.turings.investigationapplicqation.Entity.Questionnaire;

import java.util.Date;

/**
 * 修改问卷标题和简介
 */
public class EditTitleAndDescription extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;//返回/关闭
    private EditText etTitle;//问卷名称
    private EditText etInstruction;//问卷说明
    private Button btnAdd;//保存修改
    private Questionnaire questionnaire;
    private static final int TTRESULT=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_title_and_description);
        getViews();
        getRegister();
        init();
    }

    //初始化
    private void init() {
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("qn");
        etTitle.setText(questionnaire.getTitle());
        etInstruction.setText(questionnaire.getInstructions());
    }

    //注册控件
    private void getRegister() {
        back.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    //获取控件
    private void getViews() {
        back = findViewById(R.id.back1);
        etTitle = findViewById(R.id.et_title);
        etInstruction = findViewById(R.id.et_content);
        btnAdd = findViewById(R.id.btn_add);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back1:
                Intent intent1 = new Intent();
                //把返回数据存入Intent
                intent1.putExtra("q_data",questionnaire);
                //设置返回数据
                setResult(TTRESULT, intent1);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case R.id.btn_add:
                //判断问卷标题是否编辑
                if (etTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(this,"请输入标题", Toast.LENGTH_LONG).show();
                }else {
                    Questionnaire questionnaire1;
                    if (etInstruction.getText().toString().trim().isEmpty() || null == etInstruction.getText().toString().trim() || " ".equals(etInstruction.getText().toString().trim())) {
                        questionnaire.setTitle(etTitle.getText().toString().trim());
                        questionnaire.setInstructions(getString(R.string.welcome));
                    } else {
                        questionnaire.setTitle(etTitle.getText().toString().trim());
                        questionnaire.setInstructions(etInstruction.getText().toString().trim());
                    }
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("q_data",questionnaire);
                    //设置返回数据
                    setResult(TTRESULT, intent);//RESULT_OK为自定义常量
                    //关闭Activity
                    finish();
                }
                break;
        }
    }
}
