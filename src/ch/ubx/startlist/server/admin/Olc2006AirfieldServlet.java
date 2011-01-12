package ch.ubx.startlist.server.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import ch.ubx.startlist.server.AirfieldDAOobjectify;
import ch.ubx.startlist.shared.Airfield;

public class Olc2006AirfieldServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// private static final Logger log = Logger.getLogger(Olc2006AirfieldServlet.class.getName());
	private AirfieldDAOobjectify airfieldDAO = new AirfieldDAOobjectify();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if (ServletFileUpload.isMultipartContent(req)) {
			try {

				String query = req.getQueryString();
				Map<String, String> params = getQueryMap(query);
				String analyzeValStr = params.get("analyze");
				boolean analyze = analyzeValStr == null ? false : analyzeValStr.equalsIgnoreCase("true");
				ServletFileUpload upload = new ServletFileUpload();
				upload.setSizeMax(10240000); // 10MB, the maximum allowed size, in bytes.
				upload.setHeaderEncoding("UTF-8");
				FileItemIterator iterator = upload.getItemIterator(req);
				while (iterator.hasNext()) {
					FileItemStream item = iterator.next();
					InputStream stream = item.openStream();
					Serializer serializer = new Persister();
					StringBuffer sb = new StringBuffer();
					// parse xml file
					try {
						AirfieldSeeYou olc2006 = serializer.read(AirfieldSeeYou.class, stream);
						List<Airfields> list = olc2006.getList();
						if (analyze) {
							Set<String> countries = new TreeSet<String>();
							for (Airfields airfield : list) {
								countries.add(airfield.getCountry());
							}
							for (String country : countries) {
								sb.append(country + ";");
							}
							sb.setLength(sb.length() - 1); // skip last ";"
						} else {
							String country = params.get("country");
							String deleteValStr = params.get("delete");
							boolean delete = deleteValStr == null ? false : deleteValStr.equalsIgnoreCase("true");
							Set<Airfield> airfields = new HashSet<Airfield>();
							int cnt = 0;
							for (Airfields olc2006Airfield : list) {
								if (country == null || country.equalsIgnoreCase(olc2006Airfield.getCountry())) {
									Airfield airfield = new Airfield();
									airfield.setName(olc2006Airfield.getName());
									airfield.setCountry(olc2006Airfield.getCountry());
									airfield.setIcao_id(olc2006Airfield.getIcao_id());
									airfield.setId(olc2006Airfield.getId());
									airfields.add(airfield);
									cnt++;
								}
							}
							sb.append(Integer.toString(cnt) + " airfields added.");
							if (delete) {
								airfieldDAO.deleteAllAirfields();
							}
							airfieldDAO.addAirfields(airfields);
						}
						resp.setContentType("text/plain");
						resp.getOutputStream().write(sb.toString().getBytes());
						break;

					} catch (Exception ex) {
						throw new Exception(ex.getMessage());
					}
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
		}
		resp.setStatus(HttpServletResponse.SC_CREATED);
	}

	private static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String[] nameValue = param.split("=");
			String name = nameValue[0];
			String value = nameValue[1];
			map.put(name, value);
		}
		return map;
	}

}
