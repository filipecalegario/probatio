package mvc.controller;

import Mapper.Device.Signal;
import controlP5.Chart;
import mvc.model.Block;
import mvc.view.BlockView;
import utils.Utils;

public class BlockController {

	private Block block;
	private BlockView blockView;
	private Signal[] signals;

	public BlockController(Block block, BlockView blockView) {
		super();
		this.block = block;
		this.signals = new Signal[block.getDataSize()];
		this.blockView = blockView;
	}

	public Signal[] getSignals() {
		return signals;
	}

	public void setSignals(Signal[] signals) {
		this.signals = signals;
	}

	public String getName() {
		return block.getName();
	}

	public void setName(String name) {
		block.setName(name);
	}

	public int getDataSize() {
		return block.getDataSize();
	}

	public int getId() {
		return block.getId();
	}

	public void setType(int type) {
		block.setType(type);
	}

	public int[] getValues() {
		return block.getValues();
	}

	public long getLastTimeUpdated() {
		return block.getLastTimeUpdated();
	}

	public void updateValues(int[] values, long millis) {
		block.updateValues(values, millis);
	}

	public String[] getValuesLabels() {
		return block.getValuesLabels();
	}

	public Chart getChart() {
		return blockView.getChart();
	}

	public int getScreenIndex() {
		return block.getScreenIndex();
	}

	public int[] getScreenGraphColor() {
		return block.getScreenGraphColor();
	}

	public void setChart(Chart myChart) {
		int modulo = 4;
		float xSize = 100;
		float ySize = 50;
		float x = ((this.getScreenIndex()%modulo) * xSize)+((this.getScreenIndex()%modulo)+1)*20;
		float linha = (this.getScreenIndex() - (this.getScreenIndex()%modulo))/modulo;
		float y = (ySize * linha) + ((linha + 1)*20);
		this.setChart(myChart, x, y, this.getScreenGraphColor());
	}

	public void setChart(Chart chart, float x, float y, int[] color) {
		Utils utils = new Utils();
		chart.setPosition(x, y);
		chart.setSize(100, 50);
		chart.setRange(0, 255);
		chart.setView(Chart.LINE);
		chart.setStrokeWeight(10f);
		chart.setColorCaptionLabel(utils.getProcessing().color(40));
		for (int i = 0; i < this.block.getValuesLabels().length; i++) {
			String dataSetName = this.block.getValuesLabels()[i];
			chart.addDataSet(dataSetName);
			chart.getDataSet(dataSetName).setColors(color[i]);
			chart.setData(dataSetName, new float[500]);
			chart.setColorForeground(color[i]);
		}
		blockView.setChart(chart);
	}

	public void updateView(){
		for (int i = 0; i < this.block.getValuesLabels().length; i++) {
			this.blockView.update(this.block.getValuesLabels()[i], this.block.getValues()[i]);
		}
	}

}
