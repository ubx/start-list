package ch.ubx.startlist.server.admin;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root (name="olc2006")
public class AirfieldSeeYou {

	@ElementList(inline = true)
	private List<Airfields> olc2006;

	public List<Airfields> getList() {
		return olc2006;
	}

	public void setList(List<Airfields> list) {
		this.olc2006 = list;
	}

}
