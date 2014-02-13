package com.elwg.crawer.test;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;

import com.elwg.crawer.HttpClientUtil;
import com.elwg.crawer.weibo.WeiboStatus;
import com.elwg.crawer.weibo.WeiboStatusEntity;
import com.elwg.crawer.weibo.WeiboUserFansEntity;
import com.elwg.crawer.weibo.WeiboUserInfo;
import com.elwg.crawer.weibo.WeiboUserInfoEntity;
import com.elwg.util.Constant;

public class TestHttpClientUtil {
	
	private static HttpClientUtil util = new HttpClientUtil();
	

	public static void main(String[] args) {
		/*for (int i = 0; i < 10; i ++){
			testGetWebUrlContent("http://pan.baidu.com/s/1ntjqyTz");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
//		testFansCrawer(1, 1, null);
//		testUserInfoCrawer(1, null);
		testWeiboStatusCrawer(1, null);
//		Constant.compareDate("2014-1-5 12:00", "2014-1-15 15:49");
	
		try {
			testClassLoder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试普通状况下的HttpClient
	 */
	public static void testFansCrawer (int type, int count, String accounts[][]){
		ArrayList<HttpClient> clients = util.getWeiboHttpClient(count, accounts);
		HttpClient client = clients.get(0);
		WeiboUserFansEntity entity = new WeiboUserFansEntity(client);
		ArrayList<WeiboUserInfo> userInfo = entity.sendFansListRequest("1000349667", 20, true, null);
		if (userInfo.size() > 0){
			System.out.println(userInfo.get(0).getFansCount());
		}else {
			System.out.println("userInfo为空集!");
		}
	}
	
	public static void testUserInfoCrawer (int type, String accounts[][]){
		ArrayList<HttpClient> clients = util.getWeiboHttpClient(1, accounts);
		HttpClient client = clients.get(0);
		WeiboUserInfoEntity entity = new WeiboUserInfoEntity(client);
		WeiboUserInfo info = entity.sendInfoRequest("1000349667", true, null);
		info.getSex();
	}
	
	public static void testWeiboStatusCrawer (int type, String accounts[][]){
		ArrayList<HttpClient> clients = util.getWeiboHttpClient(1, accounts);
		HttpClient client = clients.get(0);
		WeiboStatusEntity entity = new WeiboStatusEntity(client);
		ArrayList<WeiboStatus> status = entity.sendWeiboStatusRequest("1851127221", "2013-12-31 00:00", null, true);
		if (status.size() != 0){
			status.get(0);
		}
	}
	
	/**
	 * 测试获取网页
	 */
	public static void testGetWebUrlContent (String url){
		System.out.println(util.getWebUrlContent(url, Constant.utf8CharSet));
	}
	
	public static void testClassLoder () throws Exception{
//			String path = TestHttpClientUtil.class.getClassLoader().getResource("template/sina_status.xquery").getFile();
			String path ="template/sina_status.xquery";
			System.out.println(new File(path).getCanonicalPath());
			path = java.net.URLDecoder.decode(path, "utf-8");
			System.out.println(path);
	}
	

}
