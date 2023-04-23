package com.had.teleconsulting.teleconsulting.Services.Util;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Bean.User;
public interface giveEncryptDecrypt {

    String SECRET_KEY = "8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@";

    static void encryptDoctor(DoctorDetails doctorDetails) throws Exception {
        if(doctorDetails.getDoctorEmail()!=null)
            doctorDetails.setDoctorEmail(EncryptDecrypt.encrypt(doctorDetails.getDoctorEmail(), SECRET_KEY));
        if(doctorDetails.getDoctorFirstName()!=null)
                doctorDetails.setDoctorFirstName(EncryptDecrypt.encrypt(doctorDetails.getDoctorFirstName(), SECRET_KEY));
        if(doctorDetails.getDoctorLastName()!=null)
                doctorDetails.setDoctorLastName(EncryptDecrypt.encrypt(doctorDetails.getDoctorLastName(), SECRET_KEY));
        if(doctorDetails.getDoctorMobileNumber()!=null)
                doctorDetails.setDoctorMobileNumber(EncryptDecrypt.encrypt(doctorDetails.getDoctorMobileNumber(), SECRET_KEY));
        if(doctorDetails.getDoctorSpecialisation()!=null)
                doctorDetails.setDoctorSpecialisation(EncryptDecrypt.encrypt(doctorDetails.getDoctorSpecialisation(), SECRET_KEY));
    }
    static void decryptDoctor(DoctorDetails doctorDetails) throws Exception {
        if(doctorDetails.getDoctorEmail()!=null)
            doctorDetails.setDoctorEmail(EncryptDecrypt.decrypt(doctorDetails.getDoctorEmail(), SECRET_KEY));
        if(doctorDetails.getDoctorFirstName()!=null)
            doctorDetails.setDoctorFirstName(EncryptDecrypt.decrypt(doctorDetails.getDoctorFirstName(), SECRET_KEY));
        if(doctorDetails.getDoctorLastName()!=null)
            doctorDetails.setDoctorLastName(EncryptDecrypt.decrypt(doctorDetails.getDoctorLastName(), SECRET_KEY));
        if(doctorDetails.getDoctorMobileNumber()!=null)
            doctorDetails.setDoctorMobileNumber(EncryptDecrypt.decrypt(doctorDetails.getDoctorMobileNumber(), SECRET_KEY));
        if(doctorDetails.getDoctorSpecialisation()!=null)
            doctorDetails.setDoctorSpecialisation(EncryptDecrypt.decrypt(doctorDetails.getDoctorSpecialisation(), SECRET_KEY));

    }

    static void encryptUser(User user) throws Exception {
        if(user.getUserEmail()!=null)
            user.setUserEmail(EncryptDecrypt.encrypt(user.getUserEmail(),SECRET_KEY));
        if(user.getUserFirstName()!=null)
            user.setUserFirstName(EncryptDecrypt.encrypt(user.getUserFirstName(),SECRET_KEY));
        if(user.getUserLastName()!=null)
            user.setUserLastName(EncryptDecrypt.encrypt(user.getUserLastName(),SECRET_KEY));
        if(user.getUserMobileNumber()!=null)
            user.setUserMobileNumber(EncryptDecrypt.encrypt(user.getUserMobileNumber(),SECRET_KEY));
    }
    static void decryptUser(User user) throws Exception {
        if(user.getUserEmail()!=null)
            user.setUserEmail(EncryptDecrypt.decrypt(user.getUserEmail(),SECRET_KEY));
        if(user.getUserFirstName()!=null)
            user.setUserFirstName(EncryptDecrypt.decrypt(user.getUserFirstName(),SECRET_KEY));
        if(user.getUserLastName()!=null)
            user.setUserLastName(EncryptDecrypt.decrypt(user.getUserLastName(),SECRET_KEY));
        if(user.getUserMobileNumber()!=null)
            user.setUserMobileNumber(EncryptDecrypt.decrypt(user.getUserMobileNumber(),SECRET_KEY));
    }


    static void encryptPatient(PatientDetails patientDetails) throws Exception{
        if(patientDetails.getPatientDOB()!=null)
            patientDetails.setPatientDOB(EncryptDecrypt.encrypt(patientDetails.getPatientDOB(),SECRET_KEY));
        if(patientDetails.getPatientEmail()!=null)
                patientDetails.setPatientEmail(EncryptDecrypt.encrypt(patientDetails.getPatientEmail(),SECRET_KEY));
        if(patientDetails.getPatientGender()!=null)
                patientDetails.setPatientGender(EncryptDecrypt.encrypt(patientDetails.getPatientGender(),SECRET_KEY));
        if(patientDetails.getPatientFirstName()!=null)
                patientDetails.setPatientFirstName(EncryptDecrypt.encrypt(patientDetails.getPatientFirstName(),SECRET_KEY));
        if(patientDetails.getPatientLastName()!=null)
                patientDetails.setPatientLastName(EncryptDecrypt.encrypt(patientDetails.getPatientLastName(),SECRET_KEY));
        if(patientDetails.getPatientMobileNumber()!=null)
                    patientDetails.setPatientMobileNumber(EncryptDecrypt.encrypt(patientDetails.getPatientMobileNumber(),SECRET_KEY));

    }
    static void decryptPatient(PatientDetails patientDetails) throws Exception{
        if(patientDetails.getPatientDOB()!=null)
            patientDetails.setPatientDOB(EncryptDecrypt.decrypt(patientDetails.getPatientDOB(),SECRET_KEY));
        if(patientDetails.getPatientEmail()!=null)
            patientDetails.setPatientEmail(EncryptDecrypt.decrypt(patientDetails.getPatientEmail(),SECRET_KEY));
        if(patientDetails.getPatientGender()!=null)
            patientDetails.setPatientGender(EncryptDecrypt.decrypt(patientDetails.getPatientGender(),SECRET_KEY));
        if(patientDetails.getPatientFirstName()!=null)
            patientDetails.setPatientFirstName(EncryptDecrypt.decrypt(patientDetails.getPatientFirstName(),SECRET_KEY));
        if(patientDetails.getPatientLastName()!=null)
            patientDetails.setPatientLastName(EncryptDecrypt.decrypt(patientDetails.getPatientLastName(),SECRET_KEY));
        if(patientDetails.getPatientMobileNumber()!=null)
            patientDetails.setPatientMobileNumber(EncryptDecrypt.decrypt(patientDetails.getPatientMobileNumber(),SECRET_KEY));
        giveEncryptDecrypt.decryptUser(patientDetails.getUser());
    }
}
