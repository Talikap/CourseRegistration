package bgu.spl.net.srv;

import java.util.ArrayList;

public class Course {
    private int Index;
    private short courseNum;
    private String courseName;
    final ArrayList<Course> KdamCourses;
    private int Capacity;
    private int currentStudentNum;
    final ArrayList<User> registeredStudents;

    public Course (int index,short number,String name,int capacity) {
        Index=index;
        courseNum=number;
        courseName=name;
        KdamCourses=new ArrayList<>();
        Capacity=capacity;
        registeredStudents=new ArrayList<>();
        currentStudentNum=0;
    }

    public Course () {
        courseNum=0;
        courseName="";
        KdamCourses=new ArrayList<>();
        Capacity=0;
        registeredStudents=new ArrayList<>();
        currentStudentNum=0;

    }


    public void setCourseNum(short courseNum) { this.courseNum=courseNum; }

   public void setCourseName(String courseName) { this.courseName = courseName;}

    public void addKdamCourses(Course kdamCourse) { KdamCourses.add(kdamCourse); }

    public void setCapacity(int numOfMaxStudents) {this.Capacity = numOfMaxStudents;}

    public void unRegisterStudent(User s){   registeredStudents.remove(s);
        currentStudentNum--;
    }

    public void registerStudent (User s){
        currentStudentNum++;
        registeredStudents.add(s);
    }

    public ArrayList<Course> getKdamCourses() {
        return KdamCourses;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCurrentStudentNum () {return currentStudentNum;}

    public int getCourseNum () {return courseNum;}

    public ArrayList<User> getRegisteredStudents() {
        return registeredStudents;
    }

    public boolean isFull() {return currentStudentNum==Capacity;}

    public int getCourseIndex() {return Index;}

    public void setCourseIndex(int index) {Index=index;}

    public int getCapacity() {return Capacity;}
}
