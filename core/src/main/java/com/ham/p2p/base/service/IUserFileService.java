package com.ham.p2p.base.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.UserFile;
import com.ham.p2p.base.query.UserFileQueryObject;

import java.util.List;

public interface IUserFileService {
    int save(UserFile record);

    UserFile get(Long id);

    int update(UserFile record);

    void apply(String imgPath);

    List<UserFile> queryUnSelectFileTypeList();

    void selectType(Long[] ids, Long[] fileTypes);

    List<UserFile> selectFileTypeByCondition(boolean b);

    PageInfo queryPage(UserFileQueryObject qo);

    void audit(Long id,int state, int score, String remark);

    List<UserFile> queryByUserId(Long userId);
}
