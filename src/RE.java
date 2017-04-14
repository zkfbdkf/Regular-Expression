import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class RE {
	static int[][] state = new int[20][12];
	static int[][] DFAstate = new int[20][12];

	static int stateNum = 0;
	static int nextstate = 1;
	static int Startstate = 0;
	static int Finalstate = 0;
	static int DFAstatenum = 0;
	static int nextDFAstate = 1;
	static String Fnstate = "";
	static boolean epsilon = false;
	static int fn = 0;
	static Stack Statestack = new Stack();
	static Stack Startstack = new Stack();
	static boolean Stop = false;

	public static void main(String[] args) {
		int Case = 0;
		boolean RV = true;
		char token;
		int OP = 0, OB = 0;

		for (int i = 0; i < 12; i++) {// state initialize
			for (int j = 0; j < 20; j++) {
				state[j][i] = -1;
				DFAstate[j][i] = -1;
			}
		}

		if (args.length==0) {
			System.out.println("please input regular expression");
			System.exit(1);
		}

		String RE = args[0];
		char start = RE.charAt(0);
		if (!(start == '(' || start == '[' || start == (char) 32)) {
			System.out.println("RE invalid. Please restart the program.");
			System.exit(0);
		}

		for (int i = 0; i < RE.length(); i++) {// RE valid check
			token = RE.charAt(i);
			switch (Case) {
			case 0: {// epsilon Case
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if ('0' <= token && token <= '9') {
					Case = 1;
					break;
				} else if (token == '^') {
					Case = 2;
					break;
				} else if (token == '(') {
					Case = 3;
					break;
				} else if (token == '.') {
					Case = 5;
					break;
				} else if (token == '|') {
					Case = 6;
					break;
				} else if (token == '*') {
					Case = 7;
					break;
				} else if (token == '[') {
					Case = 8;
					break;
				} else if (token == ']') {
					OB -= 1;
					Case = 9;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 1: {// digit Case
				if ('0' <= token && token <= '9') {
					Case = 1;
					break;
				} else if (token == ']') {
					OB -= 1;
					Case = 9;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 2: {// hat Case
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if ('0' <= token && token <= '9') {
					Case = 1;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 3: {// open parenthesis Case
				OP += 1;
				if (token == '(') {
					Case = 3;
					break;
				} else if (token == '[') {
					Case = 8;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 4: {// close parenthesis Case

				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if (token == ')') {
					OP -= 1;
					Case = 4;
					break;
				} else if (token == '.') {
					Case = 5;
					break;
				} else if (token == '*') {
					Case = 7;
					break;
				} else if (token == '|') {
					Case = 6;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 5: {// dot Case
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if (token == '(') {
					Case = 3;
					break;
				} else if (token == '[') {
					Case = 8;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 6: {// or Case
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if (token == '(') {
					Case = 3;
					break;
				} else if (token == '[') {
					Case = 8;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 7: {// asterisk Case
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if (token == '.') {
					Case = 5;
					break;
				} else if (token == '|') {
					Case = 6;
					break;
				} else if (token == ')') {
					OP -= 1;
					Case = 4;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 8: {// open bracket Case
				OB += 1;
				if (token == (char) 32) {// epsilon input
					Case = 0;
					break;
				} else if ('0' <= token && token <= '9') {
					Case = 1;
					break;
				} else if (token == '^') {
					Case = 2;
					break;
				} else {
					RV = false;
					break;
				}
			}
			case 9: {// close bracket Case
				if (token == '.') {
					Case = 5;
					break;
				} else if (token == '|') {
					Case = 6;
					break;
				} else if (token == ')') {
					OP -= 1;
					Case = 4;
					break;
				} else {
					RV = false;
					break;
				}
			}
			}
		}
		if (RV == true && OP == 0 && OB == 0)
			System.out.println("RE valid");
		else {
			System.out.println("RE invalid. Please restart the program.");
			System.exit(0);
		}

		System.out.println("RE: " + RE);
		String postfix = toPostfix(RE).toString();
		
		MakeNFA(postfix);
		MakeDFA();

		if (Fnstate.equals("")) {
			Fnstate += 0;
			for (int i = 0; i < 10; i++)
				DFAstate[0][i] = 0;
		}

		while (true) {
			System.out.println("");
			System.out.print("Enter String: ");
			Scanner input1 = new Scanner(System.in);
			String Numstr = input1.nextLine();
			int travel = 0;
			int inputtoken = 0;
			boolean Reject = false;

			try {
				for (int i = 0; i < Numstr.length(); i++) {
					inputtoken = ((int) Numstr.charAt(i) - 48);
					travel = DFAstate[travel][inputtoken];
				}
			} catch (Exception e) {
				Reject = true;
			}

			if (Fnstate.contains(String.valueOf(travel)))
				System.out.println("String Accepted");
			else if (Reject == true)
				System.out.println("String Rejected");
			else
				System.out.println("String Rejected");
		}
	}

	private static void MakeDFA() {
		String Newstart = "";
		String next = "";
		String comp = "";
		String temp = "";
		String ast = "";
		String[] arr = new String[2];
		int tmp = 0;

		Newstart = e_closure(Startstate);
		arr[0] = Newstart;

		while (Stop != true) {
			next = "";
			tmp = 0;
			temp = "";

			if (epsilontest(arr[0], comp) == true) {
				nextDFAstate--;
				epsilon = true;
				Stop = true;
			} else
				epsilon = false;

			for (int l = 0; l < arr.length; l++) {
				if (arr[l] != null) {
					Newstart = arr[l];
					if (ast.equals(Newstart)) {// asterisk
						nextDFAstate--;
						DFAstatenum--;
						Stop = true;
					}

					for (int i = 0; i < Newstart.length(); i++) {
						next += MakeDFAstate((int) Newstart.charAt(i) - 48);
					}
					if (l == 0 && arr[1] != null) {
						nextDFAstate--;
					}
					DFAstatenum++;
					ast = Newstart;
				}
			}
			arr = new String[2];
			Newstart = "";
			for (int i = 0; i < next.length(); i++) {
				fn = DFAstatenum;
				temp = e_closure((int) next.charAt(i) - 48);
				for (int j1 = 0; j1 < temp.length(); j1++) {
					tmp = (int) temp.charAt(j1) - 48;
					if (!Newstart.contains(String.valueOf(tmp)))
						Newstart += tmp;

					arr[i] = Newstart;
				}

			}
			comp = next;
		}
		Fnstate = noReap(Fnstate);
	}

	private static String noReap(String fnstate2) {
		String fin = "";
		TreeSet tset = new TreeSet();
		for (int i = 0; i < fnstate2.length(); i++) {
			tset.add(fnstate2.charAt(i));
		}
		Iterator itor = tset.iterator();
		while (itor.hasNext()) {
			fin += itor.next();
		}
		return fin;
	}

	private static boolean epsilontest(String newst, String next) {
		String comp = "";
		int temp = 0;

		try {
			for (int i = 0; i < newst.length(); i++) {
				int token = ((int) newst.charAt(i) - 48);
				for (int input = 0; input < 10; input++) {
					if (state[token][input] != -1) {
						temp = state[token][input];
						if (!comp.contains(String.valueOf(temp))) {
							comp += temp;
						}
					}
				}
			}
		} catch (Exception NullPointerException) {
		}

		if (next.equals(comp)) {
			return true;
		} else
			return false;
	}

	private static String e_closure(int st) {
		String closure = String.valueOf(st);
		int set = st;
		if (state[set][10] != -1 | state[set][11] != -1) {
			if (state[set][10] != -1 && state[set][11] != -1) {
				closure += e_closure(state[set][10]) + e_closure(state[set][11]);
			} else {
				closure += e_closure(state[st][10]);
			}
		}

		for (int i = 0; i < closure.length(); i++) {
			if ((int) closure.charAt(i) - 48 == Finalstate && epsilon == false) {
				Fnstate += fn;
				fn++;
			}
		}
		return closure;
	}

	private static String MakeDFAstate(int j) {
		String outputSt = "";
		int temp = 0;
		boolean test = false;
		for (int input = 0; input < 10; input++) {
			if (state[j][input] != -1) {
				test = true;
				temp = state[j][input];
				DFAstate[DFAstatenum][input] = nextDFAstate;
				if (!outputSt.contains(String.valueOf(temp))) {
					outputSt += temp;
				}
			}
		}
		if (test == true)
			nextDFAstate++;

		return outputSt;
	}

	private static void MakeNFA(String postfix) {
		char token;
		int j = 0;
		for (int i = j; i < postfix.length(); i++) {
			token = postfix.charAt(i);
			String exp = "";
			if (token == '[') {
				while (token != ']') {
					token = postfix.charAt(j++);
					if ((token <= '9' && token >= '0') | token == '^') {
						exp += token;
					}
				}
				Statestack.push((char) stateNum);
				MakeState(exp);

			} else if (token == '|') {
				OrState();
			} else if (token == '.') {
				ConState();
			} else if (token == '*') {
				AstState();
			}
		}

		Finalstate = stateNum - 1;
	}

	private static void AstState() {
		int start = (int) Statestack.peek();
		state[start + 1][10] = start;
		state[start + 1][11] = stateNum;

		if (Startstack.isEmpty()) {
			Startstate = start + 2;
			Startstack.push((char) (start + 2));
		}
		state[start - 1][11] = stateNum;

		++stateNum;
	}

	private static void ConState() {
		int start = (int) Statestack.peek() - 1;
		int end = start + 1;
		state[start][10] = end;
	}

	private static void OrState() {
		int start2 = (int) Statestack.pop();
		int start1 = 0;
		while (Statestack.isEmpty() != true) {
			start1 = (int) Statestack.pop();
		}

		int end1 = start2 - 1;
		int end2 = start2 + 1;

		state[stateNum][10] = start1;
		state[stateNum][11] = start2;

		if (Startstack.isEmpty()) {
			Startstate = stateNum++;
			Startstack.push((char) (stateNum));
		} else {
			Statestack.push((char) stateNum);
			Startstate = stateNum++;
		}

		state[end1][10] = stateNum;
		state[end2][10] = stateNum;
		stateNum++;
	}

	private static void MakeState(String exp) {
		int N;
		nextstate = stateNum + 1;
		if (exp.charAt(0) <= '9' && exp.charAt(0) >= '0') {
			for (int i = 0; i < 10; i++) {// make state
				for (int j = 0; j < exp.length(); j++) {
					N = (int) (exp.charAt(j) - 48);
					if (i == N) {
						state[stateNum][i] = nextstate;
					}
				}
			}
			stateNum++;

			for (int i = 0; i < 10; i++) {// make epsilon
				state[stateNum][i] = -1;
			}
			stateNum++;
		}

		if (exp.charAt(0) == '^') {
			for (int i = 0; i < 10; i++) {
				state[stateNum][i] = nextstate;
			}
			for (int j = 1; j < exp.length(); j++) {
				N = (int) (exp.charAt(j) - 48);
				for (int i = 0; i < 10; i++) {
					if (i == N) {
						state[stateNum][i] = -1;
					}
				}
			}
			nextstate++;
			stateNum++;

			for (int i = 0; i < 10; i++) {// make epsilon
				state[stateNum][i] = -1;
			}
			state[stateNum++][10] = ++nextstate;
		}
	}

	public static StringBuffer toPostfix(String infix) {
		StringBuffer postfix = new StringBuffer("");
		char Tk;
		int stsize = infix.length();
		Stack stack = new Stack();

		for (int i = 0; i < stsize; i++) {
			Tk = infix.charAt(i);
			switch (Tk) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '[':
			case ']':
			case '^':
				postfix.append(Tk);
				break;
			case '|':
			case '.':
				stack.push(Tk);
				break;
			case ')':
				postfix.append(stack.pop());
				break;
			case '*':
				postfix.insert(postfix.length() - 1, Tk);
			default:
			}
		}
		return postfix;
	}
}
