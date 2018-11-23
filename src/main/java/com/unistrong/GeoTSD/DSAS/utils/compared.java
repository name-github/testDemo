package com.unistrong.GeoTSD.DSAS.utils;

import com.alibaba.fastjson.JSONObject;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
/*
*@author shixj
*@Description 输入的参数值与数据库中数据库表获取到的实际值对比验证
*@Date 10:26 2018/7/18
**/
public class compared {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static boolean comparedDataSource(String dataSourceBasicInfo, String DS_NAME, String DS_TYPE,String MODIFY_TIME, String REMARKS) {
        //验证数据库基本信息
        JSONObject jsonDataSourceBasicInfo = JSONObject.parseObject(dataSourceBasicInfo);
        String dataSourceName = jsonDataSourceBasicInfo.getString("dataSourceName");
        System.out.println("dataSourceName的值：" + dataSourceName);
        try{
            if (dataSourceName == null || dataSourceName.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DS_NAME, dataSourceName); //验证数据源名称
            }
        String dataSourceType = jsonDataSourceBasicInfo.getString("dataSourceType");
        System.out.println("dataSourceType的值：" + dataSourceType);
        if (dataSourceType == null || dataSourceType.length() == 0) {
            System.out.println("请求参数中数据源类型为空！");
        } else {
            Assert.assertEquals(DS_TYPE, dataSourceType); //验证数据源类型
        }
        String modifyTime = jsonDataSourceBasicInfo.getString("modifyTime");
        if (modifyTime == null || modifyTime.length() == 0) {
            String currentTime = df.format(new Date());
            System.out.println("当前时间为：" + currentTime);
        } else {
            System.out.println("modifyTime的值：" + modifyTime);
            Assert.assertEquals(MODIFY_TIME, modifyTime); //验证数据源更新时间
        }
        String remarks = jsonDataSourceBasicInfo.getString("remarks");
        System.out.println("remarks的值：" + remarks);
        if (remarks == null || remarks.length() == 0) {
            System.out.println("请求参数中字段备注为空，查看字段表信息！");
        } else {
            Assert.assertEquals(REMARKS, remarks); //验证数据源备注信息
        }
        }catch (Exception e){
            System.out.println("请求参数中数据源名称为空！");
            return  false;
        }
        return true;
    }
    public static boolean comparedDatabase(String databaseBasicInfo, String DS_TYPE, String DS_ADDR, String DB_PORT, String DB_NAME, String DB_USERNAME,String DB_PWD) {
        //验证数据库基本信息
        JSONObject jsonDatabaseBasicInfo = JSONObject.parseObject(databaseBasicInfo);
        String dataBaseType = jsonDatabaseBasicInfo.getString("dataBaseType");
        System.out.println("dataBaseType的值：" + dataBaseType);
        if (dataBaseType == null || dataBaseType.length() == 0) {
            System.out.println("请求参数中数据库类型为空！");
        } else {
            Assert.assertEquals(DS_TYPE, dataBaseType); //验证数据库类型
        }
        String address = jsonDatabaseBasicInfo.getString("address");
        System.out.println("address的值：" + address);
        try{
            if (address == null || address.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DS_ADDR, address); //验证数据库访问地址
            }
        }catch (Exception e){
            System.out.println("请求参数中数据库访问地址为空！");
            return false;
        }
        String port = jsonDatabaseBasicInfo.getString("port");
        System.out.println("port的值：" + port);
        try{
            if (port == null || port.length() == 0) {

            } else {
                Assert.assertEquals(DB_PORT, port); //验证数据库的端口号
            }
        }catch (Exception e){
            System.out.println("请求参数中数据表PK为空，查看数据库表信息！");
            return false;
        }
        String dataBaseName = jsonDatabaseBasicInfo.getString("dataBaseName");
        System.out.println("dataBaseName的值：" + dataBaseName);
        try {
            if (dataBaseName == null || dataBaseName.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DB_NAME,dataBaseName); //验证数据库名称
            }
        }catch (Exception e){
            System.out.println("请求参数中数据库名称为空，新增数据库失败！");
            return false;
        }
        String userName = jsonDatabaseBasicInfo.getString("userName");
        System.out.println("userName的值：" + userName);
        try{
            if (userName == null || userName.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DB_USERNAME, userName); //验证数据库用户名
            }
        }catch (Exception e){
            System.out.println("请求参数中数据库用户名为空！");
            return false;
        }
        String password = jsonDatabaseBasicInfo.getString("password");
        System.out.println("password的值：" + password);
        try{
            if (password == null || password.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DB_PWD, password); //验证数据库用户密码
            }
        }catch (Exception e){
            System.out.println("请求参数中数据库用户密码为空！");
            return false;
        }
        return true;
    }
    /*
    *@author shixj
    *@Description 输入的参数值与数据库中文件表获取到的实际值对比验证
    *@Date 17:55 2018/7/23
    **/
    public static boolean comparedFile(String fileBasicInfo, String FILE_TYPE, String FILE_PATH) {
        //验证文件类型的数据源基本信息
        JSONObject jsonFileBasicInfo = JSONObject.parseObject(fileBasicInfo);
        String filePath = jsonFileBasicInfo.getString("filePath");
        System.out.println("filePath的值：" + filePath);
        try {
            if (filePath == null || filePath.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(FILE_PATH, filePath); //验证文件类型的数据源路径
            }
        }catch (Exception e){
            System.out.println("请求参数中文件类型的数据源路径为空！");
            return false;
        }
        String fileType = jsonFileBasicInfo.getString("fileType");
        System.out.println("fileType的值：" + fileType);
        try {
            if (fileType == null || fileType.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(FILE_TYPE, fileType); //验证文件类型的数据源文件类型
            }
        }catch (Exception e){
            System.out.println("请求参数中文件类型的数据源类型为空！");
            return false;
        }
        return true;
    }
    /*
    *@author shixj
    *@Description 输入的参数值与数据库中接口表获取到的实际值对比验证
    *@Date 17:56 2018/7/23
    **/
    public static boolean comparedInterface(String interFaceBasicInfo, String INTERFACE_TYPE, String INTERFACE_ADDR) {
        //验证接口类型的数据源基本信息
        JSONObject jsonInterFaceBasicInfo = JSONObject.parseObject(interFaceBasicInfo);
        String interfaceType = jsonInterFaceBasicInfo.getString("interfaceType");
        System.out.println("interfaceType的值：" + interfaceType);
        try {
            if (interfaceType == null || interfaceType.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(INTERFACE_TYPE, interfaceType); //验证文件类型的数据源文件类型
            }
        }catch (Exception e){
            System.out.println("请求参数中接口类型的数据源类型为空！");
            return false;
        }
        String interfaceAddress = jsonInterFaceBasicInfo.getString("interfaceAddress");
        System.out.println("interfaceAddress的值：" + interfaceAddress);
        try {
            if (interfaceAddress == null || interfaceAddress.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(INTERFACE_ADDR, interfaceAddress); //验证接口类型的数据源地址路径
            }
        }catch (Exception e){
            System.out.println("请求参数中接口类型的数据源路径为空！");
            return false;
        }
        return true;
    }
    /*
    *@author shixj
    *@Description 输入的参数值与数据库数据表获取到的实际值对比验证
    *@Date 17:57 2018/7/23
    **/
    public static boolean comparedTable(String dataTableBasicInfo, String DT_NAME, String DT_PK, String DT_ALIAS, String MODIFY_TIME, String REMARKS) {
        //验证表基本信息
        JSONObject jsonTableBasicInfo = JSONObject.parseObject(dataTableBasicInfo);
        String dataTableName = jsonTableBasicInfo.getString("dataTableName");
        System.out.println("dataTableName的值：" + dataTableName);
        try {
            if (dataTableName == null || dataTableName.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(DT_NAME, dataTableName); //验证数据表名称
            }
        String dataTableAlias = jsonTableBasicInfo.getString("dataTableAlias");
        System.out.println("dataTableAlias的值：" + dataTableAlias);
        if (dataTableAlias == null || dataTableAlias.length() == 0) {
            System.out.println("请求参数中数据表别名为空，查看数据库表信息！");
        } else {
            Assert.assertEquals(DT_ALIAS, dataTableAlias); //验证数据表别名
        }
        String dataTablePK = jsonTableBasicInfo.getString("dataTablePK");
        if (dataTablePK == null || dataTablePK.length() == 0) {
            System.out.println("请求参数中数据表PK为空，查看数据库表信息！");
        } else {
            System.out.println("dataTablePK的值：" + dataTablePK);
            Assert.assertEquals(DT_PK, dataTablePK); //验证数据表PK值
        }
        String remarks = jsonTableBasicInfo.getString("remarks");
        if (remarks == null || remarks.length() == 0) {
            System.out.println("请求参数中数据表备注为空，查看数据库表信息！");
        } else {
            System.out.println("remarks的值：" + remarks);
            Assert.assertEquals(REMARKS, remarks); //验证数据表备注信息
        }
        String updateTime = jsonTableBasicInfo.getString("updateTime");
        if (updateTime == null || updateTime.length() == 0) {
            String currentTime = df.format(new Date());
            System.out.println("当前时间点：" + currentTime);
        } else {
            System.out.println("updateTime的值：" + updateTime);
            Assert.assertEquals(MODIFY_TIME, updateTime); //验证数据表插入的时间
        }
        }catch (Exception e){
            System.out.println("请求参数中数据表名称为空！");
            return false;
        }
        return true;
    }
/*
*@author shixj
*@Description 输入的参数值与数据库字段表获取到的实际值对比验证
*@Date 17:57 2018/7/23
**/
    public static boolean comparedZD(String fieldList, String FIELD_NAME, String FIELD_ALIAS, String FIELD_TYPE, String MODIFY_TIME, String REMARKS) {
        //验证字段基本信息
        JSONObject jsonfieldList = JSONObject.parseObject(fieldList);
        String fieldName = jsonfieldList.getString("fieldName");
        System.out.println("fieldName的值：" + fieldName);
        try {
            if (fieldName == null || fieldName.length() == 0) {
                throw  new Exception();
            } else {
                Assert.assertEquals(FIELD_NAME, fieldName); //验证字段名称
            }
        String fieldAlias = jsonfieldList.getString("fieldAlias");
        System.out.println("fieldAlias的值：" + fieldAlias);
        if (fieldAlias == null || fieldAlias.length() == 0) {
            System.out.println("请求参数中字段别名为空，查看字段表信息！");
        } else {
            Assert.assertEquals(FIELD_ALIAS, fieldAlias); //验证字段别名
        }
        String fieldType = jsonfieldList.getString("fieldType");
        if (fieldType == null || fieldType.length() == 0) {
            System.out.println("请求参数中字段类型为空，查看字段表信息！");
        } else {
            System.out.println("fieldType的值：" + fieldType);
            Assert.assertEquals(FIELD_TYPE, fieldType); //验证字段类型
        }
        String remarks = jsonfieldList.getString("remarks");
        if (remarks == null || remarks.length() == 0) {
            System.out.println("请求参数中字段备注为空，查看字段表信息！");
        } else {
            System.out.println("remarks的值：" + remarks);
            Assert.assertEquals(REMARKS, remarks); //验证字段备注信息
        }
        String updateTime = jsonfieldList.getString("updateTime");
        if (updateTime == null || updateTime.length() == 0) {
            String currentTime = df.format(new Date());
            System.out.println("当前时间为：" + currentTime);
        } else {
            System.out.println("updateTime的值：" + updateTime);
            Assert.assertEquals(MODIFY_TIME, updateTime); //验证字段表插入的时间
        }
    } catch (Exception e) {
            System.out.println("请求参数中字段名称为空！");
            return false;
        }
        return true;
    }
}
