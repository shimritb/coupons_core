package application;

import client.AdminFacade;

import client.ClientType;
import client.CompanyFacade;
import client.CouponSystem;
import client.CustomerFacade;
import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.CouponType;
import dataObjects.Customer;
import db_services.db_dao.CompanyDBDAO;
import db_services.db_dao.CustomerDBDAO;


/**
* The Coupon:
*
* @author  shimrit breef ziskand
* @version 1.0
* @since   2017-01-01
*/

public class App {

	public static void main(String[] args) {
		CouponSystem couponSystem = CouponSystem.getInstance();
//		
//////-------------create company------------
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN")); // logged as admin because only admin can create company
		Company testCompany = new Company();
		
		testCompany.setCompName("cocoTum");
		testCompany.setEmail("cocotum@gmail.com");
		testCompany.setPassword("5522422");
		adminFacade.createCompany(testCompany);
		
////---------------remove company-----------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN")); // logged as admin because only admin can create company
//		Company testCompany = new Company();
//		testCompany.setCompName("tact");
//		testCompany.setId(8);
//		adminFacade.removeCompany(testCompany);

//----------------update company------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN")); // logged as admin because only admin can create company
//		Company testCompany = new Company();
//		testCompany.setCompName("shironim");
//		testCompany.setEmail("blaa@gmail.co.ill");
//		testCompany.setPassword("55588");
//		testCompany.setId(4);
//		adminFacade.updateCompany(testCompany);

//-----------------get company---------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		System.out.println(adminFacade.getCompany(0));
		
//-----------------get all companies-----------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		System.out.println(adminFacade.getAllCompanies());
		
//-----------------create customer-------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		Customer testCustomer = new Customer();
//		testCustomer.setId(7);
//		testCustomer.setCustName("david");
//		testCustomer.setPassword("sabon1");
//		adminFacade.createCustomer(testCustomer);


//////-----------------remove customer-------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		Customer testCustomer = new Customer();	
//		testCustomer.setCustName("raviv");
//		testCustomer.setId(6);
//		adminFacade.removeCustomer(testCustomer);
		
//-------------------update customer-------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));	
//		Customer testCustomer = new Customer();
//		testCustomer.setId(5);
//		testCustomer.setCustName("y"); // im giving a name only because of the check of ifCustNameExsits
//		testCustomer.setPassword("8889");
//		adminFacade.updateCustomer(testCustomer);
		
//-------------------get customer----------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		System.out.println(adminFacade.getCustomer(0));

//------------------get all customer-------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		System.out.println(adminFacade.getAllCustomer());
		
//-----------------create coupon----------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));
//		Coupon testCoupon = new Coupon();
//		
//		java.sql.Date startDate = java.sql.Date.valueOf( "2013-12-31" );
//		java.sql.Date endDate = java.sql.Date.valueOf( "2018-11-30" );
//		
////		testCoupon.setId(11);
//		testCoupon.setTitle("h88");
//		testCoupon.setStartDate(startDate);
//		testCoupon.setEndDate(endDate);
//		testCoupon.setAmount(5);
//		testCoupon.setType(CouponType.valueOf("RESTURANTS"));
//		testCoupon.setMessage("hytghk");
//		testCoupon.setPrice(30.2);
//		testCoupon.setImage("c://");
//		compFacade.createCoupon(testCoupon);
		
//-------------------remove coupon------------------------		
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));
//		Coupon testCoupon = new Coupon();
//		testCoupon.setTitle("8");
//		testCoupon.setId(10007);
//		compFacade.removeCoupon(testCoupon);
		
//------------------- update coupon---------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));
//		Coupon testCoupon = new Coupon();
//		java.sql.Date endDate = java.sql.Date.valueOf( "2019-11-30" );
//		testCoupon.setId(2);
//		testCoupon.setTitle("RESTURANTS");
//		testCoupon.setEndDate(endDate);
//		testCoupon.setPrice(100);
//		compFacade.updateCoupon(testCoupon);
		
//------------------- get coupon-----------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));
//		System.out.println(compFacade.getCoupon(0));
		
//------------------- get all coupons by of the connected company------------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("amdocs", "11113", ClientType.valueOf("COMPANY"));
//		System.out.println(compFacade.getCoupons());
//		
//------------------- get all coupons -the admin can see all the coupons exsits in the db------------------------
//		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));
//		System.out.println(adminFacade.getAllCoupon());

//-------------------get coupon by type------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));	
//		System.out.println(compFacade.getCouponByType(CouponType.valueOf("YY")));
		
////-------------------get Coupon By Price-----------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));	
//		System.out.println(compFacade.getCouponByPrice(222));
		
//---------------------get Coupon By Date---------------------
//		CompanyFacade compFacade = (CompanyFacade) couponSystem.login("nice", "55235", ClientType.valueOf("COMPANY"));
//		java.sql.Date endDate = java.sql.Date.valueOf( "2020-11-30" );
//		System.out.println(compFacade.getCouponByDate(endDate));
		
//---------------------purchase Coupon------------------------
//		CustomerFacade custFacade = (CustomerFacade) couponSystem.login("sharon", "shar2155", ClientType.valueOf("CUSTOMER"));
//		java.sql.Date endDate = java.sql.Date.valueOf( "2018-11-30" );
//		Coupon testCoupon = new Coupon();
//		testCoupon.setAmount(5);
//		testCoupon.setEndDate(endDate);
//		testCoupon.setId(1);
//		custFacade.purchaseCoupon(testCoupon);
		
//---------------------get all purchased coupons-------------------------
//		CustomerFacade custFacade = (CustomerFacade) couponSystem.login("sharon", "shar2155", ClientType.valueOf("CUSTOMER"));
//		System.out.println(custFacade.getAllPurchasedCoupons());
		
//---------------------get all purchased coupon by type-------------------
//		CustomerFacade custFacade = (CustomerFacade) couponSystem.login("sharon", "shar2155", ClientType.valueOf("CUSTOMER"));
//		CustomerFacade custFacade = (CustomerFacade) couponSystem.login("raviv", "8889", ClientType.valueOf("CUSTOMER"));
//		System.out.println(custFacade.getAllPurchasedCouponsByType(CouponType.valueOf("ELECTRICITY")));
		
////---------------------get all purchased coupon by price-----------------
//		CustomerFacade custFacade = (CustomerFacade) couponSystem.login("sharon", "shar2155", ClientType.valueOf("CUSTOMER"));
//		System.out.println(custFacade.getAllPurchasedCouponsByPrice(211));
		
	}

}
