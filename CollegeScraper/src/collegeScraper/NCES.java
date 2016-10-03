package collegeScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.*;
public class NCES {

	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		int collegeNum;
		String query, URL = "http://nces.ed.gov/collegenavigator/?q=";
		
		System.out.println("What is the name of the college you want information about?");
		query = toUrl(input.nextLine());
		URL += query;
		
		Document site = Jsoup.connect(URL).get();
		ArrayList<String[]> results = getResults(site);
		
		System.out.println("Which college is it?(number)");
		for(int i = 0; i < results.size(); i++){
			System.out.println(i + ": " + getCollegeName(results.get(i)));
		}
		collegeNum = input.nextInt();
		
		site = Jsoup.connect(getCollegeLink(results.get(collegeNum))).get();
		System.out.println("Title: " + site.title());
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
		Elements colleges = site.getElementsByClass("resultsTable").select("a[href]");
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
