package com.AutoCompleteTrie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner; 

class TrieNode {
    char data;     
    LinkedList<TrieNode> children; 
    TrieNode parent;
    boolean isEnd;
 
    public TrieNode(char c) { //constructor of the class
    	data = c;
        children = new LinkedList<TrieNode>();
        isEnd = false;        
    }  
    
    public TrieNode getChild(char c) { // returns the child node by searching the character
        if (children != null)
            for (TrieNode eachChild : children)
                if (eachChild.data == c)
                    return eachChild;
        return null;
    }
    
    protected List<String> getWords() {  // returns the associated nodes
       List<String> list = new ArrayList<String>();   
       if (isEnd) { // if node is end of the word
    	   list.add(toString()); //add whole word to the list 
       }
       
       if (children != null) { // look its child nodes and do the same
	       for (int i=0; i< children.size(); i++) {
	          if (children.get(i) != null) {
	             list.addAll(children.get(i).getWords());
	          }
	       }
       }       
       return list; 
    }
    
	public String toString() {
		if (parent == null) {
		     return "";
		} else {
		     return parent.toString() + new String(new char[] {data});
		}
	}
}
 
class Trie {
    private TrieNode root;
 
    public Trie() {
        root = new TrieNode(' '); 
    }
 
    public void insert(String word) {
        if (search(word) == true) 
            return;    
        
        TrieNode current = root; 
        TrieNode pre ;
        for (char ch : word.toCharArray()) {
        	pre = current;
            TrieNode child = current.getChild(ch);
            if (child != null) {
                current = child;
                child.parent = pre;
            } else {
                 current.children.add(new TrieNode(ch));
                 current = current.getChild(ch);
                 current.parent = pre;
            }
        }
        current.isEnd = true;
    }
    
    public boolean search(String word) {
        TrieNode current = root;      
        for (char ch : word.toCharArray()) {
            if (current.getChild(ch) == null)
                return false;
            else {
                current = current.getChild(ch);    
            }
        }      
        if (current.isEnd == true) {       
            return true;
        }
        return false;
    }
    
    public List<String> autocomplete(String prefix) {     
       TrieNode lastNode = root;
       for (int i = 0; i< prefix.length(); i++) { // looks last node of the prefix 
	       lastNode = lastNode.getChild(prefix.charAt(i));	     
	       if (lastNode == null) 
	    	   return new ArrayList<String>();      
       }
       
       return lastNode.getWords();// get all words asociated with prefix 
    }
}    

public class App {
	 public static void main(String[] args) throws IOException{  
        Trie t = new Trie();            
            
        String textName = args[0];
       	File fileName = new File("C:\\Users\\orkun\\OneDrive\\Desktop\\" + textName);
        
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        ArrayList<String> dictionary = new ArrayList<String>();

        String line = br.readLine();
        while (line!= null){
            dictionary.add(line);
            line = br.readLine();
        }
        br.close();

        for(int i=0;i<dictionary.size();i++){
            //System.out.println(dictionary.get(i));
            t.insert(dictionary.get(i));
        }
        System.out.println("Loading. Please Wait...");
        System.out.println("Dictionary Ready.");
        int nested=0;
        while(nested < 10){ 
        System.out.println("Please enter a word with uppercase letters and press Enter:");
        Scanner scan = new Scanner(System.in);

        String input = scan.next();
		List a= t.autocomplete(input);
		
        if(a.isEmpty()){
            System.out.println("Possible word not found!");
        }
        else{
            System.out.println("Possible Words: ");
            for (int i = 0; i < a.size(); i++) {
			    System.out.println(a.get(i));
		    }
        }
		}
        
	  }
}
