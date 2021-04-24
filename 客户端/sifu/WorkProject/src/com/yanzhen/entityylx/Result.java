package com.yanzhen.entityylx;

import java.util.Date;
import java.util.List;

public class Result {

	private int id;//答题者
	private int questionnatre_id;//问卷id
	private Date time;//答题时间
	private String ip;//地址ip
	private List<ResultInfo> results;//答案
	
	
	
	public Result() {
		super();
	}



	public Result(int id, int questionnatre_id, Date time, List<ResultInfo> results) {
		super();
		this.id = id;
		this.questionnatre_id = questionnatre_id;
		this.time = time;
		this.results = results;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getQuestionnatre_id() {
		return questionnatre_id;
	}



	public void setQuestionnatre_id(int questionnatre_id) {
		this.questionnatre_id = questionnatre_id;
	}



	public Date getTime() {
		return time;
	}



	public void setTime(Date time) {
		this.time = time;
	}



	public List<ResultInfo> getResults() {
		return results;
	}



	public void setResults(List<ResultInfo> results) {
		this.results = results;
	}


	

	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	@Override
	public String toString() {
		return "Result [id=" + id + ", questionnatre_id=" + questionnatre_id + ", time=" + time + ", ip=" + ip
				+ ", results=" + results + "]";
	}
	
	
}
