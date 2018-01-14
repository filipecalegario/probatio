/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio;

import javax.swing.JComponent;

import fr.emn.icon.utils.IConConstants;
import fr.inria.icon.plugins.systems.probatio.ui.JProbatioPanel;
import fr.lri.insitu.icon.plugins.AbstractIConPlugin;
import fr.lri.insitu.icon.plugins.device.PluginDevice;

/**
 * @author Stuf
 *
 */
public class Probatio extends AbstractIConPlugin {

	private JProbatioPanel panel;
	
	public Probatio() {
		setUI();
	}
	
	private void setUI() {
		panel = new JProbatioPanel();

	}

	@Override
	public PluginDevice[] getDevices() {
		return new PluginDevice[] {};
	}

	@Override
	public JComponent getJComponent() {
		return panel;
	}

	@Override
	public boolean hasPanel() {
		return true;
	}
		
	@Override
	public void init() {

	}
	
	@Override
	public void unload() {

	}

	@Override
	public boolean isSupported() {
		return IConConstants.VERSION_MAJOR >= 0 && IConConstants.VERSION_MINOR >= 7;
	}

}
