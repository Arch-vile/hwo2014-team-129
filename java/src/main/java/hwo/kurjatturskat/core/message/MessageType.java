package hwo.kurjatturskat.core.message;

public enum MessageType {

    // NOTE: Enums must be in small letter and match to the msgType field value
    // in the messages. Otherwise gson cannot map them
    carPositions, yourCar, join, gameInit, gameStart, throttle, switchLane, unknown, lapFinished, spawn, crash, finish, gameEnd, tournamentEnd;

}
