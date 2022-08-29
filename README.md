# ToDoMate-Android

## 프로젝트 소개
날짜 별로 todo list를 작성하고 친구 추가를 통하여 친구와 todo list 공유할 수 있는 앱 

## 프로젝트 기간
(2022.08.12 ~ 2022.08.28)

## 기능 소개
| 로그인 및 회원 가입| profile 등록 | todo 등록 |
|:-:|:-:|:-:|
|<img src="https://user-images.githubusercontent.com/68272971/187029439-6a91ec76-872c-4ef3-bfde-03d8773fa4a8.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029576-25032d5b-0c02-48f8-8e6e-1afcb863ec4e.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029666-318f8a3b-5c49-46d7-8672-85c68b36804c.png" height=600px>|

| todo 수정 | main 화면 | 친구 추가 |
|:-:|:-:|:-:|
|<img src="https://user-images.githubusercontent.com/68272971/187029723-b2b98053-c092-4e35-a3d1-45141e501fc0.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029774-0f558234-c8d4-4304-81d2-91e5f9967045.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029820-e8209bf8-830e-4877-a427-8524d8295f38.png" height=600px>|

| 친구 추가 된 화면 | 친구 todo list | 날짜 변경 |
|:-:|:-:|:-:|
|<img src="https://user-images.githubusercontent.com/68272971/187029868-ed52c845-ed5b-4788-9cac-6ee037104832.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029893-6b8573fb-d565-4aa4-9263-4ee17911ef4e.png" height=600px>|<img src="https://user-images.githubusercontent.com/68272971/187029926-cbcf1fb9-8267-4b0c-a95c-098b5df4c091.png" height=600px>|

## 기술 스택

#### Back-End
- firebase

#### Front-End
- android studio(java)

## 디자인 패턴

#### MVVM Pattern

## 사용 라이브러리
- firebase-database:20.0.5 => todo list 관리
- firebase-auth:21.0.7 => user email login 구현
- firebase-storage:20.0.1 => user profile 사진 저장 구현
- lifecycle-viewmodel:2.5.0 => mvvm -> viewmodel 구현
- lifecycle-livedata:2.5.0 => model repository observe 구현
- glide:4.11.0 => user profile 사진 - > imageView

## 새롭게 알게 된 점
- MVVM 모델을 적용해 Model, View, ViewModel 간의 의존성을 없애 각 역할에만 집중할 수 있다 보니 유지 보수 측면에서 매우 좋음, 기존 한 Activity에 모든 코드를 집어넣었을 때와 MVVM 모델을 적용했을 때를 비교해보면 기존에는 한 Acitivity에 view에 관한 코드 model(repository: back-end)에 관한 코드가 전부 합쳐져 의존성을 띄우다 보니 한 부분에서 이상이 생기면 view와 model에 관한 코드를 같이 고쳐야 할 경우가 있었으며 코드의 가독성도 매우 안 좋았음 하지만 mvvm 모델을 적용하니 view에 관한 코드는 view에 model에 관한 코드는 model에 있고 viewmodel과 observe를 통해 서로 간의 의존성을 없애니 model 부분에 이상이 생기면 model의 잘못된 코드 부분만 고치면 되니 유지 보수가 매우 편함 또한 각 역할을 나눴다 보니 코드의 가독성도 매우 높아졌음 
