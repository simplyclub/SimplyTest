package Tests;

import APIHandling.APIPost;
import BaseClass.BaseJSON;
import BaseClass.WebDriverSetUp;
import FunctionsClass.UpdateJSONFile;
import FunctionsClass.UpdateXMLFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;

import Tests.TestFunctions.TranEndFunctions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.EmailHandling;
import utilities.MainFunction;

import java.util.ArrayList;
import java.util.HashMap;

public  class BasePage {

    //declaration
    BaseJSON baseJSON = new BaseJSON();
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    APIHandling.APIPost APIPost = new APIPost();
    JSONGetData JSONGetData = new JSONGetData();


    //UserHandling userHandling = new UserHandling();
    ResponseHandling responseHandling = new ResponseHandling();
    UpdateXMLFile updateXMLFile = new UpdateXMLFile();
    NodeList nodeList = null;




    // response names

    Response subTotalResponse = null ;
    Response trenEndResponse = null ;
    Response userDataResponse = null;
    Response transactionViewResponse = null;
    Response trenCancelResponse = null ;
    ArrayList<Node> xmlResponseDiscountList = new ArrayList<>();

    // extent report
    public static  ExtentReports exReport = new ExtentReports();
    public static  ExtentSparkReporter spark = new ExtentSparkReporter("index.html");
    public static  ExtentTest ExReApiTestReport ;
    public static  ExtentTest ExReAccumReport ;




    //GLOBALS
    public static final  String JSON_TEST_FILE = "C:\\Users\\User\\IdeaProjects\\SimplyTest\\JSONParametersToSend.json";
    public static String s = null;
    public static double sumVal = 0.0;

    //sum
    public static HashMap<String ,Double> sumDealPoints = new HashMap<>();
    public static HashMap<String ,Double> sumDealPointsWithUse = new HashMap<>();
    public static HashMap<String ,Double> sumDealToUsePoints = new HashMap<>();
    public static HashMap<String ,Double> sumBurnd = new HashMap<>();


    //pre
    public static HashMap<String,Double> preDeal = new HashMap<>();

    //post
    public static HashMap<String,Double> postDeal = new HashMap<>();

    //Globals json objects
    public  Object TestJSONToSend = baseJSON.getObj(JSON_TEST_FILE)  ;


    @BeforeSuite
    public void init (){
        MainFunction.extentReportInit();

        // Extent report tests titles
        ExReApiTestReport = exReport.createTest("Sub Total Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReAccumReport = exReport.createTest("Accumulation Test").assignCategory("Transaction");// give the name of the test title  in the report

    }


    @BeforeTest
    public void beforeTest (){
        WebDriverSetUp.initBrowser();


        if(subTotalResponse != null) {

            subTotalResponse = null ;
        }
        if (trenEndResponse!=null){

            trenEndResponse=null;
        }
        if (userDataResponse!=null){

            userDataResponse=null;
        }
        if (xmlResponseDiscountList!=null){

            xmlResponseDiscountList=null;
        }
        if (trenCancelResponse!=null){

            trenCancelResponse=null;
        }

    }

    @AfterClass
    public  void endTest()
    {


    }
    @AfterSuite
    public  void endSuite(){
        exReport.flush();
        //EmailHandling.sendEnmail();




    }
}
