package zongyi.crm.web.listener;

import zongyi.crm.settings.domain.DicValue;
import zongyi.crm.settings.service.DicService;
import zongyi.crm.settings.service.impl.DicServiceImpl;
import zongyi.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {


    /*

        该方法是用来监听上下文域对象的方法，

            该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象自动创建。
            对象创建完毕后，马上自动执行该方法。

            event：
                该参数能够取得监听的对象，监听的是什么对象，就可以通过该参数event取得什么对象。
                例如我们现在监听的是上下文域对象，通过event就可以取得上下文域对象。

     */
    public void contextInitialized(ServletContextEvent event) {

        // System.out.println("上下文域对象创建啦~~");

        System.out.println("【开始】——————————————————服务器缓存处理数据字典");

        // 1. 拿到上下文域对象
        ServletContext application = event.getServletContext();

        // 2. 取数据字典
        /*
            应该管业务层要：7个list

            可以打包成一个map
            业务层是这样保存数据的：
                        map.put("appellationList",dvList1);
                        map.put("clueStateList",dvList2);
                        map.put("stageList",dvList3);
                        ...
                        ...
         */
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String , List<DicValue>> map = ds.getAll();
        // 将map解析为山下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {

            application.setAttribute(key , map.get(key));

        }


        System.out.println("【结束】——————————————————服务器缓存处理数据字典");

    }
}
