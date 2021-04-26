package Model;

import transforms.Point3D;

public class Axis extends Solid {

    public Axis() {
        vertexBuffer.add(new Point3D(0,0,0));
        vertexBuffer.add(new Point3D(1,0,0));
        vertexBuffer.add(new Point3D(0,1,0));
        vertexBuffer.add(new Point3D(0,0,1));

        indexBuffer.add(0);
        indexBuffer.add(1);

        indexBuffer.add(0);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(3);
    }
}
