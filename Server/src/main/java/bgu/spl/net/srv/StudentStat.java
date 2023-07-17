package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.Comparator;

public class StudentStat extends Message {

    final String studentName;

    public StudentStat(String name) {
        super();
        studentName = name;
    }

    @Override
    public Reply act(User user) {
        Short messageType = 8;
        if (user.isLoggedIn() & !user.isStudent()) {
            User student = dataBase.containsUser(studentName);
            if (student != null) {
                String studentStat = "Student: " + studentName + "\n";
                ArrayList<Course> CoursesList = student.getCourses();
                Comparator<Course> comp = new CourseComparator();
                CoursesList.sort(comp);
                String coursesList="[";
                for (Course element : CoursesList){
                    coursesList+=element.getCourseNum()+",";
                }
                int len=coursesList.length();
                if (len>1){
                    coursesList=coursesList.substring(0,len-1)+"]";}
                else {coursesList+="]";}
                studentStat+="Courses: "+coursesList;
                return new ACK(messageType, studentStat);
            }
        }
        return new ERROR(messageType);
    }
}