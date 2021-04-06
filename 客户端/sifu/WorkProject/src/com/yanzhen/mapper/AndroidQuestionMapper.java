package com.yanzhen.mapper;

import java.util.List;

import com.yanzhen.entityylx.Question;

public interface AndroidQuestionMapper {

	
	//查询Question
	
	public List<Question> findQuestionById(int uid);
	//查询全部Question
	public List<Question> findQuestion();
	//查询符合条件的Question
	public List<Question> findQuestionBankByTitle(String str);
}
