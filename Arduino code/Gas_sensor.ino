#include <SoftwareSerial.h>
#include <SPI.h>

SoftwareSerial BTSerial(10, 11); // RX | TX 

#define HEADER_H  0x42
#define HEADER_L  0x4D
#define FRAME_LENGTH  0x14

unsigned char cur_rx_data;
unsigned char pre_rx_data;
unsigned char main_status;
unsigned char recv_buff[256];
unsigned char recv_buff_index;
unsigned char incomingByte;

unsigned short check_sum;
int i;

int pm1_0_cf_1;
int pm2_5_cf_1;
int pm10_cf_1;

int pm1_0;
int pm2_5;
int pm10;

// setting analog pins to be called 'X_sensor'
int CO_sensor = A0;
int O3_low_sensor = A1;
int O3_high_sensor = A2;
int SO2_sensor = A3;
int NO2_sensor = A4;

// Set the initial X_sensorValue to 0
int CO_sensorValue = 0;
int SO2_sensorValue = 0;
int O3_low_sensorValue = 0;
int O3_high_sensorValue = 0;
int NO2_sensorValue = 0;


// The setup routine runs once when you press reset
void setup() {
  // Initialize serial communication at 9600 bits per second
  Serial.begin(9600);

  BTSerial.begin(9600);

  check_sum = 0;
  for(i=0;i< (sizeof(recv_buff)); i++) {
    recv_buff[i] = 0x00;
  }
  recv_buff_index = 0x00;
  cur_rx_data = 0;
  pre_rx_data = 0;
  main_status = 0;
  
  pinMode(CO_sensor, INPUT);
  pinMode(SO2_sensor, INPUT);
  pinMode(O3_low_sensor, INPUT);
  pinMode(O3_high_sensor, INPUT);
  pinMode(NO2_sensor, INPUT);

}


// The loop routine runs over and over again forever
void loop() {
  
  if (BTSerial.available() > 0) {
    // read the incoming byte:
     cur_rx_data = BTSerial.read();

     switch(main_status)
     {
        case 0:
          if( cur_rx_data == HEADER_L ) {
            if( pre_rx_data ==  HEADER_H ) {
                 main_status++;
                 check_sum += pre_rx_data;
                 check_sum += cur_rx_data;
                 cur_rx_data = 0;
                 pre_rx_data = 0;
             }
          }else{
            pre_rx_data = cur_rx_data;
          }
          break;
        case 1:
          if( cur_rx_data == FRAME_LENGTH ) {
            if( pre_rx_data ==  0x00 ) {
                 main_status++;
                 check_sum += pre_rx_data;
                 check_sum += cur_rx_data;
                 cur_rx_data = 0;
                 pre_rx_data = 0;
            }
          }else{
            pre_rx_data = cur_rx_data;
          }
          break;
        case 2:
          recv_buff[recv_buff_index++] = cur_rx_data;
          check_sum += cur_rx_data;
          if( recv_buff_index == 18) {
             main_status++;
             cur_rx_data = 0;
             pre_rx_data = 0;
          }
          break;
        case 3:
          recv_buff[recv_buff_index++] = cur_rx_data;
          if( recv_buff_index == 20) { 
            if( check_sum == ( (recv_buff[18]<<8) + recv_buff[19]) ) {
                recv_buff_index = 0;  
                pm1_0_cf_1 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
                recv_buff_index += 2;
                pm2_5_cf_1 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
                recv_buff_index += 2;
                pm10_cf_1 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
                recv_buff_index += 2;
                
                pm1_0 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
                recv_buff_index += 2;
                pm2_5 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
                recv_buff_index += 2;
                pm10 = (recv_buff[recv_buff_index] << 8) + recv_buff[recv_buff_index+1];
            }
            main_status = 0;    
            cur_rx_data = 0;
            pre_rx_data = 0; 
            check_sum = 0;
            for(i=0;i< (sizeof(recv_buff)); i++) {
              recv_buff[i] = 0x00;
            }
            recv_buff_index = 0x00;  
          }
          break;
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
          break;
       }
  }
  
  // Read the input on analog pins
  CO_sensorValue = analogRead(CO_sensor);
  SO2_sensorValue = analogRead(SO2_sensor);
  NO2_sensorValue = analogRead(NO2_sensor);
  O3_high_sensorValue = analogRead(O3_high_sensor);
  O3_low_sensorValue = analogRead(O3_low_sensor);
  
  // Print out the values
  Serial.flush();
  Serial.print(O3_high_sensorValue); // O_3
  Serial.print(",");
  Serial.print(SO2_sensorValue);
  Serial.print(",");
  Serial.print(NO2_sensorValue); // NO_2
  Serial.print(","); 
  Serial.print(CO_sensorValue);
  Serial.print(",");
  Serial.print(pm2_5);
  Serial.print(",");
  Serial.print(pm10);
  Serial.print(";");

  delay(1000); // send the data every 1s = 1000 ms
}
