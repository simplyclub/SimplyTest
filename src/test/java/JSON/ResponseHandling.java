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
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

// TODO :
//  1)  change all the get function not to be only on "cashBackDiscounts"
//      i need to make them more General
//  2) make all the function get an index for the array
//  3) make all the function return the correct value

public class ResponseHandling  {
    BaseJSON baseJSON = new BaseJSON();
    BaseXML baseXML = new BaseXML();

    // TODO : make this function bring all the data in CashBackDiscounts
    //       from the response
    public void getCashBackDiscounts(Response response, int index){




    }
    // TODO : make this function bring all the data in TotalDiscounts
    //       from the response
    public void getTotalDiscounts(Response response){

    }


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
    public String getServiceTranNumber(Response response,int index){
        String s = response.getBody().asString();
        JSONObject responseObj = (JSONObject) baseJSON.convertStringToJSONObj(s);
        return responseObj.get("ServiceTranNumber").toString();

    }

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




    public String getXmlResponseDescription(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("Description").item(0).getTextContent();
        }
        return null;

    }
    public String getXmlResponseAmount(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("Amount").item(0).getTextContent();
        }
        return null;

    }
    public String getXmlResponsePromoID(NodeList nodeList, int i){
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("PromoID").item(0).getTextContent();
        }
        return null;

    }



//    public void getTranViewDiscountData(Response response , int i) throws ParserConfigurationException, IOException, SAXException {
//        Document doc = baseXML.convertStringToXMLDocument(response);
//        NodeList list =doc.getElementsByTagName("PromoID");
//        Node q = list.item(0);
//        System.out.println(q.getNodeValue());
//        System.out.println(q.getFirstChild().getNodeValue());
//        System.out.println(q.getUserData("TranViewDiscountData"));






//        NodeList x = doc.getElementsByTagName("Description");
//        System.out.println(x.item(0).getNodeValue());
//
//        Node discounts = doc.getElementsByTagName("Discounts").item(i);
        //System.out.println(discounts.getFirstChild().getNodeValue());



        //Node x = NL.item(i);
       // System.out.println(x.getNodeValue());
       // getDiscountPromoID(x);





//}

    public void getDiscountPromoID(Node NPromoID){



    }

    private NodeList getDiscountsTransactionView(Document doc) throws ParserConfigurationException, IOException, SAXException {
        NodeList x = doc.getElementsByTagName("Discounts");
       //System.out.println(x.item(0).getNodeValue());

        return x;





    }














}
