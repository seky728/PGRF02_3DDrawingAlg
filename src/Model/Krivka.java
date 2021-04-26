package Model;

import transforms.Cubic;
import transforms.Mat4;
import transforms.Point3D;

public class Krivka extends CubicData {

    public Krivka(Mat4 matrix) {
        vertexBuffer.add(new Point3D(1, 2, 1));
        vertexBuffer.add(new Point3D(1.5, 1, 1));
        vertexBuffer.add(new Point3D(2.5, 1, 1));
        vertexBuffer.add(new Point3D(2.0, 2, 1));
        vertexBuffer.add(new Point3D(2.0, 4, 1.5));
        vertexBuffer.add(new Point3D(2.8, 3, 1));
        vertexBuffer.add(new Point3D(3, 2, 1));

        for (int i = 0; i < vertexBuffer.size() - 3; i += 3) {
            cubics.add(new Cubic(matrix,
                    vertexBuffer.get(i),
                    vertexBuffer.get(i + 1),
                    vertexBuffer.get(i + 2),
                    vertexBuffer.get(i + 3)));
        }


    }
}
