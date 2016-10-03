package collegeScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.*;
public class NCES {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String query;
		String URL = "http://nces.ed.gov/collegenavigator/?q=";
		System.out.println("What is the name of the college you want information about?");
		query = toUrl(input.nextLine());
		URL += query;
		System.out.println(URL);
		
		Document site = Jsoup.connect(URL).get();
		ArrayList<String[]> results = getResults(site);
		for(int i = 0; i < results.size(); i++){
			System.out.println(i + ":\n" + getCollegeName(results.get(i)) + "\n" + getCollegeLink(results.get(i)));
		}
		input.close();
	}
	
	public static String toUrl(String query){
		String newUrl = "";
		query = query.toLowerCase();
		//
		for(int i = 0; i < query.length(); i++){
			if(query.charAt(i) == ' '){
				newUrl += " ";
			}
			else newUrl += query.charAt(i);
		}//
		return newUrl;
	}
	
	public static ArrayList<String[]> getResults(Document site){
		ArrayList<String[]> results = new ArrayList<String[]>();
		Elements colleges = site.getElementsByClass("resultsTable").select("a[href");
		for(Element college: colleges){
			if(college.hasText()){
				String[] collegeData = {college.getElementsByTag("strong").text(), college.attr("href")};
				results.add(collegeData);
			}
		}
		return results;
	}
	
	public static String getCollegeName(String[] collegeData){
		return collegeData[0];
	}
	public static String getCollegeLink(String[] collegeData){
		return "http://nces.ed.gov/collegenavigator/" + collegeData[1];
	}

}
