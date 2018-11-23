package com.unistrong.GeoTSD.DSAS.utils.ExtentReport;

import org.testng.Reporter;
import java.util.Calendar;

/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class ReportUtil {
    private static String reportName = "数据源适配服务接口：自动化测试报告";

    private static String splitTimeAndMsg = "===";
    public static void log(String msg) {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        Reporter.log(timeMillis + splitTimeAndMsg + msg, true);
    }

    public static String getReportName() {
        return reportName;
    }

    public static String getSpiltTimeAndMsg() {
        return splitTimeAndMsg;
    }

    public static void setReportName(String reportName) {
        if(StringUtil.isNotEmpty(reportName)){
            ReportUtil.reportName = reportName;
        }
    }
}
