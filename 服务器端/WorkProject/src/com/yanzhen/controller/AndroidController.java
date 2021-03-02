package com.yanzhen.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.tools.internal.ws.processor.model.Model;
import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;
import com.yanzhen.service.AdminService;
import com.yanzhen.service.AndroidService;

@Controller
@RequestMapping("/ylx")
public class AndroidController {
	
	@Autowired
    private AndroidService androidService;
	//保存问卷
	@RequestMapping(value = "/saveQuestionares",produces="text/json;charset=utf-8")
	@ResponseBody
	public void saveQuestionares(HttpServletRequest request) {
		InputStream in;
		try {
			in = request.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in, "utf-8"));
			String subjectJson = br.readLine();
			in.close();
			br.close();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
			Questionnaire qs = gson.fromJson(subjectJson, Questionnaire.class);
			System.out.println("id是多少"+qs.getId());
			androidService.addQuestionares(qs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	//更新是否删除属性
	@RequestMapping(value = "/updateQuestionaresDelete",produces="text/json;charset=utf-8")
	@ResponseBody
	public void updateQuestionaresDelete(HttpServletRequest request) {
		InputStream in;
		try {
			in = request.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in, "utf-8"));
			String subjectJson = br.readLine();
			in.close();
			br.close();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
			Questionnaire qs = gson.fromJson(subjectJson, Questionnaire.class);
			androidService.updateQuestionaresDelete(qs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	//根据id搜索问卷
	@RequestMapping(value = "/findQuestionaresByUserId",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserId(@RequestParam(value = "uId") int id) {
		List<Questionnaire> questionnaire = androidService.findQuestionnaireByUserId(id);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//根据uid搜索每个人的草稿问卷
	//根据uid搜索每个人的发布问卷
	//根据id搜索发布的问卷
	@RequestMapping(value = "/findQuestionaresByUserIdPublish",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserIdPublish(@RequestParam(value = "uId") int id) {
		List<Questionnaire> questionnaire = androidService.findQuestionnaireByUserIdPublish(id);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//根据标题搜索问卷
	@RequestMapping(value = "/findQuestionaresByUserIdAndTitle",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserIdAndTitle(@RequestParam(value = "uId") int id,@RequestParam(value = "title") String title) {
		List<Questionnaire> questionnaire = androidService.findQuestionaresByUserIdAndTitle(id,title);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//预览问卷
	public void preViewQuestionares(HttpServletRequest request) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(10);
		
	}

}
