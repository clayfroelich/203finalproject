import java.awt.*;

public class Animation implements ActionType {
    private Animated entity;
    private int repeatCount;

    public Animation(Animated entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    new Animation(entity, Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}

