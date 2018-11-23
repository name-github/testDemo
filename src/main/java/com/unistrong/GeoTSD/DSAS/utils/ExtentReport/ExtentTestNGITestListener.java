package com.unistrong.GeoTSD.DSAS.utils.ExtentReport;

import com.aventstack.extentreports.*;


import org.testng.*;

import java.io.File;


/**
 * @Author:
 * @Description
 * @Data:$time$ $date$
 */
public  class ExtentTestNGITestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance("D:\\Geotsd\\DSASInterfacePro\\test-output\\ExtentReporListener.html");
    private static ThreadLocal test = new ThreadLocal();
    @Override
    public synchronized void onStart(ITestContext context){

    }
    @Override
    public synchronized void onFinish(ITestContext context){

    }
    @Override
    public synchronized void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getMethod().getMethodName()));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        ((ExtentTest)test.get()).pass("Test passed");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        ((ExtentTest)test.get()).fail(result.getThrowable());
        File directory = new File("test-output");
        try {
            String screenPath = directory.getCanonicalPath() + "/";
            File file = new File(screenPath);
            if (!file.exists()){
                file.mkdirs();
            }
            String fileName = result.getMethod().getMethodName() + ".png";
          //  driver.saveScreenshot(screenPath + fileName);
            ((ExtentTest)test.get()).addScreenCaptureFromPath(screenPath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        ((ExtentTest)test.get()).skip(result.getThrowable());
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

}

