package ch.ubx.startlist.client;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public interface TimeFormat {

	public static final DefaultFormat MM_HH_FORMAT = new DateBox.DefaultFormat(DateTimeFormat.getFormat("HHmm"));
	public static final DefaultFormat DD_MMM_YYYY_FORMAT = new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy"));
	public static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("dd.MM");
	public static final DateTimeFormat TIME_FORMAT_TABLE = DateTimeFormat.getFormat("HH:mm");

	public static final TimeZone timeZone = TimeZone.createTimeZone(-120); // TODO - why?
}