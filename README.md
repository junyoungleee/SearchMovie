# SearchMovie
네이버 영화 검색 API를 이용한 영화 검색 앱

## ✔️ 구현한 기능
### 검색 기능
+ 검색어를 입력하고 검색 버튼을 클릭하면 검색어에 대한 결과를 리스트로 보여줌
+ 영화 이미지, 제목, 출시일, 평점 정보를 나타냄
+ 영화 클릭 시, url을 로드해서 보여줌
+ [추가] 검색어가 없는 경우, 토스트 메세지 띄움
+ [추가] 검색 결과가 없는 경우, 텍스트로 명시
### 검색어 저장 기능
+ 최근 검색했던 검색어들을 보여줌 (최대 10개)
+ 검색어 클릭 시, 해당 검색어에 대한 검색 내용을 보여줌

<p align="center">
  <img src="https://user-images.githubusercontent.com/55984242/172930160-bcbecf0f-11f1-4f04-96b1-c75aa6609869.gif" height="470" width="210"/>
  <img src="https://user-images.githubusercontent.com/55984242/172931276-b6f0f354-620a-44ca-bba4-ba80154ed190.gif" height="470" width="210"/>
  <img src="https://user-images.githubusercontent.com/55984242/172931395-380c08ca-6e34-43d1-88c4-8820b9094e41.gif" height="470" width="210"/>
  <img src="https://user-images.githubusercontent.com/55984242/172931460-20da7371-9c74-4395-98c9-587bf49da4b1.gif" height="470" width="210"/>
</p>

## 💡 Development
+ IDE : Android Studio
+ Language : Kotlin

## 📌 Libraries
+ KotlinX
  - Coroutines
+ AndroidX
  - ViewModel
  - LiveData
  - Paging3
  - Room
  - DataBinding
  - RecyclerView
+ 네트워크
  - Retrofit2
  - OkHttp3
+ 이미지 로딩
  - Glide

## ⚠️ Notice
+ 보안 이슈상 네이버 API의 <b>Client Id와 Client Secret</b>은 업로드하지 않았습니다
