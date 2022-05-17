package com.stackfarm.esports.result.authorize;

/**
 * @author croton
 * @create 2021/10/21 13:40
 */
public class AnnualAuthenticationResultBean {
    private String club;
    private Integer year;
    private String result;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public AnnualAuthenticationResultBean() {
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
