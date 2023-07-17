package bgu.spl.net.srv;

public class CourseReg extends Message {

    final short courseNum;

    public CourseReg(short number) {
        courseNum = number;
    }

    @Override
    public Reply act(User user) {
        short messageType = 5;
        ERROR error = new ERROR(messageType);
        if (user.isLoggedIn() && user.isStudent()) {
            Course toRegister = dataBase.containsCourse(courseNum);
            if (toRegister != null && !toRegister.isFull()) {
                for (Course kdam : toRegister.getKdamCourses()) {
                    if (!user.getCourses().contains(kdam)) {
                        return error;
                    }
                }
                toRegister.registerStudent(user);
                user.getCourses().add(toRegister);
                return new ACK(messageType);
            }
            else {
                return error;
            }
        }
        else {
            return error;
        }
    }
}

