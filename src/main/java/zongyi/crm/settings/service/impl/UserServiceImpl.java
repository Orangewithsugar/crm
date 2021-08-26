package zongyi.crm.settings.service.impl;

import org.apache.ibatis.session.SqlSession;
import zongyi.crm.exception.LoginException;
import zongyi.crm.settings.dao.UserDao;
import zongyi.crm.settings.domain.User;
import zongyi.crm.settings.service.UserService;
import zongyi.crm.utils.DateTimeUtil;
import zongyi.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    // 创建一个dao层的代理类
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    // 方法-登录
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String , String> map = new HashMap<String, String>();
        map.put("loginAct" , loginAct);
        map.put("loginPwd" , loginPwd);

        User user = userDao.login(map);
        System.out.println("-------------------------------" + user);

        if(user==null){
            throw new LoginException("账号密码错误~");
        }

        // 如果程序能够成功的执行到这一行，说明账号密码是正确的
        // 需要继续向下验证其他3项信息

        // 1、验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){

            throw new LoginException("账号已失效~");
        }

        // 2、 判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){

            throw new LoginException("账号已锁定~");
        }


        // 3、 判断ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限~");    // 在很多
        }
        return user;
    }

    // 调用dao层代理类对象，获取用户信息列表
    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }


}
