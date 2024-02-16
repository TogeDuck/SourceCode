# 위키

# 🐥TOGEDUCK **- 생일 카페 올인원 플랫폼**

<p align="center">
    <img src="/docs/images/로고.png">
</p>


## **TOGEDUCK 링크 : 앱스토어 링크 추후**

## **소개 영상 : UCC 링크 추후**

## 📅 프로젝트 기간

2024.01.08(월) ~ 2024.01.16(금) (40일간 진행)

SSAFY 10기 2학기 공통 프로젝트 : TOGEDUCK

## 🌐 TOGEDUCK 배경

좋아하는 연예인이 있는 당신! 생일 카페 문화를 방문한 경험이 있으신가요?

생일 카페를 알아보고 방문하고 기록하는 과정 속에서 겪은 불편함은 없으셨나요?

`**‘TOGEDUCK’**` 을 통해서 생일 카페를 더 간단하게 즐겨보세요!

## 📄 **TOGEDUCK 개요**

> TOGETHER + DUCK
> 

TOGEDUCK 을 통해서 내 최애 아이돌의 생일 카페 정보를 지도로 한눈에 확인할 수 있습니다.

아이돌 별로 생일 카페 이벤트를 관리할 수 있기 때문에 다양한 아이돌들을 관심 등록할 수 있습니다. 최애 아이돌이 여러명일 경우에도 걱정하지 않아도 됩니다.

투어 시작과 종료를 통해서 내가 방문했던 경로와 카페들을 기록할 수 있습니다. 과거에 방문했던 생일 카페를 알아보기 위해 따로 작성하지 말고 기록해보세요.

투어 과정 중에는 나의 위치가 익명으로 실시간 공유됩니다. 얼마나 많은 사람들이 함께 생일 카페를 즐기고 있는지 확인할 수 있습니다. 

카페를 방문하고 나서는 채팅을 통해 다양한 사람들과 설레는 감정을 나눠보세요. 

채팅뿐만 아니라 자체 제작 굿즈를 나눔하고, 교환하고 싶었던 해당 아이돌 굿즈를 거래해보고, 더 이상 혼자가 싫다면 함께 할 사람을 구할 수도 있습니다.

이제는 더이상 나 혼자 즐기는 하루가 아닌 모두와 함께 즐기는 시간을 체험할 수 있습니다.

## **💎 주요 기능**

---

- 연예인
    - 본명/활동명/그룹명을 통해 자신의 최애 연예인을 설정할 수 있습니다.
    - 생일 카페 정보를 확인할 연예인을 선택 후 진행할 수 있습니다.
- 지도
    - 지도를 통해 자신의 위치를 확인할 수 있습니다.
    - 지도를 통해 실시간으로 생일 카페의 위치를 확인할 수 있습니다.
    - 실시간 정보를 on/off 를 통해서 현재 투어중인 사용자들의 위치와 인원수를 확인할 수 있습니다.
- 생일 카페
    - 현재/과거/미래에 따른 생일 카페의 정보를 확인할 수 있습니다.
    - 각각의 카페를 즐겨찾기 하여 확인할 수 있습니다.
- 투어
    - 생일카페 투어 기능을 제공합니다.
    - 시작버튼을 눌러 트래킹을 시작하며 투어중일때 근처 카페의 방문처리를 할 수 있습니다.
    - 종료버튼을 눌러 투어를 종료할 수 있으며 방문 기록을 지도로 확인할 수 있습니다.
- 퀘스트
    - 4가지(잡담/교환/나눔/모집)로 나뉘는 퀘스트를 각각의 목적에 따라 사용할 수 있습니다.
    - 잡담 : 생일 카페 별 간단한 후기를 확인할 수 있습니다.
    - 교환 : 게시글을 올린 후 교환 요청을 걸어 수락/거절을 통해 교환을 진행할 수 있습니다.
    - 나눔 :  게시글을 올려 현재 나눔중인 물품을 알릴 수 있습니다.
    - 모집 : 다음으로 갈 생일 카페를 선택하여 같이 갈 인원을 모집할 수 있습니다.

## ✅ **주요 기술**

---

**Backend**

- Springboot 3.2.2
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Web
- QueryDSL
- WebSocket
- Redis
- MariaDB
- Firebase Cloud Messaging

**Android**

- 

**Infra**

- S3
- AWS EC2
- JENKINS
- NGINX
- SSL
- Docker

## ✅ **프로젝트 파일 구조**

---

### **Frontend**

```
📦Android
 ┣ 📂.idea
 ┣ 📂TogeDuck
 ┃ ┣ 📂.idea
 ┃ ┣ 📂app
 ┃ ┃ ┣ 📂src
 ┃ ┃ ┃ ┣ 📂androidTest
 ┃ ┃ ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂idle
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂togeduck
 ┃ ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂idle
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂togeduck
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂di
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂view
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂favorite_setting
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂viewmodel
 ┃ ┃ ┃ ┃ ┣ 📂res
 ┃ ┃ ┃ ┃ ┃ ┣ 📂drawable
 ┃ ┃ ┃ ┃ ┃ ┣ 📂layout
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-anydpi-v26
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-hdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-mdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xxhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xxxhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂navigation
 ┃ ┃ ┃ ┃ ┃ ┣ 📂values
 ┃ ┃ ┃ ┃ ┃ ┣ 📂values-night
 ┃ ┃ ┃ ┃ ┃ ┗ 📂xml
 ┃ ┃ ┃ ┗ 📂test
 ┃ ┃ ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂idle
 ┗ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂togeduck
   ┗ 📂gradle
     ┗ 📂wrapper

```

### **Backend**

```
📦togeduck
 ┣ 📂domain
 ┃ ┣ 📂celebrity
 ┃ ┃ ┣ 📂controller
 ┃ ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂entity
 ┃ ┃ ┣ 📂repository
 ┃ ┃ ┗ 📂service
 ┃ ┣ 📂chat
 ┃ ┃ ┣ 📂controller
 ┃ ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂entity
 ┃ ┃ ┣ 📂repository
 ┃ ┃ ┗ 📂servic
 ┃ ┣ 📂event
 ┃ ┃ ┣ 📂config
 ┃ ┃ ┣ 📂controller
 ┃ ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂entity
 ┃ ┃ ┣ 📂repository
 ┃ ┃ ┗ 📂service
 ┃ ┣ 📂user
 ┃ ┃ ┣ 📂controller
 ┃ ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂entity
 ┃ ┃ ┣ 📂feign
 ┃ ┃ ┃ ┗ 📂google
 ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┣ 📂repository
 ┃ ┃ ┣ 📂serivce
 ┃ ┃ ┗ 📂util
 ┗ 📂global
   ┣ 📂config
   ┣ 📂handler
   ┣ 📂interceptor
   ┣ 📂response
   ┗ 📂util
```

## ✅ **협업 툴**

---

- Gitlab
    - 코드 버전 관리
    - 이슈 발행, 해결을 위한 토론
    - MR시, 팀원이 코드리뷰를 진행하고 피드백 게시
- 데일리 스크럼
    - 매일 아침 데일리 스크럼 진행, 전날 목표 달성량과 당일 할 업무 브리핑
    - 각자 위치에서 건네야 할 말이 생기면 팀원의 위치로 이동하여 전달
    - 빠른 소통과 신속한 대응 가능
- JIRA
    - 매주 목표량을 설정하여 Sprint 진행
    - 업무의 할당량을 정하여 Story Point를 설정하고, In-Progress -> Done 순으로 작업
    - 소멸 차트를 통해 프로젝트 진행도 확인
- Notion
    - 회의가 있을때마다 회의록을 기록하여 보관
    - 회의가 길어지지 않도록 다음날 제시할 안건을 미리 기록
    - 기술확보 시, 다른 팀원들도 추후 따라할 수 있도록 보기 쉽게 작업 순서대로 정리
    - 컨벤션 정리
    - 간트차트 관리
    - 스토리보드, 스퀀스다이어그램, 기능명세서 등 모두가 공유해야 하는 문서 관리
- Mattermost
    - 현재 작업 상황 공유
    - 실시간 정보 공유
    - GIt MR 알림
- Figma
    - 목업 제작, 와이어프레임제작, 디자인 작업 공유
- Adobe Illustrator
    - 프론트엔드 페이지 형태 상세 구성 제작
- Webex

## ✅ **팀원 역할 분배**

---

![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%201.png)

## ✅ **프로젝트 산출물**

---

- WBS
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%202.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%203.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%204.png)
    
- [**요구사항 명세서**](https://www.notion.so/eac1925d076c455bb5798a5dd6a72b7e?pvs=21)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%205.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%206.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%207.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%208.png)
    
- [**API 명세서**](https://www.notion.so/7fc8586400724899bdca039a4bf5215b?pvs=21)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%209.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2010.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2011.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2012.png)
    
- **[디자인&컨셉기획](https://www.notion.so/7a7894714d804835afdded38eb62d03e?pvs=21)**
- **시스템구성도**
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2013.png)
    
- **[와이어프레임](https://www.figma.com/file/ZzDzVc6nyHHVERPOGAeBQt/MockUp?type=whiteboard&node-id=0-1&t=Z9CUIHGOsyeUJ24h-0)**
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2014.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2015.png)
    
- **컨벤션**
- **[ERD](https://www.erdcloud.com/d/naSLT2ey5L87QN7KJ)**
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2016.png)
    
- JIRA
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2017.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2018.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2019.png)
    
    ![Untitled](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/Untitled%2020.png)
    

---

## ✅ **프로젝트 결과물**

- 포팅메뉴얼
- 중간발표자료
- 최종발표자료

## 📱 **TOGEDUCK 서비스 화면**

---

### 스플래쉬 화면

- 앱 처음 실행 시 
로딩 하는동안 뜨는 스플래쉬 화면

![1000002159.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/1000002159.gif)

### 관심 연예인 검색 화면

![1000002176.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/1000002176.gif)

![1000002179.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/1000002179.gif)

![1000002180.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/1000002180.gif)

### 생일카페 이벤트 화면

- 이벤트 정보
    
    

![이벤트움짤1.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EC%259D%25B4%25EB%25B2%25A4%25ED%258A%25B8%25EC%259B%2580%25EC%25A7%25A41.gif)

- 이벤트 즐겨 찾기

![이벤트움짤2_즐겨찾기.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EC%259D%25B4%25EB%25B2%25A4%25ED%258A%25B8%25EC%259B%2580%25EC%25A7%25A42_%25EC%25A6%2590%25EA%25B2%25A8%25EC%25B0%25BE%25EA%25B8%25B0.gif)

- 리뷰 작성

![이벤트움짤3_리뷰.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EC%259D%25B4%25EB%25B2%25A4%25ED%258A%25B8%25EC%259B%2580%25EC%25A7%25A43_%25EB%25A6%25AC%25EB%25B7%25B0.gif)

### 투어화면

- 투어 시작
    - 실시간 이동 반영
    - 생일 카페 방문 기록
- 투어 종료
    - 이동 경로 저장

### 채팅 화면

- 채팅

### 나눔 화면

- 나눔 글 확인

![나눔1.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EB%2582%2598%25EB%2588%25941.gif)

- 나눔 등록

![나눔2.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EB%2582%2598%25EB%2588%25942.gif)

### 교환 화면

- 교환 굿즈 등록

![교환1.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EA%25B5%2590%25ED%2599%25981.gif)

- 교환 신청

- 교환 거래 또는 수락

![교환2.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EA%25B5%2590%25ED%2599%25982.gif)

- 교환 채팅

![교환3.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EA%25B5%2590%25ED%2599%25983.gif)

### 모집 화면

- 모집 글 작성

![모집3_등록.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EB%25AA%25A8%25EC%25A7%25913_%25EB%2593%25B1%25EB%25A1%259D.gif)

- 모집 신청

![모집1_참여.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EB%25AA%25A8%25EC%25A7%25911_%25EC%25B0%25B8%25EC%2597%25AC.gif)

![모집2_채팅.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/%25EB%25AA%25A8%25EC%25A7%25912_%25EC%25B1%2584%25ED%258C%2585.gif)

### FCM 알림

- 교환 수락, 거절 시 알림

![ezgif.com-video-to-gif-converter.gif](%E1%84%8B%E1%85%B1%E1%84%8F%E1%85%B5%20997bd17026cf44efba953124e99585ab/ezgif.com-video-to-gif-converter.gif)
