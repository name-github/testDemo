import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unistrong.GeoTSD.DSAS.utils.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class DataTableControllerTest {
    int fag = 0;    //测试用例编号
    String url = "http://localhost:9090/dsas/api/datatable";
    String Parameter = "";
    @BeforeClass
    public  void beforeClass(){
        System.out.println("DataTableControllerTest类开始：");

    }
    @AfterClass
    public  void afterClass(){
        System.out.println("DataTableControllerTest类执行结束！\n ***************");
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
    *@Description  为指定数据源，批量添加多个数据表接口（数据库类型）
    *@Date 16:48 2018/7/12
    **/
    @DataProvider(name = "addnews")
    public Iterator<Object[]> addnews() {
        return new ExcelData("DataTableController", "addnews");
    }

    @Test(dataProvider = "addnews")
    public void testaddNews(Map<String, String> params) {
        String DT_ID = null;
        String DS_ID = null;
        String DT_NAME = null;
        String DT_PK = null;
        String DT_ALIAS = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String FIELD_NAME = null;
        String FIELD_ALIAS = null;
        String FIELD_TYPE = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "addnews";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        JSONObject json = JSONObject.parseObject(URL);
        //json字符串第一层
        String listDataSourceId = json.getString("resultData");
        JSONArray arr = JSONArray.parseArray(listDataSourceId);
        String responseResult = params.get("responseData");
        if(responseResult.equals("新增成功，数据库对比验证")){
            String expect = "{\"resultData\":"+ arr + ",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //响应结果值中的数据源id自动验证
            Assert.assertEquals(URL,expect);
            //分解json字符数组中的表id
        for(int i=0;i<arr.size();i++){
            String  DataTableId = String.valueOf(arr.get(i));
            System.out.println(DataTableId);
            //验证新增的数据表在数据库中是否存在
            //查询数据表信息
            String SjbSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_ID\" = '" + DataTableId + "'";
            Map<String, String> sjb = postgreConn.SJB(SjbSql);
            for (String key : sjb.keySet()) {
                DS_ID = sjb.get("DS_ID");
                DT_ID = sjb.get("DT_ID");
                DT_NAME = sjb.get("DT_NAME");
                DT_PK = sjb.get("DT_PK");
                DT_ALIAS = sjb.get("DT_ALIAS");
                MODIFY_TIME = sjb.get("MODIFY_TIME");
                REMARKS = sjb.get("REMARKS");
            }
            //验证新增的数据源ID在数据源基本信息表中是否正确：
            Assert.assertEquals(DT_ID, DataTableId);//验证数据表id
            //验证数据库中的数据源基本信息是否与输入的一致
            String Request = params.get("postData");
            JSONObject jsonRequest = JSONObject.parseObject(Request);
            String dataSourceId = jsonRequest.getString("dataSourceId");
            Assert.assertEquals(DS_ID,dataSourceId);//验证数据源id
            String newDataTableInfoList = jsonRequest.getString("newDataTableInfoList");
            JSONArray newDataTableInfo = JSONArray.parseArray(newDataTableInfoList);
                String  TableBaseInfo = newDataTableInfo.get(i).toString();//数据表基本信息数目与数据表id对应一致
                System.out.println("TableBaseInfo信息："+ TableBaseInfo);
                JSONObject jsonTableBaseinfo = JSONObject.parseObject(TableBaseInfo);
                //数据表信息验证
                String dataTableBasicInfo = jsonTableBaseinfo.getString("dataTableBasicInfo");
                compared.comparedTable(dataTableBasicInfo,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);
                //字段表json数组转换成list
                String fieldList = jsonTableBaseinfo.getString("fieldList");
                JSONArray fieldInfo = JSONArray.parseArray(fieldList);
                if(fieldList != null && !fieldList.isEmpty()){
                    for(int n=0;n<fieldInfo.size();n++){
                       String  fieldBaseInfo =  fieldInfo.get(n).toString();
                        JSONObject jsonfield = JSONObject.parseObject(fieldBaseInfo);
                        String fieldName = jsonfield.getString("fieldName");
                        //按照数据表名称查询字段表信息
                        String ZDSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_NAME\" = '" + fieldName + "'";
                        Map<String,String> zd= postgreConn.ZD(ZDSql);
                            for (String key : zd.keySet()) {
                                FIELD_NAME = zd.get("FIELD_NAME");
                                FIELD_TYPE = zd.get("FIELD_TYPE");
                                MODIFY_TIME = zd.get("MODIFY_TIME");
                                REMARKS = zd.get("REMARKS");
                                FIELD_ALIAS = zd.get("FIELD_ALIAS");
                            }
                        compared.comparedZD(fieldBaseInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
                    }
                }else {
                    System.out.println("请求的参数中没有字段信息！");
                }

        }
        }else{
            Assert.assertEquals(URL,responseResult);
        }
    }
    /*
     *@author shixj
     *@Description 为指定数据源，批量添加多个数据表接口（文件类型）
     *@Date 16:50 2018/7/12
     **/
    @DataProvider(name = "addnewsbyjsonfile")
    public Iterator<Object[]> addnewsbyjsonfile() {
        return new ExcelData("DataTableController", "addnewsbyjsonfile");
    }
    @Test(dataProvider = "addnewsbyjsonfile")
    public void testaddnewsbyjsonfile(Map<String, String> params) {
        String DT_ID = null;
        String DS_ID = null;
        String DT_NAME = null;
        String DT_PK = null;
        String DT_ALIAS = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String FIELD_NAME = null;
        String FIELD_ALIAS = null;
        String FIELD_TYPE = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("testjsonTable",Parameter);
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "addnewsbyjsonfile";
        System.out.println(urlType);
        String URL = HttpRequest.formUpload(urlType,fileMap);
        System.out.println(URL);
        if (params.get("responseData").equals("新增成功，数据库验证") == false ){
            Assert.assertEquals(URL,params.get("responseData"));
        }else {
           String postStr = readFile.reader(Parameter).replaceAll(" ","");
           JSONObject postJson = JSONObject.parseObject(postStr);
           String dataSourceId = postJson.getString("dataSourceId");
           String newDataTableInfoList = postJson.getString("newDataTableInfoList");
           JSONArray newDataTableInfo = JSONArray.parseArray(newDataTableInfoList);
           for(int i=0;i<newDataTableInfo.size();i++){
             String dataTableBasicInfo =  newDataTableInfo.get(i).toString();
             JSONObject dataTableBasicInfojson = JSONObject.parseObject(dataTableBasicInfo);
             String dataTableInfo = dataTableBasicInfojson.getString("dataTableBasicInfo");
             String fieldList = dataTableBasicInfojson.getString("fieldList");
             String tabelSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" T where T.\"DT_NAME\" = '" + JSONObject.parseObject(dataTableInfo).getString("dataTableName") + "'";
               Map<String, String> sjb = postgreConn.SJB(tabelSQL);
               for (String key : sjb.keySet()) {
                   DT_ID = sjb.get("DT_ID");
                   DT_NAME = sjb.get("DT_NAME");
                   DT_PK = sjb.get("DT_PK");
                   DT_ALIAS = sjb.get("DT_ALIAS");
                   MODIFY_TIME = sjb.get("MODIFY_TIME");
                   REMARKS = sjb.get("REMARKS");
                   DS_ID = sjb.get("DS_ID");
               }
               Assert.assertEquals(DS_ID,dataSourceId);
               compared.comparedTable(dataTableInfo,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);
               JSONArray fieldlist = JSONArray.parseArray(fieldList);
               for(int j=0;j<fieldlist.size();j++){
                   String fieldInfo = fieldlist.get(j).toString();
                   JSONObject fieldJson = JSONObject.parseObject(fieldInfo);
                   String fieldSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" T where T.\"FIELD_NAME\" = '" + fieldJson.getString("fieldName") + "'";
                   Map<String,String> zd= postgreConn.ZD(fieldSQL);
                   for (String key : zd.keySet()) {
                       FIELD_TYPE = zd.get("FIELD_TYPE");
                       MODIFY_TIME = zd.get("MODIFY_TIME");
                       REMARKS = zd.get("REMARKS");
                       FIELD_ALIAS = zd.get("FIELD_ALIAS");
                       FIELD_NAME = zd.get("FIELD_NAME");
                   }
                   compared.comparedZD(fieldInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
               }
           }
        }
    }
    /*
    *@author shixj
    *@Description 将指定数据源内，指定数据表集合生成为json格式文件并下载
    *@Date 16:50 2018/7/12
    **/
    @DataProvider(name = "upload")
    public Iterator<Object[]> upload() {
        return new ExcelData("DataTableController", "upload");
    }
    @Test(dataProvider = "upload")
    public void testupload(Map<String, String> params) {
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "upload";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("json文件生成成功，验证文件路径") == false){
            Assert.assertEquals(URL,params.get("responseData"));
        }else{
            JSONObject json = JSONObject.parseObject(URL);
            String pathFile = json.getString("resultData");
            String expected = "{\"resultData\":\""+ pathFile +"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URL,expected);
            System.out.println(pathFile);
        }
    }

    /*
     *@author shixj
     *@Description 从指定数据源内，移除指定数据表接口（支持批量）
     *@Date 16:51 2018/7/12
     **/

    @DataProvider(name = "Remove")
    public Iterator<Object[]> Remove() {
        return new ExcelData("DataTableController", "Remove");
    }
    @Test(dataProvider = "Remove")
    public void testRemove(Map<String, String> params) {
        for(String param:params.keySet()){
            if(params.get(param)!= null && params.get(param).length() > 0 ){
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "remove";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson( Parameter,urlType);
        if(params.get("responseData").equals("正常移除，数据库验证") == false){
            Assert.assertEquals(URL,params.get("responseData"));
        }else{
            String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URL,expected);
            //检测数据库表中是否还存在相应的数据信息
            JSONObject jsonPost = JSONObject.parseObject(Parameter);
            String dataTableIds = jsonPost.getString("dataTableIds");
            String[] dataTableId = dataTableIds.split(",");
            int len = dataTableId.length;
            for (int i = 0; i < len; i++) {
                String DT_ID = dataTableId[i];
                String tableSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_ID\" = '" + DT_ID + "'";
                Assert.assertEquals(postgreConn.querynum(tableSql),0);//验证数据表相关信息表是否删除
                String fieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"DT_ID\" = '" + DT_ID + "'";
                Assert.assertEquals(postgreConn.querynum(fieldSql),0);//验证字段表相关信息表是否删除
            }
        }
    }
    /*
     *@author shixj
     *@Description 清空指定数据源内全部数据表接口
     *@Date 16:53 2018/7/12
     **/
    @DataProvider(name = "clear")
    public Iterator<Object[]> clear() {
        return new ExcelData("DataTableController", "clear");
    }
    @Test(dataProvider = "clear")
    public void testclear(Map<String, String> params) {
        for(String param:params.keySet()){
            if(params.get(param)!= null && params.get(param).length() > 0 ){
                Parameter = "dataSourceId=" + params.get("dataSourceId");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "clear";
        System.out.println(urlType);
        String URL = HttpRequest.httpGet(urlType, Parameter);
        if(params.get("responseData").equals("成功清空数据源关联的数据表信息") == false){
            Assert.assertEquals(URL,params.get("responseData"));
        }else {
            String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URL, expected);
            String tableSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DS_ID\" = '" + Parameter + "'";
            Assert.assertEquals(postgreConn.querynum(tableSQL),0);//验证数据表相关信息表是否删除
            String fieldSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"DS_ID\" = '" + Parameter + "'";
            Assert.assertEquals(postgreConn.querynum(fieldSQL),0);//验证字段表相关信息表是否删除
        }
    }
    /*
    *@author shixj
    *@Description 编辑指定数据表的基本属性接口
    *@Date 16:50 2018/7/12
    **/
    @DataProvider(name = "update")
    public Iterator<Object[]> update() {
        return new ExcelData("DataTableController", "update");
    }
    @Test(dataProvider = "update")
    public void testupdate(Map<String, String> params) {
        String DT_NAME = null;
        String DT_PK = null;
        String DT_ALIAS = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("postData");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "update";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        if(params.get("responseData").equals("数据表信息更新成功，数据库验证") == false){
            Assert.assertEquals(URL,params.get("responseData"));
        }else {
            String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URL, expected);
            String  dataTableId = JSONObject.parseObject(Parameter).getString("dataTableId");
            String tabelSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" T where T.\"DT_ID\" = '" + dataTableId + "'";
            Map<String, String> sjb = postgreConn.SJB(tabelSQL);
            for (String key : sjb.keySet()) {
                DT_ALIAS = sjb.get("DT_ALIAS");
                MODIFY_TIME = sjb.get("MODIFY_TIME");
                REMARKS = sjb.get("REMARKS");
                DT_NAME = sjb.get("DT_NAME");
                DT_PK = sjb.get("DT_PK");
            }
            compared.comparedTable(Parameter,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);
        }
    }
    /*
     *@author shixj
     *@Description 获取指定数据源内全部数据表基本属性列表接口（支持分页）
     *@Date 16:54 2018/7/12
     **/
    @DataProvider(name = "findAllByDsId")
    public Iterator<Object[]> findAllByDsId() {
        return new ExcelData("DataTableController", "findAllByDsId");
    }
    @Test(dataProvider = "findAllByDsId")
    public void testfindAllByDsId(Map<String, String> params) {
        int totalnum = 0;
        int count = 0;
        for(String param:params.keySet()){
            if(params.get(param)!= null && params.get(param).length() > 0 ){
                Parameter = params.get("postData");
            }
        }
        System.out.println("请求参数：" + Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        //获取请求的页码值及一页显示数目
        int pageNum = Integer.parseInt(jsonPost.getString("pageNum"));
        int pageSize = Integer.parseInt(jsonPost.getString("pageSize"));

        System.out.println(params.get("testType"));
        String urlType = url + "/" + "findallbydsid";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson( Parameter,urlType);
        if(params.get("responseData").equals("正常返回查询结果，对比结果值")){
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
            String SjySql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DS_ID\" = '" + jsonPost.getString("dataSourceId") + "'";
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
    }

    /*
     *@author shixj
     *@Description 根据关键字检索指定数据源内数据表接口
     *@Date 16:54 2018/7/12
     **/
    @DataProvider(name = "findByKW")
    public Iterator<Object[]> findByKW() {
        return new ExcelData("DataTableController", "findByKW");
    }
    @Test(dataProvider = "findByKW")
    public void testfindByKW(Map<String, String> params) {
        int totalnum = 0;
        int  count = 0;
        for(String param:params.keySet()){
            if(params.get(param)!= null && params.get(param).length() > 0 ){
                Parameter = params.get("postData");
            }
        }
        //获取请求的页码值及一页显示数目
        System.out.println("请求参数：" + Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        //获取请求的页码值及一页显示数目
        int pageNum = Integer.parseInt(jsonPost.getString("pageNum"));
        int pageSize = Integer.parseInt(jsonPost.getString("pageSize"));
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "findbykw";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter,urlType);
        if(params.get("responseData").equals("正常返回查询结果，对比结果值")){
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
            //按照关键字查询数据库
            //数据表中的关键字查询sql
            String tableSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" T where T.\"DS_ID\"='" + jsonPost.getString("dataSourceId") + "' " +
                    "and (T.\"DT_NAME\"||T.\"DT_PK\"||T.\"DT_ALIAS\"||T.\"REMARKS\") like '%" + params.get("keyWords") + "%'";
            //字段表中的是关键字查询sql
            String fieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" T where T.\"DS_ID\" = '" + jsonPost.getString("dataSourceId") + "' " +
            " and ( T.\"FIELD_ALIAS\" || T.\"FIELD_NAME\" || T.\"FIELD_TYPE\" || T.\"REMARKS\") like '%" + params.get("keyWords")  + "%'";
            //查询表总数目,统计总页数，当前页个数
            int tablenum = postgreConn.querynum(tableSql);//数据表记录数
            int fieldlnum = postgreConn.querynum(fieldSql);//字段表记录数
            totalnum = tablenum + fieldlnum;//总记录数
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

    }
    /*
    *@author shixj
    *@Description 根据唯一标识，获取指定数据表全部信息接口
    *@Date 16:55 2018/7/12
    **/
    @DataProvider(name = "getById")
    public Iterator<Object[]> getById() {
        return new ExcelData("DataTableController", "getById");
    }
    @Test(dataProvider = "getById")
    public void testgetById(Map<String, String> params) {
        String DT_NAME = null;
        String DT_PK = null;
        String DT_ALIAS = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        String FIELD_NAME = null;
        String FIELD_ALIAS = null;
        String FIELD_TYPE = null;
        for(String param:params.keySet()){
            if(params.get(param)!= null && params.get(param).length() > 0 ){
                Parameter = "dataTableId=" + params.get("dataTableId");
            }
        }
        System.out.println(Parameter);
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "getbyid";
        System.out.println(urlType);
        String URL = HttpRequest.httpGet(urlType, Parameter);
        if(params.get("responseData").equals("获取值与数据库值对比验证") == false){
            Assert.assertEquals(URL,params.get("responseData"));
        }else {
            //查询数据表信息
            String SjbSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_ID\" = '" + params.get("dataTableId") + "'";
            Map<String, String> sjb = postgreConn.SJB(SjbSql);
            for (String key : sjb.keySet()) {
                    DT_NAME = sjb.get("DT_NAME");
                    DT_PK = sjb.get("DT_PK");
                    DT_ALIAS = sjb.get("DT_ALIAS");
                    MODIFY_TIME = sjb.get("MODIFY_TIME");
                    REMARKS = sjb.get("REMARKS");
            }
            JSONObject json = JSONObject.parseObject(URL);
            String resultData = json.getString("resultData");
            JSONObject jsonTableBasicInfo = JSONObject.parseObject(resultData);
            String  TableBasicInfo =jsonTableBasicInfo.getString("dataTableBasicInfo");
            compared.comparedTable(TableBasicInfo,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);
            String fieldList = jsonTableBasicInfo.getString("fieldList");
            JSONArray fieldInfo = JSONArray.parseArray(fieldList);
            if(fieldList != null && !fieldList.isEmpty()){
                for(int i=0;i<fieldInfo.size();i++){
                    String  fieldBaseInfo =  fieldInfo.get(i).toString();
                    JSONObject jsonfield = JSONObject.parseObject(fieldBaseInfo);
                    //按照数据表名称查询字段表信息
                    String ZDSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_NAME\" = '" + jsonfield.getString("fieldName") + "'";
                    Map<String,String> zd = postgreConn.ZD(ZDSql);
                    for (String key : zd.keySet()) {
                        FIELD_NAME = zd.get("FIELD_NAME");
                        FIELD_ALIAS = zd.get("FIELD_ALIAS");
                        FIELD_TYPE = zd.get("FIELD_TYPE");
                        MODIFY_TIME = zd.get("MODIFY_TIME");
                        REMARKS = zd.get("REMARKS");
                    }
                    compared.comparedZD(fieldBaseInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
                }
            }else {
                System.out.println("目标数据表字段信息为空！");
            }
        }
    }
}