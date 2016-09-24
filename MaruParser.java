/**
 * 마루마루 Http 이미지 파서
 * 순수하게 자바에 내장된 HTTP, URL, ImageIO기능을 활용해 파싱 후 저장
 * yuncomics, shencomics, blogspot 등의 업로드 사이트 지원
 * 확장자 jpg, jpeg, png, gif, bmp 지원
 * Http response code 확인 기능 추가
 * 기본 다운로드 위치 c:/Comics/ 로 변경
 * 다운로드 시작 시 다운로드 경로 출력
 * 다운로드 주소가 archives의 주소가 아닌 업데이트 알림 페이지 등에서 받아온 주소인 경우도 다운로드 가능
 * Scanner 없애고 BufferedReader로 통일시켜서 메모리 절약
 * 만화 제목에 ? 등이 포함시 폴더 생성에서 에러가 나는 문제 해결
 * 불필요한 변수사용 제거 및 가독성을 위한 변수명 변경
 * 저장시 본래의 확장자가 아니라 jpg로 강제 저장되는 문제 해결
 * 폴더명 생성규칙에 위배되는 특수문자 제거방식을 정규식 이용 코드로 대체해 폭넓게 대처 가능
 * @author occidere
 */
package Parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class MaruParser {
	public static void main(String[] args) throws Exception {
		BufferedReader in;
		System.out.print("접속할 주소명을 입력하세요 : "); String address = new BufferedReader(new InputStreamReader(System.in)).readLine();
		
		//만약 입력한 주소가 archives주소가 아닌 업데이트 란에 올라온 주소 둥일 경우
		if(address.contains("marumaru")||address.contains("manga")){
			HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			//스르림 읽어옴
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String findTitle;
			while((findTitle = in.readLine())!=null){
				if(findTitle.contains("/archives/") && findTitle.contains("http")){
					findTitle = findTitle.replace("href=\"", "@").replace("\" target", "#");
					address = "";
					for(int i=0;i<findTitle.length();i++)
						if(findTitle.charAt(i)=='@'){
							while(findTitle.charAt(i)!='#')	address+=findTitle.charAt(i++);
							break;
						}
					address = address.replace("@", "");
					break;
				}
			}//주소 추출 과정 while문 종료
		}//만화 업데이트나 텍스트 부분 처럼 직접 보러 들어가지 않은 상태의 주소를 넣었을때 archives 주소 추출 if문 종료
				
		//인터넷 연결 부분
		HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		//responseCode를 통한 응답 처리
		int responseCode = conn.getResponseCode();
		if(200<=responseCode && responseCode < 300) System.out.println("접속 성공 (response code : "+responseCode+")");
		else if(300<=responseCode && responseCode < 400) System.out.println("페이지 리다이렉션 (response code : "+responseCode+")");
		else if(400<=responseCode && responseCode < 500){
			System.out.println("접속 실패. 클라이언트 오류 (response code : "+responseCode+")");
			return;
		}
		else if(500<=responseCode){
			System.out.println("접속 실패. 서버 오류 (response code : "+responseCode+")");
			return;
		}
		
		//스르림 읽어옴
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		String line, title = "", type="", path=""; //type = 확장자, path = 저장 경로
		boolean titlePrint = false; //타이틀 1번만 출력하기 위한 boolean 값
		while ((line = in.readLine()) != null) {
			//System.out.println(line);
			
			//<h1 class="entry-title">로 부터 만화 제목 받아와 저장할 폴더명으로 저장
			if(!titlePrint && line.contains("entry-title")){
				title = line.replace("<h1 class=\"entry-title\">", "").replace("</h1>", "");
				
				//만화 제목에 폴더생성규칙에 위배되는 특수문자가 포함되있을 경우 제거
				title = title.replaceAll("[^[:alnum:]+]", "");
				System.out.println("제목 : "+ title);
				
				//위에서 저장한 제목으로 폴더 생성
				path = "c:/Comics/"+title;
				new File(path).mkdirs();
				System.out.println("저장 경로 : "+ path);
				titlePrint = true;
				continue;
			}
			//만화 jpg파일의 url을 받아오는 부분 -> yuncomics 이외에도 가능(shencomics, blogspot까지 지원)
			if(line.contains("data-src") && line.contains("http://") && 
					(line.contains(".jpg") || line.contains(".jpeg")||line.contains(".png")||line.contains(".gif")||line.contains(".bmp"))) {
				
				//확장자 jpg, jpeg, png, gif, bmp 지원
				if(line.contains(".jpg")) type=".jpg";
				else if(line.contains(".jpeg")) type = ".jpeg";
				else if(line.contains(".png")) type = ".png";
				else if(line.contains(".gif")) type = ".gif";
				else if(line.contains(".bmp")) type = ".bmp";
				
				//만화 파일이 시작되는 주소인 data-src=부분을 @로 바꾸고, 끝나는 부분인 .확장자 부분을 #으로 바꾼다.
				line = line.replace("\" data-src=\"", "@").replace(type, "#");
				int i, pageNum = 0;
				String page[] = new String[100]; //만화 1화당 100페이지 넘어가는 일은 본적이 없으므로 최대 100페이지 까지만 저장(나중에 늘리면 됨)
				
				//실제 이미지 url 부분인 @~#부분을 한글자씩 받아와 스트링 배열인 page[j]에 저장
				for(i = 0;i < line.length(); i++){
					if(line.toCharArray()[i] == '@'){
						while(line.toCharArray()[i] != '#') {
							page[pageNum]+=line.toCharArray()[i++];
						}
						//이상하게 출력해보면 null@http:// ... .jpg로 나오길래 null@ 부분 없애줌
						page[pageNum] = page[pageNum].replace("null@", "")+type;
						//System.out.println(page[j]);
						
						//ImageIO를 이용해 page[j]를 파일로 저장
						HttpURLConnection getImg = (HttpURLConnection) new URL(page[pageNum]).openConnection();
						
						//이거 필수
						getImg.setRequestProperty("User-Agent", "Mozilla/5.0");
						BufferedImage img = ImageIO.read(getImg.getInputStream());
						
						//페이지 번호가 10보다 작으면 01.jpg이런식, 10이상이면 11.jpg 이런식
						File f = new File(path+"/"+(pageNum+1<10?"0"+(pageNum+1):pageNum+1)+type);
						ImageIO.write(img, type.replace(".", ""), f);
						
						System.out.println(page[pageNum++]+"......... ok!");
						//j++; //다음 페이지 저장을 위해 j를 1씩 증가
					}
				}//page[j]에 url 순차저장해주는 for문 끝
				//System.out.println(line);
			}//이미지 url만 걸러주는 if문 끝
		}//in.read()를 이용한 while문 끝
		System.out.println("done!");
		in.close();
	}
}
