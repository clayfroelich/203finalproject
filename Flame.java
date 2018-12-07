import processing.core.PImage;

import java.util.List;

public class Flame extends Animated {
    public Flame(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod, 0);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public int repeatCount() {
        return 10;
    }
}
