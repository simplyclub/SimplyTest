package Tests;

import APIHandling.APIPost;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import FunctionsClass.UpdateJSONFile;
import FunctionsClass.UpdateXMLFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;

import Utilities.InfrastructureMainFunction;
import Utilities.LogFileHandling;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.MainFunctions;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

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
    public String trenCancelResponse_string = null ;
    public okhttp3.Response trenRefundResponse = null ;
    public String trenRefundResponse_String = null ;
    public okhttp3.Response trenEndOnePhaseResponse = null ;
    public String trenEndOnePhaseResponse_String = null ;
    public okhttp3.Response memberAddResponse = null;
    public String memberAddResponse_String =null;
    public okhttp3.Response memberSearchResponse = null;
    public String memberSearchResponse_String =null;
    public okhttp3.Response memberUpdateResponse = null;
    public String memberUpdateResponse_String =null;
    public okhttp3.Response memberSwitchRecognitionResponse = null;
    public String memberSwitchRecognitionResponse_String =null;
    public okhttp3.Response memberSwitchStatusResponse = null;
    public String memberSwitchStatusResponse_String =null;
    public okhttp3.Response joinSmsResponse = null;
    public String joinSmsResponse_String =null;
    public okhttp3.Response memberGetDetailsAndCodeResponse = null;
    public String memberGetDetailsAndCodeResponse_String =null;

    public okhttp3.Response memberGetDetailsResponse = null;
    public String memberGetDetailsResponse_String =null;
    ArrayList<Node> xmlResponseDiscountList = new ArrayList<>();

    // extent report
    public static  ExtentReports exReport = new ExtentReports();
    public static  ExtentSparkReporter spark = new ExtentSparkReporter("index.html");
    public static  ExtentTest ExReApiTestReport ;
    public static  ExtentTest ExReAccumReport ;
    public static  ExtentTest ExRePointsValiditReport;
    public static  ExtentTest ExReTernRefundReport;
    public static  ExtentTest ExReTrenEndOnePhaseReport;
    public static  ExtentTest ExReNewMemberTestReport;
    public static  ExtentTest ExReJoinSmsReport;
    public static  ExtentTest ExReStage6And7Report;
    public static  ExtentTest ExReGeneralTests;


    //public static final String HOME_DIRECTORY ="C:\\Users\\User\\IdeaProjects\\";
    public static final String HOME_DIRECTORY = InfrastructureMainFunction.GetLocalDir();



    //GLOBALS
    public static final  String JSON_TEST_FILE = HOME_DIRECTORY+"\\JSONParametersToSend.json";
    public static final  String JSON_JOIN_RENEW = HOME_DIRECTORY+"\\JoinRenewClubItemsJson.json";
    public static final  String JSON_STAGE_6_AND_7_DEAL_ITEMS = HOME_DIRECTORY+"\\Stage6And7ParametersToSendJson.json";
    public static String s = null;
    public static double sumVal = 0.0;
    public static final String LOG_FILE_DIRECTORY = HOME_DIRECTORY+"\\LogFile";

    //sum
    public static HashMap<String ,Double> sumDealPoints = new HashMap<>();
    public static HashMap<String ,Double> sumDealToUsePoints = new HashMap<>();
    public static HashMap<String ,Double> sumBurnd = new HashMap<>();


    //avg time globals
    public static ArrayList<Long> avgTimeSubTotal = new ArrayList<Long>();
    public static ArrayList<Long> avgTimeTrenEnd = new ArrayList<Long>();
    public static ArrayList<Long> avgTimeTrenEndOnePhase = new ArrayList<Long>();
    public static ArrayList<Long> avgTimeTranRefund = new ArrayList<Long>();



    //pre
    public static HashMap<String,Double> preDeal = new HashMap<>();
    public static HashMap<String ,Double> preAllAccumsPoints = new HashMap<>();

    //post
    public static HashMap<String,Double> postDeal = new HashMap<>();
    public static HashMap<String ,Double> postAllAccumsPoints = new HashMap<>();

    //Globals json objects
    public  Object TestJSONToSend = baseJSON.getObj(JSON_TEST_FILE)  ;
    public  Object JoinRenewClubJSONToSend = baseJSON.getObj(JSON_JOIN_RENEW)  ;


    //FieldID globals
    public static final String FIRST_NAME = "0";
    public static final String LAST_NAME = "1";
    public static final String LAST_SALE_DATE = "2";
    public static final String BIRTHDAY = "3";
    public static final String WEDDING_DAY = "4";
    public static final String EMAIL = "5";
    public static final String PHONE = "6";
    public static final String CELL_PHONE = "7";
    public static final String TOTAL_SALE = "8";
    public static final String EXPIRATION_DATE = "9";
    public static final String GENDER = "10";
    public static final String CITY = "11";
    public static final String STREET = "12";
    public static final String REFERNCE_FIELD = "13";
    public static final String ACCUM_FIELD = "14";
    public static final String BRANCH_FIELD = "15";
    public static final String GROUP_FIELD = "16";
    public static final String CARD_FIELD = "17";
    public static final String CREATED_DATE = "18";
    public static final String SYS_ID = "19";
    public static final String PERSONAL_ID = "20";
    public static final String TOTAL_ACC = "21";
    public static final String TOTAL_VISITS = "22";
//Stat
    public static final String TODAY = "23";
    public static final String YESTERDAY = "24";
    public static final String HTML_STATISTICS = "25";
    public static final String SMS_STATISTICS = "26";
    public static final String BY_PURCHASE = "27";
    public static final String LAST_WEEK = "28";
    public static final String LAST_MONTH = "29";
    public static final String ACCOUNT_NAME = "30";
    public static final String TOTAL_MEMBERS = "31";
    public static final String REMOVALEMAIL = "32";
    public static final String RREMOVALSMS = "33";
    public static final String MIKUD = "34";
    public static final String HOUSE_NUM = "35";
    public static final String APP_NUM = "36";
    public static final String APPLICATION_DOWNLOAD = "37";
    public static final String UDF = "38";
    public static final String ENCRYPT_MEMBERID = "39";

    //FieldOperatorType

    public static final String Equal = "0";
    public static final String NotEqual = "1";
    public static final String GreatEqual = "2";
    public static final String LessEqual = "3";
    public static final String Greate ="4";
    public static final String Less = "5";
    public static final String Between = "6";
    public static final String NotBetween = "7";
    public static final String Contains = "8";
    public static final String NotContains = "9";
    public static final String StartWith = "10";
    public static final String EndWith = "11";
    public static final String Blank = "12";
    public static final String NotBlank = "13";

    //member status globals

    public static final String MEMBER_STATUS_ACTIVE = "0";
    public static final String MEMBER_STATUS_NOT_ACTIVE = "1";

    @BeforeSuite
    public void init () throws IOException {
        MainFunctions.extentReportInit();


        // Extent report tests titles
        ExReApiTestReport = exReport.createTest("Sub Total Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReAccumReport = exReport.createTest("TrenEnd promos Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExRePointsValiditReport = exReport.createTest("Points Validity Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReTernRefundReport = exReport.createTest("Refund  Test").assignCategory("Refund");// give the name of the test title  in the report
        ExReTrenEndOnePhaseReport = exReport.createTest("Tren End One Phase Test").assignCategory("Transaction");// give the name of the test title  in the report
        ExReNewMemberTestReport = exReport.createTest(" New Member Tests").assignCategory("NewMember");// give the name of the test title  in the report
        ExReJoinSmsReport = exReport.createTest(" Send Join SMS Test").assignCategory("JoinSms");// give the name of the test title  in the report
        ExReStage6And7Report = exReport.createTest("Stage 6 And 7 Test").assignCategory("Stage6And7Test");// give the name of the test title  in the report
        ExReGeneralTests =  exReport.createTest("General Tests").assignCategory("Transaction");// give the name of the test title  in the report

        Path path = Paths.get(HOME_DIRECTORY+"\\LogFile");
        if(!Files.exists(path)){
            File file = new File(HOME_DIRECTORY+"\\LogFile");

            // true if the directory was created, false otherwise
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }

        }

        LogFileHandling.DeleteFile("EmailLog",HOME_DIRECTORY);
        LogFileHandling.CreateFile("EmailLog",HOME_DIRECTORY);






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
        if (trenEndOnePhaseResponse !=null){

            trenEndOnePhaseResponse = null;
        }
        if (memberAddResponse !=null){

            memberAddResponse = null;
        }
        if (memberSearchResponse !=null){

            memberSearchResponse = null;
        }
        if (memberUpdateResponse !=null){

            memberUpdateResponse = null;
        }
        if (memberSwitchRecognitionResponse !=null){

            memberSwitchRecognitionResponse = null;
        }
        if (memberSwitchStatusResponse !=null){

            memberSwitchStatusResponse = null;
        }if (joinSmsResponse !=null){

            joinSmsResponse = null;
        }

    }

    @AfterClass
    public  void endTest() {



    }

    @AfterSuite
    public  void endSuite() throws IOException, InterruptedException {
        exReport.flush();
//        MainFunction.convertFileEncoding(
//                "C:\\Users\\User\\IdeaProjects\\SimplyTest\\index.html","C:\\Users\\User\\IdeaProjects\\SimplyTest","UTF-8");
  //      EmailHandling.sendEnmail();

    }

}
