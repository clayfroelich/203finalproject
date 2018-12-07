final class Event
{
   public ActionType action;
   public long time;
   public EntityType entity;

   public Event(ActionType action, long time, EntityType entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
