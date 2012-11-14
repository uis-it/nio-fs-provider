package no.uis.nio.webdav;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.hamcrest.core.IsEqual;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WebdavPathTest {

	private static Properties testprops;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void initProps() throws IOException {
		File testpropsFile = new File(System.getProperty("user.home"),
				"webdav-test.xml");
		if (testpropsFile.canRead()) {
			testprops = new Properties();
			testprops.loadFromXML(new FileInputStream(testpropsFile));
		}
	}

//	@Test
	public void newFileSystemWebdav() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

		FileSystem fs = FileSystems.newFileSystem(uri, null);

		assertThat(fs, is(notNullValue()));
	}

//	@Test
	public void newFileSystemWebdavs() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdavs", "lportal-test.uis.no", -1, null);

		FileSystem fs = FileSystems.newFileSystem(uri, null);

		assertThat(fs, is(notNullValue()));
	}

//	@Test
	public void getURI() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

		Path path = Paths.get(uri);

		assertThat(path, is(notNullValue()));
	}

//	@Test
	public void getNewURI() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1, null);

		Path path = Paths.get(uri);

		assertThat(path, is(notNullValue()));
	}

//	@Test
	public void getCreateChildPath() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1,
				"/webdav/test2");
		Path path = Paths.get(uri);
		Path newPath = Files.createDirectories(path);
		assertThat(newPath, is(notNullValue()));
	}

//	@Test
	public void copyFiles() throws Exception {
		Assume.assumeNotNull(testprops);
		File src = File.createTempFile("webdavtest", ".txt");
		FileWriter fw = new FileWriter(src);
		fw.append("test test");
		fw.close();

		URI uriTo = createTestUri("webdav", "lportal-test.uis.no", -1,
				"/webdav/test2/file.txt");
		Path pathTo = Paths.get(uriTo);
		Files.copy(src.toPath(), pathTo, StandardCopyOption.REPLACE_EXISTING);
	}

//	@Test
	public void deleteFile() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1,
				"/webdav/test2/file.txt");
		Path path = Paths.get(uri);
		Files.delete(path);
	}

//	@Test
	public void deleteWrongHost() throws Exception {
		Assume.assumeNotNull(testprops);
		URI uri = createTestUri("webdav", "non-existing-host", -1, "/");
		exception.expect(is(IOException.class));
		Path path = Paths.get(uri);
		Files.delete(path);
	}

//	@Test
	public void testCatalogCreator() throws Exception {
		Assume.assumeNotNull(testprops);
		CatalogCreatorMock cc = new CatalogCreatorMock();
		URI uri = createTestUri("webdav", "lportal-test.uis.no", -1,
				"/webdav/test2/2012/emne/B/");
		Path outPath = Paths.get(uri);
		cc.createCatalog(outPath);
	}

	private URI createTestUri(String scheme, String host, int port, String path)
			throws URISyntaxException {
		String username = testprops.getProperty("webdav.user");
		String password = testprops.getProperty("webdav.password");

		return new URI(scheme, username + ':' + password, host, port, path,
				null, null);
	}
}
