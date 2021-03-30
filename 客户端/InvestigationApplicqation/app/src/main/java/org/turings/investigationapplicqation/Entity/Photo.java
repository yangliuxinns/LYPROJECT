package org.turings.investigationapplicqation.Entity;

public class Photo {
    private String name;
    private String img;
    private int pos;

    public Photo() {
    }

    public Photo(String name, String img,int pos) {
        this.name = name;
        this.img = img;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
