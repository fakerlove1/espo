package com.stackfarm.esports.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author croton
 * @create 2021/4/5 16:43
 */
public class EmailUtils {
    public static void sendVerCodeByEmail(String targetEmail, String verCode, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("邮箱验证");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好！您的验证码是: " + verCode + " ，如非本人操作，请忽略本邮件。");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendRefuseMsgByEmail(String targetEmail, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("申请退回");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 对不起，由于" + cause + "，您的申请未能通过，请尝试重新发起申请，谢谢您的合作！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendAccessMsgByEmail(String targetEmail, String name, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("申请成功");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您的 " + name + "帐号已通过申请，祝您使用愉快！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendSuccessMsgByEmail(String targetEmail, String name, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("申请成功");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您的 " + name + "活动已通过申请，具体详情可登录协会官网查看！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendAccessOMsgByEmail(String targetEmail, String name, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("报名成功");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您的 " + name + "活动报名已通过审核，具体详情可登录协会官网查看！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendCancelNotPassMsgByEmail(String targetEmail, String name, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("撤销失败");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您的 " + name + "活动撤销由于【"+ cause +"】而未通过审核，请关注活动状态，若仍需要撤销可重新提交撤销申请！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendDarkroomMsgByEmail(String targetEmail, String name, String time, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("小黑屋");
        simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您的 " + name + "帐号由于【" + cause + "】已进入小黑屋名单，时间为【" + time + "】，请遵守网络秩序，谢谢您的理解与配合！");
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendAuthenticationMsgByEmail(String targetEmail, Boolean result, String name, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("会员认证");
        if (result == true) {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 身份认证已通过，请关注官网年度审核信息，及时对会员信息进行更新！");
        } else {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 由于【" + cause + "】身份认证未能通过，请仔细检查认证材料，并重新提交申请，谢谢您的理解与配合！");
        }
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendUpdateMsgByEmail(String targetEmail, Boolean result, String name, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("会员认证");
        if (result == true) {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 修改的信息已通过，详情请登录官网查看！");
        } else {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 由于【" + cause + "】修改的信息未能通过，请仔细检查修改信息，并重新提交申请，谢谢您的理解与配合！");
        }
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendAnnualAuthenticationMsgByEmail(String targetEmail, Boolean result, String cause, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("会员认证");
        if (result == true) {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您提交的的年度审核已通过，具体详情可登录协会官网查看！");
        } else {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，您提交的的年度审核由于【" + cause + "】未能通过，请仔细检查认证材料，并重新提交申请，谢谢您的理解与配合！");
        }
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }

    public static void sendPostMsgByEmail(String targetEmail, Boolean result, String name, String cause, String trackingNumber, JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setFrom(PropertiesReadUtils.EMAIL_USERNAME);
        simpleMailMessage.setSubject("证书邮寄审核");
        if (result == true) {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 证书邮寄申请已通过，单号为【" + trackingNumber + "】，请关注证书物流信息！");
        } else {
            simpleMailMessage.setText("【江苏省电子竞技运动协会】 您好，会员 " + name + " 由于【" + cause + "】证书邮寄申请未能通过，请仔细检查是否付款，并重新提交申请，谢谢您的理解与配合！");
        }
        assert javaMailSender != null;
        javaMailSender.send(simpleMailMessage);
    }




}
