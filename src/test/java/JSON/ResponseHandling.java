package JSON;

import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;



public class ResponseHandling  {
    BaseJSON baseJSON = new BaseJSON();
    BaseXML baseXML = new BaseXML();



    public  String getAmount(Response response , String discountsType, int index){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject amount = (JSONObject) discounts.get(index);

        return amount.get("Amount").toString();
    }

    public String getIsAuto (Response response, String discountsType, int index){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject isAuto = (JSONObject) discounts.get(index);
        //System.out.println(isAuto.get("IsAuto").toString());
        return isAuto.get("IsAuto").toString();

    }
    public String getPromoId (Response response, String discountsType, int index){
        String s = response.getBody().asString();
        //System.out.println(s);
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);

        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject promoId = (JSONObject) discounts.get(index);
        //System.out.println(promoId.get("PromoId").toString());
        return promoId.get("PromoId").toString();

    }

    public String getDescription (Response response, String discountsType, int index){
        //System.out.println("ResponseHandling "+index);
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray discounts = (JSONArray) responseObj.get(discountsType);

        JSONObject description = (JSONObject) discounts.get(index);
        //System.out.println(description.get("Description").toString());
        return  description.get("Description").toString();

    }
    public String getAllItemsDiscountPercent (Response response,int index){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray totalDiscounts = (JSONArray) responseObj.get("TotalDiscounts");

        JSONObject allItemsDiscountPercent = (JSONObject) totalDiscounts.get(index);


        return allItemsDiscountPercent.get("AllItemsDiscountPercent").toString();

    }
    public String getServiceTranNumber(Response response){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        return responseObj.get("ServiceTranNumber").toString();

    }

    /**
     * get the array of the "all accums" from the user data response
     * @param response - user "get data" response
     * @return JSONArray with  "AllAccums" array
     */
    public JSONArray getAllAccums (Response response){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONObject member = (JSONObject) responseObj.get("Member");
        JSONArray AllAccums = (JSONArray) member.get("AllAccums");


        return  AllAccums;


    }


    public int getCaseBackDiscountsArrSize (Response response) {
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray caseBack = (JSONArray) responseObj.get("CashBackDiscounts");
        return caseBack.size();
    }

    public int getTotalDiscountsArrSize (Response response){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        JSONArray totalDiscounts = (JSONArray) responseObj.get("TotalDiscounts");
        return totalDiscounts.size();




    }
    public String getErrorCodeStatusJson(Response response){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        //System.out.println(responseObj.get("ErrorCode"));
      return responseObj.get("ErrorCode").toString();



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
    public String getXMLResponseAmount(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("Amount").item(0).getTextContent();
        }
        return null;

    }


    public String getXMLResponsePromoID(NodeList nodeList, int NLindexl){
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
