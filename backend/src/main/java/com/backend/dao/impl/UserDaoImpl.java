package com.backend.dao.impl;

import com.backend.dao.UserDao;
import com.backend.mapper.UserMapper;
import com.backend.dto.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    UserMapper mapper;

    @Override
    public void save(User c) {

        mapper.save(c);
    }

    @Override
    public void delete(String uid) {
        mapper.delete(uid);
    }

    @Override
    public User findByUid(String uid) {
        return mapper.findByUid(uid);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.findByEmail(email);
    }

    @Override
    public void update(User c) {
        mapper.update(c);
    }

    @Override
    public void updateprofileImage(String nickname, String url) {
        mapper.updateprofileImage(nickname, url);
    }

    @Override
    public void updateQRImage(String nickname, String url) {
        mapper.updateQRImage(nickname, url);
    }

    @Override
    public User signin(String email, String password) {
        return mapper.signin(email, password);
    }

}
