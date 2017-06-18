package com.accenture;

import com.accenture.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Handles the different web requests to routes starting with /cart.
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    private CartService cartService;

    /**
     * Default constructor to allow injecting the service class as dependency.
     * @param cartService dependency class.
     */
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Maps the resource's route (/cart) to the getCart() method.
     * Gets the user's cart by calling the appropiate function in service class.
     * @return ResponseEntity with the content of the user's shopping cart.
     */
    @GetMapping
    public ResponseEntity<?> getCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        List<CartItem> cart = cartService.retrieveCart(principalId);
        if (cart.isEmpty()) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<>(authentication.getName() + ", your shopping cart is empty.\n"
                    + "You can add some awesome products from our catalogue!", HttpStatus.OK);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/cart) to the addItemCart() method.
     * Adds an item to the user's cart by calling the appropiate function in service class.
     * @param productId the id of the product to be added to the account cart.
     * @return ResponseEntity with feedback message.
     */
    @PostMapping
    public ResponseEntity<?> addCartItem(@RequestBody Integer productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        cartService.saveCartItem(productId, principalId);
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>("Product added to your shopping cart.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/cart) to the getCart() method.
     * Removes the user's cart by calling the appropiate function in service class.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping
    public ResponseEntity<?> removeCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        List<CartItem> cart = cartService.retrieveCart(principalId);
        if (cart.isEmpty()) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<>(authentication.getName() + ", your shopping cart is already empty.", HttpStatus.OK);
        }
        cartService.deleteCart(principalId);
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(authentication.getName() + ", your shopping cart has been emptied successfully.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/cart/confirm) to the confirmCart() method.
     * Confirms the user's cart and creates a new order by calling the appropiate function in service class.
     * @param httpServletRequest the request itself.
     * @return ResponseEntity with feedback message.
     */
    @PostMapping ("/confirm")
    public ResponseEntity<?> confirmCart(HttpServletRequest httpServletRequest) {

        String authorizationToken = httpServletRequest.getHeader("Authorization");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        List<CartItem> cart = cartService.retrieveCart(principalId);

        SecurityContextHolder.getContext().setAuthentication(null);

        if (cart.isEmpty())
            return new ResponseEntity<>(authentication.getName() + ", your shopping cart is empty.\n" +
                    "Please, add some products to your shopping cart in order to make an order.", HttpStatus.OK);

        String confirmationMessage = cartService.confirmCart(principalId, authorizationToken);

        if (confirmationMessage == null)
            return new ResponseEntity<>(authentication.getName() +
                    ", your shopping cart is empty and cannot be confirmed.", HttpStatus.OK);

        return new ResponseEntity<>(confirmationMessage, HttpStatus.OK);
    }
}