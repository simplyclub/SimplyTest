package utilities;

import Tests.BasePage;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MainFunction extends BasePage {

    public static void openReport() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
        options.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().fullscreen();
        String dir = System.getProperty("user.dir");
        driver.get("file:///" + dir + "\\nullExtentReportResults.html");
    }

    public static void extentReportInit(){
        BasePage.exReport.attachReporter(spark);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Automation report");
        spark.config().setReportName("Simply Tests");

    }
    //temp


    public static void RestGlobals(){
        preDeal.clear();
        postDeal.clear();
        sumDealPoints.clear();
        sumDealToUsePoints.clear();
        sumBurnd.clear();
        sumVal = 0.0;

    }

    public static void RestTimeGlobals(){
        avgTimeSubTotal.clear();
        avgTimeTrenEnd.clear();
        avgTimeTrenEndOnePhase.clear();

    }

    public static Node[] convertNodeListToNodeArray(NodeList list){
        int length = list.getLength();
        Node[] copy = new Node[length];

        for (int n = 0; n < length; ++n)
            copy[n] = list.item(n);

        return copy;
    }

    public static String converToDoubleAsString(String str ){

        double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(d);
        d = Double.valueOf(dx);

        return Double.toString(d);

    }

    public static String convertOkHttpResponseToString(Response response) throws IOException {
        ResponseBody body = response.peekBody(Long.MAX_VALUE);
        String content = body.string();
        body.close();
        body.string();
        return content;
    }

    /**
     * This function, calculates the average times for any given Time array
     * @param arrayList avg Time Array
     * @return avg
     */
    public static int getAvgTime(ArrayList arrayList){
        int avg=0;


        for (int s=0 ; s < arrayList.size();s++){
            //System.out.println("s "+s);
           avg =  Math.toIntExact((Long) arrayList.get(s))+ avg;

        }

        return avg / arrayList.size();
    }

    public static int SearchWordInString (String WordToSearch , String str) {
            String strOrig = str ;

        int intIndex = strOrig.indexOf(WordToSearch);

            if(intIndex == -1 ) {
                System.out.println(WordToSearch +" not found in string");
                return 0;
            } else {
                System.out.println("Found "+WordToSearch+ " in the String");
                return 1;
            }
    }

    public static String RandomNumber(){
        Random rand = new Random(); //instance of random class
        int upperbound = 9999999;
        //generate random values from 0-9999999
        int int_random = rand.nextInt(upperbound);
        String int_random_string = Integer.toString(int_random);

        return int_random_string;
    }


}//End Class



