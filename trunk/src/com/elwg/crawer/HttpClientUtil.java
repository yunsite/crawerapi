package com.elwg.crawer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.elwg.crawer.weibo.ClientHelper;
import com.elwg.util.ErrorUtils;
import com.elwg.util.weibo.WeiboConstant;

/**
 * @cn:获取httpClient类
 * @en:get the httpclient
 * @author Ivsunshine
 * @since 2014/1/7
 */
public class HttpClientUtil {
	
	//定义相关的变量
	static ArrayList<HttpClient> clients = null; //定义client集合
	static HttpClient client = null;//定义client
	static HttpResponse response = null; //定义回复的消息
	static String webContent = ""; //定义网页的内容
	static HttpGet get = null; //定义httpget
	
	/**
	 * @cn:获得HttpClients
	 * @en:get httpclients
	 * @param count 获取的HttpClient的数目
	 * @return ArrayList<HttpClient>
	 */
	public ArrayList<HttpClient> getNormalHttpClient(int count) {
		clients = new ArrayList<HttpClient>();
		// 可以设置对个HttpClient一起运作
		if (count < 1) {
			count = 1;
		} else if (count > 4) {
			count = 4;
		}
		for (int i = 0; i < count; i++) {
			HttpClient client = new DefaultHttpClient();
			clients.add(client);
		}
		return clients;
	}
	
	/**
	 * @cn:获取新浪微博登陆的HttpClient
	 * @en: get the httpclient for weibo
	 * @param count
	 * @param accounts
	 * @return
	 */
	public ArrayList<HttpClient> getWeiboHttpClient(int count,
			String[][] accounts) {
		clients = new ArrayList<HttpClient>();
		// 可以设置对个HttpClient一起运作
		if (count < 1) {
			count = 1;
		} else if (count > 4) {
			count = 4;
		}
		ClientHelper helper = null;
		if (accounts == null) {
			accounts = WeiboConstant.WEIBO_ACCOUNTS;
		}
		for (int i = 0; i < count; i++) {
			helper = new ClientHelper(accounts[0][i], accounts[1][i]);
			clients.add(helper.getLoginClient());
		}
		return clients;
	}
	
	/**
	 * @cn:根据url获取Web网页的内容
	 * @en:get the web content based on url
	 * @param url
	 * @return 网页的内容
	 * @description:该函数调用了getHttpClient()生成了一个Client对象进行爬取
	 */
	public String getWebUrlContent (String url, String charSet){
		String content = "";
		//此时抓取网页的内容，使用了一个HttpClient
		client = getNormalHttpClient(1).get(0);
		get = new HttpGet(url);
		StringBuilder sb = new StringBuilder();
		try {
			response = client.execute(get);
			//获取返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != statusCode){
				//返回码不为200
				content = ErrorUtils.HTTPGET_ERROR + statusCode;
				System.out.println(content);
				return content;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charSet));
			String line  = "";
			while ((line = reader.readLine()) != null){
				sb.append(line);
			}
			reader.close();
			content = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
}
