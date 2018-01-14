/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

import com.bric.awt.BrushStroke;

import fr.emn.lite.LiteAttribute;
import fr.emn.lite.LiteColor;
import fr.emn.lite.LiteContainer;
import fr.emn.lite.LiteStroke;
import fr.emn.lite.Shadow;
//import fr.emn.utils.maths.GrahamScan;

/**
 * @author Stuf
 *
 */
public abstract class Block extends LiteContainer {
	
	private static final LiteColor shadowColor = new LiteColor(0.55f, 0.55f, 0.7f, .5f);
	
	private static final Font NAME_FONT = new Font("helvetica", Font.BOLD | Font.ITALIC, 6);
	private static final Color NAME_COLOR = new Color(0f, 0f, 0f, 0.5f);
	//private static final Stroke NAME_STROKE = new BasicStroke(.5f);
	
	public final static int WIDTH = 40, HEIGHT = 40;
	protected final static double r_w = fr.inria.icon.plugins.systems.probatio.ui.Base.r_w/2;
	protected final static double r_h = fr.inria.icon.plugins.systems.probatio.ui.Base.r_h/2;
	private final static Color BLOCK_BODY_COLOR = new Color(174, 137, 100);

	protected String name = "Block with no name";

	private final LiteStroke brushStroke = new LiteStroke(new BrushStroke(3f, .5f, (float)Math.PI/2, Math.round(Math.random()*10)));
	private Shape shape;
	protected Rectangle2D bounds = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	
	public Block() {
		this("Block with no name");
	}
	
	public Block(String name) {
		super();
		this.name = name;
		checkShape();
		LiteStroke.set(context(), brushStroke);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void doPaint(Graphics2D g) {
		checkShape();
		
		Color oldC = g.getColor();
		g.setColor(BLOCK_BODY_COLOR);
		g.fill(shape);
		g.setColor(oldC);
		g.draw(shape);
		
		paintChildren(g);
		
		Font oldF = g.getFont();
		Stroke oldS = g.getStroke();
		g.setFont(NAME_FONT);
		g.setColor(NAME_COLOR);
		//g.setStroke(NAME_STROKE);
		GlyphVector glyphs = NAME_FONT.createGlyphVector(g.getFontRenderContext(), name);
		Shape name_shape = glyphs.getOutline(2, 7);
		g.fill(name_shape);
		g.setFont(oldF);
		g.setColor(oldC);
		g.setStroke(oldS);
		
	}
	
	private void checkShape() {
		if (shape == null) {
			boolean[][] pattern = getPattern();
			double h_gap = Base.h_gap - (HEIGHT - Base.HOLE_HEIGHT)/1d;
			double v_gap = Base.v_gap - (WIDTH - Base.HOLE_WIDTH)/1d;
			int col = pattern[0].length;
			int row = pattern.length;
			double w = WIDTH * col + v_gap * (col-1);
			double h = HEIGHT * row + h_gap * (row-1);
			shape = new Area(new Rectangle2D.Double(0, 0, w, h));
			for (int i = 0; i < pattern.length; i++) {
				for (int j = 0; j < pattern[i].length; j++) {
					//TODO FIX SIZES AND GAPS
					if (!pattern[i][j]) {
							((Area)shape).subtract(new Area(new Rectangle2D.Double((WIDTH) * j + (v_gap/2) * j,
																					(HEIGHT) * i + (h_gap/2) * i,
																					WIDTH+v_gap/2d, HEIGHT+v_gap/2d)));
					}
				}
			}
			/*for (int i = 0; i < pattern.length; i++) {
				for (int j = 0; j < pattern[i].length; j++) {	
					if (pattern[i][j]) {
						if (shape == null) {
							shape = new Area(new RoundRectangle2D.Double((Block.WIDTH-5) * j + Base.h_gap * j,
																			(Block.HEIGHT-5) * i + Base.v_gap * i,
																			WIDTH, HEIGHT, r_w, r_h));
						} else {
							((Area)shape).exclusiveOr(new Area(new RoundRectangle2D.Double((Block.WIDTH-5) * j + Base.h_gap * j,
																					(Block.HEIGHT-5) * i + Base.v_gap * i,
																					WIDTH, HEIGHT, r_w, r_h)));
						}
					}
				}
			}*/
			/*FlatteningPathIterator it = new FlatteningPathIterator(shape.getPathIterator(new AffineTransform()), 0);
			Vector<Point> points = new Vector<Point>();
			float[] coords = new float[6];
			while (!it.isDone()) {
                it.currentSegment(coords);
                int x=(int)coords[0];
                int y=(int)coords[1];
                Point p = new Point(x,y);
                if (!shape.contains(p))
                	points.add(p);
                it.next();
            }
			//points = GrahamScan.computeHull(points);
			shape = new GeneralPath();
			Point p = points.get(0);
			((GeneralPath)shape).moveTo(p.x, p.y);
			for (int i = 1; i < points.size(); i++) {
				p = points.get(i);
				((GeneralPath)shape).lineTo(p.x, p.y);
			}
			((GeneralPath)shape).closePath();*/
			bounds = shape.getBounds2D();
			bounds = new Rectangle2D.Double(bounds.getX() - 5, bounds.getY() - 5, bounds.getWidth() + 10, bounds.getHeight() + 10);
		}
	}
	
	@Override
	public Shape getShadow() {
		return shape;
	}
	
	abstract public boolean[][] getPattern();

}
