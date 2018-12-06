import processing.core.PImage;

import java.util.List;

public class Blacksmith extends EntityType {
    private String id;
    private Point pos;
    private List<PImage> images;

    public Blacksmith(String id, Point pos, List<PImage> images) {
        super(id, pos, images);
    }




}
