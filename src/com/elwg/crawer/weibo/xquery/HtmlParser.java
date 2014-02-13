package com.elwg.crawer.weibo.xquery;

import java.io.File;
import java.io.UnsupportedEncodingException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.elwg.crawer.weibo.xquery.XQueryHelper;
import com.elwg.crawer.weibo.xquery.XQueryTemplate;


public class HtmlParser {
	protected XQueryHelper xqueryHelper = new XQueryHelper();
	protected XQueryTemplate template;

	
	public Document htmlparser(String data, String actionType) {
		HtmlParser testclass = new HtmlParser();
		testclass.init(actionType);
		data = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Insert title here</title></head><body>"
				+ data + "</body></html>";
//		System.out.println(html);
		return testclass.parseHTML(data);

	}

	private void init(String actionType) {
		try {
//			String path = HtmlParser.class.getClassLoader().getResource("template/sina_" + actionType + ".xquery").getFile();
			String path = "template/sina_" + actionType + ".xquery";
			/*
			path = java.net.URLDecoder.decode(path, "utf-8");
			*/
			this.template = new XQueryTemplate(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document parseHTML(String html) {
		if (html == null) {
			return null;
		}
		return xqueryHelper.parseHTML(html, template);
	}

	public static void main(String args[]) {
//		HtmlParser hp = new HtmlParser();
//
//		String path = HtmlParser.class.getClassLoader().getResource("xml.txt")
//				.getFile();
//		try {
//			path = java.net.URLDecoder.decode(path, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//	      File file=new File(path);  
//	      if(!file.exists()||file.isDirectory())
//			try {
//				throw new FileNotFoundException();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}  
//	      FileInputStream fis = null;
//		try {
//			fis = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}  
//	      byte[] buf = new byte[1024];  
//	      StringBuffer sb=new StringBuffer();  
//	      try {
//			while((fis.read(buf))!=-1){  
//			      sb.append(new String(buf));      
//			      buf=new byte[1024];//重新生成，避免和上次读取的数据重复  
//			  }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//	     String html = sb.toString(); 
	     
		String html = "<topics>	<topic>	<name>rjm</name>	<sex>girl</sex>	</topic>	<topic>	<name>yy</name>	</topic>	</topics>";
	     html = html.replaceAll("&#.*;", "");
	     Document doc = null;
	     doc = new XQueryHelper().string2Xml(html);
	     NodeList topicNodes = doc.getElementsByTagName("topic");
	     System.out.println("长度为："+topicNodes.getLength());
		for (int i = 0; i < topicNodes.getLength(); i++) {
			NodeList childNodes = topicNodes.item(i).getChildNodes();
			String name = childNodes.item(1).getTextContent().trim();
			System.out.println("name: "+name);
			System.out.print(childNodes.getLength());
			if(childNodes.getLength() == 5){
			String sex = childNodes.item(3).getTextContent().trim();
			System.out.println("sex:" +sex);
			}
		}
//		hp.htmlparser(html, "status");
	}
}
