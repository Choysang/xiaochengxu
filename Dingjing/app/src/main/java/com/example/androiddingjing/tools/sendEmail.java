package com.example.androiddingjing.tools;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendEmail {
    public static String send(String recipients/*收件邮箱*/, String title/*标题*/, String content/*内容*/) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(recipients);
        boolean isMatched = matcher.matches();
        System.out.println(isMatched);

        if(isMatched){
            new Thread(() -> {
                final String userName = "2442596430@qq.com";
                final String password = "agtxctqcsuuxeahf";
                Properties pro = System.getProperties();
                pro.put("mail.smtp.host", "smtp.qq.com");
                pro.put("mail.smtp.port", 465);
                pro.put("mail.smtp.auth", "true");
                // 根据邮件会话属性和密码验证器构造一个发送邮件的session
                Session sendMailSession = Session.getDefaultInstance(pro,
                        new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(userName, password);
                            }
                        });
                // 根据session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                // 设置邮件消息的发送者
                try {
                    mailMessage.setFrom(new InternetAddress(userName));
                    // 创建邮件的接收者地址，并设置到邮件消息中
                    mailMessage.setRecipient(Message.RecipientType.TO,
                            new InternetAddress(recipients));
                    // 设置邮件消息的主题
                    mailMessage.setSubject(title);
                    // 设置邮件消息发送的时间
                    mailMessage.setSentDate(new Date());
                    // 设置邮件消息的主要内容
                    mailMessage.setText(content);
                    // 发送邮件   由于无法准备判断邮件是否发送成功，所以如果没有抛异常则表示发送成功
                    Transport.send(mailMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }).start();
        }else{
            return "邮件不合法";
        }

        return "发送成功";

    }
}
