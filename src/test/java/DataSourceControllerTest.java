
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.unistrong.GeoTSD.DSAS.utils.ExcelData;
import com.unistrong.GeoTSD.DSAS.utils.HttpRequest;
import com.unistrong.GeoTSD.DSAS.utils.compared;
import com.unistrong.GeoTSD.DSAS.utils.postgreConn;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.util.*;


/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class DataSourceControllerTest {
    int fag = 0;    //测试用例编号
    String url = "http://localhost:9090/dsas/api/datasource";
    String Parameter = "";

    @BeforeClass
    public void beforeClass() {
        System.out.println("DataSourceController类开始：");

    }

    @AfterClass
    public void afterClass() {
        System.out.println("DataSourceController类执行结束！\n ***************");
    }

    @BeforeMethod
    public void beforeMethod() {
        fag++;
        System.out.println("开始执行第" + fag + "个用例：");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("第" + fag + "个用例执行结束！");
    }

    /*
    *@author shixj
    *@Description 新增数据源
    *@Date 10:24 2018/7/13
    **/
    @DataProvider(name = "Addnew")
    public Iterator<Object[]> Addnew() {
        return new ExcelData("DataSourceController", "Addnew");
    }

    @Test(dataProvider = "Addnew", timeOut = 1000)
    public void testAddnew(Map<String, String> params) {
        String DS_ID = null;
        String DS_NAME = null;
        String DS_TYPE = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String DK_TYPE = null;
        String DS_ADDR = null;
        String DB_PORT = null;
        String DB_NAME = null;
        String DB_USERNAME = null;
        String DB_PWD = null;
        String FILE_TYPE = null;
        String FILE_PATH = null;
        String INTERFACE_TYPE = null;
        String INTERFACE_ADDR = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        String  newDataBaseConnInfo = jsonPost.getString("newDataBaseConnInfo");//数据库基本信息
        String  newDataSourceBasicInfo = jsonPost.getString("newDataSourceBasicInfo");//数据源基本信息
        String  newFilesConnInfo = jsonPost.getString("newFilesConnInfo");//文件基本信息
        String  newInterfaceConnInfo = jsonPost.getString("newInterfaceConnInfo");//接口基本信息
        JSONObject jsonDataSource = JSONObject.parseObject(newDataSourceBasicInfo);
        int dataSourceType = jsonDataSource.getInteger("dataSourceType");//获取得到数据源类型
        System.out.println(params.get("testType"));
        Reporter.log("类型："+ params.get("testType"));
        String urlType = url + "/" + "addnew";
        System.out.println(urlType);
        Reporter.log("请求地址："+ urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("新增成功，数据库验证")){
            JSONObject json = JSONObject.parseObject(URL);
            //json字符串第一层
            String ss = json.getString("resultData");
            JSONObject jsonObject = JSONObject.parseObject(ss);
            //json字符串第二层
            String dataSourceId = jsonObject.getString("dataSourceId");
            System.out.println(dataSourceId);
            //验证数据源是否已经新增成功
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
            Map<String, String> sjkjbxx = postgreConn.SJYJBXX(SjySql);
            for (String key : sjkjbxx.keySet()) {
                DS_NAME = sjkjbxx.get("DS_NAME");
                DS_ID = sjkjbxx.get("DS_ID");
                DS_TYPE = sjkjbxx.get("DS_TYPE");
                MODIFY_TIME = sjkjbxx.get("MODIFY_TIME");
                REMARKS = sjkjbxx.get("REMARKS");
            }
            //验证新增的数据源ID在数据源基本信息表中是否正确：
            compared.comparedDataSource(newDataSourceBasicInfo,DS_NAME,DS_TYPE,MODIFY_TIME,REMARKS);
            //验证新增的数据源ID在数据源基本信息表中是否正确：
            Assert.assertEquals(dataSourceId,DS_ID);
            String responseData = "{\"resultData\":{\"dataSourceId\":\""+ dataSourceId +"\"},\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,responseData);
            //请求参数值与数据库插入的值对比验证
            switch (dataSourceType){
                case 1 :
                    String  dataBaseSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Map<String, String> dataBase = postgreConn.SJK(dataBaseSql);
                    for(String key : dataBase.keySet()){
                        DK_TYPE = dataBase.get("DK_TYPE");
                        DS_ADDR = dataBase.get("DS_ADDR");
                        DB_PORT = dataBase.get("DB_PORT");
                        DB_NAME = dataBase.get("DB_NAME");
                        DB_USERNAME = dataBase.get("DB_USERNAME");
                        DB_PWD = dataBase.get("DB_PWD");
                    }
                    compared.comparedDatabase(newDataBaseConnInfo,DK_TYPE,DS_ADDR,DB_PORT,DB_NAME,DB_USERNAME,DB_PWD);
                    break;
                case 2 :
                    String  fileSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Map<String, String> file = postgreConn.SJYWJ(fileSql);
                    for(String key : file.keySet()){
                        FILE_TYPE = file.get("FILE_TYPE");
                        FILE_PATH = file.get("FILE_PATH");
                    }
                    compared.comparedFile(newFilesConnInfo,FILE_TYPE,FILE_PATH);
                    break;
                case 3 :
                    String  interFaceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Map<String, String> interFace = postgreConn.SJYJK(interFaceSql);
                    for(String key : interFace.keySet()){
                        INTERFACE_TYPE = interFace.get("INTERFACE_TYPE");
                        INTERFACE_ADDR = interFace.get("INTERFACE_ADDR");
                    }
                    compared.comparedInterface(newInterfaceConnInfo,INTERFACE_TYPE,INTERFACE_ADDR);
                    break;
                default:
                    break;
            }
        }else{
          //实际响应结果与期望对比验证
            Assert.assertEquals(URL,params.get("responseData"));
        }
        Reporter.log("请求结果："+ URL);
    }

    /*
    *@author shixj
    *@Description 移除指定数据源
    *@Date 10:25 2018/7/13
    **/

    @DataProvider(name = "remove")
    public Iterator<Object[]> remove() {
        return new ExcelData("DataSourceController", "remove");
    }

    @Test(dataProvider = "remove")
    public void testremove(Map<String, String> params) {
        int  DS_TYPE = 0;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("dataSourceIds");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "remove";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if (params.get("responseData").equals("数据源移除成功") == false) {
            Assert.assertEquals(URL, params.get("responseData"));
        } else {
            String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URL, expected);
            //检测数据库表中是否还存在相应的数据信息
            JSONObject jsonPost = JSONObject.parseObject(Parameter);
            String dataSourceIds = jsonPost.getString("dataSourceIds");
            String[] dataSourceId = dataSourceIds.split(",");
            int len = dataSourceId.length;
            for (int i = 0; i < len; i++) {
                String DS_ID = dataSourceId[i];
                String DS_IDSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                Assert.assertEquals(postgreConn.querynum(DS_IDSql), 0);//验证数据源基本信息表是否删除
                        String sjkSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                        Assert.assertEquals(postgreConn.querynum(sjkSql),0);//验证数据库基本信息表是否删除
                        String tableSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                        Assert.assertEquals(postgreConn.querynum(tableSql),0);//验证数据表相关信息表是否删除
                        String fieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                        Assert.assertEquals(postgreConn.querynum(fieldSql),0);//验证字段表相关信息表是否删除
                        String fileSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                        Assert.assertEquals(postgreConn.querynum(fileSql),0);//验证文件源基本信息表是否删除
                        String interfaceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                        Assert.assertEquals(postgreConn.querynum(interfaceSql),0);//验证接口源基本信息表是否删除
            }
        }
     /*   Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 重命名指定数据源
    *@Date 10:25 2018/7/13
    **/
    @DataProvider(name = "rename")
    public Iterator<Object[]> rename() {
        return new ExcelData("DataSourceController", "rename");
    }

    @Test(dataProvider = "rename")
    public void testrename(Map<String, String> params) {
        String DS_ID = null;
        String DS_NAME = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        String dataSourceName = jsonPost.getString("dataSourceName");
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "rename";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("重命名成功，数据库验证")){
            String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,responseData);
        //验证数据库表相应的数据源名称是否与请求的参数一致
        String  dataSourceId = JSONObject.parseObject(Parameter).getString("dataSourceId");
        String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
        Map<String, String> sjkrename = postgreConn.SJYJBXX(SjySql);
        for (String key : sjkrename.keySet()) {
            DS_ID = sjkrename.get("DS_ID");
            DS_NAME = sjkrename.get("DS_NAME");
        }
        Assert.assertEquals(DS_ID,dataSourceId);
        Assert.assertEquals(DS_NAME,dataSourceName);
        }else{
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,params.get("responseData"));
        }
        Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);
    }

    /*
    *@author shixj
    *@Description 编辑指定数据库类型数据源连接信息
    *@Date 10:26 2018/7/13
    **/
    @DataProvider(name = "updatedbconninfo")
    public Iterator<Object[]> updatedbconninfo() {
        return new ExcelData("DataSourceController", "updatedbconninfo");
    }

    @Test(dataProvider = "updatedbconninfo",timeOut = 3000)
    public void testupdatedbconninfo(Map<String, String> params) {
        Map<String, String> sjkupdatedb = new HashMap<String, String>();
        String DS_ID = null;
        String DK_TYPE = null;
        String DS_ADDR = null;
        String DB_PORT = null;
        String DB_NAME = null;
        String DB_USERNAME = null;
        String DB_PWD = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "updatedbconninfo";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("数据库连接信息更新成功，数据库验证")){
            String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,responseData);
            //验证数据库表相应的数据源名称是否与请求的更新的参数是否一致
            String  dataSourceId = JSONObject.parseObject(Parameter).getString("dataSourceId");
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
            sjkupdatedb = postgreConn.SJK(SjySql);
            //读取数据库表中的字段信息
            for (String key : sjkupdatedb.keySet()) {
                DS_ID = sjkupdatedb.get("DS_ID");
                DK_TYPE = sjkupdatedb.get("DK_TYPE");
                DS_ADDR = sjkupdatedb.get("DS_ADDR");
                DB_PORT = sjkupdatedb.get("DB_PORT");
                DB_NAME = sjkupdatedb.get("DB_NAME");
                DB_USERNAME = sjkupdatedb.get("DB_USERNAME");
                DB_PWD = sjkupdatedb.get("DB_PWD");
            }
            //输入的参数字段值与数据库表中的实际值对比
            Assert.assertEquals(DS_ID,dataSourceId);
            Assert.assertEquals(DB_NAME,jsonPost.getString("dataBaseName"));
            Assert.assertEquals(DB_PORT,jsonPost.getString("port"));
            Assert.assertEquals(DS_ADDR,jsonPost.getString("address"));
            Assert.assertEquals(DK_TYPE,jsonPost.getString("dataBaseType"));
            Assert.assertEquals(DB_USERNAME,jsonPost.getString("userName"));
            Assert.assertEquals(DB_PWD,jsonPost.getString("password"));
        }else{
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,params.get("responseData"));
        }
   /*     Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 编辑指定数据文件类型数据源连接信息
    *@Date 10:26 2018/7/13
    **/
    @DataProvider(name = "updatefileconninfo")
    public Iterator<Object[]> updatefileconninfo() {
        return new ExcelData("DataSourceController", "updatefileconninfo");
    }

    @Test(dataProvider = "updatefileconninfo")
    public void testupdatefileconninfo(Map<String, String> params) {
        String DS_ID = null;
        String FILE_TYPE = null;
        String FILE_PATH = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "updatefileconninfo";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("文件连接信息更新成功，数据库验证")){
            String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,responseData);
            //验证数据库表相应的数据源名称是否与请求的参数一致
            String  dataSourceId = JSONObject.parseObject(Parameter).getString("dataSourceId");
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
            Map<String, String> sjkupdatefile = postgreConn.SJYWJ(SjySql);
            //读取数据库表中的字段信息
            for (String key : sjkupdatefile.keySet()) {
                DS_ID = sjkupdatefile.get("DS_ID");
                FILE_TYPE = sjkupdatefile.get("FILE_TYPE");
                FILE_PATH = sjkupdatefile.get("FILE_PATH");
            }
            //输入的参数字段值与数据库表中的实际值对比
            if(DS_ID =="" || DS_ID == null){
                Assert.assertEquals(null,DS_ID);
                Assert.assertEquals(null,FILE_TYPE);
                Assert.assertEquals(null,FILE_PATH);
            }else {
                Assert.assertEquals(DS_ID,dataSourceId);
                Assert.assertEquals(FILE_TYPE,jsonPost.getString("fileType"));
                Assert.assertEquals(FILE_PATH,jsonPost.getString("filePath"));
            }
        }else{
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,params.get("responseData"));
        }
    /*    Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 编辑指定数据接口类型数据源连接信息
    *@Date 10:26 2018/7/13
    **/
    @DataProvider(name = "updateinterfconninfo")
    public Iterator<Object[]> updateinterfconninfo() {
        return new ExcelData("DataSourceController", "updateinterfconninfo");
    }

    @Test(dataProvider = "updateinterfconninfo")
    public void testupdateinterfconninfo(Map<String, String> params) {
        String DS_ID = null;
        String INTERFACE_TYPE = null;
        String INTERFACE_ADDR = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "updateinterfconninfo";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("接口连接信息更新成功，数据库验证")){
            String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,responseData);
            //验证数据库表相应的数据源名称是否与请求的参数一致
            String  dataSourceId = JSONObject.parseObject(Parameter).getString("dataSourceId");
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
            Map<String, String> sjkupdateinterf = postgreConn.SJYJK(SjySql);
            //读取数据库表中的字段信息
            for (String key : sjkupdateinterf.keySet()) {
                DS_ID = sjkupdateinterf.get("DS_ID");
                INTERFACE_TYPE = sjkupdateinterf.get("INTERFACE_TYPE");
                INTERFACE_ADDR = sjkupdateinterf.get("INTERFACE_ADDR");
            }
            //execl表格中期望字段值与数据库表中的实际值对比
            if(DS_ID =="" || DS_ID == null){
                Assert.assertEquals(null,DS_ID);
                Assert.assertEquals(null,INTERFACE_TYPE);
                Assert.assertEquals(null,INTERFACE_ADDR);
            }else {
                Assert.assertEquals(DS_ID,dataSourceId);
                Assert.assertEquals(INTERFACE_TYPE,jsonPost.getString("interfaceType"));
                Assert.assertEquals(INTERFACE_ADDR,jsonPost.getString("interfaceAddress"));
            }
        }else{
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,params.get("responseData"));
        }
    /*    Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 获取全部数据源索引信息列表
    *@Date 10:26 2018/7/13
    **/
    @DataProvider(name = "findallindex")
    public Iterator<Object[]> findallindex() {
        return new ExcelData("DataSourceController", "findallindex");
    }

    @Test(dataProvider = "findallindex")
    public void testfindallindex(Map<String, String> params) {
        int totalnum = 0;
        int count = 0;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println("请求参数：" + Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        //获取请求的页码值及一页显示数目
        int pageNum = Integer.parseInt(jsonPost.getString("pageNum"));
        int pageSize = Integer.parseInt(jsonPost.getString("pageSize"));
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "findallindex";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("正常查询，数据库统计结果对比")){
            //分步读取响应结果值各个参数值
            JSONObject json = JSONObject.parseObject(URL);
            //json字符串第一层
            String resultData = json.getString("resultData");
            JSONObject jsonObject = JSONObject.parseObject(resultData);
            //json字符串第二层
            int resultcount = Integer.parseInt(jsonObject.getString("count"));
            int resultpagenum = Integer.parseInt(jsonObject.getString("pageNum"));
            int resultpagesize = Integer.parseInt(jsonObject.getString("pageSize"));
            int resulttotal = Integer.parseInt(jsonObject.getString("total"));
            String list = jsonObject.getString("list");
            JSONArray listArray = JSONArray.parseArray(list);
            int tableNum = listArray.size(); //list对象数组中包含的数据表个数
            //查询数据库各参数值
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\"";
            //查询表总数目,统计总页数，当前页个数
            totalnum = postgreConn.querynum(SjySql);//总记录数
            //总页数
            if(totalnum % pageSize == 0){
                count = totalnum / pageSize;
            }else{
                count = totalnum / pageSize + 1;
            }
            //验证请求页码值中的数据表个数是否显示正确
            if(pageNum == 1){
                if(totalnum > pageSize){
                    Assert.assertEquals(tableNum,pageSize);
                }else{
                    Assert.assertEquals(tableNum,totalnum);
                }
            }else{
             int expectnum = totalnum - ((pageNum - 1) * pageSize);
             if(expectnum > pageSize){
                 Assert.assertEquals(tableNum,pageSize);
             }else {
                 Assert.assertEquals(tableNum, expectnum);
             }
            }
            //对比验证数据库查询结果值与响应结果值
            Assert.assertEquals(resultcount,count);
            Assert.assertEquals(resultpagenum,pageNum);
            Assert.assertEquals(resultpagesize,pageSize);
            Assert.assertEquals(resulttotal,totalnum);
            //对比预期响应状态值
            Assert.assertEquals(json.getString("resultState"),"success");
            Assert.assertEquals(json.getString("resultCode"),"200");
        }else{
            Assert.assertEquals(URL,params.get("responseData"));
        }
    /*    Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 检索数据源接口
    *@Date 10:27 2018/7/13
    **/
    @DataProvider(name = "findbycond")
    public Iterator<Object[]> findbycond() {
        return new ExcelData("DataSourceController", "findbycond");
    }
    @Test(dataProvider = "findbycond")
    public void testfindbycond(Map<String, String> params) {
        int totalnum = 0;
        int count = 0;
        String keySql = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        JSONObject postjson = JSONObject.parseObject(Parameter);
        //获取请求的页码值及当前页数目
        int pageNum = Integer.parseInt(postjson.getString("pageNum"));
        int pageSize = Integer.parseInt(postjson.getString("pageSize"));
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "findbycond";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("统计查询结果个数与数据库对比验证")){
            //获取到数据源类型
            int dataSourceType = postjson.getInteger("dataSourceType");
            //查询关键字
            String keyWords = postjson.getString("keyWords");
            System.out.println("关键字："+ keyWords);
            String keyWordslist = postjson.getString("keyWords");
            JSONArray keylistArray = JSONArray.parseArray(keyWordslist);
              switch (keylistArray.size()){
                  case 1:
                      keySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(0) + "%'and T.\"DS_TYPE\" = "+ dataSourceType +"";
                      break;
                  case 2:
                      keySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where ( concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(0) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(1) + "%') and T.\"DS_TYPE\" = "+ dataSourceType +"";
                      break;
                  case 3:
                      keySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where ( concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(0) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(1) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(2) + "%' ) and T.\"DS_TYPE\" = "+ dataSourceType +"";
                      break;
                  case 4:
                      keySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where ( concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(0) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(1) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(2) + "%' or concat(T.\"DS_ID\",T.\"DS_NAME\",T.\"DS_TYPE\",T.\"MODIFY_TIME\",T.\"REMARKS\")" +
                              "like '%" + keylistArray.get(3) + "%' ) and T.\"DS_TYPE\" = "+ dataSourceType +"";
                      break;
                   default:
                          System.out.println("关键字超出4个，查询失败！");
                       break;
              }
            System.out.println("关键字查询sql语句："+ keySql);
            //分步读取响应结果值各个参数值
            JSONObject json = JSONObject.parseObject(URL);
            //json字符串第一层
            String resultData = json.getString("resultData");
            JSONObject jsonObject = JSONObject.parseObject(resultData);
            //json字符串第二层
            int resultcount = Integer.parseInt(jsonObject.getString("count"));
            int resultpagenum = Integer.parseInt(jsonObject.getString("pageNum"));
            int resultpagesize = Integer.parseInt(jsonObject.getString("pageSize"));
            int resulttotal = Integer.parseInt(jsonObject.getString("total"));
            System.out.println("获取到的总个数：" + resulttotal);
            String list = jsonObject.getString("list");
            JSONArray listArray = JSONArray.parseArray(list);
            int tableNum = listArray.size(); //list对象数组中包含的数据表个数

            //查询表总数目,统计总页数，当前页个数
            totalnum = postgreConn.querynum(keySql);//总记录数
            //总页数
            if(totalnum % pageSize == 0){
                count = totalnum / pageSize;
            }else{
                count = totalnum / pageSize + 1;
            }
            //验证请求页码值中的数据表个数是否显示正确
            if(pageNum == 1){
                if(totalnum > pageSize){
                    Assert.assertEquals(tableNum,pageSize);
                }else{
                    Assert.assertEquals(tableNum,totalnum);
                }
            }else{
                int expectnum = totalnum - ((pageNum - 1) * pageSize);
                Assert.assertEquals(tableNum,expectnum);
            }
            //对比验证数据库查询结果值与响应结果值
            Assert.assertEquals(resultcount,count);
            Assert.assertEquals(resultpagenum,pageNum);
            Assert.assertEquals(resultpagesize,pageSize);
            Assert.assertEquals(resulttotal,totalnum);
            //对比预期响应状态值
            Assert.assertEquals(json.getString("resultState"),"success");
            Assert.assertEquals(json.getString("resultCode"),"200");
        }else{
            Assert.assertEquals(URL,params.get("responseData"));
        }
 /*       Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 根据唯一标识，获取指定数据源基本属性信息
    *@Date 10:27 2018/7/13
    **/
    @DataProvider(name = "getbasicinfobyid")
    public Iterator<Object[]> getbasicinfobyid() {
        return new ExcelData("DataSourceController", "getbasicinfobyid");
    }
    @Test(dataProvider = "getbasicinfobyid")
    public void testgetbasicinfobyid(Map<String, String> params) {
        String DS_TYPE = null;//数据源类型
        String DS_NAME = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String DK_TYPE = null;//数据库类型
        String DS_ADDR = null;
        String DB_PORT = null;
        String DB_NAME = null;
        String DB_USERNAME = null;
        String DB_PWD = null;
        String FILE_TYPE = null;//文件类型
        String FILE_PATH = null;
        String INTERFACE_TYPE = null;//接口类型
        String INTERFACE_ADDR = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = "dataSourceId=" + params.get("dataSourceId");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "getbasicinfobyid";
        System.out.println(urlType);
        String URL = HttpRequest.httpGet(urlType, Parameter);
        if (params.get("responseData").equals("查询结果与数据库值对比验证") == false){
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL,params.get("responseData"));
        }else {
            //响应结果分解
            JSONObject jsonResult = JSONObject.parseObject(URL);
            String resultData = jsonResult.getString("resultData");
            JSONObject jsonObject = JSONObject.parseObject(resultData);
            //响应结果值中获取数据源基本信息
            String dataSourceBasicInfo = jsonObject.getString("dataSourceBasicInfo");
            //验证数据源基本信息是否正确
            String dataSourceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
            Map<String, String> sourceType = postgreConn.SJYJBXX(dataSourceSql);
            //读取数据源基本信息表中各个字段信息
            for (String key : sourceType.keySet()) {
                DS_NAME = sourceType.get("DS_NAME");
                DS_TYPE = sourceType.get("DS_TYPE");
                MODIFY_TIME = sourceType.get("MODIFY_TIME");
                REMARKS = sourceType.get("REMARKS");
            }
            compared.comparedDataSource(dataSourceBasicInfo,DS_NAME,DS_TYPE,MODIFY_TIME,REMARKS);//验证数据源基本信息
            //查询数据源对应的数据源类型
            switch (Integer.parseInt(DS_TYPE)){
                case 1:
                    String dataBaseConnInfo = jsonObject.getString("dataBaseConnInfo");//根据响应结果得出数据库基本信息
                    String dataBaseSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String,String> databaseInfo = postgreConn.SJK(dataBaseSql);
                    for (String key : databaseInfo.keySet()) {
                        DK_TYPE = databaseInfo.get("DK_TYPE");
                        DS_ADDR = databaseInfo.get("DS_ADDR");
                        DB_PORT = databaseInfo.get("DB_PORT");
                        DB_NAME = databaseInfo.get("DB_NAME");
                        DB_USERNAME = databaseInfo.get("DB_USERNAME");
                        DB_PWD = databaseInfo.get("DB_PWD");
                    }
                    compared.comparedDatabase(dataBaseConnInfo,DK_TYPE,DS_ADDR,DB_PORT,DB_NAME,DB_USERNAME,DB_PWD);//验证数据库基本信息
                    break;
                case 2:
                    String  filesConnInfo = jsonObject.getString("filesConnInfo");//根据响应结果得出文件数据源基本信息
                    String fileSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String, String> fileInfo = postgreConn.SJYWJ(fileSql);
                    for(String key : fileInfo.keySet()){
                        FILE_TYPE = fileInfo.get("FILE_TYPE");
                        FILE_PATH = fileInfo.get("FILE_PATH");
                    }
                    compared.comparedFile(filesConnInfo,FILE_TYPE,FILE_PATH);//验证文件数据源基本信息
                    break;
                case 3:
                    String interfaceConnInfo = jsonObject.getString("interfaceConnInfo");
                    String  interFaceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String, String> interFace = postgreConn.SJYJK(interFaceSql);
                    for(String key : interFace.keySet()){
                        INTERFACE_TYPE = interFace.get("INTERFACE_TYPE");
                        INTERFACE_ADDR = interFace.get("INTERFACE_ADDR");
                    }
                    compared.comparedInterface(interfaceConnInfo,INTERFACE_TYPE,INTERFACE_ADDR);//验证接口数据源基本信息
                    break;
                default:
                        System.out.println("数据源类型异常！");
                     break;
            }
        }
/*        Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
    /*
    *@author shixj
    *@Description 根据唯一标识，获取指定数据源全部信息
    *@Date 10:27 2018/7/13
    **/
    @DataProvider(name = "getallinfobyid")
    public Iterator<Object[]> getallinfobyidgetbasicinfobyid() {
        return new ExcelData("DataSourceController", "getallinfobyid");
    }

    @Test(dataProvider = "getallinfobyid")
    public void testgetallinfobyid(Map<String, String> params) {
        String DS_TYPE = null;//数据源类型
        String DS_NAME = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String DK_TYPE = null;//数据库类型
        String DS_ADDR = null;
        String DB_PORT = null;
        String DB_NAME = null;
        String DB_USERNAME = null;
        String DB_PWD = null;
        String FILE_TYPE = null;//文件类型
        String FILE_PATH = null;
        String INTERFACE_TYPE = null;//接口类型
        String INTERFACE_ADDR = null;
        String DT_NAME = null;//数据表
        String DT_PK = null;
        String DT_ALIAS = null;
        String FIELD_NAME = null;//字段
        String FIELD_ALIAS = null;
        String FIELD_TYPE = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = "dataSourceId=" + params.get("dataSourceId");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "getallinfobyid";
        System.out.println(urlType);
        String URL = HttpRequest.httpGet(urlType, Parameter);
        if (params.get("responseData").equals("查询结果与数据库值对比验证") == false) {
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL, params.get("responseData"));
        }else {
            //响应结果分解
            JSONObject jsonResult = JSONObject.parseObject(URL);
            String resultData = jsonResult.getString("resultData");
            JSONObject jsonObject = JSONObject.parseObject(resultData);
            String dataSourceType = jsonObject.getString("dataSourceType");//数据源类型
            //数据源基本信息验证
            String dataSourceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
            Map<String, String> sourceInfo = postgreConn.SJYJBXX(dataSourceSql);
            //读取数据源基本信息表中各个字段信息
            for (String key : sourceInfo.keySet()) {
                DS_TYPE = sourceInfo.get("DS_TYPE");
                MODIFY_TIME = sourceInfo.get("MODIFY_TIME");
                REMARKS = sourceInfo.get("REMARKS");
                DS_NAME = sourceInfo.get("DS_NAME");
            }
            compared.comparedDataSource(resultData,DS_NAME,DS_TYPE,MODIFY_TIME,REMARKS);
            switch (Integer.parseInt(dataSourceType)){
                case 1:
                    String dataBaseSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String,String> databaseInfo = postgreConn.SJK(dataBaseSql);
                    for (String key : databaseInfo.keySet()) {
                        DS_ADDR = databaseInfo.get("DS_ADDR");
                        DB_PORT = databaseInfo.get("DB_PORT");
                        DB_NAME = databaseInfo.get("DB_NAME");
                        DB_USERNAME = databaseInfo.get("DB_USERNAME");
                        DB_PWD = databaseInfo.get("DB_PWD");
                        DK_TYPE = databaseInfo.get("DK_TYPE");
                    }
                    compared.comparedDatabase(resultData,DK_TYPE,DS_ADDR,DB_PORT,DB_NAME,DB_USERNAME,DB_PWD);//验证数据库基本信息
                    break;
                case 2:
                    String fileSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String, String> fileInfo = postgreConn.SJYWJ(fileSql);
                    for(String key : fileInfo.keySet()){
                        FILE_TYPE = fileInfo.get("FILE_TYPE");
                        FILE_PATH = fileInfo.get("FILE_PATH");
                    }
                    compared.comparedFile(resultData,FILE_TYPE,FILE_PATH);//验证文件数据源基本信息
                    break;
                case 3:
                    String  interFaceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String, String> interFace = postgreConn.SJYJK(interFaceSql);
                    for(String key : interFace.keySet()){
                        INTERFACE_TYPE = interFace.get("INTERFACE_TYPE");
                        INTERFACE_ADDR = interFace.get("INTERFACE_ADDR");
                    }
                    compared.comparedInterface(resultData,INTERFACE_TYPE,INTERFACE_ADDR);//验证接口数据源基本信息
                    break;
                default:
                    System.out.println("数据源类型异常！");
                    break;
            }
            //验证数据表信息
            String tableAllInfo = jsonObject.getString("tableAllInfo");
            JSONArray listTable = JSONArray.parseArray(tableAllInfo);
            if(listTable.size() == 0){
                System.out.println("当前数据源无数据表信息！");
            }else{
                for(int i=0;i<listTable.size();i++){
                    JSONObject tableInfo = (JSONObject) listTable.get(i);
                    String  dataTableBasicInfo = tableInfo.getString("dataTableBasicInfo");
                    //验证数据表信息
                    String tableSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" T where T.\"DS_ID\" = '" + params.get("dataSourceId") + "'";
                    Map<String, String> sjb = postgreConn.SJB(tableSql);
                    for (String key : sjb.keySet()) {
                        DT_PK = sjb.get("DT_PK");
                        DT_ALIAS = sjb.get("DT_ALIAS");
                        MODIFY_TIME = sjb.get("MODIFY_TIME");
                        REMARKS = sjb.get("REMARKS");
                        DT_NAME = sjb.get("DT_NAME");
                    }
                    compared.comparedTable(dataTableBasicInfo,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);//验证数据表基本信息
                    //验证字段信息
                    String  fieldList = tableInfo.getString("fieldList");
                    JSONArray listField = JSONArray.parseArray(fieldList);
                    if(listField.size() == 0){
                        System.out.println("当前数据表中无字段信息！");
                    }else{
                        for(int j=0;j<listField.size();j++){
                            String fieldInfo = listField.get(j).toString();
                            String fieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_NAME\" = '" + params.get("dataSourceId") + "'";
                            Map<String,String> field = postgreConn.ZD(fieldSql);
                            for (String key : field.keySet()) {
                                FIELD_ALIAS = field.get("FIELD_ALIAS");
                                FIELD_TYPE = field.get("FIELD_TYPE");
                                MODIFY_TIME = field.get("MODIFY_TIME");
                                REMARKS = field.get("REMARKS");
                                FIELD_NAME = field.get("FIELD_NAME");
                            }
                            compared.comparedZD(fieldInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
                        }
                    }
                }
            }
            //对比预期响应状态值
            Assert.assertEquals(jsonResult.getString("resultState"),"success");
            Assert.assertEquals(jsonResult.getString("resultCode"),"200");
        }
    /*    Reporter.log("类型："+ params.get("testType"));
        Reporter.log("请求地址："+ urlType);
        Reporter.log("请求结果："+ URL);*/
    }
}