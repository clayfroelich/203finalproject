import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner {

    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, resourceLimit, position, images, actionPeriod, animationPeriod, 0);

    }

    public void executeActivity(WorldModel w, ImageStore i, EventScheduler e){
        Optional<EntityType> fullTarget = w.findNearest(this.position(), Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveTo(w, fullTarget.get(), e))
        {
            this.transform(w, e, i);
        }
        else
        {
            e.scheduleEvent(this,
                    new Activity(this, w, i),
                    this.getActionPeriod());
        }
    }

    public boolean transform(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(this.getId(), this.getResourceLimit(), 0,
                this.position(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.images());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);

        return true;
    }
}
