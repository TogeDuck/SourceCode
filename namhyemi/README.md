# 목차
- [2024년 01월 15일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240115
)
- [2024년 01월 16일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240116
)
- [2024년 01월 18일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240118
)
- [2024년 01월 19일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240119
)
- [2024년 01월 22일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240122
)
- [2024년 01월 23일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240123
)
- [2024년 01월 24일 회고](https://lab.ssafy.com/s10-webmobile4-sub2/S10P12A301/-/blob/master/namhyemi/README.md#anchor-20240124
)

## 2024.01.15 
### 프로젝트 기획 및 와이어프레임

![와이어프레임](/uploads/9ed17374f1a0891e0744dff749c7453a/롤링.png)

## 2024.01.16 
### 프로젝트 기획

데이터 제공을 위해 카페 점주와 주최자를 도입하는 것은 또 다른 데이터 문제를 불러와 다른 방안을 고민함
- 어플 기능을 생일 카페 이용자에 초점을 맞춰서 구체화하기로 하고 색다른 기능을 고민
    - 아이돌 팬들끼리의 실시간 위치 공유 (익명)
    - 동일한 카페(비슷한 위치)에 있는 사람들끼리 편하게 대화할 수 있는 채팅창 (익명) => 동행자 구하기, 굿즈 나눔 확대


## 2024.01.17
### 프로젝트 기획
- 생일 카페 정보 조회
- 일정 기록
- 유저 간의 상호작용 (나눔, 거래, 잡담)


### 와이어프레임
![20240117_와이어프레임](/uploads/382116282665f583abc189dbaf44ab6c/20240117_와이어프레임.PNG)

### ERD
![20240117_ERD](/uploads/683cf0b9e69668b4103281f4e2123195/20240117_ERD.png)


## 2024.01.18
### 요구사항 명세서
![20240118_요구사항명세서](/uploads/59ac1da7b58f63c52a3106debd937468/20240118_요구사항명세서.PNG)

### ERD
![20240118_ERD](/uploads/0cb7984f9994fcd216627caa49124a11/20240118_ERD.PNG)


##2024.01.19
### 기능 명세서
요구사항 명세서와 달리 개발 초점에 맞춰서 명세서를 작성해야 하는데, 어떤 형식으로 완성해야하는지 아직 협의 단계

### ERD
![20240119_ERD](/uploads/5c74946973d83dd8ef75062cb418a299/20240119_ERD.PNG)
컨설팅 후기를 담기 위해서 1대1 관계나 FK를 지우기 위해서 고민해봤지만, 그러자니 진행이 느려 우선은 완성을 목적으로 진행


## 2024.01.22
### ERD
![20240122_ERD](/uploads/32db4b483aa498ea88369cd0b1a91e61/20240122_ERD.PNG)

### 기능명세서
![20240122_기능명세서](/uploads/89f3e8eed124e3f7b7506618f17ace96/20240122_기능명세서.PNG)

### API 명세서
![20240122_API명세서](/uploads/072610077b536c64eb863f338109e9b9/20240122_API명세서.PNG)


## 2024.01.23
### WBS
![20240123_WBS](/uploads/a4e63b3c284e340ec3ee279bc6493ef5/image.png)

### ERD
![20240123_ERD](/uploads/019a4295ec6064a8f6904915f9d8eb63/image.png)

### API 명세서
![20240123_API명세서](/uploads/ad103f8f88700df1bea8c7fa4f933cc2/image.png)

### 팀미팅
회의록 작성 (노션)
![20240123_팀미팅](/uploads/28ddda586caff1be144d9095cdca0a28/image.png)


## 2024.01.24
### EC2 기초 설정
putty 를 통해서 EC2 환경 설정

### Gerrit 환경 설정 (GitLab 연동) 
EC2 서버에 있는 Gerrit 기본 설정과 GitLab Repository 연동
