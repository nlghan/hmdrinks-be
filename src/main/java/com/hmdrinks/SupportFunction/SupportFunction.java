package com.hmdrinks.SupportFunction;

import com.hmdrinks.Entity.User;
import com.hmdrinks.Exception.BadRequestException;
import com.hmdrinks.Exception.ConflictException;
import com.hmdrinks.Repository.UserRepository;
import com.hmdrinks.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SupportFunction {

    private UserRepository userRepository;

    private final JwtService jwtService;

    // Constructor-based dependency injection
    @Autowired
    public SupportFunction(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public boolean checkRole(String role) {
        return role.equals("ADMIN") || role.equals("CUSTOMER") || role.equals("SHIPPER");
    }

    public void checkUserAuthorization(HttpServletRequest httpRequest, Long userIdFromRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Authorization header is missing or invalid");
        }

        String jwt = authHeader.substring(7);
        String userIdFromToken = jwtService.extractUserId(jwt);

        if (!String.valueOf(userIdFromRequest).equals(userIdFromToken)) {
            throw new BadRequestException("You do not have permission to perform this action");
        }
    }

    public void checkPhoneNumber(String phoneNumber, Integer userId, UserRepository userRepository) {
        // Kiểm tra độ dài của số điện thoại
        if (phoneNumber == null || phoneNumber.length() != 10) {
            throw new BadRequestException("Số điện thoại không hợp lệ. Phải chứa 10 chữ số.");
        }

        // Kiểm tra số điện thoại có bị trùng không (trong database)
        Optional<User> existingUserOptional = userRepository.findByPhoneNumberAndIsDeletedFalse(phoneNumber);

        // Nếu tồn tại người dùng và người dùng đó không phải là chính người đang cập nhật
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get(); // Lấy user từ Optional
            if (!(existingUser.getUserId() ==userId)) {
                throw new ConflictException("Số điện thoại đã tồn tại.");
            }
        }
    }


}
