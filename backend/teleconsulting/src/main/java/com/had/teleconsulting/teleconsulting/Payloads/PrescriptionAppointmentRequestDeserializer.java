package com.had.teleconsulting.teleconsulting.Payloads;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.Prescription;

import java.io.IOException;

class PrescriptionAppointmentRequestDeserializer extends JsonDeserializer<PrescriptionAppointmentRequestDTO> {

    @Override
    public PrescriptionAppointmentRequestDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract the Prescription and Appointment data from the JSON node
        JsonNode prescriptionNode = node.get("Prescription");
        JsonNode appointmentNode = node.get("Appointment");

        // Convert the JSON nodes to string and deserialize them into the corresponding objects
        Prescription prescription = objectMapper.readValue(prescriptionNode.toString(), Prescription.class);
        Appointment appointment = objectMapper.readValue(appointmentNode.toString(), Appointment.class);

        // Create a new PrescriptionAppointmentRequestDTO object and set the Prescription and Appointment objects
        PrescriptionAppointmentRequestDTO request = new PrescriptionAppointmentRequestDTO();
        request.setPrescription(prescription);
        request.setAppointment(appointment);

        return request;
    }
}