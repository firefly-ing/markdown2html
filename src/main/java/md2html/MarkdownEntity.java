/*
 * @Author: firefly-ing fireflyinging@163.com
 * @Date: 2022-08-08 10:30:54
 * @LastEditors: firefly-ing fireflyinging@163.com
 * @LastEditTime: 2022-08-08 10:52:29
 * @FilePath: /pdf2html/src/main/java/md2html/MarkdownEntity.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package md2html;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class MarkdownEntity {

    // 代表一个空格
    private static String  BLANK = " ";

    public static String TAG_WIDTH = "<style type=\"text/css\"> %s { width:85%%} </style>";
    // css样式，美化输出html的样式
    private String css; 

    // 定义通用属性，会在html内容外增加一个<div>标签，主要设置宽高、字体
    private Map<String, String> divStyle = new ConcurrentHashMap<>();

    // 转换后的html文档
    private String html;

    public MarkdownEntity() {}

    public MarkdownEntity(String html) {this.html = html;}
    
    
    public String toString() {
        return css + "\n<div " + parseDiv() + ">\n" + html + "\n</div>";
    }

    private String parseDiv() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : divStyle.entrySet()) {
            builder.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        return builder.toString();
    }

    // 添加div属性
    public void addDivStyle(String attrKey, String value) {
        if (divStyle.containsKey(attrKey)) {
            divStyle.put(attrKey, divStyle.get(attrKey) + BLANK + value);
        } else {
            divStyle.put(attrKey, value);
        }
    }

    public void addWidthCss(String tag) {
        String wcss = String.format(TAG_WIDTH, tag);
        css += wcss;
    }
}
