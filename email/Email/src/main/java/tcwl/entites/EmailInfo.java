package tcwl.entites;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class EmailInfo implements Serializable {
    /**
     * 主键
     */
    private String eid;
    /**
     * 邮件标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 附件
     */
    private String document;
    /**
     * 收件人
     */
    private String accepter;
    /**
     * 发件人
     */
    private String sender;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 收件时间
     */
    private Date acceptTime;
    /**
     * 收件人单位名称
     */
    private String company;
    /**
     * 收件人联系方式
     */
    private String phoneNum;
    /**
     * 收件人邮箱
     */
    private String accEmail;
    /**
     * 发件人邮箱
     */
    private String sendEmail;
    private Boolean look_status;
}
