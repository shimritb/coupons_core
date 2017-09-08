package daily_task;


import java.util.Date;
import db_services.db_dao.CouponDBDAO;


public class DailyCouponExpirationTask implements Runnable {
	private CouponDBDAO couponDbDao = null;
	
	/**
	 * default Constructor 
	 */
	public DailyCouponExpirationTask() {
		couponDbDao = new CouponDBDAO();
	}
	
	/**
	 * This method runs the task that delete all expired Coupons
	 * @see deleteAllExpiredCoupon {{@link #deleteAllExpiredCoupon}
	 */
	@Override
	public void run() {
		System.out.println("Daily Task: looking for expired coupons");
		Date currentDate = new Date();
		couponDbDao.deleteAllExpiredCoupon(currentDate);
	}
	
	/**
	 * this method terminates the thread that operates the dailyTask
	 */
	public void stopTask(){
		if(Thread.currentThread().isAlive()) 
			Thread.currentThread().interrupt(); 
	}

}
