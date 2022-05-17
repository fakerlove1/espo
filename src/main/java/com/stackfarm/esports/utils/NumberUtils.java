package com.stackfarm.esports.utils;

import com.stackfarm.esports.exception.UnhandledException;
import com.stackfarm.esports.system.MemberTypeConstant;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * @author croton
 * @create 2021/10/16 12:59
 */
public class NumberUtils {

    /**
     * 证书编号生成规则——共12位数字，
     * 项目（第1-2位）+
     * 组别（第3位，职业组1、专业组2）+
     * 级别（第4位，一级1、二级2、三级3）+
     * 年份（5-8位）+
     * 注册编号（9-12位）
     *
     * @param project
     * @param type
     * @param level
     * @param year
     * @param memberNumber
     * @return
     */
    public static String getNumber(Integer project, String type, Integer level, Integer year, Integer memberNumber) throws UnhandledException {
        String projectStr = project.toString();
        String typeStr = "";
        String levelStr = level.toString();
        String yearStr = year.toString();
        String memberNumberStr = memberNumber.toString();
        if (project/100 > 0)
        if (project/10 == 0) {
            projectStr = "0" + project.toString();
        }
        //专业组为1 业余组为2
        if (MemberTypeConstant.PROFESSIONAL_PLAYER.equals(type)) {
            typeStr = "1";
        } else if (MemberTypeConstant.AMATEUR_PLAYER.equals(type)) {
            typeStr = "2";
        }
        if (memberNumber/1000<=0 && memberNumber/100>0) {
            memberNumberStr = "0" + memberNumber.toString();
        } else if (memberNumber/100<=0 && memberNumber/10>0) {
            memberNumberStr = "00" + memberNumber.toString();
        } else if (memberNumber/10<=0) {
            memberNumberStr = "000" + memberNumber.toString();
        } else {
            UnhandledException unhandledException = new UnhandledException();
            unhandledException.setMsg("编号异常，人数已满或格式错误！");
            unhandledException.setLocation(BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]));
            unhandledException.setTimestamp(new Date(System.currentTimeMillis()));
            unhandledException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            throw unhandledException;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(projectStr);
        sb.append(typeStr);
        sb.append(levelStr);
        sb.append(yearStr);
        sb.append(memberNumberStr);
        return sb.toString();
    }
}
