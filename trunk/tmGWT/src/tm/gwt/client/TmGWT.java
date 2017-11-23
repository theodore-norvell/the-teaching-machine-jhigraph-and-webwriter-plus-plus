package tm.gwt.client;

import java.util.ArrayList ;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler ;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import telford.common.Kit;
import tm.gwt.display.CodeGWTDisplay;
import tm.gwt.display.ConsoleGWTDisplay;
import tm.gwt.display.DisplayAdapterGWT ;
import tm.gwt.display.ExpressionGWTDisplay;
import tm.gwt.display.GWTContext;
import tm.gwt.display.StoreGWTDisplay;
import tm.gwt.shared.state.MirrorState ;
import tm.gwt.telford.KitGWT;
import tm.gwt.test.TestController ;
import tm.portableDisplays.PortableContextInterface;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint, Observer {
    
    ArrayList<DisplayAdapterGWT> displays = new ArrayList<DisplayAdapterGWT>() ;

    TMServiceAdapter adapter ;
    
    private Scheduler.ScheduledCommand refreshCommand
    = new Scheduler.ScheduledCommand() {
        @Override public void execute() { TmGWT.this.refresh() ; } } ;
    
	@Override
    public void onModuleLoad() {
		GWT.log("Start GWT test.", null);
	
        MirrorState theState = new MirrorState() ;
		
		String url = GWT.getModuleBaseURL() + "tmService30" ;
		GWT.log(  "URL is now " + url );
		adapter = new TMServiceAdapter( url, theState ) ;
		adapter.addObserver(  this );
		adapter.ping() ;
		adapter.createEvaluator(2); //JAVA_LANG
		
		Kit.setKit(new KitGWT());
		RootPanel menu = RootPanel.get("menuBar");
		menu.add(this.createMenu());
		PortableContextInterface context = new GWTContext();

		CodeGWTDisplay codeDisplay = new CodeGWTDisplay(theState, adapter, context);
		displays.add( codeDisplay ) ;
		
		ConsoleGWTDisplay consoleDisplay = new ConsoleGWTDisplay(theState, context);
		displays.add( consoleDisplay ) ;
		
		ExpressionGWTDisplay expDisplay = new ExpressionGWTDisplay( theState, adapter, context);
        displays.add( expDisplay ) ;
		
		StoreGWTDisplay staticDisplay = new StoreGWTDisplay(theState, context, "Static");
        displays.add( staticDisplay ) ;
		
		StoreGWTDisplay heapDisplay = new StoreGWTDisplay(theState, context, "Heap");
        displays.add( heapDisplay ) ;
		
		StoreGWTDisplay stackDisplay = new StoreGWTDisplay(theState, context, "Stack");
        displays.add( stackDisplay ) ;
		
		StoreGWTDisplay scratchDisplay = new StoreGWTDisplay(theState, context, "Scratch");
        displays.add( scratchDisplay ) ;
		
		refresh() ;
	}

    @Override
    public void update(Observable o, Object arg) {
        // TODO  Is the following really needed?
        // Couldn't we just call refresh?
        Scheduler.get().scheduleDeferred( refreshCommand );
    }
	
	private void refresh() {
	    // TODO  We need to pop up some sort of alert if
	    // the attention line is not null.
	    // For now, try just an browser alert.
	    String message = adapter.getAttentionMessage() ;
	    if( message != null ) {
	        String exeptionInformation = adapter.getExceptionInformation() ;
	        if( exeptionInformation != null ) {
	            message += "\n" + exeptionInformation ;
	        }
	        Window.alert( message );
	    }
	    
	    com.google.gwt.core.client.GWT.log(">> TmGWT.refresh()" ) ;
	    for( DisplayAdapterGWT d : displays ) {
	        com.google.gwt.core.client.GWT.log("Refreshing " + d.toString() ) ;
	        d.refresh(); 
	    }
        com.google.gwt.core.client.GWT.log("<< TmGWT.refresh()" ) ;
	}
	
	public Widget createMenu() {
	    // Create a command that will execute on menu item selection
	    Command menuCommand = new Command() {
	      private int curPhrase = 0;
	      private final String[] phrases = {"Don't click me","Simple menu sample","Try another one"};

	      @Override
          public void execute() {
	        Window.alert(phrases[curPhrase]);
	        curPhrase = (curPhrase + 1) % phrases.length;
	      }
	    };

	    // Create a menu bar
	    MenuBar menu = new MenuBar();
	    menu.setAutoOpen(true);
	    menu.setWidth("325px");
	    menu.setAnimationEnabled(true);

	    // Create a sub menu of recent documents
	    MenuBar recentDocsMenu = new MenuBar(true);
	    String[] recentDocs = {"Finishing","How to","Guide"};
	    for (int i = 0; i < recentDocs.length; i++) {
	      recentDocsMenu.addItem(recentDocs[i], menuCommand);
	    }

	    // Create the file menu
	    MenuBar fileMenu = new MenuBar(true);
	    fileMenu.setAnimationEnabled(true);
	    menu.addItem(new MenuItem("File", fileMenu));
	    String[] fileOptions = {"New","Open","Close","Recent","Exit"};
	    for (int i = 0; i < fileOptions.length; i++) {
	      if (i == 3) {
	        fileMenu.addSeparator();
	        fileMenu.addItem(fileOptions[i], recentDocsMenu);
	        fileMenu.addSeparator();
	      } else {
	        fileMenu.addItem(fileOptions[i], menuCommand);
	      }
	    }
	    
	 // Create the edit menu
	    MenuBar editMenu = new MenuBar(true);
	    menu.addItem(new MenuItem("Edit", editMenu));
	    String[] editOptions = {"Undo","Redo","Cut","Copy","Paste"};
	    for (int i = 0; i < editOptions.length; i++) {
	      editMenu.addItem(editOptions[i], menuCommand);
	    }

	    // Create the GWT menu
	    MenuBar gwtMenu = new MenuBar(true);
	    menu.addItem(new MenuItem("GWT", true, gwtMenu));
	    String[] gwtOptions = {"Download","Examples","Source Code","GWT wit's the program"};
	    for (int i = 0; i < gwtOptions.length; i++) {
	      gwtMenu.addItem(gwtOptions[i], menuCommand);
	    }

	    // Create the help menu
	    MenuBar helpMenu = new MenuBar(true);
	    menu.addSeparator();
	    menu.addItem(new MenuItem("Help", helpMenu));
	    String[] helpOptions = {"Contents","Cookies","About GWT"};
	    for (int i = 0; i < helpOptions.length; i++) {
	      helpMenu.addItem(helpOptions[i], menuCommand);
	    }

	    // Return the menu
	    menu.ensureDebugId("cwMenuBar");
	    return menu;
	  }
	
	private class TestStateCommander implements StateCommander {

	    private StateCommander controller ;

        TestStateCommander( StateCommander controller ) {
	        this.controller = controller ;
	    }
        
        private Scheduler.ScheduledCommand refreshCommand
        = new Scheduler.ScheduledCommand() {
            @Override public void execute() { TmGWT.this.refresh() ; } } ;
        
        private void refresh() {
            Scheduler.get().scheduleDeferred( refreshCommand );
        }
        
        
        @Override
        public void goForward() {
            controller.goForward();
            refresh() ;
        }
        
        @Override
        public void goBack() {
            controller.goBack();
            refresh() ;
        }

        @Override
        public void intoExp() {
            controller.intoExp();
            refresh() ;   
        }

        @Override
        public void overAll() {
            controller.overAll();
            refresh() ;
        }

        @Override
        public void intoSub() {
            controller.intoSub();
            refresh() ;
        }


        @Override
        public void go(String commandString) {
            // TODO Auto-generated method stub
        }


        @Override
        public void toCursor(String fileName, int cursor) {
            // TODO Auto-generated method stub
            
        }
	    
	}
}
