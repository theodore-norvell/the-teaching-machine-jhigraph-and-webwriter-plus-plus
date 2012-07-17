#include <iostream>
#include<string>
/*#TS*/
#include"PDV_final.h"
/*#/TS*/

void add1(int B[], int rows, int columns);

int main()
{
     /*#TS*/
     makeView("mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
     setTitle("mainView", "Matrices PDV");
     setDefaultNodeValueShow(true, CENTER);
     setDefaultNodeValueColor(MARKER_RED);
     setDefaultNodeShape(RECTANGLE);
	 //ScriptManager::relay("HigraphManager", "setDefaultZoneColor", BLUE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", WHITE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueShow", true, CENTER);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueColor", BLACK);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, WEST);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", MAGENTA);
	 ScriptManager::relay("HigraphManager", "setDefaultNodeShape", RECTANGLE);
	 ScriptManager::relay("HigraphManager","setDefaultNodeSize", 40, 40);
       
     /*#/TS*/
     const int rows = 2;
     const int cols = 3;
	 int val = 10;/*#TS*/ setupval(val);
	 /*#/TS*/
     
     int matrix[rows*cols];
     /*#TS*/makeArray(matrix,rows,cols,true,"A[]");/*#/TS*/
	 int i,j;/*#TS*//*nodes for i & j*/setup_i_j(i,j);/*#/TS*/
	 
	 /*#TS*/int **mat = new int*[rows];
     for(int k=0;k<rows;k++)
        mat[k]=new int[cols];
     makeMatrix(mat,rows,cols,true,"Matrix Form");
	 for(int k=0;k<rows;k++)
		for(int l=0;l<cols;l++)
			mat[k][l]=matrix[(k*cols)+l];/*#/TS*/
	 
     for(i = 0; i < rows; i++)
     {
        for(j = 0; j < cols; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], YELLOW);/*#/TS*/
				/*#TS*/	mat[i][j]=val;/*#/TS*/
                matrix[(i*cols)+j]=val;
				val++;
				/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], color_lookup(i));/*#/TS*/
			}
     }
	 
	 //Call module to add 1 to each element of matrix
	
	 add1(matrix, rows, cols);
	 
	 //See that the values of the original matrix have changed
     
     return 0;
}

void add1(int B[], int rows, int columns)
{	/*#TS*/ int name;
	int target;
	ScriptManager::relay("HigraphManager","makeNode",name); 
	ScriptManager::relay("HigraphManager","setNodeColor",name,WHITE);
	ScriptManager::relay("HigraphManager","setNodeNameLabel",name,"B[]");
	ScriptManager::relay("HigraphManager","setNodeNameShow",name,true);
	ScriptManager::relay("HigraphManager","setNodeValueShow",name,false);
	ScriptManager::relay("HigraphManager","setNodeNamePosition",name,CENTER);
	ScriptManager::relay("HigraphManager","placeNode",name,250,390);
	ScriptManager::relay("HigraphManager","makeNode",target); 
	ScriptManager::relay("HigraphManager","setNodeColor",target,WHITE);
	ScriptManager::relay("HigraphManager","setNodeNameShow",target,false);
	ScriptManager::relay("HigraphManager","setNodeValueShow",target,false);
	ScriptManager::relay("HigraphManager","placeNode",target,276,280);
	ScriptManager::relay("HigraphManager","makeEdge", name, target);
	ScriptManager::relay("HigraphManager", "setTargetDecorator", name, target, ARROWHEAD);
	ScriptManager::relay("HigraphManager", "setEdgeColor", name, target, BLACK);
	//ScriptManager::relay("HigraphManager","setStringBaseColor",name,MAGENTA);
	/*ScriptManager::relay("HigraphManager","setNodeNameLabel",B,true,"B[]");*/
	/*#/TS*/
	int i,j;
	for(i=0;i<rows;i++)
	{
		for(j=0;j<columns;j++)
		{
			/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*columns)+j), true, YELLOW);/*#/TS*/
			B[(i*columns)+j]++;
			/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*columns)+j), true, color_lookup(i));/*#/TS*/
		}
	}
	
	/*#TS*/ScriptManager::relay("HigraphManager", "deleteNode", target);	ScriptManager::relay("HigraphManager", "deleteNode", name);/*#/TS*/
}