package XML;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLGetData {

    public static String getXmlMBLPromoId(NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("PromoId").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLIsHasExpDate(NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("PromoId").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLMemberSysId (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("MemberSysId").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLSysTrxNumber (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("SysTrxNumber").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLStartDate (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("StartDate").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLEndDate (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("EndDate").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLTranDate (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("TranDate").item(0).getTextContent();
        }
        return null;

    }
    public static String getXmlMBLViewAmount (NodeList nodeList, int NLindexl){
        Node node = nodeList.item(NLindexl);
        if (node.getNodeType() == Node.ELEMENT_NODE){
            Element eElement = (Element) node;
            return eElement.getElementsByTagName("ViewAmount").item(0).getTextContent();
        }
        return null;

    }


}
