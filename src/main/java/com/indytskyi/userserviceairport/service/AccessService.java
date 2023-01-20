package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.model.User;

public interface AccessService {
    User checkByAdmin(String token);

    User getUserFromTokenAfterValidation(String bearerToken);
}
