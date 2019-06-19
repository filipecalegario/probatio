/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.bric.awt.BrushStroke;

import fr.emn.icon.inputeditor.MyUtil;
import fr.emn.lite.LiteAntiAliasing;
import fr.emn.lite.LiteAttribute;
import fr.emn.lite.LiteColor;
import fr.emn.lite.LiteDesk;
import fr.emn.lite.LiteFillColor;
import fr.emn.lite.LiteStroke;
import fr.emn.lite.Shadow;
import fr.inria.icon.plugins.systems.probatio.ui.Base.BaseStructureException;
import fr.inria.icon.plugins.systems.probatio.ui.Base.BlockPlacingException;
import fr.inria.icon.plugins.systems.probatio.ui.Base.Side;
import fr.inria.icon.plugins.systems.probatio.ui.blocks.Connector;
import fr.inria.icon.plugins.systems.probatio.ui.blocks.ResTouch;
import fr.inria.icon.plugins.systems.probatio.ui.blocks.TurnTable;

/**
 * @author Stuf
 *
 */
public class JProbatioPanel extends JPanel implements ComponentListener {

	private static final LiteStroke brushStroke = new LiteStroke(new BrushStroke(1.5f, .5f, (float)Math.PI/2, 0));
	private static final LiteColor shadowColor = new LiteColor(0.55f, 0.55f, 0.7f, .2f);
	private static final LiteFillColor fillColor = new LiteFillColor(new Color(193, 154, 107));
	private static final LiteColor drawColor = new LiteColor(new Color(128, 70, 27));
	private static final LiteAntiAliasing antialias = new LiteAntiAliasing(true);
	
	private LiteDesk desk;
	
	private LinkedList<Base> bases = new LinkedList<Base>();
	
	public static boolean showShadows = true;

	public JProbatioPanel() {
		super();
		setBackground(Color.WHITE);
		desk = new LiteDesk();
		LiteColor.set(desk.context(), drawColor);
		LiteFillColor.set(desk.context(), fillColor);
		LiteStroke.set(desk.context(), brushStroke);
		LiteAntiAliasing.set(desk.context(), antialias);
		
		desk.setTransform(AffineTransform.getScaleInstance(2, 2));
		Base b1 = null, b2 = null;
		try {
			b1 = new Base("Base 1", 3, 3);
			b2 = new Base("Neck", 1, 4, EnumSet.of(Side.UP, Side.TOP, Side.BOTTOM, Side.RIGHT), EnumSet.of(Side.LEFT));
		} catch (BaseStructureException e2) {
			e2.printStackTrace();
		}

		if (b1 != null && b2 != null) {
		
			bases.add(b1);
			AffineTransform t = AffineTransform.getTranslateInstance(25, 40);
			b1.setTransform(t);
			desk.add(b1);
			
			bases.add(b2);
			AffineTransform t2 = AffineTransform.getTranslateInstance(25+b1.getShapeBounds().getWidth(),
												40+(b1.getShapeBounds().getHeight()/2d - b2.getShapeBounds().getHeight()/2d));
			b2.setTransform(t2);
			desk.add(b2);
			
			TurnTable tt = new TurnTable();
			try {
				b1.setBlock(tt, Side.UP, 1, 1);
			} catch (BlockPlacingException e) {
				e.printStackTrace();
			}
	
	/*		TurnTable tt2 = new TurnTable();
			try {
				b1.setBlock(tt2, Side.RIGHT, 1, 0);
			} catch (BlockPlacingException e) {
				e.printStackTrace();
			}*/
			
			ResTouch rt = new ResTouch();
			try {
				b2.setBlock(rt, Side.UP, 0, 2);
			} catch (BlockPlacingException e) {
				e.printStackTrace();
			}
			
			ResTouch rt2 = new ResTouch();
			try {
				b2.setBlock(rt2, Side.UP, 0, 0);
			} catch (BlockPlacingException e) {
				e.printStackTrace();
			}
		
		}
		addComponentListener(this);
	}
	
	private void fitToPage() {
		Rectangle2D rDesk = desk.getChildrenBounds();  // fit entire desk

		if (rDesk != null) {
			MyUtil.growRect(rDesk, 10.0);	// Grow used space
			double wComp = getSize().width;
			double hComp = getSize().height;
			double wDesk = rDesk.getWidth();
			double hDesk = rDesk.getHeight();
			double scale = Math.min(wComp / wDesk, hComp / hDesk);
			//if (scale > MAX_ZOOMFIT)
				//scale = MAX_ZOOMFIT;
			double tx = 0.5 * (wComp / scale - wDesk) - rDesk.getX();
			double ty = 0.5 * (hComp / scale - hDesk) - rDesk.getY();
			AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
			at.translate(tx, ty);
			desk.setTransform(at);
			repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		if (showShadows) {
			// Use shadow color
			LiteAttribute old = (LiteColor)shadowColor.install(g2);
			Shadow.shadowAt.translate(2, 4);
			desk.paintShadows(g2);
			Shadow.shadowAt.translate(-2, -4);
			old.install(g2);
		}
		desk.paint(g2);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		fitToPage();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		fitToPage();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}
	
}
