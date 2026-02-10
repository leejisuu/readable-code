package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.io.provider.LockerPassFileReader;
import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import cleancode.studycafe.tobe.provider.LockerPassProvider;
import cleancode.studycafe.tobe.provider.SeatPassProvider;

public class StudyCafeApplication {

    public static void main(String[] args) {
        SeatPassProvider seatPassProvider = new SeatPassFileReader();
        LockerPassProvider lockerPassProvider = new LockerPassFileReader();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(seatPassProvider, lockerPassProvider);
        studyCafePassMachine.run();
    }

}

/*
* (1) 추상화 레벨
* - 중복 제거, 메서드 추출
* - 객체에 메세지 보내기
*
* (2) 객체의 책임과 응집도
* - IO 통합
* - 일급 컬렉션
* - display()의 책임
* - Order 객체
*
* (3) 관점의 차이로 달라지는 추상화
* 외부 데이터를 가져올 때 두 가지 관점
* 1. 어떤 데이터를 필요로 하는가 -> 방법을 아예 모르도록하고 어떤 데이터를 필요로 하는지에 대한 스펙만 뽑는게 더 나은 추상화
* 2. 데이터를 어디로부터 어떻게 가져올 것인가 -> 방법
*
* 미래에 공지사항 등 파일로 불러올 스펙들이 추가되면 StudyCafeFileHandler에 작성되게 될 것
* 왜? FileHandler, 파일에서 읽는다는 방법에 초점을 맞춰서 객체를 생성했기 때문에
*
* => 어떤 데이터를 필요로하는가에 초점을 맞춰보자
* SeatPassProvider, LockerPassProvider 인터페이스 생성해서 외부에서 주입(DIP, 제어의 역전) 사용
* 
* SeatPassProvider, LockerPassProvider -> 패스권들을 제공하는 애야(추상 개념 스펙)
* SeatPassFileReader, LockerPassFileReader -> 가져오는 방법(구현체)
* -> 방법이 바뀌면 구현체만 바꿔 끼워주면 된다
*
* 추상 스펙과 구현체의 패키지를 분리하기
*
* 핵사고날 아키텍처 - 포트와 어댑터
* 포트 -> 인터페이스
* 어댑터 -> 포트에 맞는 구현체
*
* 
* */




