package bgu.spl.net.srv;

public class MyCourses extends Message{

    public MyCourses() {
        super();
    }

    @Override
    public Reply act(User user) {
        short messageType = 11;
        if (user.isLoggedIn() & user.isStudent()){
        CourseComparator comp = new CourseComparator();
        user.getCourses().sort(comp);
        String coursesList="[";
            for (Course element : user.getCourses()){
                coursesList+=element.getCourseNum()+",";
            }
            int len=coursesList.length();
            if (len>1){
                coursesList=coursesList.substring(0,len-1)+"]";}
            else {coursesList+="]";}
            return new ACK(messageType, coursesList);
        }
        return new ERROR(messageType);
    }
}
