package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.ControlMessageMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;

public class SlowBot implements Driver {

    @Override
    public ControlMessageMsg actOncarPositions(CarPositionsMsg positions) {
        return new ControlMessageMsg(0.5d);
    }

}
