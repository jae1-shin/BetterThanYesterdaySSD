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
---  /JarLibs/shell.jar , /JarLibs/ssd.jar 파일을 아래 구조로 배치하여 실행 

```Test Console
├── Console
│   └── shell.jar
└── JarLibs
│   └── ssd.jar

```

실행 방법
1. 테스트 콘솔 java -jar shell.jar
2. 러너 스크립트 java -jar shell.jar ".\shell_script.txt" 

![image](https://github.com/user-attachments/assets/25526bc7-c645-4af8-ac8c-1b7c9e2759f9)


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

