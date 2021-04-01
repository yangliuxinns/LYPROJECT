package com.yanzhen.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.tools.internal.ws.processor.model.Model;
import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;
import com.yanzhen.entityylx.Result;
import com.yanzhen.entityylx.ResultInfo;
import com.yanzhen.entityylx.User;
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
	public String saveQuestionares(HttpServletRequest request) {
		InputStream in;
		int id=0;
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		try {
			in = request.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in, "utf-8"));
			String subjectJson = br.readLine();
			in.close();
			br.close();
			Questionnaire qs = gson.fromJson(subjectJson, Questionnaire.class);
			id = androidService.addQuestionares(qs);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gson.toJson(id);
	
	}
	@PostMapping("/s")
	public void a(HttpServletRequest request) {
		
	}
	//提交答案
	@RequestMapping(value = "/saveInfo/{id}",method = RequestMethod.POST)
	public String saveInfo(@PathVariable("id") Integer id,HttpServletRequest request,
	           HttpServletResponse respnose) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(id);
		//创建存储
		Result result = new Result();
		List<ResultInfo> list = new ArrayList<ResultInfo>();//放答案细节 
		for(int i=0 ;i<questionnaire.getList().size();i++) {
			//循环所有题目
			Question question = questionnaire.getList().get(i);
			if(question.getType().equals("填空题") || question.getType().equals("姓名") || question.getType().equals("日期") || question.getType().equals("手机号") || question.getType().equals("地区"))  {
				String re = request.getParameter(question.getOrder()+"");
				System.out.println("第"+question.getOrder()+"答案："+ re);
				ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),0,re);
				list.add(resultInfo);
			}else if(question.getType().equals("单选题") || question.getType().equals("性别")) {
				String re = request.getParameter("option"+question.getOrder());
				for(int k=0;k<question.getOptions().size();k++) {
					if(re.equals(question.getOptions().get(k).getContent())) {
						if(question.getOptions().get(k).getImg().equals("sr")) {
							String r = request.getParameter("demo"+(k+1));
							System.out.print("第"+question.getOrder()+"答案："+ r);
							ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,r);
							list.add(resultInfo);
						}else {
							ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,re);
							list.add(resultInfo);
							System.out.print("第"+question.getOrder()+"答案："+ re);
						}
					}
				}
			}else if(question.getType().equals("多选题")) {
				String[] checkbox= request.getParameterValues("option"+question.getOrder()); 
				for(String aString : checkbox) {
					for(int k=0;k<question.getOptions().size();k++) {
						if(aString.equals(question.getOptions().get(k).getContent())) {
							if(question.getOptions().get(k).getImg().equals("sr")) {
								String r = request.getParameter("demo"+(k+1));
								System.out.print("第"+question.getOrder()+"答案："+ r);
								ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,r);
								list.add(resultInfo);
							}else {
								ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,aString);
								list.add(resultInfo);
								System.out.print("第"+question.getOrder()+"答案："+ aString);
							}
						}
					}
				
				}
			}
		}
		result.setQuestionnatre_id(id);
		Date dd=new Date();
		result.setTime(dd);
		result.setResults(list);
		int k = androidService.saveAnswer(result);
		if(k>0) {
			return "NewFile";
		}else {
			return "Failed";
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
	//发布问卷，改变问卷状态
	@RequestMapping(value = "/fixQuestionaresRelease",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixQuestionaresRelease(@RequestParam(value = "uId") int id) {
		
		int n = androidService.fixQuestionaresRelease(id);
		if(n>0) {
			return "发布成功".toString();
		}else {
			return "发布失败".toString();
		}
	}
	//发布问卷，改变问卷状态
		@RequestMapping(value = "/fixQuestionaresDraf",produces="text/json;charset=utf-8")
		@ResponseBody
		public String fixQuestionaresDraf(@RequestParam(value = "uId") int id) {
			
			int n = androidService.fixQuestionaresDraf(id);
			if(n>0) {
				return "更改成功".toString();
			}else {
				return "更改失败".toString();
			}
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
	@GetMapping("/preview/{id}")
	public String preViewQuestionares(@PathVariable("id") Integer id,ModelMap modelMap) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(10);
		modelMap.addAttribute("questionnaire",questionnaire);
		return "androidPreView";
	}
	//注册（先搜索是否注册过，没有注册）
	@RequestMapping(value = "/register",produces="text/json;charset=utf-8")
	@ResponseBody
	public String register(@RequestParam(value = "phone") String phone,@RequestParam(value = "psd") String psd) {
		String string = androidService.register(phone, psd);
		return string.toString();
	}
	
	//登录
	@RequestMapping(value = "/login",produces="text/json;charset=utf-8")
	@ResponseBody
	public String login(@RequestParam(value = "phone")String phone,@RequestParam(value = "psd")String psd) {
		User user = androidService.login(phone, psd);
		if(user != null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
			String userstr = gson.toJson(user);
			return userstr;
		}else {
			return "用户名或密码不匹配".toString();
		}
	}
	/**
	 * 忘记密码
	 * 检查账号知否注册过
	 * 注册过修改
	 */
	@RequestMapping(value = "/fixPsd",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixPsd(@RequestParam(value = "phone")String phone,@RequestParam(value = "psd")String psd) {
		String string = androidService.fixPsd(phone, psd);
		return string.toString();
	}
	/**
	 * 短信登录
	 * 查找手机账号
	 */
	@RequestMapping(value = "/findPhone",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findPhone(@RequestParam(value = "phone")String phone) {
		User user = androidService.findPhone(phone);
		if(user != null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
			String userstr = gson.toJson(user);
			return userstr;
		}else {
			return "不存在用户".toString();
		}
	}

}
