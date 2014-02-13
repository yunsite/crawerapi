
package com.elwg.crawer.weibo;

/**
 * @cn:微博的内容属性类
 * @en:Weibo content attributes
 * @author Ivsunshine
 * @since 2014/1/9
 */
public class WeiboStatus {
    
	private String statusId = "";//微博状态的Id
	
	private String userId="";//用户的userId
	
    private String content="";//用户发表的内容
    
    private String publishTime="";//发表微博的时间

    private int isTransWeibo = 0;//是否是转发微博

    private String originalPicUrl = "";//原始图片的地址

	private String fStatusId = "";//转发的微博的状态Id
	
	private String fUserId="";//转发微博的userId
    
    private String fContent="";//转发微博的内容
    
    private String fPublishTime="";//转发微博的发布时间

    private String fOriginalPicUrl = "";//转发微博的原始图片路径
    
    public WeiboStatus(){
    	
    }
    
    /**
     * @cn:构造函数
     * @en:construct function
     * @param user_id
     * @param content
     * @param publish_time
     * @param is_retrans_weibo
     * @param fStatusId
     * @param retrans_weibo_content
     * @param retrans_weibo_publishtime
     */
    public WeiboStatus (String user_id, String content, String publish_time, int is_retrans_weibo, String fStatusId, String retrans_weibo_content, String retrans_weibo_publishtime){
    	this.userId = user_id;
    	this.content = content;
    	this.publishTime = publish_time;
    	this.isTransWeibo = is_retrans_weibo;
    	this.fStatusId = fStatusId;
    	this.fContent = retrans_weibo_content;
    	this.fPublishTime = retrans_weibo_publishtime;
    }
    
    //getters and setters
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public void setUserId(String userid) {
		this.userId = userid;
	}

	public String getUserId() {
		return userId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusId() {
		return statusId;
	} 

    public String getOriginalPicUrl(){
        return originalPicUrl; 
    }

    public void setOriginalPicUrl(String url){
        originalPicUrl = url;
    }

    public String getfContent() {
		return fContent;
	}

	public void setfContent(String content) {
		this.fContent = content;
	}

    
	public String getfPublishTime() {
		return fPublishTime;
	}

	public void setfPublishTime(String publishTime) {
		this.fPublishTime = publishTime;
	}

	public void setfUserId(String userid) {
		this.fUserId = userid;
	}

	public String getfUserId() {
		return fUserId;
	}

	public void setfStatusId(String statusId) {
		this.fStatusId = statusId;
	}

	public String getfStatusId() {
		return fStatusId;
	} 

    public String getfOriginalPicUrl(){
        return fOriginalPicUrl; 
    }

    public void setfOriginalPicUrl(String url){
        fOriginalPicUrl = url;
    }

	public int getIsTransWeibo() {
		return isTransWeibo;
	}

	public void setIsTransWeibo(int isTransWeibo) {
		this.isTransWeibo = isTransWeibo;
	}

	@Override
	public String toString() {
		return "WeiboStatus [userId=" + userId + ", content=" + content
				+ ", publishTime=" + publishTime + ", isTransWeibo="
				+ isTransWeibo + ", originalPicUrl=" + originalPicUrl + ", fUserId=" + fUserId
				+ ", fContent=" + fContent + ", fPublishTime=" + fPublishTime
				+ ", fOriginalPicUrl=" + fOriginalPicUrl + "]";
	}

}