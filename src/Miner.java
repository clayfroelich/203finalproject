import processing.core.PImage;

import java.util.List;

public abstract class Miner extends Moving{

    private PathingStrategy strategy = new AStarPathingStrategy();
    private int resourceLimit;

    public Miner(String id, int resourceLimit, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int imageIndex){
        super(id, position, images, actionPeriod, animationPeriod, imageIndex);
        this.resourceLimit = resourceLimit;
    }

    protected Point nextPosition(WorldModel w, Point destPos) {
        List<Point> points;

        points = strategy.computePath(this.position(), destPos,
                p -> w.withinBounds(p) && !w.isOccupied(p),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0) {
            return this.position();
        }
        return points.get(0);
    }

    protected int getResourceLimit() { return resourceLimit; }

    public abstract boolean transform(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore);

}
