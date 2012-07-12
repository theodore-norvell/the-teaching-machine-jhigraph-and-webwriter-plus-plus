package ;

import js.Lib;
import js.Dom ;

/** Tutorials
 * @author Theodore Norvell
 */

 class EdgeFunc
	{
		public var edgeId: String;
		public var type: Int;
		//funcList: List<String>;
		
		public function new()
		{
			edgeId = "";
			type = 0;
		}
	}

class Main {
	
	

	static var tmProxy : TMProxy ;
	static var vertexStack : List<TutorialVertex> = new List<TutorialVertex>() ;
	public static var edgeFunctionStack : List <EdgeFunc> = new List <EdgeFunc>();
	public static var _flag = 0;
	// The following declaration is a trick to ensure that the EdgeFunctions class
	// is linked in.
	static var neverUsed : EdgeFunctions ;

	static function main() {
	}
	
	private static function buildGraph( doc : HtmlDom ) : TutorialGraph {
		var graph = new TutorialGraph() ;
		var graphBuilt = true ;
		var graphDomNode : HtmlDom ;
		untyped{ graphDomNode = doc.getElementById("graph") ; }
		if (graphDomNode == null ) {
			trace("No element in the html file has id 'graph'") ;
			return null ; }
		var startFunctionName = graphDomNode.getAttribute("data-function") ;
		if ( startFunctionName != null ) graph.setStartFunctionName( startFunctionName ) ;
		else { trace("Graph node has no 'data-function' attribute") ;
			return null ; }
		var child = graphDomNode.firstChild ;
		while ( child != null ) {
			//trace( "Current child is  " + child ) ;
			//trace( "Node name is " + child.nodeName ) ;
			if ( child.nodeType == NodeTypes.ELEMENT_NODE && child.nodeName == "DIV" ) {
				var klass = child.getAttribute("class")  ;
				// Collect all vertices
				if ( klass == null ) {
					trace("A child of the 'graph' element has no class" ) ;
					graphBuilt = false ;  }
				else if ( klass == "vertex" ) {
					var id : String = child.getAttribute("id") ;
					if ( id == null ) {
						trace("There is a vertex with no id" ) ;
						graphBuilt = false ;}
					else if ( graph.vertices.exists( id ) ) {
						trace( "Duplicate vertex with id '" +id + "'" ) ;
						 graphBuilt = false ; }
					else {
						trace( "Building vertex " + id) ;
						var vertex = new TutorialVertex( id, child ) ;
						graph.vertices.set( id, vertex ) ; 
						if ( graph.startVertex == null ) {
							graph.setStartVertex( vertex ) ; } } }
				// Collect all edges
				else if ( klass == "edge" ) {
					var id : String = child.getAttribute("id") ;
					if ( id == null ) {
						trace("There is an edge with no id" ) ; 
						graphBuilt = false ; } 
					else if ( graph.edges.exists( id ) ) {
						trace( "Duplicate edge with id '" +id + "'" ) ;
						graphBuilt = false ; }
					else {
						trace( "Building edge " + id) ;
						var edge = new TutorialEdge( id, child ) ;
						graph.edges.set( id, edge ) ; } }
				else {
					trace("A child of the 'graph' node has a class '"
						+ klass
						+ "' that is neither 'vertex' nor 'edge'" ) ; } }
			child = child.nextSibling ;  }
			
		// Now link the vertices and edges together
		for ( edge in graph.edges ) {
			
			var edgeLabel = edge.htmlNode.getAttribute("data-label") ;
			if ( edgeLabel == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-label' attribute." ) ;
				graphBuilt = false ; }
			else {
				edge.label = edgeLabel ; }
			
			var functionName = edge.htmlNode.getAttribute("data-function") ;
			if ( functionName == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-function' attribute." ) ;
				graphBuilt = false ; }
			else { 
				edge.functionName = functionName ;
			}
				
			var sourceId = edge.htmlNode.getAttribute("data-source") ;
			if ( sourceId == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-source' attribute." ) ;
				 graphBuilt = false ; }
			else if( ! graph.vertices.exists( sourceId ) ) {
				trace("Edge '" + edge.id + "' has 'data-source' attribute of '" + sourceId + "'. But there is no such node.");
				graphBuilt = false ; }
			else {
				edge.source = graph.vertices.get( sourceId ) ;
				if( edgeLabel != null )
					graph.vertices.get( sourceId ).outGoingEdges.set( edge.id, edge ) ;  }
			
			var targetId = edge.htmlNode.getAttribute("data-target") ;
			if ( targetId == null ) {
				trace("Edge '" + edge.id + "' is missing its 'data-target' attribute." ) ;
				 graphBuilt = false ; }
			else if( ! graph.vertices.exists( targetId ) ) {
				trace("Edge '" + edge.id + "' has 'data-target' attribute of '" + targetId + "'. But there is no such node.");
				graphBuilt = false ; }
			else {
				edge.target = graph.vertices.get( targetId ) ; }
		}
		if ( graph.startVertex == null ) { graphBuilt = false ; }
		if ( graphBuilt )  return graph ; else return null ;
		
	}
	static function onLoad() {
		trace( "onLoad starts") ;
		var graph = buildGraph( Lib.document ) ;
		if ( graph == null ) { trace("Failed to build graph"); return ; }
		var applet : TMExternalCommandInterface ;
		untyped { applet = Lib.document.applets["tm_applet"]  ;  }
		if ( applet == null ) {
			trace( "Applet not found" ) ;
			return ; }
		trace( "Applet is " + applet ) ;
		tmProxy = new TMProxy( applet ) ;
		trace("At the beginning of executeFunction, startFunctionName: " + graph.startFunctionName);
		var temp : EdgeFunc = new EdgeFunc();
		temp.edgeId = graph.startFunctionName;
		edgeFunctionStack.push(temp);
		executeFunction( graph.startFunctionName ) ;
		switchToVertex( graph.startVertex ) ;
		trace( "onLoad ends" ) ;
	}
	
	static function executeFunction( name : String ) {
		trace("At the beginning of executeFunction");
		var klass = Type.resolveClass("EdgeFunctions") ;
		if ( klass == null ) {
			trace("No class 'EdgeFunctions' found.") ; }
		else {
			var fun = Reflect.field( klass, name ) ;
			if ( fun == null ) {
				trace( "Function named '" +name + "' not found." ) ; }
			else {
				Reflect.callMethod( klass, fun, [tmProxy] ) ; } }
		trace("At the end of executeFunction");
	}
	
	static function switchToVertex( vertex : TutorialVertex ) {
		
		var instructionNode = Lib.document.getElementById("instructions") ;
		while ( instructionNode.firstChild != null )
			instructionNode.removeChild( instructionNode.firstChild ) ;
		instructionNode.insertBefore( vertex.htmlNode, null ) ;
		
		var buttonsNode = Lib.document.getElementById("buttons") ;
		while ( buttonsNode.firstChild != null )
			buttonsNode.removeChild( buttonsNode.firstChild ) ;
		
			//////////////////////////////////////////////////////////////
			if (!vertexStack.isEmpty())
			{
			var undoList = edgeFunctionStack.first();
			
			var labelNode = Lib.document.createTextNode("back") ;
			var button = Lib.document.createElement("button") ;
			button.insertBefore( labelNode, null ) ;
				button.onclick = function(event : Event ) {
					if (undoList.type < 0)
					{
						trace(vertexStack.first().id + "popped from stack\n Going to switch to :" + vertexStack.first().id);
						var temp = vertexStack.pop(); 
						edgeFunctionStack.pop();
						var forward_stack: List<String> = new List<String>();
						for (edge in edgeFunctionStack)
						{
							forward_stack.push(edge.edgeId);
						}
						_flag = 1;
						for (id in forward_stack)
						{
							trace("Executing: " + id);
							executeFunction(id);
						}
						_flag = 0;
						switchToVertex(temp);
					}
					else
					{
						var count = undoList.type;
						trace("Count: " + count);
						while(count--!=0)
						{
							tmProxy.goBack();
						}
						trace(vertexStack.first().id + " popped from stack \n" +  edgeFunctionStack.first().edgeId + " popped from stack\n" +  " Going to switch to : " + vertexStack.first().id);
						edgeFunctionStack.pop();
						var temp = vertexStack.pop();
						switchToVertex(temp);
					}
				}
			buttonsNode.insertBefore( button, null ) ;
			}	
		//////////////////////////////////////////////////////////////////////////	
		
		//trace("Here");
		var flag = 0;
		for ( id in vertex.outGoingEdges.keys()  ) {
			var edge = vertex.outGoingEdges.get( id ) ;
			//trace("Edge id " + id);
			//var target = vertexStack.first() ;
			/*if (edge.label == "back")
			{
				//trace("In back, comparing : " + edge.target.id + "& stack top : " + vertexStack.first().id);
				if (edge.target.id == vertexStack.first().id)
				{
					var target = edge.target ;
					var functionName = edge.functionName ;
					var labelNode = Lib.document.createTextNode(edge.label) ;
					var button = Lib.document.createElement("button") ;
					button.insertBefore( labelNode, null ) ;
					button.onclick = function(event : Event ) {
						executeFunction( functionName ) ;
						var temp = vertexStack.pop(); 
						//trace(temp.id + "popped from stack\n Going to switch to :" + target.id);
						switchToVertex( target ) ; }
					buttonsNode.insertBefore( button, null ) ;
				}
				else
					continue;
			}
			else
			{*/
				var temp : EdgeFunc = new EdgeFunc();		
				temp.edgeId = edge.functionName;
				var target = edge.target ;
				var functionName = edge.functionName ;
				var labelNode = Lib.document.createTextNode(edge.label) ;
				var button = Lib.document.createElement("button") ;
				button.insertBefore( labelNode, null ) ;
				button.onclick = function(event : Event ) {
					trace("Pushing edge:  " + temp.edgeId + ", executing: " + functionName + ", pushing vertex: " + vertex.id);
					edgeFunctionStack.push(temp);
					executeFunction( functionName ) ;
					vertexStack.push(vertex); 
					//trace(vertex.id + "pushed  on stack\nAbout to switch to target: "+ target.id);
					switchToVertex( target ) ; }
				buttonsNode.insertBefore( button, null ) ; 
				flag++;
			}
		if (flag==0)
		{
			var labelNode = Lib.document.createTextNode("back to first") ;
			var button = Lib.document.createElement("button") ;
			button.insertBefore( labelNode, null ) ;
				button.onclick = function(event : Event ) {
					var temp = vertexStack.last();
					while (!vertexStack.isEmpty())
					{
						temp = vertexStack.pop();
						edgeFunctionStack.pop();
					}
					trace(" Executing:  " + edgeFunctionStack.first().edgeId + " and vertex: " + temp.id);
					_flag = 1;
					executeFunction(edgeFunctionStack.first().edgeId);
					_flag = 0;
					switchToVertex(temp);
				}
			buttonsNode.insertBefore( button, null ) ;
		}
	}
}
	
