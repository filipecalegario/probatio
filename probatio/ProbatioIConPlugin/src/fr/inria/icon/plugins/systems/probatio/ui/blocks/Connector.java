/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui.blocks;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
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
public class Connector extends Block {
	
	private static final LiteStroke stroke = new LiteStroke(1f);
	private static final LiteFillColor topFillColor = new LiteFillColor(Color.LIGHT_GRAY);
	private static final LiteColor topDrawColor = new LiteColor(Color.DARK_GRAY);
	private static final LiteFillColor crossFillColor = new LiteFillColor(Color.DARK_GRAY);
	private static final double lineSize = 3;
	
	private LiteShape top, cross;
	
	public Connector() {
		super();
		name = "";

		top = new LiteShape(new Rectangle2D.Double(0, 0, WIDTH-18, HEIGHT-18), LiteShape.DRAW_FILL);
		top.setTransform(AffineTransform.getTranslateInstance((WIDTH-top.getBounds().getWidth())/2d,
																(HEIGHT-top.getBounds().getHeight())/2d));
		LiteColor.set(top.context(), topDrawColor);
		LiteFillColor.set(top.context(), topFillColor);
		LiteStroke.set(top.context(), stroke);
		add(top);
		
		Area s1 = new Area(new Rectangle2D.Double(0, 0, WIDTH-16, lineSize));
		s1 = s1.createTransformedArea(AffineTransform.getRotateInstance(Math.PI / 4d, s1.getBounds2D().getCenterX(), s1.getBounds2D().getCenterY()));
		Area s2 = new Area(new Rectangle2D.Double(0, 0, WIDTH-16, lineSize));
		s1.add(s2.createTransformedArea(AffineTransform.getRotateInstance(-Math.PI / 4d, s2.getBounds2D().getCenterX(), s2.getBounds2D().getCenterY())));
		s1 = s1.createTransformedArea(AffineTransform.getTranslateInstance(-s1.getBounds2D().getX(), -s1.getBounds2D().getY()));
		System.out.println(s1.getBounds());
		cross = new LiteShape(s1, LiteShape.FILL);
		cross.setTransform(AffineTransform.getTranslateInstance((WIDTH-cross.getBounds().getWidth())/2d,
																(HEIGHT-cross.getBounds().getHeight())/2d));
		LiteFillColor.set(cross.context(), crossFillColor);
		add(cross);

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
