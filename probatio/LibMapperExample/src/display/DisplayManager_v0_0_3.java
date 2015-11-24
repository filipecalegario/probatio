package display;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Block;
import processing.core.PApplet;

public class DisplayManager_v0_0_3 {

	private int colorIndex;
	private DisplaySlot[] slots;
	private PApplet processing;
	private int numberOfSlots;

	public DisplayManager_v0_0_3(PApplet processing, int numberOfSlots) {
		this.processing = processing;
		this.slots = new DisplaySlot[numberOfSlots];
		this.colorIndex = 0;
	}

	public void addDisplaySlot(int idBlock, int idValue, String label){
		if (!contains(idBlock, idValue) && !isFull()) {
			//int slotsQuantity = slots.size();
			int nextSlotAvailable = getNextAvailableSlot();
			float heightSection = processing.height/((float)slots.length*1.0f);
			float posY = nextSlotAvailable*heightSection;
			//System.out.println("Next slot is " + nextSlotAvailable + " => " + posY);
			DisplaySlot displaySlot = new DisplaySlot(idBlock, idValue,processing, 0.0f, posY, processing.width, heightSection, processing.color(255), colorIndex++,label);
			slots[nextSlotAvailable] = displaySlot;			
		}
	}

	public void updateValueDisplaySlot(int idBlock, int idValue, int value){
		if(contains(idBlock, idValue)){
			DisplaySlot slot = getSlot(idBlock, idValue);
			if(slot != null){
				slot.updateValue(value);
			}
		}
	}
	
	public void updateDrawDisplaySlot(){
		for (int i = 0; i < slots.length; i++) {
			if(slots[i] != null){
				slots[i].updateDisplay();
			}
		}
	}

	public void removeDisplaySlot(int idBlock, int idValue){
		if (contains(idBlock, idValue)) {
			DisplaySlot slot = getSlot(idBlock, idValue);
			if (slot != null) {
				slot.prepareToBeRemoved();
			}
			removeSlot(idBlock, idValue);
		}
	}
	
	private int getNextAvailableSlot(){
		int result = -1;
		boolean findIt = false;
		int i = 0;
		while(!findIt && i < slots.length) {
			if((slots[i] == null)){
				result = i;
				findIt = true;
			}
			i++;
		}
		return result;
	}
	
	public boolean isFull(){
		return getNextAvailableSlot() == -1;
	}
	
	private void removeSlot(int idBlock, int idValue){
		int index = getSlotIndex(idBlock, idValue);
		if(index != -1){
			slots[index].prepareToBeRemoved();
			slots[index] = null;
		}
	}
	
	private int getSlotIndex(int idBlock, int idValue){
		int result = -1;
		for (int i = 0; i < slots.length; i++) {
			if(slots[i] != null && slots[i].getIdBlock() == idBlock && slots[i].getIdValue() == idValue){
				result = i;
			}
		}
		return result;
	}
	
	private boolean contains(int idBlock, int idValue){
		boolean result = false;
		if(getSlotIndex(idBlock, idValue) != -1){
			result = true;
		}
		return result;
	}
	
	private DisplaySlot getSlot(int idBlock, int idValue){
		DisplaySlot result = null;
		int index = getSlotIndex(idBlock, idValue);
		if(index != -1){
			result = slots[index];
		}
		return result;
	}

}
