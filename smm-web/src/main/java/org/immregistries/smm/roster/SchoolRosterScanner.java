package org.immregistries.smm.roster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator.MessageProfile;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.web.auth.SmmUser;

public class SchoolRosterScanner extends Thread {

  private static final long SCAN_INTERVAL_MS = 60_000L;
  private static final Map<String, SchoolRosterScanner> SCANNERS = new ConcurrentHashMap<>();

  private final SmmUser user;
  private final AtomicBoolean enabled = new AtomicBoolean(false);
  private final AtomicBoolean stopRequested = new AtomicBoolean(false);

  public static SchoolRosterScanner getOrCreate(SmmUser user) {
    if (user == null) {
      return null;
    }

    return SCANNERS.computeIfAbsent(
      user.getUsername(),
      key -> {
        SchoolRosterScanner scanner = new SchoolRosterScanner(user);
        scanner.setName("school-roster-scanner-" + user.getUsername());
        scanner.setDaemon(true);
        return scanner;
      });
  }

  public static void remove(SmmUser user) {
    if (user == null) {
      return;
    }

    SchoolRosterScanner scanner = SCANNERS.remove(user.getUsername());
    if (scanner != null) {
      scanner.shutdown();
    }
  }

  public static void stopAllScannersForUser(SmmUser user) {
    if (user == null) {
      return;
    }
    remove(user);
  }

  private SchoolRosterScanner(SmmUser user) {
    this.user = user;
  }

  public SmmUser getUser() {
    return user;
  }

  public boolean isEnabled() {
    return enabled.get();
  }

  public void setEnabled(boolean enabled) {
    this.enabled.set(enabled);
    if (enabled) {
      stopRequested.set(false);
      if (!isAlive()) {
        start();
      }
      return;
    }

    shutdown();
    SCANNERS.remove(user.getUsername());
  }

  public void shutdown() {
    stopRequested.set(true);
    enabled.set(false);
    interrupt();
  }

  @Override
  public void run() {
    while (!stopRequested.get()) {
      try {
        if (enabled.get()) {
          scanOnce();
        }
        Thread.sleep(SCAN_INTERVAL_MS);
      } catch (InterruptedException e) {
        if (stopRequested.get()) {
          break;
        }
        Thread.currentThread().interrupt();
      }
    }
  }

  protected void scanOnce() {
    if (user == null || user.getWorkspace() == null) {
      return;
    }

    try {
      Path rosterDir = SchoolRosterFileHandler.createSchoolRosterDirectory(user.getWorkspace());
      if (rosterDir == null || !Files.isDirectory(rosterDir)) {
        return;
      }

      try (Stream<Path> files = Files.list(rosterDir)) {
        files
          .filter(Files::isRegularFile)
          .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".csv"))
          .sorted()
          .forEach(path -> {
            SchoolRosterFileHandler fileHandler =
              new SchoolRosterFileHandler(
                user.getUsername(),
                user.getSchoolRosterScanningConnector(),
                user.getWorkspace(),
                MessageProfile.PROPOSED_WITH_GRADE);
            
            try {
              LoadDetails details = fileHandler.handle(Files.newInputStream(path), path.getFileName().toString());
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            
            try {
              Files.delete(path);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}