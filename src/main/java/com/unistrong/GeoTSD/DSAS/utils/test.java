package com.unistrong.GeoTSD.DSAS.utils;

import com.alibaba.fastjson.JSONArray;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import jxl.common.Assert;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class test {
    public static void main(String[] args) throws IOException {
       /* int arr =6;
        String expect = "{\"resultData\":"+ arr + ",\"resultState\":\"success\",\"resultCode\":\"200\"}";
       String  expected = "{\"resultData\":\""+ arr +"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
        String ss ="{\"resultData\":6,\"resultState\":\"success\",\"resultCode\":\"200\"}";
        System.out.println(ss);*/
 /*      String DS_ID = "30a59d0b83ad4c179928d8989702e20f";
       String MODIFY_TIME = null;
        String dataBaseSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where T.\"DS_ID\" = '" + DS_ID + "'";
        Map<String,String> databaseInfo = postgreConn.SJYJBXX(dataBaseSql);
        for (String key : databaseInfo.keySet()) {
            MODIFY_TIME = databaseInfo.get("MODIFY_TIME");
        }
        System.out.println(MODIFY_TIME);*/

/*            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,"----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
            multipartEntity.addPart("files",new FileBody(new File("D:\\Geotsd\\DSASInterfacePro\\resources\\testjsonTable.txt"),"txt"));

            HttpPost request = new HttpPost("http://localhost:9090/dsas/api/datatable/addnewsbyjsonfile");
            request.setEntity(multipartEntity);
            request.addHeader("Content-Type","multipart/form-data; boundary=----------ThIs_Is_tHe_bouNdaRY_$");

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response =httpClient.execute(request);

            InputStream is = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }

            System.out.println("发送消息收到的返回："+buffer.toString());*/
   /*         String filePath = "D:\\Geotsd\\DSASInterfacePro\\resources\\testjsonTable.txt";
           String jsonfile =  readFile.reader(filePath);
           System.out.println("jsonfile:"+jsonfile);
            System.out.println("jsonfile:"+jsonfile.replaceAll(" ",""));*/

            // start reporters

    }


}
