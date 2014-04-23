package hwo.kurjatturskat.ai.behaviours.turbo;

import hwo.kurjatturskat.ai.behaviours.spec.TurboBehaviour;
import hwo.kurjatturskat.model.World;

public class TurboOnStraightBehaviour implements TurboBehaviour {

    @Override
    public Boolean launchTurbo(World world) {
        if (!world.getTrackModel().getCurrent().isCurve()) {
            return true;
        }
        return null;
    }

}
