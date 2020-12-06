import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode {
    HashMap<String,TrieNode> children = new HashMap< String,TrieNode>();
   	ArrayList<String> words = new ArrayList<String>();
   	
   	
    public boolean isWord() {
    	return (this.words.size()!=0);
   	}
    
}