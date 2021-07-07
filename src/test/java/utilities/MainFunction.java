package utilities;

import Tests.BasePage;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

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
        sumVal = 0.0;


    }
    public static Node[] convertNodeListToNodeArray(NodeList list){
        int length = list.getLength();
        Node[] copy = new Node[length];

        for (int n = 0; n < length; ++n)
            copy[n] = list.item(n);

        return copy;

    }
}



