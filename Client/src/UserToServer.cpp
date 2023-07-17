#include <iostream>
#include <thread>
#include "../include/connectionHandler.h"
#include "ServerToUser.cpp"
#include <boost/lexical_cast.hpp>
#include <condition_variable>

//
// Created by spl211 on 04/01/2021.
//
class UserToServer {
    bool shouldStopReading=false;
    bool isLoggedIn=false;
public :
    virtual ~UserToServer();
    void shortToBytes(short i,char *array,unsigned int from);
    std::string encodeCourseNum(unsigned int i, char currChar, std::string Message);
    std::string encodeUserCredentials(unsigned int i, char curr, std::string Message);
    void setIsLoggedIn (bool stat);
    void setShouldStopReading(bool stat);
    bool getLoginStat ();
    bool getShouldStopReading ();
};


    UserToServer::~UserToServer()=default;
    void UserToServer ::setIsLoggedIn(bool stat) {isLoggedIn=stat;}
    void UserToServer ::setShouldStopReading(bool stat) {shouldStopReading=stat;}
    bool UserToServer ::getLoginStat() {return isLoggedIn;}
    bool UserToServer::getShouldStopReading() {return shouldStopReading;}

    void UserToServer:: shortToBytes(short i, char *array,unsigned int from) {
        array[from] = ((i >> 8) & 0xFF);
        array[from+1] = (i & 0xFF);
    }

    std::string UserToServer::encodeUserCredentials(unsigned int i, char curr, std::string Message) {
        std::string result;
        while ((curr != ' ') & (i < Message.length())) {
            result += curr;
            i++;
            curr = Message[i];
        }
        return result;
    }


    std::string UserToServer::encodeCourseNum(unsigned int i, char currChar, std::string Message) {
    std::string result;
    while (i < Message.length()) {
        result += currChar;
        i++;
        currChar = Message[i];
    }
    return result;
}


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    UserToServer *sender = new UserToServer();
    ServerToUser *receiver = new ServerToUser(connectionHandler);
    std::thread th1(&ServerToUser::run, receiver);

    //sending loop
    while (!sender->getShouldStopReading()) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        unsigned int i = 0;
        std::string messageType;
        char curr = line[i];
        while ((curr != ' ') & (i<line.length())) {
            messageType += curr;
            i++;
            curr = line[i];
        }
        char opcodeArray[2];
        if (messageType == "ADMINREG") {
            std::string encodedUserName;
            sender->shortToBytes(1, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i++;
            encodedUserName += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedUserName)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i = i + encodedUserName.length() + 1;
            std::string encodedPassword;
            encodedPassword += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedPassword)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "STUDENTREG") {
            std::string encodedUserName;
            sender->shortToBytes(2, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i++;
            encodedUserName += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedUserName)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i = i + encodedUserName.length() + 1;
            std::string encodedPassword;
            encodedPassword += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedPassword)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "LOGIN") {
            std::string encodedUserName;
            sender->shortToBytes(3, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i++;
            encodedUserName += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedUserName)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i = i + encodedUserName.length() + 1;
            std::string encodedPassword;
            encodedPassword += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedPassword)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            if (!sender->getLoginStat()){
                sender->setIsLoggedIn(true);
            }
        }

        if (messageType == "LOGOUT") {
            sender->shortToBytes(4, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::string end="\0";
            if (!connectionHandler.sendLine(end)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            if (sender->getLoginStat()){
                sender->setShouldStopReading(true);
            }
        }
        if (messageType == "COURSEREG") {
            char arrayToSend[4];
            std::string encodedCourseNum;
            sender->shortToBytes(5, arrayToSend, 0);
            i++;
            encodedCourseNum += sender->encodeCourseNum(i, line[i], line);
            short courseNum = boost::lexical_cast<short>(encodedCourseNum);
            sender->shortToBytes(courseNum, arrayToSend, 2);
            if (!connectionHandler.sendBytes(arrayToSend, 5)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "KDAMCHECK") {
            char arrayToSend[4];
            std::string encodedCourseNum;
            sender->shortToBytes(6, arrayToSend, 0);
            i++;
            encodedCourseNum += sender->encodeCourseNum(i, line[i], line);
            short courseNum = boost::lexical_cast<short>(encodedCourseNum);
            sender->shortToBytes(courseNum, arrayToSend, 2);
            if (!connectionHandler.sendBytes(arrayToSend, 5)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "COURSESTAT") {
            char arrayToSend[4];
            std::string encodedCourseNum;
            sender->shortToBytes(7, arrayToSend, 0);
            i++;
            encodedCourseNum += sender->encodeCourseNum(i, line[i], line);
            short courseNum = boost::lexical_cast<short>(encodedCourseNum);
            sender->shortToBytes(courseNum, arrayToSend, 2);
            if (!connectionHandler.sendBytes(arrayToSend, 5)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "STUDENTSTAT") {
            std::string encodedUserName;
            sender->shortToBytes(8, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            i++;
            encodedUserName += sender->encodeUserCredentials(i, line[i], line);
            if (!connectionHandler.sendLine(encodedUserName)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "ISREGISTERED") {
            char arrayToSend[4];
            std::string encodedCourseNum;
            sender->shortToBytes(9, arrayToSend, 0);
            i++;
            encodedCourseNum += sender->encodeCourseNum(i, line[i], line);
            short courseNum = boost::lexical_cast<short>(encodedCourseNum);
            sender->shortToBytes(courseNum, arrayToSend, 2);
            if (!connectionHandler.sendBytes(arrayToSend, 5)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }
        if (messageType == "UNREGISTER") {
            char arrayToSend[4];
            std::string encodedCourseNum;
            sender->shortToBytes(10, arrayToSend, 0);
            i++;
            encodedCourseNum += sender->encodeCourseNum(i, line[i], line);
            short courseNum = boost::lexical_cast<short>(encodedCourseNum);
            sender->shortToBytes(courseNum, arrayToSend, 2);
            if (!connectionHandler.sendBytes(arrayToSend, 5)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }

        if (messageType == "MYCOURSES") {
            sender->shortToBytes(11, opcodeArray, 0);
            if (!connectionHandler.sendBytes(opcodeArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::string end="\0";
            if (!connectionHandler.sendLine(end)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }
    }
    th1.join();
    connectionHandler.close();
    delete receiver;
    delete sender;
    return 0;
    }