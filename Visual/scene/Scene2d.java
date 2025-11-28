package Visual.scene;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

import Visual.objects.Point2d;

public class Scene2d {
    private final String TITLE;
    public final int SCREEN_WIDTH, SCREEN_HEIGHT;

    private final Renderer renderer;

    // Reusable color lookup table
    private static final Map<String, Color> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("black", Color.BLACK);
        COLOR_MAP.put("blue", Color.BLUE);
        COLOR_MAP.put("cyan", Color.CYAN);
        COLOR_MAP.put("darkgray", Color.DARK_GRAY);
        COLOR_MAP.put("gray", Color.GRAY);
        COLOR_MAP.put("green", Color.GREEN);
        COLOR_MAP.put("lightgray", Color.LIGHT_GRAY);
        COLOR_MAP.put("magenta", Color.MAGENTA);
        COLOR_MAP.put("orange", Color.ORANGE);
        COLOR_MAP.put("pink", Color.PINK);
        COLOR_MAP.put("red", Color.RED);
        COLOR_MAP.put("white", Color.WHITE);
        COLOR_MAP.put("yellow", Color.YELLOW);
    }

    public Scene2d(String title) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        SCREEN_WIDTH  = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;

        TITLE = title;

        renderer = new Renderer(SCREEN_WIDTH, SCREEN_HEIGHT, TITLE);
    }

    /**
     * Draw a vertical line at X
     */
    public void drawVertLine(int x) {
        java.util.List<Point2d> pts = new java.util.ArrayList<>(SCREEN_HEIGHT);
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            pts.add(new Point2d(x, y));
        }
        renderer.addAll(pts);
    }

    /**
     * Draw a horizontal line at Y
     */
    public void drawHorizontalLine(int y) {
        java.util.List<Point2d> pts = new java.util.ArrayList<>(SCREEN_WIDTH);
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            pts.add(new Point2d(x, y));
        }
        renderer.addAll(pts);
    }

    /**
     * Draws rectangle with verticies p1 and p2
     * @param p1 Point2d (Vertex 1)
     * @param p2 Point2d (Vertex 2)
     */
    public void drawRect(Point2d p1, Point2d p2) {
        drawLine(p1, new Point2d(p2.x, p1.y));
        drawLine(new Point2d(p1.x, p2.y), p2);
        drawLine(p1, new Point2d(p1.x, p2.y));
        drawLine(p2, new Point2d(p2.x, p1.y));

    }

    /**
     * Draw a straight line between two points using Bresenham's algorithm.
     */
    public void drawLine(Point2d p1, Point2d p2) {
        int x0 = (int)Math.round(p1.x);
        int y0 = (int)Math.round(p1.y);
        int x1 = (int)Math.round(p2.x);
        int y1 = (int)Math.round(p2.y);

        java.util.List<Point2d> pts = new java.util.ArrayList<>();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            int tmp = x0; x0 = y0; y0 = tmp;
            tmp = x1; x1 = y1; y1 = tmp;
        }

        if (x0 > x1) {
            int tmp = x0; x0 = x1; x1 = tmp;
            tmp = y0; y0 = y1; y1 = tmp;
        }

        int dx = x1 - x0;
        int dy = Math.abs(y1 - y0);
        int error = dx / 2;

        int ystep = (y0 < y1) ? 1 : -1;
        int y = y0;

        for (int x = x0; x <= x1; x++) {
            if (steep) {
                pts.add(new Point2d(y, x));
            } else {
                pts.add(new Point2d(x, y));
            }

            error -= dy;
            if (error < 0) {
                y += ystep;
                error += dx;
            }
        }

        renderer.addAll(pts);
    }

    public void drawPoint(Point2d p1) {
        renderer.add(p1);
    }

    /**
     * Convert a color name or hex code to a Color object.
     */
    private Color getColor(String colorName) {
        if (colorName == null) return Color.BLACK;

        String name = colorName.trim().toLowerCase();

        // Hex (#rrggbb or 0xrrggbb)
        if (name.startsWith("#") || name.startsWith("0x")) {
            try {
                return Color.decode(name);
            } catch (NumberFormatException e) {
                return Color.BLACK;
            }
        }

        // remove spaces: "dark gray" ⇒ "darkgray"
        name = name.replaceAll("\\s+", "");
        return COLOR_MAP.getOrDefault(name, Color.BLACK);
    }

    /**
     * Sets the color for all future rendered points and text.
     */
    public void setColor(String c) {
        renderer.setColor(getColor(c));
    }

    public void drawCircle(Point2d p, int r) {
    int x0 = (int) p.x;
    int y0 = (int) p.y;

    int x = r;
    int y = 0;
    int err = 0;

    while (x >= y) {
        // Draw all 8 octants
        drawLine(new Point2d(x0 + x, y0 + y), new Point2d(x0 + x, y0 + y));
        drawLine(new Point2d(x0 + y, y0 + x), new Point2d(x0 + y, y0 + x));
        drawLine(new Point2d(x0 - y, y0 + x), new Point2d(x0 - y, y0 + x));
        drawLine(new Point2d(x0 - x, y0 + y), new Point2d(x0 - x, y0 + y));
        drawLine(new Point2d(x0 - x, y0 - y), new Point2d(x0 - x, y0 - y));
        drawLine(new Point2d(x0 - y, y0 - x), new Point2d(x0 - y, y0 - x));
        drawLine(new Point2d(x0 + y, y0 - x), new Point2d(x0 + y, y0 - x));
        drawLine(new Point2d(x0 + x, y0 - y), new Point2d(x0 + x, y0 - y));

        y += 1;
        if (err <= 0) {
            err += 2 * y + 1;
        } 
        if (err > 0) {
            x -= 1;
            err -= 2 * x + 1;
        }
    }
}

    public void drawText(String text, Point2d p) {
        renderer.drawText(text, p);
    }

    public void popText() {
        renderer.popText();
    }

    public Point2d getMouseCoordinates() {
        return new Point2d(renderer.mouseX, renderer.mouseY);
    }
}

