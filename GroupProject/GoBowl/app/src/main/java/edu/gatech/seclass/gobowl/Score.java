package edu.gatech.seclass.gobowl;

import java.util.Date;

/**
 * Created by R on 7/4/2016.
 */
public class Score {
    private int score;
    private Date gameEndTime;
    private String customerID;
    Score(int s, Date gameEndTime, String customerID){
        score = s;
        this.gameEndTime = gameEndTime;
        this.customerID = customerID;
    }

    public int getScore() {
        return score;
    }

    public Date getGameEndTime() {
        return gameEndTime;
    }

    public String getCustomerID() {
        return customerID;
    }
}
