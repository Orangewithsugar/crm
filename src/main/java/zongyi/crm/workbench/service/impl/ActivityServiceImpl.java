package zongyi.crm.workbench.service.impl;

import zongyi.crm.settings.dao.UserDao;
import zongyi.crm.settings.domain.User;
import zongyi.crm.utils.SqlSessionUtil;
import zongyi.crm.vo.PaginationVO;
import zongyi.crm.workbench.dao.ActivityDao;
import zongyi.crm.workbench.dao.ActivityRemarkDao;
import zongyi.crm.workbench.domain.Activity;
import zongyi.crm.workbench.domain.ActivityRemark;
import zongyi.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    // ————————————————————————————————————————————————————————————————————————————————————————————————作用：添加一个创建表单到数据库中
    public boolean save(Activity a) {

        boolean flag = true ;
        // 这里可能会有异常，之前我们写登录操作的时候，抛了自定义异常，这种要掌握，之后公司会用。这个项目中之后我们就用flag，方便一点。
        int count = activityDao.save(a);

        if(count != 1){
            flag = false;
        }

        return flag;
    }


    // ————————————————————————————————————————————————————————————————————————————————————————————————作用：分页查询市场活动列表。
    /*
        目的：取得total
             取得dataList
             创建一个vo对象，将total和dataList封装到vo中
             将vo返回
     */
    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        System.out.println("1");

        // 1、取得total
        int total = activityDao.getTotalByCondition(map);

        // 2、取得dataList
        List<Activity> dataList = activityDao.getDataListByCondition(map);


        // 输出看看有没有查询到id
        for (Activity a : dataList) {

            System.out.println(a.getId());
            System.out.println(a.getName());

        }


        // 3、创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        // 4、将vo返回
        return vo;
    }


    // ————————————————————————————————————————————————————————————————————————————————————————————————删除市场活动记录以及其备注
    public boolean delete(String[] ids) {

        boolean flag = true ;

        // 1. 拿到关联的备注总数量 ，调用谁的方法就用谁的代理类对象
        int count1 = activityRemarkDao.getCountByAids(ids);

        // 2. 删除对应备注，会返回一个数量
        int count2 = activityRemarkDao.deleteByAids(ids);

        // 3. 比较总数量和删除的数量，如果相等的话，说明记录删除完全
        if (count1 != count2){
            flag = false;
        }

        // 4. 删除市场活动记录。返回1表示成功删除
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }

        return flag;
    }



    // ———————————————————————————————————————————————————————————————————————————————————————————修改模态窗口-取用户列表和单条市场活动
    public Map<String, Object> getUserListAndActivity(String id) {

        // 1. 取uList 。写过的方法直接用就行
        List<User> uList = userDao.getUserList();

        // 2. 取a
        Activity a = activityDao.getById(id);

        // 3. 打包到map里面返回
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("uList" , uList);
        map.put("a" , a);

        return map;
    }


    // ———————————————————————————————————————————————————————————————————————————————————————————————————————————修改操作
    public boolean update(Activity a) {

        boolean flag = true ;

        int count = activityDao.update(a);

        if(count != 1){
            flag = false;
        }

        return flag;

    }


    // ________________________________________________________________________________________________________________跳转到详细信息页
    public Activity detail(String id) {

        // 不能直接复用这个方法：activityDao.getById(id);   因为这个方法查到的owner是32位的随机串，而不是我们想要的宗一这种名字

        Activity a = activityDao.detail(id);


        return a;
    }


    // ___________________________________________________________________________________________________根据市场活动id取备注信息列表
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);


        return arList;
    }


    // ___________________________________________________________________________________________________删除备注
    public boolean deleteRemark(String remarkId) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemark(remarkId);

        if(count != 1){
            flag = false;
        }

        return flag;
    }



    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }



    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }


    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }


    public List<Activity> getActivityBySearchAndNotById(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityBySearchAndNotById(map);

        return aList;
    }
}

















































