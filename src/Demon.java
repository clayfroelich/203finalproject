import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Demon extends Moving {
    private PathingStrategy strategy = new AStarPathingStrategy();

    public Demon(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
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
        Optional<EntityType> demonTarget = world.findNearest(this.position(), MinerNotFull.class);
        Optional<EntityType> demonTarget2 = world.findNearest(this.position(), OreBlob.class);
        Optional<EntityType> demonTarget3 = world.findNearest(this.position(), DarkMiner.class);
        long nextPeriod = this.getActionPeriod();

        if (demonTarget.isPresent())
        {
            Point tgtPos = demonTarget.get().position();

            if (this.moveTo(world, demonTarget.get(), scheduler))
            {
                DarkMiner darkminer = new DarkMiner(this.getId(),
                        tgtPos,imagestore.getImageList("darkminer"), 400, 100);

                world.addEntity(darkminer);
                nextPeriod += this.getActionPeriod();
                darkminer.scheduleActions(scheduler, world, imagestore);
            }
        }

        if (demonTarget2.isPresent() && !demonTarget.isPresent())
        {
            Point tgtPos = demonTarget2.get().position();

            if (this.moveTo(world, demonTarget2.get(), scheduler))
            {
                Flame flame = new Flame("flame",
                        tgtPos, imagestore.getImageList("flame"), 1500, 100);

                world.addEntity(flame);
                nextPeriod += this.getActionPeriod();
                flame.scheduleActions(scheduler, world, imagestore);
            }
        }

        if (demonTarget3.isPresent() && !demonTarget2.isPresent() && !demonTarget.isPresent())
        {
            Point tgtPos = demonTarget3.get().position();

            if (this.moveTo(world, demonTarget3.get(), scheduler))
            {
                Flame flame = new Flame("flame",
                        tgtPos, imagestore.getImageList("flame"), 1500, 100);

                world.addEntity(flame);
                nextPeriod += this.getActionPeriod();
                flame.scheduleActions(scheduler, world, imagestore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imagestore),
                nextPeriod);
    }

    protected void executeAtTarget(WorldModel world,
                                   EntityType target, EventScheduler scheduler){
        System.err.println("Demon: " + target.getClass());
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

}
