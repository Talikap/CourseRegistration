package bgu.spl.net.srv;

public class Unregister extends Message {

    final short courseNum;

    public Unregister(short number) {
        super();
        courseNum = number;
    }

    @Override
    public Reply act(User user) {
        Short messageType = 10;
        if (user.isStudent()) {
            Course toUnregister = dataBase.containsCourse(courseNum);
            if (toUnregister != null && toUnregister.getRegisteredStudents().contains(user) && user.getCourses().contains(toUnregister)) {
                if (user.unRegisterCourse(toUnregister)) {
                    toUnregister.getRegisteredStudents().remove(user);
                    return new ACK(messageType);
                }
                else {
                    return new ERROR(messageType);
                }
            }
        }
        return new ERROR(messageType);
    }
}

