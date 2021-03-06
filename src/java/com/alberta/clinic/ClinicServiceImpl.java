/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alberta.clinic;

import com.alberta.dao.DAO;
import com.alberta.model.Brick;
import com.alberta.model.DoctorVO;
import com.alberta.model.Encryption;
import com.alberta.model.Product;
import com.alberta.utility.MD5;
import com.alberta.utility.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author farazahmad
 */
public class ClinicServiceImpl implements ClinicService {

    private DAO dao;

    /**
     * @return the dao
     */
    @Override
    public DAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    @Override
    public void setDao(DAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Map> getPrescriptionPatientsForDoctor(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT PAT.TW_PATIENT_ID,MAX(PAT.PATIENT_NME) PATIENT_NME,MAX(PAT.MOBILE_NO) MOBILE_NO"
                    + " FROM TW_PRESCRIPTION_MASTER PM,TW_PATIENT PAT"
                    + " WHERE PM.TW_PATIENT_ID=PAT.TW_PATIENT_ID"
                    + " AND PM.TW_DOCTOR_ID=" + doctorId + ""
                    + " GROUP BY PAT.TW_PATIENT_ID"
                    + " ORDER BY PATIENT_NME";
            list = this.getDao().getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getPrescriptionListing(String fromDate, String toDate, String doctorId, String clinicId, String patientId) {
        List<Map> list = null;
        try {
            String query = "SELECT TPM.TW_PRESCRIPTION_MASTER_ID,PT.PATIENT_NME,TPM.REMARKS,TO_CHAR(TPM.PREPARED_DTE,'DD-MON-YYYY') PREPARED_DTE"
                    + " FROM TW_PRESCRIPTION_MASTER TPM,TW_PATIENT PT"
                    + " WHERE TPM.TW_PATIENT_ID=PT.TW_PATIENT_ID"
                    + " AND TPM.TW_DOCTOR_ID=" + doctorId + ""
                    + " AND TPM.TW_CLINIC_ID=" + clinicId + ""
                    + " AND TPM.TW_PATIENT_ID=" + patientId;
            String dates = "";
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                dates = " AND TO_DATE(TO_CHAR(TPM.PREPARED_DTE,'DD-MON-YYYY'),'DD-MON-YYYY') BETWEEN TO_DATE('" + fromDate + "','DD-MON-YYYY') "
                        + " AND TO_DATE('" + toDate + "','DD-MON-YYYY')";
            }

            list = this.getDao().getData(query + dates + " ORDER BY PT.PATIENT_NME");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getMedicalColleges(String medicalCollegeId) {
        List<Map> list = null;
        try {
            String query = "SELECT  *  FROM TW_MEDICAL_COLLEGE ORDER BY TITLE";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getCountries(String countryId) {
        List<Map> list = null;
        try {
            String query = "SELECT  *  FROM COUNTRY ORDER BY COUNTRY_NME";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveDoctorEducation(DoctorVO vo) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            String masterId = "";
            String prevId = "SELECT SEQ_TW_DOCTOR_EDUCATION_ID.NEXTVAL VMASTER FROM DUAL";
            List list = this.getDao().getJdbcTemplate().queryForList(prevId);
            if (list != null && list.size() > 0) {
                Map map = (Map) list.get(0);
                masterId = (String) map.get("VMASTER").toString();
            }

            query = "INSERT INTO TW_DOCTOR_EDUCATION(TW_DOCTOR_EDUCATION_ID,TW_DOCTOR_ID,"
                    + "TW_MEDICAL_DEGREE_ID,TW_MEDICAL_COLLEGE_ID,COUNTRY_ID,CITY_ID,DATE_FROM,"
                    + "DATE_TO,PREPARED_BY)"
                    + " VALUES (" + masterId + "," + vo.getDoctorId() + ","
                    + "" + vo.getMedicalDegreeId() + ","
                    + "" + vo.getMedicalCollegeId() + ","
                    + "" + vo.getCountryId() + ","
                    + "" + vo.getCityId() + ","
                    + "TO_DATE('" + vo.getDurationEduFrom() + "','MM-YYYY'),"
                    + "TO_DATE('" + vo.getDurationEduTo() + "','MM-YYYY'),"
                    + "'" + vo.getUserName() + "')";
            arr.add(query);
            flag = this.dao.insertAll(arr, vo.getUserName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getDoctorEducation(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT DE.TW_DOCTOR_EDUCATION_ID,DE.TW_DOCTOR_ID,DE.TW_MEDICAL_DEGREE_ID,DE.TW_MEDICAL_COLLEGE_ID,"
                    + " TO_CHAR(DE.DATE_FROM,'MON-YYYY') DATE_FROM,TO_CHAR(DE.DATE_TO,'MON-YYYY') DATE_TO,MC.TITLE,MC.COUNTRY_ID,MC.CITY_ID,MD.ABBREV AS DEGREETITLE,MD.TITLE"
                    + " FROM TW_DOCTOR_EDUCATION DE,"
                    + " TW_MEDICAL_COLLEGE MC,TW_MEDICAL_DEGREE MD "
                    + " WHERE DE.TW_DOCTOR_ID=" + doctorId + ""
                    + " AND DE.TW_MEDICAL_DEGREE_ID=MD.TW_MEDICAL_DEGREE_ID"
                    + " AND DE.TW_MEDICAL_COLLEGE_ID =MC.TW_MEDICAL_COLLEGE_ID"
                    + " ORDER BY DE.TW_DOCTOR_EDUCATION_ID ";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveDoctorExperience(DoctorVO vo) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            String masterId = "";
            String prevId = "SELECT SEQ_TW_DOCTOR_EXPERIENCE_ID.NEXTVAL VMASTER FROM DUAL";
            List list = this.getDao().getJdbcTemplate().queryForList(prevId);
            if (list != null && list.size() > 0) {
                Map map = (Map) list.get(0);
                masterId = (String) map.get("VMASTER").toString();
            }

            query = "INSERT INTO TW_DOCTOR_EXPERIENCE(TW_DOCTOR_EXPERIENCE_ID,TW_DOCTOR_ID,"
                    + "TW_HOSPITAL_ID,DATE_FROM,"
                    + "DATE_TO,PREPARED_BY)"
                    + " VALUES (" + masterId + "," + vo.getDoctorId() + ","
                    + "" + vo.getHospitalId() + ","
                    + "TO_DATE('" + vo.getDurationExpFrom() + "','MM-YYYY'),"
                    + "TO_DATE('" + vo.getDurationExpTo() + "','MM-YYYY'),"
                    + "'" + vo.getUserName() + "')";
            arr.add(query);

            flag = this.dao.insertAll(arr, vo.getUserName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getHospitals(String hospitalId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_HOSPITAL ORDER BY TW_HOSPITAL_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getDoctorExperience(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT DE.TW_DOCTOR_EXPERIENCE_ID,DE.TW_DOCTOR_ID,DE.TW_HOSPITAL_ID,"
                    + " TO_CHAR(DE.DATE_FROM,'MON-YYYY') DATE_FROM,TO_CHAR(DE.DATE_TO,'MON-YYYY') DATE_TO,HP.TITLE,HP.COUNTRY_ID,HP.CITY_ID"
                    + " FROM TW_DOCTOR_EXPERIENCE DE,"
                    + " TW_HOSPITAL HP"
                    + " WHERE DE.TW_DOCTOR_ID=" + doctorId + ""
                    + " AND DE.TW_HOSPITAL_ID=HP.TW_HOSPITAL_ID"
                    + " ORDER BY DE.TW_DOCTOR_EXPERIENCE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getAssociations(String associationId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_ASSOCIATION ORDER BY TW_ASSOCIATION_ID ";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveDoctorAssociation(DoctorVO vo) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            String masterId = "";
            String prevId = "SELECT SEQ_TW_DOCTOR_ASSOCIATION_ID.NEXTVAL VMASTER FROM DUAL";
            List list = this.getDao().getJdbcTemplate().queryForList(prevId);
            if (list != null && list.size() > 0) {
                Map map = (Map) list.get(0);
                masterId = (String) map.get("VMASTER").toString();
            }

            query = "INSERT INTO TW_DOCTOR_ASSOCIATION(TW_DOCTOR_ASSOCIATION_ID,TW_DOCTOR_ID,"
                    + "TW_ASSOCIATION_ID)"
                    + " VALUES (" + masterId + "," + vo.getDoctorId() + ","
                    + "" + vo.getAssociationId() + ")";
            arr.add(query);

            flag = this.dao.insertAll(arr, vo.getUserName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getDoctorAssociation(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT DA.TW_DOCTOR_ASSOCIATION_ID,DA.TW_DOCTOR_ID,DA.TW_ASSOCIATION_ID,ASS.TITLE"
                    + " FROM TW_DOCTOR_ASSOCIATION DA,"
                    + " TW_ASSOCIATION ASS"
                    + " WHERE DA.TW_DOCTOR_ID=" + doctorId + ""
                    + " AND DA.TW_ASSOCIATION_ID=ASS.TW_ASSOCIATION_ID"
                    + " ORDER BY ASS.TITLE ";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getCities(String cityId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM CITY ORDER BY CITY_NME";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveDisease(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getDiseaseId() != null && !c.getDiseaseId().isEmpty()) {
                query = "UPDATE TW_DISEASE SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getDiseaseName()) + "'),"
                        + " SHOW_INTAKE_IND='" + c.getShowIntakeForm() + "'"
                        + " WHERE TW_DISEASE_ID=" + c.getDiseaseId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_DISEASE(TW_DISEASE_ID,TITLE,COMPANY_ID,SHOW_INTAKE_IND)"
                        + " VALUES (SEQ_TW_DISEASE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getDiseaseName().trim()) + "'),"
                        + "" + c.getCompanyId() + ",'" + c.getShowIntakeForm() + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getDiseases(String diseaseName) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT * FROM TW_DISEASE";

            if (diseaseName != null && !diseaseName.trim().isEmpty()) {
                where += " WHERE UPPER(TITLE) LIKE '%" + diseaseName.toUpperCase() + "%' ";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_DISEASE_ID");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean deleteDisease(String diseaseId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_DISEASE WHERE TW_DISEASE_ID=" + diseaseId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getDiseasesById(String diseasesId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_DISEASE WHERE TW_DISEASE_ID=" + diseasesId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean saveHospital(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getHospitalId() != null && !c.getHospitalId().isEmpty()) {
                query = "UPDATE TW_HOSPITAL SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getHospitalName()) + "'),"
                        + " COUNTRY_ID=" + c.getCountryId() + ","
                        + " CITY_ID=" + c.getCityId() + ","
                        + " ADDRESS='" + c.getHospitalAddress() + "'"
                        + " WHERE TW_HOSPITAL_ID=" + c.getHospitalId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_HOSPITAL(TW_HOSPITAL_ID,TITLE,COUNTRY_ID,CITY_ID,ADDRESS)"
                        + " VALUES (SEQ_TW_HOSPITAL_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getHospitalName()) + "'),"
                        + "" + c.getCountryId() + ","
                        + "" + c.getCityId() + ","
                        + "'" + c.getHospitalAddress() + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getHospitalById(String diseasesId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_HOSPITAL WHERE TW_HOSPITAL_ID=" + diseasesId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map> getHospital(String hospitalName) {
        List<Map> list = null;
        try {
            String query = "SELECT HP.TW_HOSPITAL_ID,HP.TITLE,HP.COUNTRY_ID,HP.CITY_ID,HP.ADDRESS,CN.COUNTRY_NME,"
                    + " CT.CITY_NME FROM TW_HOSPITAL HP,COUNTRY CN,CITY CT"
                    + " WHERE HP.COUNTRY_ID=CN.COUNTRY_ID"
                    + " AND HP.CITY_ID=CT.CITY_ID"
                    + " ORDER BY TW_HOSPITAL_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean deleteHospital(String hospitalId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_HOSPITAL WHERE TW_HOSPITAL_ID=" + hospitalId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveDegree(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getDegreeId() != null && !c.getDegreeId().isEmpty()) {
                query = "UPDATE TW_MEDICAL_DEGREE SET TITLE='" + Util.removeSpecialChar(c.getDegreeName().trim()) + "',"
                        + " ABBREV='" + Util.removeSpecialChar(c.getAbbreviation().trim()) + "'"
                        + " WHERE TW_MEDICAL_DEGREE_ID=" + c.getDegreeId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_MEDICAL_DEGREE(TW_MEDICAL_DEGREE_ID,TITLE,ABBREV)"
                        + " VALUES (SEQ_TW_MEDICAL_DEGREE_ID.NEXTVAL,'" + Util.removeSpecialChar(c.getDegreeName().trim()) + "',"
                        + "'" + Util.removeSpecialChar(c.getAbbreviation().trim()) + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getDegrees(String degreeName) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_DEGREE ORDER BY TW_MEDICAL_DEGREE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getDegreeById(String degreeId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_DEGREE WHERE TW_MEDICAL_DEGREE_ID=" + degreeId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteDegree(String degreeId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICAL_DEGREE WHERE TW_MEDICAL_DEGREE_ID=" + degreeId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveCountry(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getCountryId() != null && !c.getCountryId().isEmpty()) {
                query = "UPDATE COUNTRY SET COUNTRY_NME=INITCAP('" + Util.removeSpecialChar(c.getCountryName()) + "')"
                        + "WHERE COUNTRY_ID=" + c.getCountryId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO COUNTRY(COUNTRY_ID,COUNTRY_NME,COMPANY_ID)"
                        + " VALUES (SEQ_COUNTRY_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getCountryName()) + "'),"
                        + "" + c.getCompanyId() + ")";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getCountry(String countryName) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM COUNTRY ORDER BY COUNTRY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getCountryById(String countryId) {
        Map map = null;
        try {
            String query = "SELECT * FROM COUNTRY WHERE COUNTRY_ID=" + countryId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteCountry(String countryId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM COUNTRY WHERE COUNTRY_ID=" + countryId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveCity(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getCityId() != null && !c.getCityId().isEmpty()) {
                query = "UPDATE CITY SET CITY_NME=INITCAP('" + Util.removeSpecialChar(c.getCityName()) + "'),"
                        + " COUNTRY_ID=" + c.getCountryId() + ","
                        + " PROVINCE_ID=" + c.getProvinceId() + ""
                        + " WHERE CITY_ID=" + c.getCityId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO CITY(CITY_ID,CITY_NME,COUNTRY_ID,PROVINCE_ID,COMPANY_ID)"
                        + " VALUES (SEQ_CITY_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getCityName()) + "'),"
                        + "" + c.getCountryId() + ","
                        + "" + c.getProvinceId() + ","
                        + "" + c.getCompanyId() + ")";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveState(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getStateId() != null && !c.getStateId().isEmpty()) {
                query = "UPDATE STATE SET STATE_NME=INITCAP('" + Util.removeSpecialChar(c.getStateName().trim()) + "'),"
                        + " COUNTRY_ID=" + c.getCountryId() + ""
                        + " WHERE STATE_ID=" + c.getStateId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO STATE(STATE_ID,STATE_NME,COUNTRY_ID,TW_COMPANY_ID,PREPARED_DTE)"
                        + " VALUES (SEQ_STATE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getStateName().trim()) + "'),"
                        + "'" + c.getCountryId() + "',"
                        + "" + c.getCompanyId() + ",SYSDATE)";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveProcedure(String procedureId, String procedureName, String companyId) {
        boolean flag = false;
        try {
            String query = "";
            if (procedureId != null && !procedureId.isEmpty()) {
                query = "UPDATE TW_MEDICAL_PROCEDURE SET TITLE=INITCAP('" + Util.removeSpecialChar(procedureName.trim()) + "') "
                        + " WHERE TW_MEDICAL_PROCEDURE_ID=" + procedureId + "";
            } else {
                query = "INSERT INTO TW_MEDICAL_PROCEDURE(TW_MEDICAL_PROCEDURE_ID,TITLE,COMPANY_ID)"
                        + " VALUES (SEQ_TW_MEDICAL_PROCEDURE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(procedureName.trim()) + "'),"
                        + "" + companyId + ")";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteProcedure(String procedureId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICAL_PROCEDURE WHERE TW_MEDICAL_PROCEDURE_ID=" + procedureId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteCity(String cityId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM CITY WHERE CITY_ID=" + cityId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteState(String stateId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM STATE WHERE STATE_ID=" + stateId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getCityById(String cityId) {
        Map map = null;
        try {
            String query = "SELECT * FROM CITY WHERE CITY_ID=" + cityId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public Map getStateById(String stateId) {
        Map map = null;
        try {
            String query = "SELECT * FROM STATE WHERE STATE_ID=" + stateId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public Map getProcedureById(String procedureId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_PROCEDURE WHERE TW_MEDICAL_PROCEDURE_ID=" + procedureId + ""
                    + " ORDER BY TITLE";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public List<Map> getDoctorByAreaId(String areaId) {
        List<Map> list = null;
        try {
            String query = "SELECT CT.CITY_ID,CT.CITY_NME,CT.COUNTRY_ID,CT.COMPANY_ID,CN.COUNTRY_NME"
                    + " FROM CITY CT,COUNTRY CN"
                    + " WHERE CT.COUNTRY_ID=CN.COUNTRY_ID"
                    + " ORDER BY CT.CITY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Map> getAreaByCityId(String cityId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM CITY_AREA WHERE CITY_ID=" + cityId + " ORDER BY CITY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getCity(String cityName) {
        List<Map> list = null;
        try {
            String query = "SELECT CT.CITY_ID,CT.CITY_NME,CT.COUNTRY_ID,CT.COMPANY_ID,CN.COUNTRY_NME"
                    + " FROM CITY CT,COUNTRY CN"
                    + " WHERE CT.COUNTRY_ID=CN.COUNTRY_ID"
                    + " ORDER BY CT.CITY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getCitysOfPakistan() {
        List<Map> list = null;
        try {
            String query = "SELECT CT.CITY_ID,CT.CITY_NME,CT.COUNTRY_ID,CT.COMPANY_ID,CN.COUNTRY_NME"
                    + " FROM CITY CT,COUNTRY CN"
                    + " WHERE CT.COUNTRY_ID=CN.COUNTRY_ID AND CT.COUNTRY_ID=1"
                    + " ORDER BY CT.CITY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getState(String companyId) {
        List<Map> list = null;
        try {
            String query = "SELECT ST.STATE_ID,ST.STATE_NME,ST.COUNTRY_ID,ST.TW_COMPANY_ID,CN.COUNTRY_NME"
                    + " FROM STATE ST,COUNTRY CN"
                    + " WHERE ST.COUNTRY_ID=CN.COUNTRY_ID AND ST.TW_COMPANY_ID=" + companyId
                    + " ORDER BY ST.STATE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getProcedure(String companyId) {
        List<Map> list = null;
        try {
            String query = "SELECT *"
                    + " FROM TW_MEDICAL_PROCEDURE"
                    + " WHERE COMPANY_ID=" + companyId + ""
                    + " ORDER BY TITLE";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveMedicalServices(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getServiceId() != null && !c.getServiceId().isEmpty()) {
                query = "UPDATE TW_MEDICAL_SERVICE SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getServiceName()) + "')"
                        + " WHERE TW_MEDICAL_SERVICE_ID=" + c.getServiceId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_MEDICAL_SERVICE(TW_MEDICAL_SERVICE_ID,TITLE,COMPANY_ID)"
                        + " VALUES (SEQ_TW_MEDICAL_SERVICE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getServiceName()) + "'),"
                        + "" + c.getCompanyId() + ")";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMedicalServices(String serviceName) {
        List<Map> list = null;
        String where = "";
        try {
            String query = " SELECT * FROM TW_MEDICAL_SERVICE";

            if (serviceName != null && !serviceName.trim().isEmpty()) {
                where += " WHERE UPPER(TITLE) LIKE '%" + serviceName.toUpperCase() + "%' ";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_MEDICAL_SERVICE_ID ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getMedicalServiceById(String serviceId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_SERVICE WHERE TW_MEDICAL_SERVICE_ID=" + serviceId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteMedicalService(String serviceId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICAL_SERVICE WHERE TW_MEDICAL_SERVICE_ID=" + serviceId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveEducationInstitution(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getMedicalCollegeId() != null && !c.getMedicalCollegeId().isEmpty()) {
                query = "UPDATE TW_MEDICAL_COLLEGE SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getMedicalCollegeName()) + "'),"
                        + " COUNTRY_ID=" + c.getCountryId() + ","
                        + " CITY_ID=" + c.getCityId() + ""
                        + " WHERE TW_MEDICAL_COLLEGE_ID=" + c.getMedicalCollegeId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_MEDICAL_COLLEGE(TW_MEDICAL_COLLEGE_ID,TITLE,COUNTRY_ID,CITY_ID)"
                        + " VALUES (SEQ_TW_MEDICAL_COLLEGE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getMedicalCollegeName()) + "'),"
                        + "" + c.getCountryId() + ","
                        + "" + c.getCityId() + ")";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getEducationInstitutions(String educationInstitutionName) {
        List<Map> list = null;
        try {
            String query = "SELECT MC.TW_MEDICAL_COLLEGE_ID,MC.TITLE,MC.COUNTRY_ID,MC.CITY_ID,CN.COUNTRY_NME,"
                    + " CT.CITY_NME FROM TW_MEDICAL_COLLEGE MC,COUNTRY CN,CITY CT"
                    + " WHERE MC.COUNTRY_ID=CN.COUNTRY_ID"
                    + " AND MC.CITY_ID=CT.CITY_ID"
                    + " ORDER BY TW_MEDICAL_COLLEGE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getEducationInstitutionById(String educationInstitutionId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_COLLEGE WHERE TW_MEDICAL_COLLEGE_ID=" + educationInstitutionId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteDoctorEducation(String id) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_DOCTOR_EDUCATION WHERE TW_DOCTOR_EDUCATION_ID=" + id + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteDoctorExperience(String id) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_DOCTOR_EXPERIENCE WHERE TW_DOCTOR_EXPERIENCE_ID=" + id + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteDoctorAssociation(String id) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_DOCTOR_ASSOCIATION WHERE TW_DOCTOR_ASSOCIATION_ID=" + id + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteEducationInstitution(String educationInstitutionId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICAL_COLLEGE WHERE TW_MEDICAL_COLLEGE_ID=" + educationInstitutionId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveLabTest(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getLabTestId() != null && !c.getLabTestId().isEmpty()) {
                query = "UPDATE TW_LAB_TEST SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getLabTestName().trim()) + "'),"
                        + "TW_TEST_GROUP_ID=" + c.getTestGroupId() + " "
                        + " WHERE TW_LAB_TEST_ID=" + c.getLabTestId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_LAB_TEST(TW_LAB_TEST_ID,TITLE,COMPANY_ID,TW_TEST_GROUP_ID)"
                        + " VALUES (SEQ_TW_LAB_TEST_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getLabTestName().trim()) + "'),"
                        + "" + c.getCompanyId() + "," + c.getTestGroupId() + ")";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }
    
    @Override
    public boolean saveLabTestRate(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            if (c.getLabTestRate() != null && c.getLabTestRate().length > 0) {
                query = "DELETE FROM TW_LAB_TEST_RATE WHERE TW_LAB_MASTER_ID=" + c.getMedicalLabId() ;
                arr.add(query);
                for(int i= 0; i <c.getLabTestRate().length;i++){
                query = "INSERT INTO TW_LAB_TEST_RATE(TW_LAB_TEST_RATE_ID,TW_LAB_MASTER_ID,TW_LAB_TEST_ID,RATE,PREPARED_BY,PREPARED_DTE)"
                        + " VALUES (SEQ_TW_LAB_TEST_RATE_ID.NEXTVAL," + c.getMedicalLabId() + "," + c.getLabTestIds()[i] + ","
                        + "" + ( c.getLabTestRate()[i] != null && !c.getLabTestRate()[i].isEmpty() ? c.getLabTestRate()[i] : null ) + ",'" + c.getUserName() + "',SYSDATE)";
                arr.add(query);
                }
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getLabTests(String labTestName) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT  LT.*,TG.TITLE AS TEST_GROUP FROM TW_LAB_TEST LT,TW_TEST_GROUP TG";

            if (labTestName != null && !labTestName.trim().isEmpty()) {
                where += " WHERE UPPER(LT.TITLE) LIKE '%" + labTestName.toUpperCase() + "%' ";
            }
            list = this.dao.getData(query + where + " WHERE LT.TW_TEST_GROUP_ID=TG.TW_TEST_GROUP_ID(+) ORDER BY LT.TW_LAB_TEST_ID");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<Map> getLabTestRate(String id) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_LAB_TEST_RATE WHERE TW_LAB_MASTER_ID=" + id + " ORDER BY TW_LAB_TEST_RATE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getLabtestById(String labTestId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_LAB_TEST WHERE TW_LAB_TEST_ID=" + labTestId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteLabTest(String labTestId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_LAB_TEST WHERE TW_LAB_TEST_ID=" + labTestId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMedicineAttachments(Product c, String attachmentPath) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            if (c.getMedicineImage() != null && !c.getMedicineImage().isEmpty()) {
                String sep = File.separator;
                String picPath = attachmentPath + sep + c.getProductId() + sep;
                File folder = new File(picPath);
                if (!folder.exists()) {
                    boolean succ = (new File(picPath)).mkdir();
                }
                String image = Util.renameFileName(c.getMedicineImage().getOriginalFilename());
                c.getMedicineImage().transferTo(new File(folder + File.separator + image));

                query = "UPDATE TW_MEDICINE SET MED_IMG='" + image + "' WHERE TW_MEDICINE_ID=" + c.getProductId() + "";
                arr.add(query);
                flag = this.dao.insertAll(arr, c.getUserName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMedicine(Product c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            String masterId = "";
            if (c.getProductId() != null && !c.getProductId().isEmpty()) {
                masterId = c.getProductId();
                query = "UPDATE TW_MEDICINE SET PRODUCT_NME=INITCAP('" + Util.removeSpecialChar(c.getProductName()) + "'),"
                        + " GENERIC_NME=INITCAP('" + Util.removeSpecialChar(c.getProductGenericName()) + "'),"
                        + " THERAPEUTIC_CLASS=INITCAP('" + Util.removeSpecialChar(c.getTherapauticClass()) + "'),"
                        + " PRODUCT_DESC=INITCAP('" + Util.removeSpecialChar(c.getProductFeatures().trim()) + "'),"
                        + " TW_MEDICINE_TYP_ID=" + c.getProductType() + ""
                        + " WHERE TW_MEDICINE_ID=" + c.getProductId() + "";
                arr.add(query);
                arr.add("DELETE FROM TW_MEDICINE_DISEASE WHERE TW_MEDICINE_ID=" + masterId + "");
            } else {
                String prevId = "SELECT SEQ_TW_MEDICINE_ID.NEXTVAL VMASTER FROM DUAL";
                List list = this.getDao().getJdbcTemplate().queryForList(prevId);
                if (list != null && list.size() > 0) {
                    Map map = (Map) list.get(0);
                    masterId = (String) map.get("VMASTER").toString();
                }
                query = "INSERT INTO TW_MEDICINE(TW_MEDICINE_ID,PRODUCT_NME,GENERIC_NME,THERAPEUTIC_CLASS,TW_PHARMACEUTICAL_ID,"
                        + " PRODUCT_DESC,TW_MEDICINE_TYP_ID)"
                        + " VALUES (" + masterId + ",INITCAP('" + Util.removeSpecialChar(c.getProductName()) + "'),"
                        + "INITCAP('" + Util.removeSpecialChar(c.getProductGenericName()) + "'),"
                        + " INITCAP('" + Util.removeSpecialChar(c.getTherapauticClass()) + "')," + c.getPharmaCompanyId() + ","
                        + " INITCAP('" + Util.removeSpecialChar(c.getProductFeatures().trim()) + "'),"
                        + " " + c.getProductType() + ")";
                arr.add(query);
            }
            if (c.getMultiSelectDiseases() != null && c.getMultiSelectDiseases().length > 0) {
                for (int i = 0; i < c.getMultiSelectDiseases().length; i++) {
                    if (!c.getMultiSelectDiseases()[i].isEmpty()) {
                        arr.add("INSERT INTO TW_MEDICINE_DISEASE(TW_MEDICINE_DISEASE_ID,TW_MEDICINE_ID,TW_DISEASE_ID) VALUES ("
                                + " SEQ_TW_MEDICINE_DISEASE_ID.NEXTVAL," + masterId + "," + c.getMultiSelectDiseases()[i] + ")");
                    }
                }
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMedicineRep(Product c) {
        boolean flag = false;
        MD5 md = new MD5();
        String password = Util.generatePassword();
        String mdStr = md.calcMD5(password);
        Encryption pswdSec = new Encryption();
        String generatedPassword = pswdSec.encrypt(mdStr);
        try {
            List<String> arr = new ArrayList<>();
            String query = "";
            if (c.getPharmaRepId() != null && !c.getPharmaRepId().isEmpty()) {
                query = "UPDATE TW_PHARMA_RAP SET FULL_NME=INITCAP('" + Util.removeSpecialChar(c.getFullName().trim()) + "'),"
                        + " CONTACT_NO='" + c.getContactNo() + "',"
                        + " DESIGNITION='" + c.getDesignation() + "',"
                        + " TW_PHARMACEUTICAL_ID='" + c.getPharmaCompanyId() + "'"
                        + " WHERE TW_PHARMA_RAP_ID=" + c.getPharmaRepId() + "";
                arr.add(query);
            } else {
                String masterId = "";
                String prevId = "SELECT SEQ_TW_PHARMA_RAP_ID.NEXTVAL VMASTER FROM DUAL";
                List list = this.getDao().getJdbcTemplate().queryForList(prevId);
                if (list != null && list.size() > 0) {
                    Map map = (Map) list.get(0);
                    masterId = (String) map.get("VMASTER").toString();
                }
                query = "INSERT INTO TW_PHARMA_RAP(TW_PHARMA_RAP_ID,FULL_NME,CONTACT_NO,DESIGNITION,TW_PHARMACEUTICAL_ID)"
                        + " VALUES (" + masterId + ",INITCAP('" + Util.removeSpecialChar(c.getFullName().trim()) + "'),'"
                        + "" + c.getContactNo() + "','" + c.getDesignation() + "',"
                        + "" + c.getPharmaCompanyId() + ")";
                arr.add(query);
                arr.add("INSERT INTO TW_WEB_USERS(USER_NME,USER_PASSWORD,FIRST_NME,TW_PHARMACEUTICAL_ID,TW_PHARMA_RAP_ID) VALUES ("
                        + " '" + Util.removeSpecialChar(c.getContactNo()).trim() + "','" + generatedPassword + "',INITCAP('" + Util.removeSpecialChar(c.getFullName()) + "'),"
                        + " " + c.getPharmaCompanyId() + "," + masterId + ")");
//                arr.add("INSERT INTO TW_USER_RIGHT(TW_USER_RIGHT_ID,USER_NME,RIGHT_NME,CAN_ADD,CAN_EDIT,CAN_DELETE)"
//                        + "SELECT SEQ_TW_USER_RIGHT_ID.NEXTVAL,'" + Util.removeSpecialChar(c.getContactNo()).trim() + "',RIGHT_NME,'Y','Y','Y' FROM TW_ROLE_RIGHTS  WHERE TW_ROLE_ID=2");
            }
            flag = this.dao.insertAll(arr, c.getUserName());
            if (flag) {
                if (c.getPharmaRepId() == null || c.getPharmaRepId().isEmpty()) {
                    Util.sendSignUpMessage(Util.removeSpecialChar(c.getContactNo()).trim(),
                            Util.removeSpecialChar(c.getContactNo()).trim(), password);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMedicalRepAppointment(Product c) {
        boolean flag = false;
        try {
            String query = "";
            List<String> arr = new ArrayList();
            if (c.getPharmaRepId() != null && !c.getPharmaRepId().isEmpty()) {
                if (c.getBrickId() != null && c.getBrickId().length > 0) {
                    query = "DELETE FROM TW_RAP_BRICK WHERE TW_PHARMA_RAP_ID=" + c.getPharmaRepId();
                    arr.add(query);
                    for (int i = 0; i < c.getBrickId().length; i++) {
                        query = "INSERT INTO TW_RAP_BRICK(TW_RAP_BRICK_ID,TW_PHARMA_RAP_ID,TW_BRICK_MASTER_ID,PREPARED_BY,"
                                + " PREPARED_DTE)"
                                + " VALUES (SEQ_TW_RAP_BRICK_ID.NEXTVAL," + c.getPharmaRepId() + ","
                                + "" + c.getBrickId()[i] + ",'" + c.getUserName() + "',SYSDATE)";
                        arr.add(query);
                    }
                    flag = this.dao.insertAll(arr, c.getUserName());
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMedicines(String pharmaId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT  MED.TW_MEDICINE_ID,MED.PRODUCT_NME,MED.GENERIC_NME,MED.THERAPEUTIC_CLASS,"
                    + " MED.TW_PHARMACEUTICAL_ID,MED.PRODUCT_DESC,TMT.TW_MEDICINE_TYP_ID,TMT.TW_MEDICINE_TYP_NME ,PH.COMPANY_NME"
                    + " FROM TW_MEDICINE MED,TW_PHARMACEUTICAL PH,TW_MEDICINE_TYP TMT "
                    + " WHERE MED.TW_PHARMACEUTICAL_ID=PH.TW_PHARMACEUTICAL_ID AND MED.TW_MEDICINE_TYP_ID=TMT.TW_MEDICINE_TYP_ID";
            if (pharmaId != null && !pharmaId.isEmpty()) {
                where += " AND MED.TW_PHARMACEUTICAL_ID=" + pharmaId + "";
            }
            list = this.dao.getData(query + where + " ORDER BY MED.PRODUCT_NME");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getMedicinesRep(String pharmaCompanyId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT PR.TW_PHARMA_RAP_ID,PR.FULL_NME,PR.CONTACT_NO,PR.DESIGNITION,"
                    + " PR.TW_PHARMACEUTICAL_ID,PR.COUNTRY_ID,PH.COMPANY_NME"
                    + " FROM TW_PHARMA_RAP PR,TW_PHARMACEUTICAL PH"
                    + " WHERE PR.TW_PHARMACEUTICAL_ID=PH.TW_PHARMACEUTICAL_ID";
            if (pharmaCompanyId != null && !pharmaCompanyId.isEmpty()) {
                where += " AND PR.TW_PHARMACEUTICAL_ID=" + pharmaCompanyId + "";
            }
            list = this.dao.getData(query + where + " ORDER BY PR.FULL_NME");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getBricks(String pharmaCompanyId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT TBM.TITLE,TBM.TW_BRICK_MASTER_ID FROM TW_BRICK_MASTER TBM WHERE TBM.TW_PHARMACEUTICAL_ID="
                    + pharmaCompanyId + " ORDER BY TBM.TITLE";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getMedicineById(String medicineId) {
        Map map = null;
        try {
            String query = "SELECT MED.TW_MEDICINE_ID,MED.PRODUCT_NME,MED.GENERIC_NME,MED.THERAPEUTIC_CLASS,MED.TW_PHARMACEUTICAL_ID,"
                    + " MED.PRODUCT_DESC,TMT.TW_MEDICINE_TYP_ID,TMT.TW_MEDICINE_TYP_NME,DIS.DISEASES "
                    + " FROM TW_MEDICINE MED,TW_MEDICINE_TYP TMT,(SELECT TW_MEDICINE_ID,LISTAGG(TW_DISEASE_ID, ',') WITHIN GROUP (ORDER BY TW_DISEASE_ID) DISEASES"
                    + " FROM TW_MEDICINE_DISEASE"
                    + " GROUP BY TW_MEDICINE_ID) DIS"
                    + " WHERE MED.TW_MEDICINE_ID=" + medicineId + ""
                    + " AND MED.TW_MEDICINE_TYP_ID=TMT.TW_MEDICINE_TYP_ID AND MED.TW_MEDICINE_ID=DIS.TW_MEDICINE_ID(+)";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public Map getMedicalRepAppointmentById(String pharmaRepId) {
        Map map = null;
        try {
            String query = "SELECT LISTAGG(TW_BRICK_MASTER_ID, ',') WITHIN GROUP (ORDER BY TW_BRICK_MASTER_ID) AS BRICKS "
                    + "FROM TW_RAP_BRICK WHERE TW_PHARMA_RAP_ID=" + pharmaRepId;
            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
            list = this.getDao().getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public Map getMedicineRepById(String medicineRepId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_PHARMA_RAP WHERE TW_PHARMA_RAP_ID=" + medicineRepId;

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteMedicine(String medicineId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICINE WHERE TW_MEDICINE_ID=" + medicineId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteMedicineRep(String medicineRepId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_PHARMA_RAP WHERE TW_PHARMA_RAP_ID=" + medicineRepId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMessage(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getMessageId() != null && !c.getMessageId().isEmpty()) {
                query = "UPDATE TW_SMS_TEMPLATE SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getSubject()) + "'),"
                        + " DETAIL='" + c.getMessage() + "'"
                        + " WHERE TW_SMS_TEMPLATE_ID=" + c.getMessageId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_SMS_TEMPLATE(TW_SMS_TEMPLATE_ID,TITLE,DETAIL,TW_DOCTOR_ID,PREPARED_BY)"
                        + " VALUES (SEQ_TW_SMS_TEMPLATE_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getSubject()) + "'),"
                        + "'" + Util.removeSpecialChar(c.getMessage()) + "'," + c.getDoctorId() + ","
                        + "'" + c.getUserName() + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMessage(DoctorVO c) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_SMS_TEMPLATE WHERE TW_DOCTOR_ID=" + c.getDoctorId() + "";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean savePrintLayout(DoctorVO c, String attachmentPath) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        String headerLogo = "";
        String footerLogo = "";
        try {
            String query = "";
            String masterId = "";
            String prevId = "SELECT TW_PRINT_LAYOUT_ID FROM TW_PRINT_LAYOUT "
                    + " WHERE TW_DOCTOR_ID=" + c.getDoctorId() + " "
                    + " AND TW_CLINIC_ID=" + c.getClinicId() + "";
            List list = this.getDao().getJdbcTemplate().queryForList(prevId);
            if (list != null && list.size() > 0) {
                Map map = (Map) list.get(0);
                masterId = (String) map.get("TW_PRINT_LAYOUT_ID").toString();
            }

            if (masterId.isEmpty()) {
                prevId = "SELECT SEQ_TW_PRINT_LAYOUT_ID.NEXTVAL VMASTER FROM DUAL";
                list = this.getDao().getJdbcTemplate().queryForList(prevId);
                if (list != null && list.size() > 0) {
                    Map map = (Map) list.get(0);
                    masterId = (String) map.get("VMASTER").toString();
                }
                arr.add("INSERT INTO TW_PRINT_LAYOUT(TW_PRINT_LAYOUT_ID,TW_DOCTOR_ID,TW_CLINIC_ID) VALUES("
                        + "" + masterId + "," + c.getDoctorId() + "," + c.getClinicId() + ")");
            }
            String sep = File.separator;
            String picPath = attachmentPath + sep + c.getDoctorId() + sep;
            File folder = new File(picPath);
            if (!folder.exists()) {
                boolean succ = (new File(picPath)).mkdir();
            }
            if (c.getHeaderLogo() != null && !c.getHeaderLogo().isEmpty()) {
                headerLogo = new java.util.Date().getTime() + "_" + Util.renameFileName(c.getHeaderLogo().getOriginalFilename());
                c.getHeaderLogo().transferTo(new File(folder + File.separator + headerLogo));
                query = "UPDATE TW_PRINT_LAYOUT SET "
                        + " TOP_IMAGE='" + headerLogo + "'"
                        + " WHERE TW_PRINT_LAYOUT_ID=" + masterId + "";
                arr.add(query);
            }
            if (c.getFooterLogo() != null && !c.getFooterLogo().isEmpty()) {
                footerLogo = new java.util.Date().getTime() + "_" + Util.renameFileName(c.getFooterLogo().getOriginalFilename());
                c.getFooterLogo().transferTo(new File(folder + File.separator + footerLogo));
                query = "UPDATE TW_PRINT_LAYOUT SET "
                        + " BOTTOM_IMAGE='" + footerLogo + "'"
                        + " WHERE TW_PRINT_LAYOUT_ID=" + masterId + "";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getPrintLayouts(String doctorId, String clinicId) {
        List<Map> list = null;
        Map map = null;
        try {
            String query = "SELECT * FROM TW_PRINT_LAYOUT WHERE TW_DOCTOR_ID=" + doctorId + ""
                    + " AND TW_CLINIC_ID=" + clinicId + "";
            list = this.dao.getData(query);
            if (list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public Map getPrintLayoutById(String layoutId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_PRINT_LAYOUT WHERE TW_PRINT_LAYOUT_ID=" + layoutId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean saveDoctorAttachment(DoctorVO d, String path) {
        boolean flag = false;
        try {
            if (d.getDoctorId() != null && !d.getDoctorId().isEmpty()) {
                if (d.getFile() != null && !d.getFile().isEmpty()) {
                    String sep = File.separator;
                    String picPath = path + sep + d.getDoctorId() + sep;
                    File folder = new File(picPath);
                    if (!folder.exists()) {
                        boolean succ = (new File(picPath)).mkdir();
                    }
                    String fileFileName = new java.util.Date().getTime() + "_" + Util.renameFileName(d.getFile().getOriginalFilename());
                    d.getFile().transferTo(new File(folder + File.separator + fileFileName));
                    String query = "INSERT INTO TW_DOCTOR_ATTACHMENT (TW_DOCTOR_ATTACHMENT_ID,TW_DOCTOR_ID,FILE_NME,FILE_DESC,ATTACHMENT_TYP,PREPARED_BY,PREPARED_DTE) "
                            + " VALUES(SEQ_TW_DOCTOR_ATTACHMENT_ID.NEXTVAL," + d.getDoctorId() + ",'" + fileFileName + "',"
                            + " '" + d.getAttachDescription() + "','" + d.getAttachType() + "','" + d.getUserName() + "',SYSDATE)";
                    int i = this.getDao().getJdbcTemplate().update(query);
                    if (i > 0) {
                        flag = true;
                    }
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean updateProfileImage(DoctorVO d, String path) {
        boolean flag = false;
        String query = "";
        try {
            if (d.getDoctorId() != null && !d.getDoctorId().isEmpty()) {
                if (d.getFile() != null && !d.getFile().isEmpty()) {
                    String sep = File.separator;
                    String picPath = path + sep + d.getDoctorId() + sep;
                    File folder = new File(picPath);
                    if (!folder.exists()) {
                        boolean succ = (new File(picPath)).mkdir();
                    }
                    String fileFileName = new java.util.Date().getTime() + "_" + Util.renameFileName(d.getFile().getOriginalFilename());
                    d.getFile().transferTo(new File(folder + File.separator + fileFileName));
                    query = "UPDATE TW_DOCTOR SET PROFILE_IMAGE='" + fileFileName + "' "
                            + " WHERE TW_DOCTOR_ID=" + d.getDoctorId() + "";

                    int i = this.getDao().getJdbcTemplate().update(query);
                    if (i > 0) {
                        flag = true;
                    }
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public boolean updateVisitingCard(DoctorVO d, String path) {
        boolean flag = false;
        String query = "";
        try {
            if (d.getDoctorId() != null && !d.getDoctorId().isEmpty()) {
                if (d.getFile() != null && !d.getFile().isEmpty()) {
                    String sep = File.separator;
                    String picPath = path + sep + d.getDoctorId() + sep;
                    File folder = new File(picPath);
                    if (!folder.exists()) {
                        boolean succ = (new File(picPath)).mkdir();
                    }
                    String fileFileName = new java.util.Date().getTime() + "_" + Util.renameFileName(d.getFile().getOriginalFilename());
                    d.getFile().transferTo(new File(folder + File.separator + fileFileName));
                    query = "UPDATE TW_DOCTOR SET VISITING_CARD='" + fileFileName + "' "
                            + " WHERE TW_DOCTOR_ID=" + d.getDoctorId() + "";
                    int i = this.getDao().getJdbcTemplate().update(query);
                    if (i > 0) {
                        flag = true;
                    }
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public Map getBrickById(String brickId) {
        Map map = null;
        try {
            String query = "SELECT TBM.TITLE,LISTAGG(CA.CITY_ID,',') WITHIN GROUP (ORDER BY CA.CITY_AREA_ID) AS CITY_ID,"
                    + "LISTAGG(TBD.CITY_AREA_ID,',') WITHIN GROUP (ORDER BY TBD.TW_BRICK_DETAIL_ID) AS CITY_AREA_ID FROM TW_BRICK_MASTER TBM,TW_BRICK_DETAIL TBD,CITY_AREA CA"
                    + " WHERE TBM.TW_BRICK_MASTER_ID=TBD.TW_BRICK_MASTER_ID AND TBD.CITY_AREA_ID=CA.CITY_AREA_ID AND TBM.TW_BRICK_MASTER_ID=" + brickId + " GROUP BY (TBM.TITLE)";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean saveBrick(Brick b) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            String masterId = "";
            if (b.getBrickId() != null && !b.getBrickId().isEmpty()) {
                query = "DELETE FROM TW_BRICK_DETAIL WHERE TW_BRICK_MASTER_ID=" + b.getBrickId();
                arr.add(query);
                query = "UPDATE TW_BRICK_MASTER SET TITLE=INITCAP('" + Util.removeSpecialChar(b.getBrickName().trim()) + "')"
                        + " WHERE TW_BRICK_MASTER_ID=" + b.getBrickId();
                arr.add(query);
                if (b.getAreaId() != null && b.getAreaId().length > 0) {
                    for (int i = 0; i < b.getAreaId().length; i++) {
                        query = "INSERT INTO TW_BRICK_DETAIL(TW_BRICK_DETAIL_ID,TW_BRICK_MASTER_ID,CITY_AREA_ID)"
                                + " VALUES (SEQ_TW_BRICK_DETAIL_ID.NEXTVAL," + b.getBrickId() + ","
                                + b.getAreaId()[i] + ")";
                        arr.add(query);
                    }
                }
            } else {
                String prevId = "SELECT SEQ_TW_BRICK_MASTER_ID.NEXTVAL VMASTER FROM DUAL";
                List list = this.getDao().getJdbcTemplate().queryForList(prevId);
                if (list != null && list.size() > 0) {
                    Map map = (Map) list.get(0);
                    masterId = (String) map.get("VMASTER").toString();
                }

                query = "INSERT INTO TW_BRICK_MASTER(TW_BRICK_MASTER_ID,TITLE,TW_PHARMACEUTICAL_ID,PREPARED_BY)"
                        + " VALUES (" + masterId + ",INITCAP('" + Util.removeSpecialChar(b.getBrickName().trim()) + "'),"
                        + "" + b.getPharmaCompanyId() + ",'" + b.getUsername() + "')";
                arr.add(query);
                if (b.getAreaId() != null && b.getAreaId().length > 0) {
                    for (int i = 0; i < b.getAreaId().length; i++) {
                        query = "INSERT INTO TW_BRICK_DETAIL(TW_BRICK_DETAIL_ID,TW_BRICK_MASTER_ID,CITY_AREA_ID)"
                                + " VALUES (SEQ_TW_BRICK_DETAIL_ID.NEXTVAL," + masterId + "," + b.getAreaId()[i] + ")";
                        arr.add(query);
                    }
                }
            }
            flag = this.dao.insertAll(arr, b.getUsername());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteBrick(String brickId) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        String query = "";
        try {
            query = "DELETE FROM TW_BRICK_DETAIL WHERE TW_BRICK_MASTER_ID=" + brickId + "";
            arr.add(query);
            query = "DELETE FROM TW_BRICK_MASTER WHERE TW_BRICK_MASTER_ID=" + brickId + "";
            arr.add(query);

            flag = this.dao.insertAll(arr, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getDoctorAppointedPatients(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT PT.TW_PATIENT_ID,MAX(PT.PATIENT_NME) PATIENT_NME,MAX(PT.MOBILE_NO) MOBILE_NO"
                    + " FROM TW_APPOINTMENT AM,TW_PATIENT PT"
                    + " WHERE AM.TW_DOCTOR_ID=" + doctorId + ""
                    + " AND AM.TW_PATIENT_ID=PT.TW_PATIENT_ID"
                    + " GROUP BY PT.TW_PATIENT_ID"
                    + " ORDER BY PT.TW_PATIENT_ID ";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveMedicalAssociation(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            if (c.getLabTestId() != null && !c.getLabTestId().isEmpty()) {
                query = "UPDATE TW_ASSOCIATION SET TITLE=INITCAP('" + Util.removeSpecialChar(c.getLabTestName()) + "')"
                        + " WHERE TW_ASSOCIATION_ID=" + c.getLabTestId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_ASSOCIATION(TW_ASSOCIATION_ID,TITLE)"
                        + " VALUES (SEQ_TW_ASSOCIATION_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getLabTestName()) + "'))";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getAssociationById(String labTestId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_ASSOCIATION WHERE TW_ASSOCIATION_ID=" + labTestId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteAssociation(String labTestId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_ASSOCIATION WHERE TW_ASSOCIATION_ID=" + labTestId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean saveMedicalLab(DoctorVO c) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (c.getDiseaseId() != null && !c.getDiseaseId().isEmpty()) {
                query = "UPDATE TW_LABORATORY SET LABORATORY_NME=INITCAP('" + Util.removeSpecialChar(c.getDiseaseName()) + "'),"
                        + " RECOMENDED_IND='" + c.getShowIntakeForm() + "',"
                        + " PREPARED_BY='" + c.getUserName() + "'"
                        + " WHERE TW_LABORATORY_ID=" + c.getDiseaseId() + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_LABORATORY(TW_LABORATORY_ID,LABORATORY_NME,RECOMENDED_IND,PREPARED_BY)"
                        + " VALUES (SEQ_TW_LABORATORY_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(c.getDiseaseName().trim()) + "'),"
                        + "'" + c.getShowIntakeForm() + "','" + c.getUserName() + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, c.getUserName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMedicalLabs(String recommendedInd) {
        List<Map> list = null;
        try {
            String where = "";
            if (recommendedInd != null && recommendedInd.equalsIgnoreCase("Y")) {
                where = " WHERE RECOMENDED_IND='Y'";
            }
            String query = "SELECT * FROM TW_LABORATORY";
            list = this.dao.getData(query + where + " ORDER BY LABORATORY_NME");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean deleteMedicalLab(String diseaseId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_LABORATORY WHERE TW_LABORATORY_ID=" + diseaseId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getMedicalLabById(String diseasesId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_LABORATORY WHERE TW_LABORATORY_ID=" + diseasesId + "";
            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map> getPharmaArea(String pharmaCompanyId) {
        List<Map> list = null;
        try {
            String query = "SELECT CA.TW_PHARMA_AREA_ID,CA.AREA_NME,CT.CITY_NME FROM TW_PHARMA_AREA CA,CITY CT WHERE "
                    + "CA.CITY_ID=CT.CITY_ID AND CA.TW_PHARMACEUTICAL_ID=" + pharmaCompanyId + " ORDER BY CT.CITY_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean savePharmaArea(String areaName, String areaId, String cityId, String pharmaCompanyId) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (areaId != null && !areaId.isEmpty()) {
                query = "UPDATE TW_PHARMA_AREA SET AREA_NME=INITCAP('" + Util.removeSpecialChar(areaName.trim()) + "'),"
                        + " CITY_ID=" + cityId + ",TW_PHARMACEUTICAL_ID=" + pharmaCompanyId + " WHERE TW_PHARMA_AREA_ID=" + areaId + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_PHARMA_AREA(TW_PHARMA_AREA_ID,AREA_NME,CITY_ID,TW_PHARMACEUTICAL_ID)"
                        + " VALUES (SEQ_TW_PHARMA_AREA_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(areaName.trim())
                        + "')," + cityId + "," + pharmaCompanyId + ")";
                arr.add(query);
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deletePharmaArea(String areaId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_PHARMA_AREA WHERE TW_PHARMA_AREA_ID=" + areaId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getPharmaAreaById(String areaId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_PHARMA_AREA WHERE TW_PHARMA_AREA_ID=" + areaId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map> getCityArea(String cityId) {
        List<Map> list = null;
        try {
            String query = "SELECT CA.CITY_AREA_ID,CA.AREA_NME,CT.CITY_NME FROM CITY_AREA CA,CITY CT WHERE "
                    + "CA.CITY_ID=CT.CITY_ID AND CA.CITY_ID=" + cityId + " ORDER BY CA.AREA_NME";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getAreaByCitys(Brick b) {
        List<Map> list = null;
        try {
            if (b.getCityId() != null && b.getCityId().length > 0) {
                String ids = "";
                for (int i = 0; i < b.getCityId().length; i++) {
                    if (i > 0) {
                        ids += ",";
                    }
                    ids += b.getCityId()[i];
                }
                String query = "SELECT CA.CITY_AREA_ID,CA.AREA_NME,CT.CITY_NME FROM CITY_AREA CA,CITY CT WHERE "
                        + "CA.CITY_ID=CT.CITY_ID AND CA.CITY_ID IN (" + ids + ") ORDER BY CT.CITY_ID";
                list = this.dao.getData(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveCityArea(String areaName, String areaId, String cityId, String userName) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (areaId != null && !areaId.isEmpty()) {
                query = "UPDATE CITY_AREA SET AREA_NME=INITCAP('" + Util.removeSpecialChar(areaName.trim()) + "'),"
                        + " CITY_ID=" + cityId + " WHERE CITY_AREA_ID=" + areaId + "";
                arr.add(query);
            } else {
                query = "INSERT INTO CITY_AREA(CITY_AREA_ID,AREA_NME,CITY_ID,PREPARED_BY)"
                        + " VALUES (SEQ_CITY_AREA_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(areaName.trim())
                        + "')," + cityId + ",'" + userName + "')";
                arr.add(query);
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean deleteCityArea(String areaId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM CITY_AREA WHERE CITY_AREA_ID=" + areaId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getCityAreaById(String areaId) {
        Map map = null;
        try {
            String query = "SELECT * FROM CITY_AREA WHERE CITY_AREA_ID=" + areaId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map> getBrickByPharmaceuticalId(String pharmaceuticalId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_BRICK_MASTER WHERE TW_PHARMACEUTICAL_ID=" + pharmaceuticalId + " ORDER BY TITLE";

            list = this.getDao().getData(query);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getProvince() {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM PROVINCE ORDER BY PROVINCE_NME";

            list = this.getDao().getData(query);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveMedicineType(String medicineTypeId, String mdeicineTypeName, String username) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";

            if (medicineTypeId != null && !medicineTypeId.isEmpty()) {
                query = "UPDATE TW_MEDICINE_TYP SET TW_MEDICINE_TYP_NME=INITCAP('" + Util.removeSpecialChar(mdeicineTypeName) + "')"
                        + "WHERE TW_MEDICINE_TYP_ID=" + medicineTypeId + "";
                arr.add(query);
            } else {
                query = "INSERT INTO TW_MEDICINE_TYP(TW_MEDICINE_TYP_ID,TW_MEDICINE_TYP_NME,PREPARED_BY)"
                        + " VALUES (SEQ_TW_MEDICINE_TYP_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(mdeicineTypeName) + "'),'"
                        + "" + username + "')";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, username);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMedicineType(String medicineTypeName) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_MEDICINE_TYP ORDER BY TW_MEDICINE_TYP_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getMedicineTypeById(String medicineTypeId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICINE_TYP WHERE TW_MEDICINE_TYP_ID=" + medicineTypeId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteMedicineType(String medicineTypeId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICINE_TYP WHERE TW_MEDICINE_TYP_ID=" + medicineTypeId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    // Add Speciality
    @Override
    public boolean saveMedicalSpeciality(String specialityId, String specialityName, String showWebInd) {
        boolean flag = false;
        try {
            String query = "";
            if (specialityId != null && !specialityId.isEmpty()) {
                query = "UPDATE TW_MEDICAL_SPECIALITY SET TITLE=INITCAP('" + Util.removeSpecialChar(specialityName).trim() + "'),"
                        + " SHOW_ON_WEB='" + showWebInd + "'"
                        + " WHERE TW_MEDICAL_SPECIALITY_ID=" + specialityId + "";
            } else {
                query = "INSERT INTO TW_MEDICAL_SPECIALITY(TW_MEDICAL_SPECIALITY_ID,TITLE,SHOW_ON_WEB)"
                        + " VALUES (SEQ_TW_MEDICAL_SPECIALITY_ID.NEXTVAL,INITCAP('" + Util.removeSpecialChar(specialityName).trim() + "',"
                        + "'" + showWebInd + "'))";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getMedicalSpeciality(String specialityNameSearch) {
        List<Map> list = null;
        String where = "";
        try {
            String query = " SELECT * FROM TW_MEDICAL_SPECIALITY";

            if (specialityNameSearch != null && !specialityNameSearch.trim().isEmpty()) {
                where += " WHERE UPPER(TITLE) LIKE '%" + specialityNameSearch.toUpperCase() + "%' ";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_MEDICAL_SPECIALITY_ID ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getMedicalSpecialityById(String specialityId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_MEDICAL_SPECIALITY WHERE TW_MEDICAL_SPECIALITY_ID=" + specialityId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteMedicalSpeciality(String specialityId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_MEDICAL_SPECIALITY WHERE TW_MEDICAL_SPECIALITY_ID=" + specialityId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    // hospital Ward
    @Override
    public boolean saveHospitalWard(String wardId, String clinicId, String wardName, String beds, String userName) {
        boolean flag = false;
        try {
            String query = "";
            if (wardId != null && !wardId.isEmpty()) {
                query = "UPDATE TW_CLINIC_WARD SET WARD_NME=INITCAP('" + Util.removeSpecialChar(wardName).trim() + "'),"
                        + " TOTAL_BEDS=" + (beds.isEmpty() ? 0 : beds)
                        + " WHERE TW_CLINIC_WARD_ID=" + wardId + "";
            } else {
                query = "INSERT INTO TW_CLINIC_WARD(TW_CLINIC_WARD_ID,TW_CLINIC_ID,WARD_NME,TOTAL_BEDS,PREPARED_BY)"
                        + " VALUES (SEQ_TW_CLINIC_WARD_ID.NEXTVAL," + clinicId + ","
                        + " INITCAP('" + Util.removeSpecialChar(wardName).trim() + "'),"
                        + " " + (beds.isEmpty() ? 0 : beds) + ","
                        + " '" + userName + "')";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getHospitalWard(String clinicId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = " SELECT * FROM TW_CLINIC_WARD";

            if (clinicId != null && !clinicId.trim().isEmpty()) {
                where += " WHERE TW_CLINIC_ID=" + clinicId + "";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_CLINIC_WARD_ID ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getHospitalWardById(String wardId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_CLINIC_WARD WHERE TW_CLINIC_WARD_ID=" + wardId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteHospitalWard(String wardId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_CLINIC_WARD WHERE TW_CLINIC_WARD_ID=" + wardId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    // hospital room
    @Override
    public boolean saveHospitalRoom(String roomId, String clinicId, String roomName, String beds, String userName) {
        boolean flag = false;
        try {
            String query = "";
            if (roomId != null && !roomId.isEmpty()) {
                query = "UPDATE TW_CLINIC_ROOM SET ROOM_NME=INITCAP('" + Util.removeSpecialChar(roomName).trim() + "'),"
                        + " TOTAL_BEDS=" + (beds.isEmpty() ? 0 : beds)
                        + " WHERE TW_CLINIC_ROOM_ID=" + roomId + "";
            } else {
                query = "INSERT INTO TW_CLINIC_ROOM(TW_CLINIC_ROOM_ID,TW_CLINIC_ID,ROOM_NME,TOTAL_BEDS,PREPARED_BY)"
                        + " VALUES (SEQ_TW_CLINIC_ROOM_ID.NEXTVAL," + clinicId + ","
                        + " INITCAP('" + Util.removeSpecialChar(roomName).trim() + "'),"
                        + " " + (beds.isEmpty() ? 0 : beds) + ","
                        + " '" + userName + "')";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getHospitalRoom(String clinicId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = " SELECT * FROM TW_CLINIC_ROOM";

            if (clinicId != null && !clinicId.trim().isEmpty()) {
                where += " WHERE TW_CLINIC_ID=" + clinicId + "";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_CLINIC_ROOM_ID ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getHospitalRoomById(String roomId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_CLINIC_ROOM WHERE TW_CLINIC_ROOM_ID=" + roomId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteHospitalRoom(String roomId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_CLINIC_ROOM WHERE TW_CLINIC_ROOM_ID=" + roomId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    // patient
    @Override
    public boolean saveAdmitPatient(String hospitalPatientId, String roomId, String clinicId, String wardId, String patientId, String bedNo, String userName) {
        boolean flag = false;
        try {
            String query = "";
            if (hospitalPatientId != null && !hospitalPatientId.isEmpty()) {
                query = "UPDATE TW_CLINIC_PATIENT SET "
                        + " TW_CLINIC_ID=" + clinicId + ","
                        + " TW_PATIENT_ID=" + patientId + ","
                        + " TW_CLINIC_WARD_ID=" + (!wardId.isEmpty() ? wardId : null) + ","
                        + " TW_CLINIC_ROOM_ID=" + (!roomId.isEmpty() ? roomId : null) + ","
                        + " BED_NO=" + (bedNo.isEmpty() ? 1 : bedNo) + ""
                        + " WHERE TW_CLINIC_PATIENT_ID=" + hospitalPatientId + "";
            } else {
                query = "INSERT INTO TW_CLINIC_PATIENT(TW_CLINIC_PATIENT_ID,TW_CLINIC_ID,TW_PATIENT_ID,MR_NO,"
                        + "TW_CLINIC_WARD_ID,TW_CLINIC_ROOM_ID,BED_NO,PREPARED_BY)"
                        + " VALUES (SEQ_TW_CLINIC_PATIENT_ID.NEXTVAL," + clinicId + ","
                        + "" + patientId + ",'" + generateMrNo(clinicId) + "',"
                        + (!wardId.isEmpty() ? wardId : null) + "," + (!roomId.isEmpty() ? roomId : null)
                        + "," + (bedNo.isEmpty() ? 1 : bedNo) + ",'" + userName + "')";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    private String generateMrNo(String clinicId) {
        String mrNo = "";
        String query = "SELECT (NVL(MAX(MR_NO),0)+1) NEXT_MR_NO FROM TW_CLINIC_PATIENT"
                + " WHERE TW_CLINIC_ID=" + clinicId + "";

        List<Map> list = this.getDao().getData(query);
        if (list != null && list.size() > 0) {
            Map map = list.get(0);
            mrNo = map.get("NEXT_MR_NO").toString();
        }

        return mrNo;
    }

//    @Override
//    public boolean deleteAdmitPatient(String hospitalPatientId) {
//        boolean flag = false;
//        try {
//            String query = "DELETE FROM TW_CLINIC_PATIENT WHERE TW_CLINIC_PATIENT_ID=" + hospitalPatientId + "";
//            int num = this.dao.getJdbcTemplate().update(query);
//            if (num > 0) {
//                flag = true;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return flag;
//    }
    @Override
    public Map getAdmitPatientById(String hospitalPatientId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_CLINIC_PATIENT WHERE TW_CLINIC_PATIENT_ID=" + hospitalPatientId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map> getAdmitPatient(String clinicId, String statusInd) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT CP.TW_CLINIC_PATIENT_ID,TP.PATIENT_NME,TC.CLINIC_NME,CW.WARD_NME,CR.ROOM_NME"
                    + " FROM TW_CLINIC_PATIENT CP,TW_PATIENT TP,TW_CLINIC TC,TW_CLINIC_WARD CW,TW_CLINIC_ROOM CR"
                    + " WHERE CP.TW_PATIENT_ID=TP.TW_PATIENT_ID AND CP.TW_CLINIC_ID=TC.TW_CLINIC_ID"
                    + " AND CP.TW_CLINIC_WARD_ID=CW.TW_CLINIC_WARD_ID(+) AND CP.TW_CLINIC_ROOM_ID=CR.TW_CLINIC_ROOM_ID(+)"
                    + " AND STATUS_IND='" + statusInd + "'";

            if (clinicId != null && !clinicId.trim().isEmpty()) {
                where += " AND CP.TW_CLINIC_ID=" + clinicId + "";
            }

            list = this.dao.getData(query + where + " ORDER BY TW_CLINIC_PATIENT_ID ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveDischargeData(String hospitalPatientId, String dischargeDate, String remarks, String userName) {
        boolean flag = false;
        try {
            String query = "";
            if (hospitalPatientId != null && !hospitalPatientId.isEmpty()) {
                query = "UPDATE TW_CLINIC_PATIENT SET "
                        + " DISCHARGE_ON=TO_DATE('" + dischargeDate + "','DD-MM-YYYY'),"
                        + " DISCHARGE_REMARKS='" + remarks + "',"
                        + " DISCHARGE_BY='" + userName + "',"
                        + " STATUS_IND='D'"
                        + " WHERE TW_CLINIC_PATIENT_ID=" + hospitalPatientId + "";
            }
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    // Hospital Employee
    @Override
    public boolean saveHospitalEmployee(String employeeId, String clinicId, String fullName, String email, String loginId, String contactNo) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            MD5 md = new MD5();
            String password = Util.generatePassword();
            String mdStr = md.calcMD5(password);
            Encryption pswdSec = new Encryption();
            String generatedPassword = pswdSec.encrypt(mdStr);
            if (employeeId != null && !employeeId.isEmpty()) {
                arr.add("UPDATE TW_WEB_USERS SET FIRST_NME='" + Util.removeSpecialChar(fullName).trim() + "',"
                        + " EMAIL='" + Util.removeSpecialChar(email).trim() + "',"
                        + " CONTACT_NO='" + contactNo + "'"
                        + " WHERE UPPER(USER_NME)='" + loginId.toUpperCase() + "'");
            } else {
                arr.add("INSERT INTO TW_WEB_USERS(USER_NME,USER_PASSWORD,ACTIVE_IND,CONTACT_NO,FIRST_NME,"
                        + "EMAIL,TW_CLINIC_ID)"
                        + " VALUES ('" + Util.removeSpecialChar(loginId).toLowerCase() + "',"
                        + "'" + generatedPassword + "','Y','" + contactNo + "',"
                        + "INITCAP('" + Util.removeSpecialChar(fullName).trim() + "'),"
                        + "'" + Util.removeSpecialChar(email).trim() + "','" + clinicId + "' )");
                arr.add("INSERT INTO TW_USER_RIGHT(TW_USER_RIGHT_ID,USER_NME,RIGHT_NME,CAN_ADD,CAN_EDIT,CAN_DELETE)"
                        + "SELECT SEQ_TW_USER_RIGHT_ID.NEXTVAL,'" + Util.removeSpecialChar(loginId).toLowerCase() + "',RIGHT_NME,'Y','Y','Y' FROM TW_ROLE_RIGHTS  WHERE TW_ROLE_ID=5");
            }
            flag = this.dao.insertAll(arr, fullName);
            if (flag) {
                Util.sendSignUpMessage(contactNo, Util.removeSpecialChar(loginId).trim().toLowerCase(), password);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getHospitalEmployee(String clinicId) {
        List<Map> list = null;
        String where = "";
        try {
            String query = "SELECT WU.USER_NME,WU.EMAIL,WU.FIRST_NME,TC.CLINIC_NME FROM TW_WEB_USERS WU, TW_CLINIC TC"
                    + " WHERE WU.TW_CLINIC_ID=TC.TW_CLINIC_ID AND WU.TW_CLINIC_ID=" + clinicId + "";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Map getHospitalEmployeeById(String employeeId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_WEB_USERS WHERE UPPER(USER_NME)='" + employeeId.toUpperCase() + "'";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean deleteMessageTemplate(String templateId) {
        boolean flag = false;
        try {
            String query = "DELETE FROM TW_SMS_TEMPLATE WHERE TW_SMS_TEMPLATE_ID=" + templateId + "";
            int num = this.dao.getJdbcTemplate().update(query);
            if (num > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public Map getSmsTemplateById(String templateId) {
        Map map = null;
        try {
            String query = "SELECT * FROM TW_SMS_TEMPLATE WHERE TW_SMS_TEMPLATE_ID=" + templateId + "";

            List<Map> list = this.getDao().getData(query);
            if (list != null && list.size() > 0) {
                map = list.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean saveIntakeDisease(String specialityId, String[] diseasesId) {
        boolean flag = false;
        List<String> arr = new ArrayList();
        try {
            String query = "";
            query = "DELETE FROM TW_INTAKE_DISEASE WHERE TW_MEDICAL_SPECIALITY_ID=" + specialityId + "";
            arr.add(query);
            for (int i = 0; i < diseasesId.length; i++) {
                query = "INSERT INTO TW_INTAKE_DISEASE(TW_INTAKE_DISEASE_ID,TW_MEDICAL_SPECIALITY_ID,TW_DISEASE_ID,PREPARED_DTE)"
                        + " VALUES (SEQ_TW_INTAKE_DISEASE_ID.NEXTVAL," + specialityId + ","
                        + "" + diseasesId[i] + ",SYSDATE)";
                arr.add(query);
            }
            flag = this.dao.insertAll(arr, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Map> getIntakeDiseases(String specialityId) {
        List<Map> list = null;
        try {
            String query = "SELECT * FROM TW_INTAKE_DISEASE WHERE TW_MEDICAL_SPECIALITY_ID=" + specialityId + ""
                    + " ORDER BY TW_DISEASE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map> getIntakeDiseasesForDoctor(String doctorId) {
        List<Map> list = null;
        try {
            String query = "SELECT D.* FROM TW_INTAKE_DISEASE ID,TW_DISEASE D WHERE ID.TW_MEDICAL_SPECIALITY_ID IN "
                    + " (SELECT TW_MEDICAL_SPECIALITY_ID FROM TW_DOCTOR_SPECIALITY WHERE TW_DOCTOR_ID=" + doctorId + " )"
                    + " AND ID.TW_DISEASE_ID=D.TW_DISEASE_ID"
                    + " ORDER BY D.TW_DISEASE_ID";
            list = this.dao.getData(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
