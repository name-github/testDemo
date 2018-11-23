package com.unistrong.GeoTSD.DSAS.utils.ExtentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;


/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public class TestngListener extends TestListenerAdapter {
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestngListener.class);
    protected ExtentReports extent;
    protected ExtentTest test;

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        logger.info("【" + tr.getName() + " Start】");
        //    extent=InitDriverCase.getextent();
        //    test= extent.startTest(tr.getName());
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        logger.info("【" + tr.getName() + " Failure】");
        //    test.log(LogStatus.INFO,"TakesScreenshot ",test.addScreenCapture("../img/"+tr.getName()+".png"));
        //    test.log(LogStatus.FAIL, tr.getThrowable());
        //    extent.endTest(test);

    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        logger.info("【" + tr.getName() + " Skipped】");
        //    test.log(LogStatus.SKIP, "SKIP");
        //    extent.endTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        logger.info("【" + tr.getName() + " Success】");
        //    test.log(LogStatus.PASS, "Pass");
        //    extent.endTest(test);
    }
    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
    }
}
