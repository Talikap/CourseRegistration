package bgu.spl.net.srv;

public abstract class Reply extends Message {

    protected Short MessageType;

    protected Short [] OPCodes;

    public Reply (){
        OPCodes=new Short[2];
    }

    public Short [] getOPCodes(){return OPCodes;}

    public Reply act(User user) {
        return null;
    }
}
