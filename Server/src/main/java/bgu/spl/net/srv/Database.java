package bgu.spl.net.srv;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */


public class Database {

    final LinkedBlockingDeque<Course> courseList;
    final LinkedBlockingDeque<User> users;

    private static class SingletonHolder {
        final static Database instance = new Database();
    }

    private Database() {
        // TODO: implement
        courseList = new LinkedBlockingDeque<>();
        users = new LinkedBlockingDeque<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        // TODO: implement
        try {
            File courses = new File(coursesFilePath);
            Scanner myReader = new Scanner(courses);
            int i = 1;
            String nextCourse = myReader.nextLine();
            while (true){
                Course courseToBuild = new Course();
                int j = 0;
                String nextCourseNum = "";
                Character nextChar = nextCourse.charAt(j);
                while (!nextChar.equals('|')) {
                    nextCourseNum += nextChar;
                    j++;
                    nextChar = nextCourse.charAt(j);
                }
                short courseNum = Short.parseShort(nextCourseNum);
                boolean contains = false;
                if (!courseList.isEmpty()) {
                    for (Course element : courseList) {
                        if (element.getCourseNum() == courseNum) {
                            contains = true;
                            courseToBuild = element;
                            break;
                        }
                    }
                }
                if (!contains) {
                    courseToBuild.setCourseNum(courseNum);
                    courseToBuild.setCourseIndex(i);
                    i++;
                }
                String nextCourseName = "";
                j++;
                nextChar = nextCourse.charAt(j);
                while (!nextChar.equals('|')) {
                    nextCourseName += nextChar;
                    j++;
                    nextChar = nextCourse.charAt(j);
                }
                courseToBuild.setCourseName(nextCourseName);
                j = j + 2;
                nextChar = nextCourse.charAt(j);
                while (!nextChar.equals('|')) {
                    String nextKdamCourse = "";
                    while (!nextChar.equals(',') && !nextChar.equals(']')) {
                        nextKdamCourse = nextKdamCourse + nextChar;
                        j++;
                        nextChar = nextCourse.charAt(j);
                    }
                    if (!nextKdamCourse.equals( "")) {
                        short kdamNum = Short.parseShort(nextKdamCourse);
                        Course kdam = new Course();
                        boolean exist = false;
                        for (Course element : courseList) {
                            if (element.getCourseNum() == kdamNum) {
                                exist = true;
                                kdam = element;
                                break;
                            }
                        }
                        if (!exist) {
                            kdam.setCourseNum(kdamNum);
                            courseList.add(kdam);
                            kdam.setCourseIndex(i);
                            i++;
                        }
                        courseToBuild.addKdamCourses(kdam);
                        j++;
                        nextChar = nextCourse.charAt(j);
                    } else {
                        j++;
                        nextChar = nextCourse.charAt(j);
                    }
                }
                j++;
                nextChar = nextCourse.charAt(j);
                String nextCourseCapacity = "" + nextChar;
                while (j < nextCourse.length() - 1) {
                    j++;
                    nextChar = nextCourse.charAt(j);
                    nextCourseCapacity = nextCourseCapacity + nextChar;
                }
                courseToBuild.setCapacity(Integer.parseInt(nextCourseCapacity));
                courseList.add(courseToBuild);
                if (myReader.hasNextLine()) {
                    nextCourse = myReader.nextLine();
                }
                else {
                    break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public LinkedBlockingDeque<User> getUsers (){
        return users;
    }

    public Course containsCourse(short courseNum){
        for (Course c : courseList) {
            if (c.getCourseNum()==courseNum)
                return c;
        }
        return null;
    }

    public User containsUser(String name){
        for (User u : users) {
            if (u.getUserName().equals(name)) {
                return u;
            }
        }
        return null;
    }
}

