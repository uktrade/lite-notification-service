package uk.gov.bis.lite.notification.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ie) {
      LOGGER.error("Unable to sleep", ie);
      Thread.currentThread().interrupt();
    }
  }

}
