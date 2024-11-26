package doan.vinhbui.service.impl;

import doan.vinhbui.config.JwtService;
import doan.vinhbui.dto.WishlistDTO;
import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Tour;
import doan.vinhbui.model.User;
import doan.vinhbui.model.Wishlist;
import doan.vinhbui.repository.CustomerRepository;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.repository.WishlistRepository;
import doan.vinhbui.service.WishlistService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final TourRepository tourRepository;
    private  final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);
    @Transactional
    public Customer getCustomer(String token){
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }
    @Override
    public Wishlist addToWishlist(String token, Long tourID) throws IllegalAccessException {
        Customer customer= getCustomer(token);
        if(wishlistRepository.existsByCustomerAndTour_Id(customer,tourID)){
            throw new IllegalStateException("Tour has already in the cart");
        }
        else{
            Tour tour = tourRepository.findById(tourID).orElseThrow();
            if(tour == null){
                throw new IllegalAccessException("Product not exist");

            }else{
                Wishlist wishlist = new Wishlist();
                wishlist.setTour(tour);
                wishlist.setCustomer(customer);
                return wishlistRepository.save(wishlist);
            }
        }
    }


    @Transactional
    @Override
    public void delete(String token, Long productID) {

        Customer customer = getCustomer(token);
        if(wishlistRepository.existsByCustomerAndTour_Id(customer,productID)){
            wishlistRepository.deleteByCustomerAndTour_Id(customer, productID);
        }else{
            // Product not in the cart
            throw new IllegalStateException("Tour is not in the wishlist");
        }
    }

    @Override
    public List<WishlistDTO> getWishlist(String token) {
        Customer user = getCustomer(token);
        logger.info(String.valueOf(user.getId()));
        List<Wishlist> wishlists = wishlistRepository.findByCustomer_Id(user.getId());
        wishlists.forEach(wishlist -> logger.info("Wishlist Detail: {}", wishlist.getDetail()));
        if (wishlists.isEmpty()) {
            throw new RuntimeException("No wishlist found for the user.");
        }

        return wishlists.stream().map(wishlist -> {
            WishlistDTO dto = new WishlistDTO();
            dto.setTour_id(wishlist.getTour().getId());
            dto.setImg_Url(wishlist.getImgUrl());
            dto.setDetails(wishlist.getDetail());
            return dto;
        }).collect(Collectors.toList());
    }


}
