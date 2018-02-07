package com.ham.p2p.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ham.p2p.base.domain.SystemDictionaryItem;
import com.ham.p2p.base.domain.UserFile;
import com.ham.p2p.base.domain.Userinfo;
import com.ham.p2p.base.mapper.UserFileMapper;
import com.ham.p2p.base.query.UserFileQueryObject;
import com.ham.p2p.base.service.IUserFileService;
import com.ham.p2p.base.service.IUserinfoService;
import com.ham.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UsertFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;

    @Override
    public int save(UserFile record) {
        return userFileMapper.insert(record);
    }

    @Override
    public UserFile get(Long id) {
        return userFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(UserFile record) {
        return userFileMapper.updateByPrimaryKey(record);
    }

    @Override
    public void apply(String imgPath) {
        UserFile uf = new UserFile();
        uf.setImage(imgPath);
        uf.setApplier(UserContext.getCurrentUser());
        uf.setApplyTime(new Date());
        uf.setState(UserFile.STATE_NORMAL);
        this.save(uf);
    }

    @Override
    public List<UserFile> queryUnSelectFileTypeList() {
        return userFileMapper.queryUnSelectFileTypeList(UserContext.getCurrentUser().getId());
    }

    @Override
    public void selectType(Long[] ids, Long[] fileTypes) {
        if (ids != null && fileTypes != null && ids.length == fileTypes.length) {
            UserFile uf;
            SystemDictionaryItem fileType;

            for (int i = 0; i < ids.length; i++) {
                uf = this.get(ids[i]);
                //判断风控裁量是否属于当前用户
                if (UserContext.getCurrentUser().getId().equals(uf.getApplier().getId())) {

                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    uf.setFileType(fileType);
                    this.update(uf);
                }
            }
        }
    }

    @Override
    public List<UserFile> selectFileTypeByCondition(boolean b) {
        return userFileMapper.selectFileTypeByCondition(UserContext.getCurrentUser().getId(), b);
    }

    @Override
    public PageInfo queryPage(UserFileQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(), qo.getPageSize());
        List list = userFileMapper.queryPage(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, int score, String remark) {
        UserFile userFile = this.get(id);
        if (userFile != null && userFile.getState() == UserFile.STATE_NORMAL) {
            userFile.setAuditor(UserContext.getCurrentUser());
            userFile.setAuditTime(new Date());
            userFile.setRemark(remark);

            if (state == UserFile.STATE_PASS) {
                userFile.setState(UserFile.STATE_PASS);
                userFile.setScore(score);
                Userinfo applierUserinfo = userinfoService.selectByPrimaryKey(userFile.getApplier().getId());
                applierUserinfo.setScore(applierUserinfo.getScore() + score);
                userinfoService.update(applierUserinfo);
            } else {
                userFile.setState(UserFile.STATE_REJECT);
            }
            this.update(userFile);
        }
    }

    @Override
    public List<UserFile> queryByUserId(Long userId) {
        return userFileMapper.queryByUserId(userId, UserFile.STATE_PASS);
    }
}
