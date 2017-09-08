package client;

import java.util.concurrent.Executors;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import daily_task.DailyCouponExpirationTask;
import db_services.CouponClientFacade;
import db_services.connection_pool.ConnectionPool;


//Singleton
public class CouponSystem {
	private static CouponSystem instance = null;
	private DailyCouponExpirationTask dailyTask = null;
	private ScheduledExecutorService executor = null; 
	private ConnectionPool connPoolInstance = null;
	/**
	 * private Constructor
	 */
	private CouponSystem(){
		connPoolInstance = ConnectionPool.getInstance();
		initialiseDailyTaskThread();
	}
	
	/**
	 * This method initialises the CouponSystem object if it was not initialised previously
	 * @return single instance of CouponSystem class
	 */
	public static synchronized CouponSystem getInstance(){
		if (instance == null) {
			instance = new CouponSystem ();
		}

		return instance;	
	}
	
	private void initialiseDailyTaskThread(){
		dailyTask = new DailyCouponExpirationTask();
		executor = Executors.newScheduledThreadPool(1); // Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
		executor.scheduleAtFixedRate(dailyTask, 1, 24, TimeUnit.HOURS); // dailyTask is our Runnable, 1 is hour to delay, 24-hours is interval for repetition
	}
	
	//Factory
	/**
	 * This method returns a subClass facade according to the given clientType
	 * @param name
	 * @param password
	 * @param clientType
	 * @return AdminFacade or CompanyFacade or CustomerFacade or null
	 */
	public CouponClientFacade login(String name, String password, ClientType clientType){
		switch(clientType){
			case ADMIN : 
				AdminFacade temp_admin = new AdminFacade(); // creating new object adminfacade because according to the book login function has to be inside the facade class
				return temp_admin.login(name, password, clientType); // the function login has logic inside of her, and if the name and password belong to admin then new AdminFacade object will be returned otherwise null returned
			case COMPANY :
				CompanyFacade temp_company = new CompanyFacade();
				return temp_company.login(name, password, clientType);//we are using temp because we want to use login function, and we cant do it without creating a temp that will contain the companyfacade
			case CUSTOMER : 
				CustomerFacade temp_customer = new CustomerFacade();
				return temp_customer.login(name, password, clientType);
					
			default: return null;		
		}
	}
	
	/**
	 * This method closes ConnectionPool and stops the dailyTask
	 * @see stopTask {@link #stopTask}
	 * @see closeAllConnections {@link #closeAllConnections}
	 */
	public void shutDown(){
		executor.shutdown();	// close the thread executor
		dailyTask.stopTask();	// interrupt the thread --- not sure that we need it
		connPoolInstance.closeAllConnections(); 
	}
}
