package com.unistrong.GeoTSD.DSAS.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class postgreConn {
    static Statement statement = null;
    static Connection connection = null;
    static  ResultSet resultSet = null;
    //数据库连接方式
    private static String drivername = "org.postgresql.Driver";
    static String url = "jdbc:postgresql://192.168.251.78:1328/dsas";
    static String user = "beyondb";
    static String password = "123456";

    /*
    *@author shixj
    *@Description 数据源基本信息
    *@Date 14:05 2018/7/16
    **/

    public static Map<String ,String > SJYJBXX(String sql) {
        Map<String,String> sjkjbxx = new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            //查询的sql语句
            /*     String sql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '1b1866437e574be09cd0f035887e3ac2'";*/
            resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String DS_ID = resultSet.getString("DS_ID");
                String DS_NAME = resultSet.getString("DS_NAME");
                String DS_TYPE = resultSet.getString("DS_TYPE");
                String MODIFY_TIME =resultSet.getString("MODIFY_TIME");
                String REMARKS = resultSet.getString("REMARKS");
            //    System.out.println("新增数据源基本信息如下：\n DS_ID:" + DS_ID + "\n DS_NAME:" + DS_NAME + "\n DS_TYPE:" + DS_TYPE);
                sjkjbxx.put("DS_ID",DS_ID);
                sjkjbxx.put("DS_NAME",DS_NAME);
                sjkjbxx.put("DS_TYPE",DS_TYPE);
                sjkjbxx.put("MODIFY_TIME",MODIFY_TIME);
                sjkjbxx.put("REMARKS",REMARKS);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
      return sjkjbxx;
    }
    /*
    *@author shixj
    *@Description 数据库基本信息
    *@Date 14:05 2018/7/16
    **/
    public static Map<String ,String > SJK(String sql) {
        Map<String,String> sjk = new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String DS_ID = resultSet.getString("DS_ID");
                String DK_TYPE = resultSet.getString("DS_TYPE");
                String DS_ADDR = resultSet.getString("DS_ADDR");
                String DB_PORT = resultSet.getString("DB_PORT");
                String DB_NAME = resultSet.getString("DB_NAME");
                String DB_USERNAME = resultSet.getString("DB_USERNAME");
                String DB_PWD = resultSet.getString("DB_PWD");
                sjk.put("DS_ID",DS_ID);
                sjk.put("DK_TYPE",DK_TYPE);
                sjk.put("DS_ADDR",DS_ADDR);
                sjk.put("DB_PORT",DB_PORT);
                sjk.put("DB_NAME",DB_NAME);
                sjk.put("DB_USERNAME",DB_USERNAME);
                sjk.put("DB_PWD",DB_PWD);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return sjk;
    }
    /*
    *@author shixj
    *@Description 数据源接口基本信息
    *@Date 14:06 2018/7/16
    **/
    public static Map<String ,String > SJYJK(String sql) {
        Map<String,String> sjkjk = new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String DS_ID = resultSet.getString("DS_ID");
                String INTERFACE_TYPE = resultSet.getString("INTERFACE_TYPE");
                String INTERFACE_ADDR = resultSet.getString("INTERFACE_ADDR");
                sjkjk.put("DS_ID",DS_ID);
                sjkjk.put("INTERFACE_TYPE",INTERFACE_TYPE);
                sjkjk.put("INTERFACE_ADDR",INTERFACE_ADDR);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return sjkjk;
    }
    /*
    *@author shixj
    *@Description 数据源文件基本信息
    *@Date 14:06 2018/7/16
    **/
    public static Map<String ,String > SJYWJ(String sql) {
        Map<String,String> sjkwj = new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String DS_ID = resultSet.getString("DS_ID");
                String FILE_TYPE = resultSet.getString("FILE_TYPE");
                String FILE_PATH = resultSet.getString("FILE_PATH");
                sjkwj.put("DS_ID",DS_ID);
                sjkwj.put("FILE_TYPE",FILE_TYPE);
                sjkwj.put("FILE_PATH",FILE_PATH);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return sjkwj;
    }
    public static Map<String ,String > SJB(String sql) {
        Map<String,String> sjb = new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String DS_ID = resultSet.getString("DS_ID");
                String DT_ID = resultSet.getString("DT_ID");
                String DT_NAME = resultSet.getString("DT_NAME");
                String DT_PK = resultSet.getString("DT_PK");
                String DT_ALIAS = resultSet.getString("DT_ALIAS");
                String MODIFY_TIME = resultSet.getString("MODIFY_TIME");
                String REMARKS = resultSet.getString("REMARKS");
                sjb.put("DS_ID",DS_ID);
                sjb.put("DT_ID",DT_ID);
                sjb.put("DT_NAME",DT_NAME);
                sjb.put("DT_PK",DT_PK);
                sjb.put("DT_ALIAS",DT_ALIAS);
                sjb.put("MODIFY_TIME",MODIFY_TIME);
                sjb.put("REMARKS",REMARKS);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return sjb;
    }
    /*
    *@author shixj
    *@Description 字段基本信息
    *@Date 14:06 2018/7/16
    **/

    public static Map<String ,String > ZD(String sql) {
        Map<String,String> zd= new HashMap<String,String>();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String FIELD_ID = resultSet.getString("FIELD_ID");
                String DS_ID = resultSet.getString("DS_ID");
                String DT_ID = resultSet.getString("DT_ID");
                String FIELD_NAME = resultSet.getString("FIELD_NAME");
                String FIELD_ALIAS = resultSet.getString("FIELD_ALIAS");
                String FIELD_TYPE = resultSet.getString("FIELD_TYPE");
                String MODIFY_TIME = resultSet.getString("MODIFY_TIME");
                String REMARKS = resultSet.getString("REMARKS");
                zd.put("FIELD_ID",FIELD_ID);
                zd.put("DS_ID",DS_ID);
                zd.put("DT_ID",DT_ID);
                zd.put("FIELD_NAME",FIELD_NAME);
                zd.put("FIELD_ALIAS",FIELD_ALIAS);
                zd.put("FIELD_TYPE",FIELD_TYPE);
                zd.put("MODIFY_TIME",MODIFY_TIME);
                zd.put("REMARKS",REMARKS);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return zd;
    }
    /*
    *@author shixj
    *@Description 查询表格数目个数
    *@Date 14:06 2018/7/16
    **/
    public static  int querynum(String query){
        int  rowCount = 0;

        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet =statement.executeQuery(query);
            while(resultSet.next()){
                rowCount++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  rowCount;
    }

    /*
    *@author shixj
    *@Description 获取到字段表中的字段id
    *@Date 16:28 2018/7/18
    **/
    public static List<String> fieldID(String query){
        List<String> fieldList = new ArrayList();
        try {
            Class.forName(drivername);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet =statement.executeQuery(query);
            while(resultSet.next()){
               fieldList.add(resultSet.getString("FIELD_ID"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  fieldList;
    }

}
/*    public static String  QueryValue(String sql,String columnName) {
        Connection connection = null;
        Statement statement = null;
        String Result = "";
        try {
            String url = "jdbc:postgresql://192.168.251.78:1328/dsas";
            String user = "beyondb";
            String password = "123456";
            Class.forName("org.postgresql.Driver");  //
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("是否成功连接pg数据库"+connection);
            statement = connection.createStatement();
            //查询的sql语句
          //  String sql = "SELECT * FROM \"public\".\"SKDSJ_SJYSP_T_SJYJBXX\" t where t.\"DS_ID\" = '6dfdabd0219445b2b2ee4edd2c924ee0'";
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println(resultSet);
            while(resultSet.next()){
                //取出列值
              *//*  int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                System.out.println(id+","+name+",")*//*;
          //   String  ss =   resultSet.getString("DS_ID");
                Result =  resultSet.getString(columnName);
             System.out.println(Result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }finally{
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return  Result;
    }
}*/
