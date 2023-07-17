package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.BGRSMessageEncoderDecoder;
import bgu.spl.net.api.BGRSProtocol;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Reactor;

public class ReactorMain {
    public static void main(String[] args) {
        Database db=Database.getInstance();
        if (db.initialize("/home/spl211/Desktop/Courses.txt")) {
            Reactor server = new Reactor(Integer.parseInt(args[0]),Integer.parseInt(args[1]), () -> new BGRSProtocol(), () -> new BGRSMessageEncoderDecoder());
            server.serve();
        }
    }
}
