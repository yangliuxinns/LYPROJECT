package com.yanzhen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanzhen.entityylx.Options;
import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;
import com.yanzhen.mapper.AndroidQuestionaresDao;

@Service
@Transactional
public class AndroidService {

	@Autowired
	private AndroidQuestionaresDao androidQuestionaresDao;
	
	//添加问卷
	
	public void addQuestionares(Questionnaire questionnaire) {
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
		return androidQuestionaresDao.findQuestionaresByUserIdAndTitle(uid, title);
	}
}
