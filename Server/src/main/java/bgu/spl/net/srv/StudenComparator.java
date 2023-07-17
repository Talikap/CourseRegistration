package bgu.spl.net.srv;

import java.util.Comparator;

public class StudenComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return u1.getUserName().compareTo(u2.getUserName());
    }
}
