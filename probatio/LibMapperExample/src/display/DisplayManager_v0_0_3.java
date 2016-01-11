package display;

import Mapper.Device.Signal;
import mapper.MapperManager;
import processing.core.PApplet;

public class DisplayManager_v0_0_3 {

	private int colorIndex;
	private DisplaySlot[] slots;
	private PApplet processing;
	private int counterXPos;

	public DisplayManager_v0_0_3(PApplet processing, int numberOfSlots) {
		this.processing = processing;
		this.slots = new DisplaySlot[numberOfSlots];
		this.colorIndex = 0;
	}

	public void addDisplaySlot(int idBlock, int idValue, String label){
		if (!contains(idBlock, idValue,0) && !isFull()) {
			addSimpleDisplaySlot(idBlock, idValue, 0, label);
			String label2 = label + " - Speed";
			addSimpleDisplaySlot(idBlock, idValue, 1, label2);
		}
	}

	public void updateValueDisplaySlot(int idBlock, int idValue, int value){
		float speedFromMainSlot = -1;
		if(contains(idBlock, idValue,0)){
			DisplaySlot slot = getSlot(idBlock, idValue, 0);
			if(slot != null){
				slot.updateValue(value);
				speedFromMainSlot = slot.getSpeed();
			}
		}
		if(contains(idBlock, idValue,1)){
			DisplaySlot slot = getSlot(idBlock, idValue, 1);
			if(slot != null){
				//System.out.println(idBlock + " => " + speedFromMainSlot);
				slot.updateValue(speedFromMainSlot);
			}
		}
	}

	public void removeDisplaySlot(int idBlock, int idValue){
		if (contains(idBlock, idValue,0)) {
			DisplaySlot slot = getSlot(idBlock, idValue,0);
			if (slot != null) {
				slot.prepareToBeRemoved();
			}
			removeSlot(idBlock, idValue, 0);
		}
		if (contains(idBlock, idValue,1)) {
			DisplaySlot slot = getSlot(idBlock, idValue,1);
			if (slot != null) {
				slot.prepareToBeRemoved();
			}
			removeSlot(idBlock, idValue, 1);
		}
	}

	public void updateDrawDisplaySlot(){
		counterXPos = (counterXPos + 1) % processing.width;
		if (counterXPos == (processing.width - 1)) {
			processing.background(255);
		}
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] != null) {
				slots[i].updateDisplay(counterXPos);
			}
		}

	}

	public void resetCounter(){
		this.counterXPos = 0;
	}

	public boolean isFull(){
		return getNextAvailableSlot() == -1;
	}

	private void addSimpleDisplaySlot(int idBlock, int idValue, int idSlot, String label) {
		int nextSlotAvailable = getNextAvailableSlot();
		float heightSection = processing.height/((float)slots.length*1.0f);
		float posY = nextSlotAvailable*heightSection;
		DisplaySlot displaySlot = new DisplaySlot(idBlock, idValue, idSlot, processing, 0.0f, posY, processing.width, heightSection, processing.color(255), colorIndex++,label);
		//		Signal sig1 = MapperManager.addOutput(label, 1, 'i', "unit", 0.0d, 255.0d);
		//		if(sig1 != null){
		//			displaySlot.setSignal(sig1);			
		//		}
		slots[nextSlotAvailable] = displaySlot;
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

	private void removeSlot(int idBlock, int idValue, int idSlot){
		int index = getSlotIndex(idBlock, idValue, idSlot);
		if(index != -1){
			slots[index].prepareToBeRemoved();
			slots[index] = null;
		}
	}

	private int getSlotIndex(int idBlock, int idValue, int idSlot){
		int result = -1;
		for (int i = 0; i < slots.length; i++) {
			if(slots[i] != null && slots[i].getIdBlock() == idBlock && slots[i].getIdValue() == idValue && slots[i].getIdSlot() == idSlot){
				result = i;
			}
		}
		return result;
	}

	private boolean contains(int idBlock, int idValue, int idSlot){
		boolean result = false;
		if(getSlotIndex(idBlock, idValue, idSlot) != -1){
			result = true;
		}
		return result;
	}

	private DisplaySlot getSlot(int idBlock, int idValue, int idSlot){
		DisplaySlot result = null;
		int index = getSlotIndex(idBlock, idValue, idSlot);
		if(index != -1){
			result = slots[index];
		}
		return result;
	}

	public void cleanScreen() {
		processing.background(255);
	}

}
