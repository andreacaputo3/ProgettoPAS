package it.unisalento.pas.smartcitywastemanagement.di;

import org.springframework.stereotype.Component;

@Component
public class MySQLDBConnection implements IDBConnection{

    public void connetti(){
        System.out.println("Connesso al database MySQL");
    }

}
