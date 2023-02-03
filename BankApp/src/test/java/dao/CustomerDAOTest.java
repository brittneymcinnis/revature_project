package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.CustomerDAO;
import com.revature.dao.CustomerPostgres;
import com.revature.models.Customer;
import com.revature.util.ConnectionUtil;

public class CustomerDAOTest {

	public static CustomerDAO custDAO = new CustomerPostgres();
	
	@BeforeClass
	public static void setUp() {
		try (Connection c = ConnectionUtil.getH2Connection()){
			
			RunScript.execute(c, new FileReader("setup.sql"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getCustomerByIdTest() {
		assertEquals(new Customer(1, "Chris", "Napton", "cnapton0", "ddM6gCvf", "cnapton0@google.cn"), custDAO.getCustomerById(1));
	}
	
	@Test
	public void getCustomersByIDInvalidTest() {
		
		assertNull(custDAO.getCustomerById(100));
		
	}
	
	@Test
	public void createCustomerTest() {
		assertEquals(1, custDAO.createCustomer(new Customer("Pamela", "Beasley", "pbeasely0", "p4ssw0rd", "pbeasley123@gmail.com")));
	}
	
	@Test
	public void updateCustomerTest() {
		assertEquals(1, custDAO.updateCustomer(new Customer(1, "Christopher", "Napton", "cnapton0", "p4ssw0rd", "cnapton123@hotmail.com")));
	}
	
	@Test
	public void deleteCustomerTest() {
		assertEquals(1, custDAO.deleteCustomer(6));
	}
	
	@AfterClass
	public static void tearDown() {
		try (Connection c = ConnectionUtil.getH2Connection()){
			
			RunScript.execute(c, new FileReader("teardown.sql"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
