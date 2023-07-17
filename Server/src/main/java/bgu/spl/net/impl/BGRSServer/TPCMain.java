package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.BGRSMessageEncoderDecoder;
import bgu.spl.net.api.BGRSProtocol;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Database;

public class TPCMain {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        if (db.initialize("./Courses.txt")) {
            BaseServer server = new BaseServer(Integer.parseInt(args[0]), () -> new BGRSProtocol(), () -> new BGRSMessageEncoderDecoder()) {
                @Override
                protected void execute(BlockingConnectionHandler handler) {
                    new Thread(handler).start();
                }
            };
            server.serve();
        }
    }
}
