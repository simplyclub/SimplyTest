package Tests.TestFunctions;

import APIHandling.APIPost;
import BaseClass.BaseXML;
import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.BasePage;
import XML.XMLGetData;
import io.restassured.response.Response;
import net.bytebuddy.asm.Advice;
import org.w3c.dom.NodeList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;

public class PointsValidityFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();

    ///////
    String startDate = null;
    String endDate = null;
    String TranDate = null;
    LocalDate dateBefore = null;
    LocalDate dateAfter = null;
    LocalDate dateToday = null;

    int year;
    int month;
    int day;


    long daysToStart;
    long weeksToEnd;
    long daysBetween;


    //"yyyy-MM-dd'T'HH:mm:ss"
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    //////


    public Response makeDealSubTotal(int i) {
        updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i));
        return APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end

    public Response makeDealTrenEnd(int i, Response subTotalResponse) {
        updateJSONFile.upDateTranEndJSON(ResponseHandling.getServiceTranNumber(subTotalResponse), JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }//func end

    public Response getMemberBenefitList() {
        updateXMLFile.updateXMLGetMemberBenefitList(BaseXML.xmlToDocGetMemberBenefitList(), "loginKey", updateXMLFile.getSysLogin());
        updateXMLFile.updateXMLGetMemberBenefitList(BaseXML.xmlToDocGetMemberBenefitList(), "memberId", "1181924");
        updateXMLFile.updateXMLGetMemberBenefitList(BaseXML.xmlToDocGetMemberBenefitList(), "accountId", "287");
        return APIPost.postXMLGetMemberBenefitList(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
    }//func end


    public int calcDaysUntilEnd(NodeList nodeList, int NLIndex, int i) {


        startDate = XMLGetData.getXmlMBLStartDate(nodeList, NLIndex);
        endDate = XMLGetData.getXmlMBLEndDate(nodeList, NLIndex);


        dateBefore = LocalDate.parse(startDate, formatter);
        dateAfter = LocalDate.parse(endDate, formatter);


        daysBetween = DAYS.between(dateBefore, dateAfter);

        if ((int) daysBetween == Integer.parseInt(JSON.JSONGetData.getDaysToEnd(TestJSONToSend, i))) {
            return 1;
        } else {
            System.out.println("daysToEnd: " + daysBetween);
            return 0;
        }


    }

    public int calcDaysUntilStart(NodeList nodeList, int NLIndex, int i) {
        startDate = XMLGetData.getXmlMBLStartDate(nodeList, NLIndex);
        TranDate = XMLGetData.getXmlMBLTranDate(nodeList, NLIndex);


        dateBefore = LocalDate.parse(startDate, formatter);
        dateToday = LocalDate.parse(TranDate, formatter);


        daysToStart = DAYS.between(dateToday, dateBefore);


        //System.out.println("daysToStart: " + daysToStart);
        if ((int) daysToStart == Integer.valueOf(JSONGetData.getDaysToStart(TestJSONToSend, i))) {
            return 1;

        } else {
            return 0;
        }

    }

    public int calcUntilEndOfYear(NodeList nodeList, int NLIndex) {
        endDate = XMLGetData.getXmlMBLEndDate(nodeList, NLIndex);
        dateAfter = LocalDate.parse(endDate, formatter);
        TranDate = XMLGetData.getXmlMBLTranDate(nodeList, NLIndex);


        dateBefore = LocalDate.parse(startDate, formatter);
        dateToday = LocalDate.parse(TranDate, formatter);

        month = dateAfter.getMonthValue();
        day = dateAfter.getDayOfMonth();
        if (month == 12 && day == 31) {
            return 1;

        }

        return 0;
    }

    public int calcWeeksUntilEnd(NodeList nodeList, int NLIndex, int i) {

        startDate = XMLGetData.getXmlMBLStartDate(nodeList, NLIndex);
        endDate = XMLGetData.getXmlMBLEndDate(nodeList, NLIndex);


        dateBefore = LocalDate.parse(startDate, formatter);
        dateAfter = LocalDate.parse(endDate, formatter);


        weeksToEnd = WEEKS.between(dateBefore, dateAfter) + (long) 1;

        if ((int) weeksToEnd == Integer.parseInt(JSON.JSONGetData.getDaysToEnd(TestJSONToSend, i))) {
            return 1;

        } else {

            return 0;
        }


    }

    public int calcUntilEndOfMonth(NodeList nodeList, int NLIndex) {

        startDate = XMLGetData.getXmlMBLStartDate(nodeList, NLIndex);
        dateBefore = LocalDate.parse(startDate, formatter);


        endDate = XMLGetData.getXmlMBLEndDate(nodeList, NLIndex);
        dateAfter = LocalDate.parse(endDate, formatter);

        month = dateAfter.getMonthValue();
        day = dateAfter.getDayOfMonth();
        //System.out.println(month);
        //System.out.println(day);

        if (month == dateBefore.getMonthValue()) {
            //31 days in a month
            if (month == 1 || month == 3 || month == 4 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day == 31) {
                    System.out.println(1);
                    return 1;
                } else {
                    return 0;
                }
            } else {
                //30 days in a month
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    if (day == 30) {
                        System.out.println(2);
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    if (month == 2 && day == 29) {
                        System.out.println(3);
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }

        }
        return 0;
    }

    public int calcntilEndOfWeek (NodeList nodeList, int NLIndex){


        endDate = XMLGetData.getXmlMBLEndDate(nodeList, NLIndex);
        dateAfter = LocalDate.parse(endDate, formatter);



        DayOfWeek day = dateAfter.getDayOfWeek();
        System.out.println(day.getValue());

        if(day.getValue() == 5){
            return 1;
        }else {
            return 0;
        }











    }











}//end class