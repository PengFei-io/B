package tcwl.dao;

import org.apache.ibatis.annotations.Param;
import tcwl.entites.EmailInfo;

import java.util.List;

public interface EmailMapper {
    public void insertEmailInfo(EmailInfo emailInfo);

    public List<EmailInfo> queryAll(@Param("begin") Integer begin, @Param("count") Integer count);

    public EmailInfo queryById(@Param("eid") String id);

    public void removeById(@Param("eid") String sid);

    /**
     * @category 查询数据总条数
     */
    Integer count();

    public void updateById(EmailInfo sid);
}
