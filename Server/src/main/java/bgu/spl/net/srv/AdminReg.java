package bgu.spl.net.srv;

public class AdminReg extends Message{

    final String userName;
    final String Password;

    public AdminReg (String name,String password) {
        super();
        userName=name;
        Password=password;
    }

    @Override
    public Reply act(User user) {
        Short messageType = 1;
        User u = dataBase.containsUser(userName);
        if (u == null) {
            User toRegister = new User();
            toRegister.setUserName(userName);
            toRegister.setPassword(Password);
            toRegister.setAdmin();
            dataBase.getUsers().add(toRegister);
            return new ACK(messageType);
            }
        else {
            return new ERROR(messageType);
        }
    }
}
