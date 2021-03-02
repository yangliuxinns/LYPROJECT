package org.turings.investigationapplicqation.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Question implements Serializable {

    private int id;
    private int order;//题号
    private String title;//题干
    private String type;//题型
    private List<Options> options;//选项
    private Boolean isRequired;//是否必填
    private int PageNumber;//分页

    public Question() {
    }

    public Question(int id, int order, String title, String type, List<Options> options, Boolean isRequired, int pageNumber) {
        this.id = id;
        this.order = order;
        this.title = title;
        this.type = type;
        this.options = options;
        this.isRequired = isRequired;
        PageNumber = pageNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", order=" + order +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", options=" + options.toString() +
                ", isRequired=" + isRequired +
                ", PageNumber=" + PageNumber +
                '}';
    }
}
