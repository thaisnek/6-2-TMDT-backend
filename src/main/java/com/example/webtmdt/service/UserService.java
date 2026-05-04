package com.example.webtmdt.service;

import com.example.webtmdt.dto.request.UpdateUserRoleRequest;
import com.example.webtmdt.dto.request.UpdateUserStatusRequest;
import com.example.webtmdt.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request);

    UserResponse updateUserRole(Long id, UpdateUserRoleRequest request);
}
