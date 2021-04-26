package Render;

import Model.Axis;
import Model.CubicData;
import Model.Solid;

import transforms.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Renderer {

    private BufferedImage img;

    private Mat4 model, view, projection;

    public Renderer(BufferedImage img) {
        this.img = img;
        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    public void drawWireFrame(Solid solid) {

        // výsledná matice zobrazení
        Mat4 matFinal;
        if (solid instanceof Axis) {
            matFinal = view.mul(projection);
        } else {
            matFinal = model.mul(view).mul(projection);
        }

        if (solid instanceof CubicData) {
            CubicData cubicData = (CubicData) solid;
            Point3D a, b;

            for (Cubic cubic : cubicData.getCubics()) {
                a = cubic.compute(0);


                for (double d = .02; d < 1; d += .02) {
                    b = cubic.compute(d);
                    transformEdge(matFinal, a, b,
                            Color.BLUE.getRGB());
                    a = b;
                }


                b = cubic.compute(1);
                transformEdge(matFinal, a, b,
                        Color.BLUE.getRGB());
            }
            return;
        }

        // první index: 1. bod, druhý index: druhý bod úsečky
        for (int i = 0; i < solid.getIndexBuffer().size(); i += 2) {
            Point3D p1 = solid.getVertexBuffer().get(solid.getIndexBuffer().get(i));
            Point3D p2 = solid.getVertexBuffer().get(solid.getIndexBuffer().get(i + 1));

            Point3D endPoint = transformEdge(matFinal, p1, p2, Color.BLUE.getRGB());//solid.getColor(i / 2));
            if (solid instanceof Axis) {
                drawAxisName(endPoint, p2, p1, Color.RED.getRGB());//solid.getColor(i / 2));
            }

        }
    }

    private void drawAxisName(Point3D endPoint, Point3D p2, Point3D p1, int color) {
        String axis = "";


        if (p2.getX() > 0) {
            axis = "X";
        } else if (p2.getY() > 0) {
            axis = "Y";
        } else if (p2.getZ() > 0) {
            axis = "Z";
        }

        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(new Color(color));
        graphics.drawString(axis, (int) endPoint.getX() + 5, (int) endPoint.getY() + 5);
    }

    private Point3D transformEdge(Mat4 matFinal, Point3D p1, Point3D p2, int color) {
        // 1. vynásobit body maticí
        p1 = p1.mul(matFinal);
        p2 = p2.mul(matFinal);

        // 2. ořez dle w z bodů
        if (p1.getW() <= 0 && p2.getW() <= 0) return p1;

        // 3. tvorba z vektorů dehomogenizací (Point3D.dehomog())
        Optional<Vec3D> vo1 = p1.dehomog();
        Optional<Vec3D> vo2 = p2.dehomog();

        if (vo1.isEmpty() || vo2.isEmpty()) return p1;

        Vec3D v1 = vo1.get();
        Vec3D v2 = vo2.get();

        // 4. přepočet souřadnic na výšku/šířku okna (viewport)
        v1 = v1
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth() - 1),
                        0.5 * (img.getHeight() - 1),
                        1));

        v2 = v2
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(
                        0.5 * (img.getWidth() - 1),
                        0.5 * (img.getHeight() - 1),
                        1));

        // 5. výsledek vykreslit

        // když je bod mimo scěnu nevykreslí se čára
        if (v1.getX() > 0 && v1.getX() < img.getWidth() && v2.getX() > 0 && v2.getX() < img.getWidth() && v1.getY() > 0 && v1.getY() < img.getHeight() && v2.getY() > 0 && v2.getY() < img.getHeight())
            lineDDA(((int) v1.getX()), ((int) v1.getY()),
                    ((int) v2.getX()), ((int) v2.getY()),
                    color);

        return new Point3D(v2.getX(), v2.getY(), v2.getY());
    }

    private void lineDDA(int x1, int y1, int x2, int y2, int color) {
        float x, y, k, g, h;

        int dx = x2 - x1;
        int dy = y2 - y1;

        k = dy / (float) dx;


        if (Math.abs(dx) > Math.abs(dy)) {
            g = 1;
            h = k;
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        } else {
            g = 1 / k;
            h = 1;

            if (y1 > y2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        }
        x = x1;
        y = y1;

        int max = Math.max(Math.abs(dx), Math.abs(dy));
        for (int i = 1; i < max + 1; i++) {
            drawPixel(Math.round(x), Math.round(y), color);
            x += g;
            y += h;
        }
    }

    private void drawPixel(int x, int y, int color) {
        if (x < 0 || x >= img.getWidth()) return;
        if (y < 0 || y >= img.getHeight()) return;
        img.setRGB(x, y, color);
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }
}
