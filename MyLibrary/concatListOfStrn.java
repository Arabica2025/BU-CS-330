package MyLibrary;

import MyLibrary.FnStrn.*;
import MyLibrary.FnList.*;

public class concatListOfStrn {
	private static int listLength = 0;
	private static int iterate = 0;

	// change: forgot to add constructor
	FnList<FnStrn> xs;
	public concatListOfStrn(FnList<FnStrn> xs) {
		this.xs = xs;
	}

    static FnStrn
	FnList$FnStrn_concate(FnList<FnStrn> xs) {
	// Given a list of strings, this method return the
	// concatenation of these string. For instance, given
	// ("a", "bc", "def"), the returned string is "abcdef"
	// You implementation is NOT allowed to use loops or
	// reccursion. Try to use the 'foritm' method in FnList
	// and FnStrn to accomplish this task.
	
	// lambda function can only capture local variables (within the method) that are final (or not changing what's inside)
	// we can avoid this problem by declaring private fields outside the method
	// update the length of the list
		FnListSUtil.foritm(xs, str -> listLength += FnStrnSUtil.length(str));

		// create new output array with the length of the updated version
		// data type: char[] because that is what FnStrn takes as its argument
		char[] outputarr = new char[listLength];

		// first foritm(FnListSUtil): input is xs which is FnList object; spit out each FnStrn in the list
			// because xs is FnList<FnStrn>, str is a FnStrn object
		// second foritm(FnStrnSUtil): input is str which is FnStrn object; and the data type inside FnStrn is char[] so we can iteration through char[]
			// in str, for each character, we assign each character to outputarray at index iterate
			// iterate++: we assign the char from index 0 to the end
		FnListSUtil.foritm(xs, str -> 
			FnStrnSUtil.foritm(str, c -> outputarr[iterate++] = c));

			// return value is FnStrn
			// FnStrn takes char[] as argument; in this case, outputarr
		return new FnStrn(outputarr);
		
    } 
}
