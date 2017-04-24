
#include <Servo.h>
//create servo object to control servo
Servo myservo;
int ENA=10;
int IN1=9;
int IN2=8;
int ENB=5;
int IN3=7;
int IN4=6;

void setup()
{
  myservo.attach(3);// attach servo on pin 3 to servo object
  Serial.begin(9600);
  pinMode(IN1,OUTPUT);
  pinMode(IN2,OUTPUT);
  pinMode(IN3,OUTPUT);
  pinMode(IN4,OUTPUT);
  pinMode(ENA,OUTPUT);
  pinMode(ENB,OUTPUT);
  digitalWrite(ENA,HIGH);
  digitalWrite(ENB,HIGH);

}

void forward(){
  myservo.write(90);
  digitalWrite(IN1,HIGH);
  digitalWrite(IN3,HIGH);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);

}

void leftForward(){
  myservo.write(140);
  digitalWrite(IN3,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);
  digitalWrite(IN1,HIGH);
}
void rightForward(){
  myservo.write(40);
  digitalWrite(IN1,LOW);
  digitalWrite(IN2,LOW);
  digitalWrite(IN4,LOW);
  digitalWrite(IN3,HIGH);
}
void backward(){
  myservo.write(90);
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
    else if (command == 's') stop();
  }
}
