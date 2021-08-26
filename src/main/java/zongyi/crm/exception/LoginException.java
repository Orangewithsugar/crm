package zongyi.crm.exception;

public class LoginException extends Exception{

    // 有参的构造方法，会覆盖无参构造，但是我们不用无参，所以就不写了
    public LoginException(String msg){
        super(msg);
    }
}
