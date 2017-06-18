package com.accenture;

import com.accenture.entity.CartItem;
import com.accenture.entity.Order;
import com.accenture.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the different methods to process the requested actions
 */
@Component
public class CartService {

    private CartItemRepository cartItemRepository;
    private RestTemplate restTemplate;
    private DiscoveryClient discoveryClient;

    /**
     * Default constructor to allow injecting the repository and restTemplate classes as dependencies.
     * @param cartItemRepository the repository for the shopping carts.
     * @param restTemplate the RestTemplate to communicate with order application.
     * @param discoveryClient the client used to request instances of the microservices by its names
     */
    @Autowired
    public CartService(CartItemRepository cartItemRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.cartItemRepository = cartItemRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    /**
     * Retrieves the user cart
     * @param accountId the id for identifying the owner of the shopping cart.
     * @return the list of all items stored in the user's shopping cart
     */
    public List<CartItem> retrieveCart(Integer accountId) {
        return cartItemRepository.findAllByCartItemAccountId(accountId);
    }

    /**
     * Adds an item to the requested account cart
     * @param productId the id for identifying the product
     * @param cartItemAccountId the id for saving the cart item in the repository
     */
    public void saveCartItem(Integer productId, Integer cartItemAccountId) {

        Object productResponse = getProductDetails(productId);
        String productName = (String)getValueFromResponse(productResponse, "productName");
        Double productPrice = (Double)getValueFromResponse(productResponse, "productPrice");

        CartItem c = cartItemRepository.findCartItemByCartItemProductId(productId);
        if (c == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCartItemProductId(productId);
            cartItem.setCartItemAccountId(cartItemAccountId);
            cartItem.setCartItemName(productName);
            cartItem.setCartItemCost(new BigDecimal(productPrice));
            cartItem.setCartItemQuantity(1);
            cartItemRepository.save(cartItem);
        }
        if (c != null && c.getCartItemAccountId().equals(cartItemAccountId)) {
            CartItem cartItem = cartItemRepository.findCartItemByCartItemProductId(productId);
            cartItem.setCartItemQuantity(cartItem.getCartItemQuantity() + 1);
            cartItemRepository.save(cartItem);
        }
    }

    /**
     * Deletes the cart for the requested account
     * @param cartItemAccountId the id of the cart items to be deleted from the repository
     */
    @Transactional
    public void deleteCart(Integer cartItemAccountId) {
        cartItemRepository.deleteAllByCartItemAccountId(cartItemAccountId);
    }

    /**
     * Confirms the cart items of one account
     * @param accountId the id of the account
     * @param authorizationToken the token to be authenticated in the order application
     * @return a message with the order details
     */
    @Transactional
    public String confirmCart(Integer accountId, String authorizationToken) {

        String accountResponse = getAccountDetails(accountId, authorizationToken);
        String accountName = getValue(accountResponse, "accountName");
        String accountSurname = getValue(accountResponse, "accountSurname");
        String accountAddress = getValue(accountResponse, "accountAddress");

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        StringBuilder message = new StringBuilder();

        message.append("Cart confirmed with the following details: \n");
        message.append("\t" + "-> Date: " + date + "\n");
        message.append("\t" + "-> Addressee: " + accountName + " " + accountSurname + "\n");
        message.append("\t" + "-> Address: " + accountAddress + "\n");
        message.append("\t" + "-> Products: \n");

        List<CartItem> cartItems = cartItemRepository.findAllByCartItemAccountId(accountId);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Integer productId = cartItem.getCartItemProductId();
            String cartItemName = cartItem.getCartItemName();
            BigDecimal cartItemPrice = cartItem.getCartItemPrice();
            BigDecimal cartItemTotal = cartItemPrice.multiply(new BigDecimal(cartItem.getCartItemQuantity()));
            total = total.add(cartItemTotal);

            message.append("\t\t" + cartItem.getCartItemQuantity() + "x " + cartItemName + " (#ref " + productId + "): "
                    + cartItemPrice + " euro(s) \n");
        }
        message.append("\t" + "-> Total cost: " + total + " euro(s) \n");

        Order order = new Order();
        order.setAccountId(accountId);
        order.setAddressee(accountName + " " + accountSurname);
        order.setAddress(accountAddress);
        order.setCost(total);
        order.setDate(date);
        restTemplate.postForObject("http://order-microservice/orders/", order, Order.class);

        cartItemRepository.deleteAllByCartItemAccountId(accountId);
        return message.toString();
    }

    /**
     * Gets an object containing the requested product details from the catalogue microservice
     * @param productId the id of the requested product
     * @return an object containing the product
     */
    public Object getProductDetails(Integer productId) {

        ServiceInstance serviceInstance = discoveryClient.getInstances("catalogue-microservice")
                .stream().findFirst().orElseThrow(() -> new RuntimeException("catalogue-microservice not found."));

        String url = serviceInstance.getUri().toString() + "/catalogue/products/id=";

        return restTemplate.getForObject(url + productId, Object.class);
    }

    /**
     * Gets the value of each product field
     * @param objectResponse the generic object containing the product
     * @param value the value to be returned
     * @return the value of the requested field
     */
    public Object getValueFromResponse(Object objectResponse, String value) {
        return ((HashMap)objectResponse).get(value);
    }

    /**
     * Gets an object containing the requested account from the account microservice
     * @param accountId the id of the requested account
     * @param authorizationToken the token to be authenticated in the account application
     * @return
     */
    public String getAccountDetails(Integer accountId, String authorizationToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "http://account-microservice/accounts" + "/id=" + accountId, HttpMethod.GET, entity, Object.class);

        return response.getBody().toString();
    }

    /**
     * Gets the value of each account field
     * @param objectResponse the generic object containing the product
     * @param value the value to be returned
     * @return the value of the requested field
     */
    public String getValue(String objectResponse, String value) {

        Map<String,String> result = new HashMap<>();
        String[] elements = objectResponse.split(", ");
        for(String s : elements) {
            String[] keyValue = s.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result.get(value);
    }
}