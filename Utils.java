package �ʷ�����;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import �ʷ�����.NFA.NFANode;

public class Utils {

	//ע�⣺.*|()\��Щ��Ҫת����ַ������ܼ���alphetbet��Ҫ������ת����ֵ���룡��
	//��Ϊ����Ҫ�����Ǳ����׺ʽ���������Ǳ������ǡ��ַ��������������Ǻ��ַ�֮�����'.'
	//���磺��a*������׺ʽ���ǡ�a*�������ǲ�����a��*֮�����'.'����a.*���Ǵ���ġ�
	//����a\\*������׺ʽΪ��a.����
	public static String alphetbet = new String(" !\"#$%&'+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{}~?");
	
	/**
	 * ����NFA��һ��״̬�ıհ��������һ�����ϣ������һ�����Ա��з��ء�
	 */
	public static ArrayList<NFANode> eClosure(NFANode n)
	{
		Queue<NFANode> que = new LinkedList<>();
		ArrayList<NFANode> dstates = new ArrayList<>();
		dstates.add(n);
		que.offer(n);
		while(!que.isEmpty())
		{
			NFANode node = que.poll();
			for(int i=0;i<node.edge.size();++i)
			{
				if(node.edge.get(i).equals('\0'))
				{
					//�жϸ�Ԫ����û�б����������û�г�����dstates�У�˵����Ԫ���ǵ�һ�α����ʡ�����
					//Ҫ������У���Ȼ��������У����ⷢ��ѭ������(a*)*�������
					if(!addIntoIfNotHaveTheSameElement(dstates,node.desNode.get(i)))
						que.offer(node.desNode.get(i));
				}
			}
		}
		return dstates;
	}
	
	//���ظ�Ԫ�ط���true��û���򷵻�false��
	/**
	 * ����հ��������ظ���Ԫ�ء���������Ҫ�Ľ����һ�����ϣ�����3Ҫ�أ�ȷ���ԣ������ԣ������ԣ�
	 * ����������Ҫ��һ���ж�ĳ��Ԫ���Ƿ��ڼ����еķ�����
	 */
	public static boolean addIntoIfNotHaveTheSameElement(ArrayList<NFANode> result, NFANode r)
	{
		boolean flag = false;
		for(NFANode node:result)
		{
			if(node.state.equals(r.state))
				flag = true;
		}
		if(!flag)
		{
			result.add(r);
		}
		return flag;
	}
	
	/**
	 * ����һ������������״̬�ıհ�
	 */
	public static ArrayList<NFANode> eClosure(ArrayList<NFANode> T)
	{
		ArrayList<NFANode> result = new ArrayList<>();
		for(NFANode node:T)
		{
			//��T�е�ÿһ���ڵ㶼ʹ��eClosure(NFANode n)����
			ArrayList<NFANode> r = eClosure(node);
			//Ȼ�󽫽ڵ㲻�ظ��ؼ���result
			for(NFANode n:r)
				addIntoIfNotHaveTheSameElement(result,n);
		}
		return result;
	}
	
	/**
	 * ���ݡ��Ӽ����취����д�ķ���������һ�������ڵ�״̬����ĳ���ַ���Ľ����
	 */
	public static ArrayList<NFANode> move(ArrayList<NFANode> T, Character s)
	{
		ArrayList<NFANode> dstates = new ArrayList<>();
		
		for(NFANode node:T)
			for(int i=0;i<node.edge.size();++i)
				if(node.edge.get(i).equals(s))
					dstates.add(node.desNode.get(i));
		
		return dstates;
	}
	
	/**
	 * �ж�ĳһ״̬���Ƿ񡰳��֡��������ǵ�һ�γ��֣��򷵻�false��
	 */
	public static boolean inResult(ArrayList<ArrayList<NFANode>> result, ArrayList<NFANode> T)
	{
		for(ArrayList<NFANode> elem:result)
		{
			if(elem.size()==T.size())
			{
				Set<String> s1 = new TreeSet<>();
				Set<String> s2 = new TreeSet<>();
				for(NFANode n1:elem)
					s1.add(n1.state);
				for(NFANode n2:T)
					s2.add(n2.state);
				if(s1.hashCode()==s2.hashCode())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public static void print(ArrayList<NFANode> T)
	{
		for(NFANode n:T)
		{
			System.out.print(n.state + " ");
		}
		System.out.print("\t| \t");
	}
}
