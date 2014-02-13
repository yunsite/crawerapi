package com.elwg.crawer.weibo;

/**
 * @cn:微博用户的属性值
 * @en:The attributes of a user in Weibo
 * @author Ivsunshine
 * @since 2014/1/9
 */
public class WeiboUserInfo {

	private String userId;
	private String nickName = "";

	private String location = "";
	private String sex = "";
	private String indiviDomain = "";// 个性域名
	private String briefIntro = "";

	private String registerTime = "";
	private String headImgUrl = "";
	private String pageId = "";

	private int followCount = 0;
	private int fansCount = 0;
	private int msgCount = 0;

	// 公司的信息
	private String company = "";

	// 教育的信息
	private String education = "";

	// 标签的信息
	private String tags = "";

	// 生日信息
	private String birthday = "";

	// 性取向
	private String sexLove = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIndiviDomain() {
		return indiviDomain;
	}

	public void setIndiviDomain(String indiviDomain) {
		this.indiviDomain = indiviDomain;
	}

	public String getBriefIntro() {
		return briefIntro;
	}

	public void setBriefIntro(String briefIntro) {
		this.briefIntro = briefIntro;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSexLove() {
		return sexLove;
	}

	public void setSexLove(String sexLove) {
		this.sexLove = sexLove;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	@Override
	public String toString() {
		return "WeiboUserInfo [userId=" + userId + ", nickName=" + nickName
				+ ", location=" + location + ", sex=" + sex + ", indiviDomain="
				+ indiviDomain + ", briefIntro=" + briefIntro
				+ ", registerTime=" + registerTime + ", headImgUrl="
				+ headImgUrl + ", pageId=" + pageId + ", followCount="
				+ followCount + ", fansCount=" + fansCount + ", msgCount="
				+ msgCount + ", company=" + company + ", education="
				+ education + ", tags=" + tags + ", birthday=" + birthday
				+ ", sexLove=" + sexLove + "]";
	}
	
}
