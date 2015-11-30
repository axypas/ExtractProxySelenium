package com.axypas.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

public class ProxyExtractor {

	public static void main(String[] args) throws Exception {
		 listProxies();
	}

	public static Map<String, Integer> listProxies() {
		// The Firefox driver supports javascript
		FirefoxProfile profile = new FirefoxProfile();

		// See: http://kb.mozillazine.org/About:config_entries for a complete list of profile settings.
		profile.setPreference( "browser.link.open_newwindow.restriction", 1);

		// Run driver with this profile this profile:
		FirefoxDriver driver = new FirefoxDriver(profile);
		// Go to the Google Suggest home page
		driver.get("http://hidemyass.com/proxy-list/");

		WebElement allCountries = driver.findElement(By.xpath("//*[@id='allCountries']"));
		allCountries.click();

		WebElement select = driver.findElement(By.xpath("//*[@id='country']"));
		Select dropDownCountries = new Select(select);
		dropDownCountries.selectByValue("Indonesia");
		// dropDownCountries.selectByValue("Brazil");
		dropDownCountries.selectByValue("Russian Federation");

		// only the below ports
		driver.findElement(By.xpath("//*[@id='ports']")).sendKeys("80,8080");

		// only the most secured proxies
		for (int i = 1; i < 5; i++) {
			driver.findElement(By.xpath("/html/body/div/div/form/div/div/ul/li[4]/fieldset/label[" + i + "]/input"))
					.click();
		}

		WebElement updateresults = driver.findElement(By.xpath("//*[@id='updateresults']"));
		updateresults.click();

		List<String> ips = new ArrayList<String>();
		List<String> ports = new ArrayList<String>();
		// And now list the suggestions
		List<WebElement> allIps = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr/td[2]"));
		for (WebElement suggestion : allIps) {
			if (suggestion.isDisplayed()) {
				// System.out.println(suggestion.getText());
				ips.add(suggestion.getText());
			}

		}

		List<WebElement> allPorts = driver.findElements(By.xpath("/html/body/div/div/table/tbody/tr/td[3]"));
		for (WebElement suggestion : allPorts) {
			if (suggestion.isDisplayed()) {
				ports.add(suggestion.getText());
				// System.out.println(suggestion.getText());
			}
		}

		driver.close();

		Map<String, Integer> ipPortMap = new HashMap<String, Integer>();
		try {
			for (int i = 0; i < ips.size(); i++) {
				ipPortMap.put(ips.get(i), Integer.valueOf(ports.get(i)));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ipPortMap;
	}

}
