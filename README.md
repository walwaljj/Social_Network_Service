
# 📱 SNS 📱

<br><br>

## ⌛️ 프로젝트 정보

| 분류 | 내용 |
| --- | --- |
| 주제 | sns service |
| 1차 기간 | 2023 .08 . 03 ~  08 . 08 / 리펙토링 10.13 |
| 2차 기간 | 2023 .10 . 14 ~  10 . 17 / 리펙토링 10.18 |
| API 문서 링크 | https://documenter.getpostman.com/view/26726157/2s9YR9ZD6d |

<br><br>


## <span style="color:lightblue"> 🔨 기술 스택

개발 언어 : <img src="https://img.shields.io/badge/JAVA-17-FFFFFF?style=flate&logo=openjdk&logoColor=FFFFFF">
<br>
개발 프레임 워크: <img src="https://img.shields.io/badge/SpringBoot-3.1.1-6DB33F?style=flate&logo=SpringBoot&logoColor=6DB33F">
<br>
스킬 : 
<img src="https://img.shields.io/badge/Springsecurity-green?style=flat&logo=Springsecurity&logoColor=#6DB33F"/>
<img src="https://img.shields.io/badge/JWT-blue?style=flat&logo=jsonwebtokens&logoColor=#000000"/>
<img src="https://img.shields.io/badge/SQLite3-003B57?style=flate&logo=SQLite&logoColor=white" >
<br>
도구 : <img src="https://img.shields.io/badge/GitHub-181717?style=flate&logo=GitHub&logoColor=white">
<img src="https://img.shields.io/badge/postman-FFFFFF?style=flate&logo=postman&logoColor=postman">
<img src="https://img.shields.io/badge/intellijidea-000000?style=flate&logo=intellijidea&logoColor=white">
<img src="https://img.shields.io/badge/swagger-85EA2D?style=flate&logo=swagger&logoColor=white">
<br>

<br><br>
## ERD 
<br><br>
<img width="867" alt="image" src="https://github.com/walwaljj/Social_Network_Service/assets/108582847/b54b62cb-69f6-43ed-84dd-cc0295cb3e3a">
<br><br>

## ✨ 기능 설명 ✨

### 1️⃣사용자 인증하기
- 사용자는 회원가입을 진행할 수 있다.
  - 회원가입에 필수로 필요한 정보는 아이디와 비밀번호 이다.
  - 부수적으로 전화번호, 이메일, 주소 정보를 기입할 수 있다.

- 아이디 비밀번호를 통해 로그인을 할 수 있다.
  - 로그인 된 사용자에게는 jwt 가 발급된다. 
  - 로그인 한 상태에서, 자신을 대표하는 사진, 프로필 사진을 업로드 할 수 있다.

### 2️⃣ 피드

- 피드는 제목과 내용을 붙일 수 있다. 
  - 피드에는 복수의 이미지를 넣을 수 있다. 

- 피드를 작성하고자 한다면 로그인 된 상태여야 한다.
  - 사용자가 피드를 작성하면, 특별한 설정 없이 자신이 작성한 피드로 등록된다. 

- 피드는 작성한 사용자 기준으로, 목록 형태의 조회가 가능하다.
  - 조회를 위해 대상 사용자의 정보가 제공되어야 한다.
  - 피드 목록 조회시, 작성자 아이디, 제목과 대표 이미지에 관한 정보가 포함돼있다.
  - 이때 대표 이미지란 피드에 등록된 첫번째 이미지를 의미한다.
  - 만약 피드에 등록된 이미지가 없다면, 지정된 기본 이미지를 보여준다.

- 피드는 단독 조회가 가능하다.
  - 피드 단독 조회시, 피드에 연관된 모든 정보가 포함되어야 한다. 이는 등록된 모든 이미지를 확인할 수 있는 각각의 URL과, 댓글 목록을 포함한다.
  - 피드를 단독 조회할 시, 로그인이 된 상태여야 한다.
- 피드는 수정이 가능하다.
- 피드는 삭제가 가능하다.
  - 피드가 삭제될 경우 이미지 역시 삭제된다.

### 3️⃣ 댓글
- 댓글 작성
  - 댓글 작성은 로그인 한 사람만 쓸 수 있다.
- 댓글 삭제
  - 자신이 작성한 댓글은 수정 및 삭제가 가능하다.
- 댓글 조회
  - 댓글의 조회는 피드조회 시 함께 이뤄진다.
### 4️⃣ 좋아요
- 피드를 좋아요하거나 좋아요를 취소 할 수 있다.
  - 좋아요는 로그인 한 사람만 할 수 있다.
- 사용자는 자신이 좋아한 피드들을 모아볼 수 있다.
- 피드를 좋아한 사람을 조회할 수 있다.
### 5️⃣ 팔로우
- 사용자는 팔로우를 할 수 있다.
  - 팔로우를 하려면 로그인을 해야한다. 
- 사용자가 팔로우 하는 모든 사용자의 피드를 조회할 수 있다.
- 사용자는 팔로우를 취소할 수 있다.
### 6️⃣ 친구
- 사용자는 친구 요청을 할 수 있다.
  - 친구를 맺으려면 로그인해야한다.
- 사용자는 친구 요청 목록을 조회할 수 있다.
- 사용자는 친구 요청을 수락할 수 있다.
  - 요청을 수락하면 서로 친구 관계가 된다.
- 사용자는 친구 요청을 거절할 수 있다.
- 사용자는 자신이 요청한 친구 요청을 취소할 수 있다.
- 사용자는 친구 관계인 사용자의 피드를 조회할 수 있다.
