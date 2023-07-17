package bgu.spl.net.srv;

public class Login extends Message{
    final String userName;
    final String Password;

    public Login(String name, String password){
        super();
        userName=name;
        Password=password;
    }

    @Override
    public Reply act(User user) {
        Short messageType=3;
        User toLogin = dataBase.containsUser(userName);
        if (toLogin!=null && toLogin.getPassword().equals(Password) && !toLogin.isLoggedIn()){
            user=toLogin;
            ACK reply=new ACK(messageType);
            user.setLogin(true);
            return reply;
        }
        else {
            return new ERROR(messageType);
        }
    }

    public String getUserName () {
        return userName;
    }
}