//
// Created by spl211 on 04/01/2021.
//
#include <iostream>
#include "../include/connectionHandler.h"
class ServerToUser {
private:
    ConnectionHandler &connectionHandler;
    bool shouldTerminate;

public :
    ServerToUser(ConnectionHandler &connectionHandler1) : connectionHandler(connectionHandler1),
                                                          shouldTerminate(false) {}



    virtual ~ServerToUser() {}

    void run() {
        char answer[4];
        while (!shouldTerminate) {
            connectionHandler.getBytes(answer, 4);
            char inOp[2] = {answer[0], answer[1]};
            char outOp[2] = {answer[2], answer[3]};
            short answerOp = bytesToShort(inOp);
            short sentOp = bytesToShort(outOp);
            std::string optional;
            if (answerOp == 12) {
                if ((sentOp == 6) | (sentOp == 7) | (sentOp == 8) | (sentOp == 9) | (sentOp == 11)) {
                    connectionHandler.getLine(optional);
                }
                if (sentOp == 4) {
                    shouldTerminate = true;
                }
                std::cout << "ACK " + std::to_string(sentOp) << std::endl;

                if (optional!=""){
                    std::cout << optional << std::endl;
                }

            } else {
                std::cout << "ERROR " + std::to_string(sentOp) << std::endl;
            }
        }
    }

    short bytesToShort(char *bytesArr) {
        short result = (short) ((bytesArr[0] & 0xff) << 8);
        result += (short) (bytesArr[1] & 0xff);
        return result;
    }
}
;