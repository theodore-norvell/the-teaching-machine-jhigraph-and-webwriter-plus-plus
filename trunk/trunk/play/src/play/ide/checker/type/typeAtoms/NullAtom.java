package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class NullAtom extends AnyAtom {
	private static NullAtom instance = new NullAtom();
	
	public static synchronized NullAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean canonicalOver(TypeAtom t) {
		// TODO Auto-generated method stub
		if(t.equals(NullAtom.getInstance())){
			return true;
		}else
			return false;		
	}
	
	@Override
	public String toString(){
		return "Null";
	}
}
