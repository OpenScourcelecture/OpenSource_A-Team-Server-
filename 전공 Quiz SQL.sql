CREATE TABLE quiz1 ( 
	num int PRIMARY KEY,
	problem nvarchar(100), 
	ex1 nvarchar(50),
	ex2 nvarchar(50),
	ex3 nvarchar(50),
	ex4 nvarchar(50), 
	result int(50) 
);

insert into quiz1 values(1, '1. 다음중 메모리 크기가 4 BYTES 가 아닌 것을 고르시오', '1. int', '2. long',  '3. char',  '4. float', 3);
insert into quiz1 values(2, '2. 자바에서 입력문을 사용할 때 옳은 메소드는?', '1. scanf',  '2. Scanner',  '3.scan',   '4.printf', 2);
insert into quiz1 values(3, '3. java.lang.ArrayIndexOutOfBoundsException 오류의 원인으로 옳은것은?', '1. 선언한 배열 크기보다 크게 입력했기 때문에', '2. 선언한 방식과 입력한 방식이 다를때', '3. 중괄호를 닫아 주지 않아서', '4. 그냥싫어서', 1);
insert into quiz1 values(4, '4. 다음중 정수형 변수를 선언 할 때 올바르게 선언한 방법은?', '1. char a = 123;', '2. String a = 345;', '3. int a[] = {1,2,3};', '4. int a = 3;', 4);
insert into quiz1 values(5, '5. 결과 값을 true , false로 받는 키워드를 고르시오', '1. public',  '2. extend',  '3.boolean',  '4. try', 3);
insert into quiz1 values(6, '6. 부모 클래스의 객체를 불러오고 싶을때 사용하는 것은?', '1. this',  '2. super', ' 3. static',  '4. public', 2);
insert into quiz1 values(7, '7. 다음 접근 제어자들 중 해당 클래스에서testquiz1만 접근 가능한 것은?', '1. public',  '2. private',  '3. protected',  '4. default', 2);
insert into quiz1 values(8, '8. length 필드 사용시 올바르게 사용한 방법은?', '1. a.length',  '2. a(length)',  '3. length == a', ' 4. length.a', 1);
insert into quiz1 values(9, '9. 배열 선언시 오류가 발생하는것은?', '1.int a[] = new int[5];', '2. int[] a = new int[2];', '3. int a[][] = new int[2][3];', '4. int a[] = new int[2][3];', 4);
insert into quiz1 values(10, '10. Circle함수가 Shape를 상속받는다고 할 때다음 빈칸에 들어갈 것으로 올바른것은? class Circle __________ Shape', '1. this', '2. super',  '3. extends', ' 4. interface',3);
insert into quiz1 values(11, '11.  vim표준모드에서 명령어들 중 저장만을 하고 싶을 때 사용하는 명령어는?', '1. :q', '2. :q!', '3. :w', '4, :wq!', 3);
insert into quiz1 values(12, '12.  입력 모드에서 표준 모드로 넘어갈 때 사용하는 키는?', '1. enter', '2. Ctrl', '3. esc', '4. i', 3 );
insert into quiz1 values(13, '13.  표준 모드에서 입력 모드로 넘어갈 때 사용하는 키는?', '1. enter', '2. Ctrl', '3. esc', '4. i', 4 );
insert into quiz1 values(14, '14.  표준 모드에서 G키를 눌렀을 때 발생하는 일로 옳은 것은?', '1. 문서의 맨 마지막 행으로 이동', '2. 이전 문단으로 이동', '3. 위 행의 첫 글자로 이동', '4. 한 화면 위로 스크롤', 1);
insert into quiz1 values(15, '15.  mkdir 명령어를 사용하여 test라는 이름의 디렉토리를 만들고자 할 때 옳바르게 사용한 것은?', '1. mkdir: test', '2. test.mkdir', '3. mkdir test', '4. mkdir.test', 3);
insert into quiz1 values(16, '16.  원격 저장소의 내용을 로컬 저장소로 복사하고 싶을 때 사용하는 명령어는?', '1. git download', '2. git remote', '3. git clone', '4. git pass', 3 );
insert into quiz1 values(17, '17.  import문을 사용하여  Scanner를 사용하고 싶을 때 올바르게 작성 한 것은?', '1. import Scanner;', '2. import java.util.Scanner;', '3. import(Scanner)', '4. import.Scanner ', 2);
insert into quiz1 values(18, '18.  다음 연산자중 OR의 의미를 가지고 있는 것은?', '1. a!=b', '2. a&&b', '3. !a', '4. a||b', 4);
insert into quiz1 values(19, '19.  vim에서 전환 할 수 있는 모드중 없는 것은?', '1. 표준모드', '2. 편집 모드', '3. 입력 모드', '4. 명령라인 모드', 2);
insert into quiz1 values(20, '20.  vim에서 파일을 강제 저장 후 종료하고싶을 때 사용하는 명령어는?', '1. :wq!', '2. :q!', '3. :e', '4.:w', 1);


select * from quiz1
