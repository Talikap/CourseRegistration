package bgu.spl.net.api;

import bgu.spl.net.srv.*;

public class BGRSProtocol implements MessagingProtocol <Message> {

    private boolean ShouldTerminate=false;
    private User thisUser=new User();
    final Database dataBase = Database.getInstance();

    @Override
    public Reply process(Message msg) {
        if (msg instanceof Login) {
            if (thisUser.isLoggedIn()){
                short type=3;
                return new ERROR(type);
            }
            Reply reply = msg.act(thisUser);
            if (reply instanceof ACK) {
                Login temp = (Login) msg;
                thisUser = dataBase.containsUser(temp.getUserName());
                return reply;
            }
        }
        if (msg instanceof Logout) {
            ShouldTerminate = true;
        }

        return msg.act(thisUser);
    }

    @Override
    public boolean shouldTerminate() {
        return ShouldTerminate;
        }
    }
