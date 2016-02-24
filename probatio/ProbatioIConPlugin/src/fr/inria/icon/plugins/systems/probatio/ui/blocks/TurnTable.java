/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui.blocks;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import fr.emn.lite.LiteColor;
import fr.emn.lite.LiteFillColor;
import fr.emn.lite.LiteShape;
import fr.emn.lite.LiteStroke;
import fr.inria.icon.plugins.systems.probatio.ui.Block;

/**
 * @author Stuf
 *
 */
public class TurnTable extends Block {

	private static int instances = 0;
	
	private static final LiteStroke stroke = new LiteStroke(1f);
	private static final LiteFillColor topFillColor = new LiteFillColor(Color.LIGHT_GRAY);
	private static final LiteColor topDrawColor = new LiteColor(Color.DARK_GRAY);
	private static final LiteFillColor pointFillColor = new LiteFillColor(Color.DARK_GRAY);
	private static final double pointSize = 3;
	
	private LiteShape top, point;
	
	public TurnTable() {
		super();
		instances++;
		name = "Turntable " + instances;
		top = new LiteShape(new Ellipse2D.Double(0, 0, WIDTH-6, HEIGHT-6), LiteShape.DRAW_FILL);
		top.setTransform(AffineTransform.getTranslateInstance(3, 3));
		LiteColor.set(top.context(), topDrawColor);
		LiteFillColor.set(top.context(), topFillColor);
		LiteStroke.set(top.context(), stroke);
		add(top);
		point = new LiteShape(new Ellipse2D.Double(0, 0, pointSize, pointSize), LiteShape.FILL);
		point.setTransform(AffineTransform.getTranslateInstance(WIDTH/2d-pointSize/2, HEIGHT/2d-pointSize/2));
		LiteFillColor.set(point.context(), pointFillColor);
		add(point);
	}

	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	@Override
	public boolean[][] getPattern() {
		return new boolean[][] {{true}};
	}

}
