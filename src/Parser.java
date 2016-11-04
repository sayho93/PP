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
    private int articleCount=0;
    private int colspan=0;
    private int removeIndex;
    private static String COMPAREDATA=
            "업무공고\n" + "번호\n" + "공고명\n" + "공고기관\n" + "계약방법\n" + "입찰방법\n" +
                    "입찰서\n" + "마감일시\n" + "번호\n" + "제목\n" + "내용\n" + "담당부서\n" +
                    "등록일\n" + "조회수\n" + "구분\n" + "첨부\n" + "작성일\n" + "작성자\n" +
                    "조회\n" + "등록일\n" + "추정가격\n" + "기간\n" + "입찰일시\n" + "입찰기관\n" +
                    "입찰건명\n" + "링크\n" + "입찰일\n" + "첨부파일\n" + "No.\n" + "업무\n" +
                    "공고번호-차수\n" + "공고기관\n" + "계약방법\n" + "입찰서\n마감일시\n" +
                    "입찰방법\n" + "발주처명\n" + "현장설명\n" + "등록기한\n" + "입찰일자\n" +
                    "결과\n"+ "게시일자\n" + "등록일자\n" + "NO.\n" + "입찰건명   입찰일   \n" +
                    "분류\n" + "이름\n" + "날짜\n"+ "분야\n" + "입찰명\n" + "계약부서\n" +
                    "사업부서\n"+ "전체\n"+ "게시일자\n"+ "입찰공고번호\n"+ "개찰일자\n"+
                    "발주기관\n"+ "조달방식\n"+ "유형\n"+ "공사명\n"+ "게시일시\n"+ "발주시기\n"+
                    "등록자\n"+ "고시공고번호\n" + "관서명\n"+ "계약명\n"+ "계약금액\n"+
                    "계약일\n"+ "계약대상자\n"+ "글번호\n"+ "입찰번호\n"+ "입찰정보\n"+
                    "사업명\n"+ "부서명\n" + "계약\n"+ "입찰서마감일시\n" + "기초금액\n";
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
    public List<List<String>> article=new ArrayList();


    private boolean isArticleTable(String msg){
        if(COMPAREDATA.replaceAll(" ", "").contains(msg.replaceAll(" ", "")+"\n")) return true;
        return false;
    }

    private boolean isPlace(String msg){
        //if(msg.replaceAll(" ", "").contains("경상북도")) return true;
        return false;
    }

    private boolean isSearchTable(String msg){
        if(msg.replaceAll(" ", "").contains("검색")) return true;
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
                    ArrayList<String> temp = new ArrayList<>();
                    article.add(temp);
                    if (trTags == null) {                           //when whole line is empty
                        System.out.println("<tr> doesn't exist");
                        resultCode = CODE_CANNOT_FIND;
                        return;
                    }
                    for (Element th : trTags.select("th")) {
                        if (isArticleTable(th.text()) && !isPlace(th.text())) {
                            resultCode = CODE_SUCCESS;
                            //ParseAndInsert(resultCode);            //tableIndex?
                            //System.out.println("resultCode: " + resultCode);
                            //System.out.println("tableIndex: " + tableIndex);
                            if (trTags.text().trim().length() != 0) {
                                //System.out.println(trTags.text() + " :: " + trTags.select("a").attr("href"));
                                if(th.text().contains("첨부파일") || th.text().contains("파일") || th.text().contains("첨부") || th.text().contains("링크") || th.text().contains("결과")) continue;

                                if(!th.select("img").isEmpty()) continue;

                                category.add(th.text());
                                if(!th.attr("colspan").isEmpty()) {
                                    colspan=Integer.parseInt(th.attr("colspan"));
                                    removeIndex=category.indexOf(th.text());
                                    System.out.println(colspan + "    " + category.indexOf(th.text()) + " " + th.text());
                                    category.remove(th.text());
                                }
                            }
                            //return;
                        }
                    }

                    int i=0;
                    for(Element td : trTags.select("td, th")){
                        if(!isArticleTable(td.text()) && !isPlace(td.text())){
                            //temp.add(td.text());
                            String tdTemp = td.text();
                            String aTemp = td.select("a").text();

                            if ((tdTemp.contains(aTemp) || aTemp.contains(tdTemp)) && aTemp.length()!=0) {
                                if(tdTemp=="") continue;
                                temp.add(aTemp);
                                System.out.println("1111111111");
                            } else {
                                if(tdTemp.length() == 0) temp.add(aTemp);
                                if(aTemp.length() == 0) temp.add(tdTemp);
                                //temp.add(tdTemp);
                            }

                            System.out.println("tdTemp:" + tdTemp +"length" +tdTemp.length());
                            System.out.println("aTemp:" + aTemp + "length"+aTemp.length());
                            //System.out.println(temp.get(i++));
                            tdTemp="";
                            aTemp="";
                            for(int j=0;j<i;j++){
                                System.out.println(temp.get(j));
                            }
                        }
                        if(temp.size()>articleCount && temp.size()<=category.size()) articleCount=temp.size();

                    }
                    String raw = this.URL.substring(0, this.URL.indexOf("/", 8)) + trTags.select("a").attr("href");
                    if(raw.indexOf("javascript") == -1) temp.add(raw);
                    else temp.add(this.URL.substring(0, this.URL.indexOf("/", 8)));
                }

            }
            articleCount++;
            category.add("Link");
        }
        else {                                          //when table is empty
            System.out.println("No table");
            resultCode=CODE_CANNOT_FIND;
            return;
        }
        System.out.println("category size: "+category.size());
        System.out.println("articleCount: "+articleCount);
        /*
        for(String cat:category){
            if(cat.contains("첨부파일")) category.remove(cat);
        }
        */
        if(category.size()>articleCount && articleCount!=1){
            System.out.println("쓰레기값 테스트");
            int removeSize=category.size()-articleCount;
            for(int k=0;k<removeSize;k++){
                category.remove(0);
            }
        }




        if(colspan != 0){
            for(int i=1;i<article.size()-5;i++){
                for(int j=0;j<colspan;j++) {
                    System.out.println(i + " " + removeIndex);
                    if(article.get(i).size() >= category.size())article.get(i).remove(removeIndex);

                }
            }
        }

        ArrayList tmpArr = new ArrayList();
        for(int i=0;i<article.size();i++){
            if(article.get(i).size() != articleCount) tmpArr.add(i);
            // System.out.println(article.get(i).get(article.get(i).size()));
        }



        for(int i=0;i<tmpArr.size();i++){
            System.out.println(tmpArr.get(i));
            int tmp= (int)tmpArr.get(i);
            article.remove(tmp-i);
        }


        articleCount=0;

        printList();
    }

    public void finderForEminwon() throws IOException {
        this.document=Jsoup.connect(URL)                //connect
                .followRedirects(true)
                .method(Connection.Method.POST)
                .timeout(TIMEOUT)
                .get();

        Elements title = document.select("title");
        this.name=title.text();
        System.out.println("name: "+name);
        Elements header=document.select("thead");
        for(Element trTags: header.select("tr")){
            for(Element cat: trTags.select("th")){
                category.add(cat.text());
            }
        }

        Elements tables=document.select("table");
        Elements body=tables.select("tbody");
        //Elements body=document.select("tbody");
        for(Element trTags: body.select("tr")){
            ArrayList<String> temp = new ArrayList<>();
            article.add(temp);
            for(Element tdTags: trTags.select("td")){
                temp.add(tdTags.text());
            }
        }
        printList();
    }

    private void ParseAndInsert(int ColumnCount){
        setBoardTable();
        setColumnCount(ColumnCount);
    }
    private void setBoardTable(){

    }
    private void setColumnCount(int ColumnCount){

    }

    private void printList(){
        System.out.println("category table");
        for( String node:category){
            System.out.print(" [ " +node + " ] ");
        }
        System.out.println(" ");
        System.out.println("articletable");
        for(List<String> arr : article)
        {
            for(String s : arr)
            {
                System.out.print("[ " + s + " ] ");
            }
            System.out.println("");
        }
        /*
        System.out.println(category.get(0));
        System.out.println(category.get(1));
        System.out.println(category.get(2));
        System.out.println(category.get(3));
        System.out.println(category.get(4));
        System.out.println(article.get(2).get(0));
        System.out.println(article.get(2).get(1));
        System.out.println(article.get(2).get(2));
        System.out.println(article.get(2).get(3));
        */
        System.out.println("end");
    }

}

