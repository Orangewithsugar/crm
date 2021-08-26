package zongyi.crm.workbench.web.controller;

import zongyi.crm.settings.dao.UserDao;
import zongyi.crm.settings.domain.User;
import zongyi.crm.settings.service.UserService;
import zongyi.crm.settings.service.impl.UserServiceImpl;
import zongyi.crm.utils.*;
import zongyi.crm.vo.PaginationVO;
import zongyi.crm.workbench.domain.Activity;
import zongyi.crm.workbench.domain.ActivityRemark;
import zongyi.crm.workbench.service.ActivityService;
import zongyi.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到【市场活动】控制器~");

        String path = request.getServletPath();

        // 通过验证前端访问的serlvetPath，来验证是否是要执行-----【获取用户列表】-----相关的操作
        if("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request , response);



        }else if("/workbench/activity/save.do".equals(path)){


            save(request , response);

        }else if("/workbench/activity/pageList.do".equals(path)){


            pageList(request , response);

        }else if("/workbench/activity/delete.do".equals(path)){


            delete(request , response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){


            getUserListAndActivity(request , response);

        }else if("/workbench/activity/update.do".equals(path)){


            update(request , response);

        }else if("/workbench/activity/detail.do".equals(path)){


            detail(request , response);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){


            getRemarkListByAid(request , response);

        }else if("/workbench/activity/deleteRemark.do".equals(path)){


            deleteRemark(request , response);

        }else if("/workbench/activity/saveRemark.do".equals(path)){


            saveRemark(request , response);

        }else if("/workbench/activity/updateRemark.do".equals(path)){


            updateRemark(request , response);

        }



    }


    // ___________________________________________________________________________________________________修改备注
    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ActivityController-修改备注");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        Map<String , Object> map = new HashMap<String, Object>();
        map.put("success" , flag);
        map.put("ar" , ar);

        PrintJson.printJsonObj(response , map);


    }

    // ___________________________________________________________________________________________________添加备注
    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ActivityController-添加备注");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        Map<String , Object> map = new HashMap<String, Object>();
        map.put("success" , flag);
        map.put("ar"  , ar);

        PrintJson.printJsonObj(response , map);


    }

    // ___________________________________________________________________________________________________删除备注
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ActivityController-删除备注");

        String remarkId = request.getParameter("remarkId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(remarkId);

        PrintJson.printJsonFlag(response , flag);


    }

    // ___________________________________________________________________________________________________根据市场活动id取备注信息列表
    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ActivityController-根据市场活动id取备注信息列表");

        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response , arList);

    }


    // ________________________________________________________________________________________________跳转到详细信息页
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        System.out.println("控制器-跳转到详细信息页");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a" , a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request , response);


    }


    // _______________________________________________________________________________________更新市场活动修改的操作
    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("嘿~这里是-市场活动的修改操作~");


        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人：当前登录的用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();


        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);


        // 这是市场活动相关的业务，创建一个市场活动的代理类对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 执行方法
        boolean updateSuccess = as.update(a);

        // 然后把执行方法的结果返回给前端
        PrintJson.printJsonFlag(response , updateSuccess);


    }

    // ———————————————————————————————————————————————————————————————————————————————————————修改模态窗口需要的用户列表和单条市场活动
    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据市场活动查询单条记录的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        /*
            总结：
                controller调用service的方法，返回值应该是什么呢？
                我得想一想前端要什么，就要从service层取什么

                前端需要的，管业务层去要。

                本次就是
                        uList
                        a
                   以上两项信息，复用率不高，所以我们选择使用map打包这两项信息即可

         */
        Map<String  , Object> map = as.getUserListAndActivity(id);


        PrintJson.printJsonObj(response , map);



    }



    // ————————————————————————————————————————————————————————————————————————————————————————————————挑勾删除市场活动记录
    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到【挑勾删除市场活动记录-ActivityController】");

        // 1. 准备参数
        String ids[] = request.getParameterValues("id");

        // 2. 创建代理类对象，调用方法
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);

        // 3. 返回数据
        PrintJson.printJsonFlag(response , flag);

    }



    // ——————————————————————————————————————————————————————————————————————————————————作用：查询活动信息列表，分页查询显示
    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到【查询市场活动信息列表】的操作（结合条件查询+分页查询）");

        // 读取到数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        // 每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出略过的记录数。总结出来的公式，直接用
        int skipCount = (pageNo-1)*pageSize;


        // 传给dao层肯定不能直接传多个值，迟早要把这些参数放到一起，不如现在放。   不能用domain，因为没有对应合适的domain，所以只能用map集合
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name" , name);
        map.put("owner" , owner);
        map.put("startDate" , startDate);
        map.put("endDate" , endDate);
        map.put("skipCount" , skipCount);
        map.put("pageSize" , pageSize);

        // 创建一个业务层代理类对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            前端要什么，我就让业务层给我们返回什么
                （1）市场活动信息列表
                （2）total查询的总条数

            业务层拿到了以上两项信息之后，如何做返回呢？
                （1）map
                        map.put("dataList",dataList)
                        map.put("total",total)
                        然后
                            PrintJson map ---> json
                        得到前端想要的：
                            {"total":100,"dataList",[{市场活动1},{2},{3}...]}

                （2）vo


                复用率高就用vo，复用率低就用map。这种需求复用率超级高，因为很多地方要分页查询。

            PaginationVO<T>:
                （1）private int total;
                （2）private List<T> dataList;

                    PaginationVO<Activity>  vo = new PaginationVO<>;
                    vo.setTotal(total);
                    vo.setDataList(dataList);
                    然后
                        PrintJson  vo---> json
                    得到前端想要的：
                            {"total":100,"dataList",[{市场活动1},{2},{3}...]}

            将来分页查询是每个模块都由的，所以我们选择使用一个通用vo，这样操作起来比较方便。

         */
        PaginationVO<Activity> vo = as.pageList(map);

        // vo ---> {"total":100,"dataList",[{市场活动1},{2},{3}...]}        会自动转换的
        PrintJson.printJsonObj(response , vo);


    }

    // ——————————————————————————————————————————————————————————————————————————————————————————————作用：创建表单，添加一下
    private void save(HttpServletRequest request, HttpServletResponse response) {


        System.out.println("执行市场活动【添加操作】");

        // 把参数接收一下，然后存到一个Activity的对象里面
        String id = UUIDUtil.getUUID();   // 这个id不是前端传过来的，使我们自己生成的一个
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();  // 这个创建时间，是我们自己取得的当前系统时间
        // 这里的创建者是当前登录的用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();


        // 然后得把这些参数放到一个Activity对象里面。这样方便之后方法的编写，而且便于管理
        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);


        // 这是市场活动相关的业务，创建一个市场活动的代理类对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 执行方法
        boolean saveSuccess = as.save(a);

        // 然后把执行方法的结果返回给前端
        PrintJson.printJsonFlag(response , saveSuccess);


    }


    // ———————————————————————————————————————————————————————————————————————————————————————作用：获取用户信息列表，并且以json串的格式返回
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println(" 进入到【获取用户信息列表】~~~");

        // 这是用户相关的业务，所以创建用户的代理类
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        // 代理类调用方法，获取这个用户信息的集合
        List<User> uList = us.getUserList();

        // 把这个集合以json格式返回
        PrintJson.printJsonObj(response , uList);


    }


}













































