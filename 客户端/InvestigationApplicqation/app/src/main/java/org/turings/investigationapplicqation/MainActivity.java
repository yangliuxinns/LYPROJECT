package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.turings.investigationapplicqation.Fragment.HomePagerFragment;
import org.turings.investigationapplicqation.Fragment.MySelfFragment;
import org.turings.investigationapplicqation.Fragment.WorkBenchFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, ImageView> imageViewMap = new HashMap<>();
    private Map<String, TextView> textViewMap = new HashMap<>();
    private FragmentTabHost fragmentTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Intent intent = getIntent();
        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //改变选中的选项卡颜色
                textViewMap.get(tabId).setTextColor(getResources().getColor(R.color.colorMain));//获取所有资源
                switch(tabId){
//                    case "tag1":
////                        imageViewMap.get("tag1").setImageResource(R.mipmap.tagb1);
//                        imageViewMap.get("tag2").setImageResource(R.mipmap.taga2);
//                        imageViewMap.get("tag3").setImageResource(R.mipmap.taga3);
//                        textViewMap.get("tag2").setTextColor(getResources().getColor(android.R.color.darker_gray));
//                        textViewMap.get("tag3").setTextColor(getResources().getColor(android.R.color.darker_gray));
//                        break;
                    case "tag2":
//                        imageViewMap.get("tag1").setImageResource(R.mipmap.taga1);
                        imageViewMap.get("tag2").setImageResource(R.mipmap.tagb2);
                        imageViewMap.get("tag3").setImageResource(R.mipmap.taga3);
//                        textViewMap.get("tag1").setTextColor(getResources().getColor(android.R.color.darker_gray));
                        textViewMap.get("tag3").setTextColor(getResources().getColor(android.R.color.darker_gray));
                        break;
                    case "tag3":
//                        imageViewMap.get("tag1").setImageResource(R.mipmap.taga1);
                        imageViewMap.get("tag2").setImageResource(R.mipmap.taga2);
                        imageViewMap.get("tag3").setImageResource(R.mipmap.tagb3);
//                        textViewMap.get("tag1").setTextColor(getResources().getColor(android.R.color.darker_gray));
                        textViewMap.get("tag2").setTextColor(getResources().getColor(android.R.color.darker_gray));
                        break;
                }
            }
        });
        //根据不同intent的action，返回指定的fragment
        if(intent != null){
            switch (intent.getAction()){
                case "work"://返回工作台
                    fragmentTabHost.setCurrentTab(1);
                    imageViewMap.get("tag2").setImageResource(R.mipmap.tagb2);
                    textViewMap.get("tag2").setTextColor(getResources().getColor(R.color.colorMain));
                    break;
                case "self":
                    fragmentTabHost.setCurrentTab(2);
                    imageViewMap.get("tag2").setImageResource(R.mipmap.taga2);
                    textViewMap.get("tag2").setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
            }
        }else{
            //设置默认选中哪一项
            fragmentTabHost.setCurrentTab(0);
            imageViewMap.get("tag1").setImageResource(R.mipmap.tagb1);
            textViewMap.get("tag1").setTextColor(getResources().getColor(R.color.colorMain));
        }
    }
    //初始化
    private void init() {
        //判断是否登录
//        if(!checkUserIsLogin()){//未登录
//            Intent intent = new Intent(getApplicationContext(),LoginAndRegisterActivity.class);
//            intent.setAction("work");
//            startActivity(intent);
//            finish();
//        }
        //获取FragmentTabHost对象
        fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//管理多个fragment
                android.R.id.tabcontent//真正显示页面的内容的容器id
        );
        //创建tabspec对象
//        TabHost.TabSpec tabSpec1 = fragmentTabHost.newTabSpec("tag1")
//                .setIndicator(getTabSpecView("tag1",R.mipmap.tagb1,"首页"));
//        fragmentTabHost.addTab(tabSpec1,
//                HomePagerFragment.class,//类名.class或者对象名.getClass()去获取大写class对象
//                null);//传递数据
        TabHost.TabSpec tabSpec2 = fragmentTabHost.newTabSpec("tag2")
                .setIndicator(getTabSpecView("tag2",R.mipmap.taga2,"工作台"));
        fragmentTabHost.addTab(tabSpec2,
                WorkBenchFragment.class,//类名.class或者对象名.getClass()去获取大写class对象
                null);//传递数据
        TabHost.TabSpec tabSpec3 = fragmentTabHost.newTabSpec("tag3")
                .setIndicator(getTabSpecView("tag3",R.mipmap.taga3,"我的"));
        fragmentTabHost.addTab(tabSpec3,
                MySelfFragment.class,//类名.class或者对象名.getClass()去获取大写class对象
                null);//传递数据

    }
    //用户是否登录
    private boolean checkUserIsLogin() {
        SharedPreferences sharedPreferences= getSharedPreferences("userInfo",MODE_PRIVATE);
        String uid=sharedPreferences.getString("uId","");
        if (uid.equals("")){
            return false;
        }else{//只要用户名或者密码有一个不为空，就是用户登录了
            return true;
        }
    }
    //加载布局方法
    public View getTabSpecView(String tag, int imageResId, String title){
        //加载布局方法
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.tabspec_layout,null);
        //获取控件对象
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(imageResId);
        TextView textView = view.findViewById(R.id.tv_title);
        textView.setText(title);
        imageViewMap.put(tag,imageView);
        textViewMap.put(tag,textView);
        return view;
    }
}
