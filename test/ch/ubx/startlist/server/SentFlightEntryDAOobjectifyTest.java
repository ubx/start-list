package ch.ubx.startlist.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ubx.startlist.shared.SentFlightEntry;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class SentFlightEntryDAOobjectifyTest {

	private final static LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(datastore);
	private static SentFlightEntryDAO sentFlightEntryDAO;

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		sentFlightEntryDAO = new SentFlightEntryDAOobjectify();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testListFlightEntry() {
		testAddSentFlightEntries();
	}

	@Test
	public void testAddSentFlightEntries() {
		List<SentFlightEntry> sentFlightEntries = new ArrayList<SentFlightEntry>();
		for (long i = 0; i < 10; i++) {
			sentFlightEntries.add(new SentFlightEntry(i, "excel", 1000 + 1));
		}
		sentFlightEntryDAO.addSentFlightEntries(sentFlightEntries);
		assertEquals(sentFlightEntryDAO.listFlightEntry("excel").size(), 10);
	}

	@Test
	public void testCreateOrUpdateSentFlightEntry() {
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4711L, "excel2", 1234L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4712L, "excel3", 1235L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4713L, "excel3", 1236L));
		assertEquals(0, sentFlightEntryDAO.listFlightEntry("excel").size());
		assertEquals(1, sentFlightEntryDAO.listFlightEntry("excel2").size());
		assertEquals(2, sentFlightEntryDAO.listFlightEntry("excel3").size());
	}

	@Test
	public void testDeleteSentFlightEntry() {
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4711L, "excel", 1234L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4712L, "excel", 1235L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4713L, "excel", 1236L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4714L, "excel2", 1237L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4715L, "excel3", 1237L));
		sentFlightEntryDAO.deleteSentFlightEntry("excel");
		assertEquals(0, sentFlightEntryDAO.listFlightEntry("excel").size());
		assertEquals(1, sentFlightEntryDAO.listFlightEntry("excel2").size());
		assertEquals(1, sentFlightEntryDAO.listFlightEntry("excel3").size());
	}

	@Test
	public void testPurgeSentFlightEntry() {
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("06.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("07.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("08.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("09.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("09.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel2", TestUtil.parseTimeString("10.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("11.04.203 09:00 utc").getTime()));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(99L, "excel", TestUtil.parseTimeString("12.04.203 09:00 utc").getTime()));
		assertEquals(7, sentFlightEntryDAO.listFlightEntry("excel").size());
		assertEquals(1, sentFlightEntryDAO.listFlightEntry("excel2").size());

		Calendar purgeDay = Calendar.getInstance();
		purgeDay.setTimeInMillis(TestUtil.parseTimeString("09.04.203 09:00 utc").getTime());
		sentFlightEntryDAO.purgeSentFlightEntry("excel", purgeDay);
		assertEquals(4, sentFlightEntryDAO.listFlightEntry("excel").size());
		long awayTime = TestUtil.parseTimeString("07.04.203 09:00 utc").getTime();
		long hereTime = TestUtil.parseTimeString("11.04.203 09:00 utc").getTime();
		int awayCnt = 0;
		int hereCnt = 0;
		for (SentFlightEntry sentFlightEntry : sentFlightEntryDAO.listFlightEntry("excel")) {
			if (sentFlightEntry.getLastModified() == awayTime) {
				awayCnt++;
			}
			if (sentFlightEntry.getLastModified() == hereTime) {
				hereCnt++;
			}
		}
		assertEquals(0, awayCnt);
		assertEquals(1, hereCnt);

		sentFlightEntryDAO.purgeSentFlightEntry("excel", purgeDay);
		assertEquals(4, sentFlightEntryDAO.listFlightEntry("excel").size());
	}

	@Test
	public void testPurgeSentFlightEntry2() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(TestUtil.parseTimeString("06.02.2011 09:00 utc").getTime());
		Calendar purgeDay = Calendar.getInstance();
		List<SentFlightEntry> sentFlightEntries = new ArrayList<SentFlightEntry>();
		List<Long> millies = new ArrayList<Long>();
		for (int i = 0; i < 30; i++) {
			sentFlightEntries.add(new SentFlightEntry(99L, "excel2", now.getTimeInMillis()));
			if (i == 8) {
				purgeDay.setTimeInMillis(now.getTimeInMillis());
			}
			millies.add(now.getTimeInMillis());
			now.add(Calendar.DAY_OF_YEAR, i);
		}
		sentFlightEntryDAO.addSentFlightEntries(sentFlightEntries);
		sentFlightEntryDAO.purgeSentFlightEntry("excel2", purgeDay);
		List<SentFlightEntry> list = sentFlightEntryDAO.listFlightEntry("excel2");
		assertEquals(22, list.size());
		for (int i = 0; i < list.size(); i++) {
			assertEquals("i=" + i, millies.get(8 + i).longValue(), list.get(i).getLastModified());
		}
	}

	@Test
	public void testGetFlightEntry() {
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4711L, "excel", 1234L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4712L, "excel1", 1235L));
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4713L, "excel2", 1236L));
		SentFlightEntry newSentFlightEntry = new SentFlightEntry(4713L, "excel2", 1236L);
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(newSentFlightEntry);
		sentFlightEntryDAO.createOrUpdateSentFlightEntry(new SentFlightEntry(4714L, "excel3", 1237L));
		SentFlightEntry oldSentFlightEntry = sentFlightEntryDAO.getSentFlightEntry("excel2", 4713L);
		assertFalse(oldSentFlightEntry.getId() == 0);
		assertEquals(newSentFlightEntry.getFlightEntry(), oldSentFlightEntry.getFlightEntry());
		assertEquals(newSentFlightEntry.getLastModified(), oldSentFlightEntry.getLastModified());
		assertEquals(newSentFlightEntry.getSendExcel(), oldSentFlightEntry.getSendExcel());
	}

}
