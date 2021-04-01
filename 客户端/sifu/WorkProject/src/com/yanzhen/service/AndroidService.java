package com.yanzhen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanzhen.entityylx.Options;
import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;
import com.yanzhen.entityylx.Result;
import com.yanzhen.entityylx.ResultInfo;
import com.yanzhen.entityylx.User;
import com.yanzhen.mapper.AndroidQuestionaresDao;

@Service
@Transactional
public class AndroidService {

	@Autowired
	private AndroidQuestionaresDao androidQuestionaresDao;
	
	//添加问卷
	
	public int addQuestionares(Questionnaire questionnaire) {
		//保存之前查是否存在该问卷
		if(questionnaire.getId() == 0) {
			int qsId = androidQuestionaresDao.insertQuestionare(questionnaire);
			
			for(int i= 0;i<questionnaire.getList().size();i++) {
				//遍历存储题目
				Question question = questionnaire.getList().get(i);
				int qid = androidQuestionaresDao.insertQuestion(question);
				if(question.getType().equals("单选题") || question.getType().equals("多选题") ||question.getType().equals("性别")) {
					for(int k = 0;k<question.getOptions().size();k++) {
						Options options = question.getOptions().get(k);
						androidQuestionaresDao.insertOptions(k+1,options.getContent(),question.getId(),options.getImg());
					}
				}
				androidQuestionaresDao.insertAssociation(questionnaire.getId(), question.getId(),question.getRequired(),question.getOrder());
			}
			return qsId;
		}else {
			System.out.println("应该先更新");
			//更新
			//删除关联表
			androidQuestionaresDao.deleteRelationById(questionnaire.getId());
			//更新问卷表
			androidQuestionaresDao.updateQuestionareById(questionnaire);
			//添加题目
			for(int i= 0;i<questionnaire.getList().size();i++) {
				//遍历存储题目
				Question question = questionnaire.getList().get(i);
				int qid = androidQuestionaresDao.insertQuestion(question);
				if(question.getType().equals("单选题") || question.getType().equals("多选题") ||question.getType().equals("性别")) {
					for(int k = 0;k<question.getOptions().size();k++) {
						Options options = question.getOptions().get(k);
						androidQuestionaresDao.insertOptions(k+1,options.getContent(),question.getId(),options.getImg());
					}
				}
				androidQuestionaresDao.insertAssociation(questionnaire.getId(), question.getId(),question.getRequired(),question.getOrder());
			}
			return questionnaire.getId();
		}
	}
	
	//根据id搜索试卷
	public Questionnaire findQuestionnaireById(int id) {
		
		return androidQuestionaresDao.findQuestionareById(id);
	}
	//草稿箱子
	public List<Questionnaire> findQuestionnaireByUserId(int id){
		return androidQuestionaresDao.findQuestionareByUserId(id);
	}
	//已经发布
	public List<Questionnaire> findQuestionnaireByUserIdPublish(int id){
		return androidQuestionaresDao.findQuestionareByUserIdPublish(id);
	}
	//更新删除状态
	public void updateQuestionaresDelete(Questionnaire questionnaire) {
		androidQuestionaresDao.updateQuestionareDelete(questionnaire);
	}
	//标题搜索问卷
	public List<Questionnaire> findQuestionaresByUserIdAndTitle(int id, String title) {
		// TODO Auto-generated method stub
		return androidQuestionaresDao.findQuestionaresByUserIdAndTitle(id, title);
	}
	//注册
	public String register(String phone,String psd) {
		//先判断是否注册过该账号
		User user = androidQuestionaresDao.findUserByPhone(phone);
		if(user == null) {
			//可以注册
			int a = androidQuestionaresDao.saveUser(phone, psd);
			if(a>0) {
				//注册成功
				return "注册成功";
			}else {
				//注册失败
				return "注册失败，请重试";
			}
		}else {
			//已经注册过了
			return "已经注册过了不能注册" ;
		}
		//没有就注册
	}
	//登录
	public User login(String phone,String psd) {
		return androidQuestionaresDao.findUser(phone, psd);
	}
	//存储答案
	public int saveAnswer(Result result) {
		int i = androidQuestionaresDao.saveAnswer(result);
		if(i>0) {
			int k=0;
			for(int j=0;j<result.getResults().size();j++) {
				ResultInfo resultInfo = result.getResults().get(j);
				resultInfo.setUser_id(result.getId());
				k = androidQuestionaresDao.saveResult(resultInfo);
				if(k>0) {
					continue;
				}else {
					break;
				}
			}
			if(k>0) {
				return 1;
			}else {
				return 0;
			}
		}else {
			return 0;
		}
	}

	//修改密码
	public String fixPsd(String phone, String psd) {
		//先判断是否注册过该账号
		User user = androidQuestionaresDao.findUserByPhone(phone);
		if(user == null) {
			return "不存在用户";
		}else {
			//账号存在。去修改
			int k = androidQuestionaresDao.fixPsd(user.getId(),psd);
			if(k>0) {
				return "修改成功" ;
			}else {
				return "修改失败，请重试";
			}
		}
	}

	//查找账号
	public User findPhone(String phone) {
		//先判断是否注册过该账号
		User user = androidQuestionaresDao.findUserByPhone(phone);
		return user;
	}

	//修改问卷状态为发布
	public int fixQuestionaresRelease(int id) {
		return androidQuestionaresDao.fixQuestionaresRelease(id);
	}

	//修改问卷状态为草稿
	public int fixQuestionaresDraf(int id) {
		return androidQuestionaresDao.fixQuestionaresDraf(id);
	}
}
