package org.turings.investigationapplicqation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.turings.investigationapplicqation.DialogAdapter.RubbishAdapter;
import org.turings.investigationapplicqation.Entity.Questionnaire;

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
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;//刷新布局
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

    private Boolean flag = false;
    List<Questionnaire> mList = new ArrayList<>();//列表
    RubbishAdapter mAdapter;//适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        ButterKnife.bind(this);

        initList();

        //禁用下拉和上拉
        refresh.setEnableRefresh(false);
        refresh.setEnableLoadMore(false);
    }
    //初始化列表数据
    private void initList() {
        //数据是后台读出来的
        mAdapter = new RubbishAdapter(R.layout.rubbish_data_item, mList);//绑定视图和数据
        rv.setLayoutManager(new LinearLayoutManager(this));//设置线性布局管理器
        rv.setAdapter(mAdapter);//设置适配器

        //假数据
        Questionnaire qn1 = new Questionnaire(1,"调查问卷1","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn2 = new Questionnaire(2,"调查问卷2","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn3 = new Questionnaire(3,"调查问卷3","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn4 = new Questionnaire(4,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn5 = new Questionnaire(5,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn6 = new Questionnaire(6,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn7 = new Questionnaire(7,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn8 = new Questionnaire(8,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn9 = new Questionnaire(9,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);
        Questionnaire qn10 = new Questionnaire(10,"调查问卷4","问卷说明1lalaalalalalalala",false,null,0,true,true,true,"1",1,true);


        List<Questionnaire> resultsBeans = new ArrayList<>();
        resultsBeans.add(qn1);
        resultsBeans.add(qn2);
        resultsBeans.add(qn3);
        resultsBeans.add(qn4);
        resultsBeans.add(qn5);
        resultsBeans.add(qn6);
        resultsBeans.add(qn7);
        resultsBeans.add(qn8);
        resultsBeans.add(qn9);
        resultsBeans.add(qn10);


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
        refresh.finishRefresh();//数据加载出来之后，结束下拉动作
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
        if (mAdapter == null) return;

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
            //启用下拉
            refresh.setEnableRefresh(true);

            //下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                //重新装填数据
                initList();
                index = 0;
                mEditMode = STATE_DEFAULT;//恢复默认状态
                editorStatus = false;//恢复默认状态
                tvDelete.setText("删除");
                tvRevert.setText("恢复");
                tvEdit.setVisibility(View.VISIBLE);//显示编辑
            });
        }

        mAdapter.notifyDataSetChanged();
        runLayoutAnimation(rv);//动画显示
    }
    //删除选中的item
    private void deleteRevertItem() {
        if (mAdapter == null) return;

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
            //启用下拉
            refresh.setEnableRefresh(true);

            //下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                //重新装填数据
                initList();
                index = 0;
                mEditMode = STATE_DEFAULT;//恢复默认状态
                editorStatus = false;//恢复默认状态
                tvDelete.setText("删除");
                tvRevert.setText("恢复");
                tvEdit.setVisibility(View.VISIBLE);//显示编辑
            });
        }

        mAdapter.notifyDataSetChanged();
        runLayoutAnimation(rv);//动画显示
    }
}
