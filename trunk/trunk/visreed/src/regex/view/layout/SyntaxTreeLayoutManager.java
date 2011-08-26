/**
 * SyntaxTreeLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view.layout;

import regex.view.RegexNodeView;

/**
 * @author Xiaoyu Guo
 *
 */
public class SyntaxTreeLayoutManager extends
    RegexNodeLayoutManager {    

    /**
     * Recursive routine to layout nodes. Proceeds bottom up, laying
     * out descendants before parents. Updates extent of the
     * layout as it goes. Only the NextShape of the componenttViews
     * are affected.
     * 
     * @param n the nodeview to be layed out
     * @param px the x and y co-ordinates where the
     * @param py  nodeview should be moved, px >=0, py >= 0
     * @return the width of layout
     */
    public void layoutNode(RegexNodeView n, double px, double py){
        double shapeWidth = n.getNextShapeExtent().getWidth();  // width of the shape of a single node
		double myHeight = n.getNextHeight();
		int kids = n.getNumChildren();
		
		if(n.getDislocation() != null){
    		px += n.getDislocation().getX();  // Currently dislocations are not allowed to be
    		py += n.getDislocation().getY();	// negative so px, py cannot be negative
		}
		
		double localX = px;
		double offset = shapeWidth/2.0;
		// put kids underneath stepping over from left
		for (int i = 0; i < kids; i++){
			RegexNodeView kid = n.getRegexChild(i);
			layoutNodes(kid, localX, py + 2 * myHeight);
			localX +=kid.getNextExtent().getWidth() + offset;			
		}
		if (kids > 0) {
			double shapeCenterX = n.getChild(kids/2).getNextShapeExtent().getCenterX(); // center of middle kids shape
			if(kids%2 == 0) { // average of center of two middle kids
				shapeCenterX = (shapeCenterX + n.getChild(kids/2-1).getNextShapeExtent().getCenterX())/2.0; 
			}
			double leftToCenterX = n.getNextShapeExtent().getCenterX()-n.getNextX();
			n.placeNext(shapeCenterX - leftToCenterX, py);
		} else{
			n.placeNext(px, py);
		}
	
		for (int i = 0; i < kids; i++){
			layoutBranch(n, n.getChild(i));
		}
    }
    
    private static final SyntaxTreeLayoutManager instance = new SyntaxTreeLayoutManager();

    /**
     * @return
     */
    public static RegexNodeLayoutManager getInstance() {
        return instance;
    }
}
