
package telford.common;

import telford.common.peers.*;

abstract public class Kit {
	
	static private Kit kit ;
		
	/* Get the current Kit */
	public static  Kit getKit() { return kit ; }
	
	public static void setKit(Kit kit) { Kit.kit = kit ; }
		
	abstract public Font getFont() ;
	
	abstract public Font getFont(String name, int style, int size) ;
		
	abstract public RootPeer makeRootPeer( String title, Root root ) ;
	
	abstract public Display getDisplay();
	
	abstract public ButtonPeer makeButtonPeer (String title, Button button);

	/** Make a peer for a container */
	abstract public ContainerPeer makeContainerPeer(Container container);
	
	abstract public CanvasPeer makeCanvasPeer(Canvas canvas);
	
	abstract public BorderLayout getBorderLayoutManager();

	public abstract Timer getTimer(int delay,boolean repeats, Root root, ActionListener actionListener) ;

	public abstract LayoutManager getFlowLayoutManager() ;

	public abstract Random getRandom();
}
