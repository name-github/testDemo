package com.unistrong.GeoTSD.DSAS.utils;

import java.util.Map;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class comparedVerify {
    /*
    *@author shixj
    *@Description 根据sql语句查询数据源基本信息表，验证请求参数与数据库插入值是否一致
    *@Date 15:58 2018/8/2
    **/
    public static boolean contrastDataSource(String SQLDataSource,String dataSourceBasicInfo){
        String DS_ID = null;//数据源id
        String DS_NAME = null;
        String DS_TYPE = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        Map<String, String> DataSourceInfo = postgreConn.SJYJBXX(SQLDataSource);
        for (String key : DataSourceInfo.keySet()) {
            DS_ID = DataSourceInfo.get("DS_ID");
            DS_NAME = DataSourceInfo.get("DS_NAME");
            DS_TYPE = DataSourceInfo.get("DS_TYPE");
            MODIFY_TIME = DataSourceInfo.get("MODIFY_TIME");
            REMARKS = DataSourceInfo.get("REMARKS");
        }
        return compared.comparedDataSource(dataSourceBasicInfo,DS_NAME,DS_TYPE,MODIFY_TIME,REMARKS);
    }
    /*
    *@author shixj
    *@Description 根据sql语句查询数据库基本信息表，验证请求参数与数据库插入值是否一致
    *@Date 15:58 2018/8/2
    **/
    public static boolean contrastDataBase(String SQLDataBase,String databaseBasicInfo){
        String DK_TYPE = null;
        String DS_ADDR = null;
        String DB_PORT = null;
        String DB_NAME = null;
        String DB_USERNAME = null;
        String DB_PWD = null;
        Map<String, String> dataBase = postgreConn.SJK(SQLDataBase);
        for(String key : dataBase.keySet()){
            DK_TYPE = dataBase.get("DK_TYPE");
            DS_ADDR = dataBase.get("DS_ADDR");
            DB_PORT = dataBase.get("DB_PORT");
            DB_NAME = dataBase.get("DB_NAME");
            DB_USERNAME = dataBase.get("DB_USERNAME");
            DB_PWD = dataBase.get("DB_PWD");
        }
        return compared.comparedDatabase(databaseBasicInfo,DK_TYPE,DS_ADDR,DB_PORT,DB_NAME,DB_USERNAME,DB_PWD);
    }
    /*
    *@author shixj
    *@Description 根据sql语句查询文件型数据源信息表，验证请求参数与数据库插入值是否一致
    *@Date 15:59 2018/8/2
    **/
    public static boolean contrastFile(String SQLFile,String fileBasicInfo){
        String DS_ID = null;
        String FILE_TYPE = null;
        String FILE_PATH = null;
        Map<String, String> TypeFile = postgreConn.SJYWJ(SQLFile);
        for (String key : TypeFile.keySet()) {
            DS_ID = TypeFile.get("DS_ID");
            FILE_TYPE = TypeFile.get("FILE_TYPE");
            FILE_PATH = TypeFile.get("FILE_PATH");
        }
        return compared.comparedFile(fileBasicInfo,FILE_TYPE,FILE_PATH);
    }

    /*
    *@author shixj
    *@Description 根据sql语句查询接口型数据源信息表，验证请求参数与数据库插入值是否一致
    *@Date 15:59 2018/8/2
    **/
    public static boolean contrastInterface(String SQLInterface,String interfaceBasicInfo){
        String DS_ID = null;
        String INTERFACE_TYPE = null;
        String INTERFACE_ADDR = null;
        Map<String, String> TyprInterface = postgreConn.SJYJK(SQLInterface);
        for (String key : TyprInterface.keySet()) {
            DS_ID = TyprInterface.get("DS_ID");
            INTERFACE_TYPE = TyprInterface.get("INTERFACE_TYPE");
            INTERFACE_ADDR = TyprInterface.get("INTERFACE_ADDR");
        }
        return compared.comparedInterface(interfaceBasicInfo,INTERFACE_TYPE,INTERFACE_ADDR);
    }
    /*
    *@author shixj
    *@Description 根据sql语句查询数据表基本信息，验证请求参数与数据库插入值是否一致
    *@Date 15:59 2018/8/2
    **/
    public static boolean contrastTable(String SQLTable,String dataTableBasicInfo){
        String DT_ID = null;
        String DS_ID = null;
        String DT_NAME = null;
        String DT_PK = null;
        String DT_ALIAS = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        Map<String, String> DataTable = postgreConn.SJB(SQLTable);
        for (String key : DataTable.keySet()) {
            DS_ID = DataTable.get("DS_ID");
            DT_ID = DataTable.get("DT_ID");
            DT_NAME = DataTable.get("DT_NAME");
            DT_PK = DataTable.get("DT_PK");
            DT_ALIAS = DataTable.get("DT_ALIAS");
            MODIFY_TIME = DataTable.get("MODIFY_TIME");
            REMARKS = DataTable.get("REMARKS");
        }
        return compared.comparedTable(dataTableBasicInfo,DT_NAME,DT_PK,DT_ALIAS,MODIFY_TIME,REMARKS);
    }
    /*
    *@author shixj
    *@Description 根据sql语句查询字段基本信息表，验证请求参数与数据库插入值是否一致
    *@Date 16:00 2018/8/2
    **/
    public static boolean contrastField(String SQLField,String FieldInfo){
        String DT_ID = null;
        String DS_ID = null;
        String FIELD_ID = null;
        String FIELD_NAME = null;
        String FIELD_ALIAS = null;
        String FIELD_TYPE = null;
        String MODIFY_TIME = null;
        String REMARKS = null;
        Map<String, String> Field = postgreConn.ZD(SQLField);
        for (String key : Field.keySet()) {
            DT_ID = Field.get("DT_ID");
            FIELD_ID = Field.get("FIELD_ID");
            FIELD_NAME = Field.get("FIELD_NAME");
            FIELD_ALIAS = Field.get("FIELD_ALIAS");
            FIELD_TYPE = Field.get("FIELD_TYPE");
            MODIFY_TIME = Field.get("MODIFY_TIME");
            REMARKS = Field.get("REMARKS");
        }
        return compared.comparedZD(FieldInfo,FIELD_NAME,FIELD_ALIAS,FIELD_TYPE,MODIFY_TIME,REMARKS);
    }
}
