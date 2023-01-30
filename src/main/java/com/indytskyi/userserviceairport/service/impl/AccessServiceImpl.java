package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.exception.LimitedPermissionException;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.service.AccessService;
import com.indytskyi.userserviceairport.service.AuthenticationService;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {
    private static final String ADMIN_ROLE = "ADMIN";
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @Override
    public User checkByAdmin(String bearerToken) throws LimitedPermissionException {
        var user = getUserFromTokenAfterValidation(bearerToken);
        if (!ADMIN_ROLE.equals(user.getRole().name())) {
            throw new LimitedPermissionException("User don`t have access to this request");
        }
        return user;
    }


    @Override
    public User getUserFromTokenAfterValidation(String bearerToken) {
        var validateDto = authenticationService.validateToken(bearerToken);
        return userService.findById(validateDto.userId());
    }

}
