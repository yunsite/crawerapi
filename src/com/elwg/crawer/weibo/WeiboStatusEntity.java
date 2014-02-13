package com.elwg.crawer.weibo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.elwg.crawer.weibo.xquery.HtmlParser;
import com.elwg.tools.HttpGetUrlContent;
import com.elwg.util.Constant;
import com.elwg.util.ErrorUtils;
import com.elwg.util.weibo.WeiboConstant;

/**
 * @cn: 微博内容实体类
 * @en: the entity of the weibo status
 * @author Ivsunshine
 * @since 2014/1/10
 * @todo: 当用户输入的时间不是按照格式的处理
 */
public class WeiboStatusEntity {

	// 定义相关的变量
	private String userId = null;
	private HttpClient client = null;

	// 定义微博内容的List，表示所有的微博内容，含转发的内容，但是只是标记是转发的，具体的转发内容在下面的List
	private ArrayList<WeiboStatus> weiboStatus = new ArrayList<WeiboStatus>();

	// 定义是否该终止此账号爬取的变量，主要用于告知接下来的循环，在本次循环中，已经到达了时间的起点
	private static boolean isBreak = false;

	// 定义时间序列的数组
	static String[] time;

	// 定义起始时间
	private String startTime = "";
	private String endTime = "";

	/**
	 * Construct function
	 * @param client
	 */
	public WeiboStatusEntity(HttpClient client) {
		this.client = client;
	}

	/**
	 * @cn:爬取微博的状态
	 * @en:crawer the status of the weibo
	 * @param user_id
	 * @return ArrayList<WeiboStatus>
	 */
	public ArrayList<WeiboStatus> sendWeiboStatusRequest(String userId,
			String startTime, String endTime, boolean isShowLog) {

		// 赋值操作
		this.startTime = startTime;
		this.endTime = endTime;
		
		//判断时间部分
		if (this.startTime == "" || this.startTime == null){
			System.out.println(ErrorUtils.ERROR_WEIBO_STARTTIME);
			return null;
		}
		if (this.endTime == "" || this.endTime == null){
			this.endTime = WeiboConstant.formatTime(null);
		}
		if (!Constant.compareDate(this.startTime, this.endTime)){
			System.out.println(ErrorUtils.ERROR_TIMEDISORDER);
		}
		if (this.startTime.length() != 16 || this.endTime.length() != 16){
			System.out.println(ErrorUtils.ERROR_TIMEWRONG);
		}
		String pagebarTime = null;
		String endStatus = "status_loading";
		int countpage = 3; // 该userId的微博总数，在这个地方先手动设定，便于调试
		int page = 1;
		int pre_page = 1;
		int pagebar = -1;

		int dataStart = 0;

		while (page <= countpage && !endStatus.equals("ended")) {

			//创建地址
			String statusUrl = creatStatusUrl(userId, pre_page, page, pagebar);
			// status_url = "http://weibo.com/aj/mblog/mbloglist?page=46&pre_page=45&uid=1000218641";

			try {
				HttpGetUrlContent httpContent = new HttpGetUrlContent(client);
				String data = httpContent.getUrlContent(statusUrl);
				Document doc = null;
				if (null != data) {
					if (!data.startsWith("{")) {
						if (dataStart == 3) {
							break;
						}
						dataStart++;
						continue;
					}
					JSONObject jsonObject = JSONObject.fromObject(data);
					data = jsonObject.get("data").toString();
					if (isShowLog) {
						System.out.println("Web json data:" + data);
					}
					if (page == 1 && pagebar == 1) {
						countpage = checkCountpage(data);
						if (isShowLog){
							System.out.println("weiboStatus消息" + " user_id: "
									+ userId + " totalpage总页数为:" + countpage);
						}
					}
					doc = parseResult(data, "status");// 解析成DOM的格式
					if (doc == null || data == "") {
						if (isShowLog){
							System.out.println("this page is empty");
						}
						continue;
					} else {
						// 判断爬取status列表是否为空，如果为空，则表示已经达到了时间的起点，否则则继续向下爬
						isBreak = false;// 每次循环，重新置值
						boolean result = addStatus(doc, weiboStatus, page,
								pagebar + 2);
						if (result){
							//这里应该是批量插入数据的时刻
						}
						if (isBreak) {// 表示爬取的微博已经达到该账号设置时间的起点，跳出这个循环，爬取下一个账号信息
							return this.weiboStatus;
						}
					}
				} else {
					if (isShowLog){
						System.out
						.println("user_id: " + userId + " page: " + page
								+ " pagebar: " + pagebar + " totalpages : "
								+ countpage
								+ " it is a bug from SINA , ignore it!");
					}
				}

				pagebar++;
				/** 根据pagebar判断page和pre_page的值 **/
				switch (pagebar) {
				case 0:
					pre_page = page;
					break;
				case 2:
					pagebar = -1;
					page++;
					pre_page = page - 1;
					break;
				}

				if (pagebarTime == "empty" || null == pagebarTime) {
					// if (pagebarTime.equals("empty") || null == pagebarTime) {
					continue;
				}

				/** 判断这一个段落是否是最后一段，如果是，将包含<end>ended</end> **/
				if (null != doc) {
					NodeList endNode = doc.getElementsByTagName("end");
					endStatus = endNode.item(0).getChildNodes().item(0)
							.getTextContent().trim();
					if (endStatus.equals("ended")) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();// 打印异常
				break;// 当出现异常的时候，直接跳出.
			}
		}// end with one user_id
		return this.weiboStatus;
	}

	/**
	 * 在第一页最后一个片段获取总页数
	 * **/
	private int checkCountpage(String data) {
		Pattern stringfind = Pattern.compile("countPage=(.*?)\"");
		Matcher matcher = stringfind.matcher(data);
		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}
		return 1;
	}

	/**
	 * @cn:创建获取微博状态数据的url
	 * @en:create the url of the weibo status
	 * @param userId 爬取对象的userId
	 * @param pre_page 爬取页的前一页页码
	 * @param page 爬取的当前页页码
	 * @param pagebar 爬取的当前页的段落,值分别为-1 0 1
	 * */
	private String creatStatusUrl(String user_id, int pre_page, int page,
			int pagebar) {
		String status_url = "";
		if (pagebar == -1) {
			if (page == 1) {
				status_url = "http://weibo.com/aj/mblog/mbloglist?page=" + page
						+ "&uid=" + user_id;
			} else {
				status_url = "http://weibo.com/aj/mblog/mbloglist?page=" + page
						+ "&pre_page=" + pre_page + "&uid=" + user_id;
			}
		} else {
			status_url = "http://weibo.com/aj/mblog/mbloglist?page=" + page
					+ "&pre_page=" + pre_page + "&pagebar=" + pagebar + "&uid="
					+ user_id;
		}
		return status_url;
	}

	/**
	 * @cn: 调用模板对html格式的data进行解析
	 * @en: parse the html format data with templates
	 * @param data
	 * @param actiontype
	 * @return Document
	 */
	private Document parseResult(String data, String actiontype) {
		if (data == null || data.equals(""))
			return null;
		HtmlParser htmlparser = new HtmlParser();
		return htmlparser.htmlparser(data, actiontype);
	}

	/**
	 * @cn:将Document中的微博内容添加到List<WeiboStatus>中
	 * @en:add the weibo status content to the List<WeiboStatus>
	 * @param doc
	 * @param status
	 * @param page
	 * @param pagebar
	 * @return if the loop should be stoped
	 * @throws Exception
	 */
	private boolean addStatus(Document doc, List<WeiboStatus> status, int page,
			int pagebar) throws Exception {
		NodeList topicNodes = doc.getElementsByTagName("topic");
		for (int i = 0; i < topicNodes.getLength(); i++) {
			NodeList childNodes = topicNodes.item(i).getChildNodes();
			String statusId = childNodes.item(1).getTextContent().trim();
			String content = childNodes.item(3).getTextContent().trim();
			//删除fContent中的回车，换行，TAB等特殊字符
			content = content.replace("\\r", "").replace("\\n", "")
					.replace("\\t", "").replace("\\", "");
			String publishTime = childNodes.item(5).getTextContent().trim();
			if (publishTime == "") {// 改进了这个地方，20131222，微博接口更改，再XML最前面添加了一段空白的记录，导致publish_time为空
				continue;
			}
			/*
			 * 总共3中类型的时间格式 
			 * 1、35分钟前 
			 * 2、1月1日 00:25 
			 * 3、2013-12-31 13:49
			 */
			// 判断时间部分
			publishTime = WeiboConstant.formatTime(publishTime);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			long time1 = df.parse(this.startTime).getTime();
			long time2 = df.parse(this.endTime).getTime();
			long time_terminate = df.parse(publishTime).getTime();
			if (time_terminate < time1) {
				isBreak = true;
				break;
			} else if (time_terminate > time2) {
				continue;
			}
			//create a new weibo status
			WeiboStatus s = new WeiboStatus();
			s.setUserId(userId);
			s.setStatusId(statusId);
			s.setContent(content.toString());
			s.setPublishTime(publishTime);

			// 若为转发微博，则长度为11
			if (childNodes.getLength() == 13) {
				String fStatusId = childNodes.item(7).getTextContent();// 获取转发微博的statusId
				String fContent = childNodes.item(9).getTextContent();// 该部分内容包含原始发帖人的姓名和发帖的内容
				String fPublishTime = childNodes.item(11).getTextContent();// 原始发帖人的发帖时间

				s.setIsTransWeibo(1);
				s.setfStatusId(fStatusId);
				s.setfContent(fContent.toString());
				s.setfPublishTime(fPublishTime);
			}
			// 添加一条状态
			status.add(s);
		}
		if (status != null && status.size() != 0) {
			return true;
		}
		return false;
	}
}
