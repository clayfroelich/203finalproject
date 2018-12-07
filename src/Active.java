import processing.core.PImage;

import java.util.List;

public abstract class Active extends EntityType{
    private int actionPeriod;

    public Active(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imagestore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imagestore),
                this.getActionPeriod());
    }

    abstract void executeActivity(WorldModel w, ImageStore i, EventScheduler e);

    protected int getActionPeriod() { return actionPeriod; }

}
