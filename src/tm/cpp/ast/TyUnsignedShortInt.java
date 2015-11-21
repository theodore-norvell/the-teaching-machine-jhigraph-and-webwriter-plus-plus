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

package tm.cpp.ast;

import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.cpp.analysis.Cpp_Specifiers;
import tm.cpp.datum.UnsignedShortDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

public class TyUnsignedShortInt extends TypeNode implements TyIntegral, TyCpp {

    private TyUnsignedShortInt() {
        super() ; }

    private static TyUnsignedShortInt [] qualified = new TyUnsignedShortInt [4];

    /**
     * Returns a <code>TyUnsignedShortInt</code> instance with default (none/auto) 
     * cv-qualification.
     * @return a <code>TyUnsignedShortInt</code> instance
     * @see Cpp.Analysis.SpecifierSet
     */
    public static TyUnsignedShortInt get () {
        return get (0);
    }

    /**
     * Returns a <code>TyUnsignedShortInt</code> instance with the indicated 
     * cv-qualification.
     * @param cv_qual a flag indicating cv-qualification as follows:
     * 0 : none (auto), 1 : const, 2 : volatile, 3 : const volatile.
     * @return a <code>TyUnsignedShortInt</code> instance
     * @see Cpp.Analysis.SpecifierSet
     */
    public static TyUnsignedShortInt get (int cv_qual) {
        TyUnsignedShortInt instance = null;
        if (cv_qual >= qualified.length) {
            Assert.apology (INVALID_CV_QUALIFICATION);
        } else { 
            if (qualified [cv_qual] == null) {
                qualified [cv_qual] = new TyUnsignedShortInt ();
                qualified [cv_qual].setAttributes (cv_qual);
            }
            instance = qualified [cv_qual];
        }
        return instance;
    }

    public TypeNode getQualified (int cv_qual) { return get (cv_qual); }

    public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
        UnsignedShortDatum d = new UnsignedShortDatum( address, parent, vms.getMemory(), name, vms.getTimeManager() ) ;
        return d ; }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace( UnsignedShortDatum.size ) ;
        UnsignedShortDatum d = new UnsignedShortDatum( address, null, vms.getMemory(), name, vms.getTimeManager() ) ;
        vms.getStore().addDatum(d) ;
        return d ; }

    public boolean equal_types( TypeInterface t ) {
        return t instanceof TyUnsignedShortInt ; }
    
    public String getTypeString () { return "unsigned short int"; }

    public int getNumBytes() { return UnsignedShortDatum.size ; }
    
    public String typeId() { return typeId( "", false ) ; }
    
    public String typeId( String seed, boolean lastWasLeft ) {
        seed = "unsigned short"+seed ;
        if( (getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0 )
            seed = "const "+seed ;
        return seed ;
    }
}
