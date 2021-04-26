package Model;

import transforms.Point3D;

public class Circle extends Solid {

    public Circle(double y, double z, double r, int n){
        for (int i = 0; i <= n ;i++){
            double alpha = i*2*Math.PI/n;
            double vy = r*Math.cos(alpha);
            double vz = r*Math.sin(alpha);
            Point3D point3D = new Point3D(0, vy, vz);
            vertexBuffer.add(point3D);

            if (i != n) {
                indexBuffer.add(i);
                indexBuffer.add(i + 1);
            } else {
                indexBuffer.add(i);
                indexBuffer.add(0);
            }
        }
    }
}
