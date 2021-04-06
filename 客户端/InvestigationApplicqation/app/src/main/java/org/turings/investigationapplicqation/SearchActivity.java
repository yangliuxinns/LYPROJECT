package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.turings.investigationapplicqation.Util.ICallBack;
import org.turings.investigationapplicqation.Util.SearchView;
import org.turings.investigationapplicqation.Util.bCallBack;
//搜索页面
public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                //跳转搜搜索页面
                Intent intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("search",string);
                startActivity(intent);
                finish();
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }
}
