import java.io.IOException;

/**
 * Created by 전세호 on 2016-10-06.
 */
public class main {
    public static void main(String[] args) throws IOException {
        Parser parser=new Parser("http://spp.seoul.go.kr/main/news/news_tender.jsp");
        parser.findLocation();
    }
}
