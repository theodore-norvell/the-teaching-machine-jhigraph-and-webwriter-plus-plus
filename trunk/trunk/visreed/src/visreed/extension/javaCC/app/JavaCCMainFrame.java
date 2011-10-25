/**
 * JavaCCMainFrame.java
 * 
 * @date: Sep 13, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import tm.backtrack.BTTimeManager;
import visreed.app.VisreedMainFrame;
import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.extension.javaCC.swing.JavaCCProductionsTreeView;
import visreed.extension.javaCC.swing.editor.JavaCCTextArea;
import visreed.extension.javaCC.view.JavaCCViewFactory;
import visreed.model.VisreedNode;
import visreed.model.payload.AlternationPayload;
import visreed.model.payload.RepeatRangePayload;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.TerminalPayload;
import visreed.swing.SwingHelper;
import visreed.swing.VisreedJComponent;
import visreed.swing.TreeView.VisreedOutlineTreeView;
import visreed.swing.properties.PropertyEditor;
import visreed.view.SyntaxViewFactory;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCMainFrame extends VisreedMainFrame {
    public static void main(String[] args) {
        SwingHelper.setSystemLookAndFeel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JavaCCMainFrame frame = new JavaCCMainFrame();
                frame.setVisible(true);
            }
        });
    }
    
	private static final long serialVersionUID = -8133879588309521708L;

	public JavaCCMainFrame() {
		super();
	}

	/* (non-Javadoc)
	 * @see visreed.app.VisreedMainFrame#initializeControl()
	 */
	@Override
	protected void initializeControl(){
		super.initializeControl();
	}
	
	/* (non-Javadoc)
	 * @see visreed.app.VisreedMainFrame#initializeSidePanel()
	 */
	@Override
	protected void initializeSidePanel(){
		super.initializeSidePanel();
		
		// create and add properties panel
		PropertyEditor propertiesEditor = new PropertyEditor();
		propertiesEditor.setFillsViewportHeight(true);
		
		JScrollPane propertiesPanel = new JScrollPane(propertiesEditor);
		propertiesPanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Properties", propertiesPanel);
		
		// create and add the outline panel
		JPanel outlinePanel = new JPanel();
		outlinePanel.setLayout(new BorderLayout());
		
		VisreedOutlineTreeView outline = new VisreedOutlineTreeView(this.wholeGraph);
		outlinePanel.add(outline, BorderLayout.CENTER);
		outlinePanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Outline", outlinePanel);
		
		// create and add the productions panel
		JPanel productionsPanel = new JPanel();
		productionsPanel.setLayout(new BorderLayout());

		JavaCCProductionsTreeView productions = new JavaCCProductionsTreeView(this.wholeGraph);
		productionsPanel.add(productions, BorderLayout.CENTER);
		productionsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		this.sidePanelContainer.add("Productions", productionsPanel);
		
		this.sidePanelContainer.setPreferredSize(new Dimension(50, 600));
	}
	
	/* (non-Javadoc)
	 * @see visreed.app.VisreedMainFrame#fillTestToolBar(javax.swing.JToolBar)
	 */
	@SuppressWarnings("serial")
	@Override
	protected void fillTestToolBar(JToolBar toolBar){
        Action action = new AbstractAction("refresh") {
            public void actionPerformed(ActionEvent e) {
                refreshGraph();
            }
        };
        toolBar.add(action);
        
        action = new AbstractAction("Example Production") {
			public void actionPerformed(ActionEvent e) {
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                VisreedNode prod, seq, regspec, leaf;
                prod = wholeGraph.makeRootNode(new ProductionPayload("Production Name"));
                rootSubgraph.addTop(prod);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                prod.appendChild(seq);
                
                regspec = wholeGraph.makeRootNode(new RegexpSpecPayload("Spec Name"));
                seq.appendChild(regspec);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                regspec.appendChild(seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("TERMINAL"));
                seq.appendChild(leaf);
                
                setSubgraph(rootSubgraph);
			}
        	
        };
        toolBar.add(action);
        
        action = new AbstractAction("Production Test 2") {
			public void actionPerformed(ActionEvent e) {
                // first delete all the nodes
                List<VisreedNode> tops = rootSubgraph.getTops();
                for(int i = 0; i < tops.size(); i++){
                    VisreedNode top = tops.get(i);
                    if(top.canDelete()){
                        top.delete();
                    }
                }
                
                VisreedNode prod, alt, branch, seq, regspec, leaf;
                prod = wholeGraph.makeRootNode(new ProductionPayload("Production Name"));
                rootSubgraph.addTop(prod);
                
                branch = wholeGraph.makeRootNode(new SequencePayload());
                prod.appendChild(branch);
                
                alt = wholeGraph.makeRootNode(new AlternationPayload());
                branch.appendChild(alt);
                
                // branch 1
                seq = wholeGraph.makeRootNode(new SequencePayload());
                alt.appendChild(seq);
                
                regspec = wholeGraph.makeRootNode(new RegexpSpecPayload("Spec Name"));
                seq.appendChild(regspec);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                regspec.appendChild(seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("TERMINAL"));
                seq.appendChild(leaf);
                
                // branch 2
                seq = wholeGraph.makeRootNode(new SequencePayload());
                alt.appendChild(seq);
                
                regspec = wholeGraph.makeRootNode(new RegexpSpecPayload("Spec 2"));
                seq.appendChild(regspec);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                regspec.appendChild(seq);
                
                leaf = wholeGraph.makeRootNode(new RepeatRangePayload(4));
                seq.appendChild(leaf);
                
                seq = wholeGraph.makeRootNode(new SequencePayload());
                leaf.appendChild(seq);
                
                leaf = wholeGraph.makeRootNode(new TerminalPayload("DIGITS"));
                seq.appendChild(leaf);
                
                setSubgraph(rootSubgraph);
			}
        	
        };
        toolBar.add(action);
	}
	

    /* (non-Javadoc)
     * @see visreed.app.VisreedMainFrame#initializeGraph()
     */
	@Override
    protected void initializeGraph(){
        this.timeMan = new BTTimeManager();
        this.wholeGraph = new JavaCCWholeGraph(this.timeMan);
        this.rootSubgraph = ((JavaCCWholeGraph)wholeGraph).getRootSubgraph();
        
        // main graph
        this.mainGraphDisplay = new VisreedJComponent();
        this.mainViewFactory = new JavaCCViewFactory(timeMan, this);
        
        // secondary graph
        this.syntaxDisplay = new VisreedJComponent();
        syntaxDisplay.setForeground(SystemColor.control);
        this.syntaxViewFactory = new SyntaxViewFactory(timeMan);
        
        // text area
        this.regexText = new JavaCCTextArea();
        this.regexText.setSubHigraph(rootSubgraph);
        this.regexText.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), 
            "refreshModelFromText"
        );
        this.regexText.getActionMap().put(
            "refreshModelFromText", 
            new AbstractAction(){
                private static final long serialVersionUID = -3922739423828722390L;

                public void actionPerformed(ActionEvent e){
                    if(regexText.tryParseText() == true){
                        regexText.refreshFromText();
                        setSubgraph(rootSubgraph);
                        refreshGraph();
                    }
                }
            }
        );
        
        this.setSubgraph(rootSubgraph);
        
        // register this as the observer
        this.wholeGraph.registerObserver(this);
    }
}
