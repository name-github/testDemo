import com.unistrong.GeoTSD.DSAS.utils.ExcelData;
import com.vimalselvam.testng.SystemInfo;
import org.testng.annotations.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class testDemo {
    int fag = 0;
    @BeforeTest
    public  void beforeTest(){
      System.out.println("testDemo类开始执行！");
    }
    @BeforeMethod
    public void beforeMethod(){
        fag++;
        System.out.println("开始执行第"+fag+"个用例");
    }

    @DataProvider(name = "db1")
    public Iterator<Object[]> data() {
        return new ExcelData("interfaceDemocase","test");
    }

    @Test(dataProvider = "db1")
    public  void prmap(Map<String,String> arr) {
        String key = null;
        String aa = null;
        String bb = null;
        String cc = null;
        Set<String> set=arr.keySet();
        Iterator<String> it=set.iterator();

        while(it.hasNext()){
            key= it.next();
            String value = arr.get(key);
            System.out.println("key值："+key+",他对应的value值："+value);

          /*  switch(parameter){
                case "interface_address":
                    aa = arr.get(parameter);
                    System.out.println("参数为aa的值为："+aa);
                    break;
                case "Request":
                    bb = arr.get(parameter);
                    System.out.println("参数为bb的值为："+bb);
                    break;
                case "Response":
                    cc = arr.get(parameter);
                    System.out.println("参数为cc的值为："+cc);
                    break;
                default: break;
            }*/
        }
    }
    @Test
    public void testcase2(){
        System.out.println("当前类中的第二个方法待定");
    }
    @AfterMethod
    public void afterMethod(){
        System.out.println("第"+fag+"个用例执行完毕");
        System.out.println("**********");
    }
    @AfterTest
    public  void afterTest(){
        System.out.println("testDemo类执行完毕！");
    }

}
