package org.turings.investigationapplicqation.Entity;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class QuestionResult {

    private int order;
    private Map<Integer,String> result = new HashMap<>();

    public QuestionResult() {
    }

    public QuestionResult(int order, Map<Integer, String> result) {
        this.order = order;
        this.result = result;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Map<Integer, String> getResult() {
        return result;
    }

    public void setResult(Map<Integer, String> result) {
        this.result = result;
    }
}
