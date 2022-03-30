/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author William Dias
 */
public class FollowUpExecution extends Integration {
    
    public int fileId;
    public FollowUpActivity followUpActivity;
    public Person personRelease;
    public String deadLine;
    public String releaseDate;
    public Boolean userAlter;
    public String startFileDate;

    public FollowUpExecution() {
    }

    public FollowUpExecution(int fileId, FollowUpActivity followUpActivity, Person personRelease, String deadLine, String releaseDate, Boolean userAlter, String startFileDate, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.fileId = fileId;
        this.followUpActivity = followUpActivity;
        this.personRelease = personRelease;
        this.deadLine = deadLine;
        this.releaseDate = releaseDate;
        this.userAlter = userAlter;
        this.startFileDate = startFileDate;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public FollowUpActivity getFollowUpActivity() {
        return followUpActivity;
    }

    public void setFollowUpActivity(FollowUpActivity followUpActivity) {
        this.followUpActivity = followUpActivity;
    }

    public Person getPersonRelease() {
        return personRelease;
    }

    public void setPersonRelease(Person personRelease) {
        this.personRelease = personRelease;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isUserAlter() {
        return userAlter;
    }

    public void setUserAlter(boolean userAlter) {
        this.userAlter = userAlter;
    }

    public String getStartFileDate() {
        return startFileDate;
    }

    public void setStartFileDate(String startFileDate) {
        this.startFileDate = startFileDate;
    }
    
    
}
