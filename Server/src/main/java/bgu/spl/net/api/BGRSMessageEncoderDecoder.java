package bgu.spl.net.api;

import bgu.spl.net.srv.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BGRSMessageEncoderDecoder implements MessageEncoderDecoder<Message> {

    final byte[] decodedMessage = new byte[1 << 10];
    private int len = 0;
    private int counter = 2;
    private boolean flag=true;
    private boolean first=true;
    private int counter2 = 2;
    private String Name="";
    private Short opcode=0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (counter != 0) {
            decodedMessage[len] = nextByte;
            len++;
            counter--;
            return null;
        }
        Message toReturn;
        if (flag) {
            byte[] opcodeArray = new byte[2];
            opcodeArray[0] = decodedMessage[0];
            opcodeArray[1] = decodedMessage[1];
            opcode = bytesToShort(opcodeArray);
            flag = false;
        }

        if (opcode == 1) {
            if (nextByte != 0) {
                pushBytes(nextByte);
                return null;
            } else {
                if (first) {
                    Name = decodeUserName();
                } else {
                    String password = decodePassword();
                    toReturn = new AdminReg(Name, password);
                    Name="";
                    len=0;
                    return toReturn;
                }
            }

        }

        if (opcode == 2) {
            if (nextByte != 0) {
                pushBytes(nextByte);
                return null;
            } else {
                if (first) {
                    Name = decodeUserName();
                } else {
                    String password = decodePassword();
                    toReturn = new StudentReg(Name, password);
                    Name="";
                    len=0;
                    return toReturn;
                }
            }
        }

        if (opcode == 3) {
            if (nextByte != 0) {
                pushBytes(nextByte);
                return null;
            } else {
                if (first) {
                    Name = decodeUserName();
                } else {
                    String password = decodePassword();
                    toReturn = new Login(Name, password);
                    len=0;
                    flag=true;
                    return toReturn;
                }
            }
        }

        if (opcode == 4) {
            toReturn = new Logout();
            len=0;
            flag=true;
            counter=2;
            return toReturn;
        }

        if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 9 || opcode == 10) {
            if (counter2 != 0) {
                decodedMessage[len] = nextByte;
                len++;
                counter2--;
                return null;
            }
            if (opcode == 5) {
                short courseNum = decodeCourseNum();
                toReturn = new CourseReg(courseNum);
                len=0;
                flag=true;
                counter=2;
                counter2=2;
                return toReturn;
            }
            if (opcode == 6) {
                short courseNum = decodeCourseNum();
                toReturn = new KdamCheck(courseNum);
                len=0;
                flag=true;
                counter=2;
                counter2=2;
                return toReturn;
            }
            if (opcode == 7) {
                short CourseNumber = decodeCourseNum();
                toReturn = new CourseStat(CourseNumber);
                len=0;
                flag=true;
                counter=2;
                counter2=2;
                return toReturn;
            }
            if (opcode == 9) {
                short CourseNumber = decodeCourseNum();
                toReturn = new IsRegistered(CourseNumber);
                len=0;
                flag=true;
                counter=2;
                counter2=2;
                return toReturn;
            }

            if (opcode == 10) {
                short CourseNumber = decodeCourseNum();
                toReturn = new Unregister(CourseNumber);
                len=0;
                flag=true;
                counter=2;
                counter2=2;
                return toReturn;
            }
        }

        if (opcode == 8) {
            if (nextByte != 0) {
                pushBytes(nextByte);
                return null;
            }
            String UserName = decodeUserName();
            toReturn = new StudentStat(UserName);
            len=0;
            flag=true;
            counter=2;
            return toReturn;
        }

        if (opcode == 11) {
            toReturn = new MyCourses();
            len=0;
            flag=true;
            counter=2;
            return toReturn;
        }

        return null;

    }

    public void pushBytes(byte nextByte) {
        decodedMessage[len] = nextByte;
        len++;
    }

    public String popString (int Start){
        return new String(decodedMessage,Start,len-Start, StandardCharsets.UTF_8);
    }

    public short bytesToShort(byte[] byteArr)
    {
        ByteBuffer buffer = ByteBuffer.wrap(byteArr);
        return buffer.getShort();
    }

    private String decodeUserName (){
        String userName = popString(2);
        first=false;
        return userName;
    }

    private String decodePassword (){
        String password = popString(Name.length() + 2);
        flag=true;
        first=true;
        counter=2;
        return password;
    }

    private short decodeCourseNum (){
        byte[] courseNumArray = new byte[2];
        courseNumArray[0] = decodedMessage[2];
        courseNumArray[1] = decodedMessage[3];
        return bytesToShort(courseNumArray);
    }

    @Override
    public byte[] encode(Message message) {
        if (message instanceof ERROR) {
            ERROR error=(ERROR) message;
            byte[] opcodes = encodeOPcodes(error.getOPCodes());
            byte[] toReturn;
            toReturn = new byte[opcodes.length];
            for (int i=0;i<opcodes.length;i++) {
                toReturn[i] = opcodes[i];
            }
            return toReturn;
        }
        else {
            ACK ack=(ACK) message;
            byte[] opcodes = encodeOPcodes(ack.getOPCodes());
            byte[] toReturn;
            if (ack.getOptional().equals("")) {
                toReturn = new byte[opcodes.length];
                for (int i = 0; i < opcodes.length; i++) {
                    toReturn[i] = opcodes[i];
                }
                return toReturn;
            }
            else {
                byte[] optional = ack.getOptional().getBytes();
                toReturn = new byte[opcodes.length + optional.length+1];
                for (int i = 0; i < opcodes.length; i++) {
                    toReturn[i] = opcodes[i];
                }
                for (int j = opcodes.length; j < toReturn.length-1; j++) {
                    toReturn[j] = optional[j-opcodes.length];
                }
                toReturn[toReturn.length-1]='\0';
                return toReturn;
            }
        }
    }



    public byte [] encodeOPcodes(Short[] opcodes) {
        byte[] toReturn = new byte[4];
        byte[] outOp=shortToBytes(opcodes[0]);
        for (int i = 0; i < 2 ; i++) {
            toReturn[i] = outOp[i];
        }
        byte[] inOp=shortToBytes(opcodes[1]);
        for (int j = 2; j < 4 ; j++) {
            toReturn[j] = inOp[j-2];
        }
        return toReturn;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}