package old;
//package display;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map.Entry;
//
//import model.Block;
//import processing.core.PApplet;
//
//public class DisplayManager_v0_0_2 {
//
//	private int colorIndex;
//	private DisplaySlot[] slots;
//	private PApplet processing;
//
//	public DisplayManager_v0_0_2(PApplet processing) {
//		this.processing = processing;
//		this.slots = new DisplaySlot[4];
//		this.colorIndex = 0;
//	}
//
//	public void addDisplaySlot(Block block){
//		if (!contains(block.getId()) && !isFull()) {
//			//int slotsQuantity = slots.size();
//			int nextSlotAvailable = getNextAvailableSlot();
//			float heightSection = processing.height/4.0f;
//			float posY = nextSlotAvailable*heightSection;
//			System.out.println("Next slot is " + nextSlotAvailable + " => " + posY);
//			DisplaySlot displaySlot = new DisplaySlot(block.getId(),processing, block, 0.0f, posY, processing.width, heightSection, processing.color(255), colorIndex++,"");
//			slots[nextSlotAvailable] = displaySlot;			
//		}
//	}
//
//	public void updateValueDisplaySlot(int id, int value){
//		if(contains(id)){
//			DisplaySlot slot = getSlot(id);
//			if(slot != null){
//				slot.updateValue(value);
//			}
//		}
//	}
//	
//	public void updateDrawDisplaySlot(){
//		for (int i = 0; i < slots.length; i++) {
//			if(slots[i] != null){
//				slots[i].updateDisplay();
//			}
//		}
//	}
//
//	public void removeDisplaySlot(int id){
//		if (contains(id)) {
//			DisplaySlot slot = getSlot(id);
//			if (slot != null) {
//				slot.prepareToBeRemoved();
//			}
//			removeSlot(id);
//		}
//	}
//	
//	private int getNextAvailableSlot(){
//		int result = -1;
//		boolean findIt = false;
//		int i = 0;
//		while(!findIt && i < slots.length) {
//			if((slots[i] == null)){
//				result = i;
//				findIt = true;
//			}
//			i++;
//		}
//		return result;
//	}
//	
//	public boolean isFull(){
//		return getNextAvailableSlot() == -1;
//	}
//	
//	private void removeSlot(int id){
//		int index = getSlotIndex(id);
//		if(index != -1){
//			slots[index].prepareToBeRemoved();
//			slots[index] = null;
//		}
//	}
//	
//	private int getSlotIndex(int id){
//		int result = -1;
//		for (int i = 0; i < slots.length; i++) {
//			if(slots[i] != null && slots[i].getId() == id){
//				result = i;
//			}
//		}
//		return result;
//	}
//	
//	private boolean contains(int id){
//		boolean result = false;
//		if(getSlotIndex(id) != -1){
//			result = true;
//		}
//		return result;
//	}
//	
//	private DisplaySlot getSlot(int id){
//		DisplaySlot result = null;
//		int index = getSlotIndex(id);
//		if(index != -1){
//			result = slots[index];
//		}
//		return result;
//	}
//
//}
