package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.turings.investigationapplicqation.Entity.Question;
import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.MainActivity;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.ReleaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.turings.investigationapplicqation.Util.RecyclerViewAnimation.runLayoutAnimation;

//是否发布确认
public class CustomPublishDialog extends DialogFragment {
    private OkHttpClient okHttpClient;
    private Response response;//响应
    private Questionnaire questionnaire;//问卷

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(msg.obj.equals("") || msg.obj == null){
                        Toast.makeText(getActivity().getApplicationContext(),"失败，请重试",Toast.LENGTH_SHORT).show();
                    }else {
                        //发布问卷
                        Intent inten = new Intent(getActivity(), ReleaseActivity.class);
                        inten.putExtra("url","http://192.168.137.1:8080/WorkProject/ylx/preInvestigation/"+questionnaire.getId());
                        inten.putExtra("uId",questionnaire.getId()+"");
                        startActivity(inten);
                        getActivity().finish();
                    }
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_publish_dialog, container, false);//布局，父视图，是否立刻加载
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确定跳转发布
                for(Question qu:questionnaire.getList()){
                    if(qu.getOptions() != null){
                        for(int i=0;i<qu.getOptions().size();i++){
                            if(!qu.getOptions().get(i).getImg().equals("sr") && !qu.getOptions().get(i).getImg().isEmpty()){
                                String dataFileStr = getActivity().getFilesDir().getAbsolutePath() + "/" + qu.getOptions().get(i).getImg();
                                Bitmap bitmap = BitmapFactory.decodeFile(dataFileStr);
                                qu.getOptions().get(i).setImgcontent(bitmap2Bytes(compressImage(bitmap)));
                            }
                        }
                    }
                }
                //保存之前删除分页
                Iterator<Question> iterator = questionnaire.getList().iterator();
                while (iterator.hasNext()) {
                    Question value = iterator.next();
                    if (value.getType().equals("分页")) {
                        iterator.remove();
                    }
                }
                //先保存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadToDataBase(questionnaire);
                    }
                }).start();

            }
        });
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消按钮执行的操作
                getDialog().dismiss();//关闭当前的对话框

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        // WindowManager 接口的嵌套类
        WindowManager.LayoutParams windowParams = window.getAttributes();
        //设置弹出框周围的透明度
        windowParams.dimAmount = 0.5f;
        //设置弹出框内容的透明度
        windowParams.alpha = 1f;
        //设置弹出框距离上面的距离
        windowParams.y = 100;
        window.setBackgroundDrawableResource(R.drawable.dialog_stroke_layout_ylx);
        windowParams.width = 900;// 调整该值可以设置对话框显示的宽度
        window.setAttributes(windowParams);
        //DisplayMetrics类 获取手机显示屏的基本信息 包括尺寸、密度、字体缩放等信息
        DisplayMetrics dm = new DisplayMetrics();
        //将屏幕尺寸赋给dm
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //设置弹出框的高度和宽度
    }

    public void setMsgData(Questionnaire subjectMsg) {
        questionnaire = subjectMsg;
    }
    //访问服务器上传至数据库保存
    private void uploadToDataBase(Questionnaire subjectMsg) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String subject = gson.toJson(subjectMsg);
        okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),subject);
        String url = "http://"+getResources().getString(R.string.ipConfig)+":8080/WorkProject/ylx/saveQuestionares";
//
        Request request = new Request.Builder().post(requestBody).url(url).build();
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
                        String str = response.body().string();
                        questionnaire.setId(Integer.parseInt(str));
                        Message msg = Message.obtain();
                        msg.obj = str;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 压缩图片
     * 该方法引用自：http://blog.csdn.net/demonliuhui/article/details/52949203
     *
     * @param image
     * @return
     */
    public  Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
