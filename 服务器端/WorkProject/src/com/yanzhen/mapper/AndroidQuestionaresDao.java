package com.yanzhen.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;

public interface AndroidQuestionaresDao {

	//增加问卷
	public int insertQuestionare(Questionnaire questionnaire);
	//题库增加问题
	public int insertQuestion(Question question);
	//题目若为选择题增加选项
	public void insertOptions(@Param("order1")int order,@Param("content1")String content,@Param("qId1")int qId,@Param("img1")String img);
	//将题目填入关联表
	public void insertAssociation(@Param("qsid")int qsid,@Param("qid")int qid,@Param("Is_required")Boolean Is_required,@Param("order1")int order);

	//根据id搜索问卷
	public Questionnaire findQuestionareById(@Param("qsid")int qsid);
	//根据Uid查问卷
	public List<Questionnaire> findQuestionareByUserId(@Param("uid")int uid);
	//根据Uid查问卷
	public List<Questionnaire> findQuestionareByUserIdPublish(@Param("uid")int uid);
	//根据标题搜索问卷
	public List<Questionnaire> findQuestionaresByUserIdAndTitle(@Param("uid")int uid,@Param("title")String title);
	//删除关系
	public void deleteRelationById(@Param("qsId")int qsId);
	//更新问卷表
	public void updateQuestionareById(Questionnaire questionnaire);
	//更改问卷表
	public void updateQuestionareDelete(Questionnaire questionnaire);
}
