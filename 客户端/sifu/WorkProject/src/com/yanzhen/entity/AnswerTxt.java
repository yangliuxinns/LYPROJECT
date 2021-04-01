package com.yanzhen.entity;

import java.util.Date;

/**
 * 
 * @author 596183363@qq.com
 * @time 2020-06-09 10:18:05
 */
public class AnswerTxt{

	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	private Integer questionId;
	/**
	 * 
	 */
	private String result;
	/**
	 * 
	 */
	private Integer surveyId;
	/**
	 * 
	 */
	private String voter;

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}
	public String getVoter() {
		return voter;
	}
	public void setVoter(String voter) {
		this.voter = voter;
	}
}