import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner {
    private int resourceCount;

    public MinerNotFull(String id, int resourceLimit, int resourceCount, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, resourceLimit, position, images, actionPeriod, animationPeriod, 0);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityType> notFullTarget = world.findNearest(this.position(), Ore.class);

        if (!notFullTarget.isPresent() ||
                !this.moveTo(world, notFullTarget.get(), scheduler) ||
                !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    protected void executeAtTarget(WorldModel world,
                                   EntityType target, EventScheduler scheduler){
        resourceCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    public boolean transform(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= this.getResourceLimit())
        {
            MinerFull miner = new MinerFull(this.getId(), this.getResourceLimit(),
                    this.position(), this.getActionPeriod(), this.getAnimationPeriod(),
                    this.images());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
