package org.immregistries.smm.mover;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class FolderScanner extends Thread {
  private static final int SCAN_PAUSE = 15 * 1000;
  private List<File> foldersToScan = null;
  private String scanningStatus = "";
  private boolean scanning = false;

  public List<File> getFoldersToScan() {
    return foldersToScan;
  }

  public String getScanningStatus() {
    return scanningStatus;
  }

  public boolean isScanning() {
    return scanning;
  }

  public FolderScanner(List<File> foldersToScan, ConnectionManager managerServlet) {
    this.foldersToScan = foldersToScan;
  }

  public void setFoldersToScan(List<File> foldersToScan) {
    this.foldersToScan = foldersToScan;
  }

  @Override
  public void run() {
    // while (keepRunning)
    {
      try {
        log("Scan started");
        for (File folderToScan : foldersToScan) {
          search(folderToScan, true);
        }
        log("Scan completed");
      } catch (Throwable t) {
        t.printStackTrace();
      }
      synchronized (scanningStatus) {
        try {
          scanningStatus.wait(SCAN_PAUSE);
        } catch (InterruptedException ie) {
          // continue
        }
      }

    }
  }

  private void log(String status) {
    scanningStatus = status;
    System.out.println("SMM Folder Scan: " + scanningStatus);
  }

  private void search(File folder, boolean starting) {
    if (!starting) {
      for (File startingFolder : foldersToScan) {
        if (folder.equals(startingFolder)) {
          // Folder already in scanning tree, skipping
          return;
        }
      }
    }
    if (ConnectionManager.isRegisteredFolder(folder)) {
      // already found, don't look here again
      return;
    }
    if (folder.getName().equals("_gsdata_")) {
      // this folder should not be scanned
      return;
    }
    if (folder.getName().equals("_global")) {
      // register the folder
      ConnectionManager.getGlobalFolders().add(folder);
    }
    log("Looking in folder " + folder.getAbsolutePath());
    File configFile = new File(folder, "smm.config.txt");
    if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
      // This is a data directory!
      log("Found SMM configuration file in this folder: " + folder.getAbsolutePath());
      ConnectionManager.registerFolder(folder);
    } else {
      // not a data directory, but perhaps children are
      File[] subFolders = folder.listFiles(new FileFilter() {

        public boolean accept(File file) {
          return file.isDirectory();
        }
      });
      if (subFolders != null) {
        for (File subFolder : subFolders) {
          search(subFolder, false);
        }
      }
    }
  }
}
