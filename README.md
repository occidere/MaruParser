# MaruParser
marumaruin parser using Java
만화 이미지 파일을 다운로드 해주는 코드입니다.
json이나 xml 파서를 이용한 파싱이 아닌 순수하게 자바에 내장된 HTTP, URL, ImageIO기능과 jsoup을 활용해 파싱 후 저장하는 방식입니다.

--ver 0.3.0.1 --
 * [중요]<a href="">로 다운로드 불가능하던 부분 data-src 태그 탐색으로 문제 해결
 * [중요]만화 전권 다운로드 가능
 * 페이지 번호에 0 또는 00을 붙여 3자리수까지 오름차순 네이밍 유지 가능하게 함

-- ver 0.2.0.1 -- 
 * 제목 특수문자 제거부분 수정

-- ver 0.2.0 --
 * 이미지 url을 받아오는 부분을 jsoup 이용
 * archive주소 걸러주는 checkAddress()메소드 생성
 * 전체 파일 개수 출력
 * responseCode()관련 출력, 메소드 삭제
 * 다운로드 파일 출력 형식 변경 (현재 / 전체 ... ok!)
 * 이미지 url에 한글이 있으면 exe파일에선 저장 불가(이클립스에선 가능)

-- ver 0.1.5 --
 * responseCode체크 부분을 responseCheck()라는 메소드로 분리
 * HttpURLConnection 부분 connection()메소드로 분리
 * address를 String 타입에서 StringBuider로 변경하여 주소연산시간 단축

-- ver 0.1.4 --
 * 속도향상(이미지 url주소 추출하는 while, for문 인덱스 값 수정하여 불필요한 replace() 함수 사용 배제 및 반복횟수 감소)
 * ImageIO()함수 내에서 확장자 부분을 replace()함수 대신 substring() 사용해 시간 단축
 * 사용 변수들 클래스 앞부분에 한번에 선언


-- ver 0.1.3.1 --
 * 폴더명 생성규칙에 위배되는 특수문자 제거방식을 정규식 이용 코드로 대체해 폭넓게 대처 가능

-- ver 0.1.3 --
 * Scanner 없애고 BufferedReader로 통일시켜서 메모리 절약
 * 만화 제목에 ? 등이 포함시 폴더 생성에서 에러가 나는 문제 해결
 * 불필요한 변수사용 제거 및 가독성을 위한 변수명 변경
 * 저장시 본래의 확장자가 아니라 jpg로 강제 저장되는 문제 해결

-- ver 0.1.2 --
 * 다운로드 주소가 archives의 주소가 아닌 업데이트 알림 페이지 등에서 받아온 주소인 경우도 다운로드 가능하게 수정


-- ver 0.1.1 --
 * 다운로드 시작 시 저장 경로 출력


-- ver 0.1.0 --
 * yuncomics, shencomics, blogspot등 다양한 업로드 사이트 다운로드 가능
 * 확장자 .jpg, .jpeg, .png, .gif, .bmp 지원
 * 기본 다운로드 위치 c:/Comics로 변경
 * http response code 결과를 통한 접속 상태 출력


-- ver 0.0.0 --
 * yuncomics업데이트 이후 파일들만 다운로드 가능
 * 저장위치 : d:/JAVATEST/comics
 * 확장자 .jpg 지원
