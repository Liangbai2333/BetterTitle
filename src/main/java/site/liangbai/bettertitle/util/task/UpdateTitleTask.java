package site.liangbai.bettertitle.util.task;

import java.util.TimerTask;

public class UpdateTitleTask extends TimerTask {
     private final Runnable task;

     public UpdateTitleTask(Runnable task) {
         this.task = task;
     }

     @Override
     public void run() {
          task.run();
     }
}
