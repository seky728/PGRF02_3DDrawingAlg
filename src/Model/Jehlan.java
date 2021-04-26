package Model;

import transforms.Mat4RotX;
import transforms.Mat4RotY;
import transforms.Point3D;

public class Jehlan extends Solid {
    public Jehlan(double size){
        vertexBuffer.add(new Point3D(0, 0, 0));
        vertexBuffer.add(new Point3D(size, 0, 0));
        vertexBuffer.add(new Point3D(size, 0, size));
        vertexBuffer.add(new Point3D(0, 0, size));
        vertexBuffer.add(new Point3D(size / 2, 2 * size, size / 2));


        indexBuffer.add(0);        indexBuffer.add(1);
        indexBuffer.add(1);        indexBuffer.add(2);
        indexBuffer.add(2);        indexBuffer.add(3);
        indexBuffer.add(3);        indexBuffer.add(0);

        indexBuffer.add(4);        indexBuffer.add(0);
        indexBuffer.add(4);        indexBuffer.add(1);
        indexBuffer.add(4);        indexBuffer.add(2);
        indexBuffer.add(4);        indexBuffer.add(3);
    }
}
