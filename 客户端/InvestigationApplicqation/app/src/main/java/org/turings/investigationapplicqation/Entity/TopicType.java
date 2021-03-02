package org.turings.investigationapplicqation.Entity;

import java.io.Serializable;

public class TopicType implements Serializable {
    private String textName;
    private String iName;

    public TopicType() {
    }

    public TopicType(String textName, String iName) {
        this.textName = textName;
        this.iName = iName;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
