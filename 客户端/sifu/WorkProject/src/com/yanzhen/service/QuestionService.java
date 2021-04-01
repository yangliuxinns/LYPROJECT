package com.yanzhen.service;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.yanzhen.entity.Question;
import com.yanzhen.entity.QuestionOpt;
import com.yanzhen.mapper.QuestionDao;
import com.yanzhen.mapper.QuestionOptDao;
import com.yanzhen.utils.BeanMapUtils;
import com.yanzhen.utils.MapParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionOptDao questionOptDao;


    /**
     * 如果问题保存之后在进行编辑，要把之前的这个问题删除，否则会最后问卷会出现相同问题，因为前端一点保存会直接写入数据库
    *<p>Title: create</p> 
    *<p>Description: </p> 
    　 * @param pi
    　 * @return
     */
    public int create(Question pi){
        int flag = 0;
        if(pi.getId() != null){
            flag = this.update(pi);
            questionOptDao.delete(MapParameter.getInstance().add("questionId",pi.getId()).getMap());
        }else{
            flag = questionDao.create(pi);
        }
        if(flag>0){
            List<QuestionOpt> options = pi.getOptions();
            int i = 0;
            if(options != null && options.size()>0){
                for (QuestionOpt option : options) {
                    option.setSurveyId(pi.getSurveyId());
                    option.setQuestionId(pi.getId());
                    option.setOrderby(++i);
                    questionOptDao.create(option);
                }
            }
        }
        return pi.getId();
    }

    public int deleteBatch(String ids){
        int flag = 0;
        List<String> list = Splitter.on(",").splitToList(ids);
        for (String s : list) {
            questionDao.delete(MapParameter.getInstance().addId(Integer.parseInt(s)).getMap());
            questionOptDao.delete(MapParameter.getInstance().add("questionId",Integer.parseInt(s)).getMap());
            flag++;
        }
        return flag;
    }

    public int delete(Integer id){
        return questionDao.delete(MapParameter.getInstance().addId(id).getMap());
    }

    public int update(Question question){
        Map<String, Object> map = MapParameter.getInstance().put(BeanMapUtils.beanToMapForUpdate(question)).addId(question.getId()).getMap();
        return questionDao.update(map);
    }

    public List<Question> query(Question question){
        //仅仅查询的问题
        Map<String, Object> map = MapParameter.getInstance().put(BeanMapUtils.beanToMap(question)).getMap();
        List<Question> questionList = questionDao.query(map);
        //查询选项
        List<QuestionOpt> optList = questionOptDao.query(MapParameter.getInstance().add("surveyId", question.getSurveyId()).getMap());
        for (Question question1 : questionList) {
            List<QuestionOpt> options = new ArrayList<>();
            for (QuestionOpt questionOpt : optList) {
                if(question1.getId() == questionOpt.getQuestionId().intValue()){
                    options.add(questionOpt);
                }
            }
            question1.setOptions(options);
        }
        return questionList;
    }

    public Question detail(Integer id){
        return questionDao.detail(MapParameter.getInstance().addId(id).getMap());
    }

    public int count(Question question){
        Map<String, Object> map = MapParameter.getInstance().put(BeanMapUtils.beanToMap(question)).getMap();
        return questionDao.count(map);
    }

}
