package com.arberkuci.storeapp.ejb.cache.control;

import com.arberkuci.storeapp.common.cache.api.CacheLocal;
import com.arberkuci.storeapp.ejb.cache.common.Constant;
import com.arberkuci.storeapp.ejb.user.dto.UserDto;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

@Stateless
@Local(UserCache.class)
public class UserCacheBean implements UserCache {

    @Inject
    CacheLocal cacheLocal;

    private final String className = this.getClass().getName();

    private Logger logger = Logger.getLogger(className);

    public void storeUser(UserDto userDto) {
        if (userDto != null) {
            if (userDto.getId() != null) {
                cacheLocal.getCache(Constant.CACHE_USER_BY_ID).put(userDto.getId(), userDto);
                logger.warning("The following entry is cached associated with id -> " + userDto.getId());
            } else {
                logger.warning("The id is null, nothing stored in cache.");
            }
            if (userDto.getUserName() != null) {
                cacheLocal.getCache(Constant.CACHE_USER_BY_USERNAME).put(userDto.getUserName(), userDto);
                logger.warning("THe following entry is cached associated with username -> " + userDto.getUserName());
            } else {
                logger.warning("The username is null, nothing stored in cache.");
            }
        } else {
            logger.warning("The dto was null, nothing stored in cache!");
        }
    }

    @Override
    public UserDto findUserById(Long id) {
        UserDto foundUser = (UserDto) this.cacheLocal.getCache(Constant.CACHE_USER_BY_ID).get(id);
        logger.warning("With the given id " + id + " the following result is found in cache " + foundUser);
        return foundUser;
    }

    @Override
    public UserDto findUserByUsername(String username) {
        UserDto foundUser = (UserDto) this.cacheLocal.getCache(Constant.CACHE_USER_BY_USERNAME).get(username);
        logger.warning("With the given username " + username + " the following result is found in cache " + foundUser);
        return foundUser;
    }


}
