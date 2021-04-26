package Model;

import transforms.Cubic;

import java.util.ArrayList;
import java.util.List;

public class CubicData extends Solid {
    protected List<Cubic> cubics;

    public CubicData(){
        cubics = new ArrayList<>();
    }

    public List<Cubic> getCubics(){
        return cubics;
    }
}
