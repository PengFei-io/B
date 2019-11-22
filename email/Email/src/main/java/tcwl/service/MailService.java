package tcwl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tcwl.dao.EmailMapper;
import tcwl.entites.EmailInfo;

import java.util.*;

/**
 * 邮件相关
 */

@Service
@Slf4j
@Transactional
public class MailService {

    @Autowired
    private EmailMapper emailMapper;
    private final Integer PAGESIZE = 12;

    /**
     * 将收件人的相关信息保存到数据库
     *
     * @param emailInfo
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addEmailInfo(EmailInfo emailInfo) {
        emailInfo.setEid(UUID.randomUUID().toString().replace("-", ""));
        emailInfo.setSendTime(new Date());
        emailInfo.setAcceptTime(new Date());
        emailMapper.insertEmailInfo(emailInfo);
    }

    /**
     * 查询所有收件信息
     */
    public Map<String, Object> showEmailInfo(Integer pageIndex) {
        Integer count = emailMapper.count();
        Integer pageCount = count % PAGESIZE == 0 ? count / PAGESIZE : (count / PAGESIZE) + 1;
        if (pageIndex <= 0||pageIndex==null) {
            pageIndex = 1;
        }
        if (pageIndex >= pageCount) {
            pageIndex = pageCount;
        }

        List<EmailInfo> emailInfos = emailMapper.queryAll((pageIndex - 1) * PAGESIZE, PAGESIZE);
        for (EmailInfo emailInfo : emailInfos) {
            System.out.println(emailInfo);
        }
        HashMap hashMap = new HashMap<String, Object>();
        hashMap.put("emailInfos", emailInfos);
        hashMap.put("pageCount", pageCount);
        hashMap.put("pageIndex", pageIndex);
        return hashMap;
    }

    /**
     * 查询一条信息
     */
    public EmailInfo queryById(String id) {
        EmailInfo info = emailMapper.queryById(id);
        return info;
    }

    /**
     * 删除一条信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeById(String sid) {
        emailMapper.removeById(sid);
    }
    /**
     * 改一条信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateById(EmailInfo sid) {
        emailMapper.updateById(sid);
    }
}

