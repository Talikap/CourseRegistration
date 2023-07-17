package bgu.spl.net.srv;

public class StudentReg extends Message {

    final String userName;
    final String Password;

    public StudentReg(String name,String password){
        userName=name;
        Password=password;
    }

    @Override
    public Reply act(User user) {
        Short messageType= 2;
        User u = dataBase.containsUser(userName);
        if (u == null) {
            User toRegister = new User();
            toRegister.setUserName(userName);
            toRegister.setPassword(Password);
            toRegister.setStudent();
            dataBase.getUsers().add(toRegister);
            return new ACK(messageType);
        }
        return new ERROR(messageType);
    }
}
