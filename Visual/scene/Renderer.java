package Visual.scene;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Visual.objects.Point2d;

public class Renderer extends Frame {

    private final List<Point2d> points = new ArrayList<>();
    private final List<TextItem> texts = new ArrayList<>();
    private volatile Color color = Color.BLACK;

    public int mouseX, mouseY;

    private final Canvas canvas;

    private static final class TextItem {
        final String text;
        final Point2d pt;
        TextItem(String t, Point2d p) { text = t; pt = p; }
    }

    public Renderer(int width, int height, String title) {
        super(title);

        setUndecorated(true);
        setSize(width, height);
        setLayout(null);

        canvas = new DoubleBufferedCanvas();
        canvas.setBounds(0, 0, width, height);

        canvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        add(canvas);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);

        // Start 60 FPS repaint loop
        startLoop();
    }

    // Prevent Frame background clearing (critical)
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    // =============================================================
    //      DOUBLE BUFFER CANVAS (NO FLICKER)
    // =============================================================
    private class DoubleBufferedCanvas extends Canvas {

        private BufferedImage buffer;
        private Graphics2D bg;

        @Override
        public boolean isOpaque() {
            return true; // prevents OS clears
        }

        private void ensureBuffer() {
            int w = getWidth();
            int h = getHeight();

            if (w <= 0 || h <= 0) return;

            if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {
                buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                if (bg != null) bg.dispose();
                bg = buffer.createGraphics();

                bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                bg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
        }

        @Override
        public void update(Graphics g) {
            paint(g); // stop flicker from clears
        }

        @Override
        public void paint(Graphics g) {
            ensureBuffer();

            int w = getWidth();
            int h = getHeight();

            // Clear buffer
            bg.setColor(getBackground());
            bg.fillRect(0, 0, w, h);

            // Draw points
            List<Point2d> pSnap;
            synchronized (points) {
                pSnap = new ArrayList<>(points);
            }
            bg.setColor(color);
            for (Point2d p : pSnap) {
                bg.fillRect((int) p.x, (int) p.y, 1, 1);
            }

            // Draw text
            List<TextItem> tSnap;
            synchronized (texts) {
                tSnap = new ArrayList<>(texts);
            }
            bg.setColor(color);
            for (TextItem t : tSnap) {
                bg.drawString(t.text, (int) t.pt.x, (int) t.pt.y);
            }

            // Blit to screen
            g.drawImage(buffer, 0, 0, null);
        }
    }

    // =============================================================
    //     60 FPS REPAINT LOOP
    // =============================================================
    private void startLoop() {
        new Thread(() -> {
            while (true) {
                canvas.repaint();
                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (Exception ignore) {}
            }
        }, "RendererLoop").start();
    }

    // =============================================================
    //     THREADSAFE POINT API
    // =============================================================
    public void add(Point2d p) {
        if (p == null) return;
        synchronized (points) { points.add(p); }
    }

    public void addAll(Collection<Point2d> pts) {
        if (pts == null || pts.isEmpty()) return;
        synchronized (points) { points.addAll(pts); }
    }

    public void pop(Point2d p) {
        synchronized (points) { points.remove(p); }
    }

    public void clear() {
        synchronized (points) { points.clear(); }
    }

    // =============================================================
    //     THREADSAFE TEXT API
    // =============================================================
    public void drawText(String text, Point2d pt) {
        synchronized (texts) {
            texts.add(new TextItem(text, pt));
        }
    }

    public void popText() {
        synchronized (texts) {
            if (!texts.isEmpty()) texts.remove(texts.size() - 1);
        }
    }

    // =============================================================
    //     COLOR
    // =============================================================
    public void setColor(Color c) {
        if (c != null) color = c;
    }
}
