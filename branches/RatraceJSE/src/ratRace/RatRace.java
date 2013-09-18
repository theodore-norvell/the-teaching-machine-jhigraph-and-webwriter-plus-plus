package ratRace;
//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JToolBar;
//import javax.swing.SwingUtilities;

import ratRace.controller.Controller;
import ratRace.model.Model;
import ratRace.view.View;
import telford.common.*;


public class RatRace implements Observer{
	private Model model ;
	private View view ;
	private Controller controller ;
	private Display display = Kit.getKit().getDisplay() ;
	private BorderLayout borderLayout = Kit.getKit().getBorderLayout();
	private Button goButton = new Button("Go");
	private Button pauseButton = new Button("Pause") ;
	private Button restartButton = new Button("Restart") ;
		
	void rebuildMVC() {
		if( view != null ) { view.terminate(); display.getRoot().remove(view); }
		model = new Model(50, 50) ;
		model.addObserver( this ) ;
		view = new View( model ) ;
		controller = new Controller(model, view) ;
		display.getRoot().add(view);
		update(null, null) ; }
	
	public RatRace() {
					
		goButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				model.setPaused( false ) ;
			}} ) ;
		pauseButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				model.setPaused( true ) ;
			}} ) ;
		restartButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				rebuildMVC() ;
			}} ) ;
		
		Container toolBar = new Container() ;
		toolBar.add( restartButton ) ;
		toolBar.add( goButton ) ;
		toolBar.add( pauseButton ) ;
		display.add( toolBar, borderLayout.getNorth()) ;

		rebuildMVC() ;
	}

	@Override
	public void update(Observable o, Object arg) {
		goButton.setEnabled( model.getPaused() ) ;
		pauseButton.setEnabled( !model.getPaused() ) ;
		if( model.getPaused() ) {
			goButton.requestFocusInWindow() ; }
		else {
			pauseButton.requestFocusInWindow() ; } }
	
}