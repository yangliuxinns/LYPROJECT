package com.yanzhen.entityylx;

public class Options {

	private int id;
    private String content;
    private String img;

    public Options() {
    }

    public Options(int id, String content, String img) {
        this.id = id;
        this.content = content;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Options{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
