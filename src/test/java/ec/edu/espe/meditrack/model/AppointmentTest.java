package ec.edu.espe.meditrack.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AppointmentTest {

    @Test
    public void getters_datosDelConstructor_devuelvenValoresCorrectos() {

        // Arrange
        List<String> emails = List.of("ana@email.com");

        Appointment appointment = new Appointment(
                "A1",
                "Carlos Martinez",
                "Psicologia",
                35.0,
                emails
        );

        // Act
        String id = appointment.getId();
        String patientName = appointment.getPatientName();
        String specialty = appointment.getSpecialty();
        Double costUsd = appointment.getCostUsd();
        List<String> notifyEmails = appointment.getNotifyEmails();

        // Assert
        assertEquals("A1", id);
        assertEquals("Carlos Martinez", patientName);
        assertEquals("Psicologia", specialty);
        assertEquals(35.0, costUsd, 0.001);
        assertEquals(emails, notifyEmails);
    }

    @Test
    public void constructor_listaOriginalModificada_noCambiaListaInterna() {

        // Arrange
        List<String> originalEmails = new ArrayList<>();
        originalEmails.add("paciente@email.com");

        Appointment appointment = new Appointment(
                "A2",
                "Luis Gómez",
                "Pediatría",
                35.0,
                originalEmails
        );

        // Act
        originalEmails.add("otro@email.com");

        List<String> internalEmails = appointment.getNotifyEmails();

        // Assert
        assertEquals(1, internalEmails.size());

        /*
         * Verifica que haya diferencia entre la lista orginal y la lista interna de la cita.
         */
        assertNotSame(originalEmails, internalEmails);
    }
}