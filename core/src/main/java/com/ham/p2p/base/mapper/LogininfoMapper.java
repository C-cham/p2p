package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.Logininfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LogininfoMapper {

    int insert(Logininfo record);


    int selectCountByUsername(String username);

    Logininfo login(@Param("username") String username, @Param("password") String password, @Param("userType") int userType);

    int queryCountByUserType(int usertypeManager);

    List<Map<String, Object>> autoComplate(@Param("keyword") String keyword, @Param("userType") int userType);
}