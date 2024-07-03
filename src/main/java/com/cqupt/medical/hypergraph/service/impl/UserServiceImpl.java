package com.cqupt.medical.hypergraph.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.medical.hypergraph.vo.InsertUserVo;
import com.cqupt.medical.hypergraph.vo.UserPwd;
import com.cqupt.medical.hypergraph.mapper.UserMapper;
import com.cqupt.medical.hypergraph.service.UserService;
import com.cqupt.medical.hypergraph.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        List<User> users = userMapper.selectList(null);
        return users;
    }

    @Override
    public User getUserByName(String userName) {
        User user = userMapper.getUerByUserName(userName);
        return user;
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    @Override
    public Page<User> getAllUserInfo(int pageNum  , int pageSize) {

        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page,null);

    }

    @Override
    public boolean updateStatusById(Integer uid, Integer  role ,double uploadSize , String status) {

        boolean b =  userMapper.updateStatusById(uid, role ,uploadSize,  status);
        if ( b )  return true;
        return false;
    }

    @Override
    public boolean removeUserById(Integer uid) {
        userMapper.removeUserById(uid);
        return true;
    }


    @Override
    public boolean insertUser(InsertUserVo user) {

        userMapper.insertUser(user);
        return false;
    }




    public Map<String, Object> getUserPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<User> userList = userMapper.selectUserPage(offset, pageSize);
        int total = userMapper.countUsers();

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("data", userList);
        return result;
    }


    @Override
    public boolean updatePwd(UserPwd user) {

        userMapper.updatePwd(user);

        return false;
    }

    @Override
    public List<User> querUser() {

        List<User> users = userMapper.selectList(null);
        return users;
    }

    @Override
    public User queryByUername(String username) {
        User user = userMapper.queryByUername(username);
        return user;
    }

    //    下面方法是管理员端-数据管理新增
    @Override
    public void addTableSize(String uid,float tableSize) {
        userMapper.addTableSize(uid, tableSize);
    }

    @Override
    public void minusTableSize(String uid, float tableSize) {

        userMapper.minusTableSize(uid, tableSize);
    }

    @Override
    public PageInfo<User> findByPageService(Integer pageNum, Integer pageSize, QueryWrapper<User> queryWrapper) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> userInfos = userMapper.selectList(queryWrapper);
        return new PageInfo<>(userInfos);
    }

    @Override
    public User getUserByUserName(String username) {
        User user = userMapper.queryByUername(username);
        return user;
    }
}
