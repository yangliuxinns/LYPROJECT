package org.turings.investigationapplicqation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.turings.investigationapplicqation.Entity.Options;
import org.turings.investigationapplicqation.Entity.Question;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//编辑题目
public class EditTopicSettings extends AppCompatActivity implements View.OnClickListener {

    //录像需要的权限
    private static final String[] VIDEO_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int VIDEO_PERMISSIONS_CODE = 1;
    private TextView t_type;//编辑的题目类型
    private LinearLayout options;//选项模块
    private LinearLayout addOptionOther;//添加其他
    private EditText et_title;//标题输入
    private Question question = new Question();//编辑的新题目
    private Button btn_Add;//确认按钮
    private String type;
    private Switch aSwitch;//是否必填
    private String TAG = this.getClass().getSimpleName();
    //装在所有动态添加的Item的LinearLayout容器
    private LinearLayout addOptionsView;

    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private TextView canel;
    private Dialog dialog;
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 1;
    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;
    //裁剪图片保存的地址
    private String pathCropPhoto;
    private String dataFileStr;//file绝对路径

    private String path;//图片存储的路径
    private Bitmap photo;//相机拍下的照片

    private int flag = 0;//添加标志
    private LinearLayout addOptions;
    private int imgflag = -1;

    private int initFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic_settings);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        type = getIntent().getStringExtra("type");
        List<Options> optionsList = new ArrayList<>();
        question.setOptions(optionsList);
        question.setRequired(false);
        getViews();
        register();
        t_type.setText(type);
        judgeQuestionType(type);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //选中
                    aSwitch.setSwitchTextAppearance(EditTopicSettings.this, R.style.s_true);
                    question.setRequired(true);
                } else {
                    aSwitch.setSwitchTextAppearance(EditTopicSettings.this, R.style.s_false);
                    question.setRequired(false);
                }
            }
        });
    }

    private void register() {
        addOptions.setOnClickListener(this);
        addOptionOther.setOnClickListener(this);
        btn_Add.setOnClickListener(this);
    }


    //获得控件
    private void getViews() {
        t_type = findViewById(R.id.type);
        options = findViewById(R.id.option_ly);
        et_title = findViewById(R.id.et_title);
        btn_Add = findViewById(R.id.btn);
        aSwitch = findViewById(R.id.swh_status);
        addOptionsView = findViewById(R.id.add_allView);
        addOptions = findViewById(R.id.addOption);
        addOptionOther = findViewById(R.id.addOptionOther);
    }

    public void judgeQuestionType(String type) {
        switch (type) {
            case "单选题":
                options.setVisibility(View.VISIBLE);
                break;
            case "填空题":
                options.setVisibility(View.GONE);
                break;
            case "多选题":
                options.setVisibility(View.VISIBLE);
                break;
            case "姓名":
                options.setVisibility(View.GONE);
                et_title.setText("您的姓名：");
                break;
            case "分页":
                question.setType(type);
                question.setTitle(et_title.getText().toString().trim());
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result_question", question);
                //设置返回数据
                setResult(RESULT_FIRST_USER, intent);//RESULT_OK为自定义常量
                //关闭Activity
                finish();
                break;
            case "性别":
                options.setVisibility(View.VISIBLE);
                addViewItem(null, "男");
                addViewItem(null, "女");
                break;
            case "手机":
                options.setVisibility(View.GONE);
                et_title.setText("您的手机号：");
                break;
            case "日期":
                options.setVisibility(View.GONE);
                et_title.setText("日期：");
                break;
            case "地区":
                options.setVisibility(View.GONE);
                et_title.setText("地区：");
                break;
            case "地图":
                options.setVisibility(View.GONE);
                et_title.setText("您的地理位置：");
                break;
        }
    }

    /**
     * Item排序
     */
    private void sortHotelViewItem() {
        //获取LinearLayout里面所有的view
        for (int i = 0; i < addOptionsView.getChildCount(); i++) {
            final int q = i;
            final View childAt = addOptionsView.getChildAt(i);
            final ImageView btn_remove = childAt.findViewById(R.id.img_delete);
            final ImageView imgDelete = childAt.findViewById(R.id.img_delete2);//删除图片
            final ImageView addImg = childAt.findViewById(R.id.img_content);
            final EditText edt = childAt.findViewById(R.id.edit);
            if (i == flag - 1) {
                if (question.getOptions().get(q).getImg().equals("sr")) {
                    int icon = getResources().getIdentifier("sr", "mipmap", getPackageName());
                    // 设置图片
                    addImg.setImageResource(icon);

                    edt.setText("其他");
                }
                if (question.getOptions().get(q).getContent().equals("男")) {
                    edt.setText("男");
                }
                if (question.getOptions().get(q).getContent().equals("女")) {
                    edt.setText("女");
                }
            }
            btn_remove.setTag("remove");//设置删除标记
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //从LinearLayout容器中删除当前点击到的ViewItem
                    addOptionsView.removeView(childAt);
                    for (int k = 0; k < question.getOptions().size(); k++) {
                        if (question.getOptions().get(k).getId() == q + 1) {
                            question.getOptions().remove(k);
                            flag--;
                        }
                    }
                    for (int l = 0; l > question.getOptions().size(); l++) {
                        question.getOptions().get(l).setId(l++);
                    }
                }
            });
            addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgDelete.getVisibility() == View.GONE && !question.getOptions().get(q).getImg().equals("sr")) {
                        show(view, q);
                    }
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除刚刚保存的图片
                    deletePathFromFile(dataFileStr + question.getOptions().get(q).getImg());
                    question.getOptions().get(q).setImg("");
//                    question.getOptions().get(q).setImgcontent(null);
                    imgDelete.setVisibility(View.GONE);
                    addImg.setImageResource(R.mipmap.img);
                }
            });
//            //如果是最后一个ViewItem，就设置为添加
//            if (i == (addOptionsView.getChildCount() - 1)) {
//                Button btn_add = (Button) childAt.findViewById(R.id.btn_addHotel);
//                btn_add.setText("+新增");
//                btn_add.setTag("add");
//                btn_add.setOnClickListener(this);
//            }
        }
    }

    //添加ViewItem
    private void addViewItem(View view, String wait) {
        View hotelEvaluateView = View.inflate(this, R.layout.option_single_item, null);
        addOptionsView.addView(hotelEvaluateView);
        flag++;
        Options options = null;
        if (wait.equals("其他")) {
            options = new Options(flag, "其他", "sr", null);
        } else if (wait.equals("男")) {
            options = new Options(flag, "男", "", null);
        } else if (wait.equals("女")) {
            options = new Options(flag, "女", "", null);
        } else {
            options = new Options(flag, "", "", null);
        }
        question.getOptions().add(options);
        sortHotelViewItem();
    }

    //获取所有动态添加的Item，找到控件的id，获取数据
    private void printData() {
        for (int i = 0; i < addOptionsView.getChildCount(); i++) {
            View childAt = addOptionsView.getChildAt(i);
            EditText edt = childAt.findViewById(R.id.edit);
            question.getOptions().get(i).setContent(edt.getText().toString().trim());
        }
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addOption:
                addViewItem(null, "");
                break;
            case R.id.btn:
                Log.i("www", "onClick: 添加");
                if (et_title.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "请填写题干", Toast.LENGTH_SHORT).show();
                } else {
                    printData();
                    question.setType(type);
                    question.setTitle(et_title.getText().toString().trim());
                    //数据是使用Intent返回
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("result_question", question);
                    //设置返回数据
                    setResult(RESULT_FIRST_USER, intent);//RESULT_OK为自定义常量
                    //关闭Activity
                    finish();
                }
                break;
            case R.id.addOptionOther:
                addViewItem(null, "其他");
                break;
        }
    }

    //自定义图片选择dialog
    public void show(View view, Integer i) {
        imgflag = i;
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.custom_picture_selection_dialog, null);
        //初始化控件
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        canel = inflate.findViewById(R.id.canel);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从相册选择
                choosePhoto();

            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击拍照
                takePhoto();
            }
        });
        canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void fixImg(Integer k) {
        for (int i = 0; i < addOptionsView.getChildCount(); i++) {
            if (i == k) {
                View childAt = addOptionsView.getChildAt(i);
                final ImageView imgDelete = childAt.findViewById(R.id.img_delete2);//删除图片
                final ImageView addImg = childAt.findViewById(R.id.img_content);
                String dataFileStr = getFilesDir().getAbsolutePath() + "/" + path;
                Bitmap bitmap = BitmapFactory.decodeFile(dataFileStr);
                //添加图片
                addImg.setImageBitmap(bitmap);
                imgDelete.setVisibility(View.VISIBLE);
                question.getOptions().get(i).setImg(path);
//                question.getOptions().get(i).setImgcontent(bitmap2Bytes(compressImage(bitmap)));
//                Log.i("eee", "fixImg:没进入 ");
//                try {
//                    byte[] arr = readStream(dataFileStr);
//                    question.getOptions().get(i).setImgcontent(arr);
//                    Log.i("eee", "fixImg:寸没存 "+arr.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获得用户拍照上传的照片
        if (resultCode == RESULT_OK) {
            switch (requestCode) {                // 选择请求码
                case CAMERA_REQUEST_CODE:
                    try {
                        // 裁剪
                        startCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    dialog.dismiss();
                    //保存裁剪后的图片并显示
                    final Uri croppedUri = UCrop.getOutput(data);
                    try {
                        if (croppedUri != null) {
                            photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
                            path = saveImgToFile(photo);
                            //删除裁剪后保存的图片
                            deletePathFromFile(pathCropPhoto);
                            if (imgflag != -1) {
                                fixImg(imgflag);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case GALLERY_REQUEST_CODE://打开相册
                    imageUri = data.getData();
                    startCrop();
                    break;
                case UCrop.RESULT_ERROR:
                    final Throwable cropError = UCrop.getError(data);
                    break;
            }
        }
    }

    //    //删除file目录下指定路径的图片
    private void deletePathFromFile(String pathCropPhoto) {
        File file = new File(pathCropPhoto);
        if (file.exists()) {
            file.delete();
        }
    }

    //    //保存拍的图片到系统中
    private String saveImgToFile(Bitmap photo) {
        dataFileStr = getFilesDir().getAbsolutePath() + "/";
        String fileName = System.currentTimeMillis() + ".jpeg";
        File file = new File(dataFileStr + fileName);
        try {// 写入图片
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    //调用相机拍照
    private void takePhoto() {
        // 跳转到系统的拍照界面
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        imageUri = Uri.fromFile(new File(mTempPhotoPath));
        //下面这句指定调用相机拍照后的照片存储的路径
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
    }

    //裁剪图片
    private void startCrop() {
        String dataFileStr = getFilesDir().getAbsolutePath() + "/";
        String fileName = System.currentTimeMillis() + ".jpg";
        pathCropPhoto = dataFileStr + fileName;
        Uri destinationUri = Uri.fromFile(new File(dataFileStr + fileName));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorMain));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorMain));
        uCrop.withOptions(options);
        uCrop.start(this);
    }

    //从相册中选择
    public void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//打开图库
        //REQUEST_PICTURE_CHOOSE表示请求参数，是个常量
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    //图片转二进制流
    public byte[] readStream(String imagepath) throws Exception {
        Log.i("www", "readStream:--------------------------------------------------- " + imagepath);
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
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