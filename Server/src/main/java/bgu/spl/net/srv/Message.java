package bgu.spl.net.srv;

public abstract class Message {
    final Database dataBase;

    public Message () {
        dataBase = Database.getInstance();
    }

    public abstract Reply act(User user);
}
