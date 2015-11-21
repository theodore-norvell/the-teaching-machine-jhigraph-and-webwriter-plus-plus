//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/* Generated By:JJTree: Do not edit this line. SimpleNode.java */

package tm.javaLang.parser;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.interfaces.SourceCoords;
import tm.javaLang.analysis.Java_SpecifierSet;

/*******************************************************************************
Class: SimpleNode

Overview:
This class represents a node in the parse tree.
*******************************************************************************/

public class SimpleNode implements Node {
  protected Node parent;
  protected Node[] children;
  /** The type of node */  
  protected int id;
  protected JavaParser parser;
  protected Token first, last;
    
  private Declaration myDecl;

  // **** Added by Jon (2002 08 19) ********************************************
  private boolean myBoolean;
  private int myInt;
  private String myString;
  private ScopedName myName;
  private Java_SpecifierSet mySpecSet;
  private SourceCoords myCoords ;

  public void setBoolean(boolean toSet) { myBoolean = toSet; }
  public void setInt(int toSet) { myInt = toSet; }
  public void setString(String toSet) { myString = toSet; }
  public void setName(ScopedName toSet) { myName = toSet; }
  public void setSpecSet(Java_SpecifierSet toSet) { mySpecSet = toSet; }
  public void setCoords( SourceCoords toSet ) { myCoords = toSet ; }

  public boolean getBoolean() { return myBoolean; }
  public int getInt() { return myInt; }
  public String getString() { return myString; }
  public ScopedName getName() { return myName; }
  public Java_SpecifierSet getSpecSet() { return mySpecSet; }
  public SourceCoords getCoords() { return myCoords ; }
  
  //****************************************************************************

  public SimpleNode(int i) { id = i; }

  public SimpleNode(JavaParser p, int i) {
    this(i);
    parser = p;
  }
  
  public Declaration getDecl() { return myDecl; }
  public void setDecl(Declaration decl) { myDecl = decl; }

  public void jjtOpen() { first = parser.getToken(1);	}
  public void jjtClose() { last = parser.getToken(0);	}

  public Token getFirstToken() { return first; }
  public Token getLastToken() { return last; }
  
  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
     return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  /** Accept the visitor. **/
  public Object childrenAccept(JavaParserVisitor visitor, Object data) {
    if (children != null) {
      for (int i = 0; i < children.length; i++) {
        children[i].jjtAccept(visitor, data);
      }
    }
    return data;
  }

  public String toString() { return JavaParserTreeConstants.jjtNodeName[id]; }
  public String toString(String prefix) { return prefix + toString(); }

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + " ");
        }
      }
    }
  }
}

