package ch.ubx.startlist.server;

import ch.ubx.startlist.client.YearService;
import ch.ubx.startlist.shared.FeYear;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

public class YearServiceImpl extends RemoteServiceServlet implements YearService {

    private static final long serialVersionUID = 1L;

    @Override
    public Key<FeYear> listYears() {
        // TODO Auto-generated method stub
        return null;
    }

}
