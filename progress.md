20.10.19 월
#한 일
	- 요구 사항 분석 작성
	- 기능 목록 작성
	- 도메인 모델 및 테이블 설계 시작

20.10.20 화
#한 일
	- 도메인 모델 및 테이블 설계 마무리
	- 도메인 모델, repository (spring data JPA 사용) 개발
	- startbootstrap에서 기본 템플릿 차용하여 적용

20.10.21 수
#한 일
	- 기능 구현: 의뢰 신청, 로그인, 로그아웃
	- Spring Security 관련 설정 완료

#코멘트
admin 계정을 우선 memory 상에 설정하는 방식으로 코딩함. 그런데 코드 수정에 의해 admin의 비밀번호가 변경될 수 있으므로 sql 파일로 미리 DB에 생성하는 방식으로 admin을 설정하는 것이 좋을 수도 있음

#어려운 점
startbootstrap의 기본 템플릿을 쓰는 것이 편하고 깔끔하긴 하지만 css가 익숙하지 않으면 화면 수정에 있어서 시간이 좀 걸림

#다음날 할 일
기능 구현: 회원 가입

20.10.22 목
#한 일
회원 가입 구현(view, service, controller), 회원 가입시 아이디 중복 체크 구현

#코멘트
회원 가입시 아이디 중복 체크를 controller에서 @Valid를 통해 처리하지 않고 service에서 따로 메서드를 구현하여 처리함. 중복시 exception을 던지게 하였는데, 이것을 에러페이지로 보여주기 보다는 회원 가입창에서 message를 출력하는 것이 좋다고 판단. 따라서 던져진 exception을 controller에서 받아서 다시 bindingResult를 통해 view에 출력하는 방식으로 구현함
**로그인 시 에러도 p tag로 처리하기**

20.10.24 토
#한 일
orderService.order(), Order.createOrder() 구현, 나의 의뢰 보기 구현, 의뢰 상세보기 구현 중

20.10.27 화
#한 일
order 수정 구현, Form 객체 -> Dto 객체로 전환, 회원정보 수정 구현, 비밀번호 변경 구현, admin 페이지 구현 시작

#Dto Object 사용
	- Entity Object는 Repository, Service layer에서만 사용하고 Controller, View layer에서는 사용하지 않기
	- 이를 위해 Dto Object를 만들고 Entity 와의 전환을 시켜주어야 함. Dto <-> entity 전환은 service layer에서 진행하기 (실제 전환 method 로직은 entity class에 선언되어 있음) 
	- 그러나 현 Project의 규모가 작기 때문에 모든 request나 response에 대해서 Dto를 만들지는 말고 같은 field가 사용되는 경우는 하나의 Dto를 재활용하기

# admin 의뢰 리스트에서 의뢰 상태별 리스트 보기 기능 구현

# 할 일
admin -> 견적 기능들 구현 / 회원 -> 견적 기능들 구현

# 다음주에는 테스트 케이스 작성하기

20.10.28 수
#한 일
견적 관련 controller, service, repository 구현

#할 일
estimateList.html

20.10.29
#한 일
견적 작성, 견적 리스트, 견적 상세보기 화면 구현

#할 일
jpql join query 작성법 공부하기
-> estimate id를 사용하여 연관된 order 객체 얻기 수행해야함 -> 완료

20.11.01
#한 일
join qoery 완성

20.11.02
#한 일
견적 수정하기(admin), 견적 리스트 및 상세보기(member), dashboard 구현 시작, 의뢰 파일 업로드 및 수정하기 구현

#create_at, updated_at 설정을 Annotation으로 바꾸기 -> 완료

20.11.04
#계획
	1. File Entity 만들기, MD5 체크섬 구현, 분석 파일 업로드 DB 이용하여 구현
	2. Figure 업로드 구현 시작

#한 일
File Entity 만들기, uuid 파일 이름 생성 구현, 다중 파일 업로드 구현, 파일 삭제 구현, Figure 구현 시작

20.11.05
#계획
파일 업로드 시 선택 안 했는데 파일 첨부되는 문제 수정
figure - file 일대일 관계 설정. 그렇게 하여 figure에는 title, description 정도만 넣기

#한 일
파일 업로드시 선택 안 했는데 빈 파일 첨부되는 문제 해결, 파일 다운로드 구현, 첨부 파일 삭제 시 이전 화면으로 되돌리기 구현, dashboard 수정, 삭제 구현, dashboard 파일 첨부 구현 시작

#어려운 점 봉착
현재 File Entity는 Order Entity와 다대일 관계에 있음. 따라서 Figure Entity와 File Entity를 일대일 관계로 두는 것이 애매한 상황임. 그렇다고해서 Figure Entity에 파일 관련 속성까지 포함시킬 경우 FigureService에서 파일 업로드, 다운로드 관련 로직을 중복해서 구현해야 함.

-> 해결책1: 만약 File Entity를 Order Entity와 연관관계를 없앨 경우 -> orderDetail 보기에서 해당 order와 관련된 file들을 어떻게 가져올 것인가를 해결해야함. File Entity에 속성으로 type을 두고, Data/ Chart/ Table로 나눔. 그렇게 하여서 Data의 경우에는 order_id를 갖게하고, chart나 table의 경우에는 figure_id를 갖게 함. 문제점: 연관관계가 없으므로 해당 order entity의 수정 사항이 file entity에는 전혀 반영되지 않음. 괜찮나..?

-> 해결책2: 구조는 그대로 가고, Figure에 file 정보를 저장한다. 대신에 FileService에서 기존의 메서드 외에 DB 관련 없는 file 업로드, 삭제 로직만 지닌 메서드를 정의한다. 그래서 Figure와 관련해서 FileService를 사용할 때는 DB와 관련없는 메서드를 호출함. 사실 최소한의 file 정보만 Figure가 지니고 있으면 될 듯 하다. ex) file_name. 우선은 다 포함되게 구현한 후에 필요 없으면 없애는 방향으로 진행해보자.

!! 해결책 2 채택

#할 일
dashboard 별로 group by 해서 figure 조회하기 V

20.11.08
#계획
figure 파일 업로드 후 detail 화면에서 이미지 미리보기 설정

#한 일
figure - file download 구현, figureDetail.html 구현

20.11.10
#한 일
figure update 구현, figure delete 구현, figure 파일 업로드 후 detail 화면에서 보기 설정, dashboard에 해당 figure들 출력, 사용자 dashboardDetail 화면

#문제
figure에 이미지 파일 업로드 시 static/file 폴더 내에 생성은 되지만 figureDetail에서 img src 를 통해 접근이 안 됨 (404 error). 서버를 재시작할 경우 정상적으로 img src가 작동함
-> 저장 폴더를 프로젝트 내가 아닌 다른 곳으로 옮겨도 동일한 문제 발생. 우선은 넘어가기로 함

20.11.11
Elasticsearch 공부

20.11.12
Testcase 작성 시작. 스프링 통합 테스트 형태로 작성

#한 일
MemberServiceTest, OrderServiceTest, EstimateServiceTest 작성 완료 (Integration Test)
domain unit test 작성 완료

#남은 일
FileServiceTest, DashboardServiceTest, FigureServiceTest 작성 (Integration Test)
JPA test 작성
MVC test 작성

20.11.24
# Dashboard 구현 계획
	- figure는 observable platform을 이용하여 만듦
	- 그런 다음 해당 figure를 sharing하고, png와 embed용 iframe tag를 관리자에게 전달
	- 관리자는 figure 생성시 png와 iframe tag를 포함시킴 (DB에 file명, url 모두 저장됨)
	- 사용자의 dashboard: 모든 figure들의 png만 출력함. 여기서 각 figure를 상세보기 클릭할 경우 iframe tag를 출력함 (동적인 figure의 경우 이 때 rendering 됨)
	- 이런 방식을 취할 경우 관리자와 Data Scientist의 구분이 분명해지고, Data scientist는 project 내에 각 사용자와 dashboard에 따라서 각각의 figure를 위한 새로운 html 파일을 만들 필요가 없어진다.
	- 하지만 이 서비스가 대량의 트래픽을 갖게 될 경우에는 observable을 링크하는 방식으로 렌더링해서는 안 될 것이다. 따라서 이 때는 figure 각각에 해당하는 html 파일을 만들고 (d3.js 코드 포함) url이 아닌 파일 경로(?)를 통해 렌더링하는 방식을 사용해야 할 것으로 보인다.

# 한 일
FigureView 구현 (관리자, 사용자), Dashboard 수정
