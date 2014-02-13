package com.elwg.crawer.weibo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.elwg.util.ErrorUtils;

/**
 * @cn:获取新浪微博Client类
 * @en:class aims to get the httpclient of logging in sina weibo
 * @author Ivsunshine
 * @since 2014/1/8
 */
public class ClientHelper {

    private static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
        + "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
        + "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
        + "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";
    private String username = null;
    private String passwd = null;
    private String serverTime = null;
    private String nonce = null;
    private String rsakv = null;
    
    //构造函数
    public ClientHelper(String username, String passwd) {
		this.username = username;
		this.passwd = passwd;
	}
    
    /**
     * 取得登陆的HttpClient
     * @return httpclient
     */
    @SuppressWarnings({ "deprecation", "finally" })
	public HttpClient getLoginClient()
    {

        DefaultHttpClient client = new DefaultHttpClient();
        try{
            client.getParams().setParameter("http.protocol.cookie-policy",
                    CookiePolicy.BROWSER_COMPATIBILITY);
            client.getParams().setParameter(
                    HttpConnectionParams.CONNECTION_TIMEOUT, 5000);

            HttpPost post = new HttpPost(
                    "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)");

            this.getServerTime();
            long serverTime = Long.valueOf(this.serverTime);
            String nonce = this.nonce;
            String pwdString = serverTime + "\t" + nonce + "\n" + passwd;
            String sp = new BigIntegerRSA().rsaCrypt(SINA_PK, "10001", pwdString);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("entry", "weibo"));
            nvps.add(new BasicNameValuePair("gateway", "1"));
            nvps.add(new BasicNameValuePair("from", ""));
            nvps.add(new BasicNameValuePair("savestate", "7"));
            nvps.add(new BasicNameValuePair("useticket", "1"));
            nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
            nvps.add(new BasicNameValuePair("vsnf", "1"));
            // new NameValuePair("vsnval", ""),
            nvps.add(new BasicNameValuePair("su", encodeUserName(username)));
            nvps.add(new BasicNameValuePair("service", "miniblog"));
            nvps.add(new BasicNameValuePair("servertime", serverTime + ""));
            nvps.add(new BasicNameValuePair("nonce", nonce));
            nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
            nvps.add(new BasicNameValuePair("rsakv", this.rsakv));
            nvps.add(new BasicNameValuePair("sp", sp));
            nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
            nvps.add(new BasicNameValuePair("prelt", "115"));
            nvps.add(new BasicNameValuePair("returntype", "META"));
            nvps.add(new BasicNameValuePair(
                        "url",
                        "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));

            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = client.execute(post);  
            String entity = EntityUtils.toString(response.getEntity());  

            String url = entity.substring(entity  
                    .indexOf("http://weibo.com/ajaxlogin.php?"), entity  
                    .indexOf("code=0") + 6); 

            // 获取到实际url进行连接  
            HttpGet getMethod = new HttpGet(url);  

            response = client.execute(getMethod);  
            entity = EntityUtils.toString(response.getEntity());  
            entity = entity.substring(entity.indexOf("userdomain") + 13, entity  
                    .lastIndexOf("\""));  

        }catch (Exception e) {
        	//e.printStackTrace();
        	System.out.println(this.username + ErrorUtils.AUTHORWRONG);
		}finally{
            return client;
        }
    }

	private void getServerTime(){

        try{
            String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=&rsakt=mod&client=ssologin.js(v1.4.5)&_=1361178502112";
            HttpClient client  = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse httpresponse = client.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new  InputStreamReader(is));
            StringBuffer tmp = new StringBuffer();
            String line = null;
            String content = null;
            String NL  = System.getProperty("line.separator");
            while(null != (line = reader.readLine())){
                tmp.append(line+NL);
            }
            content = tmp.toString();

            int serverTimeStartIndex = content.indexOf("servertime")+12;
            int serverTimeEndIndex = content.indexOf(",\"pcid\"");
            if(serverTimeEndIndex != -1 && serverTimeStartIndex != -1){
                this.serverTime = content.substring(serverTimeStartIndex, serverTimeEndIndex);
            }
            if(null != this.serverTime){
                System.out.println("serverTime : "+ this.serverTime);
            }else{
                System.out.println("serverTime is null");
            }

            int nonceStartIndex = content.indexOf("nonce")+8;
            int nonceEndIndex = content.indexOf(",\"pubkey\"")-1;
            if(nonceEndIndex != -1 && nonceStartIndex != -1){
                this.nonce = content.substring(nonceStartIndex, nonceEndIndex);
            }
            if(null != nonce){
                System.out.println("nonce :"+nonce);
            }else{
                System.out.println("nonce is null");
            }

            int rsakvStartIndex = content.indexOf("rsakv")+8;
            int rsakvEndIndex = content.indexOf(",\"exectime\"")-1;
            if(rsakvEndIndex != -1 && rsakvStartIndex != -1){
                this.rsakv = content.substring(rsakvStartIndex , rsakvEndIndex);
            }
            if(null != rsakv){
                System.out.println("rsakv : "+rsakv);
            }else{
                System.out.println("rsakv is null");
            }
        }
        catch(IOException e2){
        	e2.printStackTrace();
        }
    }

    public static String getPreLoginInfo(HttpClient client)
        throws ParseException, IOException {
        String preloginurl = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&"
            + "callback=sinaSSOController.preloginCallBack&su="
            + "dW5kZWZpbmVk"
            + "&rsakt=mod&client=ssologin.js(v1.4.2)"
            + "&_=" + getCurrentTime();
        HttpGet get = new HttpGet(preloginurl);

        HttpResponse response = client.execute(get);

        String getResp = EntityUtils.toString(response.getEntity());

        int firstLeftBracket = getResp.indexOf("(");
        int lastRightBracket = getResp.lastIndexOf(")");

        String jsonBody = getResp.substring(firstLeftBracket + 1,
                lastRightBracket);
        return jsonBody;

    }

    private static String getCurrentTime() {
        long serverTime = new Date().getTime() / 1000;
        return String.valueOf(serverTime);
    }

    private static String encodeUserName(String email) {
        email = email.replaceFirst("@", "%40");// MzM3MjQwNTUyJTQwcXEuY29t
        email = Base64.encodeBase64String(email.getBytes());
        return email;
    }

}
