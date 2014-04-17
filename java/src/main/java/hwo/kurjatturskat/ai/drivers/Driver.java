package hwo.kurjatturskat.ai.drivers;

import hwo.kurjatturskat.core.message.ControlMessageMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;

public interface Driver {

    public ControlMessageMsg actOncarPositions(CarPositionsMsg positions);

}
