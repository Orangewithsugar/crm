package zongyi.workbench.test;

import org.junit.Assert;
import org.junit.Test;
import zongyi.crm.utils.ServiceFactory;
import zongyi.crm.utils.UUIDUtil;
import zongyi.crm.workbench.domain.Activity;
import zongyi.crm.workbench.service.ActivityService;
import zongyi.crm.workbench.service.impl.ActivityServiceImpl;

/*
    JUnit:
        单元测试
        是未来实际项目开发中，用来代替主方法main的

 */
public class ActivityTest {       // 标准类名： domain名字 + Test


    @Test
    public void testSave(){       // 标准方法名：test + 测试功能名


        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广会");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);

        Assert.assertEquals(flag , true);


    }


    /*@Test
    public void testUpdate(){

        String str = null;

        str.length();


        System.out.println("234");

    }



    @Test
    public void testUpdate1(){



    }*/


}
