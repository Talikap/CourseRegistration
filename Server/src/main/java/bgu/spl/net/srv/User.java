package bgu.spl.net.srv;

import java.util.ArrayList;

public class User {
    private String userName;
    private String Password;
    private boolean isStudent;
    private boolean LoggedIn;
    private ArrayList<Course> courses;

    public User () {
        userName="";
        Password="";
        LoggedIn=false;
    }

    public User (String name,String password){
        userName=name;
        Password=password;
        LoggedIn=false;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setStudent(){
        isStudent=true;
        courses = new ArrayList<>();
    }

    public void setAdmin() {isStudent=false;}

    public void setUserName(String UserName){userName=UserName;}

    public void setPassword(String password) {
        Password = password;
    }

    public void setLogin(boolean loggedIn) {
        LoggedIn = loggedIn;
    }

    public boolean isLoggedIn (){
        return LoggedIn;
    }

    public boolean isStudent(){return isStudent;}

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public boolean unRegisterCourse (Course c){
        c.unRegisterStudent(this);
        return courses.remove(c);
    }
}
