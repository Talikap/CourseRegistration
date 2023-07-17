package bgu.spl.net.srv;

public class ERROR extends Reply{

    public ERROR(Short messageType){
        super();
        MessageType=messageType;
        short opcode=13;
        OPCodes[0]=opcode;
        OPCodes[1]=messageType;
    }
}
