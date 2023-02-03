package dao;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.AccountDAO;
import com.revature.dao.AccountPostgres;
import com.revature.models.Account;
import com.revature.util.ConnectionUtil;

public class AccountDAOTest {
	public static AccountDAO accountDAO = new AccountPostgres();
	
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
	
//	@Test
//	public void getAccountById() {
//		assertEquals(new Account(1, "CHECKING", 2345.67, "ACTIVE"), accountDAO.getAccountById(1));
//	}
	
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
