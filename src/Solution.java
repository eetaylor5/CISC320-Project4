import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;

public class Solution {
	static HashMap<String,String> alpha = new HashMap<String,String>();
	static HashMap<String,String> morse = new HashMap<String,String>();
	static ArrayList<String> dict = new ArrayList<String>();
	static TrieNode root = new TrieNode();
    
	
    public static void handleSpacedLetters(String morsed) {
        String[] letters = morsed.split(" ");
        String result = "";
        
        for (int i=0; i<letters.length; i++) {
        	result = result.concat(alpha.get(letters[i]));
        }
        if (dict.contains(result)) {
        	System.out.println(result);
        }
    }
    
    
    private static void recurBacktrack(String ans, String letter, int i, String morsed, ArrayList<String> words) {
    	// Add letter to ans
    	// Bounding - cutting off dead-end (not a possible letter -> return)
    	if (alpha.get(letter) != null) {
    		ans = ans.concat(alpha.get(letter));
    	} else {
    		return;
    	}
    	
    	// Branching (if not out of bounds)
    	for (int j=1; j<5; j++) {
    		if ((i+j)<=morsed.length()) {  // one character letters
    			recurBacktrack(ans, morsed.substring(i, i+j), i+j, morsed, words);
    		}
    	}
    		
    	if ((i>=morsed.length()) && dict.contains(ans)){    //no characters left
    		words.add(ans);
    	}
    	return;
	}
    
    public static void handleWord(String morsed) {
    		int i=0;
    		String ans = "";
    		ArrayList<String> words = new ArrayList<String>();
    		for (int j=1; j<5; j++) {
    			recurBacktrack(ans, morsed.substring(i, i+j), i+j, morsed, words);
    		}
    		words.sort(null);
    		for (String word  : words) {
    			System.out.println(word);
    		}
    		
    }
 
    
    // Adds morse words to a Trie
    public static void addWord(TrieNode root, String eword) {
    	String mchar = "";
    	TrieNode curr = root;
    	
    	for (int i=0; i<eword.length(); i++) { 
    		String eletter = eword.substring(i, i+1);
    		eletter = eletter.toUpperCase();
    		String mletter = morse.get(eletter);
    		
    		for (int j=0; j<mletter.length(); j++) {
    			mchar = mletter.substring(j, j+1);
    		
    			if (!(curr.children.containsKey(mchar))) {
    				curr.children.put(mchar, new TrieNode());
    			} 
    			curr = curr.children.get(mchar);
    		}
    		
    	}
    	curr.words.add(eword);
    }
    
    
    private static void recurGetCombo(ArrayList<ArrayList<String>> wordArrs, int idx, String combo, String word) {
    	ArrayList<String> curr =  wordArrs.get(idx);
    	combo = combo.concat(word);
    	if (combo != "") {combo = combo.concat(" ");}
    	for (int i=0; i<curr.size(); i++) {
			if (idx<wordArrs.size()-1) {
				recurGetCombo(wordArrs, idx+1, combo, curr.get(i));
    		} else {
    			word = curr.get(i);
    			System.out.println(combo + word);
    		}
    	}
    }
    
	public static void handleSpacedWords(String morsed) {
		ArrayList<ArrayList<String>> ewords = new ArrayList<ArrayList<String>>();
        String[] mwords = morsed.split(" ");
        
        for (String mword : mwords) {
        	TrieNode curr = root;
        	
        	for (int i=0; i<mword.length(); i++) { 
        		String mchar = mword.substring(i, i+1);
        		
        		if (curr.children.containsKey(mchar)) {
        			curr = curr.children.get(mchar);
        		}
        	}
        	if (curr.words.size()>0) {
        			ewords.add(curr.words);
        	}
        }
        recurGetCombo(ewords, 0, "", "");
     
    }

	private static int recurGetSentence(String msen, ArrayList<String> sen, ArrayList<ArrayList<String>> sentences, int len, int min) {
//		System.out.println(sen + " " + msen.length() + " " + min + " " + msen);
		for (int i=0; i<msen.length(); i++) {	//for each possible word
			TrieNode curr = root;
			String mword = msen.substring(0, i+1);
			for (int j=0; j<mword.length(); j++) {	//for each character in possible word
				String mchar = mword.substring(j, j+1);
				if (curr.children.containsKey(mchar)) {	//traverse Trie to find possible word
        			curr = curr.children.get(mchar);
        		} else {
        			//System.out.println("no path");
        			return min;
        		}
			}
			// SMALL EXTRA CREDIT: Pruning: lenth<=minimum sentence
			if (curr.isWord() && len<=min) {	//if it has words
				for (String word: curr.words) {	//for each word add word and call on the rest of the sentence
					ArrayList<String> temp = (ArrayList<String>) sen.clone();
					temp.add(word);
					min = recurGetSentence(msen.substring(i+1, msen.length()), temp, sentences, len+1, min);
					
				}
			} 
		}

		if (msen.length()==0) {
			sentences.add(sen);
			if (len<min) {
				return len;
			}
		}
		 return min;
	}	
	
	public static void handleSentence(String morsed) {
		ArrayList<ArrayList<String>> sentences = new ArrayList<ArrayList<String>>();
		int min = recurGetSentence(morsed, new ArrayList<String>(), sentences, 0, 500000);
		
		String sen = "";
		ArrayList<String> finSen = new ArrayList<String>();
		for (ArrayList<String> sentence : sentences) {
			sen = "";
			if (sentence.size()==min) {
				for (String word : sentence) {
					sen = sen.concat(word);
					sen = sen.concat(" ");
				}
				finSen.add(sen);
			}
		}
//		System.out.println(finSen);
		finSen.sort(null);
		for (String s : finSen) {
			System.out.println(s);
		}
    }


	
    public static void main(String[] args) throws FileNotFoundException {
        // Get input from stdin
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        // Parse the style and morsed code value
        String[] parts = command.split(":");
        String style = parts[0].trim();
        String morsed = parts[1].trim();
        
        
        File morsef = new File("src/morse.txt");
        Scanner morsc = new Scanner(morsef);
        // Scan and store the morse code for each character
        while(morsc.hasNext()) {
        	String letter = morsc.next();
        	String code = morsc.next();
        	alpha.put(code, letter);
        	morse.put(letter, code);
        }
        
        
        File dictionary = new File("src/dictionary.txt");
        Scanner dictsc = new Scanner(dictionary);
        // Scan and store words from the dictionary
        while(dictsc.hasNext()) {
        	String dictWord = dictsc.next();
        	dict.add(dictWord);
        	addWord(root, dictWord);
        }
        
      
        switch (style) {
            case "Spaced Letters":
                handleSpacedLetters(morsed);
                break;
            case "Word":
            	handleWord(morsed);
                break;
            case "Spaced Words":
                handleSpacedWords(morsed);
                break;
            case "Sentence":
                handleSentence(morsed);
                break;
        }
        
    }

}