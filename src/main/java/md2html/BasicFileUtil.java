/*
 * @Author: firefly-ing fireflyinging@163.com
 * @Date: 2022-08-08 13:09:10
 * @LastEditors: firefly-ing fireflyinging@163.com
 * @LastEditTime: 2022-08-08 13:10:34
 * @FilePath: /pdf2html/src/main/java/md2html/BasicFileUtil.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package md2html;

import org.apache.commons.lang3.StringUtils;

public class BasicFileUtil {
    public static boolean isAbsFile(String fileName) {
        if (OSUtil.isWinOS()) {
            // windows 操作系统时，绝对地址形如  c:\descktop
            return fileName.contains(":") || fileName.startsWith("\\");
        } else {
            // mac or linux
            return fileName.startsWith("/");
        }
    }

    /**
     * 将用户目录下地址~/xxx 转换为绝对地址
     *
     * @param path
     * @return
     */
    public static String parseHomeDir2AbsDir(String path) {
        String homeDir = System.getProperties().getProperty("user.home");
        return StringUtils.replace(path, "~", homeDir);
    }

    /**
     * 根据文件的mime获取文件类型
     *
     * @return
     */
    public static MediaType getMediaType(String path) {
        String magicNum = FileReadUtil.getMagicNum(path);
        if (magicNum == null) {
            return null;
        }

        return MediaType.typeOfMagicNum(magicNum);
    }
}
