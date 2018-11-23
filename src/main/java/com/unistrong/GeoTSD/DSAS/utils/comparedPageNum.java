package com.unistrong.GeoTSD.DSAS.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;

/**
 * @Author:
 * @Description  对比数据库查询的数据个数是否与获取到的个数一致
 * @Data:$time$ $date$
 */
public class comparedPageNum {
    public static void comPageNum(int pageSize,int  pageNum,String URLResponse,String SQL){
        int totalnum = 0;
        int count = 0;
        JSONObject json = JSONObject.parseObject(URLResponse);
        JSONObject jsonObject = JSONObject.parseObject(json.getString("resultData"));
        int resultcount = Integer.parseInt(jsonObject.getString("count"));
        int resultpagenum = Integer.parseInt(jsonObject.getString("pageNum"));
        int resultpagesize = Integer.parseInt(jsonObject.getString("pageSize"));
        int resulttotal = Integer.parseInt(jsonObject.getString("total"));
        String list = jsonObject.getString("list");
        JSONArray listArray = JSONArray.parseArray(list);
        int tableNum = listArray.size(); //list对象数组中包含的数据个数
        //查询数据表总数目,统计总页数，当前页个数
        totalnum = postgreConn.querynum(SQL);//总记录数
        //总页数
        if(totalnum % pageSize == 0){
            count = totalnum / pageSize;
        }else{
            count = totalnum / pageSize + 1;
        }
        //验证请求页码值中的数据表个数是否显示正确
        if(pageNum == 1){
            if(totalnum < pageSize){
                Assert.assertEquals(tableNum,totalnum);
            }else{
                Assert.assertEquals(tableNum,pageSize);
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
    }
}

