import processing.core.PImage;

import java.util.List;

public abstract class Animated extends Active{
    private int animationPeriod;
    private int imageIndex;

    public Animated(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int imageIndex) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        this.imageIndex = imageIndex;
    }

    protected void nextImage() {
        imageIndex = (imageIndex + 1) % this.images().size();
    }

    protected int getAnimationPeriod() {
        return animationPeriod;
    }

    protected PImage getCurrentImage()
    {
        return (this.images().get(imageIndex));
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imagestore) {
        super.scheduleActions(scheduler, world, imagestore);
        scheduler.scheduleEvent(this,
                new Animation(this, this.repeatCount()),
                animationPeriod);
    }

    abstract protected int repeatCount();
}
