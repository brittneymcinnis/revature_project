package com.revature.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.models.Transaction;
import com.revature.models.Transfer;
import com.revature.services.AccountService;
import com.revature.services.CustomerAccountService;
import com.revature.services.CustomerService;
import com.revature.services.EmployeeService;
import com.revature.services.TransactionService;
import com.revature.services.TransferService;

public class Driver {

	private static AccountService accServ = new AccountService();
	private static CustomerService custServ = new CustomerService();
	private static EmployeeService emplServ = new EmployeeService();
	private static TransactionService transactServ = new TransactionService();
	private static TransferService transferServ = new TransferService();
	private static CustomerAccountService custAccServ = new CustomerAccountService();
	
	private static Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int choice,subMenuChoice, subChoice, accountNo = 0, customerNo = 0, transactionNo = 0;
		boolean newAccount;
		String firstName, lastName, username, password, email;
		String accountType = null;
		Customer customer = null;
		Employee employee = null;
		Transfer transfer = null;
		Account account = null, account2 = null;
		double amount = 0.0, balance = 0.0;
		List <Account> customerAccounts = new ArrayList <> ();
		List <Transaction> transactions = new ArrayList <>();
		List <Transfer> transfers = new ArrayList <>();
		int decision;
		
		// display Main Menu
		do {
			System.out.println("                Main Menu");
			System.out.println("-----------------------------------------------");
			System.out.println("Please Enter the Number from the Options Below:\n");
			System.out.println("1. Customer Login\n2. Employee Login\n3. Create New CustomerAccount\n4. Exit\n");
			
			choice = Integer.parseInt(sc.nextLine());
			
			switch (choice) {
				case 1: 
					System.out.println("Please enter your username:");
					username = sc.nextLine();
					System.out.println("Please enter your password:");
					password = sc.nextLine();
					customer = custServ.authenticateCustomer(username, password);
					if (!Objects.isNull(customer)) {
						
						do {
							System.out.println("                Menu");
							System.out.println("-----------------------------------------------");
							System.out.println("Print the Number from the Options Below:\n");
							System.out.println("1. Apply For New Account\n2. View Accounts\n3. View Account Balance");
							System.out.println("4. Make Withdrawal\n5. Make Deposit\n6. Post Funds to Transfer");
							System.out.println("7. Accept Transfer Funds\n8. Logout");
							subMenuChoice = Integer.parseInt(sc.nextLine());
							
							switch(subMenuChoice) {
								case 1:
									System.out.println("              Create New Account");
									System.out.println("--------------------------------------------------");
									System.out.println("Please Enter the Number for Type of Account You Would Like to Create:");
									System.out.println("1. Checking\n2. Savings\n3. Roth IRA\n4. Traditional IRA");
									subChoice = Integer.parseInt(sc.nextLine());
									
									switch(subChoice) {
										case 1:
											accountType = "CHECKING";
											break;
										case 2:
											accountType = "SAVINGS";
											break;
										case 3:
											accountType = "ROTH IRA";
											break;
										case 4:
											accountType = "TRADITIONAL IRA";
											break;
									}
									
									// create new account, create new customer_accounts table record
									newAccount = accServ.createAccount(new Account(accountType), customer.getUserId());
									if (newAccount) {
										
										log.info("Account Created -- Account Activation Pending Manager Approval");
									} else {
				
										log.error("Error Creating Account");
									}
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
								case 2:
									customerAccounts = custAccServ.getCustomerAccounts(customer);
									
									if (customerAccounts.size() > 0) {
										System.out.println("\t\tAccount Details");
										System.out.println("----------------------------------------------------------");
										System.out.println("Acct. #\tAcct. Type\tAcct. Balance\tAcct. Status");
										System.out.println("----------------------------------------------------------");
							
										for (Account acct : customerAccounts) {
											System.out.println(acct.getAccountId() + "\t" + acct.getType() + "\t" + acct.getBalance() + "\t\t" + acct.getStatus() + "\n");
										}
									} else {
										System.out.println("There are no Bank Accounts Associated with this User Account");
									}
									
									customerAccounts.clear();
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 3:
									
									System.out.println("Enter the Account #:");
									accountNo = Integer.parseInt(sc.nextLine());
									account = custAccServ.getCustomerAccountById(customer, accountNo);
									
									if (Objects.isNull(account)) {
										System.out.println("Account Not Found");
									} else {
										System.out.println("Acct. #\tAcct. balance");
										System.out.println("-----------------------------");
										System.out.println(account.getAccountId() + "\t" + account.getBalance() + "\n");
										account = null;
									}
									
									accountNo = 0;
									account= null;
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 4:
								
									System.out.println("Please Enter the Account # to Withdraw:");
									accountNo = Integer.parseInt(sc.nextLine());
									
									account = custAccServ.getCustomerAccountById(customer, accountNo);
									
									
									if (Objects.isNull(account)) {
										System.out.println("Account Not Found");
									} else {
										if (account.getStatus().equals("ACTIVE")) {
											balance = account.getBalance();
											do {
												System.out.println("Please Enter the Amount to Withdraw:");
												amount = Double.parseDouble(sc.nextLine());
												if (amount < 0) {
													System.out.println("Cannot Withdraw a Negative Amount");
												} else if (balance - amount < 0) {
													System.out.println("Insufficient Funds");
												}
											} while (amount < 0 || balance - amount < 0);
											
											
											balance -= amount;
											account.setBalance(balance);
											if (transactServ.makeWithdrawal(account, amount)) {
												log.info("$" + amount + " Successfully Withdrawn from Account # " + account.getAccountId());
												System.out.println("New Account Balance: $" + balance);
											} else {
												
												log.error("Error Withdrawing From Account");
											}
										} else {
											
											System.out.println("This Account is Not Active");
										}
									}
									
									account= null;
									balance = amount = 0.0;
									accountNo = 0;
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 5:
									// make deposit
									System.out.println("Please Enter the Account # to Deposit:");
									accountNo = Integer.parseInt(sc.nextLine());
									
									account = custAccServ.getCustomerAccountById(customer, accountNo);
									
									if (Objects.isNull(account)) {
										System.out.println("Account Not Found");
									} else {
										if (account.getStatus().equals("ACTIVE")) {
											balance = account.getBalance();
											do {
												System.out.println("Please Enter the Amount to Deposit:");
												amount = Double.parseDouble(sc.nextLine());
												if (amount < 0) {
													System.out.println("Cannot Withdraw a Negative Amount");
												} 
											} while (amount < 0);
											
											balance += amount;
											account.setBalance(balance);
											if (transactServ.makeDeposit(account, amount)) {
												log.info("$" + amount + " Successfully Deposited to Account # " + account.getAccountId());
												System.out.println("New Account Balance: $" + balance);
											} else {
												
												log.error("There was an Error");
												
											}
										} else {
											System.out.println("This Account is Not Active");
										}
									}
									
									account= null;
									balance = amount = 0.0;
									accountNo = 0;
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 6:
									// post transfer funds
									System.out.println("Please Enter the Account # to Transfer Funds From:");
									accountNo = Integer.parseInt(sc.nextLine());
									
									account = custAccServ.getCustomerAccountById(customer, accountNo);
									
									accountNo = 0;
									
									if (Objects.isNull(account)) {
										System.out.println("Account Not Found");
									} else {
										System.out.println("Please Enter the Account # to Transfer Funds To:");
										accountNo = Integer.parseInt(sc.nextLine());
										
										account2 = accServ.getAccountById(accountNo);
										
										// check that account exists
										if (Objects.isNull(account2)) {
											System.out.println("Account Not Found");
										} else {
											// check that account is active
											if (account2.getStatus().equals("ACTIVE")) {
												
												balance = account.getBalance();
												do {
													System.out.println("Please Enter the Amount to Transfer:");
													amount = Double.parseDouble(sc.nextLine());
													if (amount < 0) {
														System.out.println("Cannot Transfer a Negative Amount");
													} else if (balance - amount < 0) {
														System.out.println("Insufficient Funds");
													}
												} while (amount < 0 || balance - amount < 0);
												
												if (transferServ.createTransfer(account, account2, amount)) {
													System.out.println("Transfer Initiated -- Awaiting Recipient Approval");
												}
											} else {
												System.out.println("This Account is Not Active");
											}
										}
									}
									
									account = null;
									account2 = null;
									accountNo = 0;
									amount = balance = 0.0;
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 7:
									// accept transfer funds
									
									transfers = transferServ.getCustomerPendingTransfers(customer);
									
									if (transfers.size() > 0) {
										System.out.print("\nAccounts Waiting to Accept $$ Transfer:\n\n");
										System.out.println("Tranact #\tTransact. Amount\tAcct. #\tAcct. Type\tAcct. Balance");
										System.out.println("---------------------------------------------------------------------------");
							
										for (Transfer trfs : transfers) {
											System.out.println(trfs.getTransactionId() + "\t\t" + trfs.getAmount() + "\t\t\t" + trfs.getReceivingAccount().getAccountId() + "\t" + trfs.getReceivingAccount().getType() + "\t\t" + trfs.getReceivingAccount().getBalance() + "\n");
										}
										
										System.out.println("Enter the Transaction # to Accept/Deny $$ Transfer");
										transactionNo = Integer.parseInt(sc.nextLine());
										
										transfer = transferServ.getTransferById(transactionNo);
										
										System.out.println("Enter 1 to Accept the Transfer or 2 to Deny the Transfer:");
										decision = Integer.parseInt(sc.nextLine());
										
										if (decision == 1) {
											// accept transfer
											
											if (transfer.getAccount().getBalance() - transfer.getAmount() < 0) {
												transfer.setMessage("TRANSFER CANCELLED: INSUFFICIENT FUNDS");
												transferServ.denyTransfer(transfer);
												System.out.println("Transfer Cancelled: Insufficient Funds from Donor Account");
											} else {
											
												if (transferServ.acceptTransfer(transfer, (transfer.getReceivingAccount().getBalance() + transfer.getAmount()), (transfer.getAccount().getBalance() - transfer.getAmount()))) {
													System.out.println("Transfer Successfully Processed");
												} else {
													
													log.error("Error Processing Transfer");
												}	
											}
										} else {
											// deny transfer
											transfer.setMessage("TRANSFER CANCELLED BY RECIPIENT");
											transferServ.denyTransfer(transfer);
											System.out.println("Transfer Cancelled.");
										}
									} else {
										System.out.println("There are no Pending Tranfers Associated with this Account");
									}
									
									amount = balance = 0.0;
									transactionNo =0;
									transfers.clear();
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
							}
							
						} while (subMenuChoice != 8);
						
						subMenuChoice = 0;
						
					} else {
						
						System.out.println("Incorrect credentials.");
						
					}
					
					username = password = null;
					customer = null;
					
					break;
				case 2:
					System.out.println("Please Enter Your Username:");
					username = sc.nextLine();
					System.out.println("Please Enter Your Password:");
					password = sc.nextLine();
					employee = emplServ.authenticateEmployee(username, password);
					if (!Objects.isNull(employee)) {
						
						do {
							System.out.println("                Menu");
							System.out.println("-----------------------------------------------");
							System.out.println("Print the Number from the Options Below:\n");
							System.out.println("1. Approve/Deny Customer Account\n2. View Customer Accounts\n3. View Transactions\n4. Logout");
							subMenuChoice = Integer.parseInt(sc.nextLine());
							
							switch (subMenuChoice) {
								case 1: 
									System.out.println("Accounts Pending Manager Approval:");
									System.out.println("-----------------------------------------------");
									System.out.println("Acct #\tAcct. Type\t");
									System.out.println("-----------------------------------------------");
									customerAccounts = accServ.getPendingAccounts();
									if (customerAccounts.size() > 0) {
										for (Account acct: customerAccounts) {
											System.out.println(acct.getAccountId() + "\t" + acct.getType());
										}
									}
									
									System.out.println("Please Enter the account # to Review");
									accountNo = Integer.parseInt(sc.nextLine());
									
									account = accServ.getAccountById(accountNo);
									if (!Objects.isNull(account)) {
										
										System.out.println("\nAcct #\tAcct. Type\t");
										System.out.println("-----------------------------------------------");
										System.out.println(account.getAccountId() + "\t" + account.getType());
										System.out.println("\nEnter 1 to Approve or 2 to Deny this Account");
										decision = Integer.parseInt(sc.nextLine());
										if (decision == 1) {
											account.setStatus("ACTIVE");
											accServ.updateAccount(account);
										} else {
											account.setStatus("DENIED");
											accServ.updateAccount(account);
										}
										
										System.out.println("\nAccount updated.");
										
									} else {
										
										System.out.println("\nAccount Not Found");
										
									}
									
									account = null;
									customerAccounts.clear();
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
									
								case 2: 
									System.out.println("Please enter the Customer ID to View Customer Accounts");
									customerNo = Integer.parseInt(sc.nextLine());
									customer = custServ.getCustomerById(customerNo);
									
									if (!Objects.isNull(customer)) {
										customerAccounts = custAccServ.getCustomerAccounts(customer);
										
										if (customerAccounts.size() > 0) {
											
											System.out.println("\t\tAccount Details");
											System.out.println("----------------------------------------------------------");
											System.out.println("Acct. #\tAcct. Type\tAcct. Balance\tAcct. Status");
											System.out.println("----------------------------------------------------------");
								
											for (Account acct : customerAccounts) {
												System.out.println(acct.getAccountId() + "\t" + acct.getType() + "\t" + acct.getBalance() + "\t\t" + acct.getStatus() + "\n");
											}
											
										} else {
											System.out.println("No Accounts Associated with this Customer");
										}
									} else {
										System.out.println("Customer not Found");
									}
									
									customerAccounts.clear();
									customer = null;
									customerNo = 0;
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
								case 3: 
									transactions = transactServ.getTransactions();
									if (transactions.size() > 0) {
										
										System.out.println("Transact. #\tAccount #\tAmount\t\tMessage\t\tDate");
										System.out.println("-----------------------------------------------------------------------------------");
										for (Transaction transact : transactions) {
											System.out.println(transact.getTransactionId() + "\t\t" + transact.getAccount().getAccountId() + "\t\t" + transact.getAmount() + "\t\t" + transact.getMessage() + "\t\t" + transact.getTransactionCreateDateTime());
										}
										
									} else {
										
										System.out.println("No transactions listed");
										
									}
									
									transactions.clear();
									
									System.out.println("Press Enter to Return to the Menu");
									sc.nextLine();
									
									break;
							}
							
						} while (subMenuChoice != 4);
					} else {
						
						System.out.println("Incorrect credentials.");
						
					}
					subMenuChoice = 0;
					username = password = null;
					
					break;
				case 3: 
					// create new customer account
					System.out.println("Please Enter Your First Name:");
					firstName = sc.nextLine();
					System.out.println("Please Enter Your Last Name:");
					lastName = sc.nextLine();
					System.out.println("Please Enter Your Username:");
					username = sc.nextLine();
					System.out.println("Please Enter Your Password:");
					password = sc.nextLine();
					System.out.println("Please Enter Your Email:");
					email = sc.nextLine();
					
					Customer newCustomer = new Customer(firstName, lastName, username, password, email);
					
					if (custServ.createCustomer(newCustomer)) {
						log.info("New Customer Account Created.");
					} else {
						log.error("Error Creating New Account");
					}
					
					System.out.println("Press Enter to Return to the Menu");
					sc.nextLine();
					
					username = password = firstName = lastName = email = null;
					
					break;
			}
			
		} while (choice != 4);
		
		sc.close();
	}
}
