package ch.ubx.startlist.client;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class StartListSeleniumTest {
	private WebDriver driver;
	private String baseUrl = "http://start-list-test.appspot.com/";
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	@Test @Ignore
	public void testStartList() throws Exception {
		for (int i = 0; i < 100; i++) {
			testStartListX();
		}
	}

	private void testStartListX() throws Exception {
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"Grenchen Gld\"]")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"Courtelary\"]")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"Grenchen Gld\"]")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"Grenchen Gld\"]")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"2011\"]")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("option[value=\"Grenchen Gld\"]")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		driver.findElement(By.cssSelector("button.gwt-Button")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
