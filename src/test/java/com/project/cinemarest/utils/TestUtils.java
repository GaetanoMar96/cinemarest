package com.project.cinemarest.utils;

import com.project.cinemarest.model.ClientInfo;
import java.util.UUID;

public class TestUtils {

    public static ClientInfo createClient(Integer age) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserId(UUID.fromString("41e822f6-d648-4a1c-acc3-44c8336b4665"));
        clientInfo.setIdMovie("64cbabdc44707918d678692c");
        clientInfo.setSeats(new String[]{"A1"});
        clientInfo.setIsStudent(true);
        clientInfo.setAge(age);
        clientInfo.setWallet(0);
        return clientInfo;
    }

    public static ClientInfo createClientToDelete(Integer age) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setUserId(UUID.fromString("41e822f6-d648-4a1c-acc3-44c8336b4665"));
        clientInfo.setIdMovie("64cbabdc44707918d678692c");
        clientInfo.setTicketId(99L);
        clientInfo.setSeats(new String[]{"C1"});
        clientInfo.setIsStudent(false);
        clientInfo.setAge(age);
        clientInfo.setWallet(10);
        return clientInfo;
    }
}
