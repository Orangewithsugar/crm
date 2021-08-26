package zongyi.crm.settings.web.controller;

import zongyi.crm.settings.domain.User;
import zongyi.crm.settings.service.UserService;
import zongyi.crm.settings.service.impl.UserServiceImpl;
import zongyi.crm.utils.MD5Util;
import zongyi.crm.utils.PrintJson;
import zongyi.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器~");

        String path = request.getServletPath();
        System.out.println("输出path" + path);

        // 通过验证前端访问的serlvetPath，来验证是否是要执行登录相关的操作
        if("/settings/user/login.do".equals(path)){

            login(request , response);

        }else if("/settings/user/login.do".equals(path)){

            // xxx(request , response);

        }
    }


    // 负责处理登录功能的方法
    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到验证登录的操作~");
        // 取到前端发来的参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        System.out.println("--------------------" + loginAct + loginPwd);

        // 将密码的明文形式转换为密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        System.out.println("ip----" + ip);

        // 创建service对象
        // 未来的业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService)ServiceFactory.getService(new UserServiceImpl());


        try {
            User user = us.login(loginAct,loginPwd,ip);  // 不管登录操作具体执行到哪一步，应该抛对应的异常，在这层接收

            // 把user存到session域中，不用判断这里user为不为null，因为如果上一行有异常，那么执行catch，跳过下面这行代码
            request.getSession().setAttribute("user" , user);
            // 如果程序执行到此处，说明业务层没有为controller抛出任何的异常，表示登录成功
            /*
                {"success":true}  如果成功的话，就只返回这个success就够了
             */
            PrintJson.printJsonFlag(response , true);

        }catch (Exception e){

            e.printStackTrace();
            // 一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常。表示登录失败
            /*
                {"success":false,"msg":?}  如果失败的话，还得多返回一个msg，告诉前端登录验证到哪一步错了

             */
            String msg = e.getMessage();

            /*
                现在作为控制器，需要为ajax请求提供多项信息，可以有两种方式：
                    1、将多项信息打包成map，将map解析为json串
                    2、创建一个vo
                        private boolean success
                        private String msg

                    如果对于展现的信息，将来还会大量的使用，我们就创建一个vo类，使用方便
                    如果对于展现的信息，只有在这个需求中能够使用，我们使用map就可以了
             */
            Map<String , Object> map = new HashMap<String , Object>();
            map.put("success" , false);
            map.put("msg" , msg);
            PrintJson.printJsonObj(response , map);
        }
    }
}













































