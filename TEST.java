package �ʷ�����;

import java.util.Scanner;


public class TEST {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//(d(a|b)*c)|a	//TODO
		RE re = new RE("(a|b)(c|e)");
		
		//ƥ��������ʽ
		while(true)
		{
			System.out.println("RE :"+re.getRE());
			System.out.println(re.match(new Scanner(System.in).nextLine()));
		}

		
		
		
	}
	
	

	
	
}
