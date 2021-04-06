package com.yanzhen.entityylx;

import java.util.Arrays;

public class Options {

	private int id;
    private String content;
    private String img;
    private byte[] imgcontent;
    public Options() {
    }

    
    public Options(int id, String content, String img, byte[] imgcontent) {
		super();
		this.id = id;
		this.content = content;
		this.img = img;
		this.imgcontent = imgcontent;
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


	public byte[] getImgcontent() {
		return imgcontent;
	}


	public void setImgcontent(byte[] imgcontent) {
		this.imgcontent = imgcontent;
	}


	@Override
	public String toString() {
		return "Options [id=" + id + ", content=" + content + ", img=" + img + ", imgcontent="
				+ Arrays.toString(imgcontent) + "]";
	}


	
}
