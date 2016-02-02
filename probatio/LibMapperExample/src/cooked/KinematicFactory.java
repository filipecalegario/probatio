package cooked;

import model.BlockType;

public class KinematicFactory {

	public static Kinematics createKinematic(int blockId){
		Kinematics result = null;
		switch (blockId) {
		case BlockType.BELLOWS:	
			result = (Kinematics) new KinematicGeneralBlocks();
			break;
		case BlockType.CRANK:	
			result = (Kinematics) new KinematicCrank();
			break;
		case BlockType.RESTOUCH:	
			result = (Kinematics) new KinematicGeneralBlocks();
			break;
		case BlockType.BUTTON:	
			result = (Kinematics) new KinematicGeneralBlocks();
			break;
		case BlockType.TURNTABLE:	
			result = (Kinematics) new KinematicCrank();
			break;
		default:
			result = (Kinematics) new KinematicGeneralBlocks();
			break;
		}
		return result;
	}

}
