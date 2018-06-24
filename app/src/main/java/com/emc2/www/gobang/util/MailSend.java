package com.emc2.www.gobang.util;

import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSend implements Runnable {

    private String mailContext;
    private static String MY_EMAIL = "JerryZhengDev@163.com";

    public MailSend(String context) {
        this.mailContext = context;
    }

    private void sendMail() throws MessagingException {
        Properties props = new Properties();
        //使用smtp代理，且使用网易163邮箱
        props.put("mail.smtp.host", "smtp.163.cn");
        //设置验证
        props.put("mail.smtp.auth", "true");
        MyAuthenticator myauth = new MyAuthenticator(MY_EMAIL, "zhjl1996101816");
        Session session = Session.getInstance(props, myauth);
        //打开调试开关
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        InternetAddress fromAddress = null;
        //发件人邮箱地址
        fromAddress = new InternetAddress(MY_EMAIL);
        message.setFrom(fromAddress);

        InternetAddress toAddress = new InternetAddress(MY_EMAIL);//收件人
        message.addRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject("五子棋APP有新的问题反馈");
        message.setText(mailContext);// 设置邮件内容
        message.saveChanges(); //存储信息
        Transport transport = null;
        transport = session.getTransport("smtp");
        transport.connect("smtp.163.com", MY_EMAIL, "zhjl1996101816");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    @Override
    public void run() {
        try {
            sendMail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }
}