public class Stack {
	class Items {
		char Tk;
		Items next;
	}	
	Items top;
	public Stack(){
		this.top=null;
	}
	public boolean isEmpty(){
		return (top==null);
	}
	public void push(char item){
		Items node=new Items();
		node.Tk=item;
		node.next=top;
		top=node;
	}
	public char peek(){
		if(isEmpty()){
			return 0;
		}else{
			Items T = top;
			return T.Tk;
		}
	}
	public char pop() {
		if(isEmpty()){
			return 0;
		}else{
			Items T = top;
			top = T.next;			
			return T.Tk;
		}
	}
	
}
