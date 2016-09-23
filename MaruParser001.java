/**
 * 마루마루 Http 이미지 파서
 * 순수하게 자바에 내장된 HTTP, URL, ImageIO기능을 활용해 파싱 후 저장
 * yuncomics 업데이트 이후 만화들만 다운로드 가능
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
import java.util.Scanner;

public class MaruParser001 {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("접속할 주소명을 입력하세요 : "); String address = sc.next();
		//인터넷 연결 부분
		HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		System.out.printf("%s\n" ,(conn.getResponseCode()==200 ? "접속 성공" : "접속 실패"));

		//스르림 읽어옴
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line;
		String title = "";
		while ((line = in.readLine()) != null) {
			//System.out.println(line);
			//<h1 class="entry-title">로 부터 만화 제목 받아와 저장할 폴더명으로 저장
			if(line.contains("entry-title")){
				title = line.replace("<h1 class=\"entry-title\">", "").replace("</h1>", "");
				continue;
			}
			//만화 jpg파일의 url을 받아오는 부분
			if(line.contains("yuncomics") && line.contains("wp-content") && line.contains(".jpg")){
				
				//만화 파일이 시작되는 주소인 data-src=부분을 @로 바꾸고, 끝나는 부분인 .jpg부분을 #으로 바꾼다.
				line = line.replaceAll("data-src=\"", "@").replaceAll(".jpg?", "#");
				int i, j = 0, size = line.length();
				String page[] = new String[100]; //만화 1화당 100페이지 넘어가는 일은 본적이 없으므로 최대 100페이지 까지만 저장(나중에 늘리면 됨)
				//실제 이미지 url 부분인 @~#부분을 한글자씩 받아와 스트링 배열인 page[j]에 저장
				for(i=0;i<size;i++){
					if(line.toCharArray()[i] == '@'){
						while(line.toCharArray()[i] != '#'){
							page[j]+=line.toCharArray()[i++];
						}
						//이상하게 출력해보면 null@http:// ... .jpg로 나오길래 null@ 부분 없애줌
						page[j] = page[j].replaceAll("null@", "")+".jpg";
						//ImageIO를 이용해 page[j]를 파일로 저장
						HttpURLConnection getImg = (HttpURLConnection) new URL(page[j]).openConnection();
						//이거 필수
						getImg.setRequestProperty("User-Agent", "Mozilla/5.0");
						BufferedImage img = ImageIO.read(getImg.getInputStream());
						//위에서 저장한 제목으로 폴더 생성
						File d = new File("d:/JAVATEST/"+title);
						d.mkdirs();
						//페이지 번호가 10보다 작으면 01.jpg이런식, 10이상이면 11.jpg 이런식
						File f = new File("d:/JAVATEST/"+title+"/"+(j+1<10?"0"+(j+1):j+1)+".jpg");
						ImageIO.write(img, "jpg", f);
						
						System.out.println(page[j]+"......... ok!");
						j++; //다음 페이지 저장을 위해 j를 1씩 증가
					}
				}
				//System.out.println(line);
			}
		}
		sc.close();
	}
}
