
import com.aventstack.extentreports.ExtentReports;
import com.unistrong.GeoTSD.DSAS.utils.ExcelData;
import com.unistrong.GeoTSD.DSAS.utils.ExtentReport.ExtentManager;
import com.unistrong.GeoTSD.DSAS.utils.ExtentReport.ExtentTestNGITestListener;
import com.unistrong.GeoTSD.DSAS.utils.ExtentReport.ReportUtil;
import com.unistrong.GeoTSD.DSAS.utils.HttpRequest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;


import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;


public class DemoControllerTest {
    int fag = 0;    //测试用例编号
    String  url = "http://localhost:9090/dsas/demo/test";
    String  requestParameter = "";
    public  static  ExtentReports extent;
    String filepath = "D:\\Geotsd\\DSASInterfacePro\\test-output\\ExtentReporListener.html";


    @BeforeClass
    public  void beforeClass(){
        System.out.println("DemoControllerTest类开始：");

    }
    @AfterClass
    public  void afterClass(){
            System.out.println("DemoControllerTest类执行结束！\n ***************");
    }


    @BeforeMethod
    public void beforeMethod(Method method) {
        fag++;
        System.out.println("开始执行第" + fag + "个用例：");
        Reporter.log("测试方法："+ method );
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("第" + fag + "个用例执行结束！");
    }

    @DataProvider(name = "indexpost")
    public Iterator<Object[]> indexpost() {
        return new ExcelData("DemoController", "indexpost");
    }
    @Test(dataProvider = "indexpost",timeOut = 3000)
    public void testIndexpost(Map<String, String> params) {
         //通过map.keySet()方法，获取key值,在获取到value值
            //  String param = "dataSourceName=ceshirenyuan1&dataSourceType=1";
        requestParameter = "";
        for(String param:params.keySet()){
           if(params.get(param)!= null && params.get(param).length() > 0){
              requestParameter = requestParameter + param + "=" + params.get(param) + "&";
           }
        }
/*        //该方法获取的map值与excel存储的值方式不一致
        Set<String> set=params.keySet();
        for(Iterator<String> param=set.iterator();param.hasNext();){
            if(params.get(param)!= null && params.get(param).length() > 0){
                requestParameter = requestParameter + param + "=" + params.get(param) + "&";
            }
        }*/
        requestParameter = requestParameter.substring(0,requestParameter.length()-1);
        System.out.println(requestParameter);
        //调用接口进行测试
       String URL = HttpRequest.httpPost(url, requestParameter);
       Reporter.log("类型："+ params.get("testType"));
        ExtentManager.createHtmlReporter(filepath);
    }
    @DataProvider(name = "indexget")
    public Object[][] indexget(){
        return new Object[][]{
                {"test"},{"jianguo"},{"张力"},
        };
    }
    @Test(dataProvider = "indexget",timeOut = 3000)
    public void testIndexget(String name) {
        String param = "name=" + name;
        System.out.println(param);
        String URL = HttpRequest.httpGet(url,param);
        System.out.println(URL);
    }
}