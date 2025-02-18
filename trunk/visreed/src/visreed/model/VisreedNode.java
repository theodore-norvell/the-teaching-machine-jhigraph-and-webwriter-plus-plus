/**
 * VisreedNode.java
 * 
 * @date: 2011-8-15
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.model;

import higraph.model.abstractTaggedClasses.AbstractTaggedNode;

import java.util.ArrayList;
import java.util.List;

import visreed.pattern.IObservable;
import visreed.pattern.IObserver;

/**
 * @author Xiaoyu Guo
 */
public class VisreedNode 
extends AbstractTaggedNode<VisreedTag, VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements ISelectable, IObservable<VisreedNode> {

    protected VisreedNode(VisreedWholeGraph higraph, VisreedPayload payload) {
        super(higraph, payload);
        payload.setNode(this);
    }

    protected VisreedNode(VisreedNode original, VisreedNode parent) {
        super(original, parent);
        this.getPayload().setNode(this);
    }

	/* (non-Javadoc)
     * @see higraph.model.abstractClasses.AbstractNode#getThis()
     */
    @Override
    protected VisreedNode getThis() { return this ; }
    
    /**
     * Appends the specified child node to the node
     * @param child
     */
    public void appendChild(VisreedNode child){
        this.insertChild(getNumberOfChildren(), child);
    }

	/**
	 * Returns whether this node is underneath another specified node
	 * @param root the root
	 * @return
	 */
	public boolean isChildOf(VisreedNode root) {
		if(root == null){
			return false;
		}
		VisreedNode current = this;
		while(current != null){
			if(current == root){
				return true;
			}
			current = current.getParent();
		}
		return false;
	}
    
    /**
     * Gets the position in its parent<br />
     * Example: 
     * <pre>
     * Parent
     * 	 |- child0
     *   |- child1
     *   -- child2
     *   
     * child1.getOrder() = 1;
     * </pre>
     * @return -1 if parent is null
     */
    public int getOrder(){
    	VisreedNode parent = this.getParent();
    	if(parent != null){
    		return parent.getChildren().indexOf(this);
    	}
    	return -1;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.abstractClasses.AbstractNode#insertChild(int, higraph.model.abstractClasses.AbstractNode)
     */
    @Override
    public void insertChild(int position, VisreedNode newChild){
    	List<VisreedNode> children = new ArrayList<VisreedNode>(1);
    	children.add(newChild);
    	NodeInsertionParameter p = this.getTag().processInsertion(this, children, position);
    	
    	VisreedNode insertionTarget = p.insertionTarget;
    	for(int i = 0; i < p.children.size(); i++){
    		insertionTarget.privateInsertChild(position + i, p.children.get(i));
    	}
    }
    
    /**
     * Inserts a list of children to specified position
     * @param position
     * @param newChildren
     */
    public void insertChild(int position, List<VisreedNode> newChildren){
    	NodeInsertionParameter p = this.getTag().processInsertion(this, newChildren, position);
    	
    	for(int i = 0; i < newChildren.size(); i++){
    		p.insertionTarget.privateInsertChild(position + i, newChildren.get(i));
    	}
    }
    
    /**
     * Just call super.insertChild()
     * @param position
     * @param child
     */
    private final void privateInsertChild(int position, VisreedNode child){
    	super.insertChild(position, child);
    }
    
    @Override
    public void replace(VisreedNode node){
    	List<VisreedNode> children = new ArrayList<VisreedNode>(1);
    	children.add(node);
    	NodeInsertionParameter p = this.getTag().processInsertion(getParent(), children, getPosition());
    	privateReplace(p.children.get(0));
    }
    
    private final void privateReplace(VisreedNode node){
    	super.replace(node);
    }
    
    public List<VisreedNode> searchForTag(final VisreedTag[] tags){
    	List<VisreedNode> result = null;
    	for(VisreedNode n : getChildren()){
    		List<VisreedNode> childResult = n.searchForTag(tags);
    		if(childResult != null){
    			if(result == null){
    				result = childResult;
    			} else {
    				result.addAll(childResult);
    			}
    		}
    	}
    	if(this.getTag().isOneOf(tags)){
    		if(result == null){
    			result = new ArrayList<VisreedNode>();
    		}
    		result.add(this);
    	}
    	return result;
    }
    
    public List<VisreedNode> searchForTag(final VisreedTag tag){
    	List<VisreedNode> result = null;
    	for(VisreedNode n : getChildren()){
    		List<VisreedNode> childResult = n.searchForTag(tag);
    		if(childResult != null){
    			if(result == null){
    				result = childResult;
    			} else {
    				result.addAll(childResult);
    			}
    		}
    	}
    	if(this.getTag().equals(tag)){
    		if(result == null){
    			result = new ArrayList<VisreedNode>();
    		}
    		result.add(this);
    	}
    	return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String desc = "";
        if(getPayload() == null || getPayload().getDescription() == null){
            desc = "null";
        } else {
            desc = getPayload().getDescription();
        }
        return "VNode_" + desc;
    }

    /* Handles selection */
    private boolean isSelected = false;

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#isSelected()
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#select()
     */
    @Override
    public void select() {
        setSelected(true);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#deSelect()
     */
    @Override
    public void deSelect() {
        setSelected(false);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#setSelected(boolean)
     */
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    
    private boolean hover;
    protected final void setHover(boolean hover) {
        this.hover = hover;
    }
    
    public void setHoverOn() {
        this.setHover(true);
    }

    public void setHoverOff() {
        this.setHover(false);
    }
    
    public boolean isHoverOn() {
        return this.hover;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.abstractClasses.AbstractNode#duplicate()
     */
    @Override
    public VisreedNode duplicate(){
    	VisreedNode result = super.duplicate();
//    	if(this.isSelected()){
//    		result.isSelected = this.isSelected;
////    		this.getWholeGraph().addToSelection(result);
//    	}
    	// result.hover = this.hover;
    	
    	return result;
    }
    
    /* (non-Javadoc)
     * @see higraph.model.abstractClasses.AbstractNode#replacePayload(higraph.model.interfaces.Payload)
     */
    @Override
    public void replacePayload(VisreedPayload payload){
    	if(! payload.equals(this.getPayload())){
        	super.replacePayload(payload);
        	this.notifyObservers();
    	}
    }
    
    private List<IObserver<VisreedNode>> observers = new ArrayList<IObserver<VisreedNode>>();

	@Override
	public void registerObserver(IObserver<VisreedNode> o) {
		if(o != null && !this.observers.contains(o)){
			this.observers.add(o);
		}
	}

	@Override
	public void deRegisterObserver(IObserver<VisreedNode> o) {
		if(this.observers.contains(o)){
			this.observers.remove(o);
		}
	}

	@Override
	public void notifyObservers() {
		for(IObserver<VisreedNode> o : this.observers){
			o.changed(this);
		}
	}
}
