import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unistrong.GeoTSD.DSAS.utils.ExcelData;
import com.unistrong.GeoTSD.DSAS.utils.HttpRequest;
import com.unistrong.GeoTSD.DSAS.utils.postgreConn;
import com.unistrong.GeoTSD.DSAS.utils.comparedVerify;
import com.unistrong.GeoTSD.DSAS.utils.comparedPageNum;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class MultiinterfaceIntegrationTest {
    String url = "http://localhost:9090/dsas/api";
    String Parameter = "";

    @DataProvider(name = "AddData")
    public Iterator<Object[]> AddData() {
        return new ExcelData("MultiinterfaceIntegration", "test");
    }

    @Test(dataProvider = "AddData", timeOut = 1000)
    public void AddData(Map<String, String> params) {
        String DS_ID = null;
        String DT_ID = null;
        String FIELD_ID = null;
        String dataSourceId = null;

        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("DataSource");
            }
        }
        System.out.println("数据源请求参数：" + Parameter);
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        String newDataSourceBasicInfo = jsonPost.getString("newDataSourceBasicInfo");//数据源基本信息
        String newDataBaseConnInfo = jsonPost.getString("newDataBaseConnInfo");//数据库基本信息
        String newFilesConnInfo = jsonPost.getString("newFilesConnInfo");//文件基本信息
        String newInterfaceConnInfo = jsonPost.getString("newInterfaceConnInfo");//接口基本信息
        JSONObject jsonDataSource = JSONObject.parseObject(newDataSourceBasicInfo);
        int dataSourceType = jsonDataSource.getInteger("dataSourceType");//获取得到数据源类型
        System.out.println(params.get("testType"));
        Reporter.log("类型：" + params.get("testType"));
        String urlSource = url + "/" + "datasource/addnew";
        System.out.println("新增数据源：" + urlSource);
        Reporter.log("请求地址：" + urlSource);
        String URLSorce = HttpRequest.httpJson(Parameter, urlSource); //调用新增数据源接口
        if (params.get("responseDataSource").equals("新增数据源信息成功")) {
            JSONObject jsonResult = JSONObject.parseObject(URLSorce);
            JSONObject jsonObject = JSONObject.parseObject(jsonResult.getString("resultData"));
            //获取到新增的数据源id
            dataSourceId = jsonObject.getString("dataSourceId");
            //验证数据源是否已经新增成功
            String DataSourceSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
            Assert.assertTrue(comparedVerify.contrastDataSource(DataSourceSQL, newDataSourceBasicInfo));
            //验证返回参数与期望参数是否一致
            String responseData = "{\"resultData\":{\"dataSourceId\":\"" + dataSourceId + "\"},\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URLSorce, responseData);
            System.out.println("新增数据源响应结果：" + URLSorce);
            switch (dataSourceType) {
                case 1:
                    String dataBaseSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Assert.assertTrue(comparedVerify.contrastDataBase(dataBaseSql, newDataBaseConnInfo));
                    break;
                case 2:
                    String fileSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Assert.assertTrue(comparedVerify.contrastFile(fileSql, newFilesConnInfo));
                    break;
                case 3:
                    String interFaceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + dataSourceId + "'";
                    Assert.assertTrue(comparedVerify.contrastInterface(interFaceSql, newInterfaceConnInfo));
                    break;
                default:
                    break;
            }
            //根据生成的数据源id，新增数据表
            String DataTable = params.get("DataTable");
            String postTable = "{\"dataSourceId\":\"" + dataSourceId + "\"," + DataTable + "}"; //拼接成新增数据表的请求参数
            String urlTable = url + "/" + "datatable/addnews";
            System.out.println("新增数据表：" + urlTable);
            Reporter.log("请求地址：" + urlTable);
            String URLTable = HttpRequest.httpJson(postTable, urlTable);//调用新增数据表接口
            if (params.get("responseDataTable").equals("新增数据表信息成功")) {
                String listDataSourceId = JSONObject.parseObject(URLTable).getString("resultData"); //数据表数组
                JSONArray arr = JSONArray.parseArray(listDataSourceId);
                String expect = "{\"resultData\":" + arr + ",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                Assert.assertEquals(URLTable, expect);
                System.out.println("新增数据表响应结果：" + URLTable);
                for (int i = 0; i < arr.size(); i++) {
                    String DataTableId = String.valueOf(arr.get(i));
                    //验证数据表基本信息
                    String SQLTable = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_ID\" = '" + DataTableId + "'";
                    String newTableInfoList = DataTable.substring(23);
                    JSONArray newDataTableInfo = JSONArray.parseArray(newTableInfoList);
                    String TableBaseInfo = newDataTableInfo.get(i).toString();
                    JSONObject jsonTableBaseinfo = JSONObject.parseObject(TableBaseInfo);
                    String dataTableBasicInfo = jsonTableBaseinfo.getString("dataTableBasicInfo");
                    Assert.assertTrue(comparedVerify.contrastTable(SQLTable, dataTableBasicInfo));
                    String fieldList = jsonTableBaseinfo.getString("fieldList");
                    JSONArray fieldInfo = JSONArray.parseArray(fieldList);
                    if(fieldList != null && !fieldList.isEmpty()) {
                        for (int n = 0; n < fieldInfo.size(); n++) {
                            String fieldBaseInfo = fieldInfo.get(n).toString();
                            JSONObject jsonfield = JSONObject.parseObject(fieldBaseInfo);
                            String fieldName = jsonfield.getString("fieldName");
                            String FieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_NAME\" = '" + fieldName + "'";
                            Assert.assertTrue(comparedVerify.contrastField(FieldSql, fieldBaseInfo));
                        }
                    }
                    //根据生成的数据表id，新增字段信息
                    String DataField = params.get("DataField");
                    String postField = "{\"dataTableId\":\"" + DataTableId + "\"," + DataField + "}"; //拼接成新增字段的请求参数
                    String urlField = url + "/" + "field/addnews";
                    System.out.println("新增字段:" + urlField);
                    String URLField = HttpRequest.httpJson(postField, urlField); //调用新增字段接口
                    if (params.get("responseDataField").equals("新增字段信息成功")) {
                        String listFieldId = JSONObject.parseObject(URLField).getString("resultData");
                        JSONArray arrfield = JSONArray.parseArray(listFieldId);
                        String expectFile = "{\"resultData\":" + arrfield + ",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                        Assert.assertEquals(URLField, expectFile);
                        System.out.println("添加字段响应结果：" + URLField);
                        for (int j = 0; j < arrfield.size(); j++) {
                            String FieldId = String.valueOf(arrfield.get(j));
                            //验证数据表字段信息
                            String FieldSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '" + FieldId + "'";
                            Map<String, String> Field = postgreConn.ZD(FieldSql);
                            for (String key : Field.keySet()) {
                                FIELD_ID = Field.get("FIELD_ID");
                                DS_ID = Field.get("DS_ID");
                                DT_ID = Field.get("DT_ID");
                            }
                            Assert.assertEquals(FIELD_ID, FieldId);
                            Assert.assertEquals(DS_ID, dataSourceId);
                            Assert.assertEquals(DT_ID, DataTableId);
                            String newFieldInfoList = DataField.substring(19);
                            JSONArray newDataFieldInfo = JSONArray.parseArray(newFieldInfoList);
                            String FieldBaseInfo = newDataFieldInfo.get(j).toString();
                            Assert.assertTrue(comparedVerify.contrastField(FieldSql, FieldBaseInfo));
                        }
                    }else {
                        //数据表字段失败，与预期结果值对比
                        Assert.assertEquals(URLSorce, params.get("responseDataField"));
                    }
                }
            }else {
                //数据表新增失败，与预期结果值对比
                Assert.assertEquals(URLSorce, params.get("responseDataTable"));
            }
        }else {
            //数据源新增失败，与预期结果值对比
            Assert.assertEquals(URLSorce, params.get("responseDataSource"));
        }
        Reporter.log("请求结果：" + URLSorce);
    }

    @DataProvider(name = "EditTable")
    public Iterator<Object[]> EditTable() {
        return new ExcelData("MultiinterfaceIntegration", "EditTable");
    }

    @Test(dataProvider = "EditTable", timeOut = 1000)
    public void EditTable(Map<String, String> params) {
        String DT_ID = null;
        String DS_ID = null;
        String FIELD_ID = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("EditTable");
            }
        }
        String tablename = params.get("TableName");
        String TBNameSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_NAME\" = '"+tablename+"'";
        Map<String, String> TB = postgreConn.SJB(TBNameSQL);
        for (String key : TB.keySet()) {
            DT_ID = TB.get("DT_ID");
            DS_ID = TB.get("DS_ID");
        }
        String EditTable = params.get("EditTable");
        String postEditTable = "{\"dataTableId\":\""+ DT_ID + "\"," + EditTable + "}";
        System.out.println(params.get("testType"));
        String urlEdit = url + "/" + "datatable/update";
        System.out.println("编辑数据表基本属性："+ urlEdit);
        String URLEdit = HttpRequest.httpJson(postEditTable, urlEdit);
        if(params.get("responseEditTable").equals("更新成功") == false){
            Assert.assertEquals(URLEdit,params.get("responseEditTable"));
        }else {
            String expected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URLEdit, expected);
            //验证更新后数据库的信息
            String  EditTableSQl = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_ID\" = '"+ DT_ID +"'";
            Assert.assertTrue(comparedVerify.contrastTable(EditTableSQl,postEditTable));

            //编辑指定字段的基本属性
            String FieldSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"DT_ID\" = '"+ DT_ID + "'";
            Map<String, String> Field = postgreConn.ZD(FieldSQL);
            for (String key : Field.keySet()) {
                FIELD_ID = Field.get("FIELD_ID");
                DS_ID = Field.get("DS_ID");
            }
            String EditField = params.get("EditField");
            String postEditField = "{\"fieldId\":\""+ FIELD_ID + "\"," + EditField + "}";
            String urlEditField = url + "/" + "field/update";
            System.out.println("编辑字段基本属性："+ urlEditField);
            String URLEditField = HttpRequest.httpJson(postEditField, urlEditField);
            if(params.get("responseEditField").equals("更新成功") == false){
                Assert.assertEquals(URLEditField,params.get("responseEditField"));
            }else {
                String EditFieldexpected = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                Assert.assertEquals(URLEditField, EditFieldexpected);
                //验证更新后数据库的信息
                String  EditFieldSQl = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '"+ FIELD_ID +"'";
                Assert.assertTrue(comparedVerify.contrastField(EditFieldSQl,postEditField));
            }
        }
    }

    @DataProvider(name = "EditDataSource")
    public Iterator<Object[]> EditDataSource() {
        return new ExcelData("MultiinterfaceIntegration", "EditDataSource");
    }

    @Test(dataProvider = "EditDataSource")
    public void EditDataSource(Map<String, String> params) {
        String DS_ID = null;
        String DS_TYPE = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("EditDB");
            }
        }
        String DSname = params.get("DSName");
        String DSSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_NAME\" = '"+ DSname + "'";
        Map<String, String> DS = postgreConn.SJYJBXX(DSSQL);
        for (String key : DS.keySet()) {
            DS_ID = DS.get("DS_ID");
            DS_TYPE = DS.get("DS_TYPE");
        }
        //重命名数据源名称
        String ReDSName = params.get("ReDSName");
        String postReDSName = "{\"dataSourceId\":\""+ DS_ID + "\"," + ReDSName + "}";
        System.out.println(params.get("testType"));
        String urlRename = url + "/" + "datasource/rename";
        System.out.println("数据源重命名："+ urlRename);
        String URLReDSName = HttpRequest.httpJson(postReDSName, urlRename);
        if(params.get("responseReDSName").equals("重命名成功") == false){
            Assert.assertEquals(URLReDSName,params.get("responseReDSName"));
        }else {
            String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
            Assert.assertEquals(URLReDSName,responseData);
            String RenameSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '"+ DS_ID + "'";
            Assert.assertTrue(comparedVerify.contrastDataSource(RenameSql,postReDSName));
        }
        //编辑数据源连接信息
        String EditDBconn = params.get("EditDBconn");
        String postEditDB = "{\"dataSourceId\":\""+ DS_ID + "\"," + EditDBconn + "}";
        switch (Integer.parseInt(DS_TYPE)){
            case 1:
                String urlDB = url + "/" + "datasource/updatedbconninfo";
                System.out.println("数据库连接信息："+ urlDB);
                String URLDB = HttpRequest.httpJson(postEditDB, urlDB);
                if(params.get("responseDBconn").equals("数据库连接信息修改成功") == false){
                    Assert.assertEquals(URLDB,params.get("responseDBconn"));
                }else{
                    String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                    Assert.assertEquals(URLDB,responseData);
                    String dbconnSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYSJK\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                    Assert.assertTrue(comparedVerify.contrastDataBase(dbconnSQL,postEditDB));
                }
                break;
            case 2:
                String urlfile = url + "/" + "datasource/updatefileconninfo";
                System.out.println("文件型连接信息："+ urlfile);
                String URLFile = HttpRequest.httpJson(postEditDB, urlfile);
                if(params.get("responseDBconn").equals("文件型连接信息修改成功") == false){
                    Assert.assertEquals(URLFile,params.get("responseDBconn"));
                }else{
                    String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                    Assert.assertEquals(URLFile,responseData);
                    String fileconnSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYWJ\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                    Assert.assertTrue(comparedVerify.contrastFile(fileconnSQL,postEditDB));
                }
                break;
            case 3:
                String urlInterF = url + "/" + "datasource/updateinterfconninfo";
                System.out.println("文件型连接信息:"+ urlInterF);
                String URLInterF = HttpRequest.httpJson(postEditDB, urlInterF);
                if(params.get("responseDBconn").equals("接口型连接信息修改成功") == false){
                    Assert.assertEquals(URLInterF,params.get("responseDBconn"));
                }else{
                    String responseData = "{\"resultData\":\"\",\"resultState\":\"success\",\"resultCode\":\"200\"}";
                    Assert.assertEquals(URLInterF,responseData);
                    String interfconnSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJK\" t where t.\"DS_ID\" = '" + DS_ID + "'";
                    Assert.assertTrue(comparedVerify.contrastInterface(interfconnSQL,postEditDB));
                }
                break;
            default:
                break;
        }
    }

    @DataProvider(name = "FindAllData")
    public Iterator<Object[]> FindAllData() {
        return new ExcelData("MultiinterfaceIntegration", "FindAllData");
    }

    @Test(dataProvider = "FindAllData",timeOut = 1000)
    public void FindAllData(Map<String, String> params) {
        String DS_ID = null;
        String DS_TYPE = null;
        String DT_ID = null;
        for (String param : params.keySet()) {
            if (params.get(param) != null && params.get(param).length() > 0) {
                Parameter = params.get("PostPage");
            }
        }
        String DSname = params.get("DSName");
        String DSSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_NAME\" = '" + DSname + "'";
        Map<String, String> DS = postgreConn.SJYJBXX(DSSQL);
        for (String key : DS.keySet()) {
            DS_ID = DS.get("DS_ID");
            DS_TYPE = DS.get("DS_TYPE");
        }
        //根据页码值获取数据源索引信息列表
        JSONObject jsonPost = JSONObject.parseObject(Parameter);
        int pageNum = Integer.parseInt(jsonPost.getString("pageNum"));
        int pageSize = Integer.parseInt(jsonPost.getString("pageSize"));
        System.out.println(params.get("testType"));
        String urlSource = url + "/" + "datasource/findallindex"; //获取全部数据源索引信息列表接口
        System.out.println("获取数据源索引信息：" + urlSource);
        String URLSource = HttpRequest.httpJson(Parameter, urlSource);
        if (params.get("response").equals("正常查询，对比结果") == false) {
            Assert.assertEquals(URLSource, params.get("response"));
        } else {
            String DatasourceSql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\"";
            comparedPageNum.comPageNum(pageSize,pageNum,URLSource,DatasourceSql);
            //查询相应数据源内全部数据表信息
            int endindex = Parameter.indexOf("}") ;
            String postPage = Parameter.substring(1,endindex);
            String findAllTabel = "{\"dataSourceId\":\"" + DS_ID + "\"," + postPage + "}";
            String urlTable = url + "/" + "datatable/findallbydsid"; //获取指定数据源下的所有数据表接口
            System.out.println("获取指定数据源内数据表信息：" + urlTable);
            String URLTable = HttpRequest.httpJson(findAllTabel, urlTable);
            String TableSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DS_ID\" = '" + DS_ID + "'";
            comparedPageNum.comPageNum(pageSize,pageNum,URLTable,TableSQL);
            //根据数据表响应结果查询数据表名称
            JSONObject jsonResult = JSONObject.parseObject(JSONObject.parseObject(URLTable).getString("resultData"));
            String list = jsonResult.getString("list");
            JSONArray listArray = JSONArray.parseArray(list);
            int tableNum = listArray.size(); //list对象数组中包含的数据表个数
            if(tableNum !=0 && tableNum > 0){
                for(int i=0;i<tableNum;i++){
                    String DTInfo = listArray.getString(i);
                    String DTname = JSONObject.parseObject(DTInfo).getString("dataTableName");
                    String DTidSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJB\" t where t.\"DT_NAME\" = '" + DTname + "'";
                    Map<String, String> DT = postgreConn.SJB(DTidSQL);
                    for (String key : DT.keySet()) {
                        DT_ID = DT.get("DT_ID");
                    }
                    //根据数据表id获取对应表多有字段信息
                    String urlField= url + "/" + "field/findallbydtid";
                    System.out.println("获取指定数据表的所有字段信息："+urlField);
                    String URLField = HttpRequest.httpGet(urlField, DT_ID);
                    String responseResult = JSONObject.parseObject(URLField).getString("resultData");
                    String listfield = JSONObject.parseObject(responseResult).getString("list");
                    JSONArray Arrayfield = JSONArray.parseArray(listfield);
                    for(int j =0;j<Arrayfield.size();j++){
                        String fieldInfo = Arrayfield.getString(j);
                        String fieldSQL = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_ZD\" t where t.\"FIELD_ID\" = '" +JSONObject.parseObject(fieldInfo).getString("fieldId") + "'";
                        Assert.assertTrue(comparedVerify.contrastField(fieldSQL,fieldInfo));
                    }
                }
            }else{
                System.out.println("当前数据源对应的数据表为空！");
            }
        }
    }


}

