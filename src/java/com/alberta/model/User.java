/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alberta.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Faraz
 */
public class User implements Serializable, RowMapper {

    private String username;
    private String userName;
    private String password;
    private String active;
    private String sessionId;
    private String firstName;
    private String lastName;
    private String doctorId;
    private String patientId;
    private String pharmaId;
    private String email;
    private String userId;
    private String fullName;
    private String newPassword;
    private String isNewAdminUser;
    private String userType;
    private String medicalStoreId;
    private String medicalPharmacyId;
    private String labMasterId;
    private String labDetailId;
    private String clinicId;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        User user = null;
        try {
            user = new User();
            user.setUserName(rs.getString("USER_NME"));
            user.setPassword(rs.getString("USER_PASSWORD"));
            user.setActive(rs.getString("ACTIVE_IND"));
            user.setEmail(rs.getString("EMAIL"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the doctorId
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId the doctorId to set
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the pharmaId
     */
    public String getPharmaId() {
        return pharmaId;
    }

    /**
     * @param pharmaId the pharmaId to set
     */
    public void setPharmaId(String pharmaId) {
        this.pharmaId = pharmaId;
    }

    /**
     * @return the isNewAdminUser
     */
    public String getIsNewAdminUser() {
        return isNewAdminUser;
    }

    /**
     * @param isNewAdminUser the isNewAdminUser to set
     */
    public void setIsNewAdminUser(String isNewAdminUser) {
        this.isNewAdminUser = isNewAdminUser;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the medicalStoreId
     */
    public String getMedicalStoreId() {
        return medicalStoreId;
    }

    /**
     * @param medicalStoreId the medicalStoreId to set
     */
    public void setMedicalStoreId(String medicalStoreId) {
        this.medicalStoreId = medicalStoreId;
    }

    /**
     * @return the medicalPharmacyId
     */
    public String getMedicalPharmacyId() {
        return medicalPharmacyId;
    }

    /**
     * @param medicalPharmacyId the medicalPharmacyId to set
     */
    public void setMedicalPharmacyId(String medicalPharmacyId) {
        this.medicalPharmacyId = medicalPharmacyId;
    }

    /**
     * @return the labMasterId
     */
    public String getLabMasterId() {
        return labMasterId;
    }

    /**
     * @param labMasterId the labMasterId to set
     */
    public void setLabMasterId(String labMasterId) {
        this.labMasterId = labMasterId;
    }

    /**
     * @return the labDetailId
     */
    public String getLabDetailId() {
        return labDetailId;
    }

    /**
     * @param labDetailId the labDetailId to set
     */
    public void setLabDetailId(String labDetailId) {
        this.labDetailId = labDetailId;
    }

    /**
     * @return the clinicId
     */
    public String getClinicId() {
        return clinicId;
    }

    /**
     * @param clinicId the clinicId to set
     */
    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }
}
