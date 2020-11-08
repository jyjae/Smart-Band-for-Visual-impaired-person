# Smart-Band-for-Visual-impaired-person
======================

# 1. 개요
## 1.1. 주제
시각 장애인을 위한 스마트 밴드 ‘워키워키’이다.

## 1.2. 목적 / 목표
기존에 나와 있는 시각장애인 스마트 지팡이는 장애물을 감지할 수 있으나 사물과 사람의 구분이 불가능하다.<br>
그래서 사용자가 전방의 어떤 위험요소가 있는지 파악하는 것에 어려움이 있다.<br>
이러한 점을 보완하여 사용자의 안전성을 확보해 주고 비상시에 보호자에게 위험 알림을 주어 최대한 시각장애인을 보호할 수 있게 한다.

# 2. 구성
## 2.1. 하드웨어와 소프트웨어
* 소프트웨어
-아두이노 스케치
-라즈비안 OS
-안드로이드 스튜디오
-라즈베리 파이4

* 하드웨어
-wemos d1 mini
-초음파 센서(HC-SR04)
-진동 모듈(DM159)
-자이로센서(MPU9250)
-GPS 모듈(NEO 06MV2)

* 서버
-Apache
-PHP
-MySQL

## 2.2. 전체 프로그램
* JAVA 프로그램 목록
![java](https://user-images.githubusercontent.com/52684942/98462773-f66f6180-21f9-11eb-8469-f20e03271dbe.PNG)<br>

* PHP 프로그램 목록
![php](https://user-images.githubusercontent.com/52684942/98462775-f8d1bb80-21f9-11eb-8175-39ec01019e60.PNG)

* arduino 프로그램 목록
![arduino](https://user-images.githubusercontent.com/52684942/98462779-00916000-21fa-11eb-8a5e-abeed7531090.PNG)

## 3.1 소프트웨어 중요 기능
*관리자 기능
- 사용자가 스마트 밴드를 사용하는 동안 사용자 넘어짐을 감지하여 위급 상황임을 인지하는 경우에 보호자 앱으로 푸시 알림이 전송된다.<br>
- 사용자가 스마트 밴드를 사용하는 동안 사용자의 전방 또는 상방에 장애물이 감지되는 경우에 사용자에게 음성 또는 진동으로 알린다.<br>
- 보호자가 사용자의 제품 번호를 변경하는 경우에 protect DB 테이블에 값이 저장된다.<br>

*보호자 기능
- 사용자가 스마트 밴드를 사용하는 동안 사용자의 위치를 확인할 수 있다.<br>
- 사용자의 스마트 밴드 제품이 변경되는 경우, 앱을 통해서 사용자의 제품 번호를 변경할 수 있다.<br>

##3.2 HW 중요 기능
-초음파 센서로 상방 장애물을 감지하여 진동으로 알린다.
-텐서플로우 API와 카메라 모듈을 이용하여 전방 장애물을 감지하고 음성으로 장애물을 알린다.

## 3.3 HW
![smartband](https://user-images.githubusercontent.com/52684942/98462652-3550e780-21f9-11eb-9d24-0ecabb8cd1d6.PNG)

## 3.4. SW 사용자 인터페이스
![image](https://user-images.githubusercontent.com/52684942/98462389-629c9600-21f7-11eb-8bb0-4c647ce6e54b.png)<br>

## 3.5. DB 구성
### 3.5.1 gps 테이블
![gps](https://user-images.githubusercontent.com/52684942/98462470-fc644300-21f7-11eb-8c3c-61f06debf43e.PNG)
<br>

### 3.5.3 manager 테이블
![관리자](https://user-images.githubusercontent.com/52684942/98462473-01c18d80-21f8-11eb-9916-eb5abfaaeffb.PNG))<br>

### 3.5.4 user 테이블
![사용자](https://user-images.githubusercontent.com/52684942/98462477-05edab00-21f8-11eb-877a-178b27b72efc.PNG)<br>

### 3.5.5 protect 테이블
![보호자](https://user-images.githubusercontent.com/52684942/98462479-08500500-21f8-11eb-86f7-1c9cc6c82978.PNG)
<br>


    
 
