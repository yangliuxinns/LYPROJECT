package org.turings.investigationapplicqation.DialogAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.turings.investigationapplicqation.Entity.Questionnaire;
import org.turings.investigationapplicqation.Entity.Result;
import org.turings.investigationapplicqation.Entity.ResultInfo;
import org.turings.investigationapplicqation.Entity.ShowData;
import org.turings.investigationapplicqation.R;
import org.turings.investigationapplicqation.SeeFillInTheBlanksActtivity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSummaryResultAdapter extends BaseAdapter {
    private Questionnaire questionnaire;
    private List<Result> list;//数据源
    private Context context;//上下文环境
    private int itemLayout;//布局文件
    public ViewHolder holder;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
    private int currentItem1 = -1;
    private int currentItem2 = -1;
    private int sumPerson;
    public boolean flag;

    public CustomSummaryResultAdapter(Questionnaire questionnaire,List<Result> list, Context context, int itemLayout) {
        this.questionnaire = questionnaire;
        this.list = list;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        if(questionnaire.getList() != null){
            return questionnaire.getList().size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if(questionnaire.getList() != null){
            return questionnaire.getList().get(i);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if(questionnaire.getList() != null){
            return i;
        }else {
            return 0;
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int listPosition = i;
        if(null == view){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.summary_result_item,null);
            holder.qNumber = view.findViewById(R.id.question_number);
            holder.title = view.findViewById(R.id.title);
            holder.tl = view.findViewById(R.id.table_risk_profile);
            holder.btnBing = view.findViewById(R.id.bing);
            holder.btnZhu = view.findViewById(R.id.zhu);
            holder.btnTiao = view.findViewById(R.id.tiao);
            holder.mybarchart = view.findViewById(R.id.bc_1);
            holder.mylineChart = view.findViewById(R.id.lc_1);
            holder.myradarchart = view.findViewById(R.id.rc_1);
            holder.ly = view.findViewById(R.id.ly);
            holder.info = view.findViewById(R.id.info);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.btnBing.setTag(i);
        holder.btnZhu.setTag(i);
        holder.btnTiao.setTag(i);
        //添加标题
        holder.qNumber.setText("第"+questionnaire.getList().get(listPosition).getOrder()+"题：");
        holder.title.setText(questionnaire.getList().get(listPosition).getTitle());

        initLineChart(listPosition);
        initBarChart(listPosition);
        initRadarChart(listPosition);
        holder.ly.setVisibility(View.GONE);
        holder.mylineChart.setVisibility(View.GONE);
        holder.myradarchart.setVisibility(View.GONE);
        holder.mybarchart.setVisibility(View.GONE);
        if(currentItem == i || currentItem1 == i || currentItem2 == i){
            holder.ly.setVisibility(View.VISIBLE);
        }else {
            holder.ly.setVisibility(View.GONE);
        }
        if (currentItem == i) {
            holder.btnBing.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
            holder.btnBing.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.mybarchart.setVisibility(View.VISIBLE);
        } else {
            holder.btnBing.setBackgroundColor(context.getResources().getColor(R.color.whitesmoke));
            holder.btnBing.setTextColor(Color.BLACK);
            holder.mybarchart.setVisibility(View.GONE);
        }
        if (currentItem1 == i) {
            holder.btnZhu.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
            holder.btnZhu.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.mylineChart.setVisibility(View.VISIBLE);
        } else {
            holder.btnZhu.setBackgroundColor(context.getResources().getColor(R.color.whitesmoke));
            holder.btnZhu.setTextColor(Color.BLACK);
            holder.mylineChart.setVisibility(View.GONE);
        }
        if (currentItem2 == i) {
            holder.btnTiao.setBackgroundColor(context.getResources().getColor(R.color.colorMain));
            holder.btnTiao.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.myradarchart.setVisibility(View.VISIBLE);
        } else {
            holder.btnTiao.setBackgroundColor(context.getResources().getColor(R.color.whitesmoke));
            holder.btnTiao.setTextColor(Color.BLACK);
            holder.myradarchart.setVisibility(View.GONE);
        }

        //添加表格
        if(questionnaire.getList().get(listPosition).getType().equals("单选题") || questionnaire.getList().get(listPosition).getType().equals("多选题") || questionnaire.getList().get(listPosition).getType().equals("性别")){
            //显示表格
            buildTable(listPosition);
            holder.info.setVisibility(View.GONE);
            holder.btnBing.setVisibility(View.VISIBLE);
            holder.btnZhu.setVisibility(View.VISIBLE);
            holder.btnTiao.setVisibility(View.VISIBLE);
            holder.tl.setVisibility(View.VISIBLE);
            holder.tl.setStretchAllColumns(true);//设置所有的item都可伸缩扩展
            holder.tl.setDividerDrawable(view.getResources().getDrawable(R.drawable.bonus_list_item_divider));//这个就是中间的虚线
            holder.tl.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);//设置分割线为中间显示

        }else {
            //隐藏表格
            holder.info.setVisibility(View.VISIBLE);
            holder.btnBing.setVisibility(View.GONE);
            holder.btnZhu.setVisibility(View.GONE);
            holder.btnTiao.setVisibility(View.GONE);
            holder.tl.setVisibility(View.GONE);
        }

        holder.btnBing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                    currentItem1 = -1;
                    currentItem2 = -1;

                }
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步
//                mOnItemDownMoveListener.onDownMoveClick(listPosition,holder.cartView);

            }
        });
        holder.btnZhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer) view.getTag();
                if (tag == currentItem1) { //再次点击
                    currentItem1 = -1; //给 currentItem 一个无效值
                } else {
                    currentItem1 = tag;
                    currentItem = -1;
                    currentItem2 = -1;
                }
                notifyDataSetChanged();
            }
        });
        holder.btnTiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (Integer) view.getTag();
                if (tag == currentItem2) { //再次点击
                    currentItem2 = -1; //给 currentItem 一个无效值
                } else {
                    currentItem2 = tag;
                    currentItem1 = -1;
                    currentItem = -1;
                }
                notifyDataSetChanged();
            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击查看详情
                //统计这道题的数据
                List<ShowData> strs = getResultForNum(listPosition);
                //跳转页面
                Intent intent = new Intent(context, SeeFillInTheBlanksActtivity.class);
                intent.putExtra("data", (Serializable) strs);
                context.startActivity(intent);
            }
        });
        //添加图标
        return view;
    }
    public final static class ViewHolder{
        public TextView qNumber;//题号
        public TextView title;//题干
        public TableLayout tl;//动态生成表格
        public Button btnBing;
        public Button btnZhu;
        public Button btnTiao;
        public com.github.mikephil.charting.charts.LineChart mylineChart;
        public com.github.mikephil.charting.charts.BarChart mybarchart;
        public com.github.mikephil.charting.charts.PieChart myradarchart;

        public Button info;//详细信息
        public LinearLayout ly;
    }

    private void initRadarChart(int post) {

//        //统计这道题的数据
        Map<Integer,Integer> data = null;
        int num = 0;
        if(questionnaire.getList().get(post).getType().equals("单选题") || questionnaire.getList().get(post).getType().equals("多选题") || questionnaire.getList().get(post).getType().equals("性别")){
            data = getData(post);
            for (Integer key : data.keySet()) {
                num++;
            }
        }
        setBingdDesc();
        setBingLegend();
        loadBingData(data,num);

    }


    public List<ShowData> getResultForNum(int num){
        List<ShowData> listStr = new ArrayList<>();
        int n = 0;
        for(Result result : list){
            for(ResultInfo resultInfo: result.getResults()){
                if(questionnaire.getList().get(num).getOrder() == resultInfo.getQuestion_number()){
                    n++;
                    ShowData showData = new ShowData(n,resultInfo.getResult());
                    listStr.add(showData);
                }
            }
        }
        return listStr;
    }
    //饼状图描述
    private void setBingdDesc() {
        Description description = new Description();
        description.setText("饼状图统计显示");
        description.setTextColor(context.getResources().getColor(R.color.colorMain));
        description.setTextSize(18f);
        description.setTypeface(Typeface.DEFAULT_BOLD);//字体加粗

        // 获取屏幕中间x 轴的像素坐标
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float x = dm.widthPixels / 2;

        description.setXOffset(x);
//        description.setYOffset(50f);
        holder.myradarchart.setDescription(description);
    }

    public void setBingLegend(){
        Legend legend = holder.myradarchart.getLegend();
        legend.setTextSize(16f);
        legend.setTextColor(context.getResources().getColor(R.color.select_title_text));
        //设置图例是在文字的左边还是右边
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //设置图例为圆形，默认为方形
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10);
//        legend.setFormLineWidth(20);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setFormToTextSpace(15);
        legend.setXOffset(0);
    }

    private void loadBingData(Map<Integer,Integer> data,int num) {
        //设置偏移量
        holder.myradarchart.setExtraOffsets(40,0,0,0);
        //创建数据源List<PieEntry>对象
        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        //先判断有没0
        boolean flag = false;
        int sum = 0;
        float n = 0f;
        if(data!=null){
            for (Integer key : data.keySet()) {
                if(data.get(key) == 0){
                    flag =true;
                    sum++;
                }
            }
            if(sumPerson == 0){//没有人作答
                for (Integer key : data.keySet()) {
                    float d = 100;
                    float s = num;
                    float f = d/s;
                    pieEntries.add(new PieEntry(f,"第"+(key+1)+"选项"));
                }
            }else {//有人作答
                //某些选项无人作答
                if(flag){
                    int m = num - sum;
                    float sum1 = sum*1f;
                    float b = sum1/100;
                    n=b/m;
                    for (Integer key : data.keySet()) {
                        if(data.get(key) == 0){
                            pieEntries.add(new PieEntry(0.01f,"第"+(key+1)+"选项"));
                        }else {
                            float d = Float.valueOf(data.get(key));
                            float s = Float.valueOf(sumPerson);
                            float f = d/s;
                            float a = f-n;
                            pieEntries.add(new PieEntry(a,"第"+(key+1)+"选项"));
                        }
                    }

                }else { //都有人作答
                    for (Integer key : data.keySet()) {
                        float d = data.get(key);
                        float s = sumPerson;
                        float f = d/s;
                        pieEntries.add(new PieEntry(f,"第"+(key+1)+"选项"));

                    }
                }
            }
        }
        final boolean flag1 = flag;
        final float f = n;

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"options");
        pieDataSet.setColors(context.getResources().getColor(R.color.colorTomato),context.getResources().getColor(R.color.date_picker_text_light),Color.YELLOW,context.getResources().getColor(R.color.colorMain),context.getResources().getColor(R.color.date_picker_text_light));//分别为每个分组设置颜色
//        pieDataSet.setDrawValues();是否绘制文字
        pieDataSet.setValueTextColor(context.getResources().getColor(R.color.select_title_text));//设置在饼图上显示的百分数颜色
        pieDataSet.setSelectionShift(15f);//设置饼块选中时偏离饼图中心的距离
        pieDataSet.setValueTextSize(16f);//设置在饼图上显示文字的大小
        pieDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieDataSet.setSliceSpace(5f);//设置饼块与饼块之间的距离

        pieDataSet.setValueLineColor(context.getResources().getColor(R.color.select_title_text));//设置连接线的颜色

        //可用过该属性可以设置是否绘制连接线
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        //设置饼图上的文字是绘制在内部还是外部
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        pieDataSet.setValueLineVariableLength(true);//设置数据连接线的长度是否可变
        //设置连接线距离饼图的距离，注意为百分数，1-100
        pieDataSet.setValueLinePart1OffsetPercentage(80f);
        //设置连接线的长度，一般取0-1的值即可
        pieDataSet.setValueLinePart1Length(0.8f);
        //设置连接线的宽度
        pieDataSet.setValueLineWidth(2f);

//        final String strs[] = new String[]{"有违章","无违章"};

        final List<String> strs = new ArrayList<>();
        for(int i=0;i<num;i++){
            strs.add("第"+(i+1)+"选项");
        }
        //自定义格式
        pieDataSet.setValueFormatter(new IValueFormatter() {
            private int indd = -1;
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if(sumPerson == 0){
                    indd++;
                    if(indd >= strs.size()-1){
                        indd = 0;
                    }
                    DecimalFormat format = new DecimalFormat("0%");
                    String s;
                    s = format.format(0);
                    return s;
                }else {
                    if(flag1) {
                        //如果有0
                        indd++;
                        if(indd >= strs.size()-1){
                            indd = 0;
                        }
                        DecimalFormat format = new DecimalFormat("0%");
                        String s;
                        if(value == 0.01f){
                            s = format.format(0);
                        }else {
                            s = format.format(value+f);
                        }
                        return s;
                    }else {
                        indd++;
                        if(indd >= strs.size()-1){
                            indd = 0;
                        }
                        DecimalFormat format = new DecimalFormat("0%");
                        String s = format.format(value);
                        return s;
                    }
                }
            }
        });

        //设置初始旋转角度，没有动画
        holder.myradarchart.setRotationAngle(60f);

        //是否绘制中间的文本
        holder.myradarchart.setDrawCenterText(false);
        holder.myradarchart.setCenterText("中心文字");
        holder.myradarchart.setCenterTextSize(16f);
        holder.myradarchart.setCenterTextColor(Color.BLACK);
        holder.myradarchart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        holder.myradarchart.setCenterTextSizePixels(20f);//设置中心显示文字的像素
        //是否绘制空洞，若为false则为实心圆，且不会绘制透明圈
        holder.myradarchart.setDrawHoleEnabled(false);
        //设置空洞的颜色
//        pc.setHoleColor(Color.GRAY);
        //设置空洞的半径大小
//        pc.setHoleRadius(-5f);

        //是否绘制饼图上的文字，不是百分比的那个文字
        holder.myradarchart.setDrawEntryLabels(false);

        //设置透明圈的半径，注意透明圈的半径必须大于空洞的半径，不然看不到效果默认55%
        holder.myradarchart.setTransparentCircleRadius(30f);
        //设置是否可以进行旋转
        holder.myradarchart.setRotationEnabled(false);

        holder.myradarchart.setHighlightPerTapEnabled(false);//设置点击是否可以放大

        holder.myradarchart.animateXY(800,800, Easing.EasingOption.EaseInOutQuad,Easing.EasingOption.EaseInOutQuad);

        PieData pieData = new PieData(pieDataSet);

        //填充数据
        holder.myradarchart.setData(pieData);

    }
    //折线图
    public void initLineChart(int post){

        //统计这道题的数据
        Map<Integer,Integer> data = null;
        int num = 0;
        if(questionnaire.getList().get(post).getType().equals("单选题") || questionnaire.getList().get(post).getType().equals("多选题") || questionnaire.getList().get(post).getType().equals("性别")){
            data = getData(post);
            for (Integer key : data.keySet()) {
                num++;
            }
        }
        Log.i("www", "initLineChart:num "+num);
        setLineDesc();
        setLineYAxis(sumPerson);
        setLineXAxis(num);
        seLinetData(data);
    }

    public void setLineDesc(){
        Description description = new Description();
        description.setText("折线图");//设置文本
        description.setTextSize(10f); //设置文本大小
        description.setTypeface(Typeface.DEFAULT_BOLD);//设置文本样式加粗显示
        description.setTextColor(Color.BLACK);//设置文本颜色

        //获取屏幕的中间位置
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float x = dm.widthPixels / 2;

        description.setPosition(x,40);//设置文本的位置
        holder.mylineChart.setDescription(description);//添加给LineChart
    }


    public void setLineYAxis(int sumPerson){
        holder.mylineChart.getAxisRight().setEnabled(false);
        YAxis yAxis = holder.mylineChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setLabelCount(sumPerson,true);
        yAxis.setAxisMaximum((float) (sumPerson*1.0));
        yAxis.setAxisMinimum(0f);
        yAxis.setInverted(false);
        yAxis.setGridLineWidth(2);
        yAxis.setTextSize(14f);
        yAxis.setXOffset(15);//设置15dp的距离

        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String tep = value + "";
                return tep.substring(0,tep.indexOf(".")) + "人";
            }
        });

    }

    public void setLineXAxis(int num) {
        XAxis xAxis = holder.mylineChart.getXAxis();
        xAxis.setTextSize(14f);//设置字体大小
        xAxis.setTextColor(Color.RED);//设置字体颜色
        xAxis.setLabelRotationAngle(60f);//旋转60度
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//将x轴位于底部
        xAxis.setDrawGridLines(false);//不绘制网格线
        xAxis.setGranularity(1);//间隔1
        //小技巧，通过设置Axis的最小值设置为负值
        //可以改变距离与Y轴的距离
        final List<String> weeks = new ArrayList<>();
        for(int i=0;i<num;i++){
            weeks.add("第"+(i+1)+"选项");
        }
        xAxis.setAxisMaximum(num-1);//设置最小值
        xAxis.setAxisMinimum(0);//设置最大值
        xAxis.setLabelCount(num-1);//设置数量
        //自定义样式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int tep = (int) (value + 1);
                Log.i("www", "getFormattedValue: "+tep);
                return "第"+tep+"选项";

            }
        });
    }




    public void seLinetData(Map<Integer,Integer> data){

        List<Entry> entries = new ArrayList<Entry>();

        if(data!=null){
            for (Integer key : data.keySet()) {
                entries.add(new Entry(key,data.get(key)));
            }
        }
        LineDataSet lineDataSet = new LineDataSet(entries,"option");

        lineDataSet.setCircleRadius(5);//设置圆点半径大小
        lineDataSet.setLineWidth(3);//设置折线的宽度
//        lineDataSet.setCircleColor(Color.RED);//一次性设置所有圆点的颜色
        lineDataSet.setDrawCircleHole(false);//设置是否空心
        lineDataSet.setCircleColors(context.getResources().getColor(R.color.colorMain));//依次设置每个圆点的颜色

//        lineDataSet.setFillColor(Color.GRAY);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置折线类型，这里设置为贝塞尔曲线
        lineDataSet.setCubicIntensity(0.2f);//设置曲线的Mode强度，0-1
        lineDataSet.setDrawFilled(true);//是否绘制折线下方的填充
//        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);设置依附左边还是右边，
        lineDataSet.setColor(Color.RED);//设置折线的颜色,有三个构造方法
        LineData lineData = new LineData(lineDataSet);

        holder.mylineChart.setData(lineData);

    }

    //柱状图
    public void initBarChart(int post){
        //统计这道题的数据
        Map<Integer,Integer> data = null;
        int num = 0;
        if(questionnaire.getList().get(post).getType().equals("单选题") || questionnaire.getList().get(post).getType().equals("多选题") || questionnaire.getList().get(post).getType().equals("性别")){
            data = getData(post);
            for (Integer key : data.keySet()) {
                num++;
            }
        }


        setLegend();

        setDesc();

        setXAxis(num);

        setYAxis(sumPerson);

        loadData(data);
    }

    public Map<Integer,Integer>  getData(int post){
        int length = questionnaire.getList().get(post).getOptions().size();//根据数据，判断行数
        sumPerson = 0;
        for (int k = 0; k < list.size(); k++) {
            //循环每个人的答案
            for (int q = 0; q < list.get(k).getResults().size(); q++) {
                if (list.get(k).getResults().get(q).getQuestion_number() == questionnaire.getList().get(post).getOrder()) {
                    //人数加一
                    sumPerson++;
                }
            }
        }
        Map<Integer,Integer> data = new HashMap<>();
        for (int i = 0; i < length; i++) {
            //此选项选择人数
            int opNum = 0;
            for (int k1 = 0; k1 < list.size(); k1++) {
                //循环每个人的答案
                for (int q = 0; q < list.get(k1).getResults().size(); q++) {
                    if (list.get(k1).getResults().get(q).getQuestion_number() == questionnaire.getList().get(post).getOrder()) {
                        if (list.get(k1).getResults().get(q).getOrder_number() == (i + 1)) {
                            //选择此选项的人加1
                            opNum++;
                        }
                    }
                }
            }
            data.put(i,opNum);
        }
        return data;
    }


    public void buildTable(int post) {
        holder.tl.removeAllViews();
        int length = questionnaire.getList().get(post).getOptions().size();//根据数据，判断行数
        View layout = LayoutInflater.from(context).inflate(R.layout.risk_profile_table_item, null);//布局打气筒获取单行对象
        TextView name = layout.findViewById(R.id.option);
        TextView value = layout.findViewById(R.id.count);
        TextView statue = layout.findViewById(R.id.proportion);
        name.setText("选项");
        value.setText("小计");
        statue.setText("比例");
        name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        value.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        statue.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        name.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
        name.setGravity(Gravity.CENTER); //居中显示
        value.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
        value.setGravity(Gravity.CENTER); //居中显示
        statue.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
        statue.setGravity(Gravity.CENTER); //居中显示
        holder.tl.addView(layout);
        //得到此道题答题总人数
        int sumPerson = 0;
        for (int k = 0; k < list.size(); k++) {
            //循环每个人的答案
            for (int q = 0; q < list.get(k).getResults().size(); q++) {
                if (list.get(k).getResults().get(q).getQuestion_number() == questionnaire.getList().get(post).getOrder()) {
                    //人数加一
                    sumPerson++;
                }
            }
        }
            for (int i = 0; i < length; i++) {
                View layout1 = LayoutInflater.from(context).inflate(R.layout.risk_profile_table_item, null);//布局打气筒获取单行对象
                TextView name1 = layout1.findViewById(R.id.option);
                TextView value1 = layout1.findViewById(R.id.count);
                TextView statue1 = layout1.findViewById(R.id.proportion);
                //此选项选择人数
                int opNum = 0;
                for (int k1 = 0; k1 < list.size(); k1++) {
                    //循环每个人的答案
                    for (int q = 0; q < list.get(k1).getResults().size(); q++) {
                        if (list.get(k1).getResults().get(q).getQuestion_number() == questionnaire.getList().get(post).getOrder()) {
                            if (list.get(k1).getResults().get(q).getOrder_number() == (i + 1)) {
                                //选择此选项的人加1
                                opNum++;
                            }
                        }
                    }
                }

                name1.setText(questionnaire.getList().get(post).getOptions().get(i).getContent());
                value1.setText(opNum + "");
                DecimalFormat format = new DecimalFormat("0%");
                String s = format.format((Double.valueOf(opNum)/Double.valueOf(sumPerson))); // 56.79%
                if(sumPerson == 0){
                    statue1.setText(0+"");
                }else {
                    statue1.setText(s);
                }
                value1.setGravity(Gravity.CENTER); //居中显示
                name1.setGravity(Gravity.CENTER); //居中显示
                statue1.setGravity(Gravity.CENTER); //居中显示
                holder.tl.addView(layout1);//将这一行加入表格中
            }
            View layout2 = LayoutInflater.from(context).inflate(R.layout.risk_profile_table_item, null);//布局打气筒获取单行对象
            TextView name2 = layout2.findViewById(R.id.option);
            TextView value2 = layout2.findViewById(R.id.count);
            TextView statue2 = layout2.findViewById(R.id.proportion);
            name2.setText("有效答题人数：");
            value2.setText(sumPerson + "");
            statue2.setText("");
            name2.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
            value2.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
            statue2.setBackground(context.getDrawable(R.drawable.sw_bg_shape)); //背景色
            name2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            value2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            statue2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            holder.tl.addView(layout2);//将这一行加入表格中
        }
    /**
     * 下移按钮的监听接口
     */
    public interface onItemDownMoveListener {
        void onDownMoveClick(int position,View view);
    }

    private onItemDownMoveListener mOnItemDownMoveListener;

    public void setmOnItemDownMoveClickListener(onItemDownMoveListener mOnItemDownMoveListener) {
        this.mOnItemDownMoveListener  = mOnItemDownMoveListener;
    }
    private void setYAxis(int sumPerson) {
        //不显示右侧的y轴
        holder.mybarchart.getAxisRight().setEnabled(false);
        //得到左侧的y轴对象
        YAxis yAxis = holder.mybarchart.getAxisLeft();
        //是否绘制最上面的那个标签
        yAxis.setDrawTopYLabelEntry(false);

        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(sumPerson);
        //设置标签是绘制在图形内部还是外部， YAxisLabelPosition枚举值
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置靠近y轴第一条线的宽度
        yAxis.setAxisLineWidth(3f);
        //设置靠近y轴第一条线的颜色
//        yAxis.setAxisLineColor(Color.RED);
//
//        yAxis.setGridColor(Color.RED);
        //设置横向网格线的宽度
//        yAxis.setGridLineWidth(2f);

        //设置y轴在x方向上的偏移量
        yAxis.setXOffset(15f);
        //设置y轴的字体大小
        yAxis.setTextSize(14f);
        //设置y轴的字体颜色
        yAxis.setTextColor(Color.BLACK);
        //设置y轴字体的类型，如加粗等
        yAxis.setTypeface(Typeface.DEFAULT_BOLD);
        //自定义样式
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "人";
            }
        });
    }

    private void setXAxis(int num) {
        XAxis xAxis = holder.mybarchart.getXAxis();
        //设置x轴是显示在顶部还是底部，柱状图内还是外
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置文字旋转的角度
        xAxis.setLabelRotationAngle(60);
        //设置最小值，可通过该属性设置与左边的距离
        xAxis.setAxisMinimum(-0.5f);
        //设置最大值
        xAxis.setAxisMaximum(num-1);
        xAxis.setLabelCount(num-1);
        //设置靠近x轴第一条线的颜色
//        xAxis.setAxisLineColor(Color.RED);
        //设置绘制靠近x轴的第一条线的宽度
        xAxis.setAxisLineWidth(3f);
        //是否绘制靠近x轴的第一条线,不受setDrawGridLines属性影响
//        xAxis.setDrawAxisLine(false);
        //是否绘制竖向的网格
        xAxis.setDrawGridLines(false);

        //自定义格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int tep = (int) (value + 1);
                return "第" + tep + "选项";
            }
        });
        //设置x轴在y方向上的偏移量
        xAxis.setYOffset(10f);

        //设置x轴字体大小
        xAxis.setTextSize(16f);
        //设置x轴字体颜色
//        xAxis.setTextColor(Color.GREEN);

        //设置间隔
        xAxis.setGranularity(1);
    }


    private void setLegend() {
        //得到Legend对象
        Legend legend = holder.mybarchart.getLegend();
        //设置字体大小
        legend.setTextSize(18f);
        //设置排列方式
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置图例的大小
        legend.setFormSize(15f);
    }

    public void setDesc(){
        //得到Description对象
        Description description = holder.mybarchart.getDescription();
        //设置文字
        description.setText("这是柱状图统计结果");
        //设置文字大小
        description.setTextSize(18f);
        //设置偏移量

        // 获取屏幕中间x 轴的像素坐标
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float x = dm.widthPixels / 2;

        description.setPosition(x,50);
        //设置字体颜色
        description.setTextColor(context.getResources().getColor(R.color.colorMain));
        //设置字体加粗
        description.setTypeface(Typeface.DEFAULT_BOLD);

    }

    private void loadData(Map<Integer,Integer> data) {
        //为了看到更明显的效果，我这里设置了图形在上下左右四个方向上的偏移量
//        holder.mybarchart.setExtraTopOffset(25);
//        holder.mybarchart.setExtraLeftOffset(30);
//        holder.mybarchart.setExtraRightOffset(100);
//        holder.mybarchart.setExtraBottomOffset(50);

        List<BarEntry> barEntries = new ArrayList<BarEntry>();
        if(data!=null){
            for (Integer key : data.keySet()) {
                barEntries.add(new BarEntry(key,data.get(key)));
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"标题一");

        //设置柱状图的边框颜色
        barDataSet.setBarBorderColor(context.getResources().getColor(R.color.colorMain));
        //设置柱状图的边框大小，默认0
//        barDataSet.setBarBorderWidth(5f);


        //依次设置每次柱块的颜色,int... 类型
        barDataSet.setColors(context.getResources().getColor(R.color.colorMain));
        //设置住块上文字的颜色
        barDataSet.setValueTextColor(Color.BLACK);
        //设置住块上文字的大小
        barDataSet.setValueTextSize(16f);
        //设置住块上文字的类型，如加粗
        barDataSet.setValueTypeface(Typeface.DEFAULT);
        //设置柱块上文字的格式
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value+"";
            }
        });




        //设置高亮的颜色，效果不是很明显
//        barDataSet.setHighLightColor(Color.BLACK);

        //设置柱状图的阴影
//        barDataSet.setBarShadowColor(Color.RED);

        //设置x和y两个方向的动画，也可以单独设置x和y方向上的动画，但同时设置效果更好看
        holder.mybarchart.animateXY(1500,1500);
        holder.mybarchart.setDrawGridBackground(true);
        //是否绘制高亮，效果不大
        holder.mybarchart.setHighlightFullBarEnabled(false);
        //是否处理超出的柱块（这是个非常重要的属性，有时候可以帮助我们自动处理超出的柱块）
        holder.mybarchart.setFitBars(true);
        //是否将柱块上的文字绘制在柱块的上面
        holder.mybarchart.setDrawValueAboveBar(true);
        //是否绘制阴影
//        bc.setDrawBarShadow(true);

        //是否支持缩放
//        bc.setScaleEnabled(false);

        BarData ba = new BarData(barDataSet);

        //设置每个柱块的宽度，取值0-1
        ba.setBarWidth(0.2f);

        //设置一组柱块与一组柱块之间的距离，当每组只有一个柱块时将会报错
//        ba.groupBars();

        holder.mybarchart.setData(ba);
    }
}
