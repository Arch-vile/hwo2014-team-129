package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.CarPositionsMsg;
import hwo.kurjatturskat.core.message.ControlMessageMsg;

public class SlowBot implements Driver {

    @Override
    public ControlMessageMsg actOncarPositions(CarPositionsMsg positions) {
        return new ControlMessageMsg(0.5d);
    }

}
