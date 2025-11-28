# EasyVisuals

A Java library that uses AWT to make drawing easy.

---

<details>
<summary>High-Level Drawing</summary>

## Scene2d

This class is the main way to use the library.
It is intended to be easy to use and handles all low-level operations.

### Usage

**Instantiation:**

```java
import Visual.scene.Scene2d;

// Inside main
// constructor: Scene2d(String title)
Scene2d example = new Scene2d("Example Scene");
```

**Draw Methods:**

```java
import Visual.objects.Point2d;

// With previous code above in main

// void drawLine(Point2d p1, Point2d p2)
int maxX = example.SCREEN_WIDTH;
int maxY = example.SCREEN_HEIGHT;
example.drawLine(new Point2d(0, 0), new Point2d(maxX, maxY));

// void drawHorizontalLine(int y)
example.drawHorizontalLine(20);

// void drawVertLine(int x)
example.drawVertLine(20);

// void drawCircle(Point2d center, int radius)
example.drawCircle(new Point2d(500, 500), 125);

// void drawRect(Point2d p1, Point2d p2)
example.drawRect(new Point2d(0, 0), new Point2d(maxX, maxY));

// void drawPoint(Point2d p)
example.drawPoint(new Point2d(250, 250));

// void drawText(String text, Point2d bottom_left_corner)
example.drawText("Text to draw", new Point2d(25, 25));

// void popText()
example.popText(); // Removes last drawn text from rendering queue
```

**Color Methods:**

```java
import Visual.scene.Scene2d;

// Scene2dInstance.setColor(String color)
// color can be a hex code starting with # or 0x, or a name of a color
example.setColor("red");
// or
example.setColor("#ff0000");
example.setColor("0xff0000");
```

</details>

<details>
<summary>Point2d/3d Objects</summary>

Both are ways to cleanly store 2- and 3-value points/vectors.

**Instantiation:**

```java
new Point2d(x, y);
// or
new Point3d(x, y, z);
```

**Helper Methods:**

```java
// Point2d getMouseCoordinates()
Point2d mousePos = example.getMouseCoordinates();
```

</details>

<details>
<summary>Renderer (Low-Level Drawing)</summary>

## Constructor

### `Renderer(int width, int height, String title)`

* Creates the window.
* Sets size, title, installs the custom Canvas.
* Starts the 60 FPS loop.
* Tracks mouse.
* Disables window decorations.

## Frame Overrides

### `update(Graphics g)`

* Bypasses the default clearing.
* Calls `paint` directly so nothing flickers.

## DoubleBufferedCanvas (inner class)

Custom Canvas used for double-buffered drawing.

### `isOpaque()`

* Always returns `true`.
* Prevents OS from clearing the background.

### `ensureBuffer()`

* Ensures the off-screen buffer (`BufferedImage`) exists and matches the canvas size.
* Recreates the buffer when the window is resized.

### `update(Graphics g)`

* Prevents clearing.
* Calls `paint` directly.

### `paint(Graphics g)`

Main render function:

* Clears the buffer.
* Draws all points.
* Draws all text.
* Copies the buffer to the screen.

## Render Loop

### `startLoop()`

* Starts a background thread that repaints the screen at ~60 FPS.

## Point API (thread-safe)

### `add(Point2d p)`

* Adds a single point.

### `addAll(Collection<Point2d> pts)`

* Adds multiple points at once.

### `pop(Point2d p)`

* Removes a specific point.

### `clear()`

* Removes all points.

## Text API (thread-safe)

### `drawText(String text, Point2d pt)`

* Adds text at a given position.

### `popText()`

* Removes the most recently added text item.

## Color

### `setColor(Color c)`

* Sets the current drawing color (used for all points and text).

## Mouse

* `mouseX` / `mouseY`
  Automatically updated to track the mouse position within the window.

</details>
