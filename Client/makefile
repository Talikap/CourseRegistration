###   make clean - Delete all o files and the executable


# define makefile variables
CC = g++
CFLAGS  = -g -Wall -Weffc++ -std=c++11
LFLAGS  = -lboost_system -pthread

BGRSclient: bin/UserToServer.o bin/ServerToUser.o bin/connectionHandler.o
	$(CC) -o bin/BGRSclient bin/UserToServer.o bin/ServerToUser.o bin/connectionHandler.o $(LFLAGS)

bin/UserToServer.o: src/UserToServer.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/UserToServer.o src/UserToServer.cpp

bin/ServerToUser.o: src/ServerToUser.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/ServerToUser.o src/ServerToUser.cpp

bin/connectionHandler.o: src/connectionHandler.cpp include/connectionHandler.h
	$(CC) $(CFLAGS) -c -Iinclude -o bin/connectionHandler.o src/connectionHandler.cpp

#Clean the build directory
clean:
	rm -f bin/*

