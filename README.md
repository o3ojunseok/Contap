# Contap BackEnd
<details>
<summary>Backend Rules </summary>
<div markdown="1">       

## Structure

1. 패키지는 목적별로 묶는다.
  - user(User 관련 패키지), card(카드 관련 패키지)
2. Controller에서는 최대한 어떤 Service를 호출할지 결정하는 역할과 Exception처리만을 담당하자.
  - Controller 단에서 로직을 구현하는 것을 지양한다.
  - Controller의 코드 라인 수를 줄이자는 뜻은 절대 아니다.
3. 하나의 메소드와 클래스는 하나의 목적을 두게 만든다.
  - 하나의 메소드 안에서 한가지 일만 해야한다.
  - 하나의 클래스 안에서는 같은 목적을 둔 코드들의 집합이여야한다.
4. 메소드와 클래스는 최대한 작게 만든다.
  - 메소드와 클래스가 커진다면 하나의 클래스나 메소드 안에서 여러 동작을 하고 있을 확률이 크다.
  - 수많은 책임을 떠안은 클래스를 피한다. 큰 클래스 몇 개가 아니라 작은 클래스 여럿으로 이뤄진 시스템이 더욱 바람직하다.
  - 클래스 나누는 것을 두려워하지 말자.
5. 도메인 서비스를 만들어지는 것을 피하자.
  - User라는 도메인이 있을 때, UserService로 만드는 것을 피한다.
  - 이렇게 도메인 네이밍을 딴 서비스가 만들어지면 자연스레 수많은 책임을 떠안은 큰 클래스로 발전될 가능성이 높다.
  - 기능 별로 세분화해서 만들어보자. (UserService, EmailService 등...)


## Programming

1. 반복되는 코드를 작성하지 않는다.
  - 단, 테스트코드는 예외로 한다.
2. 변수는 최대한 사용하는 위치에 가깝게 사용한다.
3. 파라미터 변수와 내부 변수를 구별할 땐 언더바가 아닌 this로 구별한다.
  - this.name = name (O) / name = _name (X)
  - 추가적으로 언더바를 prefix로 사용하는 것을 지양하자.
4. 코드의 길이가 짧고 명료한 것도 좋지만, **가독성이 현저히 떨어진다면 코드를 좀 더 풀어쓴다.**
  - 무조건적으로 코드가 짧은 것이 좋다고 생각하지 않는다. 다른 개발자가 본다면 가독성이 현저히 떨어진다.
5. 모든 예외는 무시하지말고 처리한다. 만약 예외를 처리하지 않을거라면 그 이유에 대해서 명확하게 주석을 남긴다.
6. 예외를 던질 때는 최대한 세부적인 Exception(= Custom Exception)을 던진다.
  - 실패한 코드의 의도를 파악하려면 호출 스택트레이스만으로 부족하다.
  - 오류 메세지에 전후 상황의 정보를 담아 예외와 함께 던진다.
7. 예외 케이스가 발생할 확률이 있는 경우, 가능한 빨리 리턴 또는 예외를 던지도록 작성한다.
  - 쓸데없이 정상로직을 태울 필요가 없게한다.
8. 조건이 복잡한 경우 임시 boolean 변수를 만들어 단순화한다.
9. 최대한 객체 타입 대신 기본 자료형을 선택하고, 생각지도 못한 Autoboxing이 발생하지 않도록 유의한다.

### Name

1. 변수는 CamelCase를 기본으로 하자!
+ userEmail,userName,pwCheck 등등
2. 패키지명은 단어가 달라지더라도 무조건 소문자를 사용하자!
3. ENUM이나 상수는 대문자로 네이밍한다.
  - REJECT_TAP,ACCEPT_TAP 등등
4. 함수명은 소문자로 시작하고 동사로 네이밍한다.
  - getUserId(), getEmail() 등등
5. 클래스명은 명사로 작성하고 UpperCamelCase를 사용한다.
  - ChatController , HashTag 등등
6. 객체 이름을 함수 이름에 중복해서 넣지 않는다. (= 상위 이름을 하위 이름에 중복시키지 않는다.)
  - user.getAuthStatus()
7. 의도가 드러난다면 되도록 짧은 이름을 선택한다.
  - 단, 축약형을 선택하는 경우는 개발자의 의도가 명백히 전달되는 경우이다. 명백히 전달이 안된다면 축약형보다 서술형이 더 좋다.
8. 함수의 부수효과를 설명한다.
  - 함수는 한가지 동작만 수행하는 것이 좋지만, 때에 따라 부수 효과를 일으킬 수도 있다.
9. LocalDateTime -> xxxAt, LocalDate -> xxxDt로 네이밍

</div>
</details>

- [Contap Notion](https://frequent-packet-5ba.notion.site/ConTap-dda2c10905b7488fa31e7b0e5f3ee8e6)

## BackEnd(Language,Library,Framework)
+ Java8
+ JDK 1.8.0
+ Spring Boot
+ IntelliJ IDEA
+ Spring Data JPA
+ Swagger
+ JWT
+ Spring Security
+ Sentry
+ Websocket , SockJS , Stomp
+ Redis
+ QueryDSL
+ MYSQL


## Project Introduce
디자이너와 개발자를 위한 커뮤니티 플랫폼 'Contap'

프로젝트로 나를 소개하고

함께 일하고 싶은 디자이너와 개발자를 만날 수 있는 곳!

프로젝트를 한곳에 모아 아카이빙 할 수 있어요.

<img src = "https://media.vlpt.us/images/junseokoo/post/69d1eaed-69bb-43d9-a3e9-ba9d7cb85ae7/KakaoTalk_20211202_234232569.png">

## Project Intention
백엔드와 프론트엔드의 프로젝트나 협업은 생각보다 쉽지 않습니다. 디자이너와의 협업은 더더욱 쉽지 않구요.

그러기 힘든 이유로는 이제 막 개발을 배우기 시작한 주니어 개발자, 디자이너들은 아직 협업 경험도 별로 없기도 하고 서로의 정보들이 한 곳에 모여있지 않아서, 그 정보를 보고 이야기를 할 수 있는 공간 또한 많지 않기 때문 이라고 생각했습니다.

그래서 저희는 이 문제를 해결하고자 개발자와 디자이너가 서로의 프로젝트를 공유하고 더 나아가서는 프로젝트를 진행 할 사람을 만날 수 있는 사이트인 Contap을 만들게 되었습니다.

## Target
+ Developer (BackEnd)
+ Designer 

## Service Introduce
+ 일반회원가입은 이메일 인증을 통해 가입할 수 있으며, 그 외에 카카오톡,깃허브로 로그인할 수 있습니다.
+ 메인페이지에서 작성된 카드들을 확인할 수 있습니다.
+ 마이페이지에서 카드의 앞면에 수정버튼을 누를 시 나오는 해쉬태그 목록에서 사용하는 기술 및 관심있는 항목을 선택할 수 있습니다.
+ 카드의 뒷면에는 내가 진행했던 프로젝트를 설명하는 내용을 작성할 수 있습니다.
+ 메인페이지에서 카드들을 확인한 뒤 마음에 드는 사람에게 간단한 쪽지내용과 함께 Tap요청을 보낼 수 있습니다.
+ Tap요청을 받은 사람은 보낸 사람의 카드내용을 확인한 뒤에 수락 및 거절을 할 수 있습니다.
+ Tap요청을 수락하면 나의 Grab항목에 추가되며 Grab된 사람과 실시간으로 1:1채팅을 할 수 있습니다.
+ 채팅이 종료되거나 채팅시 불쾌한 내용이 오갈 시 그랩을 끊을 수 있습니다.

<img src = "https://media.vlpt.us/images/junseokoo/post/6fc90ee8-a5fb-45d7-a501-32c7ac734cef/KakaoTalk_20211202_230337351.png">

## ERD(Entity-Relationship Diagram)
<details>
<summary>ERD Image</summary>
<div markdown="1">
<img src = "https://media.vlpt.us/images/junseokoo/post/a9047c28-2396-4b39-adc7-190f749e1de7/%EC%BA%A1%EC%B2%98.PNG">
</div>
</details>


## User FeedBack
+ FeedBack 통계

---

<img src = "https://media.vlpt.us/images/junseokoo/post/5e97d7ed-817e-4d86-b1c6-f263b72b0210/image.png"> 

---

+ FeedBack - 카드작성시 뭘 해야할지 잘 모르겠습니다, 카드형식으로 프로젝트를 보여주기 때문에 제약이 많았습니다.
+ Solution - 기획단계부터 Closed Community형식으로 가기위해 뒷면카드를 작성하지 않으면 다른사람의 카드를 열람하시 못하게 하였으나 그렇게하니 카드 작성을 어떤식으로 해야할지 모르겠다는 피드백이 많았는데 이때 떠오른 해결방법은 두가지 였습니다.
  1. ClosedCommunity를 유지하고 온보딩 형식으로 카드작성 가이드를 보여준다.
  2. 뒷면카드 열람권한을 로그인만 하면 가능하게 하여 사용자가 직접 카드를 탐색하여 작성할 수 있게 한다.
  + 전자의 방법은 너무 강제성이 강할 것 같았고 아무래도 하나하나 설명하는 사이트가 좋은 사이트처럼 보이지는 않아서 PlaceHolder로 최대한 상세하게 알려주는 것을 전제로  해결방안으로 후자를 선택하게 되었습니다.
  <img src = "https://media.vlpt.us/images/junseokoo/post/55f3fcf7-e6c8-4e56-a9e9-40125e20d4a3/Untitled.png">
+ FeedBack - 백엔드,프론트엔드의 색이 구분이 되었으면 좋겠다.
+ Solution - 핵심 기능이 개발자와 디자이너의 매칭이기 때문에 개발자 끼리의 색 구분은 크게 의미가 없다고 생각 했었으나 생각보다 많은 피드백 요청이 와서 많은 고민을 했었습니다.색을 추가만하면되는 간단한 작업이었지만 이 부분 또한 사이트의 메인컬러라고 생각할 수 있기 때문에 정말 신중하게 색상을 선택하여 적용했습니다.

<img src = "https://media.vlpt.us/images/junseokoo/post/e0a140be-71a4-4229-8004-aca093799e01/%E1%84%8F%E1%85%A1%E1%84%83%E1%85%B3%20%E1%84%89%E1%85%A2%E1%86%A8%20%E1%84%87%E1%85%A7%E1%86%AB%E1%84%80%E1%85%A7%E1%86%BC.gif">

## Marketing
<img src = "https://media.vlpt.us/images/junseokoo/post/41924e47-f8fc-4c10-8659-1db5529b6e0a/Untitled.png">

