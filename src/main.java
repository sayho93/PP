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
        Parser parser=new Parser("http://www.hanayo.net/jds/wbid/wbid.php3?gov=%BC%F8%C3%B5%BD%C3%C3%BB");
        try{
            parser.findLocation();
        }
        catch (SSLHandshakeException e){}
    }
}
