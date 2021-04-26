package Model;

import transforms.Point3D;

public class Grid extends Solid {
    public Grid(int n, int m){
        for (int i = 0; i <= n; i++){
            for (int j = 0; j <= m; j++) {
                double vy = i*1./(m);
                double vz = j*1./(n);
                vertexBuffer.add(new Point3D(0, vy, vz));
            }
          //  System.out.println("vertex lenght: "+vertexBuffer.size());
        }

        //naplnění vertikálních linek
        int temp = 0;


        for (int i = 0; i <= n; i++) {
                indexBuffer.add(temp);
                temp = temp + m;
                indexBuffer.add(temp);
                temp++;
        }

        //vodorovných
        temp = -1;
        for (int i = 0; i <= m; i++) {
            indexBuffer.add(i);
            indexBuffer.add(vertexBuffer.size() - ((n)-temp));
            temp ++;
        }




    }
}
