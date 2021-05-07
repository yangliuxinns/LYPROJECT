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
import com.yanzhen.mapper.AndroidQuestionMapper;
import com.yanzhen.mapper.AndroidQuestionaresDao;

@Service
@Transactional
public class AndroidService {

	@Autowired
	private AndroidQuestionaresDao androidQuestionaresDao;
	@Autowired
	private AndroidQuestionMapper questionMapper;
	
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
						androidQuestionaresDao.insertOptions(k+1,options.getContent(),question.getId(),options.getImg(),options.getImgcontent());
					}
				}
				androidQuestionaresDao.insertAssociation(questionnaire.getId(), question.getId(),question.getRequired(),question.getOrder(),question.getPageNumber());
			}
			System.out.println("返回的问卷id"+qsId);
			return questionnaire.getId();
		}else {
			System.out.println("应该先更新");
			//更新
			//删除关联表
			int n = androidQuestionaresDao.deleteRelationById(questionnaire.getId());
			if(n>0) {
				System.out.println("删除成功呢");
				//更新问卷表
				androidQuestionaresDao.updateQuestionareById(questionnaire);
				System.out.println("问卷的id"+questionnaire.getId());
				//添加题目
				for(int i= 0;i<questionnaire.getList().size();i++) {
					//遍历存储题目
					Question question = questionnaire.getList().get(i);
					question.setId(0);
					int qid = androidQuestionaresDao.insertQuestion(question);
					if(question.getType().equals("单选题") || question.getType().equals("多选题") ||question.getType().equals("性别")) {
						for(int k = 0;k<question.getOptions().size();k++) {
							Options options = question.getOptions().get(k);
							androidQuestionaresDao.insertOptions(k+1,options.getContent(),question.getId(),options.getImg(),options.getImgcontent());
						}
					}
					androidQuestionaresDao.insertAssociation(questionnaire.getId(), question.getId(),question.getRequired(),question.getOrder(),question.getPageNumber());
				}
			}else {
				System.out.println("删除失败");
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

	//根据id搜索问卷
	public List<Questionnaire> findQuestionaresByUserIdDelete(int id) {
		return androidQuestionaresDao.findQuestionaresByUserIdDelete(id);
	}

	//根据id恢复问卷
	public int revertQuestionaire(List<Integer> obj) {
		return androidQuestionaresDao.revertQuestionaire(obj);
	}
	

	//根据id彻底删除问卷
	public int deleteQuestionaire(List<Integer> obj) {
		int n = androidQuestionaresDao.deleteQuestionaireAndQuestion(obj);
		if(n>0) {
			return androidQuestionaresDao.deleteQuestionaire(obj);
		}else {
			return n;
		}
	}

	//搜索问卷
	public List<Question> findQuestionBank() {
		return questionMapper.findQuestion();
	}

	//条件搜索问卷
	public List<Question> findQuestionBankByTitle(String str) {
		return questionMapper.findQuestionBankByTitle(str);
	}
	

	//按照id和条件搜索问卷
	public List<Questionnaire> findQsByTitle(int id, String str) {
		return androidQuestionaresDao.findQsByTitle(id,str);
	}

	//修改名字
	public String fixName(String name, int id) {
		int k =  androidQuestionaresDao.fixName(name,id);
		if(k>0) {
			return name;
		}else {
			return "修改失败，请重试";
		}
	}

	//查找User
	public User findUser(int parseInt) {
		return androidQuestionaresDao.findUserById(parseInt);
	}
	

	//修改手机号
	public String fixPhone(String phone, int id) {
		int k =  androidQuestionaresDao.fixPhone(phone,id);
		if(k>0) {
			return phone;
		}else {
			return "修改失败，请重试";
		}
	}

	//按照id修改密码
	public String fixMsd(String psd,String nPsd, int id) {
		//判断原密码是否正确
		User user = androidQuestionaresDao.findUserById(id);
		if(user.getPassword().equals(nPsd)) {
			//相同修改
			int n = androidQuestionaresDao.fixMsd(psd,id);
			if(n>0) {
				return "修改成功";
			}else {
				return "修改失败";
			}
		}else {
			//不修改
			return "原密码错误，不能修改";
		}
		
	}
	//修改头像
	public String fixHead(String uri, int id) {
		int k =  androidQuestionaresDao.fixHead(uri,id);
		if(k>0) {
			return uri;
		}else {
			return "修改失败，请重试";
		}
	}

	//搜索答案
	public List<Result> findResultByQuestionaireId(int id) {
		//搜索所有回答
		List<Result> results = androidQuestionaresDao.findResultByQuestionaireId(id);
		return results;
	}

	//搜索是否访问过
	public Result findIp(String remoteAddr, int i) {
		return androidQuestionaresDao.findIp(remoteAddr,i);
	}

	/**
	 * 
	 * @Title: cancellation
	 * @Description: 注销账户
	 * @param @param id
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	public String cancellation(int id) {
		//删除收集的数据
		//1、查询名下所有问卷
		List<Integer> ids = androidQuestionaresDao.findQIdsByUserId(id);
		//2、搜索答卷id
		List<Integer> aIds = androidQuestionaresDao.findAIdsByQIds(ids);
		//3、删除详细答案
		androidQuestionaresDao.deleteDetailResultById(aIds);
		//4、删除答卷
		androidQuestionaresDao.deleteResultById(aIds);
		//5、删除问卷
		androidQuestionaresDao.deleteQuestionaireAndQuestion(ids);
		androidQuestionaresDao.deleteQuestionaire(ids);
		//6、删除用户
		int n = androidQuestionaresDao.deleteUserById(id);
		if(n>0) {
			return "注销成功";
		}else {
			return "注销失败";
		}
	}
}
