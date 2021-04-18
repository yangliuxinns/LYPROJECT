package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.turings.investigationapplicqation.DialogAdapter.CustomShowTopicAdapter;
import org.turings.investigationapplicqation.Entity.PeddleData;
import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.QuestionResult;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Util.CustomListView;

import java.util.ArrayList;
import java.util.List;

public class PreViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Questionnaire questionnaire;//要显示的问卷
    private ImageView back;//关闭预览
    private TextView tv_title;//问卷名
    private TextView tv_info;//问卷描述
    private CustomListView listView;//题目列表
    private Button buttonT;//上一题
    private Button buttonN;//下一题
    private Button buttonC;//提交
    private int currrentPage;//当前页数
    private List<PeddleData> peddleDatas;//分页数据
    private CustomShowTopicAdapter customShowTopicAdapter;
    private List<Question> questions= new ArrayList<>();
    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        //接收数据
        questionnaire = (Questionnaire) getIntent().getSerializableExtra("q_data");
//        getViews();
//        getRegister();
//        init();
        progressBar= findViewById(R.id.progressbar);//进度条

        webView = findViewById(R.id.webview);
//        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html


        webView.setWebChromeClient(new WebChromeClient());//这行最好不要丢掉
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://192.168.10.223:8080/WorkProject/ylx/prepreview/"+questionnaire.getId());//加载url
        //使用webview显示html代码
//        webView.loadDataWithBaseURL(null,"<html><head><title> 欢迎您 </title></head>" +
//                "<body><h2>使用webview显示 html代码</h2></body></html>", "text/html" , "utf-8", null);

        webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);


        //不显示webview缩放按钮
//        webSettings.setDisplayZoomControls(false);
    }

    //初始化处理数据
    private void init() {
        tv_title.setText(questionnaire.getTitle());
        tv_info.setText(questionnaire.getInstructions());
        //处理数据
        //不分页
        if(questionnaire.getTotalPage() == 0){
            questions.addAll(questionnaire.getList());
            customShowTopicAdapter = new CustomShowTopicAdapter(questions,getApplicationContext(),R.layout.topic_item);
            listView.setAdapter(customShowTopicAdapter);
            buttonT.setVisibility(View.GONE);
            buttonN.setVisibility(View.GONE);
            buttonC.setVisibility(View.VISIBLE);
        }else {//分页
            peddleDatas = new ArrayList<>();
            for(int i=1;i<=questionnaire.getTotalPage();i++){
                PeddleData peddleData = new PeddleData();
                List<Question> questions = new ArrayList<>();
                List<QuestionResult> result = new ArrayList<>();
                for(int k=0;k<questionnaire.getList().size();k++){
                    if(questionnaire.getList().get(k).getPageNumber() == i){
                        questions.add(questionnaire.getList().get(k));
                    }
                }
                peddleData.setQuestions(questions);
                peddleData.setCurrent(i);
                peddleData.setSize(questions.size());
                peddleData.setResult(result);
                peddleDatas.add(peddleData);
            }
            //初始化页码
            currrentPage = 1;
            //设置显示
            buttonT.setVisibility(View.GONE);
            buttonC.setVisibility(View.GONE);
            //展示题目
            questions.addAll(peddleDatas.get(0).getQuestions());
            customShowTopicAdapter = new CustomShowTopicAdapter(questions,getApplicationContext(),R.layout.topic_item);
            listView.setAdapter(customShowTopicAdapter);
        }
    }
    //注册
    private void getRegister() {
        back.setOnClickListener(this);
        buttonT.setOnClickListener(this);
        buttonN.setOnClickListener(this);
        buttonC.setOnClickListener(this);
    }
    //获得控件
    private void getViews() {
//        back = findViewById(R.id.back);
//        tv_title = findViewById(R.id.txt_title);
//        listView = findViewById(R.id.lv_list);
//        buttonT = findViewById(R.id.btn_login);
//        buttonN = findViewById(R.id.btn_next);
//        buttonC = findViewById(R.id.btn_commit);
//        tv_info = findViewById(R.id.txt_info);
    }

    @Override
    public void onClick(View view) {

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient=new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen","拦截url:"+url);
            if(url.equals("http://www.google.com/")){
                Toast.makeText(getApplicationContext(),"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient=new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen","网页标题:"+title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen","是否有上一个页面:"+webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * JS调用android的方法
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void  getClient(String str){
        Log.i("ansen","html调用客户端:"+str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        webView.destroy();
        webView=null;
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.back:
//                finish();
//                break;
//            case R.id.btn_login://上一页
//                for(int i=0;i<peddleDatas.size();i++){
//                    if(peddleDatas.get(i).getCurrent() == currrentPage-1){
//                        questions.addAll(peddleDatas.get(i).getQuestions());
//                        customShowTopicAdapter.notifyDataSetChanged();
//                        listView.setAdapter(customShowTopicAdapter);
//                    }
//                }
//                currrentPage--;
//                if(currrentPage == 1){
//                    tv_title.setVisibility(View.VISIBLE);
//                    buttonT.setVisibility(View.GONE);
//                    buttonC.setVisibility(View.GONE);
//                }else if(currrentPage == peddleDatas.size()){
//                    tv_title.setVisibility(View.GONE);
//                    buttonT.setVisibility(View.VISIBLE);
//                    buttonC.setVisibility(View.VISIBLE);
//                }else {
//                    tv_title.setVisibility(View.GONE);
//                    buttonT.setVisibility(View.VISIBLE);
//                    buttonN.setVisibility(View.VISIBLE);
//                    buttonC.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.btn_next://下一页
//                List<QuestionResult> result = customShowTopicAdapter.getQuestionResult();
//                List<String> list = new ArrayList<>();
//                Boolean flag = true;
//                //首先判断是否有未达题目
//                for(int q=0;q<questions.size();q++){
//                    if(questions.get(q).getRequired()){
//                        for(int l = 0;l<result.size();l++){
//                            if(result.get(l).getOrder() == questions.get(q).getOrder()){
//                                flag = true;
//                            }else {
//                                flag = false;
//                            }
//                        }
//                        if(!flag){
//                            //此题未解答
//                            list.add("第"+questions.get(q).getOrder()+"题");
//                        }
//                    }
//                }
//                if(list.size() != 0){
//                    Toast.makeText(getApplicationContext(),"以下题目未作答:"+list.toString(),Toast.LENGTH_SHORT).show();
//                }else {
//                    for(int i=0;i<peddleDatas.size();i++){
//                        if(peddleDatas.get(i).getCurrent() == currrentPage+1){
//                            questions.addAll(peddleDatas.get(i).getQuestions());
//                            customShowTopicAdapter.notifyDataSetChanged();
//                            listView.setAdapter(customShowTopicAdapter);
//                        }
//                    }
//                    currrentPage++;
//                    if(currrentPage == 1){
//                        tv_title.setVisibility(View.VISIBLE);
//                        buttonT.setVisibility(View.GONE);
//                        buttonC.setVisibility(View.GONE);
//                    }else if(currrentPage == peddleDatas.size()){
//                        tv_title.setVisibility(View.GONE);
//                        buttonT.setVisibility(View.VISIBLE);
//                        buttonC.setVisibility(View.VISIBLE);
//                    }else {
//                        tv_title.setVisibility(View.GONE);
//                        buttonT.setVisibility(View.VISIBLE);
//                        buttonN.setVisibility(View.VISIBLE);
//                        buttonC.setVisibility(View.VISIBLE);
//                    }
//                }
//                break;
//            case R.id.btn_commit://提交
//                if(questionnaire.getTotalPage()==0){
//                    //判断题目是否回答完了
//                }else {
//
//                    Toast.makeText(getApplicationContext(),"此为浏览界面，不可提交",Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//        }
//    }
}
