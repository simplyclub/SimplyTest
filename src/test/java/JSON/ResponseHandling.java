package JSON;

import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import utilities.MainFunctions;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;



public class ResponseHandling  {
    BaseJSON baseJSON = new BaseJSON();
    BaseXML baseXML = new BaseXML();



    public  String getAmount( String s, String discountsType, int index) throws IOException {
        //String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject amount = (JSONObject) discounts.get(index);

        return amount.get("Amount").toString();
    }

    public String getIsAuto (String s, String discountsType, int index) throws IOException {
        //String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject isAuto = (JSONObject) discounts.get(index);
        //System.out.println(isAuto.get("IsAuto").toString());
        return isAuto.get("IsAuto").toString();

    }
    public String getPromoId (String s, String discountsType, int index) throws IOException {
        //String s = response.body().string();
        //System.out.println(s);
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        //System.out.println(s);

        JSONArray discounts = (JSONArray) responseObj.get(discountsType);
       // System.out.println(discounts);

        JSONObject promoId = (JSONObject) discounts.get(index);
        //System.out.println(promoId.get("PromoId").toString());
        return promoId.get("PromoId").toString();

    }

    public String getDescription (String s, String discountsType, int index) throws IOException {
        //System.out.println("ResponseHandling "+index);
        //String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject description = (JSONObject) discounts.get(index);
        //System.out.println(description.get("Description").toString());
        return  description.get("Description").toString();

    }
    public String getAllItemsDiscountPercent (String s ,int index) throws IOException {
        //String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray totalDiscounts = (JSONArray) responseObj.get("TotalDiscounts");

        JSONObject allItemsDiscountPercent = (JSONObject) totalDiscounts.get(index);


        return allItemsDiscountPercent.get("AllItemsDiscountPercent").toString();

    }
    public static String getServiceTranNumber(String s) throws IOException {
       // String s = response.body().string();
        JSONObject responseObj = (JSONObject) BaseJSON.convertStringToJSONObj(s);
        return responseObj.get("ServiceTranNumber").toString();

    }

    public String getTrenEndTranReferenceNumber(String s ) throws IOException {
       // String s = response.body().string();
        JSONObject responseObj = (JSONObject) BaseJSON.convertStringToJSONObj(s);
        System.out.println(responseObj.get("TranReferenceNumber").toString());
        return responseObj.get("TranReferenceNumber").toString();

    }

    /**
     * Get the array of the "all accums" from the user data response
     * @param s - user "get data" response
     * @return JSONArray with  "AllAccums" array
     */
    public static JSONArray getAllAccums(String s) throws IOException {
       // String s = response.body().string();
        JSONObject responseObj = (JSONObject) BaseJSON.convertStringToJSONObj(s);
        JSONObject member = (JSONObject) responseObj.get("Member");
        JSONArray AllAccums = (JSONArray) member.get("AllAccums");


        return  AllAccums;


    }


    public int getCaseBackDiscountsArrSize (String s) throws IOException {
        //String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray caseBack = (JSONArray) responseObj.get("CashBackDiscounts");
        return caseBack.size();
    }

    public int getTotalDiscountsArrSize (String s) throws IOException {
       // String s = response.body().string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray totalDiscounts = (JSONArray) responseObj.get("TotalDiscounts");
        return totalDiscounts.size();




    }
    public String getErrorCodeStatusJson(String s) throws IOException {
        //String s = response.peekBody(2042).string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        //System.out.println(responseObj.get("ErrorCode"));
      return responseObj.get("ErrorCode").toString();



    }public int getMembersArraySize(String s) throws IOException {
        //String s = response.peekBody(2042).string();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        //System.out.println(responseObj.get("ErrorCode"));
        JSONArray x =(JSONArray) responseObj.get("Members");
      return x.size();



    }


    /**
     * this function will return the card number from the response of "Member Add "
     * @param s response as a String
     * @param ApiCAllType what type of response  memberSearchResponse / memberAddResponse
     * @return CardNumber
     */
    public String getCardNumber (String s,String ApiCAllType){
        System.out.println(MainFunctions.BaseLogStringFunc() +ApiCAllType+": "+s);
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);

        if(ApiCAllType.equals("memberAddResponse")){
            JSONObject member = (JSONObject) responseObj.get("Member");
            //System.out.println(member);
            return member.get("CardNumber").toString();
        }
        if(ApiCAllType.equals("memberSearchResponse")){
            JSONArray member = (JSONArray) responseObj.get("Members");
            JSONObject x = (JSONObject) member.get(0);
            //System.out.println(member);
            return x.get("CardNumber").toString();

        }
        return null ;
    }


    public String getMemberStatus(String s){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONObject member = (JSONObject) responseObj.get("Member");

        return member.get("Status").toString();




    }



    public  String getUserDetailsExpDate (String s){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONObject member = (JSONObject) responseObj.get("Member");
        //System.out.println(member);
        String ExpDate = member.get("ExpDate").toString();

        return ExpDate;



    }

    public String getSysId(String s){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONObject member = (JSONObject) responseObj.get("Member");

        String SysId = member.get("SysId").toString();

        return SysId;
    }

    public String getMemberField(String response ,String field,int memberIndex){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(response);
        JSONArray members = (JSONArray) responseObj.get("Members");
        JSONObject memberFields = (JSONObject) members.get(memberIndex);
        JSONArray fields = (JSONArray) memberFields.get("MemberFields") ;

        //System.out.println("memberFields: "+memberFields);
        for(int s = 0 ; s<fields.size();s++){
            JSONObject fildeId = (JSONObject)  fields.get(s);


            if (field.equals(fildeId.get("FieldId"))){
                System.out.println(MainFunctions.BaseLogStringFunc()+"FieldId: "+fildeId.get("FieldId"));
                System.out.println(MainFunctions.BaseLogStringFunc()+"FieldValue: "+fildeId.get("FieldValue"));
                return fildeId.get("FieldValue").toString();
            }

        }
        return null ;



    }
    public String getResultCode(String response){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(response);

        return responseObj.get("ResultCode").toString();

    }

    /**
     *
     * @param response memberGetDetailsAndCodeResponse
     * @return
     */
    public String getCode(String response){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(response);
        System.out.println(MainFunctions.BaseLogStringFunc()+"responseObj: " + responseObj);

        return responseObj.get("Code").toString();

    }


    public String getAdditionalInfo(String response , String Field_Id_Or_Value ){
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(response);
        JSONArray additionalInfo = (JSONArray) responseObj.get("AdditionalInfo");

        if(Field_Id_Or_Value.equals("FieldId")){
            JSONObject x = (JSONObject) additionalInfo.get(0);
            return x.get("FieldId").toString();

        }
        if(Field_Id_Or_Value.equals("FieldValue")){
            JSONObject x = (JSONObject) additionalInfo.get(0);
            return x.get("FieldValue").toString();

        }

        return null;
    }




//********************************XML******************************
    public String getXmlResponseDescription(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("Description").item(0).getTextContent();
        }
        return null;

    }
    public static String getXMLResponseAmount(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("Amount").item(0).getTextContent();
        }
        return null;

    }


    public static String getXMLResponsePromoID(NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("PromoID").item(0).getTextContent();
        }
        return null;

    }
    public static NodeList getXMLFileTranViewDiscountData(String xml){
        try
        {
            //creating a constructor of file class and parsing an XML file
            // File file = new File("C:\\Users\\User\\IdeaProjects\\SimplyTest\\response.xml");

            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("TranViewDiscountData");
            return nodeList;


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * this function get the TranViewMemberBenefitData list from GetMemberBenefitList
     * @param xml response body as a string
     * @return
     */
    public  NodeList getXMLFileTranViewMemberBenefitData(String xml){
        try
        {
            //creating a constructor of file class and parsing an XML file
            // File file = new File("C:\\Users\\User\\IdeaProjects\\SimplyTest\\response.xml");

            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("TranViewMemberBenefitData");
            return nodeList;


        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }


    public static String getXMLFileNegativeCashBack(String xml){
        try
        {
            //creating a constructor of file class and parsing an XML file
            // File file = new File("C:\\Users\\User\\IdeaProjects\\SimplyTest\\response.xml");

            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            Node node = doc.getElementsByTagName("NegativeCashBack").item(0);
            return node.getTextContent();

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }
    public static String getXMLFilePaidTotal(String xml){
        try
        {
            //creating a constructor of file class and parsing an XML file
            // File file = new File("C:\\Users\\User\\IdeaProjects\\SimplyTest\\response.xml");

            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            Node node = doc.getElementsByTagName("PaidTotal").item(0);
            return node.getTextContent();

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }






















}
