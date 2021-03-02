package com.yanzhen.entity;

import com.yanzhen.utils.Entity;

import java.util.Date;

/**
 * 
 * @author 596183363@qq.com
 * @time 2020-06-09 10:18:04
 */
public class Admin extends Entity {

	/**
	 * 
	 */
	private String account;
	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String password;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String remark;

	private Date now;

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}
}