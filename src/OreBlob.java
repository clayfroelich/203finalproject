import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OreBlob extends Moving {

    private PathingStrategy strategy = new AStarPathingStrategy();
    private static final String QUAKE_KEY = "quake";

    public OreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imagestore, EventScheduler scheduler) {
        Optional<EntityType> blobTarget = world.findNearest(this.position(), Vein.class);
        Optional<EntityType> blobTarget2 = world.findNearest(this.position(), DarkMiner.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position();

            if (this.moveTo(world, blobTarget.get(), scheduler))
            {
                Active quake = new Quake(tgtPos,
                        imagestore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imagestore);
            }
        }

        if (blobTarget2.isPresent() && !blobTarget.isPresent())
        {
            Point tgtPos = blobTarget2.get().position();

            if (this.moveTo(world, blobTarget2.get(), scheduler))
            {
                Flame flame = new Flame("flame", tgtPos,
                        imagestore.getImageList("flame"), 1500, 100);

                world.addEntity(flame);
                nextPeriod += this.getActionPeriod();
                flame.scheduleActions(scheduler, world, imagestore);
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);
                return;
            }
        }
        scheduler.scheduleEvent(this,
                new Activity(this, world, imagestore),
                nextPeriod);
    }

    protected void executeAtTarget(WorldModel world,
                                EntityType target, EventScheduler scheduler){
        System.err.println("OreBlob: " + target.getClass());
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    protected Point nextPosition(WorldModel w, Point destPos){
        List<Point> points;

        points = strategy.computePath(this.position(), destPos,
                p -> w.withinBounds(p) && (!w.isOccupied(p) || w.getOccupant(p).getClass().equals(Ore.class)),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0) {
            return this.position();
        }
        return points.get(0);
    }

}
