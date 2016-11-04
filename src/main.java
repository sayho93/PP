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
        Parser parser=new Parser("http://g.hanayo.net/main.htm?gcode=gangjin&btype=&type=rslt?anc_code=4920000&channel=4920000&menu_code=PP001");
        try{
            parser.findLocation();
        }
        catch (SSLHandshakeException e){}
    }
}
