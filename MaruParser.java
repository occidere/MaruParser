/**
 * 마루마루 Http 이미지 파서
 * HTTP, URL, ImageIO, jsoup기능을 활용해 파싱 후 저장
 * yuncomics, shencomics, blogspot 등의 업로드 사이트 지원
 * 확장자 jpg, jpeg, png, gif, bmp 지원
 * 기본 다운로드 위치 c:/Comics/로 변경
 * 다운로드 시작 시 다운로드 경로 출력
 * 다운로드 주소가 archives의 주소가 아닌 업데이트 알림 페이지 등에서 받아온 주소인 경우도 다운로드 가능
 * Scanner 없애고 BufferedReader로 통일시켜서 메모리 절약
 * 만화 제목에 ? 등이 포함시 폴더 생성에서 에러가 나는 문제 해결
 * 불필요한 변수사용 제거 및 가독성을 위한 변수명 변경
 * 저장시 본래의 확장자가 아니라 jpg로 강제 저장되는 문제 해결
 * 폴더명 생성규칙에 위배되는 특수문자 제거방식을 정규식 이용 코드로 대체해 폭넓게 대처 가능
 * 속도향상(이미지 url주소 추출하는 while, for문 인덱스 값 수정하여 불필요한 replace() 함수 사용 배제 및 반복횟수 감소)
 * ImageIO()함수 내에서 확장자 부분을 replace()함수 대신 substring() 사용해 시간 단축
 * 사용 변수들 클래스 앞부분에 한번에 선언
 * responseCode체크 부분을 responseCheck()라는 메소드로 분리
 * HttpURLConnection 부분 connection()메소드로 분리
 * address를 String 타입에서 StringBuider로 변경하여 주소연산시간 단축
 * 이미지 url을 받아오는 부분을 jsoup 이용
 * archive주소 걸러주는 checkAddress()메소드 생성
 * 전체 파일 개수 출력
 * responseCode()관련 출력, 메소드 삭제
 * 다운로드 파일 출력 형식 변경 (현재 / 전체 ... ok!)
 * 이미지 url에 한글이 있으면 exe파일에선 저장 불가(이클립스에선 가능)
 * 제목 특수문자 제거부분 수정
 * @author occidere
 */
package Parsing;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MaruParser {
	public static void main(String[] args) throws Exception {
		int pageNum = 0, total;
		String title, extension="", path; //title = 만화제목, extension = 확장자, path = 저장 경로
		StringBuilder address = new StringBuilder();
		System.out.print("접속할 주소명을 입력하세요 : "); address.append(new BufferedReader(new InputStreamReader(System.in)).readLine());
		
		// 만화 업데이트나 텍스트 부분 처럼 직접 보러 들어가지 않은 상태의 주소를 넣었을때 archives 주소 추출 if문
		if (address.toString().contains("marumaru") || address.toString().contains("manga")) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection(address.toString()), "UTF-8"));
			address.replace(0, address.length(), checkAddress(in));
		}
		
		Document doc = Jsoup.connect(address.toString()).userAgent("Mozilla/5.0").timeout(5000).get();
		
		//제목 특수문자 제거
		title = doc.select("header.entry-header").text().replaceAll("[^[:alnum:]+]", "");
		path = "c:/Comics/" + title;
		System.out.println("저장 경로 : " + path);
		new File(path).mkdirs();
		System.out.println("제목 : "+title);
		
		//정규식 이용해서 href내용 중 이미지 확장자로 끝나는 부분의 주소만 불러옴
		Elements imgUrl = doc.select("[href~=(jpe?g|gif|png|bmp)]");
		total = imgUrl.size();
		System.out.println("전체 파일 개수 : "+total+"개");

		//확장자 판단
		if (imgUrl.toString().contains("jpg")) extension = "jpg";
		else if (imgUrl.toString().contains("jpeg")) extension = "jpeg";
		else if (imgUrl.toString().contains("png")) extension = "png";
		else if (imgUrl.toString().contains("gif")) extension = "gif";
		else if (imgUrl.toString().contains("bmp")) extension = "bmp";
		
		//foreach문 이용해 이미지를 파일로 저장하는 과정
		for(Element e : imgUrl){
			address.replace(0, address.length(), e.attr("href"));
			//BufferedImage 연결
			BufferedImage imgBuf = ImageIO.read(connection(address.toString()));

			// 페이지 번호가 10보다 작으면 01.jpg이런식, 10이상이면 11.jpg 이런식
			File f = new File(path + "/" + (pageNum + 1 < 10 ? "0" + (pageNum + 1) : pageNum + 1) +"."+ extension);
			
			// ImageIO를 이용해 address를 파일로 저장
			ImageIO.write(imgBuf, extension, f);

			System.out.println((++pageNum)+" / "+total+" ... ok!");
		}
		System.out.println("done!");
	}
	// Http연결 메소드
	private static InputStream connection(String address) throws Exception{
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(address).openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		} catch (Exception e) { System.out.println("error! 잘못된 URL입니다."); }
		return conn.getInputStream();
	}
	// 만약 입력한 주소가 archives주소가 아닌 업데이트 란에 올라온 주소 둥일 경우, jsoup으로 처리하면 DOM방식으로 파싱하기에 속도가 너무 느려서 while문으로 대체
	private static String checkAddress(BufferedReader in) throws Exception{
		String findTitle;
		StringBuilder address=new StringBuilder();
		while ((findTitle = in.readLine()) != null) {
			if (findTitle.contains("/archives/") && findTitle.contains("http")) {
				findTitle = findTitle.replace("href=\"", "@").replace("\" target", "#");
				address.delete(0, address.length());
				for (int i = 0; i < findTitle.length(); i++)
					if (findTitle.charAt(i) == '@') {
						while (findTitle.charAt(i + 1) != '#')
							address.append(findTitle.charAt(++i));
						break;
					}
				break;
			}
		}
		return address.toString();
	}
}
