
#include <Servo.h>


//create servo object to control servo
Servo myservo;
int ENA=10;
int IN1=9;
int IN2=8;
int ENB=5;
int IN3=7;
int IN4=6;
int echoPin = A4;
int trigPin =A5;
boolean movingForward = false;
int rightDistance=0;
int leftDistance = 0;
int middleDistance=0;

void setup()
{
  myservo.attach(3);// attach servo on pin 3 to servo object
  Serial.begin(9600);
  pinMode(echoPin, INPUT);    
  pinMode(trigPin, OUTPUT);  
  pinMode(IN1,OUTPUT);
  pinMode(IN2,OUTPUT);
  pinMode(IN3,OUTPUT);
  pinMode(IN4,OUTPUT);
  pinMode(ENA,OUTPUT);
  pinMode(ENB,OUTPUT);
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);
  stop();

}

/*Ultrasonic distance measurement Sub function*/
int Distance_test()   
{
  digitalWrite(trigPin, LOW);   
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);  
  delayMicroseconds(20);
  digitalWrite(trigPin, LOW);   
  float Fdistance = pulseIn(echoPin, HIGH);  
  Fdistance= Fdistance/58;       
  return (int)Fdistance;
}  

void autoPilot(){
  myservo.write(90);
  delay(500);
  middleDistance =Distance_test();

      if(middleDistance<=10)
    {     
      stop();
      delay(500);                         
      myservo.write(5);          
      delay(1000);      
      rightDistance = Distance_test();

      delay(500);
       myservo.write(90);              
      delay(1000);                                                  
      myservo.write(180);              
      delay(1000); 
      leftDistance = Distance_test();

      delay(500);
      myservo.write(90);              
      delay(1000);
      if(rightDistance>leftDistance)  
      {
        rightForward();
        delay(180);
       }
       else if(rightDistance<leftDistance)
       {
        leftForward();
        delay(180);
       }
       else if((rightDistance<=20)||(leftDistance<=20))
       {
        backward();
        delay(180);
       }
       else
       {
        forward();
       }
    }  
    else
        forward();                     
}

void forward(){
  digitalWrite(IN1,HIGH);
  digitalWrite(IN3,HIGH);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);

}

void leftForward(){
  digitalWrite(IN3,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);
  digitalWrite(IN1,HIGH);
}
void rightForward(){
  digitalWrite(IN1,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);
  digitalWrite(IN3,HIGH);
}
void backward(){
  digitalWrite(IN1,LOW);
  digitalWrite(IN3,LOW);
  digitalWrite(IN2,HIGH);
  digitalWrite(IN4,HIGH);
}

void leftBackward(){
  digitalWrite(IN1,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN3,LOW);
  digitalWrite(IN4,HIGH);
}

void rightBackward(){
  digitalWrite(IN1,LOW);
  digitalWrite(IN3,LOW);
  digitalWrite(IN4,LOW);
  digitalWrite(IN2,HIGH);
}
void stop(){
  digitalWrite(IN1,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN3,LOW);
  digitalWrite(IN4,LOW);
}

void loop()
{
 if(Serial.available()){
    int command = Serial.read();
    if(command=='f')forward();
    else if(command == 'b') backward();
    else if(command =='l') leftForward();
    else if (command =='r') rightForward();
    else if (command == 'L') leftBackward();
    else if (command == 'R')rightBackward();
    else if(command =='a')autoPilot();
    else if (command == 's') stop();
  }
}
