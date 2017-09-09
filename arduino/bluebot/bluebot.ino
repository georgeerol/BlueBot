//BlueBot V1.0
//2017.05.03
//Clifton C. Craig

#include <Servo.h> //servo library
#define send

Servo myservo; // create servo object to control servo
char getstr;
int AUTONOMOUS = 1;
int MANUAL = 0;
int state = 0;
int Echo = A4;  
int Trig = A5; 
//Right wheel
int ENA = 11;
int in1 = 9;
int in2 = 8;

//Left wheel
int ENB = 5;
int in3 = 7;
int in4 = 6;

int ABS = 140;
int rightDistance = 0,leftDistance = 0,middleDistance = 0;
int THRESHOLD = 30;

void _mForward()
{
 analogWrite(ENA,ABS);
 analogWrite(ENB,ABS);
 digitalWrite(in1,LOW);
 digitalWrite(in2,HIGH);
 digitalWrite(in3,LOW);
 digitalWrite(in4,HIGH);
 Serial.println("go forward!");
}

void _mBack()
{
 analogWrite(ENA,ABS);
 analogWrite(ENB,ABS);
 digitalWrite(in1,HIGH);
 digitalWrite(in2,LOW);
 digitalWrite(in3,HIGH);
 digitalWrite(in4,LOW);
 Serial.println("go back!");
}

void _mleft()
{
 analogWrite(ENA,255);
 analogWrite(ENB,255);
 digitalWrite(in1,LOW);
 digitalWrite(in2,HIGH);
 digitalWrite(in3,HIGH);
 digitalWrite(in4,LOW);
 Serial.println("go left!");
}

void _mright()
{
 analogWrite(ENA,255);
 analogWrite(ENB,255);
 digitalWrite(in1,HIGH);
 digitalWrite(in2,LOW);
 digitalWrite(in3,LOW);
 digitalWrite(in4,HIGH);
 Serial.println("go right!");
} 
void _mStop()
{
  digitalWrite(ENA,LOW);
  digitalWrite(ENB,LOW);
  Serial.println("Stop!");
} 
 /*Ultrasonic distance measurement Sub function*/
int Distance_test()   
{
  delay(90);
  digitalWrite(Trig, LOW);   
  delayMicroseconds(2);
  digitalWrite(Trig, HIGH);  
  delayMicroseconds(30);
  digitalWrite(Trig, LOW);   
  float Fdistance = pulseIn(Echo, HIGH);  
  Fdistance= Fdistance/58;       
  return (int)Fdistance;
}  

void setup() 
{ 
  myservo.attach(3);// attach servo on pin 3 to servo object
  Serial.begin(9600);     
  pinMode(Echo, INPUT);    
  pinMode(Trig, OUTPUT);  
  pinMode(in1,OUTPUT);
  pinMode(in2,OUTPUT);
  pinMode(in3,OUTPUT);
  pinMode(in4,OUTPUT);
  pinMode(ENA,OUTPUT);
  pinMode(ENB,OUTPUT);
  _mStop();
  lookAhead();
  #ifdef debug
  debug();
  #endif
}

int lookNScan(int pos)
{
    myservo.write(pos);//setservo position according to scaled value
    delay(500); 
    return Distance_test();
}

void lookAhead()
{
    //setservo position according to scaled value
    middleDistance = lookNScan(90);
    #ifdef send
    Serial.print("middleDistance=");
    Serial.println(middleDistance);
    #endif
}

int lookOffRight()
{
      int offRight = lookNScan(65);

      #ifdef send
      Serial.print("offRight=");
      Serial.println(offRight);
      #endif
      return offRight;
}

int lookOffLeft()
{
      int offLeft = lookNScan(115);

      #ifdef send
      Serial.print("offLeft=");
      Serial.println(offLeft);
      #endif
      return offLeft;
}

void loop()
{ 
  getstr=Serial.read();
  if(getstr=='A') {
    stateChange();
  } else if(state == AUTONOMOUS) {
    autonomous();
  } else if(state == MANUAL) {
    processCmd();
  }

}

void processCmd()
{
 int distance = Distance_test();
 Serial.println("Distance " + distance);
 if(getstr=='f')
  {
    _mForward();
  }
  else if(getstr=='b')
  {
    _mBack();
    delay(200);
  }
  else if(getstr=='l')
  {
    _mleft();
    delay(200);
  }
  else if(getstr=='r')
  {
    _mright();
    delay(200);
  }
  else if(getstr=='s')
  {
     _mStop();     
  }
}

void stateChange()
{
  if(state == MANUAL)
    state = AUTONOMOUS;
  else if(state == AUTONOMOUS)
    state = MANUAL;
}

bool scanArea()
{
    lookAhead();
    int r = lookOffRight();
    int l = lookOffLeft();

    Serial.print("offRight=");
    Serial.print(r);
    Serial.print(" offLeft=");
    Serial.println(l);
    return middleDistance<=THRESHOLD || l<=THRESHOLD || r<=THRESHOLD;
}

void autonomous() 
{
    bool mightCollide = scanArea();
    if(mightCollide)
    {     
      handleCollision();
    }  
    else
        _mForward();                     
}

void handleCollision()
{
        _mStop();
      rightDistance = lookNScan(5);
      delay(1000);      

      #ifdef send
      Serial.print("rightDistance=");
      Serial.println(rightDistance);
      #endif

      leftDistance = lookNScan(180);
      delay(1000); 

      #ifdef send
      Serial.print("leftDistance=");
      Serial.println(leftDistance);
      #endif

      lookAhead();
      delay(500);
      if(rightDistance>leftDistance)  
      {
        _mBack();
        delay(500);
        _mright();
        delay(500);
       }
       else if(rightDistance<leftDistance)
       {
        _mBack();
        delay(500);
        _mleft();
        delay(500);
       }
       else if((rightDistance<=THRESHOLD)||(leftDistance<=THRESHOLD))
       {
        _mBack();
        delay(1500);
       }
       else
       {
        _mForward();
       }
}


void debug()
{
  Serial.print("Debugging! Enter l, r, f, b, or x to exit debug.");
  int resume = 1;
  while(resume == 1) {
    int last = ' ';
    if(Serial.available() > 0)
      last = Serial.read();
     switch(last)
     {
      case 'l':
        _mleft();
        break;
      case 'r':
        _mright();
        break;
      case 'f':
        _mForward();
        break;
      case 'b':
        _mBack();
        break;
      case 'x':
        Serial.print("Done Debugging!");
        return;
        break;
     }
      delay(500); 
     _mStop();
     last = ' ';
  }
}

