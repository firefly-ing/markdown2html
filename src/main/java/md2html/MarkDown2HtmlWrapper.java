/*
 * @Author: firefly-ing fireflyinging@163.com
 * @Date: 2022-08-08 10:54:28
 * @LastEditors: firefly-ing fireflyinging@163.com
 * @LastEditTime: 2022-08-08 13:15:51
 * @FilePath: /pdf2html/src/main/java/md2html/MarkDown2HtmlWrapper.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package md2html;

import com.google.common.base.Joiner;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MarkDown2HtmlWrapper {
    private static String MD_CSS;

    static {
        try {
            MD_CSS = FileReadUtil.readAll("md/koala.css");
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
            // 处理中文
            MD_CSS += "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>\n";
        } catch (Exception e) {
            System.out.println(e);
            MD_CSS = "";
        }
    }

    public static MarkdownEntity ofFile(String path, String cssPath) throws IOException{
        try {
            if (StringUtils.isNotEmpty(cssPath)) {
                MD_CSS = FileReadUtil.readAll(System.getProperty("user.dir") + "/" + cssPath);
                MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
                // 处理中文
                MD_CSS += "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>\n";
            }
        } catch (Exception e) {
            System.out.println(e);
            MD_CSS = "";
        }
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    public static MarkdownEntity ofUrl(String url) throws IOException{
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }

    // 将流转化为html文档
    public static MarkdownEntity ofStream(InputStream stream) {
        BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(stream, Charset.forName("UTF-8")));
                List<String> lines = bufferedReader.lines().collect(Collectors.toList());
                String content = Joiner.on("\n").join(lines);
                return ofContent(content);
    }
    // 将markdown语义文本转化为html
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }

    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

}
