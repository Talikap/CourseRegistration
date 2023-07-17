package bgu.spl.net.srv;

import java.util.Comparator;

public class CourseComparator implements Comparator<Course> {

    @Override
    public int compare(Course c1, Course c2) {
        return c1.getCourseIndex()-c2.getCourseIndex();
    }
}
