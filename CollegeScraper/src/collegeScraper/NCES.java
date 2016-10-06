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
		getCollegeAid(site);
		input.close();
	}
	
	public static void getCollegeTuition(Document site){
		Elements finAid = site.getElementById("expenses").children();
		String title1 = finAid.get(1).getElementsByClass("tabconstraint").get(0).child(0).text();
		String title2 = finAid.get(1).getElementsByClass("tabconstraint").get(0).child(1).text();
		
		finAid = finAid.get(1).child(0).child(2).children();
		
		System.out.println(title1 + "\n" + title2 + "\n");
		
		Elements finTop = finAid.get(1).child(0).children();
		
		for(Element top: finTop){
			System.out.println(top.text());
		}
		System.out.println("\n");
		
		
		finAid = finAid.select("tbody").get(0).children();
		Elements iter_parent;
		for(Element fin: finAid){
			iter_parent = fin.children();
			for(Element iter_child:iter_parent){
				if(iter_child.hasText()){
					System.out.println(iter_child.text());
				}
			}
			System.out.println("\n");
		}
	}
	
	public static void getCollegeGeneral(Document site){
		Elements general = site.getElementsByClass("layouttab").select("tbody").get(0).children();
		Element info = site.getElementsByClass("collegedash").get(0).child(1).child(1);
		System.out.println(info.text());
		for(Element gen: general){
			System.out.println(gen.child(0).text() + gen.child(1).text());
		}
	}
	
	public static void getCollegeAid(Document site){
		Elements tableEles = site.getElementById("finaid").children();
		System.out.println(tableEles.get(1).getElementsByClass("tablenames").text());
		tableEles = tableEles.select("table").get(0).children();
		Elements iter_children;
		for(Element tableEle: tableEles.select("tbody").get(0).children()){
			//System.out.println("Parent: " + tableEle);
			iter_children = tableEle.children();
			for(Element iter_child:iter_children){
				System.out.println(iter_child.text());
			}
		}
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
