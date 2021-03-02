package org.turings.investigationapplicqation.Entity;

import java.util.List;
import java.util.Map;

public class PeddleData {
    private int current;//所属页码
    private int size;//此页题量
    private List<Question> questions;//题目
    private List<QuestionResult> result;//答题结果
    public PeddleData() {
    }

    public PeddleData(int current, int size, List<Question> questions, List<QuestionResult> result) {
        this.current = current;
        this.size = size;
        this.questions = questions;
        this.result = result;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<QuestionResult> getResult() {
        return result;
    }

    public void setResult(List<QuestionResult> result) {
        this.result = result;
    }
}
