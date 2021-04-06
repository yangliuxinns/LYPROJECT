package com.yanzhen.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanzhen.entityylx.Question;
import com.yanzhen.entityylx.Questionnaire;
import com.yanzhen.entityylx.Result;
import com.yanzhen.entityylx.ResultInfo;
import com.yanzhen.entityylx.User;

public interface AndroidQuestionaresDao {

	//增加问卷
	public int insertQuestionare(Questionnaire questionnaire);
	//题库增加问题
	public int insertQuestion(Question question);
	//题目若为选择题增加选项
	public void insertOptions(@Param("order1")int order,@Param("content1")String content,@Param("qId1")int qId,@Param("img1")String img,@Param("imgcontent")byte[] imgcontent);
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
	public int deleteRelationById(@Param("qsId")int qsId);
	//更新问卷表
	public void updateQuestionareById(Questionnaire questionnaire);
	//更改问卷表
	public void updateQuestionareDelete(Questionnaire questionnaire);
	//判断是否注册过账号
	public User findUserByPhone(@Param("phone")String phone);
	//注册
	public int saveUser(@Param("phone")String phone,@Param("psd")String psd);
	//登录
	public User findUser(@Param("phone")String phone,@Param("psd")String psd);
	//存储答案
	public int saveAnswer(Result result);
	//存储答案细节
	public int saveResult(ResultInfo resultInfo);
	//忘记密码修改
	public int fixPsd(@Param("id")int id,@Param("psd")String psd);
	//修改发布状态
	public int fixQuestionaresRelease(@Param("id")int id);
	//修改草稿状态
	public int fixQuestionaresDraf(@Param("id")int id);
	//查找删除状态的问卷
	public List<Questionnaire> findQuestionaresByUserIdDelete(@Param("uid")int id);
	//根据id恢复问卷
	public int revertQuestionaire(List<Integer> obj);
	//根据id彻底删除问卷
	public int deleteQuestionaire(List<Integer> obj);
	//先删除关联表
	public int deleteQuestionaireAndQuestion(List<Integer> obj);
	//根据条件搜索问卷
	public List<Questionnaire> findQsByTitle(@Param("id")int id,@Param("title")String str);
}
