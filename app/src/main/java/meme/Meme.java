package meme;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;

/*this is a model class for the api response call. will keep all data remembered for future reference within the app

 */
public class Meme implements Serializable {
    private String memeName;
    private JSONObject api;
    public Meme() throws ParseException, JSONException {
        //if instantiated empty, we are going to call the api to get everything
        GetMeme t = new GetMeme();
        //we need to parse the api string so lets do that
        api = (JSONObject) new JSONParser().parse(t.get());
        if((api.get("title")==null)){
            memeName = "Meme: "+System.currentTimeMillis();
        }else {
            memeName = (String)api.get("title");
        }
    }
    //getters and setters
    public String getTitle() throws JSONException {
        return (String) api.get("title");
    }
    public String getImageURL() throws JSONException {
        return (String) api.get("url");
    }
    public String getPostLink() throws JSONException {
        return (String) api.get("postLink");
    }
    public String getSubreddit() throws JSONException {
        return (String) api.get("subreddit");
    }
    public boolean isNFSW() throws JSONException {
        return (boolean)api.get("nsfw");
    }
    public boolean isSpoiler() throws JSONException {
        return (boolean)api.get("spoiler");
    }
    public String getAuthor() throws JSONException {
        return (String) api.get("author");
    }
    public int getUpvotes() throws JSONException {
        return (int)api.get("ups");
    }


}
