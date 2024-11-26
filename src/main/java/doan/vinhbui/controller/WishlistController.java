package doan.vinhbui.controller;


import doan.vinhbui.dto.WishlistDTO;
import doan.vinhbui.model.Wishlist;
import doan.vinhbui.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }
    // API thêm sản phẩm vào wishlist
    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestHeader("Authorization") String authorizationHeader, @RequestParam long tourID) {
        try {
            String token= extractToken(authorizationHeader);
            Wishlist wishlist = wishlistService.addToWishlist(token, tourID);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tour added to wishlist"); // Trả lại đối tượng wishlist đã được thêm vào
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage()); // Trả lỗi nếu sản phẩm không tồn tại
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage()); // Nếu sản phẩm đã có trong wishlist
        }
    }

    // API xóa sản phẩm khỏi wishlist
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFromWishlist(@RequestHeader("Authorization") String authorizationHeader, @RequestParam long tourID) {
        try {
            String token= extractToken(authorizationHeader);

            wishlistService.delete(token, tourID);
            return ResponseEntity.status(HttpStatus.OK).body("Tour removed from wishlist"); // Xóa thành công
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/get")
    public ResponseEntity<List<WishlistDTO>> getWishlist(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token= extractToken(authorizationHeader);
            // Lấy danh sách wishlist từ service
            List<WishlistDTO> wishlist = wishlistService.getWishlist(token);

            // Trả về kết quả
            return ResponseEntity.ok(wishlist);
        } catch (IllegalArgumentException e) {
            // Trường hợp token không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

