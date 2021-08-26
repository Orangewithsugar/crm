package zongyi.crm.settings.service;

import zongyi.crm.exception.LoginException;
import zongyi.crm.settings.domain.User;

import java.util.List;

public interface UserService {


    User login(String loginAct, String loginPwd, String ip) throws LoginException;


    List<User> getUserList();
}
