package db_services;

import client.ClientType;

public interface CouponClientFacade {

	public CouponClientFacade login(String name,String password,ClientType clientType);
	
}
