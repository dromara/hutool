package cn.hutool.core.io;

import cn.hutool.core.util.StrUtil;

import java.util.Calendar;

/**
 * @desc   ：路径工具类
 * @author ：dahuoyzs
 * @since  ：5.2.5
 */
public class PathUtil {

    /**
     * 路径添加月份
     *
     * @param fileName                 文件路径（包含文件名）
     * @return String               返回 路径中添加了月份的路径
     * 如：
     *      输入  /opt/project/avatar/a.png
     *      返回  /opt/project/avatar/2019/10/a.png
     */
    public static String toMonthPath(String fileName){

        String handler = normalize(fileName);

        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = calendar.get(Calendar.MONTH)+1+"/";
        String monthPath = year + "/" + month;

        int index = handler.lastIndexOf("/")+1;
        String pre = handler.substring(0,index);

        return  StrUtil.appendIfMissing(pre,monthPath) + handler.substring(index);
    }

    /**
     * 路径添加日期
     *
     * @param fileName              又拍云路径（包含文件名）
     * @return String               返回 路径中添加了日期的路径
     * 如：
     *      输入  /opt/project/avatar/a.png
     *      返回  /opt/project/avatar/2019/10/15/a.png
     *      d:a/b/c/
     *      d:a/b/c/2019/10/15/
     */
    public static String toDayPath(String fileName){

        String handler = toMonthPath(fileName);

        Calendar calendar = Calendar.getInstance();
        String day = calendar.get(Calendar.DATE)+"/";

        int index = handler.lastIndexOf("/")+1;
        String pre = handler.substring(0,index);

        return  StrUtil.appendIfMissing(pre,day) + handler.substring(index);
    }

    /**
     * 获得文件名(不带后缀)
     *
     * @param  fileName                 路径+文件名
     * @return String                   返回 文件名去掉后缀
     * 如：
     *      输入  D:\a\b\c/d/d/pp.txt
     *      返回  pp
     */
    public static String getFileBaseName(String fileName){

        String handler = normalize(fileName);

        int separatorIndex = handler.lastIndexOf("/");
        int dotIndex = handler.lastIndexOf('.');

        if (dotIndex < 0 || separatorIndex >= dotIndex)
            return "";

        return handler.substring(separatorIndex + 1, dotIndex);
    }

    /**
     * 获得文件后缀
     *
     * @param  fileName                 路径+文件名
     * @return String                   返回 文件后缀
     * 如：
     *      输入  D:\a\b\c/d/d/pp.txt
     *      返回  txt
     */
    public static String getFileSuffix(String fileName){
        String handler = normalize(fileName);

        int dotIndex = handler.lastIndexOf( '.');
        if (dotIndex < 0){
            return "";
        }

        return handler.substring(dotIndex + 1);
    }

    /**
     * 标准化文件路径
     *
     * @param path                  磁盘路径
     * @return String               返回 标准话的/形式的路径
     * 如：
     *      输入  D:\a\b\c/d/d/pp.txt
     *      返回  D:/a/b/c/d/d/pp.txt       [标准]
     *      输入  D:\a\b\c/d/d
     *      返回  D:/a/b/c/d/d/             [标准+补齐]
     */
    public static String normalize(String path){
        String handler;
        if (path.contains(".")){
            handler = path;
        }else {
            handler = StrUtil.appendIfMissing(path,"/");
        }
        return handler.replace("\\","/");
    }

}
