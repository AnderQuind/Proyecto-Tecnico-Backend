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

    public void leasePropertyToClient(LeasePropertyInput leasePropertyInput){
        Client tenantClient = clientUseCase.getClient(leasePropertyInput.getIdClient())
                .orElseGet(() -> new Client(leasePropertyInput.getIdClient()));

        List<String> clientProperties = tenantClient.getPropertiesName();
        if (clientProperties == null) {
            clientProperties = new ArrayList<>();
            tenantClient.setPropertiesName(clientProperties);
        }
        clientProperties.add(leasePropertyInput.getPropertyName());
    }

    @Override
    public Optional<Client> getClient(String id) {
        return clientUseCase.getClient(id);
    }
}
