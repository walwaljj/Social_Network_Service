
# ♻️멋사SNS

<br><br><br>

## ✨ 요구사항 설명 ✨

### 1️⃣사용자 인증하기
<details>
<summary> 사용자는 회원가입을 진행할 수 있다. </summary>
<div markdown="1">
<br>
<ul>
  <li>회원가입에 필수로 필요한 정보는 아이디와 비밀번호 이다..</li><br>
  <li>부수적으로 전화번호, 이메일, 주소 정보를 기입할 수 있다.</li><br>

</ul>
  </div>
</details>
<details>
<summary> 아이디 비밀번호를 통해 로그인을 할 수 있어야 한다.  </summary>
<div markdown="1">
<br>
  </div>
</details>
<details>
<summary>로그인 한 상태에서, 자신을 대표하는 사진, 프로필 사진을 업로드 할 수 있다.   </summary>
<div markdown="1">
<br>
  </div>
</details>

### 2️⃣ 피드 구현하기

<details>
<summary>피드는 제목과 내용을 붙일 수 있다..  </summary>
<div markdown="1">
<br>
<ul>
  <li>피드에는 복수의 이미지를 넣을 수 있다.</li><br>

</ul>
  </div>
</details>
<details>
<summary>피드를 작성하고자 한다면 로그인 된 상태여야 한다. </summary>
<div markdown="1">
<br>
<ul>
  <li>사용자가 피드를 작성하면, 특별한 설정 없이 자신이 작성한 피드로 등록된다..</li><br>
</ul>
  </div>
</details>

<details>
<summary>피드는 작성한 사용자 기준으로, 목록 형태의 조회가 가능하다. </summary>
<div markdown="1">
<br>
<ul>
  <li>조회를 위해 대상 사용자의 정보가 제공되어야 한다.</li><br>
  <li>피드 목록 조회시, 작성자 아이디, 제목과 대표 이미지에 관한 정보가 포함되어야 한다.</li><br>
  <li>이때 대표 이미지란 피드에 등록된 첫번째 이미지를 의미한다.</li><br>
  <li>만약 피드에 등록된 이미지가 없다면, 지정된 기본 이미지를 보여준다..</li><br>
</ul>
  </div>
</details>

<details>
<summary>피드는 단독 조회가 가능하다. </summary>
<div markdown="1">
<br>
<ul>
  <li>피드 단독 조회시, 피드에 연관된 모든 정보가 포함되어야 한다. 이는 등록된 모든 이미지를 확인할 수 있는 각각의 URL과, 댓글 목록, 좋아요의 숫자를 포함한다.</li><br>
  <li>피드를 단독 조회할 시, 로그인이 된 상태여야 한다.</li><br>
</ul>
  </div>
</details>

<details>
<summary>피드는 수정이 가능하다.</summary>
<div markdown="1">
<br>
<ul>
  <li>피드에 등록된 이미지의 경우, 삭제 및 추가만 가능하다.</li><br>
  <li>피드의 이미지가 삭제될 경우 서버에서도 해당 이미지를 삭제하도록 한다.</li><br>
</ul>
  </div>
</details>

<details>
<summary>피드는 삭제가 가능하다.</summary>
<div markdown="1">
<br>
<ul>
  <li>피드가 삭제될때는 실제로 데이터베이스에서 삭제하는 것이 아닌, 삭제 되었다는 표시를 남기도록 한다.</li><br>
</ul>
  </div>
</details>

### 3️⃣ 댓글, 좋아요 구현하기
<details>
<summary>댓글 구현하기 </summary>
<div markdown="1">
<br>
<ul>
  <li>댓글 작성은 로그인 한 사람만 쓸 수 있다.</li><br>
  <li>자신이 작성한 댓글은 수정 및 삭제가 가능하다.</li><br>
  <li>댓글의 조회는 피드의 단독 조회와 함께 이뤄진다.</li><br>
</ul>
  </div>
</details>
<details>
<summary>좋아요 구현하기 </summary>
<div markdown="1">
<br>
<ul>
  <li>다른 사용자의 피드는 좋아요를 할 수 있다.</li><br>
  
</ul>
  </div>
</details>

<br><br>


### 🗓️ 프로젝트 기간 🗓️
### 2023 .08 . 03 ~  08 . 08
* * *

