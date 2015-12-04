package model;

public class BlockType {

	public static final int BELLOWS = 50;
	public static final int CRANK = 45;
	public static final int RESTOUCH = 49;
	public static final int BUTTON = 24;
	public static final int NONE = 0;
	public static final int TURNTABLE = 46;
	public static final int DEBUG = 99;
	

	public static String getBlockNameById(int id){
		String result = "NONE";
		switch (id) {
		case BELLOWS:	
			result = "BELLOWS";
			break;
		case CRANK:	
			result = "CRANK";
			break;
		case RESTOUCH:	
			result = "RESTOUCH";
			break;
		case BUTTON:	
			result = "BUTTON";
			break;
		case TURNTABLE:	
			result = "TURNTABLE";
			break;
		case DEBUG:	
			result = "DEBUG";
			break;
		default:
			result = "NONE";
			break;
		}
		return result;
	}
	
	public static String getBlockStringCodeById(int id){
		String result = "    ";
		switch (id) {
		case BELLOWS:	
			result = "BLWS";
			break;
		case CRANK:	
			result = "CRNK";
			break;
		case RESTOUCH:	
			result = "RSTC";
			break;
		case BUTTON:	
			result = "BUTT";
			break;
		case TURNTABLE:	
			result = "TRNT";
			break;
		case DEBUG:	
			result = "DBUG";
			break;
		default:
			result = "    ";
			break;
		}
		return result;
	}

}
