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
		String URL = "http://nces.ed.gov/collegenavigator/?q=";
		
		System.out.println("What is the name of the college you want information about?");
		URL += toUrl(input.nextLine());
		
		Document site = Jsoup.connect(URL).get();
		ArrayList<String[]> results = getResults(site);
		
		System.out.println("Which college is it?(number)");
		for(int i = 0; i < results.size(); i++){
			System.out.println(i + ": " + getCollegeName(results.get(i)));
		}
		collegeNum = input.nextInt();
		
		site = Jsoup.connect(getCollegeLink(results.get(collegeNum))).get();
		getCollegeScores(site);
		input.close();
	}
	
	public static void getCollegeScores(Document site){
		Elements finAid = site.getElementById("expenses").children();
		String title1 = finAid.get(1).getElementsByClass("tabconstraint").get(0).child(0).text();
		String title2 = finAid.get(1).getElementsByClass("tabconstraint").get(0).child(1).text();
		
		System.out.println(title1 + "\n" + title2);
		for(Element fin:finAid){
			System.out.println("Element: " + fin);
		}
		//System.out.println(title);
	}
	
	public static String getCollegeGeneral(Document site){
		Elements general = site.getElementsByClass("layouttab").select("tbody").get(0).children();
		Element info = site.getElementsByClass("collegedash").get(0).child(1).child(1);
		System.out.println(info.text());
		for(Element gen: general){
			System.out.println(gen.child(0).text() + " " + gen.child(1).text());
		}
		
		
		return "";
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
