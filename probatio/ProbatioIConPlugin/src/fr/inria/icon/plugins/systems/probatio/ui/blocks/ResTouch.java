/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui.blocks;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import fr.emn.lite.LiteColor;
import fr.emn.lite.LiteFillColor;
import fr.emn.lite.LiteShape;
import fr.emn.lite.LiteStroke;
import fr.inria.icon.plugins.systems.probatio.ui.Base;
import fr.inria.icon.plugins.systems.probatio.ui.Block;

/**
 * @author Stuf
 *
 */
public class ResTouch extends Block {

	private static int instances = 0;
	
	private static final LiteStroke stroke = new LiteStroke(1f);
	private static final LiteFillColor topFillColor = new LiteFillColor(Color.LIGHT_GRAY);
	private static final LiteColor topDrawColor = new LiteColor(Color.DARK_GRAY);
	private static final LiteFillColor lineFillColor = new LiteFillColor(Color.DARK_GRAY);
	private static final double lineSize = 3;
	
	private LiteShape top, line;
	
	private boolean vertical = false;
	
	public ResTouch() {
		this(false);
	}
	
	public ResTouch(boolean vertical) {
		super();
		instances++;
		name = "Restouch " + instances;
		//TODO vertical
		this.vertical = vertical;
		top = new LiteShape(new Rectangle2D.Double(0, 0, 2*WIDTH-6, HEIGHT/3), LiteShape.DRAW_FILL);
		top.setTransform(AffineTransform.getTranslateInstance(3+(Base.v_gap - (WIDTH - Base.HOLE_WIDTH))/2d, (HEIGHT-(HEIGHT/3))/2));
		LiteColor.set(top.context(), topDrawColor);
		LiteFillColor.set(top.context(), topFillColor);
		LiteStroke.set(top.context(), stroke);
		add(top);
		line = new LiteShape(new Rectangle2D.Double(0, 0, 2*WIDTH-12, lineSize), LiteShape.FILL);
		line.setTransform(AffineTransform.getTranslateInstance(6+(Base.v_gap - (WIDTH - Base.HOLE_WIDTH))/2d, (HEIGHT-(lineSize))/2));
		LiteFillColor.set(line.context(), lineFillColor);
		add(line);
	}

	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	@Override
	public boolean[][] getPattern() {
		if (vertical) {
			return new boolean[][] {{true}, {true}};
		} else {
			return new boolean[][] {{true, true}};
		}
	}

}
