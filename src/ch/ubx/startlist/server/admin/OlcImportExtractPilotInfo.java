package ch.ubx.startlist.server.admin;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;
import ch.ubx.startlist.shared.FeFlightEntry;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class OlcImportExtractPilotInfo {

	private int maxImport = Integer.MAX_VALUE;

	private static final Logger log = Logger.getLogger(Olc2006AirfieldServlet.class.getName());

	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private List<FeFlightEntry> storedFlightEntries;
	static {
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	private Cache cache;
	private String webServer = "http://www.onlinecontest.org";

	public OlcImportExtractPilotInfo(List<FeFlightEntry> storedFlightEntries) {
		this.storedFlightEntries = new ArrayList<FeFlightEntry>(storedFlightEntries);

		Map<Integer, Integer> props = new HashMap<Integer, Integer>();
		props.put(GCacheFactory.EXPIRATION_DELTA, 600); // 600 seconds
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(props);
		} catch (CacheException e) {
			log.log(Level.WARNING, e.getMessage());
		}
	}

	public List<FeFlightEntry> olcImportFromPlace(String placeID, String place, int year, String country) throws Exception {
		List<FeFlightEntry> retFightEntries = new ArrayList<FeFlightEntry>(olcImport(year, placeID, place, null, country));
		// TODO - OLC starts in October, find a better solution.
		if (retFightEntries.size() == 0 && Calendar.getInstance().get(Calendar.MONTH) >= Calendar.OCTOBER) {
			retFightEntries.addAll(olcImport(year + 1, placeID, place, null, country));
		}
		return retFightEntries;
	}

	public void setMaxImport(int maxImport) {
		this.maxImport = maxImport;
	}

	private List<FeFlightEntry> olcImport(int olcYear, String placeID, String place, String club, String country) throws Exception {

		String sourceUrlString;
		if (placeID != null) {
			placeID = placeID.replace(" ", "%20");
			sourceUrlString = webServer + "/olc-2.0/gliding/flightsOfAirfield.html?rt=olc&sp=" + String.valueOf(olcYear) + "&d-2348235-p=&aa=" + placeID
					+ "&sc=&c=" + country + "&st=olc&paging=100000";
		} else {
			sourceUrlString = null; // TODO - implement
		}

		log.log(Level.INFO, "URL=" + sourceUrlString);

		Source source = getPageFromCache(sourceUrlString);
		boolean fromCache = source != null;
		if (!fromCache) {
			source = new Source(new URL(sourceUrlString));
		}

		// Call fullSequentialParse manually as most of the source will be
		// parsed.
		source.fullSequentialParse();
		List<Element> trElements = null;
		List<Element> tableElements = source.getAllElements(HTMLElementName.TABLE);

		log.log(Level.INFO, "tableElements size=" + tableElements.size());

		for (Element element : tableElements) {
			String id = element.getAttributeValue("id");
			if (id != null && id.equals("dailyScore")) {
				trElements = element.getAllElements(HTMLElementName.TR);
				break;
			}
		}

		// final String[] COLUMN_TXT = { "Date", "Pilot", "Ort", "Club",
		// "Aircracf", "Start", "End", "href" };
		final int[] WORLDWIDE_IDX = { -1, 2, 6, 7, 8, 9, 10, 11 };
		final int[] FLIGHTBOOK_IDX = { 0, 2, -1, 5, 6, 7, 8, 9 };

		int[] idx;
		final String reqUrl = webServer + "/olc-2.0/gliding/flightinfo.html";
		String urlStr0;
		if (placeID != null) {
			idx = FLIGHTBOOK_IDX;
			urlStr0 = reqUrl + "?flightId=";
		} else {
			idx = WORLDWIDE_IDX;
			urlStr0 = reqUrl + "?dsId=";
		}
		List<FeFlightEntry> flightEntries = new ArrayList<FeFlightEntry>();
		if (trElements != null) {
			for (Element trElement : trElements) {
				String cls = trElement.getAttributeValue("class");
				if (cls == null)
					continue;
				if (cls.equals("empty"))
					continue;

				if (flightEntries.size() == maxImport) {
					// save html page in cache for next iteration
					if (!fromCache) {
						putPageToCache(sourceUrlString, source);
					}
					break;
				}

				FeFlightEntry flightEntry = new FeFlightEntry();
				flightEntry.setSource(flightEntry.SRC_OLC);
				String dateStr = "";
				List<Element> tdElements = trElement.getAllElements(HTMLElementName.TD);
				for (int j = 0; j < idx.length - 1; j++) {
					switch (j) {
					case 0:
						if (idx[j] == -1) {
							// skip
						} else {
							dateStr = titleOrText(tdElements.get(idx[j]));
						}
						break;

					case 1:
						flightEntry.setPilot(normPilotName(titleOrText(tdElements.get(idx[j]))));
						break;

					case 2:
						if (idx[j] == -1) {
							flightEntry.setPlace(place);
							flightEntry.setLandingPlace(place);
						} else {
							flightEntry.setPlace(titleOrText(tdElements.get(idx[j])));
							flightEntry.setLandingPlace(flightEntry.getPlace());
						}
						break;

					case 3:
						flightEntry.setClub(titleOrText(tdElements.get(idx[j])));
						break;

					case 4:
						flightEntry.setRegistrationGlider(titleOrText(tdElements.get(idx[j])));
						break;

					case 5: {
						Date startDate = timeFormat.parse(dateStr + " " + titleOrText(tdElements.get(idx[j])));
						flightEntry.setStartTimeInMillis(startDate.getTime());
						flightEntry.setStartTimeValid(true);
					}
						break;

					case 6: {
						Date endDate = timeFormat.parse(dateStr + " " + titleOrText(tdElements.get(idx[j])));
						flightEntry.setEndTimeGliderInMillis(endDate.getTime());
						flightEntry.setEndTimeGliderValid(true);
					}
						break;

					default:
						break;
					}
				}
				if (!inList(storedFlightEntries, flightEntry)) {
					Element aElement = tdElements.get(idx[idx.length - 1]).getFirstElement(HTMLElementName.A);
					String href = aElement.getAttributeValue("href");
					if (href == null) {
						flightEntry.setRegistrationGlider("");
						flightEntry.setCompetitionID("");
					} else {
						String[] t = href.split("=");
						OlcImportExtractAircraftinfo aircraftinfo = new OlcImportExtractAircraftinfo(urlStr0 + t[t.length - 1]);
						flightEntry.setRegistrationGlider(aircraftinfo.getCallsign());
						flightEntry.setCompetitionID(aircraftinfo.getCompetitionID());
					}
					flightEntries.add(flightEntry);
				}
			}
		}
		List<FeFlightEntry> retFightEntries = new ArrayList<FeFlightEntry>();
		for (FeFlightEntry flightEntry : flightEntries) {
			if (!inList(retFightEntries, flightEntry)) {
				retFightEntries.add(flightEntry);
			}
		}
		log.log(Level.INFO, "retFightEntries size=" + retFightEntries.size());

		if (retFightEntries.size() == 0 && fromCache) {
			removePageFromCache(sourceUrlString);
		}

		return retFightEntries;
	}

	private void putPageToCache(String sourceUrlString, Source source) {
		if (cache != null) {
			if (!cache.containsKey(sourceUrlString)) {
				// TODO - should we use SourceCompactor?
				String cont = source.toString();
				cache.put(sourceUrlString, cont);
				log.log(Level.INFO, "Page put cache, URL=" + sourceUrlString);
			}
		}
	}

	private Source getPageFromCache(String sourceUrlString) {
		if (cache != null) {
			String cont = (String) cache.get(sourceUrlString);
			if (cont != null) {
				Source source = new Source((CharSequence) cont);
				if (source != null) {
					log.log(Level.INFO, "Page get from cache, URL=" + sourceUrlString);
					return source;
				}
			}
		}
		return null;
	}

	private void removePageFromCache(String sourceUrlString) {
		if (cache != null) {
			cache.remove(sourceUrlString);
			log.log(Level.INFO, "Page removed from cache, URL=" + sourceUrlString);

		}
	}

	private boolean inList(List<FeFlightEntry> storedFlightEntries2, FeFlightEntry storedFlightEntry) {
		for (FeFlightEntry flightEntry : storedFlightEntries2) {
			if (flightEntry.compareToMajor(storedFlightEntry) == 0) {
				return true;
			}
		}
		return false;
	}

	private String titleOrText(Element element) {
		String title = element.getAttributeValue("title");
		return title != null ? title : element.getContent().getTextExtractor().toString();
	}

	private String normPilotName(String olcPilotName) {
		int endIndex = olcPilotName.indexOf(" (");
		if (endIndex == -1) {
			return olcPilotName;
		} else {
			return olcPilotName.substring(0, endIndex);
		}
	}
}
