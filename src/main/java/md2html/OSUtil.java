/*
 * @Author: firefly-ing fireflyinging@163.com
 * @Date: 2022-08-08 13:09:56
 * @LastEditors: firefly-ing fireflyinging@163.com
 * @LastEditTime: 2022-08-08 13:09:58
 * @FilePath: /pdf2html/src/main/java/md2html/OSUtil.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package md2html;

public class OSUtil {
    /**
     * 是否windows系统
     */
    public static boolean isWinOS() {
        boolean isWinOS = false;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String sharpOsName = osName.replaceAll("windows", "{windows}").replaceAll("^win([^a-z])", "{windows}$1")
                    .replaceAll("([^a-z])win([^a-z])", "$1{windows}$2");
            isWinOS = sharpOsName.contains("{windows}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isWinOS;
    }
}
