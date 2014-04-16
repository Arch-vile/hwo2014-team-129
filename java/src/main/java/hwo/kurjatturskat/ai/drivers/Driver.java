package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.CarPositionsMsg;
import hwo.kurjatturskat.core.message.ControlMessageMsg;

public interface Driver {

    public ControlMessageMsg actOncarPositions(CarPositionsMsg positions);

}
