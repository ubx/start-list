package ch.ubx.startlist.server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ErrorOutputTestHelper {

	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private PrintStream oldPrintStream;
	private boolean outOutput;

	public ErrorOutputTestHelper() {
		oldPrintStream = null;
		outOutput = true;
	}

	public void setUp() {
		if (oldPrintStream == null) {
			oldPrintStream = System.err;
		}
		System.setErr(new PrintStream(errContent));
		errContent.reset();
	}

	public void tearDown() {
		System.setErr(oldPrintStream);
	}

	public void setOutOutput(boolean outOutput) {
		this.outOutput = outOutput;
	}

	public String getSysErr() {
		String sysErr = errContent.toString();
		if (outOutput) {
			System.out.println("Err>" + sysErr + "<erR");
		}
		return sysErr;
	}

	public String takeSysErr() {
		String sysErr = getSysErr();
		errContent.reset();
		return sysErr;
	}

	public boolean contains(String string) {
		return getSysErr().contains(string);
	}
}
