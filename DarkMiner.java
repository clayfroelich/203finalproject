import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class DarkMiner extends Moving {
    private PathingStrategy strategy = new AStarPathingStrategy();

    public DarkMiner(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod, 0);
    }

    public Point nextPosition(WorldModel w, Point destPos) {
        List<Point> points;

        points = strategy.computePath(this.position(), destPos,
                p -> w.withinBounds(p) && !(w.isOccupied(p) && !w.getOccupant(p).getClass().equals(Ore.class)),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0) {
            return this.position();
        }
        return points.get(0);
    }

    public void executeActivity(WorldModel world, ImageStore imagestore, EventScheduler scheduler) {
        Optional<EntityType> darkMinerTarget = world.findNearest(this.position(), Miner.class);
        long nextPeriod = this.getActionPeriod();

        if (darkMinerTarget.isPresent())
        {
            Point tgtPos = darkMinerTarget.get().position();

            if (this.moveTo(world, darkMinerTarget.get(), scheduler))
            {
                DarkMiner darkminer = new DarkMiner(this.getId(),
                        this.position(), this.images(), this.getActionPeriod(), this.getAnimationPeriod());

                world.addEntity(darkminer);
                nextPeriod += this.getActionPeriod();
                darkminer.scheduleActions(scheduler, world, imagestore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imagestore),
                nextPeriod);
    }

    protected void executeAtTarget(WorldModel world,
                                   EntityType target, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }
}
