package com.elwg.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

public class HtmltoXML {
	private int connectionTimeout = 5000;
	private int soTimeout = 12000;

	/**
	 * org.w3c.dom.Document转成 org.dom4j.Document
	 * 
	 * @param doc
	 *            Document(org.w3c.dom.Document)
	 * @throws Exception
	 * @return Document
	 */
	public static Document parse(org.w3c.dom.Document doc) throws Exception {
		if (doc == null) {
			return null;
		}

		DOMReader domReader = new DOMReader();
		return domReader.read(doc);
	}

	/**
	 * org.dom4j.Document转成org.w3c.dom.Document
	 * 
	 * @param doc
	 *            Document(org.dom4j.Document)
	 * @throws Exception
	 * @return Document
	 */
	public static org.w3c.dom.Document parse(Document doc) throws Exception {
		if (doc == null) {
			return null;
		}

		// 返回这个节点的文本XML表示
		StringReader reader = new StringReader(doc.asXML());
		org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
		javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		javax.xml.parsers.DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		return documentBuilder.parse(source);
	}

	/**
	 * 获取对应的节点
	 * @param url
	 * @param client
	 * @return
	 */
	public Document getDocument2 (String url, HttpClient client){
		HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        HttpEntity httpEntity;
        HttpParams params = new BasicHttpParams();
        //设置相关的参数
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
        
        if (client == null){
        	client = new DefaultHttpClient();
        }
        
        try{
        	httpResponse = client.execute(httpGet);
        	StatusLine statusLine = httpResponse.getStatusLine();
        	int statusCode = statusLine.getStatusCode();
        	if (200 != statusCode){
        		System.out.println("错误码为："+statusCode);
        	}
        	
        	httpEntity = httpResponse.getEntity();
        	if (httpEntity != null){
        		InputStream in = httpEntity.getContent();
        		DOMParser parser = new DOMParser();
    			parser.setProperty(
    					"http://cyberneko.org/html/properties/default-encoding",
    					"gb2312"); // 中文乱码解决
    			parser.parse(new InputSource(in));
    			return parse(parser.getDocument());
        	}
        }catch (Exception e) {
        	e.printStackTrace();
		}finally{
			if (httpGet != null){
				httpGet.releaseConnection();
			}
		}
		return null;
	}
	
	//获取页面代码
	public String getHtml (String url, HttpClient client){
		String html = "";
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        HttpEntity httpEntity;
        
        if (client == null){
        	client = new DefaultHttpClient();
        }
        
        try{
        	httpResponse = client.execute(httpGet);
        	StatusLine statusLine = httpResponse.getStatusLine();
        	int statusCode = statusLine.getStatusCode();
        	if (200 != statusCode){
        		return html;
        	}
        	
        	httpEntity = httpResponse.getEntity();
        	if (httpEntity != null){
        		html = readHtmlContentFromEntity(httpEntity);
        	}
        }catch (Exception e) {
        	e.printStackTrace();
		}finally{
			if (httpGet != null){
				httpGet.releaseConnection();
			}
		}
		return html;
	}
	
	/**
     * 从response返回的实体中读取页面代码
     * @param httpEntity Http实体
     * @return 页面代码
     * @throws ParseException
     * @throws IOException
     */
    private String readHtmlContentFromEntity(HttpEntity httpEntity) throws ParseException, IOException {
        String html = "";
        InputStream in = httpEntity.getContent();
        html = readInStreamToString(in, "gb2312");
        return html;
    }
    
    /**
     * 读取InputStream流
     * @param in InputStream流
     * @return 从流中读取的String
     * @throws IOException
     */
    private String readInStreamToString(InputStream in, String charSet) throws IOException {
        StringBuilder str = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charSet));
        while((line = bufferedReader.readLine()) != null){
            str.append(line);
            str.append("\n");
        }
        if(bufferedReader != null) {
            bufferedReader.close();
        }
        return str.toString();
    }

	/**
	 * doc2XmlFile 将Document对象保存为一个xml文件到本地
	 * 
	 * @param filename
	 *            保存的文件名
	 * @param document
	 *            需要保存的document对象
	 * @return true:保存成功 flase:失败
	 */
	public boolean doc2XmlFile(Document document, File file2Write) {
		boolean flag = true;
		try {
			/* 将document中的内容写入文件中 */
			// 默认为UTF-8格式，指定为"GB2312"
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf8"); // XML中文乱码解决
			XMLWriter writer = new XMLWriter(
					new FileWriter(file2Write), format);
			if (document == null){
				return false;
			}
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @cn:将页面的内容保存到XML，如果熟悉Xpath或XQuery解析，可以使用这种方法
	 * @en:put the web content with the url to XML, so you can use XPath or XQuery to parse it if you like parsing this way
	 * @param url
	 * @param fileName
	 * @param client
	 * @return 是否保存成功
	 */
	public boolean saveHtml2XML(String url, String fileName, HttpClient client) {
		boolean result = false;
		try {
			HtmltoXML html2xml = new HtmltoXML();
			Document doc = html2xml.getDocument2(url, client);
			File file = new File(fileName);
			File tmpFile = file;
			if (file.getParentFile()!= null && file.getParent() != ""){
				file.getParentFile().mkdir();
				file = file.getParentFile();
			}
			if (!tmpFile.exists()){
				result = tmpFile.createNewFile();
				if (!result){
					return result;
				}
			}
			return html2xml.doc2XmlFile(doc, tmpFile);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}