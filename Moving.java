import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Moving extends Animated{

    private PathingStrategy strategy = new SingleStepPathingStrategy();
    private List<Point> path;

    public Moving(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int imageIndex){
        super(id, position, images, actionPeriod, animationPeriod, imageIndex);
    }

    protected int repeatCount() {
        return 0;
    }

    /*
        if next to target, executeAtTarget
            return true
        else
            nextPosition(target position)

            return false
     */
    protected boolean moveTo(WorldModel world,
                          EntityType target, EventScheduler scheduler){
        if (neighbors(this.position(),target.position()))
        {
            this.executeAtTarget(world, target, scheduler);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.position());

            if (!this.position().equals(nextPos))
            {
                Optional<EntityType> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected static boolean neighbors(Point p1, Point p2) {
        return  p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;

    }

    protected void executeAtTarget(WorldModel world,
                                   EntityType target, EventScheduler scheduler){};

    abstract protected Point nextPosition(WorldModel w, Point destPos);
}
