package tm.portableDisplays;

import java.util.Vector;

import telford.common.FontMetrics;
import telford.common.Graphics;
import tm.interfaces.StateInterface ;

public class ExpressionDisplayer extends PortableDisplayer {
	public ExpressionDisplayer(StateInterface model, PortableContextInterface context) {
		super(model, context);
	}

	private static final int LEFTMARGIN = 4;
	private static final int TOPMARGIN = 2;

	public String theExpression = "initial value from JAVA";

	@Override
	public void refresh() {
		if (model == null)
			theExpression = "";
		else
			theExpression = model.getExpression();
		super.refresh();
	}

	@Override
	public void paintComponent(Graphics g) {
		drawArea(g);
	}

	public void drawArea(Graphics g) {
		int advance = LEFTMARGIN;
		g.setFont(context.getDisplayFont());
		
		context.log("Displaying expression") ;
		if (theExpression.length() > 0) {
			char tempArray[] = theExpression.toCharArray();
			FontMetrics fm = g.getFontMetrics(g.getFont());
			int currWidth = 0;
			int baseline = TOPMARGIN + fm.getAscent();

			g.setColor(0x000000);// black
			int currColor = 0x000000;
			boolean currUnderline = false;
			Vector<Object> attrStack = new Vector<Object>();
			for (int i = 0, sz = theExpression.length(); i < sz; ++i) {
			    context.log("Processing character "+i) ;
				char c = tempArray[i]; // Next character
                context.log("character is"+ (int)c) ;
				switch (c) {
				case StateInterface.EXP_START_VALUE:
	                context.log("Start value") ;
					attrStack.addElement( new Integer( currColor ) );
					currColor = 0xFF0000;
					g.setColor(currColor);
					break;
				//case StateInterface.MARKER2:
				//	attrStack.addElement(new Boolean(currUnderline));
				//	currUnderline = false;
				//	break;
				case StateInterface.EXP_START_SELECTED:
                    context.log("Start selected") ;
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = true;
					break;
				case StateInterface.EXP_START_LVALUE:
                    context.log("Start lvalue") ;
                    attrStack.addElement( new Integer( currColor ) );
					currColor = 0x0000FF;
					g.setColor(currColor);
					break;
				case StateInterface.EXP_END:
                    context.log("End") ;
					Object temp = attrStack.elementAt(attrStack.size() - 1);
                    context.log("temp is" + temp.toString() ) ;
					if (temp instanceof Integer) {
						currColor = ((Integer) temp).intValue() ;
						g.setColor(currColor);
					} else {
						currUnderline = ((Boolean) temp).booleanValue();
					}
                    context.log("popping" ) ;
					attrStack.removeElementAt(attrStack.size() - 1);
                    context.log("done End" ) ;
					break;
				default:
                    context.log("Regular") ;
					g.drawString(String.valueOf(tempArray[i]), advance, baseline);
					currWidth = fm.stringWidth(String.valueOf(tempArray[i]));
					if (currUnderline) {
						g.setColor(0x000000);
						g.fillRect(advance, baseline + 2, currWidth, +3);
						g.setColor(currColor);
					}
					advance += currWidth;
				} // Switch
			} // For loop
		} // End if we have an expression
	} // End drawArea
	
    public void setState(StateInterface evaluator) {
		this.model = evaluator;
	}
	
	public StateInterface getState() {
		return model;
	}
	
	public void updateExp(String exp){
		theExpression = exp;
	}
}
