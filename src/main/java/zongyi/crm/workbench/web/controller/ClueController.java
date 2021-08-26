package zongyi.crm.workbench.web.controller;

import zongyi.crm.settings.domain.User;
import zongyi.crm.settings.service.UserService;
import zongyi.crm.settings.service.impl.UserServiceImpl;
import zongyi.crm.utils.DateTimeUtil;
import zongyi.crm.utils.PrintJson;
import zongyi.crm.utils.ServiceFactory;
import zongyi.crm.utils.UUIDUtil;
import zongyi.crm.vo.PaginationVO;
import zongyi.crm.workbench.domain.Activity;
import zongyi.crm.workbench.domain.ActivityRemark;
import zongyi.crm.workbench.domain.Clue;
import zongyi.crm.workbench.service.ActivityService;
import zongyi.crm.workbench.service.ClueService;
import zongyi.crm.workbench.service.impl.ActivityServiceImpl;
import zongyi.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PipedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到【线索】控制器~");

        String path = request.getServletPath();


        if("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request , response);



        }else if("/workbench/clue/save.do".equals(path)){


            save(request , response);

        }else if("/workbench/clue/detail.do".equals(path)){


            detail(request , response);

        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){


            getActivityListByClueId(request , response);

        }else if("/workbench/clue/unbund.do".equals(path)){


            unbund(request , response);

        }else if("/workbench/clue/getActivityBySearchAndNotById.do".equals(path)){


            getActivityBySearchAndNotById(request , response);

        }else if("/workbench/clue/bund.do".equals(path)){


            bund(request , response);

        }



    }

    // _____________________________________________________________________________________________________关联市场活动和线索
    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("[--------------ClueController-关联市场活动和线索--------------]");


        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");


        System.out.println("+++++++++++++" + cid);
        System.out.println("+++++++++++++" + aids);

        // !!!!!这里没必要放到一个map里面，主要是因为到业务层还得必须拆开，所以不如直接传两个参数过去
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("cid" , cid);
        map.put("aids" , aids);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(map);

        PrintJson.printJsonFlag(response , flag);


    }

    // _____________________________________________________________________________________________________查询可以关联的市场活动
    private void getActivityBySearchAndNotById(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("[--------------ClueController-查询可以关联的市场活动--------------]");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String , String> map  = new HashMap<String, String>();
        map.put("aname" , aname);
        map.put("clueId" , clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityBySearchAndNotById(map);

        PrintJson.printJsonObj(response , aList);

    }

    // _______________________________________________________________________________________________________解除连接
    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response , flag);

    }

    // _______________________________________________________________________________________________________获取市场活动列表
    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response , aList);

    }


    // _______________________________________________________________________________________________________跳转到详细信息页
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c" , c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request , response);



    }

    // _______________________________________________________________________________________________________创建线索
    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response , flag);


    }


    // _______________________________________________________________________________________________________获取用户信息列表
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ClueController-获取用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response , uList);


    }


}













































