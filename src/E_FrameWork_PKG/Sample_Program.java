package E_FrameWork_PKG;

import E_FrameWork_PKG.E_FrameWork;
public class Sample_Program {
	public static void main(String args[]) throws Exception
	{
		FUNCTION_NAME1();	
	}
	public static void FUNCTION_NAME1() throws Exception
	{
		//DECLARATION SECTION
			
			//CRAETED OBJECT OF fRAMEwORK CLASS
				E_FrameWork driv = new E_FrameWork();
			//CALL OBJECT FILE(REPOSITORY)
				driv.E_CallWebObjectsFile("PHP_Travels");
			//HTML REPORT NAME	
				driv.EReport_CreateHTMLReport("PHP_Travel");	
			//TEST DATA FILE NAME&PATH
				driv.ETestData_File("C:\\E_FrameWork\\TestDataFiles\\PHP_Travels.xlsx", "Sheet1");

		//CODE SECTION
				driv.E_LoadURL("#URL");
				driv.E_WaitForPageLoad();
				driv.E_Click("HOTELS_TAB.XPATH");
				driv.E_WaitUntilElementDisplay("Location_txt.ID");
				driv.E_Write("Location_txt.ID", "#Location");
				driv.E_Click("TOURS_TAB.XPATH");
				driv.E_WaitUntilElementDisplay("Search_By_Listing.XPATH");
				driv.E_Write("Search_By_Listing.XPATH", "#Tours");
				
		//CLOSING SECTION
				driv.Efile_CloseALL();
	}
}

