package uk.gov.bis.lite.notification.scheduler;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.notification.config.NotificationAppConfig;
import uk.gov.bis.lite.notification.service.NotificationService;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class NotificationScheduler implements Managed {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationScheduler.class);

  private final Scheduler scheduler;
  private final NotificationAppConfig config;
  private final NotificationService notificationService;

  @Inject
  public NotificationScheduler(Scheduler scheduler, NotificationAppConfig config,
                               NotificationService notificationService) {
    this.scheduler = scheduler;
    this.config = config;
    this.notificationService = notificationService;
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("NotificationScheduler start...");

    JobKey jobKey = JobKey.jobKey("notificationJob");
    JobDetail jobDetail = newJob(NotificationRetryJob.class)
        .withIdentity(jobKey)
        .build();

    jobDetail.getJobDataMap().put("notificationService", notificationService);
    
    CronTrigger trigger = newTrigger()
        .withIdentity(TriggerKey.triggerKey("notificationRetryJobTrigger"))
        .withSchedule(cronSchedule(config.getNotificationRetryJobCron()))
        .build();

    scheduler.scheduleJob(jobDetail, trigger);
    scheduler.start();
    scheduler.triggerJob(jobKey);
  }

  @Override
  public void stop() throws Exception {
    scheduler.shutdown(true);
  }
}
