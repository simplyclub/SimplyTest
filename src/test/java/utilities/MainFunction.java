package utilities;

import Tests.BasePage;
import com.aventstack.extentreports.reporter.configuration.Theme;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.IOException;
import java.text.DecimalFormat;


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
}



