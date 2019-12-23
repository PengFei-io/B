package tcwl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tcwl.entites.EmailInfo;
import tcwl.service.MailService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private MailService mailService;

    @RequestMapping("/sender")
    public String home() {
        return "redirect:/sendEmail.jsp";
    }

    /**
     * 用户上传邮件相关信息进行发送邮件
     *
     * @param doc
     * @param emailInfo
     * @param session
     */
    @RequestMapping("/send")
    public String sendEmail(MultipartFile doc, EmailInfo emailInfo, HttpSession session, Model model) {
        String title = emailInfo.getTitle();
        if (title.equals("") || title == null) {
            model.addAttribute("message", "标题不能为空！！！");
            return "forward:/sendEmail.jsp";
        }

        //      如果上传的文件为D://document/  如下
        File upload = new File("D://document");
        if (!upload.exists()) upload.mkdirs();
        String filename = null;
        if (doc != null) {
            filename = doc.getOriginalFilename();
        }
        try {
            if (filename != null) {
                log.info("文件上传路径为：" + upload.getAbsolutePath());
                doc.transferTo(new File(upload.getAbsolutePath() + "/" + filename));
            } else {
                emailInfo.setDocument(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("收件人信息为：{}", emailInfo);
        emailInfo.setDocument(filename);
        mailService.addEmailInfo(emailInfo);
        model.addAttribute("message", "发送成功！！！");
        return "forward:/sendEmail.jsp";
    }

    /**
     * 显示所有收件
     *
     * @return
     */
    @RequestMapping("/show")
    public String showEmail(Model model, String pageIndex) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        boolean matches = pattern.matcher(pageIndex).matches();
        if (matches == false) {
            return "forward:/a.jsp";
        }
        Integer pageIndex1 = Integer.parseInt(pageIndex);
        if (pageIndex1 == null || pageIndex1 <= 0) pageIndex1 = 1;
        Map<String, Object> map = mailService.showEmailInfo(pageIndex1);
        model.addAttribute("map", map);
        return "forward:/table.jsp";
    }

    @RequestMapping("/detail")
    public String detail(String sid, Model model) {
        EmailInfo info = mailService.queryById(sid);
        model.addAttribute("emailInfo", info);
        return "forward:/detail.jsp";
    }

    @RequestMapping("/remove")
    @ResponseBody
    public String remove(String sid, Model model) {
        mailService.removeById(sid);
        return "ok";
    }

    @RequestMapping("/updateStatus")
    public String updateStatus(EmailInfo info) {
        System.out.println(info);
        mailService.updateById(info);
        return "redirect:/email/show";
    }

    /**
     * 文件下载
     */
    @RequestMapping("/downLoad")
    public void downLoad(String fileName, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        log.info("请求文件名为：{}", fileName);

        response.setHeader("content-disposition", "attachment;filename=" +new String( fileName.getBytes(), "ISO8859-1" ) );
        //获取源文件的字节数组
        FileInputStream in = new FileInputStream("D://document/" + fileName);

        byte[] buffer = new byte[4096];
        ServletOutputStream os = response.getOutputStream();
        int bytesToRead;
        while ((bytesToRead = in.read(buffer)) != -1) {
            os.write(buffer, 0, bytesToRead);
        }
    }
}
