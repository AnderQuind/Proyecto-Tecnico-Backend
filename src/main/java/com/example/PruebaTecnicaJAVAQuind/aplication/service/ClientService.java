package com.example.PruebaTecnicaJAVAQuind.aplication.service;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.error.CustomException;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.ClientInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor
public class ClientService implements ClientUseCase {

    private final ClientUseCase clientUseCase;
/*
    public Client toDomainModel(ClientInput clientInput) {

        Client client = clientUseCase.getClient(clientInput.getIdClient()).orElse(null);
        LocalDateTime creationDate = null;
        LocalDateTime modificationDate = null;

        if(client != null){
            creationDate = client.getCreationDate();
            modificationDate = client.getModificationDate();
        }

        return new Client(clientInput.getIdClient(), clientInput.getIdType(), clientInput.getIdNumber(),
                clientInput.getNames(), clientInput.getLastNames(), clientInput.getEmail(), clientInput.getBirthDate(),
                creationDate, modificationDate);
    }

    public static int calculateAge(Date birthDate) {
        LocalDate birthLocalDate = new java.sql.Date(birthDate.getTime()).toLocalDate();

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthLocalDate, currentDate);

        return period.getYears();
    }

    public static boolean isValidEmail(String email) {
        // Regular expression for validating email addresses
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object to match the input email with the pattern
        Matcher matcher = pattern.matcher(email);

        // Return true if the email matches the pattern, otherwise false
        return matcher.matches();
    }
/*
    @Override
    public Client createClient(Client client) {

        if(calculateAge(client.getBirthDate()) < 18){
            throw new CustomException(HttpStatus.FORBIDDEN, "Menores de edad no permitidos");
        }

        if(!isValidEmail(client.getEmail())){
            throw new CustomException(HttpStatus.FORBIDDEN, "Formato de email incorrecto");
        }

        if(client.getNames().length() < 2){
            throw new CustomException(HttpStatus.FORBIDDEN, "campos 'nombres' y 'apellidos' no pueden " +
                    "tener menos de 2 caracteres");
        }

        if(client.getLastNames().length() < 2){
            throw new CustomException(HttpStatus.FORBIDDEN, "campos 'nombres' y 'apellidos' no pueden " +
                    "tener menos de 2 caracteres");
        }

        Client clientIfExist = clientUseCase.getClient(client.getIdClient()).orElse(null);

        if(clientIfExist == null){
            client.setCreationDate(LocalDateTime.now());
            client.setModificationDate(LocalDateTime.now());
        } else {
            return clientIfExist;
        }

        return clientUseCase.createClient(client);
    }

    @Override
    public Optional<Client> getClient(Long id) {
        return clientUseCase.getClient(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientUseCase.getAllClients();
    }

    @Override
    public Optional<Client> updateClient(Client updateClient) {
        if(calculateAge(updateClient.getBirthDate()) < 18){
            throw new CustomException(HttpStatus.FORBIDDEN, "Menores de edad no permitidos");
        }

        if(!isValidEmail(updateClient.getEmail())){
            throw new CustomException(HttpStatus.FORBIDDEN, "Formato de email incorrecto");
        }

        if(updateClient.getNames().length() < 2){
            throw new CustomException(HttpStatus.FORBIDDEN, "campos 'nombres' y 'apellidos' no pueden " +
                    "tener menos de 2 caracteres");
        }

        if(updateClient.getLastNames().length() < 2){
            throw new CustomException(HttpStatus.FORBIDDEN, "campos 'nombres' y 'apellidos' no pueden " +
                    "tener menos de 2 caracteres");
        }

        updateClient.setModificationDate(LocalDateTime.now());

        return clientUseCase.updateClient(updateClient);
    }

    @Override
    public boolean deleteClient(Long id) {
        return clientUseCase.deleteClient(id);
    }
*/
    public void leasePropertyToClient(LeasePropertyInput leasePropertyInput){
        Client tenantClient = clientUseCase.getClient(leasePropertyInput.getIdClient())
                .orElseGet(() -> new Client(leasePropertyInput.getIdClient()));

        List<String> clientProperties = tenantClient.getPropertiesName();
        if (clientProperties == null) {
            clientProperties = new ArrayList<>();
            tenantClient.setPropertiesName(clientProperties);
        }
        clientProperties.add(leasePropertyInput.getPropertyName());
        /*
        Client tenantClient = clientUseCase.getClient(leasePropertyInput.getIdClient()).orElse(null);

        if(tenantClient == null){
            tenantClient = new Client();
            tenantClient.setIdClient(leasePropertyInput.getIdClient());
        }
        List<String> clientProperties = tenantClient.getPropertiesName();
        clientProperties.add(leasePropertyInput.getPropertyName());
        tenantClient.setPropertiesName(clientProperties);*/
    }

    @Override
    public Optional<Client> getClient(String id) {
        return clientUseCase.getClient(id);
    }
}
