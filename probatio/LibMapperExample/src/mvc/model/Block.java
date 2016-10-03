package mvc.model;

public class Block {
	
	private String name;
	private int id;
	private int[] values;
	private String[] valuesLabels;
	private long lastTimeUpdated;
	private int dataSize;
	
	public Block(int type, int[] values, long timeAdded) {
		super();
		this.id = type;
		this.values = values;
		this.dataSize = values.length;
		this.lastTimeUpdated = timeAdded;
		this.name = BlockType.getBlockNameById(type);
	}
	
	public Block(int type, int[] values, long timeAdded, String[] labels) {
		super();
		this.id = type;
		this.values = values;
		this.dataSize = values.length;
		this.lastTimeUpdated = timeAdded;
		this.name = BlockType.getBlockNameById(type);
		this.valuesLabels = labels;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDataSize(){
		return this.dataSize;
	}
	
	public int getId() {
		return id;
	}
	
	public void setType(int type) {
		this.id = type;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public void setValues(int[] values) {
		this.values = values;
	}
	
	public long getLastTimeUpdated() {
		return lastTimeUpdated;
	}

	public void updateValues(int[] values, long millis){
		 this.lastTimeUpdated = millis;
		 this.values = values;
	}
	

	public String[] getValuesLabels() {
		return valuesLabels;
	}

	@Override
	public String toString() {
		String preffix = this.id + " => ";
		for (int i = 0; i < values.length; i++) {
			preffix = preffix + values[i] + ", ";
		}
		return preffix;
	}
	
}
