package com.mindtree.Selenium.Milestone3;

import java.time.Duration;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.CharMatcher;

public class MilestoneAssessment3 {

	public static void main(String[] args) throws InterruptedException {
		final String CHROME_DRIVER = "webdriver.chrome.driver";
		final String CHROME_DRIVER_PATH = "D:\\Rakesh\\Downloads\\Compressed\\chromedriver_win32\\chromedriver.exe";
		System.setProperty(CHROME_DRIVER, CHROME_DRIVER_PATH);
		final String URL = "https://www.indiabookstore.net/";
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		String bookName = "Selenium testing tools Cookbook | By: Roy De Kleijn";
		String bookStore = "Amazon";
		String isbn = "ISBN-13";

		WebElement searchBar = driver.findElement(By.id("searchBox"));
		WebElement searchButton = driver.findElement(By.id("searchButtonInline"));
		searchBar.sendKeys("Selenium");
		searchButton.click();
		//Thread.sleep(3000);
		
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(6));
		By bookListResultBy = By.xpath("//li[contains(@class,'bookInfo text-center')] //img");
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(bookListResultBy));
		List <WebElement> bookList = driver.findElements(bookListResultBy);
		for(WebElement book : bookList) {
			if(book.getAttribute("title").trim().equalsIgnoreCase(bookName.trim())) {
				book.click();

			}
		}
		
		By isbnBookStoreBy = By.xpath("//div[@class='isbnContainer']");
		wait.until(ExpectedConditions.presenceOfElementLocated(isbnBookStoreBy));
		String isbnBookStore = driver.findElement(isbnBookStoreBy).getText();
		
		List <WebElement> retailers = driver.findElements(By.xpath("//div[@class='priceContainer'] //div[contains(@class,'ratings')] //a"));
		for(WebElement retail : retailers) {
			if(retail.getAttribute("href").trim().toLowerCase().contains(bookStore.trim().toLowerCase())) {
				retail.click();
			}
		}
		
		// Switching window
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> it = windows.iterator();
		it.next();
		String child = it.next();
		driver.switchTo().window(child);
		
		// Amazon
		String isbnAmazon = "";
		String isbnAmazonNew = "";
		By isbnAmazonBy = By.xpath("//div[@id='detailBullets_feature_div'] //li");
		wait.until(ExpectedConditions.presenceOfElementLocated(isbnAmazonBy));
		List <WebElement> isbnAmazone = driver.findElements(isbnAmazonBy);
		for(int i = 0 ; i< isbnAmazone.size() ; i++) {
			WebElement pdDetail = isbnAmazone.get(i).findElement(By.xpath(".//span"));
			if(pdDetail.getText().toLowerCase().contains(isbn.toLowerCase())) {
				WebElement adj = isbnAmazone.get(i).findElement(By.xpath(".//span/following-sibling::span"));
				isbnAmazon = adj.getText();
			}

		}
		for(int i = 0 ; i < isbnAmazon.length() ; i++) {
			//System.out.println(isbnAmazon.charAt(i));
			if(isbnAmazon.charAt(i) != '-') {
				isbnAmazonNew += isbnAmazon.charAt(i);
			}
		}
		
		if(isbnBookStore.equals(isbnAmazonNew)) {
			System.out.println("ISBN matched");
		}
		
		WebElement stock = driver.findElement(By.xpath("//div[@id='availability'] //span"));
		String inStockNum = CharMatcher.inRange('0','9').retainFrom(stock.getText());
		if(inStockNum != "0") {
			System.out.println("In stock");
		}
		
		WebElement price = driver.findElement(By.xpath("//div[@id='booksHeaderSection'] //span[@id='price']"));
		System.out.println("Price : " + price.getText());
		
		driver.close();
	}

}
