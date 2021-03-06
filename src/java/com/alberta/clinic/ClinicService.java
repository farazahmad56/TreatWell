/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alberta.clinic;

import com.alberta.dao.DAO;
import com.alberta.model.Brick;
import com.alberta.model.DoctorVO;
import com.alberta.model.Product;
import java.util.List;
import java.util.Map;

/**
 *
 * @author farazahmad
 */
public interface ClinicService {

    /**
     * @return the dao
     */
    DAO getDao();

    /**
     * @param dao the dao to set
     */
    void setDao(DAO dao);

    List<Map> getPharmaArea(String pharmaCompanyId);

    List<Map> getCitysOfPakistan();

    boolean savePharmaArea(String areaName, String areaId, String cityId, String pharmaCompanyId);

    boolean deletePharmaArea(String areaId);

    Map getPharmaAreaById(String areaId);

    List<Map> getCityArea(String cityId);

    List<Map> getAreaByCitys(Brick b);

    boolean saveMedicineAttachments(Product c, String attachmentPath);

    boolean saveCityArea(String areaName, String areaId, String cityId, String userName);

    boolean deleteCityArea(String areaId);

    Map getMedicalRepAppointmentById(String pharmaRepId);

    Map getCityAreaById(String areaId);

    List<Map> getBrickByPharmaceuticalId(String pharmaceuticalId);

    List<Map> getProvince();

    List<Map> getProcedure(String companyId);

    List<Map> getDoctorByAreaId(String areaId);

    List<Map> getAreaByCityId(String cityId);

    Map getProcedureById(String procedureId);

    boolean saveProcedure(String procedureId, String procedureName, String companyId);

    boolean updateProfileImage(DoctorVO d, String path);

    boolean saveBrick(Brick b);

    List<Map> getPrescriptionListing(String fromDate, String toDate, String doctorId, String clinicId, String patientId);

    List<Map> getMedicalColleges(String medicalCollegeId);

    List<Map> getCountries(String countryId);

    boolean saveDoctorEducation(DoctorVO p);

    List<Map> getDoctorEducation(String doctorId);

    boolean deleteProcedure(String procedureId);

    boolean deleteBrick(String brickId);

    boolean saveDoctorExperience(DoctorVO p);

    List<Map> getHospitals(String hospitalId);

    List<Map> getDoctorExperience(String doctorId);

    List<Map> getAssociations(String associationId);

    boolean saveDoctorAssociation(DoctorVO p);

    List<Map> getDoctorAssociation(String doctorId);

    boolean saveMedicalRepAppointment(Product c);

    List<Map> getCities(String cityId);

    boolean saveDisease(DoctorVO c);

    List<Map> getDiseases(String diseaseName);

    boolean deleteDisease(String diseaseId);

    Map getDiseasesById(String diseasesId);

    boolean saveHospital(DoctorVO c);

    Map getHospitalById(String diseasesId);

    List<Map> getHospital(String hospitalName);

    boolean deleteHospital(String hospitalId);

    boolean saveDegree(DoctorVO c);

    List<Map> getDegrees(String degreeName);

    Map getDegreeById(String degreeId);

    boolean deleteDegree(String degreeId);

    boolean saveCountry(DoctorVO c);

    List<Map> getCountry(String countryName);

    Map getCountryById(String countryId);

    boolean deleteCountry(String countryId);

    boolean saveMedicineType(String medicineTypeId, String MdeicineTypeName, String username);

    List<Map> getMedicineType(String medicineTypeName);

    Map getMedicineTypeById(String medicineTypeId);

    boolean deleteMedicineType(String medicineTypeId);

    boolean saveCity(DoctorVO c);

    boolean saveState(DoctorVO c);

    boolean deleteCity(String cityId);

    boolean deleteState(String stateId);

    Map getCityById(String cityId);

    Map getStateById(String stateId);

    List<Map> getCity(String cityName);

    List<Map> getState(String companyId);

    boolean saveMedicalServices(DoctorVO c);

    List<Map> getMedicalServices(String serviceName);

    Map getMedicalServiceById(String serviceId);

    boolean deleteMedicalService(String serviceId);

    boolean saveEducationInstitution(DoctorVO c);

    List<Map> getEducationInstitutions(String educationInstitutionName);

    Map getEducationInstitutionById(String educationInstitutionId);

    boolean deleteEducationInstitution(String educationInstitutionId);

    boolean saveLabTest(DoctorVO c);
    
    boolean saveLabTestRate(DoctorVO c);

    List<Map> getLabTests(String labTestName);
    
    List<Map> getLabTestRate(String id);

    Map getLabtestById(String labTestId);

    boolean deleteLabTest(String labTestId);

    boolean saveMedicine(Product c);

    boolean saveMedicineRep(Product c);

    List<Map> getMedicines(String medicineName);

    List<Map> getMedicinesRep(String pharmaCompanyId);

    List<Map> getBricks(String pharmaCompanyId);

    Map getMedicineById(String medicineId);

    Map getMedicineRepById(String medicineRepId);

    boolean deleteMedicine(String medicineId);

    boolean deleteMedicineRep(String medicineRepId);

    boolean saveMessage(DoctorVO c);

    List<Map> getMessage(DoctorVO c);

    boolean savePrintLayout(DoctorVO c, String attachmentPath);

    Map getPrintLayouts(String doctorId, String clinicId);

    Map getPrintLayoutById(String layoutId);

    boolean saveDoctorAttachment(DoctorVO d, String path);

    List<Map> getDoctorAppointedPatients(String doctorId);

    List<Map> getPrescriptionPatientsForDoctor(String doctorId);

    boolean saveMedicalAssociation(DoctorVO c);

    Map getAssociationById(String labTestId);

    Map getBrickById(String brickId);

    boolean deleteAssociation(String labTestId);

    List<Map> getMedicalLabs(String recommendedInd);

    boolean saveMedicalLab(DoctorVO c);

    boolean deleteMedicalLab(String diseaseId);

    Map getMedicalLabById(String diseasesId);

    boolean deleteDoctorEducation(String id);

    boolean deleteDoctorExperience(String id);

    boolean deleteDoctorAssociation(String id);

    boolean saveMedicalSpeciality(String specialityId, String specialityName, String showWebInd);

    List<Map> getMedicalSpeciality(String specialityNameSearch);

    Map getMedicalSpecialityById(String specialityId);

    boolean deleteMedicalSpeciality(String specialityId);

    boolean saveHospitalWard(String wardId, String clinicId, String wardName, String beds, String userName);

    List<Map> getHospitalWard(String clinicId);

    Map getHospitalWardById(String wardId);

    boolean deleteHospitalWard(String wardId);

    boolean saveHospitalRoom(String roomId, String clinicId, String roomName, String beds, String userName);

    List<Map> getHospitalRoom(String clinicId);

    Map getHospitalRoomById(String roomId);

    boolean deleteHospitalRoom(String roomId);

//    boolean deleteAdmitPatient(String hospitalPatientId);
    Map getAdmitPatientById(String hospitalPatientId);

    List<Map> getAdmitPatient(String clinicId, String statusInd);

    boolean saveAdmitPatient(String hospitalPatientId, String roomId, String clinicId, String wardId, String patientId, String bedNo, String userName);

    boolean saveDischargeData(String hospitalPatientId, String dischargeDate, String remarks, String userName);

    List<Map> getHospitalEmployee(String clinicId);

    Map getHospitalEmployeeById(String employeeId);

    boolean saveHospitalEmployee(String employeeId, String clinicId, String fullName, String email, String loginId, String contactNo);

    boolean updateVisitingCard(DoctorVO d, String path);

    boolean deleteMessageTemplate(String templateId);

    Map getSmsTemplateById(String templateId);

    boolean saveIntakeDisease(String specialityId, String[] diseasesId);

    List<Map> getIntakeDiseases(String specialityId);

    List<Map> getIntakeDiseasesForDoctor(String doctorId);
}
