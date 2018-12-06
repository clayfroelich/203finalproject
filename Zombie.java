import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Zombie extends Moving {
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Zombie(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
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
        Optional<EntityType> zombieTarget = world.findNearest(this.position(), Miner.class);
        long nextPeriod = this.getActionPeriod();

        if (zombieTarget.isPresent())
        {
            Point tgtPos = zombieTarget.get().position();

            if (this.moveTo(world, zombieTarget.get(), scheduler))
            {
                Zombie zombie = new Zombie(this.getId(),
                        this.position(), this.images(), this.getActionPeriod(), this.getAnimationPeriod());

                world.addEntity(zombie);
                nextPeriod += this.getActionPeriod();
                zombie.scheduleActions(scheduler, world, imagestore);
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
