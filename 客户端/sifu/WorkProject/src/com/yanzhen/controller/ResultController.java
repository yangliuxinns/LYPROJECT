package com.yanzhen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/piano")
public class ResultController {

	//提交答案
	@RequestMapping(value = "/saveInfo",produces="text/json;charset=utf-8")
	public String saveInfo(HttpServletRequest request) {
		System.out.println("进入了---------------------------------------------------------------");
//		Questionnaire questionnaire = androidService.findQuestionnaireById(id);
//		//创建存储
//		System.out.println("哈哈哈");
//		Result result = new Result();
//		List<ResultInfo> list = new ArrayList<ResultInfo>();//放答案细节 
//		for(int i=0 ;i<questionnaire.getList().size();i++) {
//			//循环所有题目
//			Question question = questionnaire.getList().get(i);
//			if(question.getType().equals("填空题") || question.getType().equals("姓名") || question.getType().equals("日期") || question.getType().equals("手机号") || question.getType().equals("地区"))  {
//				String re = request.getParameter(question.getOrder()+"");
//				System.out.println("第"+question.getOrder()+"答案："+ re);
//				ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),0,re);
//				list.add(resultInfo);
//			}else if(question.getType().equals("单选题") || question.getType().equals("性别")) {
//				String re = request.getParameter("option"+question.getOrder());
//				for(int k=0;k<question.getOptions().size();k++) {
//					if(re.equals(question.getOptions().get(k).getContent())) {
//						if(question.getOptions().get(k).getImg().equals("sr")) {
//							String r = request.getParameter("demo"+k+1);
//							System.out.print("第"+question.getOrder()+"答案："+ r);
//							ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,r);
//							list.add(resultInfo);
//						}else {
//							ResultInfo resultInfo = new ResultInfo(0,question.getOrder(),k+1,re);
//							list.add(resultInfo);
//						}
//					}
//				}
//			}else if(question.getType().equals("多选题")) {
//				
//			}
//		}
		return "NewFile.jsp";
	}
}
