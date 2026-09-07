package MyLibrary;

import MyLibrary.FnList.FnList;
import MyLibrary.FnList.FnListSUtil;
import MyLibrary.FnStrn.*;

public class balance_barcket {
	// change: forgot to add constructor
	String text;
	public balance_barcket(String text){
		this.text = text;
	}
    static boolean balencedqFnList(String text) {
	//
	// There are only '(', ')', '[', ']', '{', and '}'
	// appearing in [text]. This method should return
	// true if and only if the parentheses/brackets/braces
	// in [text] are balenced.
	// Your solution must make proper use of FnList (as a stack)!
	//

		FnList<Character> appendStack = FnListSUtil.nil(); // create a stack
		int iteration = 0;

		while (iteration < text.length()){
			// set pointer
			char match = text.charAt(iteration); // head
			if (match == '[' || match == '{' || match =='('){ // append opener
				appendStack = FnListSUtil.cons(match, appendStack);
				iteration++;
			} else { // check for closer; 
				// 1. empty stack at this point -> unbalanced
				if (appendStack.nilq()){
					return false;
				}
				// FnList: pushes to the left and hd is the closer 
				// that we are pushing into the stack at this point
				char opener = appendStack.hd();
				if (!((opener == '{' && match == '}') ||
					(opener == '[' && match == ']') ||
					(opener == '(' && match == ')'))){
					return false;
				}
				// if match, delete the pair
				appendStack = appendStack.tl();
				iteration++;
			}
		}
		return appendStack.nilq(); // see if we have empty stack 
		// if empty: true
		// if not empty: false
    }

	// FnList for stack
	private	static FnList<Character> appendStack;
	
    static boolean balencedqFnStrn(String text) {
	//
	// There are only '(', ')', '[', ']', '{', and '}'
	// appearing in [text]. This method should return
	// true if and only if the parentheses/brackets/braces
	// in [text] are balenced.
	// Your solution must make proper use of FnList (as a stack)!
	//
		if (text == null){
			return false;
		}
		appendStack = FnListSUtil.nil();
		// create new FnStrn object with input text
		FnStrn raw = new FnStrn(text);
		// FnList for stack
		
		if (FnStrnSUtil.nilq(raw)){
			return true;
		}

		boolean check = FnStrnSUtil.forall(raw, ch -> checker(ch));

		return check && appendStack.nilq();
    }

	private static boolean checker(char ch){
		if (ch == '[' || ch == '{' || ch == '('){
			appendStack = FnListSUtil.cons(ch, appendStack);
			return true;
		} else {
			if (appendStack.nilq()){
				return false;
			}
			char opener = appendStack.hd();

			boolean match = (ch == ')' && opener == '(')
						|| (ch == '}' && opener == '{')
						|| (ch == ']' && opener == '[');

			if (!match){
				return false;
			}
			appendStack = appendStack.tl();
			return true;
		}
	}
}
