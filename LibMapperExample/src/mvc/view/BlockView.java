package mvc.view;

import controlP5.Chart;

public class BlockView {

	private Chart chart;

	public void update(String dataSet, float value) {
		if(this.chart != null){
			chart.push(dataSet, value);
		}
	}

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

}
