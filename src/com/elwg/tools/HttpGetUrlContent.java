package com.elwg.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;

public class HttpGetUrlContent {
	private HttpClient client = null;
	
	public HttpGetUrlContent(HttpClient client){
		this.client = client;
	}
	/**
	 * 通过url，利用httpclient获取到页面数据，并将其从json格式中解析出来，返回String类型的data
	 * @return String data 若返回值为null，则说明返回的data数据类型错误，或inputstream出现异常
	 * */
	public String getUrlContent(String url) throws ClientProtocolException, IOException {

		HttpGet getMethod5 = new HttpGet(url);
		HttpResponse lastResponse5 = null;
			
		try {
			lastResponse5 = client.execute(getMethod5);
		}catch (HttpResponseException e) {
			
		}
		HttpEntity entity = lastResponse5.getEntity();
		InputStream inputStream = null;	
		
		//如果返回码不为200，则直接返回
		if (lastResponse5.getStatusLine().getStatusCode() != 200) {
			System.out.println("该账号为企业账号!退出该账号爬取!其返回码为：" + lastResponse5.getStatusLine().getStatusCode());
			return null;
		}
		
		Header encodingHeader = lastResponse5.getFirstHeader("Content-Encoding");
		if(encodingHeader != null && encodingHeader.getValue() != null){
			String encodingStr = encodingHeader.getValue();
			
			if(encodingStr.toLowerCase().indexOf("gzip") != -1){
				inputStream = new GZIPInputStream(entity.getContent());
			} else if(encodingStr.toLowerCase().indexOf("deflate") != -1){
				inputStream = new InflaterInputStream(entity.getContent());
			} else{
				System.out.println("unknown Content-Encoding#" + encodingStr);
			}
			
		} else {
			inputStream = entity.getContent();
		}
		
		if(inputStream.available() != 0){
			System.out.println("inputstream available: "+inputStream.available());
			inputStream.close();
			getMethod5.abort();
			try {
				Thread.sleep(2*1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		StringBuilder builder = new StringBuilder();
		
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line);
		}
		
		bufferedReader.close();
		inputStream.close();
		getMethod5.abort();
	
//		log.info(builder.toString());
		return builder.toString();
	}
}
