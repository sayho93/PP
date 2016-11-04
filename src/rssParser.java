import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 전세호 on 2016-11-04.
 */
public class rssParser {
    private static String URL;
    public List<String> category=new ArrayList();
    public List<List<String>> article=new ArrayList();

    public rssParser(String str){
        this.URL=str;
    }

    public String getURL(){return URL;}

    public void parseRss(){
        System.out.println("rss parser running...");
        Entity list=null;
        int len=20;

        try{
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document document=builder.parse("http://openapi.naver.com/search?key=lelab&query="+"대전시교육청홈페이지"+"&target=local&start=1&display=3");
            NodeList rss=document.getElementsByTagName("rss");
            NodeList channel=rss.item(0).getChildNodes();
            NodeList _n=channel.item(0).getChildNodes();
            System.out.println(" [ "+rss.item(0).getNodeValue()+ " ] ");
        }
        catch(Exception e) {
            System.out.println("exception occured in rssParser");
            e.printStackTrace();
        }
    }
}
