package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.Comparator;

public class KdamCheck extends Message{

    final short courseNum;

    public KdamCheck(short number){
        super();
        courseNum=number;
    }

    @Override
    public Reply act(User user) {
        Short messageType=6;
        Course course = dataBase.containsCourse(courseNum);
        if (user.isStudent() & course!=null){
            ArrayList<Course> KdamCoursesList = course.getKdamCourses();
            Comparator<Course> comp = new CourseComparator();
            KdamCoursesList.sort(comp);
            String optional ="[";
            for (Course element : KdamCoursesList){
                optional+=element.getCourseNum()+",";
            }
            int len=optional.length();
            if (len>1){
                optional=optional.substring(0,len-1)+"]";}
            else {optional+="]";}
            return new ACK(messageType,optional);
        }
        return new ERROR(messageType);
    }

}
