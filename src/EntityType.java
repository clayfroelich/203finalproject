import processing.core.PImage;

import java.util.List;

public abstract class EntityType {
    private String id;
    private Point position;
    private List<PImage> images;

    public EntityType(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    protected Point position() {
        return position;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    protected PImage getCurrentImage()
    {
        return (images.get(0));
    }

    protected String getId() { return id; }

    protected List<PImage> images() {
        return images;
    }
}
