package hwo.kurjatturskat.core.message.carpositions;

public class CarPositionsMsg {

    public Car data[];
    public final int gameTick;

    CarPositionsMsg(Car data[], int gameTick) {
        this.data = data;
        this.gameTick = gameTick;
    }
}
