package com.baizhi.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Controller
@RequestMapping(value = "/kaptcha")
public class KaptchaController {
    private static final Logger LOGGER=LoggerFactory.getLogger(KaptchaController.class);
    @Autowired
    private DefaultKaptcha captchaProducer;

    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest request,
                               HttpServletResponse response) throws Exception{
        byte[] imageBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = captchaProducer.createText();
            LOGGER.debug("verifyCode:"+verifyCode);
            request.getSession().setAttribute("verifyCode", verifyCode);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage image = captchaProducer.createImage(verifyCode);
            ImageIO.write(image, "jpg", baos);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        imageBytes = baos.toByteArray();

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(imageBytes);

        //关闭流
        outputStream.flush();
        outputStream.close();
    }

}