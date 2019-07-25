package com.example.scanbluetooth;

public enum CommunicationArduinoEnum {
    DESLIGAR("0"),
    LIGAR("1"),
    STATUS("2");

    private String codeStates;

    CommunicationArduinoEnum(String codStatus){
        this.codeStates = codStatus;
    }

    public String getCodeStates(){
        return this.codeStates;
    }
}
