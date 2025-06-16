# BetterThanYesterdaySSD

### 🚡 프로젝트 구성
---

- SSD Driver
```SSD Driver
├── command
│   ├── context
│   ├── impl
│   └── validation
├── common
│   ├── util
│   └── SSDConstants
└── SSD.java
└── Main.java

```

- Test Console
```Test Console
├── command
│   ├── common
│   ├── console
│   └── script
├── logger
│   ├── ConsoleLogListener
│   ├── Logger
│   ├── LoggerContext
│   └── ...
└── Main.java
```


### ❓ How to Use
---



### ✏️ Commit Convention
---

우리 팀은 협업 시 **일관된 커밋 메시지 작성**을 위해 아래와 같은 규칙을 사용합니다.  
팀장, 팀원 모두 이 규칙을 반드시 준수해주세요.

---

### ✅ 커밋 메시지 형식

`[type]`  +  내용 <br><br>
ex) `[feature] ssd write 기능 추가 `



### ✅ 커밋 타입 목록

| 타입 | 설명 |
|------|------|
| `feature` | 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변화 없음) |
| `test` | 테스트 코드 추가/수정 |

### ✅ 커밋 작성 규칙

- 한 가지 목적만 담기
- 의미 없는 메시지 금지 (예: `temp`, `수정`, `asdf` 등)
- 메시지는 짧고 명확하게 작성

---

### 📝 Main Test시 working directory 콘솔로 변경 
as-is C:\Users\User\IdeaProjects\BetterThanYesterdaySSD
to-be C:\Users\User\IdeaProjects\BetterThanYesterdaySSD\command.console.TestConsole

### 📝 Shell.jar test시 
 아래 폴더 2개 생성 및 각각에 jar 복사
 root 폴더  
   ㄴ test/shell.jar   (test폴더 아무이름이나)
   ㄴ JarLibs/ssd.jar  ( 폴더명 필수 확인 필수!!!!)

