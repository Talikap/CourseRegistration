package bgu.spl.net.srv;

public class CourseStat extends Message{

    final short courseNum;

    public CourseStat (short number) {
        courseNum = number;
    }

    @Override
    public Reply act(User user) {
        short messageType = 7;
        if (user.isLoggedIn() & !user.isStudent()){
            Course course= dataBase.containsCourse(courseNum);
            if (course!=null){
                String courseStat = "Course:" + "(" + courseNum + ")" + " " + course.getCourseName() + "\n";
                int seatsAvailable=course.getCapacity()-course.getCurrentStudentNum();
                courseStat = courseStat + "Seats Available:" + seatsAvailable + "/" + course.getCapacity()+ "\n";
                courseStat = courseStat + "Students Registered:";
                StudenComparator comp = new StudenComparator();
                course.getRegisteredStudents().sort(comp);
                String studentsList="[";
                for (User element : course.getRegisteredStudents()){
                    studentsList+=element.getUserName()+",";
                }
                int len=studentsList.length();
                if (len>1){
                    studentsList=studentsList.substring(0,len-1)+"]";}
                else {studentsList+="]";}
                courseStat+=studentsList;
                return new ACK(messageType,courseStat);
            }
        }
        return new ERROR(messageType);
    }
}
