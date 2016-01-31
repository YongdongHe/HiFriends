package club.hifriends.auth;

/**
 * Created by heyon on 2016/1/29.
 */
public class AuthException extends Exception{
    String exceptionMsg;
    public AuthException(String msg){
        this.exceptionMsg = msg;
    }
    public String getExcptionMsg(){
        return exceptionMsg;
    }
}