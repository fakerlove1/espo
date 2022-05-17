package com.stackfarm.esports.system;

/**
 * @Author xiaohuang
 * @create 2021/4/3 13:12
 */
public class UserActivityStateConstant {
    /**
     * 审核中
     */
    public static final String UNDER_REVIEW = "UNDER_REVIEW";
    /**
     * 已通过
     */
    public static final String PASSED = "PASSED";
    /**
     * 未通过/拒绝
     */
    public static final String TURN_DOWN = "TURN_DOWN";
    /**
     * 退出活动，这是最终状态，和PASSED，TURN_DOWN一样，不可再被更新，只能增加记录来实现新的记录
     */
    public static final String SIGN_OUT = "SIGN_OUT";
    /**
     * 活动已删除
     */
    public static final String ACTIVITY_DELETED = "ACTIVITY_DELETED";

    /**
     * 成功完成活动
     */
    public static final String COMPLETED = "COMPLETED";

    /**
     * 未成功完成活动
     */
    public static final String UNCOMPLETED = "UNCOMPLETED";
}
