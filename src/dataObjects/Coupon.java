package dataObjects;

import java.sql.Date;

//import java.util.Date;

public class Coupon {
	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String Message;
	private double price;
	private String image;
	
	/**
	 * default Constructor of Coupon class
	 */
	public Coupon(){
		
	}
	/**
	 * This method returns the id of the Coupon
	 * @return id
	 */
	public long getId() {
		return id;
	}
	/**
	 * This method set the id of the Coupon
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * This method returns the title of the Coupon
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * This method set the title of the Coupon
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * This method returns the startDate of the Coupon
	 * @return startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * This method set the startDate of the Coupon
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * This method returns the endDate of the Coupon
	 * @return endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * This method set the endDate of the Coupon
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * This method returns the amount of the Coupon
	 * @return amount
	 */
	public int getAmount() {
		return amount;
	}
	/**
	 * This method set the amount of the Coupon
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	/**
	 * This method returns the type of the Coupon
	 * @return type
	 */
	public CouponType getType() {
		return type;
	}
	/**
	 * This method set the type of the Coupon
	 * @param type
	 */
	public void setType(CouponType type) {
		this.type = type;
	}
	/**
	 * This method returns the Message of the Coupon
	 * @return Message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * This method set the message of the Coupon
	 * @param message
	 */
	public void setMessage(String message) {
		Message = message;
	}
	/**
	 * This method returns the price of the Coupon
	 * @return price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * This method set the price of the Coupon
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * This method returns the image of the Coupon
	 * @return image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * This method set the image of the Coupon
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/** 
	 * This method display all the fields of Coupon object
	 * @return String
	 */
	@Override
	public String toString() {
		return "\n Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", type=" + type + ", Message=" + Message + ", price=" + price + ", image="
				+ image + "]";
	}
}
