package ec.edu.espe.meditrack.service;

import ec.edu.espe.meditrack.model.Appointment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

public class AppointmentServiceTest {

    @Test
    public void getValidAppointments_cincoCitas_emiteSoloLasTresValidas() {

        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getValidAppointments_todasInvalidas_emiteCitaGenerica() {

        // Arrange
        Flux<Appointment> invalidAppointments = Flux.just(

                /*
                 * Cita inválida porque el costo es igual a cero.
                 */
                new Appointment(
                        "I1",
                        "Paciente uno",
                        "Cardiología",
                        0.0,
                        List.of("paciente1@email.com")
                ),

                /*
                 * Cita inválida porque no tiene correos.
                 */
                new Appointment(
                        "I2",
                        "Paciente dos",
                        "Pediatría",
                        25.0,
                        List.of()
                )
        );

        AppointmentService service =
                new AppointmentService(invalidAppointments);

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextMatches(appointment ->
                        appointment.getId().equals("DEFAULT")
                )
                .verifyComplete();
    }

    @Test
    public void findById_idInexistente_terminaConError() {

        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Mono<Appointment> resultado =
                service.findById("NO-EXISTE");

        // Assert
        StepVerifier.create(resultado)
                .expectError(NoSuchElementException.class)
                .verify();
    }
}