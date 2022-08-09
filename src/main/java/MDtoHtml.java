
import md2html.MarkDown2HtmlWrapper;
import md2html.MarkdownEntity;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class MDtoHtml {
    public static void main(String[] args) throws IOException{
        String path = System.getProperty("user.dir") + "/";
        if (args.length != 3) {
            // 新建一个参数列表
            String[] temArgs = new String[3];
            temArgs[0] = args[0];
            String cssPath = "";
            temArgs[1] = cssPath;
            temArgs[2] = args[1];
            args = temArgs;
        }
        String file = path +  args[0];
        String cssPath = args[1];
        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file, cssPath);

        System.out.println(html.toString());

         md2html(html.toString(), path + args[2]);
//        FileWriteUtil.saveFile(src, inputType)
    }
    // 生成html文件
    static void md2html(String html, String fileName) throws IOException{
        File file = new File(fileName);
        //如果文件不存在，创建一个文件
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(html);
        } catch (IOException e) {

        } finally {
            bw.close();
            fw.close();
        }

        System.out.println("md to html success!");
    }
}
