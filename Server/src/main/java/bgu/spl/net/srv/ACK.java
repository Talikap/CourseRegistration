package bgu.spl.net.srv;

public class ACK extends Reply{

    final String Optional;

    public ACK(Short messageType){
        super();
        MessageType=messageType;
        short opcode=12;
        OPCodes[0]=opcode;
        OPCodes[1]=messageType;
        Optional="";
    }

    public ACK(Short messageType,String optional){
        super();
        MessageType=messageType;
        short opcode=12;
        OPCodes[0]=opcode;
        OPCodes[1]=messageType;
        Optional=optional;
    }

    public String getOptional() {
        return Optional;
    }
}
