import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;

/**
 * Created by 전세호 on 2016-10-06.
 */
public class main {
    public static void main(String[] args) throws IOException {
        //Parser parser=new Parser("http://www.dje.go.kr/dje/2/board.do?boardID=451&menuID=030102");
        String url="http://www.dje.go.kr/boardCnts/rssNotice.do?rss=2d84b2374966469fe7729309a5b46e30";
        try{
            if(url.contains("rss")){
                //parser.finderForEminwon();
                rssParser rssparser=new rssParser(url);
                rssparser.parseRss();
            }
            else{
                Parser parser=new Parser(url);
                parser.findLocation();
            }
        }
        catch (SSLHandshakeException e){
            System.out.println("security exception occured");

        }
    }
}
