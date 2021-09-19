package Tests;

import APIHandling.APIPost;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import BaseClass.WebDriverSetUp;
import FunctionsClass.UpdateJSONFile;
import FunctionsClass.UpdateXMLFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.response.Response;
import okhttp3.OkHttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.MainFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public  class BasePage {

    //declaration
    public BaseJSON baseJSON = new BaseJSON();
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    public APIPost APIPost = new APIPost();
    JSONGetData JSONGetData = new JSONGetData();
    BaseXML baseXML = new BaseXML();



    //UserHandling userHandling = new UserHandling();
    ResponseHandling responseHandling = new ResponseHandling();
    public UpdateXMLFile updateXMLFile = new UpdateXMLFile();
    public NodeList nodeList = null;




    // response names

    public okhttp3.Response subTotalResponse = null ;
    public String subTotalResponse_String = null ;
    public okhttp3.Response trenEndResponse = null ;
    public String trenEndResponse_String = null;
    public okhttp3.Response userDataResponse = null;
    public String userDataResponse_String = null ;
    public Response transactionViewResponse = null;
    public static Response getMemberBenefitListResponse = null;
    public okhttp3.Response trenCancelResponse = null ;
    public okhttp3.Response trenRefundResponse = null ;
    public String trenRefundResponse_String = null ;
    ArrayList<Node> xmlResponseDiscountList = new ArrayList<>();

    // extent report
    public static  ExtentReports exReport = new ExtentReports();
    public static  ExtentSparkReporter spark = new ExtentSparkReporter("index.html");
    public static  ExtentTest ExReApiTestReport ;
    public static  ExtentTest ExReAccumReport ;
    public static  ExtentTest ExRePointsValiditReport;
    public static  ExtentTest ExReTernRefundReport;




    //GLOBALS
    public static final  String JSON_TEST_FILE = "C:\\Users\\User\\IdeaProjects\\SimplyTest\\JSONParametersToSend.json";
    public static String s = null;
    public static double sumVal = 0.0;
    public static final String LOG_FILE_DIRECTORY = "C:\\Users\\User\\IdeaProjects\\SimplyTest\\LogFile";

    //sum
    public static HashMap<String ,Double> sumDealPoints = new HashMap<>();
    public static HashMap<String ,Double> sumDealToUsePoints = new HashMap<>();
    public static HashMap<String ,Double> sumBurnd = new HashMap<>();



    //pre
    public static HashMap<String,Double> preDeal = new HashMap<>();
    public static HashMap<String ,Double> preAllAccumsPoints = new HashMap<>();

    //post
    public static HashMap<String,Double> postDeal = new HashMap<>();
    public static HashMap<String ,Double> postAllAccumsPoints = new HashMap<>();

    //Globals json objects
    public  Object TestJSONToSend = baseJSON.getObj(JSON_TEST_FILE)  ;


    @BeforeSuite
    public void init (){
        MainFunction.extentReportInit();

        // Extent report tests titles
        ExReApiTestReport = exReport.createTest("Sub Total Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReAccumReport = exReport.createTest("Accumulation Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExRePointsValiditReport = exReport.createTest("Points Validit Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReTernRefundReport = exReport.createTest("Refund  Test").assignCategory("Refund");// give the name of the test title  in the report


    }


    @BeforeTest
    public void beforeTest (){
        //WebDriverSetUp.initBrowser();


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
        if (getMemberBenefitListResponse !=null){

            getMemberBenefitListResponse = null;

        }
        if (trenRefundResponse !=null){

            trenRefundResponse = null;
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
