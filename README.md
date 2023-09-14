<img src="https://capsule-render.vercel.app/api?type=cylinder&color=auto&height=200&section=header&text=PETNexus&fontSize=90" />

# 🐾PetNexus
* 1인 가구가 늘어나는 요즘 세상. 반려동물을 위한 커뮤니티  ”PetNexus”를 소개합니다.
* Nexus : 연결, 연계, 관계, 집합체, 무언가의 중심을 뜻하는 영어 단어로써
* 반려동물 관련 정보 및 커뮤니티를 연결하는 역할을 하기위해 만들어 졌습니다.
* [펫넥스 브로셔](https://www.notion.so/PetNexus-2e88c713687f4cf28f1c16c61ba6519f)
* [펫넥스 홈페이지](https://petnexus.xyz/home)
<br/><br/>
  
# 🔎 주요기능

### ✅ 채팅 (Websoket)

- 오픈채팅방 게시물에 따른 채팅방 생성
- 중거거래 게시물에 따른 채팅방 생성<br/>
![3D2BFFE5-715F-4DCE-ACF2-37F9AC12836F_1_105_c](https://github.com/JihyeChu/PetNexus/assets/51440636/5980f486-981c-4282-83a2-463656fffee2)
#

### ✅ 지도 (kakao Map)

- kakao Map 을 사용한 지도 검색 기능<br/>
![Untitled](https://github.com/JihyeChu/PetNexus/assets/51440636/e7812685-63f9-48ee-ad9e-a8c8ce34b724)
#

### ✅ 알림 (SSE)

- 해당 채팅방에 있지않을 때 알람 송신
    - 새로운 메세지
    - 새로운 댓글<br/>
<hr/><br/><br/>

# 🔗서비스 아키텍쳐
![Untitled](https://github.com/JihyeChu/PetNexus/assets/51440636/4a29497e-c03d-4849-9deb-85137588793d)
<br/><br/>

# ⚙기술적 의사 결정에 따른 주요 기술
<details>
<summary>Redis</summary>
  
`도입 이유`

1. Refresh Token 보관 및 로그 아웃 시, Access Token 블랙리스트에 등록하기 위한 기술 구현
2. 채팅 및 알람 데이터 캐싱 설계

`문제 상황`

- Access Token 만료 시 reissue를 띄울 때, 빈번하게 DB에 접근하는 것이 비효율적 판단
- 채팅방 데이터 및 많은 데이터를 불러올 때 성능 저하 예상

`조율 및 결정`

In-memory 방식으로 빠른 엑세스 속도와 다른 다양한 데이터를 담을 수 있어 추가로 사용하기 좋기에 Redis 구현

## Redis(pub/sub)

`도입 이유`

1. Stomp의 pub/sub를 이용해 채팅 구현 시, 해당 pub/sub가 발생한 서버 내에서만 메시지를 주고 받는것이 가능함.
2. 생성된 서버 안에서만 유요하므로 다른 서버에서 접속해도 보이지 않는 문제 발생 예상.
3. 채팅방이 여러 서버에 접근할 수 있도록 개선을 위하여 공통으로 사용하는 pub/sub 시스템을 구축하여 모든 서버가 해당 시스템을 통해 메시지를 주고받을 수 있도록 구현 필요

Redis pub/sub 채널 구현

`해결 방안`

- Redis
- kafka
- RabbitMQ

`조율 및 결정`

Redis 자체의 pub/sub 채널 기능 지원.

오픈 채팅방의 경우 따로 수신 확인이 필요 없기 때문에 in-memory기반의 빠른 엑세스 속도를 가지는 Redis 채택
</details>
<hr/>
<details>
<summary>SSE</summary>
  
`도입 이유`

중고 장터의 구매자가 판매자에게 구매 요청 알람 기능 구현

`해결 방안`

- **SSE**
- **Web Soket**
- **Polling**

`조율 및 결정`

일반적인 HTTP요청은 [요청 - 응답]의 과정을 거치고 연결을 종료하는 반면, SSE 방식은 한번 연결하면 클라이언트로 데이터를 계속 보낼 수 있음. 클라이언트가 주기적으로 HTTP 요청을 보낼 필요가 없이 HTTP 연결을 통해 서버에서 클라이언트로 테이터 전달 가능.
구매 요청을 확인만 하면 바로 채팅 API로 넘어가기 때문에, 이벤트가 [ 서버 → 클라이언트 ] 방향으로만 흐르는 단방향 통신만이 필요했기 때문에, polling보다 리소스 낭비가 적고 양방향 통신인 웹 소켓에 비해 가벼운 SSE방식으로 진행
</details>
<hr/>
<details>
<summary>WEB SOKET (Stomp)</summary>
  
`도입 이유`

중고 장터의 구매자와 판매자 간의 1:1채팅 서비스를 구현

`해결 방안`

- **Sock JS**
- **Web Soket**
- **Stomp**

`조율 및 결정`

STOMP (Simple Text Oriented Messaging Protocol)은 메세징 전송을 효율적으로 하기 위해 탄생한 프로토콜로써 pub / sub 구조로 되어있어 메세지를 전송하고 메세지를 받아 처리하는 부분이 확실히 정해져 있고, 메세지의 헤더에 값을 줄 수 있어 헤더 값을 기반으로 통신 시 인증 처리를 구현하는 것도 가능하여 Stomp을 채택
</details>
<hr/>
<details>
<summary>GITHUB ACTIONS Docker CI/CD</summary>
  
`도입 이유`

배포 자동화를 통해 효율적인 협업 및 작업 환경을 구축

Docker 사용으로 개발 환경을 이미지화 시키고 해당 이미지를 통해 개발 및 배포 환경을 쉽게 컨테이너화 시켜 구축

`해결 방안`

- **GitHub Action ( 깃허브 액션 )**
- **Jenkins ( 젠킨스 )**
- **Bamboo**

`조율 및 결정`

시간 제약 및 복잡한 절차 없이 현재 사용중인 Github 내에서 바로 적용 가능한 Github Actions 채택
</details>
<hr/>
<details>
<summary>Kakao Map API</summary>
  
`도입 이유`

판매를 원하는 위치를 등록하기 위해, 주소 검색 및 저장 기술 구현

`해결 방안`

- WebClient
- RestTemplete
- Kakao Map APi

`문제 상황`

서버 내에 쿼리를 통한 검색어로 주소 검색 시, 정확한 주소를 리스트를 받아 저장하는 번거러움

`조율 및 결정`

Front에서 KAKAO MAP API를 도입하여 주소를 찾은 뒤, 주소데이터와 연동하여 위도, 경도값을 가져와 서버에 저장. 게시글에 반환 시 다시 KAKAO MAP API를 통해 구매자에게 정확한 위치를 표기 가능
</details><hr/><br/>



# 🛠기술 스택
### Back-End & Front-End
<div align="left">
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" />
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black"/>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=yellow">
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/WebSocket-F56640?style=flat-square&logo=WebSocket">
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>
</div>

### Server
<div align="left">
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
<img src="https://img.shields.io/badge/AmazonS3-569A31?style=flat-square&logo=AmazonS3&logoColor=white"/>
</div>


### Collaboration
<div align="left">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/>
</div>
<hr/><br/>

# 📌 **트러블슈팅**

## STOMP - JWT TOKEN

[STOMP - JWT TOKEN](https://www.notion.so/STOMP-JWT-TOKEN-7bc88f4581a743b6985c45237f20f2cf?pvs=21)

## SSE - 503 Error

[****503 Service Unavailable****](https://www.notion.so/503-Service-Unavailable-22b206f74c2048ef9659e4713599ab10?pvs=21)

## R****edis**** pub/sub Channel ****메시지 직렬화 및 역직렬화 문제****

[****redis 메시지 직렬화 및 역직렬화 문제****](https://www.notion.so/redis-6a73f3d9824f415ab637fbed92804d76?pvs=21)

## 채팅 데이터 ****Cache를 통해 성능 개선****

[채팅 데이터 ****Cache를 통해 성능 개선****](https://www.notion.so/Cache-367ffba92147419ea1554fd182730536?pvs=21)
<hr/><br/>

# 🧶ERD
![Test](https://github.com/JihyeChu/PetNexus/assets/51440636/033ba086-92b8-4fa8-abc3-5502e8abf144)

# 🚩API 명세
[API postman](https://documenter.getpostman.com/view/27923748/2s9Xy6rVWx)<br/>
[API swagger](https://github.com/JihyeChu/PetNexus/files/12602705/REST.API.pdf)
<br/><br/>

# 팀원    
| 이름 | 이메일 |
| --- | --- |
| 임호중 | hojooglim@gmail.com |
| 최혜원 | esc0218@naver.com |
| 박주민 | j2mie561@gmail.com |
| 추지혜 | 09strawberry@naver.com |
