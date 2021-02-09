package meme;

public class ShortStorage {
    public static Meme meme;
    public ShortStorage(){}
    public static void setMeme(Meme n){
        meme=n;
    }
    public static Meme getMeme(){
        return meme;
    }
}
