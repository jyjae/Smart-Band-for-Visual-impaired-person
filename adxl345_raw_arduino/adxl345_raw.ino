#include "Wire.h"
#include "I2Cdev.h"
#include "ADXL345.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>
#define GPSBAUD 9600

typedef struct {
  String latiV;
  String longiV;
} GpsV;

int echoPin = D6;
int trigPin = D7;
float distance, duration;

ADXL345 accel;

int16_t ax, ay, az;

bool blinkState = false;

SoftwareSerial uart_gps(0, 2); // Wemos D1 Mini D4 (GPIO 0) RX, D3 (GPIO 2) TX
TinyGPS gps;

int Led_OnBoard = LED_BUILTIN;                  // Initialize the Led_OnBoard

const char* ssid = "sdy";                  // Your wifi Name
const char* password = "11223344";          // Your wifi Password

const char* host = "172.20.10.5"; //Your pc or server (database) IP
const uint16_t port = 80;

GpsV getgps(TinyGPS &gps);



void setup() {
  pinMode(Led_OnBoard, OUTPUT);
  Serial.begin(115200);
  // trig를 출력모드로 설정, echo를 입력모드로 설정
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(RX, OUTPUT);
  Wire.begin();

  Serial.println("Initializing I2C devices...");
  accel.initialize();
  Serial.println("Testing device connections...");
  Serial.println(accel.testConnection() ? "ADXL345 connection successful" : "ADXL345 connection failed");

  WiFi.mode(WIFI_OFF);        //Prevents reconnection issue (taking too long to connect)
  delay(1000);
  WiFi.mode(WIFI_STA);        //This line hides the viewing of ESP as wifi hotspot

  WiFi.begin(ssid, password);     //Connect to your WiFi router
  Serial.println("");
  Serial.print("Connecting");
  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(Led_OnBoard, LOW);
    delay(250);
    Serial.print(".");
    digitalWrite(Led_OnBoard, HIGH);
    delay(250);
  }

  digitalWrite(Led_OnBoard, HIGH);
  //If connection successful show IP address in serial monitor
  Serial.println("");
  Serial.println("Connected to Network/SSID");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());  //IP address assigned to your ESP

  uart_gps.begin(GPSBAUD);
}

int count = 0;  //넘어진 횟수 카운트

void loop() {
  // put your main code here, to run repeatedly:
  HTTPClient http;    //Declare object of class HTTPClient

  String lati, longi, danger; //파라미터
  String postData1, postData2, postDanger;
  GpsV gpsVal;

  while (uart_gps.available())    // While there is data on the RX pin...
  {
    int c = uart_gps.read();    // load the data into a variable...
    if (gps.encode(c))     // if there is a new valid sentence...
    {
      Serial.println("GPS available");
      gpsVal = getgps(gps); // then grab the data.
      ///////
      lati = gpsVal.latiV;
      longi = gpsVal.longiV;
      ////////////////////
      Serial.println("Lati" + lati + " Longi" + longi);



      analogWrite(RX, 0);

      // 초음파를 보낸다. 다 보내면 echo가 HIGH 상태로 대기하게 된다.
      digitalWrite(trigPin, HIGH);
      delay(10);
      digitalWrite(trigPin, LOW);

      duration = pulseIn(echoPin, HIGH);
      distance = (float)duration * 0.017;

      Serial.print("Distance:");
      Serial.print(distance);
      Serial.print("cm\n");

      if (distance < 50) {
        analogWrite(RX, 255);
        Serial.println("위에 물체가 있어요!!");
        delay(250);
      }


      // read raw accel measurements from device
      accel.getAcceleration(&ax, &ay, &az);

      // display tab-separated accel x/y/z values
      float pitch = (atan2( ax, sqrt(ay * ay + az * az)) * 180.0) / 3.14;
      float roll = (atan2( -ay, az) * 180.0) / 3.14;
      if (abs(pitch) > 44.5) {
        count += 1;
      }
      if (isnan(pitch) == 1 && abs(roll) > 75) {
        Serial.println("난일때!!!!!!!!!!!!!!!!!!!!!");
        count += 1;
      }

      if (count >= 4) {
        Serial. println("위험해요!!!!!!!!11");
        Serial. println("위험해요!!!!!!!!11");
        ///////faksdlfajdlaflgakfgldakfgaldfjg

        danger = "1";
        
        for (int i= 0; i < 3; i++) {
          postDanger = "lati=" + lati + "&longi=" + longi + "&danger=" + danger;
          http.begin("http://172.20.10.5/Nodemcu_db_record_view/InsertDB.php");              //Specify request destination
          http.addHeader("Content-Type", "application/x-www-form-urlencoded");    //Specify content-type header

          int httpCode1 = http.POST(postDanger); //Send the request
          String payload = http.getString();    //Get the response payload
          Serial.println(httpCode1);   //Print HTTP return code
          //Serial.println(httpCode2);   //Print HTTP return code
          Serial.println(payload);    //Print request response payload
          //Serial.println("LDR Value=" + LdrValueSend);
          //Serial.println("latitude: " + lati + " longitude: " + longi);
          http.end();  //Close connection

        }
        count = 0;
      }

      Serial.print("count: "); Serial.println(count);
      Serial.println("============================================");
      danger = "0";
      postData1 = "lati=" + lati;
      postData2 = "&longi=" + longi + "&danger=" + danger;
      http.begin("http://172.20.10.5/Nodemcu_db_record_view/InsertDB.php");              //Specify request destination
      http.addHeader("Content-Type", "application/x-www-form-urlencoded");    //Specify content-type header

      int httpCode1 = http.POST(postData1 + postData2); //Send the request
      // int httpCode2 = http.POST(postData2);
      String payload = http.getString();    //Get the response payload

      //Serial.println("LDR Value=" + ldrvalue);
      Serial.println(httpCode1);   //Print HTTP return code
      //Serial.println(httpCode2);   //Print HTTP return code
      Serial.println(payload);    //Print request response payload
      //Serial.println("LDR Value=" + LdrValueSend);
      Serial.println("latitude: " + lati + " longitude: " + longi);
      http.end();  //Close connection
    }
  }
}

// The getgps function will get and print the values we want.
GpsV getgps(TinyGPS &gps)
{
  float flati, flongi;
  char clati[10], clongi[10];
  String strlati, strlongi;
  // Then call this function
  gps.f_get_position(&flati, &flongi);

  //float to char
  dtostrf(flati, 8, 5, clati);
  dtostrf(flongi, 8, 5, clongi);

  //char to String
  strlati = clati;
  strlongi = clongi;

  GpsV val;
  val.latiV = strlati;
  val.longiV = strlongi;

  /*Serial.print("위도: ");
    Serial.println(lati);
    Serial.print("경도: ");
    Serial.println(longi);*/

  // Here you can print statistics on the sentences.
  unsigned long chars;
  unsigned short sentences, failed_checksum;
  gps.stats(&chars, &sentences, &failed_checksum);
  //Serial.print("Failed Checksums: ");Serial.print(failed_checksum);
  //Serial.println(); Serial.println();
  delay(1000);

  return val;
}
