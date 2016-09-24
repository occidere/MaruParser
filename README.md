# parsing-MaruParser
marumaruin parser using Java
만화 이미지 파일을 다운로드 해주는 코드입니다.
json이나 xml 파서를 이용한 파싱이 아닌 순수하게 자바에 내장된 HTTP, URL, ImageIO기능을 활용해 파싱 후 저장하는 방식입니다.


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
