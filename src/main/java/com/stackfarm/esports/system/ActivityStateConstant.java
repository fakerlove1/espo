package com.stackfarm.esports.system;

/**
 * @author croton
 * @create 2021/4/2 16:30
 */
public class ActivityStateConstant {
    /**
     * 审核中
     */
    public static final String CHECKING = "CHECKING";

    /**
     * 审核通过
     */
    public static final String ACCESS = "ACCESS";

    /**
     * 未通过
     */
    public static final String FAILED = "FAILED";

    /**
     * 报名中
     */
    public static final String ENROLLING = "ENROLLING";

    /**
     * 报名结束
     */
    public static final String ENROLL_CLOSED= "ENROLL_CLOSED";

    /**
     * 进行中
     */
    public static final String HOLDING = "HOLDING";

    /**
     * 活动结束
     */
    public static final String ENDED = "ENDED";

    /**
     * 活动取消
     */
    public static final String CANCELED = "CANCELED";

    /**
     * 活动撤销中
     */
    public static final String CANCELING = "CANCELING";


}
