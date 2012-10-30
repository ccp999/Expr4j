/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.expr;

import org.boris.expr.engine.DependencyEngine;
import org.boris.expr.engine.Range;

public class DependencyExample
{
    public static void main(String[] args) throws Exception {
        DependencyEngine e = new DependencyEngine(new BasicEngineProvider());
        e.set("B1", "=A1*2");
        e.set("A1", "=12*2");
        e.set("C1", "=B1*A1");
        System.out.println(e.getValue(Range.valueOf("B1")));
        System.out.println(e.getValue(Range.valueOf("C1")));
        e.set("A1", "2");
        System.out.println(e.getValue(Range.valueOf("B1")));
        System.out.println(e.getValue(Range.valueOf("C1")));
    }
}