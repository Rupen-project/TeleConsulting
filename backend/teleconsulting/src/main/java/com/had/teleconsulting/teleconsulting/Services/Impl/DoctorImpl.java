package com.had.teleconsulting.teleconsulting.Services.Impl;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.had.teleconsulting.teleconsulting.Bean.*;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Repository.AppointmentRepo;
import com.had.teleconsulting.teleconsulting.Repository.DoctorRepo;
import com.had.teleconsulting.teleconsulting.Repository.PatientRepo;
import com.had.teleconsulting.teleconsulting.Repository.PrescriptionRepo;
import com.had.teleconsulting.teleconsulting.Services.DoctorService;
import com.had.teleconsulting.teleconsulting.Services.Util.EncryptDecrypt;
import com.had.teleconsulting.teleconsulting.Services.Util.giveEncryptDecrypt;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.support.NullValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;







@Service
public class DoctorImpl implements DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PrescriptionRepo prescriptionRepo;

    @Autowired
    private AppointmentRepo appointmentRepo;

    private DoctorDTO doctorDTO;

    private PatientDTO patientDTO;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${pdf.reportFileName}")
    private String reportFileName;

    @Value("${pdf.table.columnNames}")
    private List<String> columnNames;

    @Value("${application.bucket.name}")
    private String bucketName;

    private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);

    private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
    private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public PrescriptionDTO createPrescription(Map<String, Object> prescDetails){
        Prescription prescription = new Prescription();
        prescription.setEPrescription((String) prescDetails.get("ePrescription"));
        Prescription savedPrescription = this.prescriptionRepo.save(prescription);
        int aID = (int) prescDetails.get("appointmentID");
        Long apptID = Long.valueOf(aID);
        Optional<Appointment> appt = this.appointmentRepo.findById(apptID);
        if(appt!=null){
            appt.get().setPrescription(savedPrescription);
            this.appointmentRepo.save(appt.get());
        }
        return new ModelMapper().map(savedPrescription,PrescriptionDTO.class);
    }

    @Override
    public DoctorDTO loginDoctor(LoginModel loginModel) throws DoctorNotFoundException {
        String doctorEmail = null;
        try {
            doctorEmail = EncryptDecrypt.encrypt(loginModel.getEmail(), giveEncryptDecrypt.SECRET_KEY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String doctorPassword = loginModel.getPassword();


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        doctorEmail, doctorPassword
                )
        );


        DoctorDetails doctorDetails;
        try {
            doctorDetails = doctorRepo.findByDoctorEmail(doctorEmail);
            doctorDetails.setDoctorAvailable(1);
            doctorDetails.setDoctorQueueSize(10);
            doctorRepo.save(doctorDetails);
            giveEncryptDecrypt.decryptDoctor(doctorDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelMapper().map(doctorDetails, DoctorDTO.class);
    }

    @Override
    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        DoctorDetails doctorDetails = new ModelMapper().map(doctorDTO, DoctorDetails.class);
        try {
            doctorDetails.setDoctorEmail(doctorDTO.getDoctorEmail());
            doctorDetails.setDoctorPassword(new BCryptPasswordEncoder().encode(doctorDetails.getDoctorPassword()));
            giveEncryptDecrypt.encryptDoctor(doctorDetails);
            doctorRepo.save(doctorDetails);
            giveEncryptDecrypt.decryptDoctor(doctorDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ModelMapper().map(doctorDetails, DoctorDTO.class);
    }

    @Override
    public List<AppointmentDTO> getDoctorsAppointments(Long doctorID) {
        Optional<DoctorDetails> doctor = this.doctorRepo.findById(doctorID);
        List<AppointmentDTO> appointments = this.appointmentRepo.findAllBydoctorDetailsOrderByAppointmentDateDesc(doctor.get()).stream().map(appts ->
                new ModelMapper().map(appts,AppointmentDTO.class)).collect(Collectors.toList());
        List<AppointmentDTO> firstFiftyAppointments = appointments.stream().limit(50).collect(Collectors.toList());
        Map<PatientDetails,Integer> patientMap=new HashMap<PatientDetails,Integer>();
        Map<DoctorDetails,Integer> doctorMap=new HashMap<DoctorDetails,Integer>();
        for(int i = 0; i<firstFiftyAppointments.size(); i++){
            try {
                if(doctorMap.get(firstFiftyAppointments.get(i).getDoctorDetails())==null){
                    giveEncryptDecrypt.decryptDoctor(firstFiftyAppointments.get(i).getDoctorDetails());
                    doctorMap.put(firstFiftyAppointments.get(i).getDoctorDetails(),1);
                }
                if(patientMap.get(firstFiftyAppointments.get(i).getPatientDetails())==null){
                    giveEncryptDecrypt.decryptPatient(firstFiftyAppointments.get(i).getPatientDetails());
                    patientMap.put(firstFiftyAppointments.get(i).getPatientDetails(),1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return firstFiftyAppointments;
    }

    @Override
    public PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException {
        Optional<PatientDetails> patientDetails=this.patientRepo.findById(patientID);


        if(!patientDetails.isPresent()){
            throw new PatientNotFoundException("No patient available with provided patientID");
        }
        PatientDetails patientDetails1;
        try {
            patientDetails1=patientDetails.get();
            giveEncryptDecrypt.decryptPatient(patientDetails1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelMapper().map(patientDetails1,PatientDTO.class);
    }

    @Override
    public void logoutDoctor(Long doctorId) {
        DoctorDetails doctorDetails = this.doctorRepo.findById(doctorId).get();
        doctorDetails.setDoctorAvailable(0);
        doctorRepo.save(doctorDetails);
    }

    @Override
    public String uploadPrescription(Prescription prescription, Long patientID, Long appointmentID, Appointment appointment) {
        System.out.println("Prescription saving is :"+prescription.getMedicinesAndDosage());
        Document document = new Document();

        try {
            //ByteArrayOutputStream because of this it will be stored in memory and not write in disk
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            addDocTitle(document,prescription,appointmentID);
            createTable(document, 2,prescription);
            addFooter(document,prescription,appointment);
            document.close();
            byte[] pdfBytes = baos.toByteArray();
            String output = uploadToAws(pdfBytes,reportFileName,prescription,patientID,appointmentID, appointment);
            System.out.println(output);
            System.out.println("------------------Your Prescription is ready!-------------------------");

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "Prescription Uploaded successfully";
    }



    private void addDocTitle(Document document, Prescription prescription,Long appointmentID) throws DocumentException {
        System.out.println("Inside Title");
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
        Paragraph p1 = new Paragraph();
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph(reportFileName, COURIER));
        p1.setAlignment(Element.ALIGN_CENTER);
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph("Prescription given on " + localDateString, COURIER_SMALL));
        String doctorName = appointmentRepo.findDoctorNameByAppointmentID(appointmentID);
        String patientName = appointmentRepo.findPatientNameByAppointmentID(appointmentID);
        p1.add(new Paragraph("Prescribed by:  " + "Dr. "+ doctorName, COURIER_SMALL));
        p1.add(new Paragraph("Patient Name: " + patientName, COURIER_SMALL));
        leaveEmptyLine(p1,2);
        document.add(p1);
        Paragraph p2 = new Paragraph();
        p2.add(new Paragraph("Symptoms: "+prescription.getSymptoms(), COURIER_SMALL));
        document.add(p2);
        System.out.println("Outside Title");
    }

    private void createTable(Document document, int noOfColumns,Prescription prescription) throws DocumentException {
        System.out.println("Inside create table");
        Paragraph paragraph = new Paragraph();
        leaveEmptyLine(paragraph, 3);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(noOfColumns);
        columnNames = new ArrayList<>();
        columnNames.add("Medicines");
        columnNames.add("Dosage");
        for (int i = 0; i < noOfColumns; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.CYAN);
            table.addCell(cell);
        }

        table.setHeaderRows(1);
        getDbData(table,prescription);
        document.add(table);
    }

    private void getDbData(PdfPTable table,Prescription prescription) {
        System.out.println("Inside getDbData");
        if (prescriptionRepo == null)
            System.out.println("Prescription is null");
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        String medicines = prescription.getMedicinesAndDosage();
        String eachRow[] = medicines.split("\\$");
        for(String s: eachRow)
        {
            System.out.println(s);
            String eachColumn[] = s.split(":");
            String medicineName = eachColumn[0];
            String dosage = eachColumn[1];
            table.addCell(medicineName);
            table.addCell(dosage);
        }
    }

    private void addFooter(Document document, Prescription prescription, Appointment appointment) throws DocumentException {
        System.out.println("Inside footer");
        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 3);
        p2.setAlignment(Element.ALIGN_MIDDLE);
        p2.add(new Paragraph("Advice: " + prescription.getAdvice(), COURIER_SMALL));
//        Appointment appointment = appointmentRepo.findAppointmentByAppointmentID(appointmentID);
        if(appointment.getIsFollowUp().equals("true"))
            p2.add(new Paragraph("Follow up after: " + appointment.getFollowUpDay() +" days.", COURIER_SMALL));
        leaveEmptyLine(p2,2);
        p2.add(new Paragraph(
                "------------------------End Of " + reportFileName + "------------------------",
                COURIER_SMALL_FOOTER));
        document.add(p2);
        System.out.println("Outside footer");
    }

    private static void leaveEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private String uploadToAws(byte[] pdfBytes, String reportFileName,Prescription prescription,Long patientID, Long appointmentID, Appointment appointment) {
        System.out.println("Inside uploadtoaws");
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
        String localDateString1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss"));
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
        //tells S3 how many bytes are in the object being uploaded
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(pdfBytes.length);
        String folderName = "Prescription/"+patientID;
        String fileName = reportFileName + "-" + localDateString + ".pdf";
        String keyName = folderName + "/" + fileName;
        amazonS3.putObject(new PutObjectRequest(bucketName,keyName,bis,metadata));
        prescription.setEPrescription(fileName);
        //Setting uploaddate as full timestamp and uploading at aws as dd-mm-yyyy because my appointments is
        //showing dd-mm-yyyy
        prescription.setPrescriptionUploadDate(localDateString1);
        prescriptionRepo.save(prescription);
        Appointment appointmentAlready = appointmentRepo.findAppointmentByAppointmentID(appointmentID);
        System.out.println(appointment.getFollowUpDay());
        appointmentAlready.setFollowUpDay(appointment.getFollowUpDay());
        appointmentAlready.setIsFollowUp(appointment.getIsFollowUp());
        appointmentAlready.setPrescription(prescription);
        appointmentRepo.save(appointmentAlready);
        return keyName;
    }


}


