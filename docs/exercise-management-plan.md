ㅐ# CareFit 운동관리(일정관리) 기능 실행 계획

## 📋 개요
CareFit 앱의 운동관리 기능 구현을 위한 백엔드 API 설계 및 개발 계획서입니다.

### 프로젝트 구조
```
com.kspo.carefit
├── base                          # 공통 기능
│   ├── config                   # 설정 (Security, Exception, etc)
│   ├── jpa                      # JPA 공통 (BaseEntity, Converter)
│   └── security                 # 인증/인가 (OAuth2, JWT)
└── domain
    ├── user                     # 회원 도메인 (완료)
    └── exercise                 # 운동관리 도메인 (신규)
        ├── controller
        ├── service
        ├── facade
        ├── repository
        ├── entity
        └── dto
```

### 아키텍처 패턴
- **Controller**: HTTP 요청/응답 처리
- **Facade**: 여러 Service를 조합하여 비즈니스 로직 조율
- **Service**: 단일 도메인 비즈니스 로직 처리
- **Repository**: 데이터 접근 계층

---

## 🎯 기능 분석 (와이어프레임 기반)

### 1. 오늘의 운동 시작하기
- **컨디션 체크**: 오늘의 특이사항 선택
  - 전날 운동을 했음
  - 부상이 있음
  - 현재 재활중
  - 해당사항 없음
- **운동 선택 방법**:
  - 나의 운동일정에서 불러오기
  - 오늘의 운동 추천받기 (LLM API 연동)

### 2. 운동 실행
- 운동 시작 시간 기록
- 운동 진행 중 상태 표시
- 운동 종료 시간 기록
- 총 운동 시간 계산 및 저장

### 3. 운동일정 등록
- 운동명 입력 (직접 입력 가능)
- 날짜 선택
- 운동 시간 입력
- 특이사항 입력 (선택)

### 4. 운동 현황 조회
- 이번달 운동 추이 (그래프)
- 캘린더 형식으로 운동 기록 조회
- 특정 날짜 운동 상세 조회

---

## 🗄️ 데이터베이스 설계

### 1. Exercise (운동 기록)
```sql
CREATE TABLE exercise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    exercise_name VARCHAR(100) NOT NULL COMMENT '운동명',
    exercise_date DATE NOT NULL COMMENT '운동 날짜',
    start_time DATETIME COMMENT '운동 시작 시간',
    end_time DATETIME COMMENT '운동 종료 시간',
    duration_minutes INT COMMENT '운동 시간(분)',
    condition_status VARCHAR(50) COMMENT '컨디션 상태',
    notes TEXT COMMENT '특이사항/메모',
    is_from_schedule BOOLEAN DEFAULT FALSE COMMENT '일정에서 가져온 운동 여부',
    is_recommended BOOLEAN DEFAULT FALSE COMMENT 'LLM 추천 운동 여부',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
```

### 2. ExerciseSchedule (운동 일정)
```sql
CREATE TABLE exercise_schedule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    exercise_name VARCHAR(100) NOT NULL COMMENT '운동명',
    scheduled_date DATE NOT NULL COMMENT '예정 날짜',
    scheduled_time TIME COMMENT '예정 시간',
    duration_minutes INT COMMENT '예상 운동 시간(분)',
    notes TEXT COMMENT '메모',
    is_completed BOOLEAN DEFAULT FALSE COMMENT '완료 여부',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
```

### 3. ConditionCheck (컨디션 체크)
```sql
CREATE TABLE condition_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    check_date DATE NOT NULL COMMENT '체크 날짜',
    condition_type VARCHAR(50) NOT NULL COMMENT '컨디션 유형',
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
```

### 4. ExerciseRecommendation (운동 추천 기록)
```sql
CREATE TABLE exercise_recommendation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    recommendation_date DATE NOT NULL COMMENT '추천 날짜',
    exercise_name VARCHAR(100) NOT NULL COMMENT '추천된 운동명',
    sport_name VARCHAR(100) COMMENT '스포츠 종목명',
    condition_type VARCHAR(50) COMMENT '당시 컨디션',
    llm_prompt TEXT COMMENT 'LLM 프롬프트',
    llm_response TEXT COMMENT 'LLM 응답',
    is_accepted BOOLEAN DEFAULT FALSE COMMENT '추천 수락 여부',
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
```

---

## 📡 API 설계

### 1. 운동 시작하기

#### 1.1 컨디션 체크 저장
```
POST /api/v1/exercise/condition
Authorization: Bearer {token}

Request Body:
{
  "checkDate": "2024-11-24",
  "conditionType": "EXERCISED_YESTERDAY" // EXERCISED_YESTERDAY, INJURED, REHABILITATION, NONE
}

Response:
{
  "status": "SUCCESS",
  "data": {
    "conditionCheckId": 1,
    "checkDate": "2024-11-24",
    "conditionType": "EXERCISED_YESTERDAY"
  }
}
```

#### 1.2 오늘의 운동 추천받기 (LLM)
```
POST /api/v1/exercise/recommend
Authorization: Bearer {token}

Request Body:
{
  "conditionType": "EXERCISED_YESTERDAY",
  "userSportsCodes": [1, 2, 3] // User의 관심 스포츠 코드
}

Response:
{
  "status": "SUCCESS",
  "data": {
    "recommendationId": 1,
    "exerciseName": "가벼운 스트레칭",
    "sportName": "요가",
    "reason": "전날 운동을 하셨기 때문에 가벼운 스트레칭을 추천드립니다."
  }
}
```

### 2. 운동 실행

#### 2.1 운동 시작
```
POST /api/v1/exercise/start
Authorization: Bearer {token}

Request Body:
{
  "exerciseName": "러닝",
  "conditionType": "NONE",
  "scheduleId": 1, // 선택: 일정에서 가져온 경우
  "recommendationId": 1 // 선택: 추천받은 운동인 경우
}

Response:
{
  "status": "SUCCESS",
  "data": {
    "exerciseId": 1,
    "exerciseName": "러닝",
    "startTime": "2024-11-24T14:30:00",
    "exerciseDate": "2024-11-24"
  }
}
```

#### 2.2 운동 종료
```
PATCH /api/v1/exercise/{exerciseId}/end
Authorization: Bearer {token}

Request Body:
{
  "notes": "오늘은 5km 달렸습니다"
}

Response:
{
  "status": "SUCCESS",
  "data": {
    "exerciseId": 1,
    "exerciseName": "러닝",
    "startTime": "2024-11-24T14:30:00",
    "endTime": "2024-11-24T15:00:00",
    "durationMinutes": 30,
    "notes": "오늘은 5km 달렸습니다"
  }
}
```

#### 2.3 운동 현황 조회 (진행 중인 운동)
```
GET /api/v1/exercise/current
Authorization: Bearer {token}

Response:
{
  "status": "SUCCESS",
  "data": {
    "exerciseId": 1,
    "exerciseName": "러닝",
    "startTime": "2024-11-24T14:30:00",
    "elapsedMinutes": 15
  }
}
```

### 3. 운동일정 관리

#### 3.1 운동일정 등록
```
POST /api/v1/exercise/schedule
Authorization: Bearer {token}

Request Body:
{
  "exerciseName": "수영",
  "scheduledDate": "2024-11-25",
  "scheduledTime": "14:00",
  "durationMinutes": 60,
  "notes": "수영장 예약 완료"
}

Response:
{
  "status": "SUCCESS",
  "data": {
    "scheduleId": 1,
    "exerciseName": "수영",
    "scheduledDate": "2024-11-25",
    "scheduledTime": "14:00",
    "durationMinutes": 60
  }
}
```

#### 3.2 운동일정 조회 (날짜별)
```
GET /api/v1/exercise/schedule?date=2024-11-25
Authorization: Bearer {token}

Response:
{
  "status": "SUCCESS",
  "data": [
    {
      "scheduleId": 1,
      "exerciseName": "수영",
      "scheduledDate": "2024-11-25",
      "scheduledTime": "14:00",
      "durationMinutes": 60,
      "isCompleted": false
    }
  ]
}
```

#### 3.3 운동일정 수정
```
PATCH /api/v1/exercise/schedule/{scheduleId}
Authorization: Bearer {token}

Request Body:
{
  "exerciseName": "수영",
  "scheduledDate": "2024-11-25",
  "scheduledTime": "15:00",
  "durationMinutes": 90
}
```

#### 3.4 운동일정 삭제
```
DELETE /api/v1/exercise/schedule/{scheduleId}
Authorization: Bearer {token}
```

### 4. 운동 현황 조회

#### 4.1 나의 운동 현황 (이번달 추이)
```
GET /api/v1/exercise/stats?year=2024&month=11
Authorization: Bearer {token}

Response:
{
  "status": "SUCCESS",
  "data": {
    "totalExerciseDays": 15,
    "totalDurationMinutes": 900,
    "averageDurationMinutes": 60,
    "mostFrequentExercise": "러닝",
    "exerciseByDate": [
      {
        "date": "2024-11-01",
        "exerciseName": "러닝",
        "durationMinutes": 30
      },
      {
        "date": "2024-11-03",
        "exerciseName": "수영",
        "durationMinutes": 60
      }
    ]
  }
}
```

#### 4.2 캘린더 형식 조회
```
GET /api/v1/exercise/calendar?year=2024&month=11
Authorization: Bearer {token}

Response:
{
  "status": "SUCCESS",
  "data": {
    "year": 2024,
    "month": 11,
    "exercises": [
      {
        "date": "2024-11-01",
        "hasExercise": true,
        "exerciseCount": 1,
        "totalDuration": 30
      },
      {
        "date": "2024-11-02",
        "hasExercise": false,
        "exerciseCount": 0,
        "totalDuration": 0
      }
    ]
  }
}
```

#### 4.3 특정 날짜 운동 상세 조회
```
GET /api/v1/exercise/detail?date=2024-11-24
Authorization: Bearer {token}

Response:
{
  "status": "SUCCESS",
  "data": [
    {
      "exerciseId": 1,
      "exerciseName": "러닝",
      "startTime": "2024-11-24T14:30:00",
      "endTime": "2024-11-24T15:00:00",
      "durationMinutes": 30,
      "conditionStatus": "NONE",
      "notes": "5km 달렸습니다"
    }
  ]
}
```

---

## 🔧 기술 스택 및 구현 요소

### 1. LLM API 연동
- **사용 API**: OpenAI GPT API 또는 다른 LLM 서비스
- **프롬프트 설계**:
  ```
  사용자 정보:
  - 관심 스포츠: {sportsCode에 해당하는 스포츠명}
  - 오늘 컨디션: {conditionType}
  - 최근 운동 기록: {최근 3일간 운동 기록}

  위 정보를 바탕으로 오늘 할 수 있는 적절한 운동을 추천해주세요.
  JSON 형식으로 응답해주세요:
  {
    "sport_name": "추천 스포츠명",
    "sport_info": "추천 스포츠에 대한 간단한 소개",
    "recommend_time": "추천 운동 시간(분)"
  }
  ```

### 2. Enum 정의
```java
// ConditionType.java
public enum ConditionType implements CodeCommInterface {
    EXERCISED_YESTERDAY("전날 운동을 했음"),
    INJURED("부상이 있음"),
    REHABILITATION("현재 재활중"),
    NONE("해당사항 없음");
}
```

### 3. 예외 처리
- `EXERCISE_NOT_FOUND`: 운동 기록을 찾을 수 없음
- `EXERCISE_ALREADY_STARTED`: 이미 진행 중인 운동이 있음
- `EXERCISE_NOT_STARTED`: 시작되지 않은 운동을 종료하려 함
- `SCHEDULE_NOT_FOUND`: 일정을 찾을 수 없음
- `LLM_API_ERROR`: LLM API 호출 오류

---

## 📅 개발 순서

### Phase 1: 기본 인프라 구축
1. ✅ BaseEntity 생성
2. Enum 클래스 생성 (ConditionType)
3. Exception 추가 (Exercise 관련)
4. Entity 클래스 생성
   - Exercise
   - ExerciseSchedule
   - ConditionCheck
   - ExerciseRecommendation

### Phase 2: 운동 실행 기능
1. 운동 시작 API
2. 운동 종료 API
3. 진행 중인 운동 조회 API
4. 컨디션 체크 저장 API

### Phase 3: 운동일정 관리
1. 운동일정 등록 API
2. 운동일정 조회 API (날짜별)
3. 운동일정 수정 API
4. 운동일정 삭제 API

### Phase 4: 운동 현황 조회
1. 이번달 운동 추이 API
2. 캘린더 형식 조회 API
3. 특정 날짜 상세 조회 API

### Phase 5: LLM 추천 기능
1. LLM API 클라이언트 구현
2. 운동 추천 API
3. 추천 기록 저장 및 조회

### Phase 6: 테스트 및 최적화
1. 단위 테스트 작성
2. 통합 테스트 작성
3. 성능 최적화 (쿼리 최적화, 캐싱)

---

## 🚀 예상 이슈 및 해결 방안

### 1. 동시에 여러 운동을 시작하는 경우
- **문제**: 사용자가 동시에 여러 운동을 진행할 수 있는가?
- **해결**: 현재 진행 중인 운동이 있으면 새로운 운동 시작 불가 (비즈니스 로직)

### 2. 운동 시작했지만 종료를 하지 않은 경우
- **문제**: 운동 시작 후 앱을 종료하고 종료 버튼을 누르지 않은 경우
- **해결**:
  - 스케줄러로 24시간 이상 진행 중인 운동 자동 종료
  - 사용자가 다음날 새 운동 시작 시 이전 운동 종료 확인 팝업

### 3. LLM API 응답 시간이 긴 경우
- **문제**: LLM API 응답이 느려서 사용자 경험 저하
- **해결**:
  - 비동기 처리 (CompletableFuture)
  - 로딩 중 UI 표시
  - 타임아웃 설정 (5초)

### 4. 운동 기록이 많아질 경우 조회 성능
- **문제**: 사용자의 운동 기록이 수천 건 이상일 때 조회 성능 저하
- **해결**:
  - 인덱스 추가 (user_id, exercise_date)
  - 페이징 처리
  - 캐싱 (Redis)

---

## 📝 참고사항

### 1. User 엔티티와의 관계
- User의 `sportsCode` 리스트를 활용하여 운동 추천
- User의 `sidoCode`, `sigunguCode`를 활용하여 주변 체육시설 연계 가능

### 2. 추후 확장 가능한 기능
- 운동 친구 기능 (같이 운동한 사람 기록)
- 운동 목표 설정 및 달성률
- 운동별 소모 칼로리 계산
- 체육시설 예약과 연동
- 동호회/대회 참가와 연동
- 소셜 기능 (운동 기록 공유)

### 3. 데이터 정합성
- 운동 시작/종료 시간은 서버 시간 기준으로 저장
- 날짜는 사용자 TimeZone 고려 필요
- 일정 삭제 시 연관된 운동 기록은 유지 (is_from_schedule 필드만 활용)

---

## ✅ 체크리스트

### 개발 전 준비
- [ ] ERD 최종 검토
- [ ] API 명세서 프론트엔드와 협의
- [ ] LLM API 선정 및 테스트
- [ ] Exception 코드 정의

### 개발 중
- [ ] Entity 및 Repository 구현
- [ ] Service 계층 구현
- [ ] Facade 계층 구현
- [ ] Controller 구현
- [ ] DTO 클래스 생성
- [ ] Validation 처리
- [ ] Exception Handling

### 개발 후
- [ ] Swagger 문서 작성
- [ ] 단위 테스트 작성
- [ ] 통합 테스트 작성
- [ ] API 문서화
- [ ] 코드 리뷰

---

## 📚 참고 문서
- [Spring Data JPA 공식 문서](https://spring.io/projects/spring-data-jpa)
- [Spring Security 공식 문서](https://spring.io/projects/spring-security)
- [OpenAPI Specification](https://swagger.io/specification/)

---

**작성일**: 2024-11-24
**버전**: 1.0
**작성자**: Claude Code
**프로젝트**: CareFit Backend