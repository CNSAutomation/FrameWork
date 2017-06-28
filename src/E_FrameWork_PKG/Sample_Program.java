package E_FrameWork_PKG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Sample_Program {
	public static void main(String args[]) throws Exception
	{
		System.setProperty("webdriver.gecko.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.get("https://mail.google.com");
		Thread.sleep(4000);
		System.out.println("Badshahooo..! In da Process");
		try{
			driver.findElement(By.xpath("//table[@id=':ug']/tbody/tr/td[2]/div[2]/div")).sendKeys("Balle Balle");
		}catch(org.openqa.selenium.NoSuchElementException NSE)
		{
			driver.findElement(By.xpath(".//*[@id=':n6']")).sendKeys("Balle Balle...................!");
			
		}
	}

}

