int incomingByte = 0;   // for incoming serial data

char st = '0';
#define LED 6
void setup() {
        Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
         pinMode(LED, OUTPUT);
         Serial.println("OK");
         digitalWrite(LED, HIGH);

         pinMode(13, OUTPUT);
         Serial.println("OK");
         digitalWrite(13, HIGH);
}

void loop() {
    // send data only when you receive data:
    if (Serial.available() > 0) {
      // read the incoming byte:
      char r = Serial.read();
     
      Serial.print(r);
      if(r=='1'){
        digitalWrite(12, HIGH);
        digitalWrite(13, HIGH);
        st = '1';
      }else if(r == '0'){             
         digitalWrite(12, LOW);
         digitalWrite(13, LOW);
          st = '0';
      }

      
    }
}

 
