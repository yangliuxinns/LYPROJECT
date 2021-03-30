package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import org.turings.investigationapplicqation.DialogAdapter.CustomSearchResultListViewAdapter;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomListView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private ListView listView;
    private CustomSearchResultListViewAdapter customSearchResultListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn2 = new Questionnaire(1,"调查问卷2","问卷说明1lalaalalalalalala",true,null,0,false,false,false,"");
        Questionnaire qn3 = new Questionnaire(1,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn4 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",true,null,0,false,false,false,"");
        Questionnaire qn5 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn6 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn7 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn8 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn9 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        Questionnaire qn10 = new Questionnaire(1,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,false,false,false,"");
        List<Questionnaire> list = new ArrayList<>();
        list.add(qn1);
        list.add(qn2);
        list.add(qn3);
        list.add(qn4);
        list.add(qn5);
        list.add(qn6);
        list.add(qn7);
        list.add(qn8);
        list.add(qn9);
        list.add(qn10);

        listView = findViewById(R.id.list);
        customSearchResultListViewAdapter = new CustomSearchResultListViewAdapter(list,getApplicationContext(),R.layout.search_result_questionare_item_layout);

        listView.setAdapter(customSearchResultListViewAdapter);
    }
}
