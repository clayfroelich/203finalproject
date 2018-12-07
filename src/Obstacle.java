import processing.core.PImage;

import java.util.List;

public class Obstacle extends EntityType {
    private String id;
    private Point position;
    private List<PImage> images;

    public Obstacle(String id, Point position, List<PImage> images){
        super(id, position, images);
    }

}
