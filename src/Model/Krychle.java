package Model;

import transforms.Point3D;

public class Krychle extends Solid {
    public Krychle(double size){

        vertexBuffer.add(new Point3D(0, 0, 0));     // 0.
        vertexBuffer.add(new Point3D(0, size, 0));    // 1.
        vertexBuffer.add(new Point3D(size, 0, 0));    //2
        vertexBuffer.add(new Point3D(size, size, 0));    //3
        vertexBuffer.add(new Point3D(0, 0, size));     //4
        vertexBuffer.add(new Point3D(0, size, size));    //5
        vertexBuffer.add(new Point3D(size, 0, size));    //6
        vertexBuffer.add(new Point3D(size, size, size));   //7


        indexBuffer.add(0); indexBuffer.add(1);
        indexBuffer.add(1); indexBuffer.add(3);
        indexBuffer.add(2); indexBuffer.add(3);
        indexBuffer.add(2); indexBuffer.add(0);

        indexBuffer.add(4); indexBuffer.add(5);
        indexBuffer.add(5); indexBuffer.add(7);
        indexBuffer.add(6); indexBuffer.add(7);
        indexBuffer.add(6); indexBuffer.add(4);

        indexBuffer.add(0); indexBuffer.add(4);
        indexBuffer.add(1); indexBuffer.add(5);
        indexBuffer.add(2); indexBuffer.add(6);
        indexBuffer.add(3); indexBuffer.add(7);


      //transformace ->  transform = new
    }
}
