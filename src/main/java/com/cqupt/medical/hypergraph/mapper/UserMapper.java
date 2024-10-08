package com.cqupt.medical.hypergraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqupt.medical.hypergraph.vo.InsertUserVo;
import com.cqupt.medical.hypergraph.vo.UserPwd;
import com.cqupt.medical.hypergraph.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUerByUserName(@Param("userName") String userName);

    User getUserById(String id);

    void saveUser(@Param("user") User user);

    IPage<User> getAllUserInfo(Page<?> page);

    boolean updateStatusById(Integer uid,Integer role , double uploadSize, String status);

//    int updateByname(String pwd, String username);

    void removeUserById(Integer uid);

    void insertUser(InsertUserVo user);

    List<User> selectUserPage(int offset, int pageSize);

    int countUsers();

    int updatePwd(UserPwd user);

    @Update("UPDATE software6user SET upload_size = upload_size-#{size} WHERE uid = #{id}")
    int decUpdateUserColumnById(@Param("id") String id, @Param("size") Double size);

    @Update("UPDATE software6user SET upload_size = upload_size+#{size} WHERE uid = #{id}")
    int recoveryUpdateUserColumnById(@Param("id") String id, @Param("size") Double size);

    User queryByUername(String username);

    //    下面方法是管理员端-数据管理新增
    User selectByUid(String uid);
    void addTableSize(String uid, float tableSize);
    void minusTableSize(String uid, float tableSize);
}
