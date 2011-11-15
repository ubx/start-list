package ch.ubx.startlist.shared;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;

@Cached
@Entity
public abstract class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	protected String name;

	public abstract void execute(String string, Calendar now);

}
