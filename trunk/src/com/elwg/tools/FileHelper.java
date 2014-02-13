package com.elwg.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class FileHelper {

    private static final String DEFAULT_ENCODING = "utf-8";
	
	public static void write(String fileName, String text){
		try {
			File f = new File(fileName);
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter resultFile = new FileWriter(f);  
			PrintWriter myFile = new PrintWriter(resultFile);  
			myFile.println(text);  
			resultFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
	}


    public static String readFile(String filename) {
        return readFile(filename, DEFAULT_ENCODING);
    }
	
	public static String readFile(String path, String encoding){
	    FileInputStream inStream = null;
        String content = "";
        try {
            inStream = load(path);
            byte[] byteBuf = new byte[inStream.available()];
            inStream.read(byteBuf);
            content = new String(byteBuf, encoding);
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            try {
                if(inStream != null)
                    inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
	}
	
	public static FileInputStream load(String filename) throws FileNotFoundException{
	    return new FileInputStream(filename);
	}
	
	public static String[] readLine(String filename, String encoding){
        FileInputStream is = null;
        InputStreamReader inReader = null;
        BufferedReader reader = null;
        List<String> texts = new ArrayList<String>();
        try {
            is = load(filename);
            inReader = new InputStreamReader(is, encoding);
            reader = new BufferedReader(inReader);
            String line = null;
            
            while((line = reader.readLine()) != null)
                texts.add(line);
            
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            try {
                if(is != null)
                    is.close();
                if(inReader != null)
                    inReader.close();
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return texts.toArray(new String[0]);
    }
	
	public static String[] readLines(String filename){
		return readLine(filename, DEFAULT_ENCODING);
	}
	
	public static void  doc2xml(Document doc , String out){
		
		try {
			TransformerFactory tfac = TransformerFactory.newInstance();
		    Transformer tra;
			tra = tfac.newTransformer();
			DOMSource doms = new DOMSource(doc);
		    File file = new File(out);
		    FileOutputStream outstream = new FileOutputStream(file);
		    StreamResult sr = new StreamResult(outstream);
		    tra.transform(doms,sr);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将文本内容写入到文件中
	 * @param content
	 * @param fileName
	 * @param isAppend
	 * @param charCode
	 */
	public static void write2File (String content, String fileName, boolean isAppend, String charCode){
		File file = new File(fileName);
		File tmp = file;
		boolean result = false;
		try {
			if (!file.exists()){
				if (file.getParentFile() != null){
					while (!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
						file = file.getParentFile();
					}
				}
				result = tmp.createNewFile();
				if (result){
					System.out.println(fileName + "创建成功!");
				}else {
					System.out.println(fileName + "创建失败!");
				}
			}
			//BufferedWriter write = new BufferedWriter(new FileWriter(file, isAppend));
			if (result){
				FileOutputStream fos = new FileOutputStream(tmp, isAppend);
				OutputStreamWriter writer = new OutputStreamWriter(fos, charCode);
				writer.write(content);
				writer.flush();
				writer.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @cn:从文本中读取文件内容
	 * @en:read the file content from the file
	 * @param fileName
	 * @param charCode
	 * @return ArrayList<String>
	 */
	public static  ArrayList<String> readFromFile (String fileName, String charCode){
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(fileName);
		String line = "";
		try {
			if (!file.exists()){
				System.out.println(fileName + "文件不存在!");
				return result;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charCode));
			while ((line = reader.readLine()) != null && (line = reader.readLine()) != ""){
				result.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

}
