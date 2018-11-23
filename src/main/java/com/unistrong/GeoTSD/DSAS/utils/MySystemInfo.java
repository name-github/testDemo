package com.unistrong.GeoTSD.DSAS.utils;

import com.google.common.collect.Maps;
import com.vimalselvam.testng.SystemInfo;

import java.util.Map;

public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String ,String > getSystemInfo(){
        Map<String,String> systemInfo = Maps.newHashMap();
        systemInfo.put("Creator","");
        systemInfo.put("Browser","Chown");
        systemInfo.put("测试执行人","****");
        return systemInfo;
    }
}
