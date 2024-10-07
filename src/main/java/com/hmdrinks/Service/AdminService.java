package com.hmdrinks.Service;

import com.hmdrinks.Entity.User;
import com.hmdrinks.Enum.Sex;
import com.hmdrinks.Enum.TypeLogin;
import com.hmdrinks.Exception.BadRequestException;
import com.hmdrinks.Exception.ConflictException;
import com.hmdrinks.Repository.UserRepository;
import com.hmdrinks.Request.CreateAccountUserReq;
import com.hmdrinks.Response.CRUDAccountUserResponse;
import com.hmdrinks.SupportFunction.SupportFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupportFunction supportFunction;

    @Autowired
    PasswordEncoder passwordEncoder;

    public CRUDAccountUserResponse createAccountUser(CreateAccountUserReq req){
        Optional<User> user = userRepository.findByUserNameAndIsDeletedFalse(req.getUserName());

        if (user.isPresent()) {
            throw new ConflictException("User name already exists");
        }

        if (!supportFunction.checkRole(req.getRole().toString())) {
            throw new BadRequestException("Role is wrong");
        }

        User userWithEmail = userRepository.findByEmail(req.getEmail());
        if (userWithEmail != null && !(userWithEmail.getUserName() == (req.getUserName()))) {
            // Nếu tìm thấy người dùng có email này nhưng không phải chính người đang tạo
            throw new ConflictException("Email already exists with another user");
        }

        // Gọi hàm checkPhoneNumber từ SupportFunction

        LocalDate currentDate = LocalDate.now();
        User user1 = new User();
        user1.setType(TypeLogin.BASIC);
        user1.setEmail(req.getEmail());
        user1.setRole(req.getRole());
        user1.setIsDeleted(false);
        user1.setUserName(req.getUserName());
        user1.setAvatar("");
        user1.setDistrict("");
        user1.setCity("");
        user1.setStreet("");
        user1.setSex(Sex.OTHER);
        user1.setDateCreated(Date.valueOf(currentDate));
        user1.setPhoneNumber("");
        user1.setPassword(passwordEncoder.encode(req.getPassword()));
        user1.setFullName(req.getFullName());
        userRepository.save(user1);

        Optional<User> userNewq = userRepository.findByUserNameAndIsDeletedFalse(req.getUserName());
        User userNew = userNewq.get();
        return new CRUDAccountUserResponse(
                userNew.getUserId(),
                userNew.getUserName(),
                userNew.getFullName(),
                userNew.getAvatar(),
                userNew.getBirthDate(),
                "",
                userNew.getEmail(),
                userNew.getPhoneNumber(),
                userNew.getSex().toString(),
                userNew.getType().toString(),
                userNew.getIsDeleted(),
                userNew.getDateDeleted(),
                userNew.getDateUpdated(),
                userNew.getDateCreated(),
                userNew.getRole().toString()
        );
    }
}
