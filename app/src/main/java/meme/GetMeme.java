package meme;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class GetMeme {
	URL memeURL;
	String url = "https://meme-api.herokuapp.com/gimme";
	String api = "";
	public GetMeme() {
		try {
			memeURL = new URL("https://meme-api.herokuapp.com/gimme");
		}catch(MalformedURLException e) {
			System.out.println("I'm absolutely useless to society because i cant even copy and paste right. kill me pls");
			e.printStackTrace();
			System.exit(0);
		}
	}
	public String get() throws ParseException {
		try {
			HttpURLConnection o = (HttpURLConnection) memeURL.openConnection();
			o.setRequestMethod("GET");
			o.connect();
			if(o.getResponseCode() != 200) {
				System.out.println("im a failure cos i got the response code "+o.getResponseCode()); //i had to put something
			}else {
				Scanner scan = new Scanner(memeURL.openStream());
				while(scan.hasNext()) {
					api += scan.nextLine();
				}
				scan.close();
				
				//System.out.println(api);
			}
		} catch (IOException e) {
			System.out.println("i eat ass when it comes to grabbing HTTP");
			e.printStackTrace();
		}

		return api;
	}
	public URL getMemeURL(){
		return memeURL;
	}
}
