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

        scheduler.scheduleEvent(this,
                new Activity(this, world, imagestore),
                nextPeriod);
    }

    protected void executeAtTarget(WorldModel world,
                                EntityType target, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    /*
    protected Point nextPosition(WorldModel world,
                              Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position().x);
        Point newPos = new Point(this.position().x + horiz,
                this.position().y);

        Optional<EntityType> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
        {
            int vert = Integer.signum(destPos.y - this.position().y);
            newPos = new Point(this.position().x, this.position().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.getClass().equals(Ore.class))))
            {
                newPos = this.position();
            }
        }

        return newPos;
    }
    */


    protected Point nextPosition(WorldModel w, Point destPos){
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

}
