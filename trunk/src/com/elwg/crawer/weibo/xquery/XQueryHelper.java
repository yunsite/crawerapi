package com.elwg.crawer.weibo.xquery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentWrapper;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XQueryHelper {

	private static final Logger logger = Logger.getLogger(XQueryHelper.class
			.getName());
	public XQueryHelper() {

	}

	public Document parseHTML(String html, XQueryTemplate template) {
		if (template == null || template.getEncoding() == null
				|| template.getContent() == null) {
			logger.log(Level.WARNING, "template error#" + template);
			return null;
		}
		
//		logger.info(html);
		
		Document doc = this.html2Xml(html, template.getEncoding());
		
		//将该部分存储下来
//		doc2disk(doc, "status.html");
				
		Document result = this.parseDocument(doc, template.getContent());
		return result;
	}

	/**
	 * 将html内容转化为xml文件
	 * @param htmlStr
	 * @param encoding
	 * @return
	 */
	public Document html2Xml(String htmlStr, String encoding) {

		Document doc = null;
		DOMParser parser = NekoHelper.getParser(encoding);
		StringReader sr = new StringReader(htmlStr);
		try {
			parser.parse(new InputSource(sr));
			doc = parser.getDocument();
			doc.normalize();

			NodeList list = doc.getElementsByTagName("HTML");
			Element e = (Element) list.item(0);
			e.removeAttribute("xmlns");
		} catch (SAXException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		return doc;
	}

	/**
	 * 在内存中完成操作
	 * 
	 * @param doc
	 * @return
	 */
	public Document parseDocument(Document doc, String templateContent) {
		Document document = null;
		StringWriter sw = null;
		try {
			Configuration c = new Configuration();
			StaticQueryContext qp = c.newStaticQueryContext();
			XQueryExpression xe = qp.compileQuery(templateContent);
			DynamicQueryContext dqc = new DynamicQueryContext(c);
			dqc.setContextItem(new DocumentWrapper(doc, null, c));

			sw = new StringWriter();
			final Properties props = new Properties();
			props.setProperty(OutputKeys.METHOD, "xml");
			props.setProperty(OutputKeys.INDENT, "yes");
			xe.run(dqc, new StreamResult(sw), props);
			String xml = sw.toString();
			/*
			 * 打印出xqury解析过的信息
			 * */
//			System.out.println(xml);  
//			logger.info(xml);
			
			
			xml = xml.replaceAll("&#.*;", "");
			document = string2Xml(xml);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.info(e.getMessage());
				}
			}
			doc = null;
		}
		return document;
	}
	
	private static void doc2disk(Document doc , String outputpath){
	   
	try {
		TransformerFactory tfac = TransformerFactory.newInstance();
	    Transformer tra = tfac.newTransformer();
	    DOMSource doms = new DOMSource(doc);
	    File file = new File(outputpath);
	    FileOutputStream outstream;
		outstream = new FileOutputStream(file);
		StreamResult sr = new StreamResult(outstream);
		   tra.transform(doms,sr);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.info(e.getMessage());
	} catch (TransformerConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.info(e.getMessage());
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.info(e.getMessage());
	}
	   

	}

	/**
	 * 在内存中完成操作
	 * 
	 * @param doc
	 * @return
	 */
	public String getStringResult(Document doc, String templateContent) {
		String result = null;
		StringWriter sw = null;
		try {
			Configuration c = new Configuration();
			StaticQueryContext qp = c.newStaticQueryContext();
			XQueryExpression xe = qp.compileQuery(templateContent);
			DynamicQueryContext dqc = new DynamicQueryContext(c);
			dqc.setContextItem(new DocumentWrapper(doc, null, c));

			sw = new StringWriter();
			final Properties props = new Properties();
			props.setProperty(OutputKeys.METHOD, "text");
			props.setProperty(OutputKeys.INDENT, "yes");
			xe.run(dqc, new StreamResult(sw), props);
			result = sw.toString();

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.info(e.getMessage());
				}
			}
			doc = null;
		}
		return result;
	}

	/**
	 * 
	 * 将字符串解析为 DOM 文档
	 * 
	 * @param strXml
	 * @return
	 * 
	 * @since crawler_agent　Ver1.0
	 */
	public Document string2Xml(String strXml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder;
		Document doc = null;
		StringReader is = null;
		try {
			is = new StringReader(strXml);
			InputSource inputSource = new InputSource(is);
			builder = factory.newDocumentBuilder();
			doc = builder.parse(inputSource);//这个地方为空
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			logger.info(e2.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} catch (SAXException e1) {
			e1.printStackTrace();
			logger.info(e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.info(e1.getMessage());
		} finally {
			is.close();
		}
		doc.normalize();
		return doc;
	}

}