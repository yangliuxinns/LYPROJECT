package com.yanzhen.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
			if(question.getType().equals("填空题") || question.getType().equals("姓名") || question.getType().equals("日期") || question.getType().equals("手机") || question.getType().equals("地区") || question.getType().equals("地图"))  {
				System.out.println("题目类型："+question.getType());
				String re = request.getParameter(question.getOrder()+"");
				System.out.println("第"+question.getOrder()+"答案："+ re);
				ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),0,re);
				list.add(resultInfo);
			}else if(question.getType().equals("单选题") || question.getType().equals("性别")) {
				String re = request.getParameter("option"+question.getOrder());
				System.out.print("选项内容："+re);
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
		//记得ip
		result.setIp("");
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
	//根据问卷id，搜索问卷的答案
	@RequestMapping(value = "/findResultByQuestionaireId",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findResultByQuestionaireId(@RequestParam(value = "qId") int id) {
		List<Result> results = androidService.findResultByQuestionaireId(id);
		for(Result result : results) {
			System.out.println("问卷结果"+result.toString());
			for(ResultInfo resultInfo:result.getResults()) {
				System.out.println("详细结果"+resultInfo.toString());
			}
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String str = gson.toJson(results);
		return str;
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
	//根据id搜索发布的问卷
	@RequestMapping(value = "/findQuestionaresByUserIdPublish",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserIdPublish(@RequestParam(value = "uId") int id) {
		List<Questionnaire> questionnaire = androidService.findQuestionnaireByUserIdPublish(id);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//根据id搜索删除的问卷
	@RequestMapping(value = "/findQuestionaresByUserIdDelete",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserIdDelete(@RequestParam(value = "uId") int id) {
		List<Questionnaire> questionnaire = androidService.findQuestionaresByUserIdDelete(id);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//根据标题搜索问卷
	@RequestMapping(value = "/findQuestionaresByUserIdAndTitle",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresByUserIdAndTitle(@RequestParam(value = "uId") int id,@RequestParam(value = "title") String title) {
		List<Questionnaire> questionnaire1 = androidService.findQuestionaresByUserIdAndTitle(id,title);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire1);
		return subjectMsgList;
	}
	//根据id搜索问卷
	@RequestMapping(value = "/findQuestionaresById",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionaresById(@RequestParam(value = "uId") int id) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(id);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	//预览问卷之前
	@GetMapping("/prepreview/{id}")
	public String prePreViewQuestionares(@PathVariable("id") Integer id,ModelMap modelMap,HttpServletRequest request) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(id);
		if(questionnaire.getAppearance() == null || questionnaire.getAppearance().equals("")) {
			for(Question qu:questionnaire.getList()){
	            for(int i=0;i<qu.getOptions().size();i++){
	                if(qu.getOptions().get(i).getImgcontent() != null){
	                    OutputStream os;
						try {
							os = new FileOutputStream("F://proImg/"+qu.getOptions().get(i).getId()+questionnaire.getId()+".jpeg");
							 os.write(qu.getOptions().get(i).getImgcontent(),0,qu.getOptions().get(i).getImgcontent().length);
			                 os.flush();
			                 os.close(); 
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            }
	        }
			modelMap.addAttribute("questionnaire",questionnaire);
			return "androidPreView";
		}else {
			//只传递id
			modelMap.addAttribute("questionnaire",questionnaire);
			return "openFile";
		}
	}
	//预览问卷
	@GetMapping("/preview/{id}")
	public String preViewQuestionares(@PathVariable("id") Integer id,ModelMap modelMap,HttpServletRequest request) {
		Questionnaire questionnaire = androidService.findQuestionnaireById(id);
		for(Question qu:questionnaire.getList()){
            for(int i=0;i<qu.getOptions().size();i++){
                if(qu.getOptions().get(i).getImgcontent() != null){
                    OutputStream os;
					try {
						os = new FileOutputStream("F://proImg/"+qu.getOptions().get(i).getId()+questionnaire.getId()+".jpeg");
						 os.write(qu.getOptions().get(i).getImgcontent(),0,qu.getOptions().get(i).getImgcontent().length);
		                 os.flush();
		                 os.close(); 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
		modelMap.addAttribute("questionnaire",questionnaire);
		return "androidPreView";
	}
	//获取图片
	@RequestMapping(value = "/seekExperts",produces="text/json;charset=utf-8")
	@ResponseBody
	public String createFolw(@RequestParam(value = "id") int id,@RequestParam(value = "pId") int pid,
			HttpServletResponse response) {
		System.out.println("进来啦恶魔"+id+pid);
		 response.setContentType("image/*");
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			fis = new FileInputStream("F://proImg/"+id+pid+".jpeg");
			os = response.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((count = fis.read(buffer)) != -1) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fis.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
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
	/*
	 * 修改密码
	 * 
	 */
	@RequestMapping(value = "/fixMsd",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixMsd(@RequestParam(value = "psd")String psd,@RequestParam(value = "nPsd")String nPsd,@RequestParam(value = "userId")String userId) {
		int id = Integer.valueOf(userId);
		String string = androidService.fixMsd(psd,nPsd,id);
		return string.toString();
	}
	/**
	 * 修改名字
	 */
	@RequestMapping(value = "/fixName",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixName(@RequestParam(value = "name")String name,@RequestParam(value = "userId")String userId) {
		int id = Integer.valueOf(userId);
		String string = androidService.fixName(name,id);
		return string.toString();
	}
	/**
	 * 修改头像
	 */
	@RequestMapping(value = "/fixHead",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixHead(@RequestParam(value = "uri")String uri,@RequestParam(value = "userId")String userId) {
		int id = Integer.valueOf(userId);
		String string = androidService.fixHead(uri,id);
		return string.toString();
	}
	/**
	 * 修改手机号
	 */
	@RequestMapping(value = "/fixPhone",produces="text/json;charset=utf-8")
	@ResponseBody
	public String fixPhone(@RequestParam(value = "name")String phone,@RequestParam(value = "userId")String userId) {
		int id = Integer.valueOf(userId);
		String string = androidService.fixPhone(phone,id);
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
	
	/**
	 * 查找用户信息
	 */
	@RequestMapping(value = "/findUser",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findUser(@RequestParam(value = "userId")String id) {
		User user = androidService.findUser(Integer.parseInt(id));
		if(user != null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
			String userstr = gson.toJson(user);
			return userstr;
		}else {
			return "不存在用户".toString();
		}
	}
	/**
	 * 恢复功能
	 * 根据问卷id，更改问卷属性到草稿箱
	 */
	@RequestMapping(value = "/revertQuestionaire",produces="text/json;charset=utf-8")
	@ResponseBody
	public String revertQuestionaire(@RequestParam(value = "ids") String ids) {
		Gson gson = new Gson();
	    List<Integer> obj = gson.fromJson(ids,new TypeToken<List<Integer>>(){}.getType());
	    int n = androidService.revertQuestionaire(obj);
	    if(n>0) {
			return "恢复成功";
		}else {
			return null;
		}
	}
	
	/**
	 * 删除功能
	 * 彻底删除问卷
	 */
	@RequestMapping(value = "/deleteQuestionaire",produces="text/json;charset=utf-8")
	@ResponseBody
	public String deleteQuestionaire(@RequestParam(value = "ids") String ids) {
		Gson gson = new Gson();
	    List<Integer> obj = gson.fromJson(ids,new TypeToken<List<Integer>>(){}.getType());
	    int n = androidService.deleteQuestionaire(obj);
	    if(n>0) {
			return "删除成功";
		}else {
			return null;
		}
	}
	//搜索所有题目
	@RequestMapping(value = "/findQuestionBank",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionBank(@RequestParam(value = "uId") int id) {
		List<Question> list = androidService.findQuestionBank();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(list);
		return subjectMsgList;
	}
	//条件搜索所有题目
	@RequestMapping(value = "/findQuestionBankByTitle",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQuestionBankByTitle(@RequestParam(value = "str")String str) {
		List<Question> list = androidService.findQuestionBankByTitle(str);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String subjectMsgList = gson.toJson(list);
		return subjectMsgList;
	}
	
	//根据条件和id搜索问卷
	@RequestMapping(value = "/findQsByTitle",produces="text/json;charset=utf-8")
	@ResponseBody
	public String findQsByTitle(@RequestParam(value = "uId") int id,@RequestParam(value = "str")String str) {
		List<Questionnaire> questionnaire = androidService.findQsByTitle(id,str);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		for(Questionnaire q : questionnaire) {
			System.out.println("是否发布"+q.isIsRelease());
		}
		String subjectMsgList = gson.toJson(questionnaire);
		return subjectMsgList;
	}
	
}
