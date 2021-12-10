package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;


public class PlaceOrderController extends BaseController{


    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }


    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }


    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }


    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    

    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        //Nguyen Hoang Anh - 20180010
    	if(!validateName(info.get("name"))){
            throw new InterruptedException("Name is invalid");
        }
        if(!validateAddress(info.get("address"))){
            throw new InterruptedException("Address is invalid");
        }
        if(!validatePhoneNumber(info.get("phone"))){
            throw new InterruptedException("Phone number is invalid");
        }

        //Neu khach hang lua chon Place Rush Order thi tien hanh kiem tra xem du dieu kien khong
        if(info.get("rushOrder") == "true") {
            if(!validateProvince(info.get("province"))) {
                throw new InterruptedException("Province does not support rush order");
            }
        }
    }
    
    public boolean validatePhoneNumber(String phoneNumber) {
    	//Nguyen Hoang Anh - 20180010
    	// TODO: your work
    	
    	//check the phone number has 10 digits
    	if(phoneNumber.length() != 10) return false;
    	
    	//check the phone number start with 0
    	if (!phoneNumber.startsWith("0")) return false;
    	
    	//check phone number contains only number
    	try {
    		Integer.parseInt(phoneNumber);
    	}
    	catch (NumberFormatException e) {
    		return false;
    	}
    	return true;
    }
    
    public boolean validateName(String name) {
    	//Nguyen Hoang Anh - 20180010
    	// TODO: your work
    	//check not null
    	if (name == null) return false;
    	
    	//check only letter
    	Pattern p = Pattern.compile("[^A-Za-z ]");
    	Matcher m = p.matcher(name);
    	
    	boolean check = m.find();
    	if (check == true) return false;
    	return true;
    }
    
    public boolean validateAddress(String address) {
    	//Nguyen Hoang Anh - 20180010
    	// TODO: your work
    	//check not null
    	if (address == null) return false;
    	
    	//check no special char
    	
    	Pattern p = Pattern.compile("[^A-Za-z0-9 ,]");
    	Matcher m = p.matcher(address);
    	
    	boolean check = m.find();
    	if (check == true) return false;
    	return true;
    }


    public boolean validateProvince(String province){
        //Nguyen Hoang Anh - 20180010
        // Dia diem dao hang khong duoc de trong
        if(province == null){
            return false;
        }

        //Chi giao hang nhanh o Ha Noi, Da Nang va Ho Chi Minh
        if(province.equals("Hà Nội") || province.equals("Đà Nẵng") || province.equals("Hồ Chí Minh")) {
            return true;
        }
        return false;
    }
    


    public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
