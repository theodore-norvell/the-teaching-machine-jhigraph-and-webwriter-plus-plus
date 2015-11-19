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

package tm.virtualMachine;

/**
 * <p>Title: Recovery</p>
 * <p>Description: A Recovery is a strategy for recovering from
 * abrupt completion. The virtual machine keeps a
 * stack of Recovery objects, each knowing what kind of abrupt
 * completion it can recover from
 * </p>
 * <p>Company: </p>
 * @author Theo
 * @version 1.0
 */

public interface Recovery {

    boolean canHandle( AbruptCompletionStatus acs ) ;

    void handle( AbruptCompletionStatus acs ) ;
}