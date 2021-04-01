package com.yanzhen.entityylx;

public class ResultInfo {

	private int id;//id
	private int question_number;//题号
	private int order_number;//选的第几个
	private String result;//具体大体内容
	private int user_id;//答题的用户
	
	
	
	public ResultInfo() {
		super();
	}

	public ResultInfo(int id, int question_number, int order_number, String result) {
		super();
		this.id = id;
		this.question_number = question_number;
		this.order_number = order_number;
		this.result = result;
	}

	public ResultInfo(int id, int question_number, int order_number, String result, int user_id) {
		super();
		this.id = id;
		this.question_number = question_number;
		this.order_number = order_number;
		this.result = result;
		this.user_id = user_id;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getQuestion_number() {
		return question_number;
	}



	public void setQuestion_number(int question_number) {
		this.question_number = question_number;
	}



	public int getOrder_number() {
		return order_number;
	}



	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}



	public String getResult() {
		return result;
	}



	public void setResult(String result) {
		this.result = result;
	}



	public int getUser_id() {
		return user_id;
	}



	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}



	@Override
	public String toString() {
		return "ResultInfo [id=" + id + ", question_number=" + question_number + ", order_number=" + order_number
				+ ", result=" + result + ", user_id=" + user_id + "]";
	}
	
	
}
