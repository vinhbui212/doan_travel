package doan.vinhbui.service;

import doan.vinhbui.dto.TourDTO;
import doan.vinhbui.dto.WishlistDTO;
import doan.vinhbui.model.Wishlist;
import jakarta.transaction.Transactional;

import java.util.List;

public interface WishlistService {
    Wishlist addToWishlist(String token,Long tourID) throws IllegalAccessException;
    @Transactional
    void delete(String token, Long productID);
    List<WishlistDTO> getWishlist(String token);
}
