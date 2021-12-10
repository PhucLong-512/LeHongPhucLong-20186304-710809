package controller;

import java.util.List;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;


public class BaseController {
    

    public CartMedia checkMediaInCart(Media media){
        return Cart.getCart().checkMediaInCart(media);
    }

    public List getListCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
