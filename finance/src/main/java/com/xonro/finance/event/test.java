package com.xonro.finance.event;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by henry on 2018-1-22.
 */
public class test {
    public String getHtml(String url1) throws IOException {
        StringBuffer html = new StringBuffer();
        URL url = new URL(url1);  //根据 String 表示形式创建 URL 对象。
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))){
            String temp;
            while ((temp = br.readLine()) != null) {  //按行读取输出流
                if(!temp.trim().equals("")){
                    html.append(temp).append("\n");  //读完每行后换行
                }
            }
            return html.toString();   //返回此序列中数据的字符串表示形式。
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
