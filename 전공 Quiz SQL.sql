CREATE TABLE 전공 ( 
	num int PRIMARY KEY,
	problem nvarchar(100), 
	ex1 nvarchar(50),
	ex2 nvarchar(50),
	ex3 nvarchar(50),
	ex4 nvarchar(50), 
	result int(50) 
);

insert into 전공 values(1, '1. 다음중 메모리 크기가 4 BYTES 가 아닌 것을 고르시오', '1. int', '2. long',  '3. char',  '4. float', 3);
insert into 전공 values(2, '2. 자바에서 입력문을 사용할 때 옳은 메소드는?', '1. scanf',  '2. Scanner',  '3.scan',   '4.printf', 2);
insert into 전공 values(3, '3. java.lang.ArrayIndexOutOfBoundsException 오류의 원인으로 옳은것은?', '1. 선언한 배열 크기보다 크게 입력했기 때문에', '2. 선언한 방식과 입력한 방식이 다를때', '3. 중괄호를 닫아 주지 않아서', '4. 그냥싫어서', 1);
insert into 전공 values(4, '4. 다음중 정수형 변수를 선언 할 때 올바르게 선언한 방법은?', '1. char a = 123;', '2. String a = 345;', '3. int a[] = {1,2,3};', '4. int a = 3;', 4);
insert into 전공 values(5, '5. 결과 값을 true , false로 받는 키워드를 고르시오', '1. public',  '2. extend',  '3.boolean',  '4. try', 3);
insert into 전공 values(6, '6. 부모 클래스의 객체를 불러오고 싶을때 사용하는 것은?', '1. this',  '2. super', ' 3. static',  '4. public', 2);
insert into 전공 values(7, '7. 다음 접근 제어자들 중 해당 클래스에서만 접근 가능한 것은?', '1. public',  '2. private',  '3. protected',  '4. default', 2);
insert into 전공 values(8, '8. length 필드 사용시 올바르게 사용한 방법은?', '1. a.length',  '2. a(length)',  '3. length == a', ' 4. length.a', 1);
insert into 전공 values(9, '9. 배열 선언시 오류가 발생하는것은?', '1.int a[] = new int[5];', '2. int[] a = new int[2];', '3. int a[][] = new int[2][3];', '4. int a[] = new int[2][3];', 4);
insert into 전공 values(10, '10. Circle함수가 Shape를 상속받는다고 할 때다음 빈칸에 들어갈 것으로 올바른것은? class Circle __________ Shape', '1. this', '2. super',  '3. extends', ' 4. interface',3);
insert into 전공 values(11, '11.  vim표준모드에서 명령어들 중 저장만을 하고 싶을 때 사용하는 명령어는?', '1. :q', '2. :q!', '3. :w', '4, :wq!', 3);
insert into 전공 values(12, '12.  입력 모드에서 표준 모드로 넘어갈 때 사용하는 키는?', '1. enter', '2. Ctrl', '3. esc', '4. i', 3 );
insert into 전공 values(13, '13.  표준 모드에서 입력 모드로 넘어갈 때 사용하는 키는?', '1. enter', '2. Ctrl', '3. esc', '4. i', 4 );
insert into 전공 values(14, '14.  표준 모드에서 G키를 눌렀을 때 발생하는 일로 옳은 것은?', '1. 문서의 맨 마지막 행으로 이동', '2. 이전 문단으로 이동', '3. 위 행의 첫 글자로 이동', '4. 한 화면 위로 스크롤', 1);
insert into 전공 values(15, '15.  mkdir 명령어를 사용하여 test라는 이름의 디렉토리를 만들고자 할 때 옳바르게 사용한 것은?', '1. mkdir: test', '2. test.mkdir', '3. mkdir test', '4. mkdir.test', 3);
insert into 전공 values(16, '16.  원격 저장소의 내용을 로컬 저장소로 복사하고 싶을 때 사용하는 명령어는?', '1. git download', '2. git remote', '3. git clone', '4. git pass', 3 );
insert into 전공 values(17, '17.  import문을 사용하여  Scanner를 사용하고 싶을 때 올바르게 작성 한 것은?', '1. import Scanner;', '2. import java.util.Scanner;', '3. import(Scanner)', '4. import.Scanner ', 2);
insert into 전공 values(18, '18.  다음 연산자중 OR의 의미를 가지고 있는 것은?', '1. a!=b', '2. a&&b', '3. !a', '4. a||b', 4);
insert into 전공 values(19, '19.  vim에서 전환 할 수 있는 모드중 없는 것은?', '1. 표준모드', '2. 편집 모드', '3. 입력 모드', '4. 명령라인 모드', 2);
insert into 전공 values(20, '20.  vim에서 파일을 강제 저장 후 종료하고싶을 때 사용하는 명령어는?', '1. :wq!', '2. :q!', '3. :e', '4.:w', 1);
insert into 전공 values(21, '21.  현재 디렉토리에 git저장소를 생성하고싶을 때 사용하는 명령어는?', '1. git clone', '2. git add', '3. git init', '4. git push', 3);
insert into 전공 values(22, '22.  내가 연결하고있는 서버를 확인하고싶을 때 사용하는 명령어는?', '1.   git remote -v', '2. git remote origin', '3. git remote rm', '4. git remote show', 1);
insert into 전공 values(23, '23.  vim에서 커서의 위치 다음에 입력하고싶을 때 쓰는 키는?', '1. A', '2. o', '3. s', '4. a', 4);
insert into 전공 values(24, '24.  현재 커서에서 한 글자를 삭제하고싶을 때 쓰는 키는? ', '1. x', '2. d', '3. dx', '4. dd', 1);
insert into 전공 values(25, '25.  현재 폴더에서 상위 폴더로 돌아가고싶을 때 사용하는 명령어는?' , '1. cd ..', '2. cd', '3. cd return', '4. cd back', 1); 
insert into 전공 values(26, '26.  vim표준모드에서 ZZ키를 입력하면 일어나는 현상으로 옳은것은?', '1. 저장', '2. vi 종료', '3. 저장 후 종료', '4. 강제 저장 후 종료', 3);
insert into 전공 values(27, '27.  다음중 컴퓨터 언어가 아닌것은?', '1. C', '2. C#', '3. C++', '4. C##', 4);
insert into 전공 values(28, '28.  표준모드에서 a를 입력했을 때 옳은 현상은?', '1. 커서의 위치에 입력', '2. 커서 위치 다음 칸부터 입력', '3. 커서의 이전 행에 입력', '4. 커서 행의 맨 마지막부터 입력', 2);
insert into 전공 values(29, '29.  ArithmeticException오류의 원인으로 옳은 것은?', '1. 어떤 수를 0으로 나눌 때', '2. 널 객체를 참조할 때', '3. 배열의 크기가 음수값인 경우', '4. 원하는 클래스를 찾지 못하였을 경우', 1);
insert into 전공 values(30, '30.  자바 언어를 처음 개발한 사람은?', '1. 제임스 고슬링', '2. 빌게이츠', '3. 팁 머너스 리', '4. 스티브 잡스', 1);

select * from 전공
