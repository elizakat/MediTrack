package ec.edu.espe.meditrack.service;

import ec.edu.espe.meditrack.model.Appointment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
public class AppointmentService {

    private final Flux<Appointment> appointments;

    /*
     * Aquí se crean las citas directamente en memoria.
     */
    public AppointmentService() {
        this(Flux.just(
                new Appointment(
                        "A1",
                        "Ana Pérez",
                        "Cardiología",
                        45.0,
                        List.of("ana@email.com")
                ),
                new Appointment(
                        "A2",
                        "Katherine Vargas",
                        "Pediatría",
                        35.0,
                        List.of("luis@email.com")
                ),
                new Appointment(
                        "A3",
                        "María Torres",
                        "Dermatología",
                        50.0,
                        List.of("maria@email.com")
                ),
                new Appointment(
                        "A4",
                        "Carlos Ruiz",
                        "Medicina General",
                        0.0,
                        List.of("carlos@email.com")
                ),
                new Appointment(
                        "A5",
                        "Carmen Pineida",
                        "Neurología",
                        60.0,
                        List.of()
                )
        ));
    }

    AppointmentService(Flux<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Flux<Appointment> getValidAppointments() {

        return appointments

                /*
                 * filter para las citas válidas
                 */
                .filter(appointment ->
                        appointment.getCostUsd() > 0
                                && !appointment.getNotifyEmails().isEmpty()
                )

                /*
                 * map transforma la especialidad escrita en mayúsculas.
                 */
                .map(appointment -> new Appointment(
                        appointment.getId(),
                        appointment.getPatientName(),
                        appointment.getSpecialty().toUpperCase(Locale.ROOT),
                        appointment.getCostUsd(),
                        appointment.getNotifyEmails()
                ))

                /*
                 * defaultIfEmpty devuelve una cita genérica cuando
                 * ninguna de las citas disponibles cumple las reglas.
                 */
                .defaultIfEmpty(createDefaultAppointment());
    }

    public Mono<Appointment> findById(String id) {

        return getValidAppointments()

                /*
                 * devuelve citas válidas.
                 */
                .filter(appointment ->
                        appointment.getId().equalsIgnoreCase(id)
                )

                /*
                 * next convierte el Flux en Mono
                 */
                .next()

                /*
                 * switchIfEmpty lanza un errorcuando no se encuentra la cita con el id solicitado.
                 */
                .switchIfEmpty(Mono.error(
                        new NoSuchElementException(
                                "No se encontró la cita con id: " + id
                        )
                ));
    }

    private Appointment createDefaultAppointment() {
        return new Appointment(
                "DEFAULT",
                "Paciente genérico",
                "MEDICINA GENERAL",
                1.0,
                List.of("notificaciones@meditrack.com")
        );
    }
}