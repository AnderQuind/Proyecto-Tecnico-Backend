package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.ClientService;
import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.ClientInput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;
    private final PropertyService productService;

/*
    @PostMapping
    public ResponseEntity<Object> createClient(@RequestBody ClientInput clientInput){
        Client client = clientService.toDomainModel(clientInput);

        Client createdClient = new Client();
        try{
             createdClient = clientService.createClient(client);
         }catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
         }
        return new ResponseEntity<>(createdClient, HttpStatus.OK);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable String clientId) {
        return clientService.getClient(clientId)
                .map(client -> new ResponseEntity<>(client, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping()
    public ResponseEntity<Object> updateClient(@RequestBody ClientInput clientInput){
        try{
            Client updateClient = clientService.toDomainModel(clientInput);

            return clientService.updateClient(updateClient)
                    .map(client -> ResponseEntity.status(HttpStatus.OK).body((Object) client))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /*
    @DeleteMapping("/{ClientId}")
    public ResponseEntity<Object> deleteClientById(@PathVariable Long ClientId) {
        try{
            if (productService.deleteClientById(ClientId)) {
                return ResponseEntity.status(HttpStatus.OK).body("cliente eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cliente eliminado correctamente");
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }*/

}
