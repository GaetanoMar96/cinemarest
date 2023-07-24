package com.project.cinemarest.controller;

import com.project.cinemarest.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cinema/users")
@RequiredArgsConstructor
public class UserControllerApi {

    @PostMapping(value = "/user", produces = {"application/json"})
    public ResponseEntity<Void> userLogIn(@RequestBody UserInfo userInfo) {
        //TODO studiare come si effettua un log in, username e pwd
        //se non ci sono dati in db ri fa una post per la registrazione (form)
        //in seguito si entra nell propria pagina
        return null;
    }
}
