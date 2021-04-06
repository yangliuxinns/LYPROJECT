package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.turings.investigationapplicqation.DialogAdapter.RubbishAdapter;
import org.turings.investigationapplicqation.Entity.Questionnaire;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.turings.investigationapplicqation.Util.RecyclerViewAnimation.runLayoutAnimation;

//回收站
public class RubbishActivity extends AppCompatActivity {

    @BindView(R.id.tv_edit)
    TextView tvEdit;//编辑
    @BindView(R.id.rv_normal_show)
    LinearLayout rvNormalShow;//没有数据时展示的布局
    @BindView(R.id.rv)
    RecyclerView rv;//列表
    @BindView(R.id.tv_check_all)
    TextView tvCheckAll;//全选
    @BindView(R.id.tv_delete)
    TextView tvDelete;//删除
    @BindView(R.id.tv_revert_all)
    TextView tvRevert;//删除
    @BindView(R.id.lay_bottom)
    LinearLayout layBottom;//底部布局
    @BindView(R.id.back2)
    ImageView back;

    private static final int STATE_DEFAULT = 0;//默认状态
    private static final int STATE_EDIT = 1;//编辑状态
    private int mEditMode = STATE_DEFAULT;
    private boolean editorStatus = false;//是否为编辑状态
    private int index = 0;//当前选中的item数

    private List<Questionnaire> resultsBeans = new ArrayList<>();
    private List<Questionnaire> list;
    private Boolean flag = false;
    List<Questionnaire> mList = new ArrayList<>();//列表
    RubbishAdapter mAdapter;//适配器
    private OkHttpClient okHttpClient;
    private String uId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    for (Questionnaire questionnaire : list) {
                        resultsBeans.add(questionnaire);
                        if (resultsBeans.size() > 0) {
                            mList.clear();
                            mList.addAll(resultsBeans);
                            mAdapter.notifyDataSetChanged();//刷新数据
                            runLayoutAnimation(rv);//动画显示
                            rv.setVisibility(View.VISIBLE);
                            rvNormalShow.setVisibility(View.GONE);
                        } else {
                            rv.setVisibility(View.GONE);
                            rvNormalShow.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 2:
                    if(msg.obj.equals("恢复成功")){
                        //去恢复
                        for (int i = mList.size() - 1; i >= 0; i--) {

                            if (mList.get(i).isSelect() == true) {
                                mList.remove(i);
                            }
                        }
                        //删除选中的item之后判断是否还有数据，没有则退出编辑模式
                        if (mList.size() != 0) {
                            index = 0;//删除之后置为0
                            tvDelete.setText("删除");
                            tvRevert.setText("恢复");
                        }else {
                            tvEdit.setText("编辑");
                            layBottom.setVisibility(View.GONE);
                            editorStatus = false;
                            //没有数据自然也不存在编辑了
                            tvEdit.setVisibility(View.GONE);
                            rvNormalShow.setVisibility(View.VISIBLE);
                        }

                        mAdapter.notifyDataSetChanged();
                        runLayoutAnimation(rv);//动画显示
                    }else {
                        Toast.makeText(getApplicationContext(),"恢复失败，请重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if(msg.obj.equals("删除成功")){
                        //去删除
                        for (int i = mList.size() - 1; i >= 0; i--) {
                            if (mList.get(i).isSelect() == true) {
                                mList.remove(i);
                            }
                        }
                        //删除选中的item之后判断是否还有数据，没有则退出编辑模式
                        if (mList.size() != 0) {
                            index = 0;//删除之后置为0
                            tvDelete.setText("删除");
                            tvRevert.setText("恢复");
                        }else {
                            tvEdit.setText("编辑");
                            layBottom.setVisibility(View.GONE);
                            editorStatus = false;
                            //没有数据自然也不存在编辑了
                            tvEdit.setVisibility(View.GONE);
                            rvNormalShow.setVisibility(View.VISIBLE);
                        }

                        mAdapter.notifyDataSetChanged();
                        runLayoutAnimation(rv);//动画显示
                    }else {
                        Toast.makeText(getApplicationContext(),"删除失败，请重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        ButterKnife.bind(this);

        initList();
    }
    //初始化列表数据
    private void initList() {
        //数据是后台读出来的
        mAdapter = new RubbishAdapter(R.layout.rubbish_data_item, mList);//绑定视图和数据
        rv.setLayoutManager(new LinearLayoutManager(this));//设置线性布局管理器
        rv.setAdapter(mAdapter);//设置适配器

        //假数据
//        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn2 = new Questionnaire(2,"调查问卷2","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn3 = new Questionnaire(3,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn4 = new Questionnaire(4,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn5 = new Questionnaire(5,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn6 = new Questionnaire(6,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn7 = new Questionnaire(7,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn8 = new Questionnaire(8,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn9 = new Questionnaire(9,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//        Questionnaire qn10 = new Questionnaire(10,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
//
//
//        List<Questionnaire> resultsBeans = new ArrayList<>();
//        resultsBeans.add(qn1);
//        resultsBeans.add(qn2);
//        resultsBeans.add(qn3);
//        resultsBeans.add(qn4);
//        resultsBeans.add(qn5);
//        resultsBeans.add(qn6);
//        resultsBeans.add(qn7);
//        resultsBeans.add(qn8);
//        resultsBeans.add(qn9);
//        resultsBeans.add(qn10);
        //获取用户的id
        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        uId = sp.getString("uId",null);
        //搜索数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDataBase();
            }
        }).start();
    }
    //页面的点击事件
    @OnClick({R.id.tv_edit, R.id.tv_check_all, R.id.tv_delete,R.id.tv_revert_all,R.id.back2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit://编辑
                updateEditState();
                break;
            case R.id.tv_check_all://全选
                //判断是不是第一次选
                if(flag){
                    setAllItemUnchecked();
                    flag = false;
                }else {
                    setAllItemChecked();
                    flag = true;
                }
                break;
            case R.id.tv_delete://删除
                deleteCheckItem();
                break;
            case R.id.tv_revert_all://恢复
                deleteRevertItem();
                break;
            case R.id.back2:
                finish();
                break;
        }
    }

    //改变编辑状态
    private void updateEditState() {
        mEditMode = mEditMode == STATE_DEFAULT ? STATE_EDIT : STATE_DEFAULT;
        if (mEditMode == STATE_EDIT) {
            tvEdit.setText("取消");
            layBottom.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            tvEdit.setText("编辑");
            layBottom.setVisibility(View.GONE);
            editorStatus = false;

            setAllItemUnchecked();//取消全选
        }
        mAdapter.setEditMode(mEditMode);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (editorStatus) {//编辑状态
                    Questionnaire dataBean = mList.get(position);
                    boolean isSelect = dataBean.isSelect();
                    if (!isSelect) {
                        index++;
                        dataBean.setSelect(true);

                    } else {
                        dataBean.setSelect(false);
                        index--;
                    }
                    if (index == 0) {
                        tvDelete.setText("删除");
                        tvRevert.setText("恢复");
                    } else {
                        tvDelete.setText("删除(" + index + ")");
                        tvRevert.setText("恢复(" + index + ")");
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //全部选中
    private void setAllItemChecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(true);
        }
        mAdapter.notifyDataSetChanged();
        index = mList.size();
        tvDelete.setText("删除(" + index + ")");
        tvRevert.setText("恢复(" + index + ")");
        tvCheckAll.setText("反选");
    }

    //取消全部选中
    private void setAllItemUnchecked() {
        if (mAdapter == null) return;
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        mAdapter.notifyDataSetChanged();
        tvDelete.setText("删除");
        tvRevert.setText("恢复");
        tvCheckAll.setText("全选");
        index = 0;
    }



    //删除选中的item
    private void deleteCheckItem() {
        List<Integer> listId = new ArrayList<>();
        if (mAdapter == null) return;

        for (int i = mList.size() - 1; i >= 0; i--) {

            if (mList.get(i).isSelect() == true) {
                //去删除
                listId.add(mList.get(i).getId());
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToDelete(listId);
            }
        }).start();

    }
    //恢复选中的item
    private void deleteRevertItem() {
        List<Integer> listId = new ArrayList<>();
        if (mAdapter == null) return;

        for (int i = mList.size() - 1; i >= 0; i--) {

            if (mList.get(i).isSelect() == true) {
                listId.add(mList.get(i).getId());
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadToRevert(listId);
            }
        }).start();

    }
    //访问服务器上传至数据库，搜索
    private void uploadToDataBase() {
        okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uId", uId)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/findQuestionaresByUserIdDelete";
        final Request request = new Request.Builder().post(formBody).url(url).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        Type type = new TypeToken<List<Questionnaire>>() {
                        }.getType();
                        //反序列化
                        list = new ArrayList<>();
                        list = gson.fromJson(response.body().string(), type);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    //恢复
    private void uploadToRevert(List<Integer> ids) {
        okHttpClient = new OkHttpClient();
        for(int i:ids){
            Log.i("qqq", "uploadToRevert:id是： "+i);
        }
        String idList = new Gson().toJson(ids);
        FormBody formBody = new FormBody.Builder()
                .add("ids",idList)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/revertQuestionaire";
        final Request request = new Request.Builder().post(formBody).url(url).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = Message.obtain();
                        if (response.body().toString() !=null && !response.body().toString().equals("")) {
                            msg.obj = "恢复成功";
                        } else {
                            msg.obj = "恢复失败";
                        }
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    //删除
    private void uploadToDelete(List<Integer> ids) {
        okHttpClient = new OkHttpClient();
        String idList = new Gson().toJson(ids);
        FormBody formBody = new FormBody.Builder()
                .add("ids",idList)
                .build();
        String url = "http://" + getResources().getString(R.string.ipConfig) + ":8080/WorkProject/ylx/deleteQuestionaire";
        final Request request = new Request.Builder().post(formBody).url(url).build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("lww", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = Message.obtain();
                        if (response.body().toString() !=null && !response.body().toString().equals("")) {
                            msg.obj = "删除成功";
                        } else {
                            msg.obj = "删除失败";
                        }
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
