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
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
	
	//Browser_Selection
	int brow=0;
	
	//Test Data File
	File file;
	FileInputStream inputStream;
	XSSFWorkbook srcBook;
	XSSFSheet sourceSheet;
	XSSFRow rowsline,rowslineSearchColumn,rowslineSearch;
	FileOutputStream outputStream;
	
	//Tab Switch
	 ArrayList<String> tabs2;
	//Default PAth
	String ClassNm1;
	
	public static void main(String args[])
	{
		
	}
    public void EReport_CreateHTMLReport(String ClassName)throws Exception
    {
    	if(iterationPoint==0)
    	{
        	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));//HH:mm:ss
    		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
    		//File fh = new File("C:\\E_FrameWork\\HTML_Report_Template\\"+ClassName+"_DATE_"+date+" & TIME_"+time+".html");
    		File fh = new File(ClassName+"_DATE_"+date+" & TIME_"+time+".html");
    		bwhtml = new BufferedWriter(new FileWriter(fh));
    		iterationPoint++;
    		
    	}
    	//if(dpass!=0)
    	//{
    	//	EReport_Stat();
    	//}
    	if(StepNo!=0)
    	{
    		EReport_Stat();
    	}
		//Start - Writing HTML Template   Add Reserve
    	
    	//To get Exact Class Name
    	StringBuffer buffer = new StringBuffer(ClassName);  
    	
    	String ClassNm = StringUtils.substringBefore(buffer.reverse().toString(), "\\");
    	
    	buffer = new StringBuffer(ClassNm);
    	buffer.reverse();
    	    	
		String Hearder="</table><br><h1>Component : "+buffer.toString()+"</h1>"
				+ "<br>"
				+ "<table align=\"center\"  border=\"1\" style=\"width:70%\">"
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
		
		ClassNm1=ClassName;
		//END - Writing HTML Template
    }
    public void EReport_Stat() throws IOException
    {
    	int tot=dpass+dfail+dexecution;
		String Statuz="</table><br>"
				+ "<table align=\"center\" border=\"0\"  style=\"width:50%\">"
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
			String dateSS = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));//HH:mm:ss
    		String timeSS = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
    		
    		String DriverTitle = RemoveAllSpecialCharacters(driver.getTitle()+":");
    		
    		
    		
    				
    		String NameAndTime = DriverTitle+"_DATE_"+dateSS+" & TIME_"+timeSS;
    		
    		//new Lines
        	StringBuffer buffer = new StringBuffer(ClassNm1);  
        	
        	String ClassNm = buffer.reverse().toString();
        	
        	ClassNm = ClassNm.substring(ClassNm.indexOf('\\')+1);
        	
        	buffer = new StringBuffer(ClassNm);
        	buffer.reverse();
        	
    		//new Lines
    		
    	
    		//buffer.toString().replace("\\Result", "");
    		
			E_TakeScreenShot(buffer.toString().replace("\\Result", "")+"\\ScreenShots", NameAndTime);
			
			String IssueSnapLink = buffer.toString().replace("\\Result", "")+"\\ScreenShots\\"+NameAndTime+".jpg";
			
			DataTest2="<td align=\"center\" bgcolor=\"#FF0000\"><a href=\""+IssueSnapLink+"\">"+Result+"</a></th>"
					+ "</tr>";
			dfail=dfail+1;
			//checkfail=1;
			
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
			//EReport_Stat();
			EReport_CloseHTMLReport();
			System.exit(0);
		}
    }
    public void EReport_CloseHTMLReport() throws IOException
    {
    	EReport_Stat();
    	bwhtml.close();
    }
    //START : #0 BROWSER SELECTION
    public void E_Browser_Selection(Integer broww) throws Exception
    {
    	brow = broww;
    }
    //START : #1 Browser Selection and Load URL
    public void E_LoadURL(String url) throws Exception
    {
	    	if(inifunction==0)
	    	{
	    		String browser = null;
	    		try{
		    		
			    	/*
			    	System.out.println("\n   = = = Select Browser = = = ");
			    	System.out.println("\t1. for Firefox");
			    	System.out.println("\t2. for IE");
			    	System.out.println("\t3. for Chrome");
			    	System.out.println("   = = = = = = = = = = = = = = ");
			    	System.out.println("Please Enter Selection : ");

			    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			    	brow = Integer.parseInt(in.readLine());
			    	*/
		    		
			    	//brow=Integer.parseInt(objpro.getProperty("Browser"));

			    	if(brow>=1 && brow<=4)
			    	{
			    		switch(brow)
			    		{
			    			case 1:
			    				//Firefox
			    				System.setProperty("webdriver.gecko.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\geckodriver.exe");
			    				System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
			    				
			    			break;
			    			case 2:
			    				//IE
			    			
			    				System.setProperty("webdriver.ie.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\IEDriverServer.exe");
			    				
			    			break;
			    			case 3:
			    				//Chrome
			    				System.setProperty("webdriver.chrome.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\chromedriver.exe");
			    			break;
			    			case 4:
			    				//Edge
			    				System.setProperty("webdriver.edge.driver", "C:\\E_FrameWork\\Automation JARS_Drivers\\Browser Drivers\\MicrosoftWebDriver_3.exe");
			    			break;
			    		}
			    		
			    		if(brow==1)
			    		{
			    			driver=new FirefoxDriver();
			    			browser="\"Firefox\" Browser";
			    		}
			    		else if(brow==2)
			    		{
			    			driver=new InternetExplorerDriver();
			    			browser="\"Internet Explorer\" Browser";
			    		}
			    		else if(brow==3)
			    		{
			    			driver=new ChromeDriver();
			    			browser="\"Chrome\" Browser";
			    		}
			    		else if(brow==4)
			    		{
				    		driver = new EdgeDriver();
				    		browser = "\"Windows Edge\" Browser";
			    		}

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
	    			driver.manage().window().maximize();
	    			driver.get(Eexcel_HashCheck(url));
			    	System.out.println("\nInvoke \""+browser+"\" Browser, Opening \'"+Eexcel_HashCheck(url)+"\' URL.");
			    	EReport_UpdateHTMLReport(browser,"Invoke \""+browser+"\" Browser","PASS");
			    	EReport_UpdateHTMLReport(browser,"Opening \""+Eexcel_HashCheck(url)+"\" URL.","PASS");
	    		}catch(org.openqa.selenium.InvalidArgumentException | java.lang.NullPointerException IAE){
	    			System.out.println("\nInvalid URL, cannot open ["+Eexcel_HashCheck(url)+"] this URL.");
	    			EReport_UpdateHTMLReport(browser,"Invoke \""+browser+"\" Browser","FAIL");
	    			EReport_UpdateHTMLReport(browser,"Opening \""+Eexcel_HashCheck(url)+"\" URL.","FAIL");
	    			throw new java.io.FileNotFoundException("");
	    			
	    			//System.exit(0);
	    		}
	    	}
	    	else
	    	{
	    		try{
	    		driver.navigate().to(Eexcel_HashCheck(url));
	    		System.out.println("\nRedirected to : \'"+Eexcel_HashCheck(url)+"\' URL.");
	    		EReport_UpdateHTMLReport("Navigation","Redirect URL to :"+Eexcel_HashCheck(url),"PASS");
	    		}catch(org.openqa.selenium.InvalidArgumentException IAE){
	    			System.out.println("\nInvalid URL, cannot redirect to ["+Eexcel_HashCheck(url)+"] this URL.");
	    			EReport_UpdateHTMLReport("Navigation","Redirect URL to :"+Eexcel_HashCheck(url),"FAIL");
	    			//System.exit(0);
	    		}
	    	}
    }
    //END : #1 Browser Selection and Load URL
    
    //-------------------------------------------------------------------
    public String RemoveAllSpecialCharacters(String sstring)
    {
    //	String[] specialchars = {"~","`","!","@","#","£","€","$","¢","¥","§","%","°","^","&","*","(",")","-","_","+","=","{","}","[","]","|","\\","/",":",";","\"","\'","<",">",",",".","?"};

    	String FN1,FN2,FN3,FN4,FN5,FN6,FN7,FN8,FN9,FN10,FN11;
    	FN1=sstring.replace("\\","");
    	FN2=FN1.replace("\'", "");
    	FN3=FN2.replace("\"", "");
    	FN4=FN3.replace("/", "");
    	FN5=FN4.replace(":", "");
    	FN6=FN5.replace("*", "");
    	FN7=FN6.replace("?", "");
    	FN8=FN7.replace("<", "");
    	FN9=FN8.replace(">", "");
    	FN10=FN9.replace("|", "");
    	FN11=FN10.replace("  ", "");
    	/*for(int i=0;i<=specialchars.length-1;i++)
    	{
    		tempString = sstring.replace(specialchars[i], "");
    		
    	}*/
    	return FN11;
    }
    //START : #2 CallWebObjectsFile
    public void E_CallWebObjectsFile(String WebObjectFileName)throws Exception
    {
    	try{
        	objpro = new Properties();
        	objfile = new FileInputStream(WebObjectFileName);
        	System.out.println("\""+WebObjectFileName+"\" Properties File Loaded.");
        	objpro.load(objfile); 
    	}catch(FileNotFoundException FNFE)
    	{
    		System.out.println("\nFile \""+WebObjectFileName+"\" Not Found.");
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
    public void E_MyWait(int TimeSeconds)throws Exception
    {
    	Thread.sleep(TimeSeconds*1000);
    	System.out.println("\nWait for : "+TimeSeconds+" Seconds.");
    }
    //Start : Wait For Page Load
    public void E_WaitForPageLoad() throws Exception
    {
		//driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
    		((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", driver.findElement(AtrributeObj));
    		driver.findElement(AtrributeObj).sendKeys(Eexcel_HashCheck(EnterTestData));
    		System.out.println("\nEnter Value in ["+Eexcel_HashCheck(WebObjectName)+"] TextObject.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Entered Test Data :"+Eexcel_HashCheck(EnterTestData),"PASS");
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Enter Test Data :"+Eexcel_HashCheck(EnterTestData)+" [Error:Element Not Found]","FAIL");
    		//System.exit(0);
    		throw new org.openqa.selenium.NoSuchElementException("Element Not Found");
    	}
    }
    public void E_Click(String WebObjectName) throws Exception
    {
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", driver.findElement(AtrributeObj));
    		
    		driver.findElement(AtrributeObj).click(); 
    		
    		//Highlighter BC
    		System.out.println("\nClick on ["+Eexcel_HashCheck(WebObjectName)+"] Object.");
    		
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Click","PASS");
    		
    	}catch(org.openqa.selenium.NoSuchElementException NSEE)
    	{
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Click [Error:Element Not Found]","FAIL");
    		//EReport_CloseHTMLReport();
    		//System.exit(0);
    		throw new org.openqa.selenium.NoSuchElementException("Element Not Found");
    	}
    }
    public void E_Select(String WebObjectName,String ListValue) throws Exception
    {
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		Select select = new Select(driver.findElement(AtrributeObj));
    		select.selectByVisibleText(Eexcel_HashCheck(ListValue));
    		System.out.println("\nSelect ["+Eexcel_HashCheck(ListValue)+"] Value from ["+Eexcel_HashCheck(WebObjectName)+"] Object");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Select ["+Eexcel_HashCheck(ListValue)+"] Value","PASS");
    	}catch(org.openqa.selenium.NoSuchElementException NSEE){
    		System.out.println("\n\""+Eexcel_HashCheck(WebObjectName)+"\" Element not found.");
    		EReport_UpdateHTMLReport(Eexcel_HashCheck(WebObjectName),"Select ["+Eexcel_HashCheck(ListValue)+"] Value. [Error:Element Not Found]","FAIL");
    		//System.exit(0);	
    		throw new org.openqa.selenium.NoSuchElementException("Element Not Found");
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
 	
 	
	public void ETestData_File(String PathAndFileName)throws Exception
	{
		try{
			file = new File(PathAndFileName);
		    inputStream = new FileInputStream(file);
			srcBook = new XSSFWorkbook(inputStream);
			
		}catch(FileNotFoundException FNFE)
		{
			System.out.println("File not found, it might be opened or not available at location.");
		}
	}
	public void ETestData_FileSheetAndRow(String Sheet_Name,int Row_Number)
	{
		try{
			sourceSheet= srcBook.getSheet(Sheet_Name);
			rowsline= sourceSheet.getRow(Row_Number);
		}
		catch(NullPointerException e)
		{
			if(rowsline == null)
			{
				rowsline= sourceSheet.createRow(Row_Number);
			}
		}
	}
	
	public String ETestData_GetData(String ColumnName) throws Exception
	{
			int ColInNum = Eexcel_SearchColumn(ColumnName);
		try
		{
			return rowsline.getCell(ColInNum).getStringCellValue();
		}catch(NullPointerException e)
		{
			return null;
		}
	}
	public void ETestData_SetData(String ColumnName,String Set_Data) throws Exception
	{
		CellStyle style = srcBook.createCellStyle(); //temp color
		Font blueFont = srcBook.createFont(); //temp color
		blueFont.setColor(HSSFColor.BLUE.index); //temp color
		style.setFont(blueFont); //temp color
	    
			int ColInNum = Eexcel_SearchColumn(ColumnName);
			XSSFCell newCell = rowsline.createCell(ColInNum);
			newCell.setCellValue(Set_Data);
			newCell.setCellStyle(style);//temp color
			outputStream = new FileOutputStream(file);
			srcBook.write(outputStream);
			srcBook.setForceFormulaRecalculation(true);
	}
 	public int Eexcel_SearchColumn(String ColumnName)throws Exception
 	{
 		int i=0,y=0;
 		rowslineSearchColumn = sourceSheet.getRow(0); 
		try
		{
			for(;i<=500;i++)
			{
				if(rowslineSearchColumn.getCell(i).getStringCellValue().equals(ColumnName))
				{
					break;
				}
			}
			return i;
		}catch(NullPointerException e)
		{
			System.out.print("\nColumn ["+ColumnName+"] was not available, so added in Test Data File.");
			if(rowslineSearchColumn == null)
			{
				rowslineSearchColumn= sourceSheet.createRow(0);
			}
			try
			{
				for(;y<=500;y++)
				{
					if(rowslineSearchColumn.getCell(y).getStringCellValue().equals(""))
					{
						break;
					}
				}
			}catch(NullPointerException e1)
			{
				XSSFCell newCell = rowslineSearchColumn.createCell(y);
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
 		srcBook.setForceFormulaRecalculation(true); //For Excel Formula Calculation
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
 	public String E_CaptureProperty(String WebObjectName,String PropertyName2Capture,String ColumnName)throws Exception
 	{
 		String CapturedPro=null;
 		int justcheck=0;
    	try{
    		By AtrributeObj = E_GetObject(Eexcel_HashCheck(WebObjectName));
    		CapturedPro = driver.findElement(AtrributeObj).getAttribute(Eexcel_HashCheck(PropertyName2Capture));
    		
    		if(ColumnName.equals(""))
    		{
    			System.out.println("\nCaptured attribute of ["+Eexcel_HashCheck(WebObjectName)+"] Object, ["+Eexcel_HashCheck(PropertyName2Capture)+" = "+CapturedPro+"].");
    		}
    		else
    		{
    			//To Check Whether Hash or not
    			String FirstChar = ColumnName.substring(0,1);
    			if(FirstChar.equals("#"))
    			{
    				String ColumnName2 = ColumnName.substring(1);
    				ETestData_SetData(ColumnName2, CapturedPro);
    			}
    			else
    			{
    				ETestData_SetData(ColumnName, CapturedPro);
    			}
    			System.out.println("\nCaptured attribute of ["+Eexcel_HashCheck(WebObjectName)+"] Object, ["+Eexcel_HashCheck(PropertyName2Capture)+" = "+CapturedPro+"].");
    		}
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
			TData= ETestData_GetData(ColumnName);
		}
		else
		{
			TData=CheckData;
		}
		return TData;
 	}
 	public void E_Alert_Accept() throws Exception
 	{
 		driver.switchTo().alert().accept();
 		System.out.println("\nAccept Alert.");
 	}
 	public void E_Alert_dismiss() throws Exception
 	{
 		driver.switchTo().alert().dismiss();
 		System.out.println("\nDismiss Alert.");
 		
 	}
 	public void E_Alert_GetText() throws Exception //NOT_ADDED_GUI
 	{
 		// Get a handle to the open alert, prompt or confirmation
 		Alert alert = driver.switchTo().alert();
 		// Get the text of the alert or prompt
 		System.out.println("\nAlert Text is :"+alert.getText());  
 	}
 	public void E_Switch2tab2() throws Exception //NOT_ADDED_GUI
 	{
 	    tabs2 = new ArrayList<String> (driver.getWindowHandles());
 	    driver.switchTo().window(tabs2.get(1));
 	    System.out.println("\nSwitched to Tab 2.");
 	}
 	public void E_Switch_back2maintab() throws Exception //NOT_ADDED_GUI
 	{
 		driver.close();
 	    driver.switchTo().window(tabs2.get(0));
 	    tabs2=null;
 	    System.out.println("\nSwitched Back to Main Tab.");
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
 	public void E_Switch_Back2Main_Frame() throws Exception
 	{
 		driver.switchTo().defaultContent();
 		System.out.println("\nSwitched Back to Main Frame.");
 		EReport_UpdateHTMLReport("","Switched back to Main Frame.","Done");
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
 	public void E_Browser_GetURL(String ColumnName) throws Exception
 	{
 		ETestData_SetData(ColumnName, driver.getCurrentUrl());
 		System.out.println("\nCurrent URL Captured : "+driver.getCurrentUrl());
 		EReport_UpdateHTMLReport("","Stored URL : "+driver.getCurrentUrl()+" in "+ColumnName+" Column.","Done");
 	}
 	public void E_Browser_Back() throws Exception
 	{
 		driver.navigate().back();
 		System.out.println("\nBrowser Back Operation.");
 		EReport_UpdateHTMLReport("","Back to previous page.","Done");
 	}
 	public void E_Browser_refresh() throws Exception
 	{
 		driver.navigate().refresh();
 		System.out.println("\nPage Re-Freshed.");
 		EReport_UpdateHTMLReport("","Refresh page.","Done");
 	}
 	public void E_Browser_forward() throws Exception
 	{
 	 	driver.navigate().forward();
 		System.out.println("\nBrowser Forward Operation.");
 		EReport_UpdateHTMLReport("","Forward to next page.","Done");
 	}

}