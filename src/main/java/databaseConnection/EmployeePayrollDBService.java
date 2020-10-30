package databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.capgemini.employeepayrollservice.jdbc.*;

public class EmployeePayrollDBService {
	
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	private EmployeePayrollDBService() {
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}
	
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
		String userName = "root";
		String password = "Abhi@123";
		Connection connection;
		System.out.println("connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("connection successful !!!! " + connection);
		return connection;
	}

	public List<EmployeePayrollData> readData() {
		String sql = "SELECT * FROM employee_payroll; ";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeePayrollList=this.getEmployeePayrollData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) {
		String sql = String.format("update employee_payroll set salary=%.2f where name='%s';", salary, name);
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeeParollList = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1,name);
			ResultSet resultSet=employeePayrollDataStatement.executeQuery();
			employeeParollList= this.getEmployeePayrollData(resultSet);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeParollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double Salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, Salary, startDate));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM employee_payroll WHERE name=?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}