package org.immregistries.smm.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WorkspaceStoreTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void testOpenCreatesEmptyWorkspaceOnce() throws Exception {
    WorkspaceStore store = new WorkspaceStore(temporaryFolder.getRoot());

    Workspace workspace = store.openForOwner("Pat.Smith@Example.org", "Pat Smith", "Example");
    assertEquals("pat.smith_at_example.org", workspace.getId());
    assertTrue(workspace.getDirectory().isDirectory());
    String[] files = workspace.getDirectory().list();
    assertEquals(1, files.length);
    assertEquals(WorkspaceStore.PROPERTIES_FILE_NAME, files[0]);
    assertEquals("Pat Smith", workspace.getOwnerName());

    // A second sign-in reuses the workspace and keeps the original details
    Workspace again = store.openForOwner("pat.smith@example.org", "Patricia Smith", "Other");
    assertEquals(workspace.getDirectory(), again.getDirectory());
    assertEquals("Pat Smith", again.getOwnerName());
    assertEquals(workspace.getCreated(), again.getCreated());
  }

  @Test
  public void testWorkspaceIdIsFileSystemSafe() {
    assertEquals("a_b_at_c.org", WorkspaceStore.makeWorkspaceId("a/b@c.org"));
    assertEquals("x_at_y.org", WorkspaceStore.makeWorkspaceId("..x@y.org"));
  }

  @Test
  public void testResolveBaseDirectory() {
    assertEquals(new File("/data/smm"), WorkspaceStore.resolveBaseDirectory("/data/smm"));
    File defaultDirectory = WorkspaceStore.resolveBaseDirectory("  ");
    assertEquals("workspaces", defaultDirectory.getName());
    assertEquals("smm", defaultDirectory.getParentFile().getName());
  }
}
