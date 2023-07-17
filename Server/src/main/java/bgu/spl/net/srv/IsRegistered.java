package bgu.spl.net.srv;

public class IsRegistered extends Message{

    final short courseNum;

    public IsRegistered (short number) {
        courseNum = number;
    }

    @Override
    public Reply act(User user) {
        short messageType = 9;
        if (user.isLoggedIn() & user.isStudent()){
            Course course= dataBase.containsCourse(courseNum);
            if (course!=null){
                String isRegistered;
                if (course.getRegisteredStudents().contains(user))
                    isRegistered= "REGISTERED";
                else
                    isRegistered= "NOT REGISTERED";
                return new ACK(messageType,isRegistered);
            }
        }
         return new ERROR(messageType);
    }
}
