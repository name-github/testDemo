import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unistrong.GeoTSD.DSAS.utils.ExcelData;
import com.unistrong.GeoTSD.DSAS.utils.HttpRequest;
import com.unistrong.GeoTSD.DSAS.utils.compared;
import com.unistrong.GeoTSD.DSAS.utils.postgreConn;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import java.util.Iterator;
import java.util.Map;


public class FieldControllerTest {

    /**
     * @Author:
     * @Description
     * @Data:$time$ $date$
     */
    int fag = 0;    //测试用例编号
    String url = "http://localhost:9090/dsas/api/field";
    String Parameter = "";

    @BeforeClass
    public void beforeClass() {
        System.out.println("DataTableControllerTest类开始：");
    }

    @AfterClass
    public void afterClass() {
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
     *@Description 为指定的数据表，批量添加字段接口
     *@Date 10:28 2018/7/13
     **/
    @DataProvider(name = "addnews")
    public Iterator<Object[]> addnews() {
        return new ExcelData("FieldController", "addnews");
    }

    @Test(dataProvider = "addnews")
    public void testAddNews(Map<String, String> params) {
        String DT_ID = null;
        String DS_ID = null;
        String FIELD_ID = null;
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
        System.out.println(params.get("testType"));
        String urlType = url + "/" + "addnews";
        System.out.println(urlType);
        String URL = HttpRequest.httpJson(Parameter, urlType);
        JSONObject json = JSONObject.parseObject(URL);
        //json字符串第一层
        String listDataSourceId = json.getString("resultData");
        JSONArray arr = JSONArray.parseArray(listDataSourceId);
        String responseResult = params.get("responseData");
        if (responseResult.equals("新增成功，数据库验证") == false) {
            //验证返回参数与期望参数是否一致
            Assert.assertEquals(URL, params.get("responseData"));
        } else {
            String expect = "{\"resultData\":" + arr + ",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            //响应结果值中的数据源id自动验证
            Assert.assertEquals(URL, expect);
            //分解json字符数组中的字段id
            for (int i = 0; i < arr.size(); i++) {
                String FieldId = String.valueOf(arr.get(i));
                System.out.println(FieldId);
                //验证新增的数据表在数据库中是否存在
                //查询字段表信息
                String FieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '" + FieldId + "'";
                Map<String, String> Field = postgreConn.ZD(FieldSql);
                for (String key : Field.keySet()) {
                    DT_ID = Field.get("DT_ID");
                    FIELD_ID = Field.get("FIELD_ID");
                    FIELD_NAME = Field.get("FIELD_NAME");
                    FIELD_ALIAS = Field.get("FIELD_ALIAS");
                    FIELD_TYPE = Field.get("FIELD_TYPE");
                    MODIFY_TIME = Field.get("MODIFY_TIME");
                    REMARKS = Field.get("REMARKS");
                }
                //验证新增的字段id在字段基本信息表中是否正确：
                Assert.assertEquals(FIELD_ID, FieldId);//验证字段表id
                //验证数据库中的数据源基本信息是否与输入的一致
                String Request = params.get("postData");
                JSONObject jsonRequest = JSONObject.parseObject(Request);
                String dataTableId = jsonRequest.getString("dataTableId");
                Assert.assertEquals(DT_ID,dataTableId);//验证数据表id
                String newFieldInfoList = jsonRequest.getString("newFieldInfoList");
                JSONArray newDataFieldInfo = JSONArray.parseArray(newFieldInfoList);
                String  FieldBaseInfo = newDataFieldInfo.get(i).toString();//数据表基本信息数目与数据表id对应一致
                compared.comparedZD(FieldBaseInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
                }
            }
        }

        /*
         *@author shixj
         *@Description 从指定数据表中，移除指定字段接口
         *@Date 10:28 2018/7/13
         **/
        @DataProvider(name = "remove")
        public Iterator<Object[]> remove () {
            return new ExcelData("FieldController", "remove");
        }
        @Test(dataProvider = "remove")
        public void testRemove (Map < String, String > params){
            for (String param : params.keySet()) {
                if (params.get(param) != null && params.get(param).length() > 0) {
                    Parameter = params.get("postData");
                }
            }
            System.out.println(params.get("testType"));
            String urlType = url + "/" + "remove";
            System.out.println(urlType);
            String URL = HttpRequest.httpJson(Parameter, urlType);
            if (params.get("responseData").equals("移除成功")==false){
                //验证返回参数与期望参数是否一致
                Assert.assertEquals(URL, params.get("responseData"));
            }else {
                String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                Assert.assertEquals(URL,expected);
                //验证已移除的字段是否还存在数据库字段表中
                JSONObject jsonRequest = JSONObject.parseObject(Parameter);
                String postField = jsonRequest.getString("fieldIds");
                String [] field = postField.split(",");
                int len = field.length;
                for(int i=0;i<len;i++){
                  String   FieldId = field[i];
                    //查询字段表信息
                    String FieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '" + FieldId + "'";
                    System.out.println(FieldSql);
                  Assert.assertEquals(postgreConn.querynum(FieldSql),0);
                }
            }
        }
        /*
         *@author shixj
         *@Description 从清空指定数据表内全部字段接口
         *@Date 10:28 2018/7/13
         **/

        @DataProvider(name = "clear")
        public Iterator<Object[]> clear () {
            return new ExcelData("FieldController", "clear");
        }
        @Test(dataProvider = "clear")
        public void testClear (Map < String, String > params){
            for (String param : params.keySet()) {
                if (params.get(param) != null && params.get(param).length() > 0) {
                    Parameter = params.get("postData");
                }
            }
            System.out.println(Parameter);
            System.out.println(params.get("testType"));
            String urlType = url + "/" + "clear";
            System.out.println(urlType);
            String URL = HttpRequest.httpJson(Parameter, urlType);
            if (params.get("responseData").equals("指定表成功清空")==false){
                //验证返回参数与期望参数是否一致
                Assert.assertEquals(URL, params.get("responseData"));
            }else {
                String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                Assert.assertEquals(URL,expected);
                //验证已移除的数据表是否还存在数据库字段表中
                JSONObject jsonRequest = JSONObject.parseObject(Parameter);
                String postTableIds = jsonRequest.getString("dataTableIds");
                String [] table = postTableIds.split(",");
                int len = table.length;
                for(int i=0;i<len;i++){
                    String   TableId = table[i];
                    //查询字段表中的数据表信息
                    String TableIdSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"DT_ID\" = '" + TableId + "'";
                    System.out.println(TableIdSql);
                    Assert.assertEquals(postgreConn.querynum(TableIdSql),0);
                }
            }
        }
        /*
         *@author shixj
         *@Description 获取指定数据表的全部字段属性信息列表接口
         *@Date 10:28 2018/7/13
         **/
        @DataProvider(name = "findallbydtid")
        public Iterator<Object[]> findallbydtid () {
            return new ExcelData("FieldController", "findallbydtid");
        }
        @Test(dataProvider = "findallbydtid")
        public void testFindAllByDtId (Map < String, String > params){
            for (String param : params.keySet()) {
                if (params.get(param) != null && params.get(param).length() > 0) {
                    Parameter = "dataTableId=" + params.get("dataTableId");
                }
            }
            System.out.println(Parameter);
            System.out.println(params.get("testType"));
            String urlType = url + "/" + "findallbydtid";
            System.out.println(urlType);
            String URL = HttpRequest.httpGet(urlType, Parameter);
            if(params.get("responseData").equals("与数据库表信息验证")==false){
                //验证返回参数与期望参数是否一致
                Assert.assertEquals(URL, params.get("responseData"));
            }else{
                 //分步读取响应结果值各个参数值
                JSONObject json = JSONObject.parseObject(URL);
                //json字符串第一层
                String resultData = json.getString("resultData");
                System.out.println("返回值错误，验证待定！" + resultData);
                JSONObject jsonObject = JSONObject.parseObject(resultData);
                String list = jsonObject.getString("list");
                JSONArray listArray = JSONArray.parseArray(list);
            }
        }
        /*
         *@author shixj
         *@Description 编辑指定字段的基本属性接口
         *@Date 10:29 2018/7/13
         **/
        @DataProvider(name = "update")
        public Iterator<Object[]> update () {
            return new ExcelData("FieldController", "update");
        }
        @Test(dataProvider = "update")
        public void testUpdate (Map < String, String > params){
            String DT_ID = null;
            String DS_ID = null;
            String FIELD_ID = null;
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
            String urlType = url + "/" + "update";
            System.out.println(urlType);
            String URL = HttpRequest.httpJson(Parameter, urlType);
            if(params.get("responseData").equals("验证数据库是否正常更新")==false){
                //验证返回参数与期望参数是否一致
                Assert.assertEquals(URL, params.get("responseData"));
            }else{
                String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                Assert.assertEquals(URL,expected);
                JSONObject json = JSONObject.parseObject(Parameter);
                String fieldId = json.getString("fieldId");
                //查询字段表中的数据表信息
                String FieldIdSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '" + fieldId + "'";
                Map<String, String> Field = postgreConn.ZD(FieldIdSql);
                for (String key : Field.keySet()) {
                    DS_ID = Field.get("DS_ID");
                    DT_ID = Field.get("DT_ID");
                    FIELD_ID = Field.get("FIELD_ID");
                    FIELD_NAME = Field.get("FIELD_NAME");
                    FIELD_ALIAS = Field.get("FIELD_ALIAS");
                    FIELD_TYPE = Field.get("FIELD_TYPE");
                    MODIFY_TIME = Field.get("MODIFY_TIME");
                    REMARKS = Field.get("REMARKS");
                }
                //验证更新的字段id在字段基本信息表中是否正确：
                Assert.assertEquals(FIELD_ID, fieldId);//验证字段表id
                Assert.assertEquals(DT_ID,json.get("dataTableId"));
                //验证数据库中的数据源基本信息是否与输入的一致
                compared.comparedZD(Parameter,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
            }
        }
    }
