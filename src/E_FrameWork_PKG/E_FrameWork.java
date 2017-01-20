package E_FrameWork_PKG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


public class E_FrameWork 
{
	// START : #Variable Declaration Section
	WebDriver driver;
	int inifunction=0;
	
	
	 //Object properties Variables 
	Properties objpro = null;
	FileInputStream objfile;
	
	//HTML Report
	BufferedWriter bwhtml;
	int StepNo=0;
	String ClassNm;
	int iterationPoint=0;
	int dpass=0,dfail=0,dexecution=0;
	// END : #Variable Declaration Section
	
	
	//Test Data File
	File file;
	FileInputStream inputStream;
	XSSFWorkbook srcBook;
	XSSFSheet sourceSheet;
	XSSFRow rowsline,rowslineSearch;
	FileOutputStream outputStream;
	
    public void EReport_CreateHTMLReport(String ClassName)throws Exception
    {
    	if(iterationPoint==0)
    	{
        	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy "));//HH:mm:ss
    		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(" HH-mm-ss "));
    		File fh = new File("C:\\E_FrameWork\\HTML_Report_Template\\"+ClassName+"_DATE_"+date+" & TIME_"+time+".html");
    		bwhtml = new BufferedWriter(new FileWriter(fh));
    		iterationPoint++;
    		
    	}
    	if(dpass!=0)
    	{
    		EReport_Stat();
    	}
		//Start - Writing HTML Template
		String Hearder="</table><br><h1>Component : "+ClassName+"</h1>"
				+ "<br>"
				+ "<table border=\"1\" style=\"width:70%\">"
				+ "<tr>"
				+ "<th style=\"width:10%\" bgcolor=\"#AFAAA9\">Step No.</th>"
				+ "<th style=\"width:20%\" bgcolor=\"#AFAAA9\">Object Name</th>"
				+ "<th style=\"width:30%\" bgcolor=\"#AFAAA9\">Action</th>"
				+ "<th style=\"width:10%\" bgcolor=\"#AFAAA9\">Result</th>"
				+ "</tr>";
		bwhtml.write(Hearder);
		dpass=0;
		dfail=0;
		dexecution=0;
		//END - Writing HTML Template
    }
    public void EReport_Stat() throws IOException
    {
    	int tot=dpass+dfail+dexecution;
		String Statuz="</table><br>"
				+ "<table border=\"0\" style=\"width:50%\">"
				+ "<tr>"
				+ "<th style=\"width:4%\" bgcolor=\"#AFAAA9\"></th>"
				+ "<th style=\"width:28%\" align=\"left\">TOTAL(Test Steps) : <b>"+tot+"</b></th>"
				+ "<th style=\"width:4%\" bgcolor=\"#006400\"></th>"
				+ "<th style=\"width:18%\" align=\"left\">PASS : <b>"+dpass+"</b></th>"
				+ "<th style=\"width:4%\" bgcolor=\"#FF0000\"></th>"
				+ "<th style=\"width:18%\" align=\"left\">FAIL : <b>"+dfail+"</b></th>"
				+ "<th style=\"width:4%\" bgcolor=\"#110A08\"></th>"
				+ "<th style=\"width:26%\" align=\"left\">DONE : <b>"+dexecution+"</b></th>"
				+ "</tr>"
				+ "<tr>"
				+ "<th style=\"width:4%\"></th>"
				+ "<th style=\"width:28%\" align=\"center\"><b>(100 %)</b></th>"
				+ "<th style=\"width:4%\"></th>"
				+ "<th style=\"width:18%\" align=\"left\"><b>("+Math.round((float)(dpass*100)/tot)+"%)</b></th>"
				+ "<th style=\"width:4%\"></th>"
				+ "<th style=\"width:18%\" align=\"left\"><b>("+Math.round((float)(dfail*100)/tot)+"%)</b></th>"
				+ "<th style=\"width:4%\"></th>"
				+ "<th style=\"width:26%\" align=\"left\"><b>("+Math.round((float)(dexecution*100)/tot)+"%)</b></th>"
				+ "</tr>";
		bwhtml.write(Statuz);
    }
    public void EReport_UpdateHTMLReport(String WebObjectName, String Action, String Result)throws Exception
    {
    	int checkfail=0;
		StepNo=StepNo+1;
    	String DataTest1="<tr>"
				+ "<td align=\"center\">"+StepNo+"</th>"
				+ "<td align=\"center\">"+WebObjectName+"</th>"
				+ "<td align=\"center\">"+Action+"</th>";
		
		
		bwhtml.write(DataTest1);
		
		String DataTest2 = null;
		if(Result=="PASS")
		{
			DataTest2="<td align=\"center\" bgcolor=\"#006400\">"+Result+"</th>"
					+ "</tr>";
			dpass=dpass+1;
		}
		else if(Result=="FAIL")
		{
			String dateSS = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy "));//HH:mm:ss
    		String timeSS = LocalDateTime.now().format(DateTimeFormatter.ofPattern(" HH-mm-ss "));
    		String NameAndTime = driver.getTitle()+"_DATE_"+dateSS+" & TIME_"+timeSS;
			E_TakeScreenShot("C:\\E_FrameWork\\ScreenShots", NameAndTime);
			
			String IssueSnapLink = "C:\\E_FrameWork\\ScreenShots\\"+NameAndTime+".jpg";

			DataTest2="<td align=\"center\" bgcolor=\"#FF0000\"><a href=\""+IssueSnapLink+"\">"+Result+"</a></th>"
					+ "</tr>";
			dfail=dfail+1;
			checkfail=1;
		}else
		{
			DataTest2="<td align=\"center\">"+Result+"</th>"
					+ "</tr>";
			dexecution=dexecution+1;
		}
				//+ "</table>";
				//Start - Close HTML
		bwhtml.write(DataTest2);	
		if(checkfail==1)
		{
			EReport_CloseHTMLReport();
			System.exit(0);
		}
    }
    public void EReport_CloseHTMLReport() throws IOException
    {
    	EReport_Stat();
    	bwhtml.close();
    }
    //START : #1 Browser Selection and Load URL
    public void E_LoadURL(String url) throws Exception
    {
	    	if(inifunction==0)
	    	{
	    		String browser = null;
	    		try{
		    		int brow=0;
			    	
			    	System.out.println("\n   = = = Select Browser = = = ");
			    	System.out.println("\t1. for Firefox");
			    	System.out.println("\t2. for IE");
			    	System.out.println("\t3. for Chrome");
			    	System.out.println("   = = = = = = = = = = = = = = ");
			    	System.out.println("Please Enter Selection : ");

			    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			    	brow = Integer.parseInt(in.readLine());
			    	
			    	if(brow>=1 && brow<=3)
			    	{
			    		switch(brow)
			    		{
			    			case 1:
			    				//Firefox
			    				System.setProperty("webdriver.gecko.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\geckodriver.exe");
			    				
			    			break;
			    			case 2:
			    				//IE
			    			
			    				System.setProperty("webdriver.ie.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\IEDriverServer.exe");
			    				
			    			break;
			    			case 3:
			    				//Chrome
			    				System.setProperty("webdriver.chrome.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\chromedriver.exe");
			    			break;
			    		}
			    		
			    		if(brow==1)
			    		{
			    			driver=new FirefoxDriver();
			    			browser="Firefox";
			    		}
			    		else if(brow==2)
			    		{
			    			driver=new InternetExplorerDriver();
			    			browser="Internet Explorer";
			    		}
			    		else if(brow==3)
			    		{
			    			driver=new ChromeDriver();
			    			browser="Chrome";
			    		}
			    		driver.manage().window().maximize();
			    	}
			    	else
			    	{
			    		System.out.println("Wrong Selection.... Have a Nice Day Ahead... !");
			    		System.exit(0);
			    	}
			    	inifunction++;
		    	}
		    	catch(Exception ex)
		    	{
		    		System.out.println("\nCheck setProperty option of Selected Browser");
		    		System.out.println("Log Below : ");
		    		System.out.println(ex);
		    		System.exit(0);
		    	}
	    		try{
	    			driver.get(Eexcel_HashCheck(url));
			    	System.out.println("\nInvoke \""+browser+"\" Browser, Opening \'"+Eexcel_HashCheck(url)+"\' URL.");
			    	EReport_UpdateHTMLReport(browser,"Invoke \""+browser+"\" Browser","PASS");
	    		}catch(org.openqa.selenium.InvalidArgumentException IAE){
	    			System.out.println("\nInvalid URL, cannot open ["+Eexcel_HashCheck(url)+"] this URL.");
	    			EReport_UpdateHTMLReport(browser,"Invoke \""+browser+"\" Browser","FAIL");
	    			System.exit(0);
	    		}
	    	}
	    	else
	    	{
	    		try{
	    		driver.navigate().to(Eexcel_HashCheck(url));
	    		System.out.println("\nRedirected to : \'"+Eexcel_HashCheck(url)+"\' URL.");
	    		EReport_UpdateHTMLReport("Nevigation","Redirect URL to :"+Eexcel_HashCheck(url),"PASS");
	    		}catch(org.openqa.selenium.InvalidArgumentException IAE){
	    			System.out.println("\nInvalid URL, cannot redirect to ["+Eexcel_HashCheck(url)+"] this URL.");
	    			EReport_UpdateHTMLReport("Nevigation","Redirect URL to :"+Eexcel_HashCheck(url),"FAIL");
	    			System.exit(0);
	    		}
	    	}
    }
    //END : #1 Browser Selection and Load URL
    
    //-------------------------------------------------------------------
    
    //START : #2 CallWebObjectsFile
    public void E_CallWebObjectsFile(String WebObjectFileName)throws Exception
    {
    	try{
        	objpro = new Properties();
        	objfile = new FileInputStream("C:\\E_FrameWork\\WebObject Repository\\"+WebObjectFileName+".properties");
        	System.out.println("\"C:\\E_FrameWork\\WebObject Repository\\"+WebObjectFileName+".properties\" Properties File Loaded.");
        	objpro.load(objfile); 
    	}catch(FileNotFoundException FNFE)
    	{
    		System.out.println("\nFile \""+WebObjectFileName+"\" Not Found At \"C:\\E_FrameWork\\WebObject Repository\\\" Location.");
    	}
    	
    }
    //END : #2 CallWebObjectsFile
    
    //-------------------------------------------------------------------
    
    //START : get Object
    public By E_GetObject(String WebObjectName)
	{
		char[] divObj = WebObjectName.toCharArray();
		int getval=0;
		String gotAttribute="";
		
		for(int i=0;i<divObj.length;i++)
		{
			if(divObj[i] == '.')
			{
				getval=1;
			}
			if(getval==1)
			{
				gotAttribute = gotAttribute+""+divObj[i];
			}
		}
		
		By CodePath = null;
		
		switch(gotAttribute.substring(1).toUpperCase())
		{
			case "XPATH":
				CodePath = By.xpath(objpro.getProperty(WebObjectName));
				//CodePath = By.xpath("get.properties(NAME)");
				break;
			case "ID":
				CodePath = By.id(objpro.getProperty(WebObjectName));
				break;
			case "NAME":
				CodePath = By.name(objpro.getProperty(WebObjectName));
				break;
			case "CLASS":
				CodePath = By.className(objpro.getProperty(WebObjectName));
				break;
			case "LINK_EXACT":
				CodePath = By.linkText(objpro.getProperty(WebObjectName));
				break;
			case "LINK_PARTIAL":
				CodePath = By.partialLinkText(objpro.getProperty(WebObjectName));
				break;
			case "INNERTEXT":
				CodePath = By.xpath("//*[text()='"+objpro.getProperty(WebObjectName)+"']");
				break;
			case "CSS":
				CodePath = By.cssSelector(objpro.getProperty(WebObjectName));
				break;
			default:
				System.out.println("\ngetObject() : Attribute Do Not Match");
		}
		
		return CodePath;
		
	}
    //END : get Object
    
    //Start : Wait For Page Load
    public void E_WaitForPageLoad() throws Exception
    {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		//This loop will rotate for 100 times to check If page Is ready after every 1 second.
	    //You can replace your if you wants to Increase or decrease wait time.
		int waittime;
		waittime = 60;
	    for (int i=0; i<waittime; i++)
	    { 
	       try 
	        {
	    	   Thread.sleep(1000);
	        }catch (InterruptedException e) {} 
	        //To check page ready state.
	        if (js.executeScript("return document.readyState").toString().equals("complete"))
	        { 
	        	//System.out.println("Wait for Page Load : "+js.executeScript("return document.readyState").toString());
	            break; 
	        }   
	    }
	    System.out.println("\nWeb-Page Loaded.");
	    EReport_UpdateHTMLReport("","Wait For Page Load","Done");
    }
    //END : Wait For Page Load
    
    public void E_Write(String WebObjectName,String EnterTestData)throws Exception
    {
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		driver.findElement(AtrributeObj).sendKeys(Eexcel_HashCheck(EnterTestData));
    		System.out.println("\nEnter Value in ["+Eexcel_HashCheck(WebObjectName)+"] TextObject.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Entered Test Data :"+Eexcel_HashCheck(EnterTestData),"PASS");
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Enter Test Data :"+Eexcel_HashCheck(EnterTestData)+" [Error:Element Not Found]","FAIL");
    		System.exit(0);
    	}
    }
    public void E_Click(String WebObjectName) throws Exception
    {
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		driver.findElement(AtrributeObj).click();
    		
    		System.out.println("\nClick on ["+Eexcel_HashCheck(WebObjectName)+"] Object.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Click","PASS");
    		
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Click [Error:Element Not Found]","FAIL");
    		System.exit(0);
    	}
    }
    public void E_Select(String WebObjectName,String ListValue) throws Exception
    {
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		Select select = new Select(driver.findElement(AtrributeObj));
    		select.selectByVisibleText(Eexcel_HashCheck(ListValue));
    		System.out.println("\nSelect ["+Eexcel_HashCheck(ListValue)+"] Value from ["+Eexcel_HashCheck(ListValue)+"] Object");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Select ["+Eexcel_HashCheck(WebObjectName)+"] Value","PASS");
    	}catch(org.openqa.selenium.NoSuchElementException NSEE){
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Select ["+Eexcel_HashCheck(ListValue)+"] Value. [Error:Element Not Found]","FAIL");
    		System.exit(0);	
    	}
    }
    public void E_RunTimeChangeWebObjectValue(String WebObjectPropertyName,String WebObjectValue) throws Exception
    {
    	objpro.setProperty(Eexcel_HashCheck(WebObjectPropertyName), Eexcel_HashCheck(WebObjectValue));
    	System.out.println("\nChanged Runtime property value of ["+Eexcel_HashCheck(WebObjectPropertyName)+"] object to ["+Eexcel_HashCheck(WebObjectValue)+"].");
    }
  
 	public void E_TakeScreenShot(String Path,String FileName)throws Exception
  	{
  			String FilePathandNameP = Eexcel_HashCheck(Path)+"\\"+Eexcel_HashCheck(FileName)+".jpg";
  			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
  			FileUtils.copyFile(scrFile, new File(FilePathandNameP));
  	}
 	
 	
	public void ETestData_File(String PathAndFileName,String SheetName)throws Exception
	{
		try{
			file = new File(PathAndFileName);
		    inputStream = new FileInputStream(file);
			srcBook = new XSSFWorkbook(inputStream);
			sourceSheet= srcBook.getSheet(SheetName);
		}catch(FileNotFoundException FNFE)
		{
			System.out.println("File not found, it might be opened or not available at location.");
		}
	}

	public String ETestData_GetData(String ColumnName,int RowNo) throws Exception
	{
			int ColInNum = Eexcel_SearchColumn(ColumnName);
			rowsline= sourceSheet.getRow(RowNo);
		try
		{
			return rowsline.getCell(ColInNum).getStringCellValue();
		}catch(NullPointerException e)
		{
			return null;
		}
	}
	public void ETestData_SetData(String ColumnName,String Set_Data,int RowNo) throws Exception
	{
			int ColInNum = Eexcel_SearchColumn(ColumnName);
			rowsline = sourceSheet.getRow(RowNo);
			
			if(rowsline == null)
			{
				rowsline= sourceSheet.createRow(RowNo);
			}
			XSSFCell newCell = rowsline.createCell(ColInNum);
			newCell.setCellValue(Set_Data);
			outputStream = new FileOutputStream(file);
			srcBook.write(outputStream);
	}
 	public int Eexcel_SearchColumn(String ColumnName)throws Exception
 	{
 		int i=0,y=0;
 		rowsline = sourceSheet.getRow(0); 
		try
		{
			for(;i<=500;i++)
			{
				if(rowsline.getCell(i).getStringCellValue().equals(ColumnName))
				{
					break;
				}
			}
			return i;
		}catch(NullPointerException e)
		{
			System.out.print("\nColumn ["+ColumnName+"] was not available, so added in Test Data File.");
			if(rowsline == null)
			{
				rowsline= sourceSheet.createRow(0);
			}
			try
			{
				for(;y<=500;y++)
				{
					if(rowsline.getCell(y).getStringCellValue().equals(""))
					{
						break;
					}
				}
			}catch(NullPointerException e1)
			{
				XSSFCell newCell = rowsline.createCell(y);
				newCell.setCellValue(ColumnName);
				outputStream = new FileOutputStream(file);
				srcBook.write(outputStream);
				System.out.print(" Add Test Data for ["+ColumnName+"] Column.");
				System.exit(0);
				return y;
			}
		}
		return i;
 	}
 	public void ETestData_CloseFile() throws IOException
 	{
		srcBook.close();
		inputStream.close();
		if(outputStream != null)
		{
			outputStream.close();
			
		}
 	}
 	public boolean E_IsElementDisplayed(String WebObjectName) throws Exception
 	{
 		try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		boolean eledisplay = driver.findElement(AtrributeObj).isDisplayed();

    		System.out.println("\nIs Element Displayed ["+Eexcel_HashCheck(WebObjectName)+" = "+eledisplay+"].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Displayed = "+eledisplay,"Done");
    		
    		return eledisplay;
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\nIs Element Displayed ["+ Eexcel_HashCheck(WebObjectName) +" = False].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Displayed = false","Done");
    		return false;
    	}
 	}
 	public boolean E_IsElementEnabled(String WebObjectName) throws Exception
 	{
 		try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		boolean eledisplay = driver.findElement(AtrributeObj).isEnabled();

    		System.out.println("\nIs Element Enabled ["+Eexcel_HashCheck(WebObjectName)+" = "+eledisplay+"].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Enabled = "+eledisplay,"Done");
    		
    		return eledisplay;
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\nIs Element Enabled ["+ Eexcel_HashCheck(WebObjectName) +" = False].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Enabled = false","Done");
    		return false;
    	}
 	}
 	public boolean E_IsElementExist(String WebObjectName) throws Exception
 	{
 		try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		boolean eledisplay = driver.findElements(AtrributeObj).size()!=0;

    		System.out.println("\nIs Element Exist ["+Eexcel_HashCheck(WebObjectName)+" = "+eledisplay+"].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Exist = "+eledisplay+"","Done");
    		
    		return eledisplay;
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\nIs Element Exist ["+ Eexcel_HashCheck(WebObjectName) +" = False].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Check Element Exist = false","Done");
    		return false;
    	}
 	}
 	public void E_WaitUntilElementDisplay(String WebObjectName) throws Exception
 	{
 		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
 		int i=1;
		boolean eleche,eleche1 = false;	
		while(i<=1)
		{
				try{
					eleche = driver.findElements(AtrributeObj).size()!=0;
				}catch(InvalidSelectorException ISExcep)
				{
					eleche = false;
				}
				if(eleche == true)
				{
					
					while(i<=1)
					{
						try{
							eleche1=driver.findElement(AtrributeObj).isDisplayed();
						}catch(org.openqa.selenium.NoSuchElementException NSEE){
							eleche1=false;
						}
						if(eleche1 == true)
						{
							i=2;
							System.out.println("\nElement ["+Eexcel_HashCheck(WebObjectName)+"] Displayed.");
						}
						else
						{
							i=1;
							Thread.sleep(1500);
							System.out.println("\nWaiting for ["+Eexcel_HashCheck(WebObjectName)+"] element, to display.");
						}
					}
				}
				else
				{
					i=1;
					Thread.sleep(1500);
					System.out.println("\nWaiting for ["+Eexcel_HashCheck(WebObjectName)+"] element, to display.");
				}
		}
 	}
 	public String E_CaptureProperty(String WebObjectName,String PropertyName2Capture)throws Exception
 	{
 		String CapturedPro=null;
 		int justcheck=0;
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		CapturedPro = driver.findElement(AtrributeObj).getAttribute(Eexcel_HashCheck(PropertyName2Capture));
    		System.out.println("\nCaptured property for ["+Eexcel_HashCheck(WebObjectName)+"] Object, is ["+Eexcel_HashCheck(PropertyName2Capture)+" = "+CapturedPro+"].");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Captured Property :["+Eexcel_HashCheck(PropertyName2Capture)+" = "+CapturedPro+"]","Done");
    		justcheck=1;
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Capture Property :"+Eexcel_HashCheck(PropertyName2Capture)+" [Error:Element Not Found]","FAIL");
    	}
    	if(justcheck==1)
    	{
    		return CapturedPro;
    	}
    	else
    	{
    		return null;
    	}
		//return PropertyName2Capture;
 	}
 	public String Eexcel_HashCheck(String CheckData) throws Exception
 	{
 		String FirstChar = CheckData.substring(0,1);
 		String ColumnName = CheckData.substring(1);
 		//String ColumnName = CheckData.replaceAll(CheckData, CheckData.substring(0,1));
		String TData = null;
		if(FirstChar.equals("#"))
		{
			TData= ETestData_GetData(ColumnName,1);
		}
		else
		{
			TData=CheckData;
		}
		return TData;
 	}
 	public void E_Alert_Accept()
 	{
 		driver.switchTo().alert().accept();
 	}
 	public void E_Alert_dismiss()
 	{
 		driver.switchTo().alert().dismiss();
 	}
 	public void E_Switch2Frame(String FrameName) throws Exception
 	{
 		try{
 			driver.switchTo().frame(Eexcel_HashCheck(FrameName));
 			System.out.println("\nSwitched to ["+Eexcel_HashCheck(FrameName)+"] frame.");
 			EReport_UpdateHTMLReport("","Switched to [ "+Eexcel_HashCheck(FrameName)+" ] frame.","Done");
 		}catch(NoSuchFrameException NSFE)
 		{
 			System.out.println("\n["+Eexcel_HashCheck(FrameName)+"] frame not found.");
 			EReport_UpdateHTMLReport("","Switched to [ "+Eexcel_HashCheck(FrameName)+" ] frame [ERROR : Frame Not Found]","FAIL");
 		}
 	}
 	public void E_Switch_Back()
 	{
 		driver.switchTo().defaultContent();
 	}
 	public void Efile_CloseALL() throws Exception
 	{
 		//Close HTML Report
 		EReport_CloseHTMLReport();
 		//Close Test Data File
 		ETestData_CloseFile();
 		//Close Object File
		if(objfile != null)
		{
			objfile.close();
		}
 	}
}