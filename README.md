# 체스 게임

## 기능 목록

### 도메인 기능

- [x] 초기화된 체스판을 만든다.
    - [x] 말을 만든다.
    - [x] 빈 체스판에 말을 배치한다.
- [x] 플레이어 턴에 따라 체스 말을 움직인다.
    - [x] 화이트의 턴이 먼저다.
    - [x] 턴을 번갈아가며 게임을 진행한다.
    - [x] source와 target 위치가 유효한지 확인한다.
        - [x] 범위를 벗어 나지 않는지 확인한다.
        - [x] source에 아군 말이 있는지 확인한다.
        - [x] target에 체스말이 있는 경우 source와 target이 다른 컬러의 말인지 확인한다.
    - [x] source와 target이 서로 다른 컬러의 말이라면 공격한다.
        - [x] source 위치의 체스말이 공격할 수 있는지 확인한다.
        - [x] 공격이 가능한 경우 target 위치의 체스말을 잡아먹는다.
    - [x] target이 비어있는 경우 source의 체스말이 이동한다.
        - [x] source 위치의 체스말이 이동할 수 있는지 확인한다.
        - [x] 이동이 가능한 경우 target 위치로 이동한다.
- [x] 각 플레이어의 점수를 계산한다.
  - [x] queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점, king은 0점이다.
  - [x] pawn의 기본 점수는 1점이다. 
    - [x] 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.
### UI 기능

- [x] 유효하지 않은 입력을 받은 경우 재입력받는다.
- [x] 게임을 시작할지 입력받는다.
    - [x] 게임시작 메시지와 사용 가능한 명령을 출력한다.
    - [x] 입력이 "start"면 시작한다.
    - [x] 입력이 "status"면 각 진영의 점수를 출력한다.
    - [x] 입력이 "end"면 끝난다.
- [x] 게임 명령을 입력받는다.
    - [x] 입력이 "move [source 위치] [target 위치]"이면 체스말을 움직인다.
        - [x] 올바른 source 위치와 target 위치인지 확인한다.
    - [x] 입력이 "end"면 끝난다.
- [x] 체스판을 출력한다.
    - [x] 각 위치에 말이 존재하면 각 말 이름의 첫번째 알파벳을 출력한다.
        - [x] 검은 색 말은 대문자로, 흰 색 말은 소문자로 출력한다.
    - [x] 각 위치에 말이 존재하지 않으면 "."을 출력한다.
- [x] 각 진영의 점수를 출력한다.