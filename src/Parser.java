import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 전세호 on 2016-10-06.
 */
public class Parser {
    private String URL="";
    private String name="";
    private Document document;
    private static String COMPAREDATA="업무공고번호공고명공고기관계약방법입찰방법입찰서마감일시번호제목내용담당부서등록일조회수구분첨부작성일작성자조회등록일입찰일시추정가격기간";
    public static final int CODE_NOTYET=2, CODE_CANNOT_FIND=1, CODE_SUCCESS=0;
    private static final int TIMEOUT=10000;
    private int tableIndex=-1;
    private int resultCode=CODE_NOTYET;

    public Parser(String str){
        this.URL=str;
    }

    public String getURL(){
        return URL;
    }
    public String getName() { return name; }        //홈페이지 이름

    public List<String> category=new ArrayList();
    public List<List<String>> article=new ArrayList();  //반복 나중에


    private boolean isArticleTable(String msg){
        if(COMPAREDATA.contains(msg)) return true;
        return false;
    }

    public void findLocation() throws IOException {
        this.document=Jsoup.connect(URL)                //connect
                .followRedirects(true)
                .method(Connection.Method.POST)
                .timeout(TIMEOUT)
                .get();
        Elements title = document.select("title");
        this.name=title.text();
        System.out.println("name: "+name);
        Elements tables=document.select("table");
        if(tables !=null) {
            for(Element elem: tables)
            {
                ++tableIndex;
                //Element trTags=elem.select("tr").first();
                for(Element trTags:elem.select("tr")) {
                    if (trTags == null) {                           //when whole line is empty
                        System.out.println("<tr> doesn't exist");
                        resultCode = CODE_CANNOT_FIND;
                        return;
                    }
                    for (Element td : trTags.select("td, th")) {
                        if (isArticleTable(td.text())) {
                            resultCode = CODE_SUCCESS;
                            //ParseAndInsert(resultCode);            //tableIndex?
                            System.out.println("resultCode: " + resultCode);
                            System.out.println("tableIndex: " + tableIndex);
                            if (trTags.text().trim().length() != 0)
                                System.out.println(trTags.text() + " :: " + trTags.select("a").attr("href"));
                            //return;
                        }
                    }
                }
            }
        }
        else {                                          //when table is empty
            System.out.println("No table");
            resultCode=CODE_CANNOT_FIND;
            return;
        }
    }

    private void ParseAndInsert(int ColumnCount){
        setBoardTable();
        setColumnCount(ColumnCount);
    }
    private void setBoardTable(){

    }
    private void setColumnCount(int ColumnCount){

    }
}

