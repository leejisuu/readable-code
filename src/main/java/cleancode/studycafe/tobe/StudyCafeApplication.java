package cleancode.studycafe.tobe;

public class StudyCafeApplication {

    public static void main(String[] args) {
        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine();
        studyCafePassMachine.run();
    }

}

/*
* (1) 추상화 레벨
* - 중복 제거, 메서드 추출
* - 객체에 메세지 보내기
*
* */
