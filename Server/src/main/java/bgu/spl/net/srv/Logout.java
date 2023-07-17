package bgu.spl.net.srv;

public class Logout extends Message {

    public Logout() {
        super();
    }

    @Override
    public Reply act(User user) {
        Short messageType = 4;
        if (user.isLoggedIn()) {
            user.setLogin(false);
            return new ACK(messageType);
        }

        else {
            return new ERROR(messageType);
        }
    }
}
