package org.turings.investigationapplicqation.Entity;

import java.io.Serializable;
import java.util.List;

public class TopicBigType implements Serializable {

    private String textTopic;
    private List<TopicType> list;

    public TopicBigType() {
    }

    public TopicBigType(String textTopic, List<TopicType> list) {
        this.textTopic = textTopic;
        this.list = list;
    }

    public String getTextTopic() {
        return textTopic;
    }

    public void setTextTopic(String textTopic) {
        this.textTopic = textTopic;
    }

    public List<TopicType> getList() {
        return list;
    }

    public void setList(List<TopicType> list) {
        this.list = list;
    }
}
