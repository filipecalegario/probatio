package old;
//package display;
//
//import java.util.HashMap;
//import java.util.Map.Entry;
//
//import model.Block;
//import processing.core.PApplet;
//
//public class DisplayManager_v0_0_1 {
//
//	private int colorIndex;
//	private HashMap<Integer, DisplaySlot> slots;
//	private PApplet processing;
//	private boolean[] emptySlots;
//
//	public DisplayManager_v0_0_1(PApplet processing) {
//		this.processing = processing;
//		this.slots = new HashMap<Integer, DisplaySlot>();
//		this.colorIndex = 0;
//	}
//
//	public void addDisplaySlot(Block block){
//		if (!slots.containsKey(block.getId())) {
//			int slotsQuantity = slots.size();
//			float heightSection = processing.height/4.0f;
//			float posY = slotsQuantity*heightSection;
//			System.out.println("Slots quantity = " + slotsQuantity + " => " + posY);
//			slots.put(block.getId(), new DisplaySlot(block.getId(),processing, block, 0.0f, posY, processing.width, heightSection, processing.color(255), colorIndex++, ""));			
//		}
//	}
//
//	public void updateValueDisplaySlot(int id, int value){
//		if(slots.containsKey(id)){
//			DisplaySlot slot = slots.get(id);
//			if(slot != null){
//				slot.updateValue(value);
//			}
//		}
//	}
//	
//	public void updateDrawDisplaySlot(){
//		for (Entry<Integer, DisplaySlot> entry : slots.entrySet())
//		{
//		    entry.getValue().updateDisplay();
//		}
//	}
//
//	public void removeDisplaySlot(int id){
//		if (slots.containsKey(id)) {
//			DisplaySlot slot = slots.get(id);
//			if (slot != null) {
//				slot.prepareToBeRemoved();
//			}
//			slots.remove(id);
//		}
//	}
//
//}
