# parsing-MaruParser
marumaruin parser using Java
만화 이미지 파일을 다운로드 해주는 코드입니다.
json이나 xml 파서를 이용한 파싱이 아닌 순수하게 자바에 내장된 HTTP, URL, ImageIO기능을 활용해 파싱 후 저장하는 방식입니다.

-- ver 0.1.3.1 --
폴더명 생성규칙에 위배되는 특수문자 제거방식을 정규식 이용 코드로 대체해 폭넓게 대처 가능

-- ver 0.1.3 --
Scanner 없애고 BufferedReader로 통일시켜서 메모리 절약
만화 제목에 ? 등이 포함시 폴더 생성에서 에러가 나는 문제 해결
불필요한 변수사용 제거 및 가독성을 위한 변수명 변경
저장시 본래의 확장자가 아니라 jpg로 강제 저장되는 문제 해결

-- ver 0.1.2 --
다운로드 주소가 archives의 주소가 아닌 업데이트 알림 페이지 등에서 받아온 주소인 경우도 다운로드 가능하게 수정


-- ver 0.1.1 --
다운로드 시작 시 저장 경로 출력


-- ver 0.1.0 --
yuncomics, shencomics, blogspot등 다양한 업로드 사이트 다운로드 가능
확장자 .jpg, .jpeg, .png, .gif, .bmp 지원
기본 다운로드 위치 c:/Comics로 변경
http response code 결과를 통한 접속 상태 출력


-- ver 0.0.0 --
yuncomics업데이트 이후 파일들만 다운로드 가능
저장위치 : d:/JAVATEST/comics
확장자 .jpg 지원
