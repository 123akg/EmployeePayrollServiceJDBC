package com.capgemini.employeepayrollservice.jdbc;

import java.util.Arrays;
import exception.PayrollSystemException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.capgemini.employeepayrollservice.jdbc.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	
	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEnteries() {
		EmployeePayrollData[] arrayOfEmps= {
				new EmployeePayrollData(1,"satyam", 50000.0),
				new EmployeePayrollData(2,"shivam", 51000.0),
				new EmployeePayrollData(3,"sundram", 45000.0)
		};
		EmployeePayrollService employeePayrollService;
		employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		long entries=employeePayrollService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3,entries);	
	}
	
	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> entries = employeePayrollService.readPayrollData(IOService.FILE_IO);
	}
	
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
	Assert.assertEquals(6,employeePayrollData.size());
	}
	
	//@Test /*UC3*/
	/*public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws PayrollSystemException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("satyam",20000.0);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("satyam");
		Assert.assertTrue(result);} */
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() 
			throws PayrollSystemException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("satyam", 300000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("satyam");
		Assert.assertTrue(result);
	}
}