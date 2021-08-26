package zongyi.settings.test;

import sun.security.provider.MD5;
import zongyi.crm.utils.DateTimeUtil;
import zongyi.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {


    public static void main(String[] args) {

        // 验证失效时间expireTime
        // 失效时间
       /* String expireTime = "2021-05-25 10:00:00";

        // 当前系统时间
        *//*Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        System.out.println(strDate);*//*

        // 当前系统时间
        String currentTime = DateTimeUtil.getSysTime();


        // 两者比较 相当于是 expireTime-currentTime   不用关注这个值是多少，看他是大于0还是小于0就行了。
        // 如果失效时间大于系统当前时间的话，是大于0，也就是账号没有失效
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);*/

        /*String lockState = "0";   // 0代表锁定，1代表启用
        if("0".equals(lockState)){
            System.out.println("嘿！账号已锁定");  // 以后这里就是抛异常
        } else if("1".equals(lockState)){
            System.out.println("");
        }*/


        // 验证浏览器的ip地址
        /*String ip = "192.168.1.3";
        // 允许访问的ip地址
        String allowIps = "192.168.1.1,192.168.1.2";

        if(allowIps.contains(ip)){
            System.out.println("有效的ip地址，这个ip可以访问");   // 以后这里抛异常
        } else {
            System.out.println("无效的ip地址，这个ip地址不能访问");
        }
*/

        String pwd = "222";
        String md5Pwd = MD5Util.getMD5(pwd);

        System.out.println(md5Pwd);




    }
}

























