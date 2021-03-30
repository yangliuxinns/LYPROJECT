package org.turings.investigationapplicqation.Entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;

//要展示的问题的细节
@SmartTable(name="回答详情")
public class ShowData implements Serializable {
    @SmartColumn(id =1,name = "排序")
    private int order;
    @SmartColumn(id=2,name="答案")
    private String answer;

    public ShowData() {
    }

    public ShowData(int order, String answer) {
        this.order = order;
        this.answer = answer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
