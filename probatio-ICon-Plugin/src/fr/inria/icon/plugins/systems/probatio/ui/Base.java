/**
 * 
 */
package fr.inria.icon.plugins.systems.probatio.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.EnumSet;
import java.util.HashMap;

import fr.emn.lite.LiteAttribute;
import fr.emn.lite.LiteColor;
import fr.emn.lite.LiteContainer;
import fr.emn.lite.Shadow;

/**
 * @author Stuf
 *
 */
public class Base extends LiteContainer {

	public enum Side {
		UP,
		TOP,
		RIGHT,
		BOTTOM,
		LEFT
	}
	
	public class BaseStructureException extends Exception {
		
		private String cause = "";
		private Base b;
		
		public BaseStructureException(Base b, String cause) {
			this.cause = cause;
			this.b = b;
		}
		
		@Override
		public String getMessage() {
			return "Base '" + b.getName() + "' cannot be created: " + cause;
		}
	}
	
	public class BlockPlacingException extends Exception {
		
		private String cause = "";
		private Base b;
		private Block bl;
		int r, c;
		
		public BlockPlacingException(Base b, Block bl, int r, int c, String cause) {
			this.cause = cause;
			this.b = b;
			this.bl = bl;
			this.r = r;
			this.c = c;
		}
		
		@Override
		public String getMessage() {
			return "Cannot place the block '" + bl.getName() + "' on base '" + b.getName() + "' [r:" + r + ";c:" + c + "]: " + cause;
		}
	}
	
	private static final Font NAME_FONT = new Font("helvetica", Font.BOLD | Font.ITALIC, 7);
	private static final Color NAME_COLOR = new Color(0f, 0f, 0f, 0.5f);
	//private static final Stroke NAME_STROKE = new BasicStroke(.25f);
	private static final Color CONNECTORS_COLOR = new Color(255,215,0);
	
	public final static int HOLE_WIDTH = Block.WIDTH-4, HOLE_HEIGHT = Block.HEIGHT-4;
	
	protected final static int h_gap = 10;
	public static final int v_gap = 10;
	protected static final int side_base_gap = 0;
	protected final static double r_w = 10, r_h = 10;
	private final static Color EMPTY_BLOCK_COLOR = new Color(0f, 0f ,0f, .10f);
	
	private int row = 1, col = 1;

	private String name = "Base with no name";
	
	private Shape shape = null;
	private Shape shadowShape = null;
	private Rectangle2D bounds = new Rectangle2D.Double();
	
	private HashMap<Side, Block[][]> blocks;
	private EnumSet<Side> sides, connectors = null;

	public Base(int row, int col) throws BaseStructureException {
		this("Base with no name", row, col, EnumSet.of(Side.UP, Side.TOP, Side.RIGHT, Side.BOTTOM, Side.LEFT), null);
	}
	
	public Base(String name, int row, int col) throws BaseStructureException {
		this(name, row, col, EnumSet.of(Side.UP, Side.TOP, Side.RIGHT, Side.BOTTOM, Side.LEFT), null);
	}
	
	public Base(String name, int row, int col, EnumSet<Side> sides, EnumSet<Side> connectors) throws BaseStructureException {
		super();
		this.name = name;
		this.row = row;
		this.col = col;
		if (sides != null) {
			this.sides = sides;
		} else {
			this.sides = EnumSet.of(Side.UP, Side.TOP, Side.RIGHT, Side.BOTTOM, Side.LEFT);
		}
		this.connectors = connectors;
		//Should always contain UP...
		if (!this.sides.contains(Side.UP)) {
			throw new BaseStructureException(this, "A base must always contain a UP side");
		}
		//check sides and connectors
		if (connectors != null) {
			if (connectors.contains(Side.UP)) {
				throw new BaseStructureException(this, "UP side cannot be defined as a connector");		
			}
			for (Side s : connectors) {
				if (this.sides.contains(s)) {
					throw new BaseStructureException(this, "A base connector cannot be defined as a side (" + s.name() + ")");
				}
			}
		}
		blocks = new HashMap<Side, Block[][]>();
		Block[][] s_blocks;
		if (sides.contains(Side.UP)) {
			s_blocks = new Block[row][col];
			blocks.put(Side.UP, s_blocks);
		}
		if (sides.contains(Side.TOP)) {
			s_blocks = new Block[1][col];
			blocks.put(Side.TOP, s_blocks);
		}
		if (sides.contains(Side.RIGHT)) {
			s_blocks = new Block[row][1];
			blocks.put(Side.RIGHT, s_blocks);
		}
		if (sides.contains(Side.BOTTOM)) {
			s_blocks = new Block[1][col];
			blocks.put(Side.BOTTOM, s_blocks);
		}
		if (sides.contains(Side.LEFT)) {
			s_blocks = new Block[row][1];
			blocks.put(Side.LEFT, s_blocks);
		}
		checkShape();
	}
	
	public String getName() {
		return name;
	}
	
	public void setBlock(Block b, Side side, int r, int c) throws BlockPlacingException {
		//TODO account for blocks patterns...
		//TODO account for occupied blocks on the contiguous sides!!!
		Block[][] s_blocks = blocks.get(side);
		if (s_blocks == null)
			throw new BlockPlacingException(this, b, r, c, "undefined side (" + side.name() + ")");
		if (r < 0 || r >= row || c < 0 || c >= col)
			throw new BlockPlacingException(this, b, r, c, "undefined row or column");
		if ((side.equals(Side.TOP) || side.equals(Side.BOTTOM)) && r >= 1)
			throw new BlockPlacingException(this, b, r, c, "undefined row for this side");
		if ((side.equals(Side.LEFT) || side.equals(Side.RIGHT)) && c >= 1)
			throw new BlockPlacingException(this, b, r, c, "undefined column for this side");
		if (s_blocks[r][c] != null) {
			remove(s_blocks[r][c]);
		}
		s_blocks[r][c] = b;
		if (b != null) {
			int v_gap_diff = Block.WIDTH - HOLE_WIDTH;
			int h_gap_diff = Block.HEIGHT - HOLE_HEIGHT;
			double x_b = (Block.WIDTH) * c + (v_gap-v_gap_diff) * (c) + (v_gap - v_gap_diff/2d);
			double y_b = (Block.HEIGHT) * r + (h_gap-h_gap_diff) * (r) + (v_gap - h_gap_diff/2d);
			if (side.equals(Side.UP)) {
				x_b += x_base;
				y_b += y_base;
			} else if (side.equals(Side.TOP)) {
				x_b += x_base;
			} else if (side.equals(Side.BOTTOM)) {
				x_b += x_base;
				y_b += y_base + h_base + side_base_gap;
			} else if (side.equals(Side.LEFT)) {
				y_b += y_base;
			} else if (side.equals(Side.RIGHT)) {
				x_b += x_base + w_base + side_base_gap;
				y_b += y_base;
			}
			add(b);
			AffineTransform t = b.getTransform();
			t.translate(x_b, y_b);
			b.setTransform(t);
		}
		repaint();
	}
	
	int w_base = HOLE_WIDTH * col + h_gap * (col + 1);
	int h_base = HOLE_HEIGHT * row + v_gap * (row + 1);
	int w_side = HOLE_HEIGHT + v_gap * 2;
	int h_side = HOLE_WIDTH + h_gap * 2;		
	int x_base = HOLE_HEIGHT + v_gap * 2 + side_base_gap;
	int y_base = HOLE_WIDTH + h_gap * 2 + side_base_gap;
	
	private void checkShape() {
		if (shape != null)
			return;
		w_base = HOLE_WIDTH * col + h_gap * (col + 1);
		h_base = HOLE_HEIGHT * row + v_gap * (row + 1);
		w_side = HOLE_HEIGHT + v_gap * 2;
		h_side = HOLE_WIDTH + h_gap * 2;
		if (sides.contains(Side.LEFT)) {
			x_base = HOLE_WIDTH + v_gap * 2 + side_base_gap;
		} else if (connectors != null && connectors.contains(Side.LEFT)) {
			x_base = HOLE_WIDTH;			
		} else {
			x_base = 0;
		}
		if (sides.contains(Side.TOP)) {
			y_base = HOLE_HEIGHT + h_gap * 2 + side_base_gap;
		} else if (connectors != null && connectors.contains(Side.TOP)) {
			y_base = HOLE_HEIGHT;	
		} else {
			y_base = 0;
		}
		
		shadowShape = new RoundRectangle2D.Double(x_base, y_base, w_base, h_base, r_w, r_h);
		Area s_area = new Area(shadowShape);
		Area s;
		//Top side or connector
		if (sides.contains(Side.TOP)) {
			s = new Area(new RoundRectangle2D.Double(x_base, 0, w_base, h_side, r_w, r_h));
			s_area.add(s);
		} else if (connectors != null && connectors.contains(Side.TOP)) {
			s = new Area(new RoundRectangle2D.Double(x_base + w_base / 2d - Block.WIDTH / 2d, 0, Block.WIDTH, Block.HEIGHT, r_w, r_h));
			s_area.add(s);			
		}
		//Right side
		if (sides.contains(Side.RIGHT)) {
			s = new Area(new RoundRectangle2D.Double(x_base + w_base + side_base_gap, y_base, w_side, h_base, r_w, r_h));
			s_area.add(s);
		} else if (connectors != null && connectors.contains(Side.RIGHT)) {
			s = new Area(new RoundRectangle2D.Double(x_base + w_base, y_base + h_base / 2d - Block.HEIGHT / 2d, Block.WIDTH, Block.HEIGHT, r_w, r_h));
			s_area.add(s);			
		}
		//Bottom side
		if (sides.contains(Side.BOTTOM)) {
			s = new Area(new RoundRectangle2D.Double(x_base, y_base + h_base + side_base_gap, w_base, h_side, r_w, r_h));
			s_area.add(s);
		} else if (connectors != null && connectors.contains(Side.BOTTOM)) {
			s = new Area(new RoundRectangle2D.Double(x_base + w_base / 2d - Block.WIDTH / 2d, y_base + h_base, Block.WIDTH, Block.HEIGHT, r_w, r_h));
			s_area.add(s);			
		}
		//Left side
		if (sides.contains(Side.LEFT)) {
			s = new Area(new RoundRectangle2D.Double(0, y_base, w_side, h_base, r_w, r_h));
			s_area.add(s);
		} else if (connectors != null && connectors.contains(Side.LEFT)) {
			s = new Area(new RoundRectangle2D.Double(0, y_base + h_base / 2d - Block.HEIGHT / 2d, Block.WIDTH, Block.HEIGHT, r_w, r_h));
			s_area.add(s);			
		}
		
		shadowShape = s_area;
		
		Area b_area = new Area(shadowShape);
		
		//Holes in Up side
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Area b = new Area(new RoundRectangle2D.Double(x_base + HOLE_WIDTH * j + v_gap * (j + 1),
						y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
						HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h));
				b_area.subtract(b);
			}
		}
		Area b;
		if (sides.contains(Side.TOP) || sides.contains(Side.BOTTOM)) {
			for (int i = 0; i < col; i++) {
				//Holes in Top side
				if (sides.contains(Side.TOP)) {
					b = new Area(new RoundRectangle2D.Double(x_base + HOLE_WIDTH * i + v_gap * (i + 1),
							h_gap, 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h));
					b_area.subtract(b);
				}
				//Holes in Bottom side
				if (sides.contains(Side.BOTTOM)) {
					b = new Area(new RoundRectangle2D.Double(x_base + HOLE_WIDTH * i + v_gap * (i + 1),
							y_base + h_base + side_base_gap + h_gap, 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h));
					b_area.subtract(b);
				}
			}
		}
		if (sides.contains(Side.TOP) || sides.contains(Side.BOTTOM)) {
			for (int i = 0; i < row; i++) {
				//Holes in Left side
				if (sides.contains(Side.LEFT)) {
					b = new Area(new RoundRectangle2D.Double(v_gap,
							y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h));
					b_area.subtract(b);
				}
				//Holes in Right side
				if (sides.contains(Side.RIGHT)) {
					b = new Area(new RoundRectangle2D.Double(x_base + w_base + side_base_gap + v_gap,
							y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h));
					b_area.subtract(b);
				}
			}
		}

		
		shape = b_area;
		bounds = b_area.getBounds2D();
		bounds = new Rectangle2D.Double(bounds.getX() - 5, bounds.getY() - 5, bounds.getWidth() + 10, bounds.getHeight() + 10);
	}
	
	@Override
	public void doPaint(Graphics2D g) {

		checkShape();
		
		Color oldC = g.getColor();
		g.setColor(g.getBackground());
		g.fill(shape);
		g.setColor(oldC);
		g.draw(shape);

		g.setColor(EMPTY_BLOCK_COLOR);
		
		//Draw overlays on holes
		Block[][] s_blocks = blocks.get(Side.UP);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				//TODO account for blocks patterns...
				if (s_blocks[i][j] == null) {
					Shape r = new RoundRectangle2D.Double(x_base + HOLE_WIDTH * j + v_gap * (j + 1),
							y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h);
					g.fill(r);
				}
			}
		}
		s_blocks = blocks.get(Side.TOP);
		if (s_blocks != null) {
			for (int i = 0; i < col; i++) {
				//TODO account for blocks patterns...
				if (s_blocks[0][i] == null) {
					Shape r = new RoundRectangle2D.Double(x_base + HOLE_WIDTH * i + v_gap * (i + 1),
							h_gap, 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h);
					g.fill(r);
				}
			}
		}
		s_blocks = blocks.get(Side.BOTTOM);
		if (s_blocks != null) {
			for (int i = 0; i < col; i++) {
				//TODO account for blocks patterns...
				if (s_blocks[0][i] == null) {
					Shape r = new RoundRectangle2D.Double(x_base + HOLE_WIDTH * i + v_gap * (i + 1),
							y_base + h_base + side_base_gap + h_gap, 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h);
					g.fill(r);
				}
			}
		}
		s_blocks = blocks.get(Side.LEFT);
		if (s_blocks != null) {
			for (int i = 0; i < row; i++) {
				//TODO account for blocks patterns...
				if (s_blocks[i][0] == null) {
					Shape r = new RoundRectangle2D.Double(v_gap,
							y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h);
					g.fill(r);
				}
			}
		}
		s_blocks = blocks.get(Side.RIGHT);
		if (s_blocks != null) {
			for (int i = 0; i < row; i++) {
				//TODO account for blocks patterns...
				if (s_blocks[i][0] == null) {
					Shape r = new RoundRectangle2D.Double(x_base + w_base + side_base_gap + v_gap,
							y_base + HOLE_HEIGHT * i + h_gap * (i + 1), 
							HOLE_WIDTH, HOLE_HEIGHT, Block.r_w, Block.r_h);
					g.fill(r);
				}
			}
		}
		
		//Draw points on connectors
		if (connectors != null) {
			g.setColor(CONNECTORS_COLOR);
			if (connectors.contains(Side.TOP)) {
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), (int)Math.round(Block.HEIGHT / 4d - 1.5), 3, 3);	
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), (int)Math.round(Block.HEIGHT / 4d - 1.5 + 6), 3, 3);	
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), (int)Math.round(Block.HEIGHT / 4d - 1.5 + 12), 3, 3);		
			}
			if (connectors.contains(Side.BOTTOM)) {
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), y_base + h_base + (int)Math.round(3*Block.HEIGHT / 4d - 1.5), 3, 3);	
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), y_base + h_base + (int)Math.round(3*Block.HEIGHT / 4d - 1.5 - 6), 3, 3);	
				g.fillOval((int)Math.round(x_base + w_base / 2d - 1.5), y_base + h_base + (int)Math.round(3*Block.HEIGHT / 4d - 1.5 - 12), 3, 3);		
			}
			if (connectors.contains(Side.LEFT)) {
				g.fillOval((int)Math.round(Block.WIDTH / 4d - 1.5), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);
				g.fillOval((int)Math.round(Block.WIDTH / 4d - 1.5 + 6), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);
				g.fillOval((int)Math.round(Block.WIDTH / 4d - 1.5 + 12), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);
			}
			if (connectors.contains(Side.RIGHT)) {
				g.fillOval(x_base + w_base + (int)Math.round(3*Block.WIDTH / 4d - 1.5), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);
				g.fillOval(x_base + w_base + (int)Math.round(3*Block.WIDTH / 4d - 1.5 - 6), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);
				g.fillOval(x_base + w_base + (int)Math.round(3*Block.WIDTH / 4d - 1.5 - 12), (int)Math.round(y_base + h_base / 2d - 1.5), 3, 3);		
			}
		}
		
		Font oldF = g.getFont();
		Stroke oldS = g.getStroke();
		g.setFont(NAME_FONT);
		g.setColor(NAME_COLOR);
		//g.setStroke(NAME_STROKE);
		GlyphVector glyphs = NAME_FONT.createGlyphVector(g.getFontRenderContext(), "|" + name + "|");
		Shape name_shape = glyphs.getOutline(x_base + 2, y_base + 7);
		g.fill(name_shape);
		g.setFont(oldF);
		g.setColor(oldC);
		g.setStroke(oldS);
		
		paintChildren(g);
	}
	
	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	public Rectangle2D getShapeBounds() {
		return shape.getBounds2D();
	}
	
	@Override
	public Shape getShadow() {
		return shape;
	}
	
}
