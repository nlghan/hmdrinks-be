package com.hmdrinks.Controller;

import com.hmdrinks.Request.CreateAccountUserReq;
import com.hmdrinks.Response.CRUDAccountUserResponse;
import com.hmdrinks.Response.ListAllUserResponse;
import com.hmdrinks.Service.AdminService;
import com.hmdrinks.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired

    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/listUser")
    public ResponseEntity<ListAllUserResponse> listAllUser(
            @RequestParam(name = "page") String page,
            @RequestParam(name = "limit") String limit
    ) {
        return ResponseEntity.ok(userService.getListAllUser(page, limit));
    }

    @PostMapping(value = "/create-account")
    public ResponseEntity<CRUDAccountUserResponse> createAccount(@RequestBody CreateAccountUserReq req){
        return ResponseEntity.ok(adminService.createAccountUser(req));
    }

    @GetMapping(value = "/search-user")
    public ResponseEntity<?> searchByUser(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "page") String page, @RequestParam(name = "limit") String limit) {
        return ResponseEntity.ok(userService.totalSearchUser(keyword, page, limit));
    }
}
