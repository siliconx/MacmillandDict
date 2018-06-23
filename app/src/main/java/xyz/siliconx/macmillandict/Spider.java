package xyz.siliconx.macmillandict;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Spider {
    private static String url = "https://www.macmillandictionary.com/search/british/direct/";
    private static String ug = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    public static ArrayList<String> extra(final String word) {
        ArrayList<String> result = new ArrayList<>();

        Document doc;
        Element leftContent, entryBody, syntax_coding, definition;
        Elements lis;
        StringBuilder temp = new StringBuilder();

        try {
            doc = Jsoup.connect(url)
                    .data("q", word)
                    .header("User-Agent", ug)
                    .get();
        } catch (IOException e) {
            System.out.println("http get error");
            return null;
        }

        if (doc == null) {
            System.out.println("no doc");
            return null;
        }

        // id=leftContent的div标签
        leftContent = doc.select("div#leftContent").first();

        if (leftContent == null) {
            System.out.println("no leftContent");
            return null;
        }

        // class=entryBody
        entryBody = leftContent.selectFirst("div.entryBody");

        if (entryBody == null) {
            System.out.println("no entryBody");
            return null;
        }

        lis = entryBody.select("li");

        if (lis == null || lis.size() == 0) {
            System.out.println("no lis");
            return null;
        }

        for (Element li : lis) {
            temp.setLength(0);  // 清空

            syntax_coding = li.selectFirst("span.SYNTAX-CODING");  // 词性
            definition = li.selectFirst("span.DEFINITION");  // 定义

            if (syntax_coding != null) {
                temp.append(syntax_coding.text().toUpperCase());
                temp.append(" -- ");
            }

            if (definition != null) {
                temp.append(definition.text());
            }

            result.add(temp.toString());
        }

        return result;
    }

    public static void main(String[] args) {
        ArrayList<String> result = extra("another");
        System.out.println(result.size());
        for (String e : result) {
            System.out.println(e);
        }
    }
}
