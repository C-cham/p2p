package com.ham.p2p.base.mapper;

import com.ham.p2p.base.domain.UserFile;
import com.ham.p2p.base.query.UserFileQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    List<UserFile> selectAll();

    int updateByPrimaryKey(UserFile record);

    List<UserFile> queryUnSelectFileTypeList(Long userId);

    List<UserFile> selectFileTypeByCondition(@Param("userId") Long userId, @Param("b") boolean b);

    List queryPage(UserFileQueryObject qo);

    List<UserFile> queryByUserId(@Param("userId") Long userId, @Param("statePass") int statePass);
}