package Model;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vertexBuffer = new ArrayList<>(); //obsahuje souřadnice vrcholů
    protected List<Integer> indexBuffer = new ArrayList<>(); //slouží pro ukládání hran, dvojice souvisejících pozic ve vertexBufferu
    protected Mat4 transform = new Mat4Identity(); // inicializace jednotkové matice
    protected Color color;




    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Mat4 getTransform() {
        return transform;
    }
}
