package �ʷ�����;

import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * ����������ʽ
 * 1.�ȴ���������ʽ��ת���ַ���escape�д��ת���,��'.'��escape�е�����Ϊ1�����'.'ת����ASCII��Ϊ1���ַ���
 * '\0'��ʾ���ַ��ıߣ�����Ϊ�˱���ת���ASCIIΪ0���ַ���escape�ĵ�һ���ַ�����0. ʵ��Ϊ��pre()
 * 2.��������ʽ�����׺ʽ����׺ʽ��addDot()��������ʽ��ʡ�Ե����ӷ���'.'��������,����������ʽ�����׺��ʽ��
 * infixToPostfix()����׺ʽ��ɺ�׺ʽ��
 * 3.���ú�׺ʽ����NFA��evaluateExpression()ʵ���˽���׺ʽ����NFA���㷨������˫ջ����
 * 4.����DFA��makeDFA()���õ�3�����ɵ�NFA����DFA��
 * 5.ƥ��,match()ģ��DFA���С�
 * @author earayu
 *
 */
public class RE {

	private String re;
	private DFA dfa;
	private String escape = "A.*|()\\";//Ϊ����ת��ĵ�һ���ַ�����Ϊ1�����ϸ�A��A��ת�⣬��������ν����
	
	public RE(String s)
	{
		re = s;
		makeDFA();
	}
	
	public String getRE()
	{
		return re;
	}

//	private boolean isCharacter(int i) {
//		char[] alp = Utils.alphetbet.toCharArray();
//		char c = re.charAt(i);
//		for(int j=0;j<alp.length;++j)
//		{
//			if(alp[j]==c)
//				return true;
//		}
//		return false;
//	}
	
	private boolean isCharacter(char c) {
		char[] alp = Utils.alphetbet.toCharArray();
		for(int j=0;j<alp.length;++j)
		{
			if(alp[j]==c)
				return true;
		}
		return false;
	}
	
	/**
	 * ��������ʽת�塣
	 * @param s
	 * @return
	 */
	private String escapeRE(String s)
	{
		for(int i=1;i<s.length();++i)
		{
			if(s.charAt(i-1)=='\\' && 
					(	s.charAt(i)=='.'|| s.charAt(i)=='|' || s.charAt(i)=='*'
					||	s.charAt(i)=='(' || s.charAt(i)==')' || s.charAt(i)=='\\'
					)	
			  )
			{
				String h = s.substring(0,i-1);
				String m = String.valueOf( (char)escape.indexOf( s.charAt(i) ) );
				String t = s.substring(i+1,s.length());
				s = h + m + t;
			}
		}
		return s;
	}
	
	private String addDot(String re) {
		StringBuffer sb = new StringBuffer();
		sb.append(re.charAt(0));
		for(int i=1; i<re.length(); i++) {
			//������������(.)
			boolean flag = isCharacter(re.charAt(i)) && (re.charAt(i-1) == '*' || re.charAt(i-1) == ')' || isCharacter(re.charAt(i-1))) 
					 || (re.charAt(i) == '(' && (isCharacter(re.charAt(i-1)) || re.charAt(i-1)==')' || re.charAt(i-1)=='*'));
			if(flag) 
				sb.append('.');
			sb.append(re.charAt(i));
		}
		return sb.toString();
	}
	
	
	private String infixToPostfix(String expression) {
		StringBuffer postfix = new StringBuffer();
		//�洢��������ջ
		Stack<Character> operatorStack = new Stack<Character>();
		// ����������������ֿ�
	    StringTokenizer tokens =
	    	new StringTokenizer(expression, "()*|.", true);
	    // �׶�1: ɨ����Ŵ�
	    while(tokens.hasMoreTokens()) {
	    	String token = tokens.nextToken();
	    	if(token.charAt(0) == '|') {
	    		// Process all * , . in the top of the operator stack
	    		while(!operatorStack.isEmpty() 
	    			&& (operatorStack.peek() == '*' || operatorStack.peek() == '.')) {
	    			postfix.append(operatorStack.pop());
	    		}
	    		// Push the | operator into the operator stack
	    		operatorStack.push(token.charAt(0));
	    		
	    	} else if(token.charAt(0) == '.') {
	    		// Process all . in the top of the operator stack
	    		while (!operatorStack.isEmpty() && operatorStack.peek().equals('.')) {
					postfix.append(operatorStack.pop());
		        }
	    		// Push the . operator into the operator stack
		        operatorStack.push(token.charAt(0));
		        
	    	} else if(token.charAt(0) == '*') {
	    		postfix.append(token.charAt(0));    		
	    	} else if(token.charAt(0) == '(') {
	    		operatorStack.push(new Character('(')); // Push '(' to stack
	    	} else if(token.charAt(0) == ')') { 
	    		// Process all the operators in the stack until seeing '('
		        while (!operatorStack.peek().equals('(')) {
					postfix.append(operatorStack.pop());
		        }
		        operatorStack.pop();
		        
	    	} else {
	    		postfix.append(token);
	    	}	    	
	    }
	    
	    // �׶� 2: process all the remaining operators in the stack
	    while(!operatorStack.isEmpty()) {
	    	postfix.append(operatorStack.pop());
	    }
	    return postfix.toString();
	}
	

	
	private NFA evaluateExpression(String postfix) {
		//����һ��������ջ���洢������		
		Stack<NFA> operandStack = new Stack<NFA>();
		//����������������
		//StringTokenizer tokens = new StringTokenizer(postfix, "*|.() ", true);
		//��������
		for(int i=0;i<postfix.length();++i) {
			//String token = tokens.nextToken().trim();
			char c = postfix.charAt(i);//���ڴ�����ַ�
			if(c == '*') {	//*������(��Ŀ�����)
				NFA nfa = operandStack.pop();
				nfa.closure();			//���бհ�����
				operandStack.push(nfa);
			} else if(c == '|'
					|| c == '.') {
				processAnOperator(operandStack, c);
			}
			else {		//������
				operandStack.push(NFA.ins(c)); //Ϊ�����ַ�����NFA����
			}
		}
		return operandStack.pop();
	}
	
	//����һ��˫Ŀ���������
	private void processAnOperator(Stack<NFA> operandStack, char c) {		
		NFA op1 = operandStack.pop();	//������1
		NFA op2 = operandStack.pop();	//������2
		if(c == '|') {			//connect����
			op2.parallel(op1);
			operandStack.push(op2);
		} else if(c == '.') {	//concatenation����
			op2.connect(op1);
			operandStack.push(op2);
		}
	}

	/*
	 * Ԥ����������ʽ��ת��,�����﷨�ǣ�δʵ�֣�,������ӷ�,ת�ɺ�׺ʽ��.
	 */
	private String pre()
	{
		return infixToPostfix(addDot(escapeRE(re)));
	}
	
	//���ݺ�׺ʽ����NFA, Ȼ������DFA
	private void makeDFA()
	{
		String preRE = pre();
		NFA nfa = this.evaluateExpression(preRE);
		this.dfa = new DFA(nfa);
	}
	
	/**
	 * Ҫƥ��ı��ʽû�бհ������Ӻͻ����㣬����������Щ�ַ�ֱ��ת�壬Ȼ��ģ��DFA����ƥ�䡣
	 */
	public boolean match(String s)
	{
		for(int i=0;i<s.length();++i)
		{
			if(s.charAt(i)=='.'|| s.charAt(i)=='|' || s.charAt(i)=='*'
					|| s.charAt(i)=='(' || s.charAt(i)==')' || s.charAt(i)=='\\'
					)
			{
				String h = s.substring(0,i);
				String m = String.valueOf((char)escape.indexOf(s.charAt(i)));
				String t = s.substring(i+1,s.length());
				s = h + m + t;
			}
		}
		return dfa.match(s);
	}
	
	public static void main(String[] args) {
		RE r = new RE("\\(a\\|b\\)\\*\\\\");
		while(true)
		{
			System.out.print("input:");
			System.out.println(r.match(new Scanner(System.in).nextLine()));
		}
	}
}
