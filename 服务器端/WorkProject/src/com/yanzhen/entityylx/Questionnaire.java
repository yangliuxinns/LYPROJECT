package com.yanzhen.entityylx;

import java.util.Date;
import java.util.List;

public class Questionnaire{

	private int id;//问卷id
    private String title;//问卷标题
    private String instructions;//问卷说明
    private boolean IsRelease;//是否已经发布
    private List<Question> list;//所有题目
    private int totalPage;//总分页数目
    private Boolean onlyPhone;//一个手机只能回答一次
    private Boolean onlyWeixin;//仅限微信作答
    private Boolean isRecordWeixinInfo;//是否记录微信信息
    private String appearance;//记录外观
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private int user_id;//用户id
    private Boolean isDel;//是否删除
    public Questionnaire() {
    }

    
    public Questionnaire(int id, String title, String instructions, boolean isRelease, List<Question> list,
			int totalPage, Boolean onlyPhone, Boolean onlyWeixin, Boolean isRecordWeixinInfo, String appearance,
			Date startTime, Date endTime, int user_id,Boolean isDel) {
		super();
		this.id = id;
		this.title = title;
		this.instructions = instructions;
		IsRelease = isRelease;
		this.list = list;
		this.totalPage = totalPage;
		this.onlyPhone = onlyPhone;
		this.onlyWeixin = onlyWeixin;
		this.isRecordWeixinInfo = isRecordWeixinInfo;
		this.appearance = appearance;
		this.startTime = startTime;
		this.endTime = endTime;
		this.user_id = user_id;
		this.isDel = isDel;
	}


	public Questionnaire(int id, String title, String instructions, boolean isRelease, List<Question> list, int totalPage, Boolean onlyPhone, Boolean onlyWeixin, Boolean isRecordWeixinInfo, String appearance,Boolean isDel) {
        this.id = id;
        this.title = title;
        this.instructions = instructions;
        IsRelease = isRelease;
        this.list = list;
        this.totalPage = totalPage;
        this.onlyPhone = onlyPhone;
        this.onlyWeixin = onlyWeixin;
        this.isRecordWeixinInfo = isRecordWeixinInfo;
        this.appearance = appearance;
        this.isDel = isDel;
    }

    public Questionnaire(int id, String title, String instructions, boolean isRelease, List<Question> list, int totalPage, Boolean onlyPhone, Boolean onlyWeixin, Boolean isRecordWeixinInfo, String appearance, Date startTime, Date endTime) {
        this.id = id;
        this.title = title;
        this.instructions = instructions;
        IsRelease = isRelease;
        this.list = list;
        this.totalPage = totalPage;
        this.onlyPhone = onlyPhone;
        this.onlyWeixin = onlyWeixin;
        this.isRecordWeixinInfo = isRecordWeixinInfo;
        this.appearance = appearance;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isRelease() {
        return IsRelease;
    }

    public void setRelease(boolean release) {
        IsRelease = release;
    }

    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Boolean getOnlyPhone() {
        return onlyPhone;
    }

    public void setOnlyPhone(Boolean onlyPhone) {
        this.onlyPhone = onlyPhone;
    }

    public Boolean getOnlyWeixin() {
        return onlyWeixin;
    }

    public void setOnlyWeixin(Boolean onlyWeixin) {
        this.onlyWeixin = onlyWeixin;
    }

    public Boolean getRecordWeixinInfo() {
        return isRecordWeixinInfo;
    }

    public void setRecordWeixinInfo(Boolean recordWeixinInfo) {
        isRecordWeixinInfo = recordWeixinInfo;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    
	public boolean isIsRelease() {
		return IsRelease;
	}


	public void setIsRelease(boolean isRelease) {
		IsRelease = isRelease;
	}


	public Boolean getIsRecordWeixinInfo() {
		return isRecordWeixinInfo;
	}


	public void setIsRecordWeixinInfo(Boolean isRecordWeixinInfo) {
		this.isRecordWeixinInfo = isRecordWeixinInfo;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	

	public Boolean getIsDel() {
		return isDel;
	}


	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}


	@Override
	public String toString() {
		return "Questionnaire [id=" + id + ", title=" + title + ", instructions=" + instructions + ", IsRelease="
				+ IsRelease + ", list=" + list + ", totalPage=" + totalPage + ", onlyPhone=" + onlyPhone
				+ ", onlyWeixin=" + onlyWeixin + ", isRecordWeixinInfo=" + isRecordWeixinInfo + ", appearance="
				+ appearance + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
    
    
    
}
